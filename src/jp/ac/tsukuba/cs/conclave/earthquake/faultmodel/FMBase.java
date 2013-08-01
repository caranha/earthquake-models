package jp.ac.tsukuba.cs.conclave.earthquake.faultmodel;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.Duration;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.GeoUtils;


/**
 * This base stores the basic information needed to calculate statistical information for 
 * FM models. In other words, from an event and a time duration, it calculate and stores 
 * the probable aftershock events. 
 * 
 * @author caranha
 *
 */



public class FMBase {
	private final static Logger logger = Logger.getLogger(FMBase.class.getName());
	
	static int MODEL_N = 2;
	static int RADIUS_RATE = 2;
	
	DataPoint origin;
	DataList aftershocks;
	int model[][]; // candidate models
	double radius; // Radius for events
	
	
	public FMBase()
	{
		logger.setLevel(Level.FINER);
		model = new int[2][3]; // two models, three attributes (S,D,R)
		origin = null;
		aftershocks = null;
	}
	
	public boolean init(DataPoint o)
	{
		if (o.FM == false)
		{
			logger.warning("FMTester.init() received a data point without FM data!");
			return false;
		}
		
		origin = new DataPoint(o);
		aftershocks = new DataList();
		radius = RADIUS_RATE*getAftershockRadius(origin.magnitude);		

		logger.info("Origin Information:\n Magnitude: "+origin.magnitude+"  Selected Radius: "+radius);
		
		/* Setting the base models */
		for (int i = 0; i < MODEL_N; i++)
		{
			model[i][0] = origin.S[i];
			model[i][1] = origin.D[i];
			model[i][2] = origin.R[i];
		}		
		
		return true;
	}
	
	/**
	 * Calculates the aftershock radius based on eq.9 from Helmstetter et al., JGR, 2005.
	 * Average results with standard parameters are:
	 * 
	 * Mag 7: 70km
	 * Mag 6: 30km
	 * Mag 5: 10km
	 * Mag 4: 5km
	 * 
	 * @param magnitude
	 * @return the radius to consider aftershocks in kilometers.
	 */
	public static double getAftershockRadius(double magnitude)
	{
		return	0.01*Math.pow(10, (0.5*magnitude));
	}
	
	
	/**
	 * Fills the "AfterShocks" field based on a list of events and JODA time duration.
	 * This function appends to the current list of Aftershocks
	 * 
	 * @param origdata a DataList with the events that we want to search for AfterShocks
	 * @param days the number of days
	 * @return the current number of events in the AfterShocks structure;
	 */
	public int fillAfterShockList(DataList origdata, Duration timeLimit)
	{
		Iterator<DataPoint> it = origdata.data.iterator();	    
	    while (it.hasNext())
		{
			DataPoint dt = it.next(); /* Zeratul */
			if (dt.time.isAfter(origin.time) && 
				dt.time.isBefore(origin.time.plus(timeLimit)) &&
			    GeoUtils.haversineDistance(dt.latitude, dt.longitude, 
			    						   origin.latitude, origin.longitude) < radius)
			{
				aftershocks.addData(dt);
			}
		}
	    logger.info("Added "+aftershocks.data.size()+" events, based on time and strength");
	    return aftershocks.size();
	}
	
	public void clearAfterShockList()
	{
		aftershocks.clear();
	}
	
	public DataList getAfterShockList()
	{
		return aftershocks;
	}
}
