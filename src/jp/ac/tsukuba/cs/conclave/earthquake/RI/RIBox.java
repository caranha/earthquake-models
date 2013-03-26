/**
 * This Class defines a single cell in the RI model.
 * 
 * In the RI model, we are just interested in the number of earthquakes of a 
 * certain magnitude in a period, so this is all that we count.
 */

package jp.ac.tsukuba.cs.conclave.earthquake.RI;

import java.util.ArrayList;

import jp.ac.tsukuba.cs.conclave.earthquake.Logger;

public class RIBox {

	int[] eventCount; // counts the number of earthquakes for each magnitude of interest
	double deltaMag; // magnitude interval
	
	/**
	 * 
	 * @param dm variation between event magnitudes of interest. This gives us the number of intervals.
	 */
	public RIBox(double dm)
	{
		eventCount = new int[(int) Math.round(9/dm)+1]; //FIXME: check this +1
		deltaMag = dm;
	}
	
	public void addEvent(double Magnitude)
	{
		int index = (int) Math.floor(Magnitude/deltaMag) + 1;
		for (int i = 0; i < index; i++)
			eventCount[i]++;
	}
	
	/**
	 * Returns the number of events in this cell with magnitude equal or above minMag.
	 * @param minMag
	 * @return
	 */
	public int getEvents(double minMag)
	{
		return eventCount[(int) Math.floor(minMag/deltaMag)];
	}
	
	/* Returns the number of events stronger than minMag, but weaker than MaxMag */
	public int getEvents(double minMag, double maxMag)
	{
		if (minMag > maxMag)
		{
			Logger l = Logger.getInstance();
			l.log("Error counting events: Min is bigger than Max)");
			return -1;
		}
		
		int minindex = (int) Math.floor(minMag/deltaMag);
		int maxindex = (int) Math.floor(maxMag/deltaMag)+1;
		
		if (maxindex >= eventCount.length)
		{
			return eventCount[minindex]; 
		}
		else
		{
			return eventCount[minindex] - eventCount[maxindex];
		}
			
	}
	
}
