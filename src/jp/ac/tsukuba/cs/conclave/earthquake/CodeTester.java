package jp.ac.tsukuba.cs.conclave.earthquake;

public class CodeTester {

	/**
	 * This class is used for temporary testing of isolated code pieces that I have written here.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public static void testingDegreeDistance()
	{
		double deg1 = -100;
		double deg2 = 100;
		System.out.println(GeoUtils.degreeDistance(deg1, deg2));
	}
	
	public static void testingAzimuth()
	{
		double[] p1 = {31.5645, 131.8818};
		double[] p2 = {33.2268, 132.3592};
		
		double distance = GeoUtils.haversineDistance(p1[0],p1[1],p2[0],p2[1]);
		double bearing = GeoUtils.forwardAzimuth(p1[0], p1[1], p2[0], p2[1]);
		
		System.out.println("Distance= " + distance + "km, Bearing: "+bearing);
	}
	
}
