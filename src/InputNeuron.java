/**
 * An Input neuron.
 * These objects store the indexes of the links that are connected to this neuron
 * @author Rob Impey
 * @date 19-iii-03
 */

import java.io.*;

public class InputNeuron implements Serializable
{
    // The array of indexes of outgoing links
    private int[] out;

    /**
     * Creates a new InputNeuron
     * @param _n The number of neurons in the first hidden layer
     */
    public InputNeuron(int _n)
    {
	out = new int[_n];
    }

    /**
     * Adds a Link to the array
     * @param _i The index in this array
     * @param _l The index in the central array of Links
     */
    public void setLink(int _i, int _l)
    {
	out[_i] = _l;
    }

    /**
     * Returns the central index of this neuron's _ith link
     * @param _i The index in this neuron's array of the central link index to be returned
     * @return The index in the central array of this neuron's _ith Link
     */
    public int getLink(int _i)
    {
	return out[_i];
    }
}
