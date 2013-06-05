/**
 * A Network class.
 * @author Rob Impey
 * @date 13-iii-03
 */

import java.io.*;
import java.awt.*;

public class Network implements Serializable
{
    /**
     * The instance fields.
     */
    private Genome genome;
    private int generation;
    private Link[] weights;
    private double learningRate, sumSqError;
    private InputNeuron[] inputNeurons;
    private HiddenNeuron[][] hiddenNeurons;
    private OutputNeuron[] outputNeurons;
  
    /**
     * Creates a new Network and sets up the weights.
     * The weights are stored in a 1D array.
     * The default range for the weights (1.0 to -1.0) is used.
     * @param _g The Genome.
     * @param _generation The generation in which this Network occurs.
     * @param _lR The learning rate
     */
    public Network(Genome _g, int _generation, double _lR)
    {
	genome = _g;
	generation = _generation;
	learningRate = _lR;

	// Create the weights
	weights = new Link[genome.getBinStringLength()];

	for (int i = 0; i < weights.length; i++)
	    {
		weights[i] = new Link(genome.getBit(i));
	    }

	// Set up the database of neurons and their connections
	int counter = 0; 
	inputNeurons = new InputNeuron[genome.getInputs()];
	hiddenNeurons = new HiddenNeuron[genome.getHiddenLayers()][genome.getNeuronsInHiddenLayer()];
	// The first hidden layer
	for (int i = 0; i < genome.getNeuronsInHiddenLayer(); i++)
	    {
		hiddenNeurons[0][i] = new HiddenNeuron(genome.getInputs(), genome.getNeuronsInHiddenLayer());
		for (int j = 0; j < genome.getInputs(); j++)
		    {		       
			hiddenNeurons[0][i].setIncomingLink(j, counter);
			inputNeurons[j] = new InputNeuron(genome.getNeuronsInHiddenLayer());
			inputNeurons[j].setLink(i, counter);
			counter++;
		    }
	    }
	
	// The remaining layers of hidden neurons
	// NB The outgoing links of the neurons in the previous layer are set up here
	for (int i = 1; i < genome.getHiddenLayers(); i++) // Loop through the layers of hidden neurons
	    {
		for (int j = 0; j < genome.getNeuronsInHiddenLayer(); j++) // Loop through the neurons in this layer
		    {
			hiddenNeurons[i][j] = new HiddenNeuron(genome.getNeuronsInHiddenLayer(), genome.getNeuronsInHiddenLayer());
			for (int k = 0; k < genome.getNeuronsInHiddenLayer(); k++) // Loop through previous layer
			    {
				hiddenNeurons[i][j].setIncomingLink(k, counter);
				hiddenNeurons[i - 1][k].setOutgoingLink(j, counter);
				counter++;
			    }
		    }
	    }

	// The outputs
	// NB The outgoing links of the neurons in the previous layer are set up here
	outputNeurons = new OutputNeuron[genome.getOutputs()];
	for (int i = 0; i < genome.getOutputs(); i++) // Loop through the output neurons 
	    {
		outputNeurons[i] = new OutputNeuron(genome.getNeuronsInHiddenLayer());
		for (int j = 0; j < genome.getNeuronsInHiddenLayer(); j++) // loop through neurons in previous layer
		    {
			outputNeurons[i].setLink(j, counter);
			hiddenNeurons[genome.getHiddenLayers() - 1][j].setOutgoingLink(i, counter);
			counter++;
		    }
	    }
    }

    /**
     * Tells you the maximum number of links there could be in the network
     * @return The maximum number of links there could be in the network
     */
    public int getNumWeights()
    {
	return weights.length;
    }

    /**
     * Returns the weight of the _ith Link.
     * @param _i The index of the weight we want
     * @return The weight of the _ith Link.
     */
    public double getWeight(int _i)
    {
	return weights[_i].getWeight();
    }

