package jp.ac.tsukuba.cs.conclave.earthquake.faultmodel;

import java.util.Iterator;

import org.joda.time.DateTime;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.GeoUtils;

/**
 * Defines one Fault model that can be analysed.
 * 
 * @author caranha
 *
 */
public class FaultModel {

	//Event that is related to this model. The event includes 
	//model data, but we should ignore it for now.
	DataPoint event; 

	// Model parameters
	int strike;
	int dip;
	int rake;

	// contains the four points forming the fault plane. These are 
	// calculated from the earthquake origin and the depth/strike/dip
	// ATTENTION! Plane[i][0] is LONGITUDE!
	// ATTENTION! Plane[i][1] is LATITUDE!
	double plane[][];
	
	/** 
	 * initializes an empty FaultModel -- this can't be used directly
	 */
	public FaultModel()
	{
		event = null;
		plane = new double[4][3];
	}
	
	/**
	 * Initializes a Fault model and sets its default data
	 * @param src
	 * @param model
	 */
	public FaultModel(DataPoint src, int model, double dist)
	{
		event = null; 
		plane = new double[4][3];
		setData(src,model,dist);
	}
	
	/**
	 * Changes the data for the Fault Model
	 * @param src
	 * @param model
	 */
	public void setData(DataPoint src, int model, double dist)
	{
		if (!src.FM)
		{
			System.err.println("FaultModel java: Error - tried to create a model from a non-FM entry");
			return;
		}
			
		event = new DataPoint(src);
		strike = event.S[model];
		dip = event.D[model];
		rake = event.R[model];
		
		calculatePlane(dist);
	}
	
	
	/**
	 * Calculate the fault plane based on the event data;
	 */
	void calculatePlane(double radius)
	{
		double sdist, ddist;
		
		// calculate distance to surface
		// surface distance = depth/tan(D) 
		sdist = event.depth/Math.tan(Math.toRadians(dip)); // TODO: throw an exception if D = 0
		
		// calculate distance to max depth (20km or event depth
		if (event.depth < 20)
			ddist = sdist*(20/event.depth) - sdist; // if depth < 20, calculate dist to depth = 20
		else
			ddist = 1; // else, use a minimal depth;
				
		
		// calculate the plane distance along the strike, based on magnitude
		//fdist = GeoUtils.getAftershockRadius(event.magnitude);
		
		// calculate top and bottom midpoints. Top is 0, bottom is 1.
		double[][] midpoint = new double[2][2];
		
		midpoint[0] = GeoUtils.calcPointFromDirectionDistance(event.longitude, event.latitude, ((strike-90)+360)%360, sdist);
		midpoint[1] = GeoUtils.calcPointFromDirectionDistance(event.longitude, event.latitude, ((strike+90)+360)%360, ddist);
		
		
		// Calculate the four points in the plane:
		double[] tmp = new double[2];
		tmp = GeoUtils.calcPointFromDirectionDistance(midpoint[0][0], midpoint[0][1], strike, radius);
		plane[0][0] = tmp[0];
		plane[0][1] = tmp[1];
		plane[0][2] = 0;
		
		tmp = GeoUtils.calcPointFromDirectionDistance(midpoint[0][0], midpoint[0][1], (strike+180)%360, radius);
		plane[1][0] = tmp[0];
		plane[1][1] = tmp[1];
		plane[1][2] = 0;
		
		tmp = GeoUtils.calcPointFromDirectionDistance(midpoint[1][0], midpoint[1][1], (strike+180)%360, radius);
		plane[2][0] = tmp[0];
		plane[2][1] = tmp[1];
		plane[2][2] = event.depth*2; // TODO: Depth is not necessarily double depth!
		
		tmp = GeoUtils.calcPointFromDirectionDistance(midpoint[1][0], midpoint[1][1], strike, radius);
		plane[3][0] = tmp[0];
		plane[3][1] = tmp[1];
		plane[3][2] = event.depth*2; // TODO: Depth is not necessarily double depth!
		
	}
	
	@Override
	public String toString()
	{
		String ret = "[Fault Model]\n";
		ret += "  Event: "+event.getEventString()+"\n";
		ret += "  Model: "+"S"+strike+", D"+dip+", R"+rake+"\n";
		ret += "  Plane: \n"+dumpPlane();
		
		return ret;
	}
	
	public String dumpPlane()
	{
		String ret = "";
		ret += "  0: "+plane[0][0]+", "+plane[0][1]+", "+ plane[0][2]+"\n";
		ret += "  1: "+plane[1][0]+", "+plane[1][1]+", "+ plane[1][2]+"\n";
		ret += "  2: "+plane[2][0]+", "+plane[2][1]+", "+ plane[2][2]+"\n";
		ret += "  3: "+plane[3][0]+", "+plane[3][1]+", "+ plane[3][2]+"\n";
		return ret;
	}
	
	public double[][] getPlane()
	{
		return plane;
	}
	
	public double getLat()
	{
		return event.latitude;
	}
	public double getLon()
	{
		return event.longitude;
	}
	
	/**
	 * Get a list of points that can be pluged into R or other csv reader
	 * @param xy 0- get longitude, 1- get latitude
	 * @return
	 */
	public String getRPoints(int xy)
	{
		String ret = "";
		double tmp[] = new double[2];
		tmp[0] = event.longitude;
		tmp[1] = event.latitude;
		
		ret += tmp[xy];
		for(int i = 0; i < 4; i++)
			ret+= ","+plane[i][xy];
		
		return ret;		
	}
	
	/** 
	 * @return the time when the event happens
	 */
	public DateTime getTime()
	{
		return event.time;
	}
	
	public Double getMag()
	{
		return event.magnitude;
	}
	
	/** 
	 * Calculates if an event is located in the plane defined by this Fault Model
	 * 
	 * @param p event to be checked
	 * @return true is the event is located inside the plane, false if not;
	 */
	public boolean isEventInPlane(DataPoint p)
	{
		return GeoUtils.isPointInPlane(p.latitude, p.longitude, plane);
	}
	
	/**
	 * Creates a new list of events that are located inside the plane, from the 
	 * original pp list.
	 * 
	 * @param pp Original list of events
	 * @return a new list of events where every event is inside the plane
	 */
	public DataList pointsInPlane(DataList pp)
	{
		DataList ret = new DataList();
		Iterator<DataPoint> it = pp.data.iterator();
		while(it.hasNext())
		{
			DataPoint curr = it.next();
			if (isEventInPlane(curr))
				ret.addData(new DataPoint(curr));
		}		
		return ret;
	}
	
	
	/**
	 * Calculates the distance between the point p, and the fault defined by this fault model.
	 * Returns -1 if the point is on the left side of the fault (above the surface)
	 * 
	 * This calculations is made as 
	 * FD = cos(D)*((tan(D)*sdist)-AD)
	 * 
	 * sdist is the surface distance between the point and the fault line at surface
	 * AD is the depth of the point
	 * 
	 * @param p
	 * @return
	 */
	public double calcFaultDistance(DataPoint p)
	{
		// 1- Calculate the crosstrack distance. If negative, return -1;
		double crosstrack = GeoUtils.crossTrackDistance(plane[1][1], plane[1][0], strike, p.latitude, p.longitude);		
		if (crosstrack < 0)
			return -1; // point on the wrong side of the fault
		
		// 2- If Ctrosstrack distance is not negative, calculate DF
		double DF = Math.cos((Math.tan(dip)*crosstrack)-p.depth);
		
		return DF; //Beyond Quality!		
	}
	
}
