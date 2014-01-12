package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.RISolver;

import org.joda.time.Duration;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.DateUtils;
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
	
	CSEPModel trainingmodel;
	CSEPModel RIModel;
	
	
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
		
		trainingtimewindow = new Duration(DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("training start date", "2000-01-01")),
				DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("training end date", "2001-01-01")));
		testtimewindow = new Duration(DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("testing start date", "2001-01-01")),
				DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("testing end date", "2002-01-01")));
				
		trainingmodel = (new CSEPModelFactory(param)).modelFromData(data);
	}
	
	
	
	
	public void execute()
	{
		double minPI = Double.POSITIVE_INFINITY;
		double[][] PI = new double[trainingmodel.getTotalLonBins()][trainingmodel.getTotalLatBins()];
		int totlon = trainingmodel.getTotalLonBins();
		int totlat = trainingmodel.getTotalLatBins();
		int totevent = trainingmodel.getTotalEvents();
		
		//// Algorithm
		// Calculate Pi (smoothed sum/total events) for each geographical bin
		for (int i = 0; i < totlon; i++)
			for (int j = 0; j < totlat; j++)
			{
				double bincount = 0;
				double sum = 0;
				for (int ii = 0; ii < 2*s; ii++)
					for (int jj = 0; jj < 2*s; jj ++)
					{
						int di = i + ii - s/2;
						int dj = j + jj - s/2;
						if ((di >= 0 && di < totlon && dj >= 0 && dj < totlat) &&
							 (s > distance(di,dj,i,j)))
						{
							bincount++;
							sum += trainingmodel.getEventsFromBin(di, dj);
						}
					}
				PI[i][j] = (sum/bincount)/totevent;
				if (PI[i][j] > 0 && PI[i][j] < minPI)
					minPI = PI[i][j];
			}
		
		// Estimate the number of events for each bin
		// -- Multiply Pi by "Delta T(test)/Delta T(train)"*Number of events
		// Replace zero bins with minimum PI
		for (int i = 0; i < totlon; i++)
			for (int j = 0; j < totlat; j++)
			{
				if (PI[i][j] == 0)
					PI[i][j] = minPI;
				
				PI[i][j] = PI[i][j]*totevent*((double) testtimewindow.getStandardSeconds()/(double)trainingtimewindow.getStandardSeconds()); 
			}
		
		if (usemagnitudebins)
		{
			// TODO: Magnitude bins (which end up being the same as geographical
			// -- Extrapolate Magnitude mi = (between M1 and M2)
			// ---- bin(mi) = 10^B(m2) - 10^B(m1)
			// ---- B(m) = log10(Geobin) - b*(m - Mmin)
		}
		else
		{
			// round PI and get a model from it.
			int array[] = new int[trainingmodel.getTotalBins()];
			for (int i = 0; i < totlon; i++)
				for (int j = 0; j < totlat; j++)
					array[i*totlat+j] = (int) Math.ceil(PI[i][j]);
			RIModel = (new CSEPModelFactory(param)).modelFromIntegerArray(array);
		}
	}
	
	public CSEPModel getBest()
	{
		return RIModel;
	}
	
	double distance(int x0, int y0, int x1, int y1)
	{
		return Math.sqrt((x0 - x1)*(x0 - x1) + (y0 - y1)*(y0 - y1));
	}	
}
