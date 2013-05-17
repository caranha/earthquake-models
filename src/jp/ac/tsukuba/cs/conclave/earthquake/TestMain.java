package jp.ac.tsukuba.cs.conclave.earthquake;


import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.ac.tsukuba.cs.conclave.earthquake.RI.RIModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

public class TestMain {

	
	
	public static void main(String[] args) {
		DataList r = new DataList();
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.INFO);

		r.loadData("data/jma_cat_2000_2012_Mth2.5_formatted.dat","jma");
		r.loadData("data/catalog_fnet_1997_20130429_f3.txt","fnet");

		//System.out.println(r.data.get(0).time.toString());
		
		Iterator<DataPoint> it = r.data.iterator();
		
		DataPoint pold = null;
		DataPoint pnew = null;
		while (it.hasNext())
		{
			pnew = it.next();
			if (pold != null)
			{
				if (pold.compareTo(pnew) == 0 && (pold.FM!=pnew.FM))
				{
					DataPoint JMA = (pold.FM == true?pnew:pold);
					DataPoint Fnet = (pold.FM == true?pold:pnew);
					
					
					System.out.println(pnew.time.toString()+": "+JMA.longitude+" "+JMA.latitude+" "+JMA.magnitude+" "+Fnet.longitude+" "+Fnet.latitude+" "+Fnet.magnitude);
				}
			}
			
			pold = pnew;
		}
		
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
