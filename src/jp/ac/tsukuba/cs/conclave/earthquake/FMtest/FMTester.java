package jp.ac.tsukuba.cs.conclave.earthquake.FMtest;

import java.util.logging.Logger;

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
	
	DataPoint origin;
	int model[][]; // candidate models
	double radius; // Radius for events
	
	
	public FMTester()
	{
		model = new int[2][3]; // two models, three attributes (S,D,R)
	}
	
	public boolean init(DataPoint o)
	{
		if (o.FM == false)
		{
			logger.warning("FMTester.init() received a data point without FM data!");
			return false;
		}
		
		origin = new DataPoint(o);
		
		/* Calculating aftershock radius. This radius is taken from 
		 * eq 9 of Helmstetter et al., JGR, 2005 
		 */
		radius = 0.01*Math.pow(10, (0.5*origin.magnitude));
		radius = RADIUS_RATE*radius;
		
		/* Setting the base models */
		for (int i = 0; i < MODEL_N; i++)
		{
			model[i][0] = origin.S[i];
			model[i][1] = origin.D[i];
			model[i][2] = origin.R[i];
		}
		
		return true;
	}
	
	
	
}
