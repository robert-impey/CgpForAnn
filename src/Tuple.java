/**
 * The holder for the data that the networks use to train and test.
 * The networks find continuous mappings between multivariate tuples.
 * @author Rob Impey
 * @date 10-ii-02
 */

import java.io.*;

public class Tuple implements Serializable
{
    // The instance fields
    private double[] inputs, outputs;
    
    /**
     * Creates a new tuple.
     * @param _i the inputs
     * @param _o the outputs
     */
    public Tuple(double[] _i, double[] _o)
    {
	inputs = _i;
	outputs = _o;
    }

    /**
     * Returns the _ith input
     * @return the _ith input
     */
    public double getInput(int _i)
    {
	return inputs[_i];
    }

    /**
     * Returns the _ith output.
     * @return the _ith output
     */
    public double getOutput(int _i)
    {
	return outputs[_i];
    }

    /** 
     * Returns a String to represent the Tuple
     * @return A String to represent the Tuple
     */
    public String toString()
    {
	String s = "";

	for (int i = 0; i < inputs.length; i++)
	    {
		s += inputs[i] + "\t";
	    }

	s += "\t";
	
	for (int i = 0; i < outputs.length; i++)
	    {
		s += outputs[i] + "\t";
	    }

	return s;
    }
}
