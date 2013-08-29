package jp.ac.tsukuba.cs.conclave.earthquake;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadablePeriod;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.faultmodel.FaultModel;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.GeoUtils;

/***
 * 
 * This class tests all quakes that fit a given filter, using all available model testing algorithms.
 * It outputs comparison data between all the model testing algorithms.
 * 
 * @author caranha
 *
 */
public class TestAllQuakes {

	static Logger logger;
	
	DataList FilteredQuakes; // Earthquakes that fit the interest filter
	ArrayList<DataList> FilteredAS; // A list of aftershocks for each Filtered Quake

	// Filter variables for quakes
	boolean filterMag = true;
	double minMag = 6; // Minimum magnitude for selecting an event

	boolean filterDepth = false; 
	double maxDepth = 40; // Maximum magnitude for selecting an event
	
	int minAftershock = 10; // Minimum number of aftershocks after a quanta for selecting an event
	
	boolean filterASMag = true;
	double minASMag = 2; // Minimum magnitude for selecting an aftershock
	
	boolean filterASDepth = false;
	double maxASDepth = 40;
	
	ReadablePeriod ASquanta = Days.ONE; // time period for aftershock testing
	int ASquantaN = 3; // number of periods for aftershock testing
	
	
	
	
	
	String[] datafiles = {"jma_cat_2000_2012_Mth2.5_formatted.dat","jma",
		"catalog_fnet_1997_20130429_f3.txt","fnet"}; // file, type, file, type
	
	Days afterShockTimeSize = Days.ONE; // size of the Period for aftershock testing
	int afterShockTimeQuanta = 3; // number of Periods for aftershock testing
	
	public static void main(String[] args) {
		TestAllQuakes tester = new TestAllQuakes();
		tester.runTestAllQuakes();
		
		DataList data = new DataList();
		for (int i = 0; i < tester.datafiles.length; i+=2) // reading data
		{
			data.loadData(tester.datafiles[i],tester.datafiles[i+1]);
		}
		tester.filterQuakes(data);
		log("*"+tester.getFilteredQuakeSize()+"*");
	}
	
