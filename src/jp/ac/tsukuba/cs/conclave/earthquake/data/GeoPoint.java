package jp.ac.tsukuba.cs.conclave.earthquake.data;

public class GeoPoint {

	public double lon;
	public double lat;
	
	public GeoPoint()
	{
		
	}
	
	public GeoPoint(double x, double y)
	{
		lon = x;
		lat = y;
	}
	
	public double getX()
	{
		return lon;
	}
	
	public double getY()
	{
		return lat;
	}
}
