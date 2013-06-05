/**
 * A data set used for training and testing in the networks.
 * This class is a collection for Tuple objects.
 * The collection of objects can shuffled.
 * @author Rob Impey
 */

public class DataSet
{
    // instance fields
    private Tuple[] data;
    private int nextEmpty;

    /**
     * Creates a new data set of a specified size.     
     * @param _n The number of Tuples for the DataSet to hold.
     */
    public DataSet(int _n)
    {
	nextEmpty = 0;
	data = new Tuple[_n];
    }

    /**
     * Tells you the number of Tuples in the DataSet.
     * @return The number of Tuples in the DataSet.
     */
    public int getLength()
    {
	return data.length;
    }

    /**
     * A method for adding Tuples.
     * @param _t The tuple to add.
     */
    public void add(Tuple _t)
    {
	data[nextEmpty] = _t;
	nextEmpty++;
    }

    /**
     * Returns the _ith Tuple in the object.
     * @param _i The index of the Tuple that we want
     * @return The _ith Tuple.
     */
    public Tuple getTuple(int _i)
    {
	return data[_i];
    }

    /**
     * Returns an array of Tuples to train or test a network.
     * The method picks the Tuples at random.
     * @param _n The number of Tuples requested.
     * @return An array of _n Tuples.
     */
    public Tuple[] getSample(int _n)
    {
	Tuple[] t = new Tuple[_n];

	for (int i = 0; i < _n; i++)
	    {
		t[i] = data[(int)(Math.random() * data.length)];
	    }

	return t;
    }

    /**
     * Represents the DataSet as a String.
     * @return A String to represent the DataSet
     */
    public String toString()
    {
	String s = "";

	for (int i = 0; i < data.length; i++)
	    {
		s += data[i] + "\n";
	    }

	return s;
    }
}
