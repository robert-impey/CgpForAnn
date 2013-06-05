/**
 * A serializable class that stores the progress of the evolution.
 * Used for saving and opening files.
 * @author Rob Impey
 * @date 21-iii-03
 */

import  java.awt.*;
import java.io.*;

public class EvolutionPerformance implements Serializable
{
    private String title; // The title of the evolutionary run
    private double[] progress; // The integrals of the MSSE of the best network in each generation

    /**
     * Creates a new EvolutionPerformance 
     * @param _t The title of the evolutionary run
     * @param _p The integrals of the MSSE of the best network in each generation
     */
    public EvolutionPerformance(String _t, double[] _p)
    {
	title = _t;
	progress = _p;
    }

    /**
     * Returns a graph to show the progress
     * @return A graph to show the progress
     */
    public GraphFrame getGraph()
    {
	return new GraphFrame(title, "Generation", "Int. MSSE", progress, new Color(27, 137, 91));
    }
}
