/**
 * A class to store the performance of a network.
 * Network.train(,,) returns an instance of this class.
 * @author Rob Impey
 * @version 21-iii-03
 */

import java.awt.*;
import java.io.*;

public class NetworkPerformance implements Serializable
{
    // The details of a Network
    private Genome genome;
    private int generation, epochs, trainSetSize, testSetSize;
    private double learningRate, mSSEIntegral;
    private double[] mSSEs;

    /**
     * Creates a new instance of NetworkPerformance
     * @param _g The Network's genome.
     * @param _generation The generation in which this Network occured.
     * @param _e The number of epochs for which this Network was trained.
     * @param _tr The size of the training set.
     * @param _te The size of the test set.
     * @param _lR The learning rate.
     * @param _m The array of the MSSEs of the Network with the test set.
     */
    public NetworkPerformance(Genome _g, int _generation, int _e, int _tr, int _te, double _lR, double[] _m)
	{
	    genome = _g;
	    generation = _generation;
	    epochs = _e;
	    trainSetSize = _tr;
	    testSetSize = _te;
	    learningRate = _lR;
	    mSSEs = _m;

	    // Calculate the area under the curve of test performance
	    // This caching will save repeated calculations
	    mSSEIntegral = 0.0;

	    for (int i = 0; i < mSSEs.length; i++)
		{
		    mSSEIntegral += mSSEs[i];
		}
	}

    /**
     * Returns the genome of this network
     * @return The genome of this network
     */
    public Genome getGenome()
    {
	return genome;
    }

    /**
     * Returns a graph the shows how the network's performance changed during training
     * @param _t The title of the graph
     */
    public GraphFrame getGraph(String _t)
    {
	return new GraphFrame(_t, "Epochs", "SSE", mSSEs, Color.blue);
    }
    
    /**
     * Returns a graph the shows how the network's performance changed during training
     */
    public GraphFrame getGraph()
    {
	return getGraph(("Graph for " + genome.getHiddenLayers() + " by " + genome.getNeuronsInHiddenLayer() 
			      + " from generation " + generation));
    }

    /**
     * Returns the integral of the MSSE over all epochs.
     * @return the integral of the MSSE over all epochs.
     */
    public double getMSSEIntegral()
    {
	return mSSEIntegral;
    }
    
    /**
     * To test the selection sort
     */
    public static void main(String[] args)
    {
	// Create a random array to sort
	double[] data = new double[100];
	for(int i = 0; i < data.length; i++)
	    {
		data[i] = Math.random();
	    }

	// Display the random array
	GraphFrame ranGF = new GraphFrame("Random", "index", "Value", data, Color.blue);
	ranGF.show();

	// Sort the array
	int k;
	double l;
	for (int i = 0; i < data.length; i++)
	    {
		k = i;
		for (int j = i + 1; j < data.length; j++)
		    {
			if (data[j] < data[k])
			    k = j;
		    }
		l = data[i];
		data[i] = data[k];
		data[k] = l;
	    }

	// Display the results
	GraphFrame sortGF = new GraphFrame("Sorted", "index", "Value", data, Color.blue);
	sortGF.show();
    }
}
