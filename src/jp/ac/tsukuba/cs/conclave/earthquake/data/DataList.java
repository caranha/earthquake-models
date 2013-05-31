/***
 * This class takes all the data from a formatted file, and stores it.
 * It performs some simple, useful operations
 */

package jp.ac.tsukuba.cs.conclave.earthquake.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import org.joda.time.DateTime;

public class DataList {
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
	 * Loads data from a catalog file.
	 * Data type can be:
	 * "JMA" - long, lat, Y,M,D, Mag, Depth, H, min, sec
	 * "Fnet" - Y,M,D,H,min,sec lat,long, Depth, M, S1, S2, D1, D2, R1, R2
	 * 
	 * @param filename
	 */
	public void loadData(String filename, String datatype)
	{
		
		boolean moredata = !data.isEmpty();
		BufferedReader reader = null;
		int lines_read = 0;
		
		try
		{	
			reader = new BufferedReader(new FileReader(new File(filename)));
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
}
