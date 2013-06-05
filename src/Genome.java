/**
 * The Genome Class.
 * There is immutable preamble to the genome that encodes 
 * the number of inputs to the network,
 * the number of neurons in each hidden layer,
 * the number of hidden layers &
 * the number of output neurons.
 * The mutatable part of the genome is a binary string that encodes the links between the neurons.
 * @author Rob Impey
 * @date 30-i-03
 */

import java.io.*;

public class Genome implements Serializable
{
    // The instance fields
    private final int inputs, neuronsInHiddenLayer, hiddenLayers, outputs;
    private boolean[] binString;
    
    /**
     * Creates a new instance of Genome.
     * The default Genome has a random binary string.
     * @param _i the number of inputs to the network
     * @param _hN the number of neurons in each hidden layer
     * @param _h the number of hidden layers 
     * @param _o the number of output neurons
     */
    public Genome(int _i, int _hN, int _h, int _o)
    {
	inputs = _i;
	neuronsInHiddenLayer = _hN;
	hiddenLayers = _h;
	outputs = _o;

	binString= new boolean[(inputs * neuronsInHiddenLayer) 
			       + (neuronsInHiddenLayer * neuronsInHiddenLayer * (hiddenLayers - 1))
			       + (neuronsInHiddenLayer * outputs)];

	for (int i = 0; i < binString.length; i++)
	    {	
		binString[i] = Math.random() > 0.5;
	    }
    }
    
    /**
     * @return The number of input neurons      
     */
    public int getInputs()
    {
	return inputs;
    }

    /**
     * @return The number of neurons in each hidden layer
     */
    public int getNeuronsInHiddenLayer()
    {
	return neuronsInHiddenLayer;
    }

    /**
     * @return The number of hidden layers
     */
    public int getHiddenLayers()
    {
	return hiddenLayers;
    }

    /**
     * @return The number of output neurons
     */
    public int getOutputs()
    {
	return outputs;
    }

    /**
     * @return The length of the binary string
     */
    public int getBinStringLength()
    {
	return binString.length;
    }

    /**
     * Returns the _bth bit in the String.
     * @param _b The bit we want to know
     * @return The _bth bit
     */
    public boolean getBit(int _b)
    {
	return binString[_b];
    }

    /**
     * @param _b The bit we want to set
     * @param _v The new value for that bit
     */
    public void setBit(int _b, boolean _v)
    {
	binString[_b] = _v; 
    }

    /**
     * A method for bit-flipping
     * @param _b The bit we want to flip
     */
    public void flipBit(int _b)
    {
	if (binString[_b])
	    binString[_b] = false;
	else
	    binString[_b] = true;
    }

    /**
     * A method for single-point crossover
     * The crossover point is random
     * @param _g The genome to cross with this genome
     * @return The child genome
     */
    public Genome singlePointCross(Genome _g)
    {
	Genome child = new Genome(inputs, neuronsInHiddenLayer, hiddenLayers, outputs);
	
	int cP = (int)(Math.random() * binString.length);

	for(int i = 0; i < cP; i++)
	    child.setBit(i, binString[i]);
	
	for(int i = cP; i < _g.getBinStringLength(); i++)
	    child.setBit(i, _g.getBit(i));
	
	return child;
    }
    

    /**
     * A method for single-point crossover
     * The crossover point is specified
     * @param _g The genome to cross with this genome
     * @param _cP The crossover point
     * @return The child genome
     */
    public Genome singlePointCross(Genome _g, int _cP)
    {
	Genome child = new Genome(inputs, neuronsInHiddenLayer, hiddenLayers, outputs);

	for(int i = 0; i < _cP; i++)
	    child.setBit(i, binString[i]);
	
	for(int i = _cP; i < _g.getBinStringLength(); i++)
	    child.setBit(i, _g.getBit(i));

	return child;
    }

    /**
     * A method for uniform crossover.
     * A bit in the child genome has an equal chance of coming from either parent
     * @param _g The genome to cross with this genome
     * @return The child genome
     */
    public Genome uniformCross(Genome _g)
    {
	Genome child = new Genome(inputs, neuronsInHiddenLayer, hiddenLayers, outputs);
	
	for (int i = 0; i < binString.length; i++)
	    {
		if (Math.random() > 0.5)
		    child.setBit(i, binString[i]);
		else
		    child.setBit(i, _g.getBit(i));
	    }
	return child;	
    }

    /**
     * Mutates this genome.
     * @param _p The percentage to mutate the genome.
     */
    public void mutate(int _p)
    {
	double percentage = _p / 100.0;

	for(int i = 0; i < binString.length; i++)
	    {
		if (Math.random() > percentage)
		    {
			this.flipBit(i);
		    }
	    }
    }

    /**
     * @return A String to represent the genome
     */
    public String toString()
    {
	/*	String s = "Number of inputs: \t\t\t" + inputs + "\n"
	    + "Number of units in each hidden layer: \t" + neuronsInHiddenLayer + "\n"
	    + "Number of  hidden layers: \t\t" + hiddenLayers + "\n"
	    + "Number of outputs: \t\t\t" + outputs + "\n"
	    + "The Binary string: \t\t\t";
	*/
	String s = "";

	for (int i = 0; i < binString.length; i++)
	    {
		if (binString[i])
		    s += "1";
		else
		    s += "0";
	    }
 
	return s;
    }

    /**
     * For testing 
     */
    public static void main(String[] args)
    {
	Genome[] parents = new Genome[100];
	for (int i = 0; i < parents.length; i++)
	    {
		parents[i] = new Genome(3,3,3,1);
	    }

	Genome[] children = new Genome[parents.length];
	int p1, p2;

	for (int i = 0; i < children.length; i++)
	    {
		p1 = (int)(Math.random() * parents.length);
		p2 = (int)(Math.random() * parents.length);
		System.out.println("p1[" + p1 + "]:\t" + parents[p1]); 
		System.out.println("p2[" + p2 + "]:\t" + parents[p2]); 

		children[i] = parents[p1].uniformCross(parents[p2]);
		System.out.println("\t" + children[i]);
		children[i].mutate(50);
		System.out.println("\t" + children[i]);
		
		System.out.println();
	    }
    }
}
