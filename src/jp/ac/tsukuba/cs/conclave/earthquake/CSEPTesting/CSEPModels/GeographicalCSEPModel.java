package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPUtils;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;


/** 
 * This class stores a simple latitude-longitude CSEP model.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class GeographicalCSEPModel extends CSEPModel {

	// Array of events;
	int[][] bins;
	int totalevents;
	int maxevents; // bin with maximum number of events
	
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
	
	public String toString()
	{
		String ret = "Simple Geographical Model:";
		ret = ret + " "+(dimlength[0])+" cols, "+(dimlength[1])+" rows,";
		ret = ret + " total events: "+totalevents+".";
		return ret;
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
				try {
					ret.drawRectangle(base[0]+i*delta[0], 
									  base[1]+j*delta[1], 
									  base[0]+(i+1)*delta[0], 
									  base[1]+(j+1)*delta[1], 
									  new Color(1f,0f,0f,(bins[i][j]+0.9f)/(maxevents+1f)));
				} catch (IllegalArgumentException e)
				{
					System.err.println(bins[i][j] + " max:" +maxevents);
					System.exit(1);
				}
		return ret;
	}
	
	@Override
	public int getTotalEvents()
	{
		return totalevents;
	}

	@Override
	public Iterator<Integer> iterator() {
		ArrayList<Integer> ret = new ArrayList<Integer>(dimlength[0]*dimlength[1]);
		for (int i = 0; i < dimlength[0]; i++)
			for (int j = 0; j < dimlength[1]; j++)
				ret.add(bins[i][j]);
		return ret.iterator();
	}

	@Override
	public int getTotalBins() {
		return dimlength[0]*dimlength[1];
	}

	@Override
	public int getTotalLatBins() {
		return dimlength[1];
	}

	@Override
	public int getTotalLonBins() {
		return dimlength[0];
	}

	@Override
	public int getTotalMagBins() {
		return 1;
	}

	// TODO: check for invalid parameters;
	@Override
	public int getEventsFromBin(int lonbin, int latbin, int magbin) {		
		return bins[lonbin][latbin];
	}

	@Override
	public int getEventsFromBin(int lonbin, int latbin) {
		return bins[lonbin][latbin];
	}
}