    /**
     * Sets the _ith weight in the array of Links to _w.
     * @param _i The index of the weight to be changed
     * @param _w The new weight
     */
    public void setWeight(int _i, double _w)
    {
	weights[_i].setWeight( _w);
    }

    /**
     * Says how many active weights there are in the network.
     * Used by the selection operators.
     * The hope is that the evolution will avoid simply 'filling' the networks.
     * @return The percentage of active weights in the network.
     */
    public double getPercentActWeights()
    {
	double w = 0;

	for (int i = 0; i < weights.length; i++)
	    {
		if (weights[i].isActive())
		    w++;
	    }

	w /= weights.length;

	return w;
    }

    /**
     * Adjusts the weight of the _ith Link by _w.
     * @param _i The index of the weight to change.
     * @param _w The change for weight _i.
     */
    public void adjustWeight(int _i, double _w)
    {
	weights[_i].adjustWeight(_w);
    }

    /**
     * Sets the activity of a weight
     * @param _i The index of the weight to change
     * @param _a The new activity of the _ith weight
     */
    public void setActivity(int _i, boolean _a)
    {
	weights[_i].setActivity(_a);
    }

    /**
     * Returns the learning rate of the network.
     * @return The learning rate of the network.
     */
    public double getLearningRate()
    {
	return learningRate;
    }

    /**
     * Sets the learning rate to _lR
     * @param _lR The new learning rate of the network.
     */
    public void setLearningRate(double _lR)
    {
	learningRate = _lR;
    }

    /**
     * Returns the genome of the network
     * @return The genome for the network
     */
    public Genome getGenome()
    {
	return genome;
    }

