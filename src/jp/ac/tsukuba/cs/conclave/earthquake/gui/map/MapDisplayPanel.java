package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JPanel;

import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;

/**
 * 
 * This class displays part of an image. It can zoom in/zoom out, and move the 
 * displayed part of the image, but it does not manipulate the image itself.
 * 
 * TODO: Deal with resizing of the panel.
 *
 */
public class MapDisplayPanel extends JPanel {

	private static final long serialVersionUID = -5205387903071024831L;
	
	MapImage image;
	
	int displayposX = 0;
	int displayposY = 0;
	double zoom=1;

	public MapDisplayPanel(int w, int h, MapImage map)
	{
		super();

		this.setPreferredSize(new Dimension(w,h));
		image = map;		
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        
        g.drawRect(0, 0, this.getWidth(), this.getHeight());        
        if (image != null)
        {
            // Rectangle to draw from the map:
            int MapStartX = displayposX;
            int MapStartY = displayposY;
            int MapEndX = (int) Math.round(displayposX + image.getWidth()/zoom);
            int MapEndY = (int) Math.round(displayposY + image.getHeight()/zoom);
            
        	g.drawImage(image.getImage(), 5, 5, this.getWidth()-5, this.getHeight()-5, // Position in the panel
        			    MapStartX, MapStartY, MapEndX, MapEndY, 
        			    null); // observer to be noticed as the map is processed
        }
    }
	
    /**
     * Increases the zoom by a fixed ammount
     */
    public void multiplyZoom(double mult)
    {
    	zoom *= mult;
    	fixZoomLocation();
    }
        
    public void moveMap(int dirx, int diry)
    {
    	displayposX += Math.signum(dirx)*(image.getWidth()/(20*zoom));
    	displayposY += Math.signum(diry)*(image.getWidth()/(20*zoom));
    	fixZoomLocation();
    }
    
    public void setZoom(double z)
    {
    	zoom = z;
    	fixZoomLocation();
    }
    
    public void setLocation(int offx, int offy)
    {
    	displayposX = offx;
    	displayposY = offy;
    	fixZoomLocation();
    }
    
    public void resetZoomLocation()
    {
    	displayposX = displayposY = 0;
    	zoom = 1;
    	repaint();
    }

    /** 
     * Modifies Zoom and location if they become meaningless values
     */
    public void fixZoomLocation()
    {
    	if (zoom < 1)
    		zoom = 1;
    	
    	if (displayposX < 0)
    		displayposX = 0;
    	if (displayposY < 0)
    		displayposY = 0;
    	
    	if (displayposX + image.getWidth()/zoom > image.getWidth())
    		displayposX = (int)Math.round(image.getWidth() - image.getWidth()/zoom);
    	if (displayposY + image.getHeight()/zoom > image.getHeight())
    		displayposY = (int)Math.round(image.getHeight() - image.getHeight()/zoom);
    	repaint();
    }
}
