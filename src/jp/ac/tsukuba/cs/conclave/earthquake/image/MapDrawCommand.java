package jp.ac.tsukuba.cs.conclave.earthquake.image;

import java.awt.Color;

/**
 * This abstract class describes something that will be drawn on the map.
 * 
 * @author caranha
 *
 */
public abstract class MapDrawCommand implements Comparable<MapDrawCommand> {

	int depth = 0;	
	Color mainColor = Color.BLACK; // Main color to be used when drawing this event;
	Color subColor = Color.GREEN; // Sub color to be used when drawing this event;
	String name = "";
	
	/**
	 * 
	 * @param img
	 */
	public abstract void draw(MapImage img);	

	public void setDepth(int d)
	{
		depth = d;
	}
	
	public int getDepth()
	{
		return depth;
	}
	
	public int compareTo(MapDrawCommand o)
	{
		return (this.getDepth() - o.getDepth());
	}
	
	public void setMainColor(Color c)
	{
		mainColor = c;
	}
	public Color getMainColor()
	{
		return mainColor;
	}
	
	
	public void setSubColor(Color c)
	{
		subColor = c;
	}
	public Color getSubColor()
	{
		return subColor;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
