package jp.ac.tsukuba.cs.conclave.earthquake.data;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Geoline is a series of points in Lon,Lat notation that 
 * 
 * @author caranha
 *
 */
public class GeoLine {

	ArrayList<GeoPoint> data;
	
	public GeoLine()
	{
		data = new ArrayList<GeoPoint>();
	}
	

	
	public void addPoint(GeoPoint p)
	{
		data.add(p);
	}
	
	public int getSize()
	{
		return data.size();
	}
	
	/**
	 * Gets a point from the GeoLine
	 */
	public GeoPoint getPoint(int index)
	{
		return data.get(index);
	}
	
	/**
	 * Gets a GeoPoint Iterator
	 */
	public Iterator<GeoPoint> getIterator()
	{
		return data.iterator();
	}
}

