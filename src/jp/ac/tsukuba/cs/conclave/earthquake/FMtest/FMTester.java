package jp.ac.tsukuba.cs.conclave.earthquake.FMtest;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Months;

import jp.ac.tsukuba.cs.conclave.earthquake.GeoUtils;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/***
 * This package recieves a candidate event, which must include FM models.
 * It also receives a 
 * 
 * @author caranha
 *
 */



public class FMTester {
	private final static Logger logger = Logger.getLogger(FMTester.class.getName());
	
	static int MODEL_N = 2;
	static int RADIUS_RATE = 2;
	
	Duration timelimit; /* Number of days to consider for this tester */	
	DataPoint origin;
	DataList aftershocks;
	int model[][]; // candidate models
	double radius; // Radius for events
	
	
	public FMTester()
	{
		logger.setLevel(Level.FINER);
		model = new int[2][3]; // two models, three attributes (S,D,R)
		origin = null;
		aftershocks = null;
		timelimit = null;
	}
	
	public boolean init(DataPoint o, DataList origdata, int days)
	{
		if (o.FM == false)
		{
			logger.warning("FMTester.init() received a data point without FM data!");
			return false;
		}
		
		origin = new DataPoint(o);
		aftershocks = new DataList();
		timelimit = Duration.standardDays(days);
		
		/* Calculating aftershock radius. This radius is taken from 
		 * eq 9 of Helmstetter et al., JGR, 2005 
		 * 
		 * NOTE: This gives very low results:
		 * Mag 7: 70km
		 * Mag 6: 30km
		 * Mag 5: 10km
		 * Mag 4: 5km
		 * 
		 * Feels a bit too small for under 7 earthquakes
		 */
		radius = 0.01*Math.pow(10, (0.5*origin.magnitude));
		radius = RADIUS_RATE*radius;
		
		logger.info("Origin Information:\n Magnitude: "+origin.magnitude+"  Selected Radius: "+radius);
		
		/* Setting the base models */
		for (int i = 0; i < MODEL_N; i++)
		{
			model[i][0] = origin.S[i];
			model[i][1] = origin.D[i];
			model[i][2] = origin.R[i];
		}
		
		Iterator<DataPoint> it = origdata.data.iterator();	    
	    while (it.hasNext())
		{
			DataPoint dt = it.next(); /* Zeratul */
			if (dt.time.isAfter(origin.time) && 
				dt.time.isBefore(origin.time.plus(timelimit)) &&
			    GeoUtils.haversineDistance(dt.latitude, dt.longitude, 
			    						   origin.latitude, origin.longitude) < radius)
			{
				aftershocks.addData(dt);
			}
		}
	    logger.info("Added "+aftershocks.data.size()+" events, based on time and strength");
		
		return true;
	}
	
	
	
}
