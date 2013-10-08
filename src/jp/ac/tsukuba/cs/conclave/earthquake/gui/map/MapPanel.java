package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JPanel;

/**
 * This class packages a MapImage and a MapController for display in the GUI;
 * @author caranha
 *
 * TODO: This does not implement zooming: I should change "paintComponent" to deal with zooming,
 * by drawing the entire map into the image and only showing what is necessary
 * 
 * TODO: the size of the map image is defined arbitrarily. I should probably have 
 * some external object define the size of the map image, draw everything in it, and then 
 * Just show the needed part in "paintComponent";
 *
 */
public class MapPanel extends JPanel {

	private static final long serialVersionUID = -5205387903071024831L;

	// image size
	int width;
	int height;	
	MapImage image;
	ArrayList<MapDrawCommand> drawcommands;
	
	public MapPanel(int w, int h)
	{
		super();

		width = (w > 0?w:200);
		height = (h > 0?h:200);		
		
		// TODO: Parametrize this
		image = new MapImage(width-10, height-10, 20,120,21,121);
		this.setPreferredSize(new Dimension(width,height));
		
		drawcommands = new ArrayList<MapDrawCommand>();
	}
	
	public void addDrawCommand(MapDrawCommand c)
	{
		drawcommands.add(c);
		Collections.sort(drawcommands);
	}
	
	public boolean removeDrawCommand(MapDrawCommand c)
	{
		return drawcommands.remove(c);
	}
	
	public void clearDrawCommand()
	{
		drawcommands.clear();
	}
	
	public void redrawMap()
	{
		image.clear();
		
		Iterator<MapDrawCommand> it = drawcommands.iterator();
		while(it.hasNext())
		{
			it.next().draw(image);
		}
	}
	
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawRect(0, 0, width, height);        
        g.drawImage(image.getImage(), 5, 5, width-5, height-5, null);
    }
	
	
}
