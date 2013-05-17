/** 
 * This class implements a forecast compatible with the RELM test battery, 
 * and related data visualization methods
 */

package jp.ac.tsukuba.cs.conclave.earthquake.forecast;

import java.util.Iterator;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

import org.joda.time.DateTime;

public class RELMForecast {

	// TODO: replace this with ParallelColt Matrixes
	double[][][] event; // forecast/observation matrix. Axis are long, lat and magnitude
	int longsize, latsize, magsize; // axis sizes
	
	double magDelta = 0.1; // Magnitude step
	double spaceDelta = 0.1; // Latitude/Longitude step (bin size)
	
	// Data Dependent 
	double startLong, endLong;
	double startLat, endLat;
	DateTime startTime, endTime;
	
	// Cached Results
	int[] totalEvents; //Total Events by Magnitude
	
	/**
	 * Simple constructor without adding any data. Sets the matrix size.
	 * @param sLo
	 * @param eLo
	 * @param sLa
	 * @param eLa
	 */
	public RELMForecast(double sLo, double eLo, double sLa, double eLa)
	{
		int longsize = (int)Math.floor((eLo - sLo)/spaceDelta + 1);
		int latsize = (int)Math.floor((eLa - sLa)/spaceDelta + 1);
		int magsize = (int)Math.floor(9/magDelta + 1);
		
		startLong = sLo;
		endLong = eLo;
		startLat = sLa;
		endLat = eLa;
		
		event = new double[longsize][latsize][magsize];
		totalEvents = new int[magsize];
	}
	
	/**
	 * Inserts data from a RawData object. Assumes start/end values for long are correctly set
	 * @param sT
	 * @param eT
	 * @param r
	 */
	public void readRawData(DateTime sT, DateTime eT, DataList r)
	{
		Iterator<DataPoint> it = r.data.iterator();
		while (it.hasNext())
		{
			DataPoint t = it.next();
			if ((t.time.isAfter(sT)||t.time.equals(sT)) && 
				(t.time.isBefore(eT)||t.time.equals(eT)))
			{
				int px = (int)Math.floor((t.longitude - startLong)/spaceDelta);
				int py = (int)Math.floor((t.latitude - startLat)/spaceDelta);
				int pmag = (int)Math.floor(t.magnitude/magDelta);
				event[px][py][pmag]++;
				totalEvents[pmag]++;
			}
		}
	}
	
	public int getTotalEvents()
	{
		int ret = 0;
		for (int i = 0; i < totalEvents.length; i++)
			ret += totalEvents[i];
		return ret;
	}
	
	
	/* TODO: Generators for Hypothesis testing
	 */
	// Generates a Modification based on error parameters
	public RELMForecast generateModification()
	{
		return null;
	}
	// Generates a simulation based on Poisson distribution
	public RELMForecast generateSimulation()
	{
		return null;
	}
	
	
}
