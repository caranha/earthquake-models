package jp.ac.tsukuba.cs.conclave.earthquake;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import jp.ac.tsukuba.cs.conclave.earthquake.FMtest.FaultModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoDataReader;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoLine;

public class CodeTester {

	/**
	 * This class is used for temporary testing of isolated code pieces that I have written here.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		testingGeoLines();
	}
	
	public static void testingGeoLines()
	{
		BufferedReader reader = null;
		String filename = "/home/caranha/Desktop/Work/Earthquake_bogdan/data/faults_japan.m";
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GeoLine[] geolines = GeoDataReader.readGeoLines(reader);
		System.out.println("Total Geolines read: "+geolines.length);
		System.out.print("Points per line: ");
		for (int i = 0; i < geolines.length; i++)
		{			
			System.out.print(geolines[i].getSize()+" ");
		}
		System.out.println("\nFirst Point: "+geolines[0].getPoint(0).lon+" "+geolines[0].getPoint(0).lat);
	}
	
	/**
	 * Tests if the "Fault Model" class is working correctly
	 */
	public static void testingFaultModel()
	{
		DataList fnet = new DataList();
		fnet.loadData("catalog_fnet_1997_20130429_f3.txt","fnet");
		FaultModel fm = new FaultModel(fnet.data.get(0),1);

		System.out.println(fm);
				
//		for (int i = 0; i < fnet.size(); i++)
//		{
//			fm = new FaultModel(fnet.data.get(i),0);
//		}
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
