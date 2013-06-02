package jp.ac.tsukuba.cs.conclave.earthquake.gui;

import java.util.ArrayList;

import org.joda.time.Duration;

import jp.ac.tsukuba.cs.conclave.earthquake.FMtest.FMBase;
import jp.ac.tsukuba.cs.conclave.earthquake.FMtest.FMStatModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/**
 * Static class that holds all the data needed for the GUI to work
 * @author caranha
 *
 */

public class DataContext {
	
	private static DataContext instance;
	
	DataList total;
	DataList fmdata;
	ArrayList<Integer> eventselection;

	DataPoint origin;
	Duration timelimit;
	
	FMBase modelBase;
	FMStatModel[] models;

	protected DataContext() 
	{
		total = new DataList();
		fmdata = new DataList();
		modelBase = new FMBase();
		
		eventselection = new ArrayList<Integer>();
		origin = null;
		timelimit = null;
		
		models = new FMStatModel[2];		
	}
	
	public static synchronized DataContext getInstance() 
	{
		if(instance == null) {
			instance = new DataContext();
	      }	
		return instance;
	}

	public void init(String FnetData, String JMAData)
	{
		total.loadData(JMAData, "jma");
		total.loadData(FnetData, "fnet");
		fmdata.loadData(FnetData, "fnet");
	}
	
	public void setEventSelection(double min, double max)
	{
		eventselection = fmdata.getEventsByMagnitude(min, max);
	}
	
}
