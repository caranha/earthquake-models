package jp.ac.tsukuba.cs.conclave.earthquake.image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

public class DrawEarthquakeList extends MapDrawCommand {

	ArrayList<DrawEarthquake> list;
	
	public DrawEarthquakeList(Iterator<DataPoint> it)
	{
		depth = 1;
		list = new ArrayList<DrawEarthquake>();
		
		while (it.hasNext())
		{
			list.add(new DrawEarthquake(it.next()));
		}
	}
	
	
	@Override
	/**
	 * Sets the color for itself, and for all its children
	 */
	public void setMainColor(Color c)
	{
		mainColor = c;
		for (DrawEarthquake aux: list)
		{
			aux.setMainColor(mainColor);
		}
	}
	
	/**
	 * Sets the color for itself, and for all its children
	 */
	public void setSubColor(Color c)
	{
		subColor = c;
		for (DrawEarthquake aux: list)
		{
			aux.setSubColor(subColor);
		}
	}
	
	
	@Override
	/**
	 * Draws all the earthquakes drawers in this composite.
	 */
	public void draw(MapImage img) {		
		for (DrawEarthquake aux: list)
		{
			aux.draw(img);
		}
	}
}
