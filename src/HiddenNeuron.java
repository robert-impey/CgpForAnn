/**
 * An Hidden neuron.
 * These objects store the indexes of the links that are connected to this neuron
 * @author Rob Impey
 * @date 19-iii-03
 */

import java.io.*;

public class HiddenNeuron  implements Serializable
{
    // The array of indexes of incoming and outgoing links
    private int[] in, out;

    /**
     * Creates a new HiddenNeuron
     * @param _p The number of neurons in the previous layer
     * @param _n The number of neurons in the next layer
     */
    public HiddenNeuron(int _p, int _n)
    {
	in = new int[_p];
	out = new int[_n];
    }

    /**
     * Adds a incoming Link to the in array
     * @param _i The index in this array
     * @param _l The index in the central array of Links
     */
    public void setIncomingLink(int _i, int _l)
    {
	in[_i] = _l;
    }

    /**
     * Adds a outgoing Link to the out array
     * @param _i The index in this array
     * @param _l The index in the central array of Links
     */
    public void setOutgoingLink(int _i, int _l)
    {
	out[_i] = _l;
    }

    /**
     * Returns the central index of this neuron's _ith incoming link
     * @param _i The index in this neuron's array of the central link index to be returned
     * @return The index in the central array of this neuron's _ith incoming Link
     */
    public int getIncomingLink(int _i)
    {
	return in[_i];
    }

    /**
     * Returns the central index of this neuron's _ith outgoing link
     * @param _i The index in this neuron's array of the central link index to be returned
     * @return The index in the central array of this neuron's _ith outgoing Link
     */
    public int getOutgoingLink(int _i)
    {
	return out[_i];
    }
}
