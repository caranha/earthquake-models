package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import java.awt.Color;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPUtils;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
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
	}

	/**
	 * resets the values of all bins to 0
	 */
	public void clearBins()
	{
		bins = new int[dimlength[0]][dimlength[1]];
		totalevents = 0;
	}
	
	public String toString()
	{
		String ret = "Simple Geographical Model:";
		ret = ret + " "+(dimlength[0])+" cols, "+(dimlength[1])+" rows,";
		ret = ret + " total events: "+totalevents+".";
		return ret;
	}

	@Override
	public void addData(DataList d) {
		double maxlon = base[0] + delta[0]*(dimlength[0]-1);
		double maxlat = base[1] + delta[1]*(dimlength[1]-1);
		
		for (DataPoint aux: d)
		{
			if (aux.longitude >= base[0] && aux.longitude <= maxlon &&
				aux.latitude >= base[1] && aux.latitude <= maxlat)
			{
				int xindex = (int) Math.round((aux.longitude - base[0])/delta[0]);
				int yindex = (int) Math.round((aux.latitude - base[1])/delta[1]);
				
				bins[xindex][yindex] += 1;
				totalevents++;
			}
		}
	}

	@Override
	public void initRandom(int eventN) {
		Random dice = new Random(); // FIXME: use a seeded random here;
		
		for (int i = 0; i < eventN; i++)
		{
			bins[dice.nextInt(dimlength[0])][dice.nextInt(dimlength[1])] += 1;
			totalevents++;
		}
	}

	@Override
	public float calculatelogLikelihood(CSEPModel comp) {
		// TODO Auto-generated method stub
		return 0;
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
					ret.drawEvent(base[0] + delta[0]*i, base[1]+delta[1]*j, Color.red, bins[i][j]*10);
		return ret;
	}
	
}
