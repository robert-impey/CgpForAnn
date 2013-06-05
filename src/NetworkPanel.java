/**
 * A panel that displays a diagram of the network
 * @author Rob Impey
 * @date 4-ii-03
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;

public class NetworkPanel extends JPanel
{
    // Coordinate constants
    private static final double leftMargin = 50.0;
    private static final double topMargin = 70.0;
    private static final double titleSpace = 20.0;
    private static final double space = 70.0;

    // variables for drawing
    private double currentX, currentY;

    // Images
    private static final Image inputNeuron = Toolkit.getDefaultToolkit().getImage("inputNeuron.jpg");
    private static final Image hiddenNeuron = Toolkit.getDefaultToolkit().getImage("hiddenNeuron.jpg");
    private static final Image outputNeuron = Toolkit.getDefaultToolkit().getImage("outputNeuron.jpg");
    
    // The dimensions of the images
    private static final double iconHeight = 28.0;
    private static final double inW = 32.0;
    private static final double hW = 29.0;
    private static final double outW = 46.0;
  
    private MediaTracker tracker = new MediaTracker(this);
    
    // The genome of the network and the weights
    private Genome genome;
    private Link[] weights;

    // The lines to represent the weights
    private Line2D.Double[] linkLines;

    /**
     * Creates a new NetworkPanel
     * @param _g The genome of the network
     * @param _w The weights of the network
     */
    public NetworkPanel(Genome _g, Link[] _w)
    {
	setBackground(Color.white);	

	genome = _g;
	weights = _w;

	// Tracker for the images
	tracker.addImage(inputNeuron, 0);
	tracker.addImage(hiddenNeuron, 1);
	tracker.addImage(outputNeuron, 2);
	try
	    {
		tracker.waitForAll();
	    }
	catch (InterruptedException e)
	    {	
	    }

	// Create the lines for the links between the layers
	// The array of lines is actually complete (as for a fully connected network)
	// The loop in paintComponent decideds whether to draw the line or not.
	linkLines = new Line2D.Double[weights.length];
	int counter = 0;
	double fromX = leftMargin + inW; 
	double fromY = topMargin + titleSpace + iconHeight / 2.0;
	double toX = leftMargin + space;
	double toY = fromY;

	// Create the lines for the links between the first hidden layer and the input layer
	for (int i = 0; i < genome.getNeuronsInHiddenLayer(); i++) // Loop through the neurons in 1st hidden layer
	    {
		fromY = topMargin + titleSpace + iconHeight / 2.0;
		for (int j = 0; j < genome.getInputs(); j++) // Loop through the neurons in the input layer
		    {
			linkLines[counter] = new Line2D.Double(fromX, fromY, toX, toY);
			fromY += space;
			counter++;
		    }
		toY += space;
	    }

	// Create the lines for the links between the hidden layers
	fromX += space; 
	fromY = topMargin + titleSpace + iconHeight / 2.0;
	toX = fromX + space - hW;
	toY = fromY;
	for (int i = 0; i < genome.getHiddenLayers() - 1; i++) // Loop across the hidden layers
	    {
		fromY = topMargin + titleSpace + iconHeight / 2.0;
		toY = fromY;
		for (int j = 0; j < genome.getNeuronsInHiddenLayer(); j++) // Loop down the 'to' hidden layer
		    {
			fromY = topMargin + titleSpace + iconHeight / 2.0;
			for (int k = 0; k < genome.getNeuronsInHiddenLayer(); k++) // Loop down the 'from' hidden layer
			    {
				linkLines[counter] = new Line2D.Double(fromX, fromY, toX, toY);
				fromY += space;
				counter++;
			    } 
			toY += space;
		    } 
		fromX += space;
		toX = fromX + space - hW;
	    }
	
	// Create the lines for the links between the output layer and the final hidden layer
	fromX = leftMargin + space * genome.getHiddenLayers() + hW; 
	fromY = topMargin + titleSpace + iconHeight / 2.0;
	toX = leftMargin + space * (genome.getHiddenLayers() + 1);
	toY = fromY;
	for (int i = 0; i < genome.getOutputs(); i++) // Loop through the outputs
	    {
		fromY = topMargin + titleSpace + 14.0;
		for (int j = 0; j < genome.getNeuronsInHiddenLayer(); j++) // Loop through the neurons in the final hidden layer 
		    {
			linkLines[counter] = new Line2D.Double(fromX, fromY, toX, toY);
			fromY += space;
			counter++;
		    }
		toY += space;
	    }	 
	
    }
    
    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D)g;

	currentX = leftMargin;
	currentY = topMargin;

	// Draw the links

	for (int i = 0; i < linkLines.length; i++)
	    {
		if (weights[i].isActive())
		    {
			g2.setPaint(weights[i].getColor());
			g2.draw(linkLines[i]);
		    }
	    }

	g2.setPaint(Color.black);

	// Draw the input neurons	
	g2.drawString("Inputs",(float)currentX, (float)currentY);
	currentY += titleSpace;

	for (int i = 0; i < genome.getInputs(); i++)
	    {
		g2.drawImage(inputNeuron, (int)currentX, (int)currentY, null);
		currentY += space;
	    }

	// Draw the hidden Neurons
	currentX += space;
	currentY = topMargin;
	g2.drawString("Hidden Layers",(float)currentX, (float)currentY);

	for (int i = 0; i < genome.getHiddenLayers(); i++)
	    {
		currentY = topMargin + titleSpace;
		for (int j = 0; j < genome.getNeuronsInHiddenLayer(); j++)
		    {			
			g2.drawImage(hiddenNeuron, (int)currentX, (int)currentY, null);
			currentY += space;
		    }
		currentX += space;
	    }

	// Draw the output neurons
	currentY = topMargin;
	g2.drawString("Outputs",(float)currentX, (float)currentY);
	currentY += titleSpace;
	
	for (int i = 0; i < genome.getOutputs(); i++)
	    {
		g2.drawImage(outputNeuron, (int)currentX, (int)currentY, null);
		currentY += space;					
	    }

	// Draw the links

	for (int i = 0; i < linkLines.length; i++)
	    {
		if (weights[i].isActive())
		    {
			g2.setPaint(weights[i].getColor());
			g2.draw(linkLines[i]);
		    }
	    }
    }
}
