package jp.ac.tsukuba.cs.conclave.earthquake.RI;

import java.util.Iterator;

import jp.ac.tsukuba.cs.conclave.earthquake.RawData;
import jp.ac.tsukuba.cs.conclave.earthquake.RawDataPoint;

import org.joda.time.DateTime;

public class RIModel {

	public RIBox eventgrid[][];
	
	public double boxsize; // size, in degrees, of a box
	public double startboxX;
	public double startboxY;
	public int boxsizeX;
	public int boxsizeY;
	
	
	public DateTime initTime,endTime; // instants to begin and end the training of the model
	public double minMag; // minimum magnitude for events to be considered
	public double magDelta; // steps to consider the magnitude
	
	public int highestCount; // highest event count from all cells
	
	
	/**
	 * Constructor using default parameter values. Receives a training data set as input.
	 * @param d
	 */
	public RIModel(RawData d)
	{
		// setting the box size
		boxsize = 0.05;
		startboxX = d.minlong;
		startboxY = d.minlat;
		boxsizeX = (int) Math.ceil((d.maxlong - d.minlong)/boxsize);
		boxsizeY = (int) Math.ceil((d.maxlat - d.minlat)/boxsize);

		eventgrid = new RIBox[boxsizeX][boxsizeY];
		for (int i = 0; i < boxsizeX; i++)
			for (int j = 0; j < boxsizeY; j++)
				eventgrid[i][j] = new RIBox(boxsize);
		
		initTime = d.mindate;
		endTime = d.maxdate;		
		
		minMag = 2.5;
		magDelta = 0.1;
		
		highestCount = 0;
		
		readData(d);
	}
	
	/**
	 * Transforms a rawdata into grid event data. Used after the parameters have been set.
	 * @param d
	 */
	void readData(RawData d)
	{
		Iterator<RawDataPoint> it = d.data.iterator();
		while (it.hasNext())
		{
			RawDataPoint t = it.next();
			if (t.magnitude >= minMag)
			{
				int px = (int)Math.floor((t.longitude - startboxX)/boxsize);
				int py = (int)Math.floor((t.latitude - startboxY)/boxsize);
				eventgrid[px][py].addEvent(t.magnitude);
				if (eventgrid[px][py].eventCount[0] > highestCount)
					highestCount = eventgrid[px][py].eventCount[0];
			}
		}		
	}
}
