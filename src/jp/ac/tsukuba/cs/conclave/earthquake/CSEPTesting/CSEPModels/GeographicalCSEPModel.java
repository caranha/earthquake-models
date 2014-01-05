package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import java.awt.Color;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPUtils;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;


/** 
 * This class stores a simple latitude-longitude CSEP model.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class GeographicalCSEPModel implements CSEPModel {

	// Array of events;
	int[][] bins;
	int totalevents;
	float maxevents; // bin with maximum number of events
	
	Double loglikelihood = null; // ll cache
	CSEPModel loglikelihoodbase = null; // model/data used for creating the ll cache

	double[] base; // base value for the SW lon/lat
	double[] delta; // value change from base for each bin
	int[] dimlength; // maximum bin number

	public GeographicalCSEPModel(double baselon, double baselat, 
								  double deltalon, double deltalat, 
								  int binlon, int binlat)
	{	
		bins = new int[binlon][binlat];
		
		base = new double[2];		
		base[0] = baselon;
		base[1] = baselat;
		
		delta = new double[2];
		delta[0] = deltalon;
		delta[1] = deltalat;
		
		dimlength = new int[2];
		dimlength[0] = binlon;
		dimlength[1] = binlat;
		
		maxevents = 0;
	}

	/**
	 * resets the values of all bins to 0
	 */
	public void clearBins()
	{
		bins = new int[dimlength[0]][dimlength[1]];
		totalevents = 0;
	}
	
	public void clearLLcache()
	{
		loglikelihood = null;
		loglikelihoodbase = null;
	}
	
	public String toString()
	{
		String ret = "Simple Geographical Model:";
		ret = ret + " "+(dimlength[0])+" cols, "+(dimlength[1])+" rows,";
		ret = ret + " total events: "+totalevents+".";
		return ret;
	}



	@Override
	public Double getLogLikelihood(CSEPModel comp) 
	{		
		if (loglikelihood == null || !(comp.equals(loglikelihoodbase)))
			calculateLogLikelihood((GeographicalCSEPModel) comp);
		return loglikelihood;
	}
	
	@Override
	public Double getLogLikelihood() 
	{		
		return loglikelihood;
	}
	
	
	private void calculateLogLikelihood(GeographicalCSEPModel c)
	{
		loglikelihoodbase = c;
		loglikelihood = 0.0;
		
		for (int i = 0; i < dimlength[0]; i++)
			for (int j = 0; j < dimlength[1]; j++)
			{
				if (bins[i][j] == 0)
				{
					if (c.bins[i][j] == 0)
						loglikelihood +=1;
					else
					{	
						// TODO: replace this with a log message
						//System.err.println("[WARNING] Log likelihood calculation: Lambda 0, Omega not 0");
						loglikelihood = null;
						return;
					}
				}		
				else 
				{
					int facw = 0;
					for (int k = 0; k < c.bins[i][j]; k++)
						facw += Math.log(k+1);
					loglikelihood += -bins[i][j] + c.bins[i][j]*Math.log(bins[i][j])-facw;
				}
			}
	}
	

	@Override
	public MapImage getAreaMap() {
		return CSEPUtils.getGeographicalMap(base[0], base[1], 
				 base[0]+delta[0]*(dimlength[0]-1), base[1] + delta[1]*(dimlength[1]-1));
	}

	@Override
	public MapImage getEventMap() {
		MapImage ret = getAreaMap();
		
		for (int i = 0; i < dimlength[0]; i++)
			for (int j = 0; j < dimlength[1]; j++)
				if (bins[i][j] > 0)		
					ret.drawRectangle(base[0]+i*delta[0], 
									  base[1]+j*delta[1], 
									  base[0]+(i+1)*delta[0], 
									  base[1]+(j+1)*delta[1], 
									  new Color(1f,0f,0f,(bins[i][j]+0.9f)/(maxevents+1f)));
		return ret;
	}
	
	public int getTotalEvents()
	{
		return totalevents;
	}
	
}