	public void runTestAllQuakes()
	{
		DataList data = new DataList();
		DataList[] AfterShocks = new DataList[afterShockTimeQuanta];
		
		double[][] ratios = new double[afterShockTimeQuanta][2];
		
		for (int i = 0; i < afterShockTimeQuanta; i++)
			AfterShocks[i] = new DataList();
		
		for (int i = 0; i < datafiles.length; i+=2) // reading data
		{
			data.loadData(datafiles[i],datafiles[i+1]);
		}

		int total = 0;
		for (int i = 0; i < data.size(); i++)
		{
			DataPoint curr = data.data.get(i);
			if (curr.FM == true && curr.magnitude >= minMag) // Filtering: FM Quake, Magnitude
			{
				// Getting Aftershocks;
				for (int ii = 0; ii < afterShockTimeQuanta; ii++)
					AfterShocks[ii].clear();

				int j = i;
				
				// testing the time period to get aftershocks
				// TODO: Maybe make aftershock lists based on total aftershocks, not necessarily time?
				while (j < data.size() && data.data.get(j).time.isBefore(curr.time.plus(afterShockTimeSize.multipliedBy(afterShockTimeQuanta))))
				{
					DataPoint after = data.data.get(j);
					if (after.magnitude >= minASMag && 
							GeoUtils.getAftershockRadius(curr.magnitude) >= GeoUtils.haversineDistance(curr.latitude, curr.longitude, after.latitude, after.longitude))	
					{
						for (int ii = 0; ii < afterShockTimeQuanta; ii++)
							if (after.time.isBefore(curr.time.plus(afterShockTimeSize.multipliedBy(ii+1))))
								AfterShocks[ii].addData(new DataPoint(after));
					}
					j++;
				}
				
				if (AfterShocks[0].size() >= minAftershock) 
				{
					// This earthquake has the minimum required number of aftershocks. Now we test it.
					total ++; 
					// Printing basic data
					log("# Earthquake "+total+" #");
					log("  "+curr.time.toString("YYYY-MM-dd HH:MM")+"  Mag: "+curr.magnitude+" Depth: "+curr.depth+
							" Lat:"+curr.latitude+" Lon:"+curr.longitude);
					String afscand = "";
					for (int ii = 0; ii < afterShockTimeQuanta; ii++)
						afscand+=AfterShocks[ii].size()+" ";
					
					log("  Total Aftershock Candidates: "+afscand);
					log("\t\tModel 1\t\tModel 2\t\tResults");
					log("\t\t"+"S"+curr.S[0]+" D"+curr.D[0]+" R"+curr.R[0]+"\t"+
							"S"+curr.S[1]+" D"+curr.D[1]+" R"+curr.R[1]);
					// Creating FMs
					FaultModel f1 = new FaultModel(curr,0);
					FaultModel f2 = new FaultModel(curr,1);
					// Point-in-plane testing
					
					int[] result = new int[afterShockTimeQuanta];
					for (int ii = 0; ii < afterShockTimeQuanta; ii++)
					{
						DataList sub1 = f1.pointsInPlane(AfterShocks[ii]);
						DataList sub2 = f2.pointsInPlane(AfterShocks[ii]);
						ratios[ii][0] = (double)sub1.size()/(double)AfterShocks[ii].size();
						ratios[ii][1] = (double)sub2.size()/(double)AfterShocks[ii].size();
						
						// Calculating which model won: model wins if difference > 20%
						if (Math.abs(ratios[ii][0] - ratios[ii][1]) < 0.2)
							result[ii] = 0;
						else 
							result[ii] = (ratios[ii][0] > ratios[ii][1]?1:2);
					}
					
					String reslog = "  Pts in Plane\t";
					for (int ii = 0; ii < afterShockTimeQuanta;ii++)
						reslog+=String.format("%.2f", ratios[ii][0])+"/";
					reslog+="\t";
					
					for (int ii = 0; ii < afterShockTimeQuanta;ii++)
						reslog+=String.format("%.2f", ratios[ii][1])+"/";
					reslog+="\t";
					
					for (int ii = 0; ii < afterShockTimeQuanta;ii++)
						reslog+=result[ii]+"/";
					
					log(reslog);
					
				}
			}
		}
		log("\n\n"+total);

	}

	static void log(String s)
	{
		System.out.println(s);
	}
	
	public TestAllQuakes()
	{
		if (TestAllQuakes.logger == null)
			logger = Logger.getLogger(DataPoint.class.getName());
		FilteredQuakes = new DataList();
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
		FilteredQuakes = new DataList(); // list of filtered earthquakes
		FilteredAS = new ArrayList<DataList>(); // list of aftershocks
		
		for (int i = 0; i < l.size(); i++) 
		{
			DataPoint curr = l.data.get(i);
			if ((!curr.hasFaultModel())|| // fault model
					(filterMag && curr.magnitude < minMag)|| // filter magnitude
					(filterDepth && curr.depth > maxDepth)) // filter depth
				continue;
			
			DataList ASList = filterASbyPeriod(l, i, curr, ASquanta);
			// FIXME: you want the period multiplied by the Nperiod here
			
			if (ASList.size() >= minAftershock) // passed filters and has minimum number of aftershocks
			{
				FilteredAS.add(ASList);
				FilteredQuakes.addData(curr);
			}			
		}		
	}
	
	/**
	 * Return the number of events that have been filtered, under the current parameters.
	 * @return
	 */
	public int getFilteredQuakeSize()
	{
		if (FilteredQuakes != null)
			return FilteredQuakes.size();
		
		logger.warning("Tried to access TestAllQuakes object before it was initialized");
		return 0;
	}
	
	public void printEventsAndAfterShocks()
	{
		for (int i = 0; i < getFilteredQuakeSize(); i++)
		{
			log(FilteredQuakes.data.get(i).dump());
			log(FilteredAS.get(i).size()+"\n");
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
