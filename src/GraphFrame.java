/**
 * A frame to contain a GraphPanel.
 * @author Rob Impey
 * @date 18-iii-03
 */

import java.awt.*;
import javax.swing.*;

public class GraphFrame extends JFrame
{
    // All the graphs will be the same size
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    // The height of the Y-axis
    double yH;

    /**
     * Creates a new GraphFrame
     * @param _t The title of the Graph
     * @param _xTitle The title of the x-axis
     * @param _yTitle The title of the y-axis
     * @param _a The array of doubles that the graph will display
     * @param _c The color of the line of the graph
     */
    public GraphFrame(String _t, String _xTitle, String _yTitle, double[] _a, Color _c)
    {
	setTitle(_t);
	setSize(WIDTH, HEIGHT);	
	setResizable(false);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Set the height of the y-axis
	yH = 1.0;
	for (int i = 0; i < _a.length; i++)
	    {
		if (_a[i] > yH)
		    yH *= 2.0;
	    }
	
	GraphPanel panel = new GraphPanel(_xTitle, _yTitle, _a, yH, _c);
	Container contentPane = getContentPane();
	contentPane.add(panel);
    }

    /**
     * For testing
     */
    public static void main(String[] args)
    {
	// A random graph
	double[] ran = new double[100];
	
	for (int i = 0; i < ran.length; i++)
	    {
		ran[i] = Math.random();
	    }
	
	GraphFrame ranF = new GraphFrame("Random Graph", "Epochs", "MSSE", ran, Color.blue);
	
	ranF.show();
	
	// A parabolic graph
	double[] parab = new double[20];
	
	for (int i = 0; i < parab.length; i++)
	    {
		parab[i] = i * i;
	    }
	
	GraphFrame parabF = new GraphFrame("Squaring", "x", "x * x", parab, Color.red);
	
	parabF.show();
    }
}
