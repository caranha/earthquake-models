package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;


/** 
 * Performs ASS test and makes Molchan Diagrams. Requires a 
 * CSEPModel forecast, and a CSEPModel data, with the same number of bins.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class ASSTesting {

	
	/**
	 * Returns a list of points where the miss rate is reduced. 
	 * First value of each element in the list is the miss rate, the second is the coverage.
	 * This is expensive.
	 * 
	 * The list is ordered by higher coverage.
	 * 
	 * @return
	 */
	public static List<double[]> calculateMolchanPoints(CSEPModel forecast, CSEPModel data)
	{
		
		ArrayList<double[]> ret = new ArrayList<double[]>();
		int count = 0;
		double minRate = Double.NEGATIVE_INFINITY;
		
		double[] result;

		do {
			
			result = calculateMissRateAndCoverage(forecast, data, count);
			if (result[0] > minRate)
			{
				ret.add(result);
				minRate = result[0];
			}
			count++;
		} while (result[1] > 0);
	
		return ret;
	}
	
	
	/**
	 * First value return is the miss ratio. Second value returned is the coverage.
	 */
	public static double[] calculateMissRateAndCoverage(CSEPModel forecast, CSEPModel data, int treshold)
	{
		double[] ret = new double[2];
		
		Iterator<Integer> f = forecast.iterator();
		Iterator<Integer> d = data.iterator();
		
		double missedquakes = 0;
		double alarmbins = 0;
		
		for (int i = 0; i < forecast.getTotalBins(); i++)
		{
			int fquake = f.next();
			int dquake = d.next();
			
			if (fquake >= treshold)
				alarmbins ++;
			else
				missedquakes += dquake; // if 0, no quakes are added
		}
		
		ret[0] = missedquakes/data.getTotalEvents();
		ret[1] = alarmbins/data.getTotalBins();
		
		return ret;		
	}
	
	
	
}