    /**
     * Returns the output of the network for a given Tuple _t.
     * The output is an array of doubles.
     * The method builds local arrays of doubles that store the values of the outputs of each neuron.
     * The hidden neurons have sigmoid activation functions.
     * The output of the sigmoid function lies in the range 0.0 to 1.0.
     * The outputs of the function we are trying to approximate lie in the range 0.0 to 8.0.
     * The output of a neuron in the final layer is the weighted sum of its inputs.
     * This sum is not put through the sigmoid function. 
     * @param _t The Tuple
     * @return The output for the Tuple _t as an array of doubles.
     */
    public double[] getOutput(Tuple _t)
    {
	double[] in = new double[genome.getInputs()]; // The output of the input neurons
	double[][] hidden = new double[genome.getHiddenLayers()][genome.getNeuronsInHiddenLayer()]; // The outputs of the hidden neurons
	double[] out = new double[genome.getOutputs()]; // The outputs of the output neurons

	double wSI = 0.0; // Used to calculate the weigted sum of the inputs to a neuron

	// Calculate the outputs of the input neurons
	for (int cIN = 0; cIN < in.length; cIN++) // cIN is current input neuron
	    {
		// in[cIN] = getSigmoid(_t.getInput(cIN));
		in[cIN] = _t.getInput(cIN);
	    }
	
	// Calculate the outputs of the first layer of hidden neurons
	for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++) // cHN is current hidden neuron
	    {
		// Calculate the weighted sum of the inputs to each neuron
		wSI = 0.0;
		
		for (int fIN = 0; fIN < in.length; fIN++) // fIN is from input neuron
		    {
			if (weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].isActive())
			    wSI += in[fIN] * weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].getWeight();
		    }

		hidden[0][cHN] = getSigmoid(wSI);
	    }
	
	// Calculate the outputs of the remaining layers of hidden neurons
	for (int cHL = 1; cHL < genome.getHiddenLayers(); cHL++) // cHL is current hidden layer
	    {
		for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++) // Loop through the neurons in a layer
		    {
			// Calculate the weighted sum of the inputs to each neuron
			wSI = 0.0;

			for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++) // fHN is from hidden neuron
			    {
				if (weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)].isActive())
				    wSI += hidden[cHL - 1][fHN] 
					* weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)].getWeight();
			    }

			hidden[cHL][cHN] = getSigmoid(wSI);
		    }
	    }

	// Calculating the values of the outputs
	for (int cON = 0; cON < out.length; cON++) // cON is current output neuron
	    {
		// Calculate the weighted sum of the inputs to each neuron
		wSI = 0.0;

		for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++) // fHN is from hidden neuron
		    {
			if (weights[outputNeurons[cON].getLink(fHN)].isActive())
			    wSI += hidden[genome.getHiddenLayers() - 1][fHN] 
				* weights[outputNeurons[cON].getLink(fHN)].getWeight();
		    }

		out[cON] = wSI;
	    }

	return out;
    }

    /** 
     * Calculates the error of the network for a given Tuple _t.
     * The error is the target output (of Tuple _t) minus the networks output.
     * @param _t The tuple whose error we want to find.
     * @return An array of doubles to represent the error.
     */
    public double[] getError(Tuple _t)
    {
	double[] o, e;

	o = this.getOutput(_t);
	e = new double[o.length];

	for (int i = 0; i < o.length; i++)
	    {
		e[i] = _t.getOutput(i) - o[i];
	    }

	return e;
    }

    /**
     * Calculates the average sum of squared errors of the network for a given array of Tuples
     * @param _v The set of Tuples
     * @return The SSE
     */
    public double getSSE(Tuple[] _v)
    {
	double sSE = 0.0;
	double[] e;

	for (int i = 0; i < _v.length; i++)
	    {
		e = this.getError(_v[i]);

		for (int j = 0; j < e.length; j++)
		    {
			sSE += e[j] * e[j];
		    }
	    }

	return sSE / _v.length;
    }

    /**
     * Finds the SSE of the network for a given array of Tuples
     * @param _v The set of Tuples 
     */
    public void cacheSSE(Tuple[] _v)
    {
	sumSqError = this.getSSE(_v);
    }

    /**
     * Returns the cached SSE
     */
    public double getCachedSSE()
    {
	return sumSqError;
    }

    /** 
     * Trains the network with _tr, an array of Tuples, for _e epochs using BP.
     * At the end of each epoch, the network is tested with an array of Tuples, _te.
     * The MSSE of this test set is recorded in an array of doubles.
     * @param _tr The training set.
     * @param _te The training set.
     * @param _e The number of epochs to train for.
     * @return a NetworkPerformance object to show how well the Network trained
     */
    public NetworkPerformance train(Tuple[] _tr, Tuple[] _te, int _e)
    {
	// Variables used to train the network
       	double[] in = new double[genome.getInputs()]; // The output of the input neurons
	double[][] hidden = new double[genome.getHiddenLayers()][genome.getNeuronsInHiddenLayer()]; // The outputs of the hidden neurons
	double[] out = new double[genome.getOutputs()]; // The outputs of the output neurons	
	double wSI = 0.0; // Used to calculate the weigted sum of the inputs to a neuron
	double wSD = 0.0; // Used to calculate the weighted sum of the delta values of the neurons in the next layer
	double[] outDeltas = new double[genome.getOutputs()]; // The delta values of the output neurons
	double[][] hiddenDeltas = new double[genome.getHiddenLayers()][genome.getNeuronsInHiddenLayer()];
	
	// Variables used to test the network
	double[] e = new double[genome.getOutputs()]; // The errors of the network for the test set
	double sSE = 0.0; // The sum of the squared errors of the network for the test set
	double[] sSEs = new double[_e]; // To store the SSEs of the network at the end of each epoch for graph

	for (int currentEpoch = 0; currentEpoch < _e; currentEpoch++) // Loop of training epochs
	    {
		///////////////////////
		// Train the network //
		///////////////////////
		for (int currentExample = 0; currentExample < _tr.length; currentExample++) // Loop for each example
		    {
			// COMPUTE THE DELTA VALUES OF THE OUTPUTS FOR THIS EXAMPLE
			// Calculate the outputs of the input neurons
			for (int cIN = 0; cIN < in.length; cIN++) // cIN is current input neuron 
			    {
				// in[cIN] = getSigmoid(_tr[currentExample].getInput(cIN));
				in[cIN] = _tr[currentExample].getInput(cIN);
			    }
			
			// Calculate the outputs of the first layer of hidden neurons
			for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++) // cHN is current hidden neuron
			    {
				// Calculate the weighted sum of the inputs to each neuron
				wSI = 0.0;
				
				for (int fIN = 0; fIN < in.length; fIN++) // fIN is from input neuron
				    {
					if (weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].isActive())
					    wSI += in[fIN] 
						* weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].getWeight();
				    }
				
				hidden[0][cHN] = getSigmoid(wSI);
			    }
			
			// Calculate the outputs of the remaining layers of hidden neurons
			for (int cHL = 1; cHL < genome.getHiddenLayers(); cHL++) // cHL is current hidden layer
			    {
				for (int cHN = 0; cHN< genome.getNeuronsInHiddenLayer(); cHN++) // cHN is current hidden neuron
				    {
					// Calculate the weighted sum of the inputs to each neuron
					wSI = 0.0;
					
					for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++) // fHN is from hidden neuron
					    {
						if (weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)].isActive())
						    wSI += hidden[cHL - 1][fHN] 
							* weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)]
							.getWeight();
					    }
					
					hidden[cHL][cHN] = getSigmoid(wSI);
				    }
			    }
			
			// Calculating the values of the outputs
			for (int cON = 0; cON < out.length; cON++) // cON is current output neuron
			    {
				// Calculate the weighted sum of the inputs to each neuron
				wSI = 0.0;
				
				for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++) // loop through left layer
				    {
					if (weights[outputNeurons[cON].getLink(fHN)].isActive())
					    wSI += hidden[genome.getHiddenLayers() - 1][fHN] 
						* weights[outputNeurons[cON].getLink(fHN)].getWeight();
				    }
				
				out[cON] = wSI;
			    }
		       	
			// Calculate output delta values
			// The output neurons have linear activation functions, y = x.
			// Therefore, dy/dx = 1
			// delta_i = error_i, p. 579 of AIMA
			for (int cON = 0; cON < genome.getOutputs(); cON++) // Loop down the output neurons
			    {
				outDeltas[cON] = _tr[currentExample].getOutput(cON) - out[cON];
			    }
			
			// UPDATE THE WEIGHTS OF THE LINKS LEADING TO THE OUTPUT LAYER
			// w_ji += lR * out_j * delta_i , see p. 579 of AIMA
			for (int cON = 0; cON < genome.getOutputs(); cON++) // Loop down the output neurons
			    {
				for(int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++) // down the last hidden layer
				    {
					if (weights[outputNeurons[cON].getLink(fHN)].isActive())
					    {
						weights[outputNeurons[cON].getLink(fHN)]
						    .adjustWeight(learningRate
								  * outDeltas[cON] 
								  * hidden[genome.getHiddenLayers() - 1][fHN]);
					    }
				    }
			    }
			
			// THE HIDDEN NEURONS UPDATE
			
			// Calculate the delta values for the neurons in the last hidden layer
			// delta_j = sigDeriv(inj) * sum_i(w_ji * delta_i), p 580, AIMA
			for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++) // Down rightmost hidden layer
			    {
				// Estimate the effect this neuron will have on neurons in following layers
				wSD = 0.0;
				
				for (int tON = 0; tON < genome.getOutputs(); tON++) // tON is to output neuron
				    {
					if (weights[hiddenNeurons[genome.getHiddenLayers() - 1][cHN]
						    .getOutgoingLink(tON)].isActive())
					    wSD += weights[hiddenNeurons[genome.getHiddenLayers() - 1][cHN]
							   .getOutgoingLink(tON)].getWeight() 
						* outDeltas[tON];
				    }
				
				// Calculate the input to this neuron
				wSI = 0.0;
				for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++)
				    {
					if (weights[hiddenNeurons[genome.getHiddenLayers() - 1][cHN]
						    .getIncomingLink(fHN)].isActive())
					    wSI += weights[hiddenNeurons[genome.getHiddenLayers() - 1][cHN]
							   .getIncomingLink(fHN)].getWeight() 
						* hidden[genome.getHiddenLayers() - 2][fHN]; 
				    }
				
				hiddenDeltas[genome.getHiddenLayers() - 1][cHN] = getSigmoidDerivative(wSI) * wSD;
			    }				         
			
			// Update the incoming weights to the last hidden layer
			// w_kj += lR * out_k * delta_j, see p. 580, AIMA
			for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++)
			    {
				wSI = 0.0; // We want to know the input to this neuron
				for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++)
				    {
					if (weights[hiddenNeurons[genome.getHiddenLayers() - 1][cHN]
						    .getIncomingLink(fHN)].isActive())
					    wSI += weights[hiddenNeurons[genome.getHiddenLayers() - 1][cHN]
							   .getIncomingLink(cHN)].getWeight() 
						* hidden[genome.getHiddenLayers() - 2][fHN]; 

				    }

				// Update the weights
				for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++)
				    {
					if (weights[hiddenNeurons[genome.getHiddenLayers() - 1][cHN]
						    .getIncomingLink(fHN)].isActive())
					    {
						weights[hiddenNeurons[genome.getHiddenLayers() - 1][cHN].getIncomingLink(fHN)]
						    .adjustWeight(learningRate
								  * hiddenDeltas[genome.getHiddenLayers() - 1][cHN]
								  * wSI);
					    }
				    }
			    }
			
			// Calculate the delta values for the middle hidden layers
			for (int cHL = genome.getHiddenLayers() - 2; cHL >= 1; cHL--) // cHL is current hidden layer
			    {
				for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++) // down hidden layer
				    {
					// Estimate the effect this neuron will have on neurons in following layers
					wSD = 0.0;
					
					for (int tHN = 0; tHN < genome.getNeuronsInHiddenLayer(); tHN++) // tHN is to hidden neuron
					    {
						if (weights[hiddenNeurons[cHL][cHN].getOutgoingLink(tHN)].isActive())
						    wSD += weights[hiddenNeurons[cHL][cHN].getOutgoingLink(tHN)]
							.getWeight() 
							* hiddenDeltas[cHL + 1][tHN];
					    }
					
					// Calculate the input to this neuron
					wSI = 0.0;
					for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++)
					    {
						if (weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)].isActive())
						    wSI += weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)]
							.getWeight() 
							* hidden[cHL - 1][fHN]; 
					    }
					
					hiddenDeltas[cHL][cHN] = getSigmoidDerivative(wSI) * wSD;
				    }
			    } 
			
			// Update the incoming weights of the middle hidden layers
			for (int cHL = genome.getHiddenLayers() - 2; cHL >= 1; cHL--) // Move leftward 
			    {
				for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++) // Down this hidden layer
				    {
					wSI = 0.0; // We want to know the input to this neuron
					for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++)
					    {
						if (weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)].isActive())
						    {
							wSI += weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)]
							    .getWeight() 
							    * hidden[cHL - 1][fHN]; 					
						    }
					    }
					
					// Update the weights
					for (int fHN = 0; fHN < genome.getNeuronsInHiddenLayer(); fHN++)
					    {
						if (weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)].isActive())
						    weights[hiddenNeurons[cHL][cHN].getIncomingLink(fHN)]
							.adjustWeight(learningRate
								      * hiddenDeltas[cHL][cHN]
								      * wSI);
					    }
				    }
			    }
			
			// Calculate the delta values for the first hidden layer
			for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++) // down first hidden layer
			    {
				// Estimate the effect this neuron will have on neurons in following layers
				wSD = 0.0;
				
				for (int tHN = 0; tHN < genome.getOutputs(); tHN++) // down next hidden layer layer
				    {
					if (weights[hiddenNeurons[0][cHN].getOutgoingLink(tHN)].isActive())
					    wSD += weights[hiddenNeurons[0][cHN].getOutgoingLink(tHN)].getWeight() 
						* hiddenDeltas[1][tHN];
				    }
				
				// Calculate the input to this neuron
				wSI = 0.0;
				for (int fIN = 0; fIN < genome.getInputs(); fIN++) // down input layer
				    {
					if (weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].isActive())
					    wSI += weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].getWeight() 
						* in[fIN]; 
				    }
				
				hiddenDeltas[0][cHN] = getSigmoidDerivative(wSI) * wSD;
			    }
			
			// Update the incoming weights of the first hidden layer 
			for (int cHN = 0; cHN < genome.getNeuronsInHiddenLayer(); cHN++) // down first hidden layer
			    {
				wSI = 0.0; // We want to know the input to this neuron
				for (int fIN = 0; fIN < genome.getInputs(); fIN++)
				    {
					if (weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].isActive())
					    wSI += weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].getWeight() 
						* hidden[0][cHN]; 
				    }
				
				for (int fIN = 0; fIN < genome.getInputs(); fIN++)
				    {
					if (weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)].isActive())
					    {
						weights[hiddenNeurons[0][cHN].getIncomingLink(fIN)]
						    .adjustWeight(learningRate
								  * hiddenDeltas[0][cHN]
								  * wSI);
					    }
				    }
			    }		    
		    } // End of example
		
		// CALCULATE THE SSE FOR THE EXAMPLES IN THE TEST SET //
		
		sSEs[currentEpoch] = this.getSSE(_te);
	    } // End of loop of epochs
	
	return new NetworkPerformance(genome, generation, _e, _tr.length, _te.length, learningRate, sSEs);
    }

    /**
     * Returns a NetworkFrame to represent the network.
     * The weights of the links of the network are shown by their colour.
     * Blue for high, red for low.
     * @param _t The title of the diagram
     * @return A diagram of the network.
     */
    public NetworkFrame getDiagram(String _t)
    {
	return new NetworkFrame(_t, genome, weights);
    }
    
    /** 
     * Returns a NetworkFrame to represent the network.
     * The NetworkFrame constructor takes the dimensions of the network and the array of links.
     * @return A diagram of the network.
     */
    public NetworkFrame getDiagram()
    {
	return new NetworkFrame(("Network from generation " + generation), genome, weights);
    }
    
    /**
     * Returns the sigmoid of a number.
     * To avoid overflow problems, the input to the function is bounded.
     * If the input is outside the range, a rounded answer is returned.
     * @param _d The input
     * @return The sigmoid of _d
     */
    public static double getSigmoid(double _d)
    {
	double out;
	
	if (_d < -709.0)
	    out = 0.0;
	else 
	    if (_d > 36.0)
		out = 1.0;
	    else
		out = 1.0 / (1.0 + Math.exp(-1.0 * _d));

	return out;
    }

    /**
     * Returns the sigmoid derivative of a number.
     * Like <code>getSigmoid</code>, the input to this function is bounded.
     * @param _d The input
     * @return The derivative of the sigmoid of _d
     */
    public static double getSigmoidDerivative(double _d)
    {
	double out;

	if (_d < -709.0)
	    out = 0.0;
	else 
	    if (_d > 745.0)
		out = 0.0;
	    else
		/*
		out = Math.exp(-1.0 * _d) 
		    / ((1 + Math.exp(-1.0 * _d)) 
		       * (1 + Math.exp(-1.0 * _d)));
		       */
		// out = getSigmoid(_d) * (1.0 - getSigmoid(_d));
		out = getSigmoid(1.0 - getSigmoid(_d));

	return out;
    }

    /**
     * For testing
     */
    public static void main(String[] args)
    {
	// Create a genome according to the first pattern in notes for 14-iii-03

	Genome g = new Genome(3, 4, 3, 1);
	// The input layer
	g.setBit(0, true);
	g.setBit(1, true);
	g.setBit(2, true);

	g.setBit(3, true);
	g.setBit(4, true);
	g.setBit(5, false);

	g.setBit(6, false);
	g.setBit(7, true);
	g.setBit(8, false);

	g.setBit(9, true);
	g.setBit(10, false);
	g.setBit(11, true);

	// The 1st hidden layer
	g.setBit(12, false);
	g.setBit(13, true);
	g.setBit(14, false);
	g.setBit(15, true);

	g.setBit(16, true);
	g.setBit(17, false);
	g.setBit(18, true);
	g.setBit(19, true);

	g.setBit(20, true);
	g.setBit(21, false);
	g.setBit(22, false);
	g.setBit(23, false);

	g.setBit(24, true);
	g.setBit(25, true);
	g.setBit(26, true);
	g.setBit(27, true);

	// The 2nd hidden layer
	g.setBit(28, true);
	g.setBit(29, false);
	g.setBit(30, false);
	g.setBit(31, true);

	g.setBit(32, true);
	g.setBit(33, false);
	g.setBit(34, true);
	g.setBit(35, true);

	g.setBit(36, false);
	g.setBit(37, true);
	g.setBit(38, false);
	g.setBit(39, false);

	g.setBit(40, false);
	g.setBit(41, false);
	g.setBit(42, true);
	g.setBit(43, false);

	// The third hidden layer
	g.setBit(44, true);
	g.setBit(45, false);
	g.setBit(46, true);
	g.setBit(47, false);

	// Create the network, setting all the weights to 0.5
	Network n = new Network(g, 0, 0.01);

	for (int i = 0; i < n.getNumWeights(); i++)
	    {
		n.setWeight(i, 0.5);
	    }

	// Create the Tuple
	double[] tupleIn = new double[3];
	double[] tupleOut = new double[1];

	tupleIn[0] = 0.25;
	tupleIn[1] = 0.5;
	tupleIn[2] = 0.75;
	tupleOut[0] = 1.0;

	Tuple t = new Tuple(tupleIn, tupleOut);

	// Calculate and print the output of the network for the Tuple
	double[] out = n.getOutput(t);

	for (int i = 0; i < out.length; i++)
	    {
		System.out.println(out[i]);
	    }

	// Calculate the errors
	double[] e1 = n.getError(t);

	for (int i = 0; i < e1.length; i++)
	    {
		System.out.println(e1[i]);
	    }

	// Draw a diagram of the network
	NetworkFrame nf1 = n.getDiagram();
	nf1.show();
	
	System.out.println();
	// Create a genome to match the second pattern from 14-iii-03
	Genome g2 = new Genome(4,6,2,3);

	// The input layer
	g2.setBit(0, true);
	g2.setBit(1, false);
	g2.setBit(2, true);
	g2.setBit(3, true);

	g2.setBit(4, false);
	g2.setBit(5, true);
	g2.setBit(6, true);
	g2.setBit(7, false);

	g2.setBit(8, false);
	g2.setBit(9, true);
	g2.setBit(10, false);
	g2.setBit(11, false);

	g2.setBit(12, false);
	g2.setBit(13, false);
	g2.setBit(14, true);
	g2.setBit(15, false);

	g2.setBit(16, true);
	g2.setBit(17, false);
	g2.setBit(18, false);
	g2.setBit(19, true);

	g2.setBit(20, true);
	g2.setBit(21, false);
	g2.setBit(22, true);
	g2.setBit(23, false);

	// First hidden
	g2.setBit(24, false); 
	g2.setBit(25, true); 
	g2.setBit(26, true); 
	g2.setBit(27, false); 
	g2.setBit(28, false); 
	g2.setBit(29, false); 

	g2.setBit(30, true); 
	g2.setBit(31, false); 
	g2.setBit(32, false); 
	g2.setBit(33, true); 
	g2.setBit(34, false); 
	g2.setBit(35, false); 

	g2.setBit(36, true); 
	g2.setBit(37, true); 
	g2.setBit(38, false); 
	g2.setBit(39, true); 
	g2.setBit(40, true); 
	g2.setBit(41, false); 

	g2.setBit(42, false); 
	g2.setBit(43, false); 
	g2.setBit(44, true); 
	g2.setBit(45, false); 
	g2.setBit(46, true); 
	g2.setBit(47, false); 

	g2.setBit(48, false); 
	g2.setBit(49, false); 
	g2.setBit(50, true); 
	g2.setBit(51, false); 
	g2.setBit(52, true); 
	g2.setBit(53, false); 

	g2.setBit(54, false); 
	g2.setBit(55, false); 
	g2.setBit(56, false); 
	g2.setBit(57, false); 
	g2.setBit(58, true); 
	g2.setBit(59, true); 

	// Second hidden

	g2.setBit(60, true); 
	g2.setBit(61, false); 
	g2.setBit(62, true); 
	g2.setBit(63, true); 
	g2.setBit(64, false); 
	g2.setBit(65, true); 

	g2.setBit(66, false); 
	g2.setBit(67, true); 
	g2.setBit(68, false); 
	g2.setBit(69, true); 
	g2.setBit(70, false); 
	g2.setBit(71, false); 

	g2.setBit(72, false); 
	g2.setBit(73, false); 
	g2.setBit(74, true); 
	g2.setBit(75, false); 
	g2.setBit(76, true); 
	g2.setBit(77, false); 

	// Create the network, setting all the weights to -0.3
	Network n2 = new Network(g2, 0, 0.01);

	for (int i = 0; i < n2.getNumWeights(); i++)
	    {
		n2.setWeight(i, -0.3);
	    }

	// Create the Tuple
	double[] tupleIn2 = new double[4];
	double[] tupleOut2 = new double[3];

	tupleIn2[0] = 0.3;
	tupleIn2[1] = 0.2;
	tupleIn2[2] = 0.5;
	tupleIn2[3] = 0.7;
	tupleOut2[0] = 0.4;
	tupleOut2[1] = 0.2;
	tupleOut2[2] = 0.8;

	Tuple t2 = new Tuple(tupleIn2, tupleOut2);

	// Calculate and print the output of the network for the Tuple
	double[] out2 = n2.getOutput(t2);

	for (int i = 0; i < out2.length; i++)
	    {
		System.out.println(out2[i]);
	    }

	// Calculate the errors
	double[] e2 = n2.getError(t2);

	for (int i = 0; i < e2.length; i++)
	    {
		System.out.println(e2[i]);
	    }

	// Draw a diagram of the network
	NetworkFrame nf2 = n2.getDiagram();
	nf2.show();

	// Create a random network with 3 inputs, 5 hidden layers of 20 and 1 output
	// Train the network and then show the graph of its improving performance
	Genome g3 = new Genome(3, 20, 3, 1);
	Network n3 = new Network(g3, 0, 0.01);	
	n3.getDiagram("Before training").show();

	DataSet dS = new SecondYearData();
	n3.train(dS.getSample(100), dS.getSample(10), 100).getGraph().show();
	n3.getDiagram("After training").show();

	// Create a fully connected network with 3 inputs, 5 hidden layers of 20 and 1 output
	// Train the network and then show the graph of its improving performance
	Genome g4 = new Genome(3, 20, 3, 1);
	for (int i = 0; i < g4.getBinStringLength(); i++)
	    {
		g4.setBit(i, true);
	    }

	Network n4 = new Network(g4, 0, 0.01);

	NetworkFrame nF4 = n4.getDiagram("Before training");
	nF4.show();

	n4.train(dS.getSample(100), dS.getSample(10), 100).getGraph().show();
	n4.getDiagram("After training").show();
    }
}
