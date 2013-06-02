package jp.ac.tsukuba.cs.conclave.earthquake;

/**
 * Static Class containing useful scripts for Geo Calculations.
 * Most of the scripts in this class are adapted (if not copied) from the
 * "scripts-geo" by Chris Veness, found at:
 * 
 * http://www.movable-type.co.uk/scripts/latlong.html
 * 
 * All the code in this class is licensed under a creative commons CC-BY license,
 * which means that you can use it for any purposes as long as the CC-BY copyright notice
 * and attribution to the author with a link to the above website is kept.
 * 
 * CC-BY license:
 * http://creativecommons.org/licenses/by/3.0/
 * 
 * 
 */
public class GeoUtils {

	
	static final double RADIUS = 6371; /* Mean Radius of the Earth in km */
	
	/**
	 * Calculates the earth surface distance in kilometers using the 
	 * Haversine formula, from two lat-long pairs
	 * 
	 * @param lat1 latitude for the first point
	 * @param lon1 longitude for the first point
	 * @param lat2 latitude for the second point 
	 * @param lon2 longitude for the second point
	 * @return
	 */
	public static double haversineDistance(double lat1, double lon1, double lat2, double lon2)
	{
		double latDistance = Math.toRadians(lat1 - lat2);
	    double lngDistance = Math.toRadians(lon1 - lon2);

	    double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
	                    (Math.cos(Math.toRadians(lat1))) *
	                    (Math.cos(Math.toRadians(lat2))) *
	                    (Math.sin(lngDistance / 2)) *
	                    (Math.sin(lngDistance / 2));

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return RADIUS * c;
	}
	
	/**
	 * This function calculates the bearing from the first point to 
	 * the second point. It is also used in further calculations below.
	 * 
	 * @param lat1
	 * @param long1
	 * @param lat2
	 * @param long2
	 * @return Return the bearing direction in degrees with north at 0
	 */
	public static double forwardAzimuth(double lat1, double lon1, double lat2, double lon2)
	{
		double la1 = Math.toRadians(lat1);
		double la2 = Math.toRadians(lat2);
		double lo1 = Math.toRadians(lon1);
		double lo2 = Math.toRadians(lon2);
		
		double y = Math.sin(lo2-lo1) * Math.cos(la2);
		double x = Math.cos(la1)*Math.sin(la2) -
		        Math.sin(la1)*Math.cos(la2)*Math.cos(lo2-lo1);
		
		
		return (Math.toDegrees(Math.atan2(y, x))+360)%360;
	}
	
	/**
	 * Calculates the distance between point 2 and the great circle defined by Point 1 and the Azimuth.
	 * The signal of the distance indicats on which side of the great circle the third point lies.
	 * 
	 * @param lat1 Latitude for Point 1
	 * @param lon1 Longitude for Point 1
	 * @param azi1 Direction for Point 1, in degrees
	 * @param lat2 Latitude for Point 2
	 * @param lon2 Longitude for Point 2
	 * @return
	 */
	public static double crossTrackDistance(double lat1, double lon1, double azi1, double lat2, double lon2)
	{
		/* 	Formula dxt = asin(sin(d13/R)*sin(θ13−θ12)) * R
		 *	where d13 is distance from start point to third point
		 * 	θ13 is (initial) bearing from start point to third point -- Calculate this
		 * 	θ12 is (initial) bearing from start point to end point -- I have this (azi1)
		 * 	R is the earth’s radius */

		double a12 = Math.toRadians(azi1);
		double a13 = Math.toRadians(forwardAzimuth(lat1,lon1,lat2,lon2));
		double d13 = haversineDistance(lat1,lon1,lat2,lon2);

		return Math.asin(Math.sin(d13/RADIUS)*Math.sin(a13-a12)) * RADIUS;		
	}	
	
	public static double degreeDistance(double deg1, double deg2)
	{

	    double angle = (Math.abs(deg1 - deg2))%360;
	    if(angle > 180)
	        angle = 360 - angle;
	    return angle;
	}
	
}
