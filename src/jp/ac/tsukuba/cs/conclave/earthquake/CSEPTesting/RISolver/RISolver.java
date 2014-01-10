package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.RISolver;

import org.joda.time.Duration;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;


/***
 * Implements the Relative Intensity Model with Attenuation Effects. 
 * Proposed by K. Z. Nanjo, Earth Planets Space (63), 2011
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class RISolver {

	
	Parameter param;
	
	//// Parameters
	double Mmin; // Minimum magnitude
	double Ms; // Minimum magnitude bin;
	double Me; // Maximum magnitude bin;
	double Mdelta; // Magnitude bin size;
	
	boolean usemagnitudebins;
	
	double b; // Parameter B for magnitude bin estimation
	int s; // distance (in bins) for the attenuation operator
		
	// Training data. Assumes cutting by depth, geo, date and min magnitude;
	DataList data; // Training data 
	Duration trainingtimewindow;
	Duration testtimewindow;
	
	
	public void setup(Parameter p, DataList l)
	{
		param = p; 
		data = l;
		
		Mmin = Double.parseDouble(param.getParameter("cutoff magnitude","2.5"));
		usemagnitudebins = Boolean.parseBoolean(param.getParameter("magnitude bins", "false"));
		if (usemagnitudebins)
		{
			Ms = Double.parseDouble(param.getParameter("magnitude bin min","4"));
			Me = Double.parseDouble(param.getParameter("magnitude bin max", "9"));
			Mdelta = Double.parseDouble(param.getParameter("magnitude bin delta", "0.1"));
		}
		
		b = Double.parseDouble(param.getParameter("RI B Parameter", "0.9"));
		// TODO: the S parameter should be better expressed in "degree" units
		s = Integer.parseInt(param.getParameter("RI S parameter", "5"));
		
		// CALCULATE DURATION PARAMETERS HERE		
		
	}
	
	public void execute()
	{
		//// Algorithm
		
		// Calculate Pi for each geographical bin
		// -- Sum all events in the S radius
		// -- Divide by the total bins in the radius
		// -- Divide the result by the total number of events.
		
		// Estimate the number of events for each bin
		// -- Multiply Pi by "Delta T(test)/Delta T(train)"
		// -- Extrapolate Magnitude mi = (between M1 and M2)
		// ---- bin(mi) = 10^B(m2) - 10^B(m1)
		// ---- B(m) = log10(Geobin) - b*(m - Mmin)

	}
	
	public CSEPModel getBest()
	{
		// Should probably be a factory method instead
		return null;
	}
}
