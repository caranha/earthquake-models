package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;


/** 
 * 
 * This class stores a simple latitude-longitude CSEP-like model.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class LatLonCSEPModel {

	// Array of events;
	int[][] bins;

	double[] base; // base value for the SW lat/lon
	double[] delta; // value change from base for each bin
	int[] maxIndex; // maximum bin number

	public LatLonCSEPModel(double[] baseLatLon, double[] deltaLatLon, int[] maximum)
	{
		bins = new int[maximum[0]][maximum[1]];
		base = baseLatLon;
		delta = deltaLatLon;
	}

	/**
	 * resets the values of all bins to 0
	 */
	public void clearBins()
	{
		bins = new int[maxIndex[0]][maxIndex[1]];
	}

	public void addFromDataList(DataList d)
	{
		
	}
	
	public double logLikelihood(LatLonCSEPModel m)
	{
		return 0;
	}
	
	
	/**
	 * Returns the size of the Bin Matrix, and what kinds of elements are 
	 * stored in its bins
	 */
	public String toString()
	{
		String ret = "LatLon Simple Model:";
		ret = ret + " "+maxIndex[0]+" cols, "+maxIndex[1]+" rows\n";
		return ret;
	}
	
}
