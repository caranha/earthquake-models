package jp.ac.tsukuba.cs.conclave.earthquake;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public class RawDataPoint {
	public double longitude;
	public double latitude;
		
	public double magnitude;
	public double depth; 
	
	DateTime time; // time when the event happened
	
	/*
	 * Create a new data point based on the normal string format
	 * "Long Lat Y M D Mag Dep H M S"
	 * M, D, H, M, S start on 1
	 * 
	 * FIXME: assumes no missing data
	 */
	RawDataPoint(String s)
	{
		// Removing the initial space;
		int i;
		for (i = 0; s.charAt(i) == ' '; i++);
		s = s.substring(i);
		
		String token[] = s.split(" +");
				
		longitude = Double.parseDouble(token[0]);
		latitude = Double.parseDouble(token[1]);
		magnitude = Double.parseDouble(token[5]);
		depth = Double.parseDouble(token[6]);
		
		time = new DateTime(
				Integer.parseInt(token[2]), 
				Integer.parseInt(token[3]),
				Integer.parseInt(token[4]), 
				Integer.parseInt(token[7]), 
				Integer.parseInt(token[8]), 
				(int) Math.floor(Double.parseDouble(token[9])));		
	}
	
	public String dump()
	{
		String ret = "";
		ret = ret + "Location "+longitude+","+latitude+", ";
		ret = ret + "M"+magnitude+" Depth: "+depth+", ";
		ret = ret + DateFormat.getInstance().format(time);
		
		return ret;
	}
}
