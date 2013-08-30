package jp.ac.tsukuba.cs.conclave.earthquake;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePeriod;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.faultmodel.FaultModel;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.GeoUtils;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.StatUtils;

/***
 * 
 * This class tests all quakes that fit a given filter, using all available model testing algorithms.
 * It outputs comparison data between all the model testing algorithms.
 * 
 * @author caranha
 *
 */
public class TestAllQuakes {


	
	// INTERNAL VARIABLES //	
	static Logger logger;
	
	DataList maindata; // Link to the main Earthquake data
	DataList filteredQuakes; // Earthquakes that fit the interest filter
	ArrayList<DataList> filteredAS; // A list of aftershocks for each filtered Quake, at the maximum period
	ReadablePeriod[] periods; // Pre-calculated periods

	// FILTER PARAMETERS //
	boolean filterMag = true;
	double minMag = 6; // EVENT: Minimum magnitude
	boolean filterDepth = false; 
	double maxDepth = 40; // EVENT: Maximum magnitude
	int minAftershock = 30; // EVENT: Minimum number of aftershocks
	boolean filterASMag = true;
	double minASMag = 2; // AFTERSHOCK: Minimum magnitude
	boolean filterASDepth = false;
	double maxASDepth = 40; // AFTERSHOCK: Maximum depth
	
	ReadablePeriod ASquanta = Hours.ONE; // Size of Time Period
	int ASquantaN = 5; // Number of time Periods	
	
	// DATA FILES //
	// Default data files: filename, type, filename, type
	String[][] datafiles = {{"jma_cat_2000_2012_Mth2.5_formatted.dat","jma"},
			{"catalog_fnet_1997_20130429_f3.txt","fnet"}}; 
	
	
	public static void main(String[] args) {
		TestAllQuakes tester = new TestAllQuakes();
		tester.fileLoader(tester.datafiles);
		
		// FILTERING EARTHQUAKE DATA
		tester.filterQuakes(tester.maindata); // TODO: make maindata private
		
		// RUNNING THE EXPERIMENT
		tester.runTestAllQuakes();
		
		// CLEARING THE DATA
	}

	
	
	public void runTestAllQuakes()
	{

		for (int i = 0; i < filteredQuakes.size(); i++) // for each filtered quake
		{
			DataPoint curr = filteredQuakes.data.get(i);
			
			// Printing Basic earthquake data
			log("# Earthquake "+i+" #");
			log("  "+curr.time.toString("YYYY-MM-dd HH:MM")+"  Mag: "+curr.magnitude+" Depth: "+curr.depth+
					" Lat:"+curr.latitude+" Lon:"+curr.longitude);
			log("  Model 1: "+"S"+curr.S[0]+" D"+curr.D[0]+" R"+curr.R[0]);
			log("  Model 2: "+"S"+curr.S[1]+" D"+curr.D[1]+" R"+curr.R[1]);
			
			// Point in Plane Testing
			log("  * Point in plane Testing *");
			log("  Period #\tTotal/M1/M2/Decision");
			FaultModel f1 = new FaultModel(curr,0);
			FaultModel f2 = new FaultModel(curr,1);

			for (int j = 0; j < ASquantaN; j++) // for each Quanta
			{
				// Calculate the Aftershocks in this period
				Interval time = new Interval(curr.time,periods[j]); // this quanta
				DataList ASlocal = new DataList();
				for (int k = 0; k < filteredAS.get(i).size(); k++)
					if (time.contains(filteredAS.get(i).data.get(k).time))
						ASlocal.addData(filteredAS.get(i).data.get(k));
	
				// POINT IN PLANE
				// Calculate Ratios
				DataList sub1 = f1.pointsInPlane(ASlocal); // points in plane 1
				DataList sub2 = f2.pointsInPlane(ASlocal); // points in plane 2
				double ratioM1 = (double)sub1.size()/(double)ASlocal.size(); 
				double ratioM2 = (double)sub2.size()/(double)ASlocal.size(); 

				int result;
				String reslog = "";
				
				// Calculating which model won: model wins if difference > 20%
				if ((Math.abs(ratioM1 - ratioM2)) < 0.2)
					result = 0;
				else 
					result = (ratioM1 > ratioM2?1:2);


				// DISTANCE FROM PLANE TESTING
				ArrayList<Double> t1 = new ArrayList<Double>();
				ArrayList<Double> t2 = new ArrayList<Double>();
				
				for (int jj = 0; jj < sub1.size(); jj++)
				{
					double dist = f1.calcFaultDistance(sub1.data.get(jj));
					if (dist != -1)
						t1.add(Math.abs(dist));
				}
				
				for (int jj = 0; jj < sub2.size(); jj++)
				{
					double dist = f2.calcFaultDistance(sub2.data.get(jj));
					if (dist != -1)
						t2.add(Math.abs(dist));
				}

				// FAULT DISTANCE TESTING
				
				// Printing Point in Plane Result
				reslog = "  P# "+j+":\t\t"+ASlocal.size()+"/"+
						String.format("%.2f",ratioM1)+"/"+
						String.format("%.2f",ratioM2)+"/"+ 
						result;
				// Printing Plane distance result
				double[] tots;
				
				if (t1.size() > 0)
				{
					tots = StatUtils.averageVariance(t1);
					reslog += "  --  "+t1.size()+":"+String.format("%.3f",tots[0])+"+-"+String.format("%.3f",Math.sqrt(tots[1]));
				}
				else
					reslog += "  --  0:--";
				
				if (t2.size() > 0)
				{
					tots = StatUtils.averageVariance(t2);
					reslog += "  "+t2.size()+":"+String.format("%.3f",tots[0])+"+-"+String.format("%.3f",Math.sqrt(tots[1]));
				}
				else
					reslog += "  0:--";
				
				log(reslog);

			}
		}
	}

	
	/**
	 * Loads the main data for this testing using filenames and file types
	 * Filenames must be full-pathed filenames, while filetypes can be "jma" or "fnet"
	 * @param files
	 */
	public void fileLoader(String[][] files)
	{
		maindata = new DataList();
		for (int i = 0; i < files.length; i++) // reading data
		{
			maindata.loadData(files[i][0], files[i][1]);
		}
	}
	
