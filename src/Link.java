/**
 * A class for the links between neurons.
 * The class stores the weight of the link and the maximum or minimum possible values for that link.
 * @author Rob Impey
 * @date 4-ii-03
 */

import java.io.*;
import java.awt.*;

public class Link implements Serializable
{
    // Instance fields
    private double weight;                                                                               
    private final double max, min;
    private boolean isActive;

    /** Creates a new instance of a link with a random weight.
     * @param _max The maximum value for the weight
     * @param _min The minimum value for the weight
     * @param _a whether the Link is active or not.
     */
    public Link(double _max, double _min, boolean _a)
    {
	max = _max;
	min = _min;
	weight = (Math.random() * (max - min)) + min;
	isActive = _a;
    }

    /**
     * Creates a new Link with a default range for the weight.
     * @param _a Whether the link is active or not.
     */
    public Link(boolean _a)
    {
	this(1.0, -1.0, _a);
    }

    /**
     * Creates a new Link, with default parameters.
     */
    public Link()
    {
	this(1.0, -1.0, true);
    }

    /**
     * @return the max value of the weight
     */
    public double getMax()
    {
	return max;
    }

    /**
     * @return the min value of the weight
     */
    public double getMin()
    {
	return min;
    }  

    /**
     * Returns the weight of the link
     * @return the weight of the link
     */
    public double getWeight()
    {
	return weight;
    }

    /**
     * Changes the weight of a link
     * @param _w The new weight of the link
     */
    public void setWeight(double _w)
    {
	weight = _w;
    }

    /**
     * Changes the weight of a link
     * Stops weights getting extreme values
     * @param _w The adjustment to the weight of the link
     */
    public void adjustWeight(double _w)
    {
	weight += _w;

	if (weight > 100000)
	    weight = 100000;
	else if (weight < -100000)
	    weight = -100000;
    }

    /**
     * Says whether the Link is active or not.
     * @return whether the Link is active or not.
     */
    public boolean isActive()
    {
	return isActive;
    }

    /**
     * Sets the activity of the Link
     * @param _a The new activity of the Link
     */
    public void setActivity(boolean _a)
    {
	isActive = _a;
    }

    /**
     * Returns a String to represent the Link.
     * @return a String to represent the Link.
     */
    public String toString()
    {
	String s = "" + weight + " ";

	if (isActive)
	    s += "a";
	else 
	    s += "i";

	return s;
    }

    /**
     * Returns the color of the link
     * Blue for a high value, red for lower values
     * @return The color of the link
     */
    public Color getColor()
    {
	float redness, greenness, blueness;
	
	blueness = (float)((weight - min) / (max - min));
	greenness = 0.0f;
	
	if (blueness > 1.0f)
	    blueness = 1.0f;

	if (blueness < 0.0f)
	    blueness = 0.0f;
	
	redness = 1.0f - blueness;

	return new Color(redness, greenness, blueness);
    }
}
