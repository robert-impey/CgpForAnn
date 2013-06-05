/**
 * A frame to contain the panel with the diagram of the network
 * @author Rob Impey
 * @date 4-ii-03
 */

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class NetworkFrame extends JFrame
{
    /**
     * Creates a new NetworkFrame
     * @param _t The title of the diagram     
     * @param _g The genome of the network
     * @param _w The weights of the network
     */
    public NetworkFrame(String _t, Genome _g, Link[] _w)
    {
	setTitle(_t);
	
	int width = 175 + _g.getHiddenLayers() * 82;
	int height = 100 + _g.getNeuronsInHiddenLayer() * 78;
	setSize(width, height);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	NetworkPanel panel = new NetworkPanel(_g, _w);
	Container contentPane = getContentPane();
	contentPane.add(panel);
    }
}
