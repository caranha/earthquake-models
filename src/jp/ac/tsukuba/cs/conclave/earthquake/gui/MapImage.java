package jp.ac.tsukuba.cs.conclave.earthquake.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import jp.ac.tsukuba.cs.conclave.earthquake.FMtest.FaultModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoLine;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoPoint;

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
	int width;
	int height;
	
	double zoomx; // Magnification factor, usually bigger than 1
	double zoomy; // Magnification factor for the y axis
	
	public MapImage(double offx, double offy, int w, int h, double zoom)
	{
		zoomx = zoomy = zoom;
		this.offx = offx;
		this.offy = offy;
		this.width = w;
		this.height = h;
		
		map = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		this.clear();
	}
	
	/**
	 * Clear the map for redrawing
	 */
	public void clear()
	{
		Graphics g = map.createGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		map.flush();
	}
	
	public void saveToFile(String filename)
	{
		File f = new File(filename);
	    
		try {
			ImageIO.write(map, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void drawGeoLine(GeoLine l, Color c)
	{
		Iterator<GeoPoint> it = l.getIterator();
		GeoPoint prev, cur;
		Graphics g = map.createGraphics();	
		g.setColor(c);
		
		prev = it.next();
		while (it.hasNext())
		{
			int x0,y0,x1,y1;
			cur = it.next();
			x0 = (int)Math.floor((prev.lon - offx)*zoomx);
			y0 = height - (int)Math.floor((prev.lat - offy)*zoomy);
			x1 = (int)Math.floor((cur.lon - offx)*zoomx);
			y1 = height - (int)Math.floor((cur.lat - offy)*zoomy);
						
			g.drawLine(x0,y0,x1,y1);
		}
		
			
		map.flush();
	}
	
	public void drawFaultModelPlane(FaultModel fm, Color c)
	{
		Graphics g = map.createGraphics();	
		g.setColor(c);
		
		
		double plane[][] = fm.getPlane();
		int j = 3;
		for (int i = 0; i < 4; i++)
		{
			int x0,y0,x1,y1;
			x0 = (int)Math.floor((plane[j][0] - offx)*zoomx);
			y0 = height - (int)Math.floor((plane[j][1] - offy)*zoomy);
			x1 = (int)Math.floor((plane[i][0] - offx)*zoomx);
			y1 = height - (int)Math.floor((plane[i][1] - offy)*zoomy);
			
			g.drawLine(x0,y0,x1,y1);
			j = i;
		}
		
		map.flush();		
	}
	
	public void drawEvent(DataPoint p, Color c)
	{
		
	}
	
}
