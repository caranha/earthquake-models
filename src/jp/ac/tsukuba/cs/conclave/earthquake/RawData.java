/***
 * This class takes all the data from a formatted file, and stores it.
 * It performs some simple, useful operations
 */

package jp.ac.tsukuba.cs.conclave.earthquake;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.joda.time.DateTime;

public class RawData {
	public ArrayList<RawDataPoint> data;

	int linecount;
	
	public double minlong,maxlong;
	public double minlat,maxlat;
	public DateTime mindate, maxdate;
	public double mindepth,maxdepth;
	public double minmag,maxmag;
	
	public RawData()
	{
		data = new ArrayList<RawDataPoint>();
	}

	
	public void loadData(String filename)
	{
		BufferedReader reader = null;
		int line_count = 0;
		
		try
		{	
			reader = new BufferedReader(new FileReader(new File(filename)));
		}
		catch (Exception e)
		{
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}

		try 
		{
			String line;			
			while ((line = reader.readLine()) != null)
			{
				RawDataPoint dt = new RawDataPoint(line); // passes one line to the data point parser
				if (dt != null) //Zeratul exists
				{

					data.add(dt);
					if (line_count == 0)
					{
						minlong = maxlong = dt.longitude;
						minlat = maxlat = dt.latitude;
						mindate = maxdate = dt.time;
						minmag = maxmag = dt.magnitude;
						mindepth = maxdepth = dt.depth;
					}
					else
					{
						minmag = (dt.magnitude < minmag?dt.magnitude:minmag);
						maxmag = (dt.magnitude > maxmag?dt.magnitude:maxmag);
						mindepth = (dt.depth < mindepth?dt.depth:mindepth);
						maxdepth = (dt.depth > maxdepth?dt.depth:maxdepth);
						minlat = (dt.latitude < minlat?dt.latitude:minlat);
						maxlat = (dt.latitude > maxlat?dt.latitude:maxlat);
						minlong = (dt.longitude < minlong?dt.longitude:minlong);
						maxlong = (dt.longitude > maxlong?dt.longitude:maxlong);
						mindate = (dt.time.isBefore(mindate)?dt.time:mindate);
						maxdate = (dt.time.isAfter(maxdate)?dt.time:maxdate);
					}
					line_count++;
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("Error reading lines: " + e.getMessage());
			System.exit(1);
		}
		Logger l = Logger.getInstance();

		l.log("Read "+line_count+" lines from "+filename);
		linecount = line_count;
	}
}
