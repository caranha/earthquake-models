package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting;

import java.util.ArrayList;
import java.util.Iterator;

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
	 * First value of each element in the list is the coverage, the second is the miss rate.
	 * This is expensive.
	 * 
	 * The list is ordered by higher coverage.
	 * 
	 * @return
	 */
	public static ArrayList<double[]> calculateMolchanTrajectory(CSEPModel forecast, CSEPModel data)
	{
		
		ArrayList<double[]> ret = new ArrayList<double[]>();
		int count = forecast.getMaxEvents()+1;
		double missRate = Double.POSITIVE_INFINITY;
		
		double[] result;

		do {
			result = calculateMissRateAndCoverage(forecast, data, count);
			if (result[1] < missRate)
			{
				ret.add(result);
				missRate = result[1];
			}
			count--;
		} while (count >= 0);
	
		return ret;
	}
	
	public static double calculateAreaSkillScore(CSEPModel forecast, CSEPModel data)
	{
		int count = forecast.getMaxEvents()+1;
		double missRate = data.getTotalEvents();
		
		int[] result;
		double area=0;
		
		do {
			result = calculateIntegerMissRateAndCoverage(forecast, data, count);
			if (result[1] < missRate)
			{				
				area += (missRate-result[1])*((double)result[0]/(double)data.getTotalBins());
				missRate = result[1];
			}
			count--;
		} while (count >= 0);
	
		return 1.0 - (area/data.getTotalEvents());
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
		
		ret[1] = missedquakes/data.getTotalEvents();
		ret[0] = alarmbins/data.getTotalBins();
		
		return ret;		
	}
	
	public static int[] calculateIntegerMissRateAndCoverage(CSEPModel forecast, CSEPModel data, int treshold)
	{
		int[] ret = new int[2];
		Iterator<Integer> f = forecast.iterator();
		Iterator<Integer> d = data.iterator();
		
		int missedquakes = 0;
		int alarmbins = 0;
		
		for (int i = 0; i < forecast.getTotalBins(); i++)
		{
			int fquake = f.next();
			int dquake = d.next();
			
			if (fquake >= treshold)
				alarmbins ++;
			else
				missedquakes += dquake; // if 0, no quakes are added
		}
		
		ret[1] = missedquakes;
		ret[0] = alarmbins;
		
		return ret;	
	}
	
	
}
