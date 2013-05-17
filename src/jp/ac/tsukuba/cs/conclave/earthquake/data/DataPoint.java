package jp.ac.tsukuba.cs.conclave.earthquake.data;

import java.text.DateFormat;
import java.util.logging.Logger;

import org.joda.time.DateTime;

public class DataPoint implements Comparable {

	private final static Logger logger = Logger.getLogger(DataPoint.class.getName());
	
	public double longitude;
	public double latitude;
		
	public double magnitude;
	public double depth; 
	
	public DateTime time; // time when the event happened
	
	public boolean FM; // this data point has Focus Mechanism information
	
	// Focus mechanism data
	public int S[];
	public int D[];
	public int R[];
	
	/* Token Index is the position of data in each data file type.
	 * Token order: Y,    M,   D, H, min, sec, long,  lag,  Mag,   Depth
	 * "JMA" -      long, lat, Y, M, D,   Mag, Depth, H   , min,   sec
	 * "Fnet" -    Y,    M,   D, H, min, sec, lat,   long, Depth, Mag, S1, S2, D1, D2, R1, R2
	 */
	private static int[][] ti = 
		{
			{2,3,4,7,8,9,0,1,5,6},//jma
			{0,1,2,3,4,5,7,6,9,8}//fnet
		};
	
	
	
	DataPoint(String s, String t)
	{
		// Removing the initial space;
		int i;
		int type;
		for (i = 0; s.charAt(i) == ' '; i++);
		s = s.substring(i);		
		FM = false;
		
		if (t.equalsIgnoreCase("JMA"))
			type = 0;
		else if	 (t.equalsIgnoreCase("fnet"))
			type = 1;
		else
		{
			logger.severe("Undefined Data Type passed: "+t);
			return;
		}
		
		
		String token[] = s.split(" +");

		time = new DateTime(
				Integer.parseInt(token[ti[type][0]]), 
				Integer.parseInt(token[ti[type][1]]),
				Integer.parseInt(token[ti[type][2]]), 
				Integer.parseInt(token[ti[type][3]]), 
				Integer.parseInt(token[ti[type][4]]), 
				(int) Math.floor(Double.parseDouble(token[ti[type][5]])));		
		
		longitude = Double.parseDouble(token[ti[type][6]]);
		latitude = Double.parseDouble(token[ti[type][7]]);

		magnitude = Double.parseDouble(token[ti[type][8]]);
		depth = Double.parseDouble(token[ti[type][9]]);
		
		if ((t.equalsIgnoreCase("fnet")))
		{
			S = new int[2];
			D = new int[2];
			R = new int[2];
			FM = true;
			S[0] = Integer.parseInt(token[10]);
			S[1] = Integer.parseInt(token[11]);
			D[0] = Integer.parseInt(token[12]);
			D[1] = Integer.parseInt(token[13]);
			R[0] = Integer.parseInt(token[14]);
			R[1] = Integer.parseInt(token[15]);
		}


	}
	
	/**
	 * Copy constructor
	 * @param o
	 */
	public DataPoint(DataPoint o) {
		longitude = o.longitude;
		latitude = o.latitude;
		magnitude = o.magnitude;
		depth = o.depth;
		time = new DateTime(o.time);
		FM = o.FM;
		S = new int[2];
		D = new int[2];
		R = new int[2];
		for (int i = 0; i < 2; i++)
		{
			S[i] = o.S[i];
			D[i] = o.D[i];
			R[i] = o.R[i];
		}
	}

	public String dump()
	{
		String ret = "";
		ret = ret + "Location "+longitude+","+latitude+", ";
		ret = ret + "M"+magnitude+" Depth: "+depth+", ";
		ret = ret + DateFormat.getInstance().format(time);
		
		if (FM)
		{
			ret = ret + " FM1: "+S[0]+","+D[0]+","+R[0];
			ret = ret + " FM2: "+S[1]+","+D[1]+","+R[1];
		}
		return ret;
	}
	
	@Override
	public int compareTo(Object arg0) {
		DataPoint tmp = (DataPoint) arg0;
		
		return (time.compareTo(tmp.time));		
	}

	/**
	 * Calculates distance in meters from a geopoint, using latitude and longitude.
	 * 
	 * Code by Espen Herseth Halvorsen, from StackExchange
	 * 
	 * FIXME: maybe this doesn't take into account if the long/lat are negative or not? 
	 * I'm not sure how this is treated in the original data.
	 * 
	 * @param p -- another point to compare to
	 * @return -- distance in meters
	 */
	public double distFrom(DataPoint p) 
	{
		double lat1 = this.latitude; 
		double lng1 = this.longitude;
		double lat2 = p.latitude;
		double lng2 = p.longitude;
		
		double earthRadius = 3958.75; // radius in miles
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (double) (dist * meterConversion);
	    }
	
	
}
