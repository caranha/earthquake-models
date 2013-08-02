package jp.ac.tsukuba.cs.conclave.earthquake;

import org.joda.time.Days;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.faultmodel.FaultModel;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.GeoUtils;

/***
 * This Class tests all the quakes from a data file at once, against all known hypothesis. 
 * Data for each valid earthquake is saved on a "earthquakes" file, while 
 * 
 * 
 * @author caranha
 *
 */
public class TestAllQuakes {

	// Parameters: 
	// TODO: Read these from a parameter file
	// TODO: Modify the file reader object to automagically detect the type

	static double minMag = 6; // Minimum magnitude for selecting an event
	static int minAftershock = 10; // Minimum number of aftershocks after a day for selecting an event
	static double minAfterShockMag = 2; // Minimum magnitude for selecting an aftershock
	static 	String[] datafiles = {"jma_cat_2000_2012_Mth2.5_formatted.dat","jma",
		"catalog_fnet_1997_20130429_f3.txt","fnet"}; // file, type, file, type
	
	static Days afterShockTimeSize = Days.ONE; // size of the Period for aftershock testing
	static int afterShockTimeQuanta = 3; // number of Periods for aftershock testing
	
	public static void main(String[] args) {

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
					if (after.magnitude >= minAfterShockMag && 
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
	
	
	
	
}
