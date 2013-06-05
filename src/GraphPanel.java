/**
 * A panel that represents an array of doubles as a graph.
 * @author Rob Impey
 * @date 18-iii-03
 */

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class GraphPanel extends JPanel 
{
    // The axis of the graph
    private static final Line2D.Double yMain = new Line2D.Double(50.0, 35.0, 50.0, 300.0); // The main part of the y-axis
    private static final Line2D.Double yLeft = new Line2D.Double(50.0, 35.0, 45.0, 45.0); // The left arrow of the y-axis
    private static final Line2D.Double yRight = new Line2D.Double(50.0, 35.0, 55.0, 45.0); // The right arrow of the y-axis
    private static final Line2D.Double xMain = new Line2D.Double(50.0, 300.0, 350.0, 300.0); // The main part of the x-axis
    private static final Line2D.Double xTop = new Line2D.Double(350.0, 300.0, 340.0, 295.0); // The top arrow of the x-axis
    private static final Line2D.Double xBottom = new Line2D.Double(350.0, 300.0, 340.0, 305.0); // The bottom arrow of the x-axis

    // The rules on the axes
    private static final Line2D.Double xRule1 = new Line2D.Double(50.0, 300.0, 50.0, 305.0);
    private static final Line2D.Double xRule2 = new Line2D.Double(112.5, 300.0, 112.5, 305.0);
    private static final Line2D.Double xRule3 = new Line2D.Double(175.0, 300.0, 175.0, 305.0);
    private static final Line2D.Double xRule4 = new Line2D.Double(237.5, 300.0, 237.5, 305.0);
    private static final Line2D.Double xRule5 = new Line2D.Double(300.0, 300.0, 300.0, 305.0);

    private static final Line2D.Double yRule1 = new Line2D.Double(45.0, 50.0, 50.0, 50.0);
    private static final Line2D.Double yRule2 = new Line2D.Double(45.0, 112.5, 50.0, 112.5);
    private static final Line2D.Double yRule3 = new Line2D.Double(45.0, 175.0, 50.0, 175.0);
    private static final Line2D.Double yRule4 = new Line2D.Double(45.0, 237.5, 50.0, 237.5);
    private static final Line2D.Double yRule5 = new Line2D.Double(45.0, 300.0, 50.0, 300.0);

    // The instance variables
    private String xTitle, yTitle;
    private double yHeight;
    private Color lineColor;
    private String[] xAxisNumbers, yAxisNumbers;
    private double[] points;
    private Line2D.Double[] lines;

    /**
     * Creates a new GraphPanel
     * @param _xTitle The title of the x-axis
     * @param _yTitle The title of the y-axis
     * @param _a The array of doubles that the graph represents
     * @param _h The height of the y-axis
     * @param _c The color of the line.
     */
    public GraphPanel(String _xTitle, String _yTitle, double[] _a, double _h, Color _c)
    {
	setBackground(Color.white);

	xTitle = _xTitle;
	yTitle = _yTitle;
	points = _a;
	yHeight = _h;
	lineColor = _c;

	xAxisNumbers = new String[5];
	
	xAxisNumbers[0] = "" + 0;
	xAxisNumbers[1] = "" + (int)(_a.length * 0.25);
	xAxisNumbers[2] = "" + (int)(_a.length * 0.5);
	xAxisNumbers[3] = "" + (int)(_a.length * 0.75);
	xAxisNumbers[4] = "" + _a.length;

	yAxisNumbers = new String[5];
	
	yAxisNumbers[0] = "" + 0;
	yAxisNumbers[1] = "" + _h * 0.25;
	yAxisNumbers[2] = "" + _h * 0.5;
	yAxisNumbers[3] = "" + _h * 0.75;
	yAxisNumbers[4] = "" + _h;

	// The lines of the curve
	lines = new Line2D.Double[points.length - 1]; // Think of stretches of wire between fence posts

	double xInc = 250.0 / (double)lines.length;
	double xS = 50.0;
	double yS = 300.0 - (points[0] / _h) * 250.0;
	double xF = 50.0;
	double yF = 0.0;

	for (int i = 0; i < lines.length; i++)
	    {	  
		xF += xInc;
		yF = 300.0 - (points[i + 1] / _h) * 250.0;
		lines[i] = new Line2D.Double(xS, yS, xF, yF);
		xS = xF;
		yS = yF;
	    }
    }

    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D)g;

	// Draw the axes and label them
	g2.setPaint(Color.black);
	g2.draw(yMain);
	g2.draw(yLeft);
	g2.draw(yRight);
	g2.draw(xMain);
	g2.draw(xTop);
	g2.draw(xBottom);

	g2.draw(xRule1);
	g2.draw(xRule2);
	g2.draw(xRule3);
	g2.draw(xRule4);
	g2.draw(xRule5);
 
	g2.draw(yRule1);
	g2.draw(yRule2);
	g2.draw(yRule3);
	g2.draw(yRule4);
	g2.draw(yRule5);

	g2.drawString(xAxisNumbers[0], 45.0f, 320.0f);
	g2.drawString(xAxisNumbers[1], 107.5f, 320.0f);
	g2.drawString(xAxisNumbers[2], 170.0f, 320.0f);
	g2.drawString(xAxisNumbers[3], 232.5f, 320.0f);
	g2.drawString(xAxisNumbers[4], 295.0f, 320.0f);

	g2.drawString(yAxisNumbers[0], 15.0f, 300.0f);
	g2.drawString(yAxisNumbers[1], 15.0f, 237.5f);
	g2.drawString(yAxisNumbers[2], 15.0f, 175.0f);
	g2.drawString(yAxisNumbers[3], 15.0f, 112.5f);
	g2.drawString(yAxisNumbers[4], 15.0f, 50.0f);

	g2.drawString(xTitle, 320.0f, 340.0f);
	g2.drawString(yTitle, 20.0f, 25.0f);

	// Draw the line
	g2.setPaint(lineColor);
	
	for (int i = 0; i < lines.length; i++)
	    {
		g2.draw(lines[i]);
	    }
    }
}
