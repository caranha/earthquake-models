package jp.ac.tsukuba.cs.conclave.earthquake;

import java.util.ArrayList;

import jp.ac.tsukuba.cs.conclave.earthquake.RI.RIModel;

public class TestMain {

	public static void main(String[] args) {
		RawData r = new RawData();
		//r.loadData("/home/caranha/Desktop/Work/Earthquake_bogdan/data/jma_cat_100l_test");
		r.loadData("/home/caranha/Desktop/Work/Earthquake_bogdan/data/jma_cat_2000_2012_Mth2.5_formatted.dat");

		RIModel tr = new RIModel(r);
	
		System.out.println(tr.boxsizeX + " " + tr.boxsizeY);
		System.out.println(tr.highestCount); // maximum number of events in a single grid
		
		int[] hist = frequencyArray(0,tr);		
		for (int i = 0; i < hist.length; i++)
			if (hist[i] > 0)
				System.out.println(i + " " + hist[i]);
		
		
	}
	
	
	static int[] frequencyArray(int minMag, RIModel r)
	{
		int[] ret = new int[r.highestCount+1];
		for (int i = 0; i < r.boxsizeX; i++)
			for (int j = 0; j < r.boxsizeY; j++)
				ret[r.eventgrid[i][j].getEvents(minMag)]++;
		return ret;
	}
}
