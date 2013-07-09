package jp.ac.tsukuba.cs.conclave.earthquake.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import jp.ac.tsukuba.cs.conclave.earthquake.FMtest.FaultModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/**
 * This object handles the drawing of a map, and all its related info.
 * It does not retain any drawing info, so to do any significant redrawing, you 
 * have to draw all the primitives again.
 * 
 * @author caranha
 *
 */
public class MapImage {

	public BufferedImage map;
	
	double offx; // longitude offset
	double offy; // latitude offset
	double width;
	double height;
	
	double zoomx; // Magnification factor, usually bigger than 1
	double zoomy; // Magnification factor for the y axis
	
	public MapImage(double offx, double offy, double w, double h, double zoom)
	{
		
	}
	
	/**
	 * Clear the map for redrawing
	 */
	public void clear()
	{
		
	}
	
	public void drawFaultModelPlane(FaultModel fm, Color c)
	{
		
	}
	
	public void drawEvent(DataPoint p, Color c)
	{
		
	}
	
}
