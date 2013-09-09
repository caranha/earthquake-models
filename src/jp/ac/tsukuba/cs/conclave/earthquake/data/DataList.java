/***
 * This class takes all the data from a formatted file, and stores it.
 * It performs some simple, useful operations
 */

package jp.ac.tsukuba.cs.conclave.earthquake.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class DataList {
	
	// FIXME: ArrayList data needs to be made private
	public ArrayList<DataPoint> data;
	private final static Logger logger = Logger.getLogger(DataList.class.getName());

	int linecount;
	
	public double minlong,maxlong;
	public double minlat,maxlat;
	public DateTime mindate, maxdate;
	public double mindepth,maxdepth;
	public double minmag,maxmag;
	
	public DataList()
	{
		data = new ArrayList<DataPoint>();
		linecount = 0;
	}
	
	/**
	 * Resets the DataList and clears all data.
	 */
	public void clear()
	{
		data.clear();
		linecount = 0;
		minlong = maxlong = minlat = maxlat = mindepth = maxdepth = minmag = maxmag = 0;
		mindate = maxdate = null;
	}
	
	/**
	 * Appends one event to the end of the Datalist. Does not sort the DataList
	 * @param d
	 */
	public void addData(DataPoint d)
	{
		data.add(d);
		if (linecount == 0)
		{
			minlong = maxlong = d.longitude;
			minlat = maxlat = d.latitude;
			mindate = maxdate = d.time;
			minmag = maxmag = d.magnitude;
			mindepth = maxdepth = d.depth;
		}
		else
		{
			minmag = (d.magnitude < minmag?d.magnitude:minmag);
			maxmag = (d.magnitude > maxmag?d.magnitude:maxmag);
			mindepth = (d.depth < mindepth?d.depth:mindepth);
			maxdepth = (d.depth > maxdepth?d.depth:maxdepth);
			minlat = (d.latitude < minlat?d.latitude:minlat);
			maxlat = (d.latitude > maxlat?d.latitude:maxlat);
			minlong = (d.longitude < minlong?d.longitude:minlong);
			maxlong = (d.longitude > maxlong?d.longitude:maxlong);
			mindate = (d.time.isBefore(mindate)?d.time:mindate);
			maxdate = (d.time.isAfter(maxdate)?d.time:maxdate);
		}
		linecount++;
	}

	
	/**
	 * Loads data from a cif (curr.FM == true && curr.magnitude >= minMag) atalog file.
	 * Data type can be:
	 * "JMA" - long, lat, Y,M,D, Mag, Depth, H, min, sec
	 * "Fnet" - Y,M,D,H,min,sec lat,long, Depth, M, S1, S2, D1, D2, R1, R2
	 * 
	 * @param filename
	 */
	public void loadData(String filename, String datatype)
	{
		/*
		 * FIXME: Adding some black magic to load from assets folder. Should probably
		 * change this so that the data is loaded from assets folder if it can't be found externally
		 */
		
		boolean moredata = !data.isEmpty();
		BufferedReader reader = null;
		int lines_read = 0;
		
		try
		{	
			reader = new BufferedReader(new FileReader(new File(filename))); // read from filesystem
			//reader = new BufferedReader(new InputStreamReader(
			//		Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)));
			// BLACK MAGIC!
		}
		catch (Exception e)
		{
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}

		
		try {
			String line;			
			while ((line = reader.readLine()) != null)
				if (!line.startsWith("#")) {
					DataPoint dt = new DataPoint(line,datatype); // passes one line to the data point parser
					if (dt != null) //Zeratul exists
					{
						addData(dt);
						lines_read++;
					}
				}
			reader.close();
		}
		catch (Exception e)
		{
			System.err.println("Error reading lines: " + e.getMessage());
			System.exit(1);
		}

		logger.info("Read "+lines_read+" lines from "+filename);		
		if (moredata)
		{
			Collections.sort(data);
		}
	}

	/** 
	 * @return Returns the number of events in this DataList
	 */
	public int size()
	{
		return data.size();
	}
	
	/**
	 * This function creates a list of indexes in this data set, 
	 * with earthquake events within the specified magnitudes.
	 * 
	 * @param min Events have magnitude equal or higher than this
	 * @param max Events have magnitude equal or lower than this
	 * @return ArrayList of Integers with indexes to all events that fit the above conditions
	 */
	public ArrayList<Integer> getEventsByMagnitude(double min, double max)
	{
		ArrayList<Integer> ret = new ArrayList<Integer>();

		for (int i = 0; i < data.size(); i++)
		{
			double tmag = data.get(i).magnitude;
			if (tmag >= min && tmag <= max)
				ret.add(i);				
		}
		
		return ret;		
	}
	
	/**
	 * Creates a sublist with the events that fall in this interval
	 * @param start
	 * @param end
	 * @return
	 */
	public DataList getEventsInInterval(DateTime start, DateTime end)
	{
		DataList ret = new DataList();
		Iterator<DataPoint> it = data.iterator();
		while(it.hasNext())
		{
			DataPoint next = it.next();
			if (start.isBefore(next.time) && end.isAfter(next.time))
				ret.addData(new DataPoint(next));
			
			if (!end.isAfter(next.time)) // got to end of time, break away
				break;
		}
		return ret;
	}
	
	/** 
	 * Getter for the internal iterator
	 * @return
	 */
	public Iterator<DataPoint> iterator()
	{
		return data.iterator();
	}
}





