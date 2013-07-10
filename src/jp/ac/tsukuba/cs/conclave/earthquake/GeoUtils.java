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
	
	/**
	 * Calculates a final point based on an initial point, a direction (in degrees) and a distance
	 * in kilometers.
	 * 
	 * @param lon1 longitude of the starting point
	 * @param lat1 latitude of the starting point
	 * @param bearing direction in degrees, 0 is north, increases clockwise
	 * @param dist distance in kilometers.
	 * @return
	 */
	public static double[] calcPointFromDirectionDistance(double lon1, double lat1, double bearing, double dist)
	{
		double[] ret = new double[2];
		
		double dir = Math.toRadians(bearing);

		double lo0 = Math.toRadians(lon1);
		double la0 = Math.toRadians(lat1);
		
		
		ret[1] = Math.asin(Math.sin(la0)*Math.cos(dist/RADIUS) + 
				 Math.cos(la0)*Math.sin(dist/RADIUS)*Math.cos(dir));
		
		ret[0] = lo0 + Math.atan2(Math.sin(dir)*Math.sin(dist/RADIUS)*Math.cos(la0), 
	                     Math.cos(dist/RADIUS)-Math.sin(la0)*Math.sin(ret[1]));
		ret[0] = Math.toDegrees(ret[0]);
		ret[1] = Math.toDegrees(ret[1]);
		
		return ret;
	}
			
	/**
	 * Calculates the aftershock radius based on eq.9 from Helmstetter et al., JGR, 2005.
	 * Average results with standard parameters are:
	 * 
	 * Mag 7: 70km
	 * Mag 6: 30km
	 * Mag 5: 10km
	 * Mag 4: 5km
	 * 
	 * @param magnitude
	 * @return the radius to consider aftershocks in kilometers.
	 */
	public static double getAftershockRadius(double magnitude)
	{
		int RADIUS_RATE = 2;
		return	RADIUS_RATE*0.01*Math.pow(10, (0.5*magnitude));
	}
}
