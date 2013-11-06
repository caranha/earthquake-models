package jp.ac.tsukuba.cs.conclave.earthquake.image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoLine;

/**
 * This Draw Command draws a map in the back of the image, using a default depth of -1;
 * It holds a set of Geolines that describe the map to be drawn. Standard color is black, 
 * but that can be changed.
 * 
 * @author caranha
 *
 */
public class DrawGeography extends MapDrawCommand {

	ArrayList<GeoLine> currentmap;
	
	public DrawGeography()
	{
		currentmap = new ArrayList<GeoLine>();
		depth = -1;
	}
	
	/**
	 * Stores a reference to the lines to be drawn. Passing a null value will clear the map.
	 * 
	 * @param linelist
	 */
	public void setMap(ArrayList<GeoLine> linelist)
	{
		currentmap = linelist;
		if (currentmap == null)
			currentmap = new ArrayList<GeoLine>();
	}
	
	/** 
	 * Gets a reference to the current map.
	 * @return
	 */
	public ArrayList<GeoLine> getMap()
	{
		return currentmap;
	}
	
	/**
	 * 	
	 */
	@Override
	public void draw(MapImage img) {
		Iterator<GeoLine> it = currentmap.iterator();
		while (it.hasNext())
		{
			img.drawGeoLine(it.next(), mainColor);
		}
	}

}
