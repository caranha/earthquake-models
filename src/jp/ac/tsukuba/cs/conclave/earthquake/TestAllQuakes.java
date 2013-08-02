package jp.ac.tsukuba.cs.conclave.earthquake;

import java.util.Iterator;

import org.joda.time.Days;
import org.joda.time.ReadableInstant;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.GeoUtils;

/***
 * This Class tests all the quakes from a data file at once, against all known hypothesis. 
 * Data for each valid earthquake is saved on a "earthquakes" file, while 
 * 
 * 
 * @author caranha
 *
 */
public class TestAllQuakes {

	// Parameters: 
	// TODO: Read these from a parameter file
	// TODO: Modify the file reader object to automagically detect the type

	static double minMag = 6; // Minimum magnitude for selecting an event
	static int minAftershock = 10; // Minimum number of aftershocks after a day for selecting an event
	static double minAfterShockMag = 2; // Minimum magnitude for selecting an aftershock
	static 	String[] datafiles = {"jma_cat_2000_2012_Mth2.5_formatted.dat","jma",
		"catalog_fnet_1997_20130429_f3.txt","fnet"}; // file, type, file, type
	static Days minAfterShockTime = Days.ONE;
	
	public static void main(String[] args) {

		DataList data = new DataList();
		DataList QuakesOfInterest = new DataList();
		DataList AfterShocks = new DataList();
		
		for (int i = 0; i < datafiles.length; i+=2) // reading data
		{
			data.loadData(datafiles[i],datafiles[i+1]);
		}

		int total = 0;
		for (int i = 0; i < data.size(); i++)
		{
			DataPoint curr = data.data.get(i);
			if (curr.FM == true && curr.magnitude >= minMag) // Filtering: FM Quake, Magnitude
			{
				// Getting Aftershocks;
				AfterShocks.clear();
				int j = i;
				
				// testing the time period to get aftershocks
				while (j < data.size() && data.data.get(j).time.isBefore(curr.time.plus(minAfterShockTime)))
				{
					DataPoint after = data.data.get(j);
					if (after.magnitude >= minAfterShockMag && 
							GeoUtils.getAftershockRadius(curr.magnitude) >= GeoUtils.haversineDistance(curr.latitude, curr.longitude, after.latitude, after.longitude))	
					{
						AfterShocks.addData(new DataPoint(after));
					}
					j++;
				}
				
				if (AfterShocks.size() >= minAftershock) 
				{
					// This earthquake has the minimum required number of aftershocks. Now we test it.
					// For each quake in the files test:
					// If it is an FM quake
					// If it is above the minimum magnitude
					// Make a list of aftershocks for 1, 2, 3 days (taking into account minimum aftershocks)
					// If the 1 day list is above the minimum size, 
					
					total ++;
				}
			}
		}
		
		System.out.println(total);

	}

}
