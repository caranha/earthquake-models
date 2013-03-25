package jp.ac.tsukuba.cs.conclave.earthquake;

import java.util.Calendar;
import java.util.Date;

public class RawDataPoint {
	public double longitude;
	public double latitude;
		
	public double magnitude;
	public double depth; 
	
	Date time; // time when the event happened
	
	/*
	 * Create a new data point based on the normal string format
	 * "Long Lat Y M D Mag Dep H M S"
	 * M, D, H, M, S start on 1
	 * 
	 * FIXME: assumes no missing data
	 */
	RawDataPoint(String s)
	{
		String token[] = s.split(" ");
		longitude = Double.parseDouble(token[0]);
		latitude = Double.parseDouble(token[1]);
		magnitude = Double.parseDouble(token[5]);
		depth = Double.parseDouble(token[6]);
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(token[2]), 
				Integer.parseInt(token[3]), 
				Integer.parseInt(token[4]), 
				Integer.parseInt(token[7]), 
				Integer.parseInt(token[8]), 
				(int) Math.round(Double.parseDouble(token[9])));
		time = c.getTime();
	}
	
}
