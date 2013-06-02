package jp.ac.tsukuba.cs.conclave.earthquake;


import java.io.Console;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.ac.tsukuba.cs.conclave.earthquake.FMtest.FMTester;
import jp.ac.tsukuba.cs.conclave.earthquake.RI.RIModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

public class TestMain {

	
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.FINER);
		
		DataList total = new DataList();
		DataList fnet = new DataList();

		total.loadData("data/jma_cat_2000_2012_Mth2.5_formatted.dat","jma");
		total.loadData("data/catalog_fnet_1997_20130429_f3.txt","fnet");
		fnet.loadData("data/catalog_fnet_1997_20130429_f3.txt","fnet");
		

		// Getting a List of points of desired Magnitude
		ArrayList<Integer> magPoints = fnet.getEventsByMagnitude(5, 6);
	    DataPoint centralPoint = fnet.data.get(magPoints.get(4));
		int timewindow = 10;
		
		FMTester fmtester = new FMTester();		
		fmtester.init(centralPoint, total, timewindow);

		
	}
	
	
	static int[] frequencyArray(double minMag, RIModel r)
	{
		int[] ret = new int[r.getHighCount(minMag)+1];
		for (int i = 0; i < r.boxsizeX; i++)
			for (int j = 0; j < r.boxsizeY; j++)
				ret[r.eventgrid[i][j].getEvents(minMag)]++;
		return ret;
	}
}