	/** 
	 * Loads the main data for this testing object using an existing DataList Object
	 * @param d
	 */
	public void dataLoader(DataList d)
	{
		maindata = d;
	}
	
	
	
	static void log(String s)
	{
		System.out.println(s);
	}
	
	public TestAllQuakes()
	{
		if (TestAllQuakes.logger == null)
			logger = Logger.getLogger(DataPoint.class.getName());
		filteredQuakes = new DataList();
		
		calculatePeriods();
	}
	
	/**
	 * Calculate the internal periods based on the parameter values. Generates
	 * N periods of size "quanta*N"
	 */
	public void calculatePeriods()
	{
		periods = new ReadablePeriod[ASquantaN];
		int type = ASquanta.getPeriodType().hashCode();
		
		for(int i = 0; i < ASquantaN; i++)
		{
			if (type == PeriodType.minutes().hashCode())
				periods[i] = ((Minutes) ASquanta).multipliedBy(i+1).toPeriod();
			if (type == PeriodType.hours().hashCode())
				periods[i] = ((Hours) ASquanta).multipliedBy(i+1).toPeriod();
			if (type == PeriodType.days().hashCode())
				periods[i] = ((Days) ASquanta).multipliedBy(i+1).toPeriod();
		}		
	}
	
	
	/**
	 * Creates a list of quakes based on the filter values set for this "Test All Quakes" object.
	 * Theses list is stored internally and used when testing all objects.
	 * 
	 * Also creates total lists of aftershocks for each filtered quake.
	 * 
	 * @param l
	 */
	void filterQuakes(DataList l)
	{
		filteredQuakes = new DataList(); // list of filtered earthquakes
		filteredAS = new ArrayList<DataList>(); // list of aftershocks
		
		for (int i = 0; i < l.size(); i++) 
		{
			DataPoint curr = l.data.get(i);
			if ((!curr.hasFaultModel())|| // fault model
					(filterMag && curr.magnitude < minMag)|| // filter magnitude
					(filterDepth && curr.depth > maxDepth)) // filter depth
				continue;

			DataList ASList = filterASbyPeriod(l, i, curr, periods[periods.length-1]);
			
			// We add an event if it pass all the filters and has the minimum number of aftershocks in the 
			// entire period
			if (ASList.size() >= minAftershock) 
			{
				filteredAS.add(ASList);
				filteredQuakes.addData(curr);
			}			
		}		
	}
	
	/**
	 * Return the number of events that have been filtered, under the current parameters.
	 * @return
	 */
	public int getFilteredQuakeSize()
	{
		if (filteredQuakes != null)
			return filteredQuakes.size();
		
		logger.warning("Tried to access TestAllQuakes object before it was initialized");
		return 0;
	}
	
	public void printEventsAndAfterShocks()
	{
		for (int i = 0; i < getFilteredQuakeSize(); i++)
		{
			log(filteredQuakes.data.get(i).dump());
			log(filteredAS.get(i).size()+"\n");
		}
	}
	
	/**
	 * Generates a list of aftershocks for an event, based on the filter attributes of this object.
	 * This functions requires a ReadablePeriod composed by the Quanta, multiplied by a scalar
	 * 
	 * WARNING: this datalist is not supposed to be modified!
	 * WARNING: multiply the period by the scalar before calling this method.
	 * 
	 * WARNING: this searches "data" from the beginning. Try to avoid putting data lists with 
	 * tens of thousands of events frequently.
	 * 
	 * @param data The list with all earthquakes in record
	 * @param event The event that we are studying
	 * @param period The time period for which we are searching aftershocks.
	 * @return
	 */
	DataList filterASbyPeriod(DataList data, int dataindex, DataPoint event, ReadablePeriod period)
	{
		DataList ret = new DataList();
		
		Interval ASperiod = new Interval(event.time,period);

		for (int i = dataindex; (i < data.size() && !ASperiod.isBefore(data.data.get(i).time)); i++)
		{
			DataPoint curr = data.data.get(i);
			if ((filterASMag && curr.magnitude < minASMag)||
					(filterASDepth && curr.depth > maxASDepth)||
					(GeoUtils.getAftershockRadius(event.magnitude) < GeoUtils.haversineDistance(event.latitude, event.longitude, curr.latitude, curr.longitude))||
					(!ASperiod.contains(curr.time)))
				continue;
			
			ret.addData(curr);			
		}
		
		return ret;
	}
}
