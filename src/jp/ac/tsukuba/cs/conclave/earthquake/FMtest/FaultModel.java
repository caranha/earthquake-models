package jp.ac.tsukuba.cs.conclave.earthquake.FMtest;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.joda.time.format.ISODateTimeFormat;

import jp.ac.tsukuba.cs.conclave.earthquake.GeoUtils;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

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
	public FaultModel(DataPoint src, int model)
	{
		event = null; 
		plane = new double[4][3];
		setData(src,model);
	}
	
	/**
	 * Changes the data for the Fault Model
	 * @param src
	 * @param model
	 */
	public void setData(DataPoint src, int model)
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
		
		calculatePlane();
	}
	
	
	/**
	 * Calculate the fault plane based on the event data;
	 */
	void calculatePlane()
	{
		
		// calculate distance to surface and max depth
		// surface distance = depth/tan(D) 
		double sdist = event.depth/Math.tan(dip); // TODO: throw an exception if D = 0
		
		// calculate top and bottom midpoints. Top is 0, bottom is 1.
		double[][] midpoint = new double[2][2];
		
		midpoint[0] = GeoUtils.calcPointFromDirectionDistance(event.longitude, event.latitude, ((strike-90)+360)%360, sdist);
		midpoint[1] = GeoUtils.calcPointFromDirectionDistance(event.longitude, event.latitude, ((strike+90)+360)%360, sdist);
		
		
		// Calculate the four points in the plane:
		double[] tmp = new double[2];
		tmp = GeoUtils.calcPointFromDirectionDistance(midpoint[0][0], midpoint[0][1], strike, sdist);
		plane[0][0] = tmp[0];
		plane[0][1] = tmp[1];
		plane[0][2] = 0;
		
		tmp = GeoUtils.calcPointFromDirectionDistance(midpoint[0][0], midpoint[0][1], (strike+180)%360, sdist);
		plane[1][0] = tmp[0];
		plane[1][1] = tmp[1];
		plane[1][2] = 0;
		
		tmp = GeoUtils.calcPointFromDirectionDistance(midpoint[1][0], midpoint[1][1], (strike+180)%360, sdist);
		plane[2][0] = tmp[0];
		plane[2][1] = tmp[1];
		plane[2][2] = event.depth*2;
		
		tmp = GeoUtils.calcPointFromDirectionDistance(midpoint[1][0], midpoint[1][1], strike, sdist);
		plane[3][0] = tmp[0];
		plane[3][1] = tmp[1];
		plane[3][2] = event.depth*2;
		
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
	
}