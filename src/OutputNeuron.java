/**
 * An Output neuron.
 * These objects store the indexes of the links that are connected to this neuron
 * @author Rob Impey
 * @date 19-iii-03
 */

import java.io.*;

public class OutputNeuron implements Serializable
{
    // The array of indexes of incoming links
    private int[] in;

    /**
     * Creates a new OutputNeuron
     * @param _n The number of neurons in the last hidden layer
     */
    public OutputNeuron(int _n)
    {
	in = new int[_n];
    }

    /**
     * Adds a Link to the array
     * @param _i The index in this array
     * @param _l The index in the central array of Links
     */
    public void setLink(int _i, int _l)
    {
	in[_i] = _l;
    }

    /**
     * Returns the central index of this neuron's _ith link
     * @param _i The index in this neuron's array of the central link index to be returned
     * @return The index in the central array of this neuron's _ith Link
     */
    public int getLink(int _i)
    {
	return in[_i];
    }
}
