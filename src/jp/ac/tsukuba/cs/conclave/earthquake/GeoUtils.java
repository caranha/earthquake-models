package jp.ac.tsukuba.cs.conclave.earthquake;

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
	public static double forwardazimuth(double lat1, double lon1, double lat2, double lon2)
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
	
//	static double crosstrackdistance
//	
//	ript: 	
//
//		var dXt = Math.asin(Math.sin(d13/R)*Math.sin(brng13-brng12)) * R;
//
//		Here, the great-circle path is identified by a start point and an end point – depending on what initial data you’re working from, you can use the formulæ above to obtain the relevant distance and bearings. The sign of dxt tells you which side of the path the third point is on.
//
//		The along-track distance, from the start point to the closest point on the path to the third point, is
//		Formula: 	dat = acos(cos(d13/R)/cos(dxt/R)) * R
//		where 	d13 is distance from start point to third point
//		dxt is cross-track distance
//		R is the earth’s radius
//		JavaScript: 	
//
//		var dAt = Math.acos(Math.cos(d13/R)/Math.cos(dXt/R)) * R;
	
	
}
