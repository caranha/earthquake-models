package jp.ac.tsukuba.cs.conclave.earthquake.utils;

import java.util.ArrayList;

/**
 * A few useful functions regarding statistics. 
 * I probably want to see if I can find this implemented somewhere already.
 * 
 * @author caranha
 *
 */
public class StatUtils {

	/**
	 * Calculates the average and the variance of an array of doubles. 
	 * @param data ArrayList with doubles;
	 * @return array where the first value is the average, and the second is the variance
	 */
	static public double[] averageVariance(ArrayList<Double> data)
	{
		double[] ret = new double[2];
		ret[0] = 0;
		ret[1] = 0;

		for (int i = 0; i < data.size(); i++)
		{
			ret[0] += data.get(i);
		}
		ret[0] /= data.size();
		
		for (int i = 0; i < data.size(); i++)
		{
			ret[1] += Math.pow((data.get(i) - data.size()), 2);
		}		
		ret[1] /= data.size();
		
		return ret;
	}
	
	
}
