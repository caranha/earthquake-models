package jp.ac.tsukuba.cs.conclave.earthquake.directionclustering;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.LocationFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.UnitaryDateFilter;

import org.joda.time.Days;

/***
 * This class finds the earthquakes that are "near" the target earthquake in the Catalog. Near earthquakes are defined by a 
 * temporal limit before the target earthquake (defined in days), and a geographical limit (defined in degrees). These limits 
 * can be modified, but the default values are set according to the values observed in Brodsky et al. 2014.
 * 
 * @author caranha
 *
 */


public class BasicLocalFiltering {
	long timelimit;
	double latlimit;
	double lonlimit;
	
	public BasicLocalFiltering()
	{
		timelimit = Days.ONE.toStandardDuration().getMillis()*21; // standard time limit: 21 days;
		latlimit = 1;
		lonlimit = 1;
	}
	
	DataList filter(DataPoint target, DataList data)
	{
		LocationFilter floc = new LocationFilter();
		UnitaryDateFilter fdate = new UnitaryDateFilter();
		CompositeEarthquakeFilter filter = new CompositeEarthquakeFilter();
		
		floc.setMinimum(target.longitude - lonlimit, target.latitude - latlimit);
		floc.setMaximum(target.longitude + lonlimit, target.latitude + latlimit);
		fdate.setMinimum(target.time.minus(timelimit));
		fdate.setMaximum(target.time);
		
		filter.addFilter(floc);
		filter.addFilter(fdate);
		
		return filter.filter(data);
	}	
	
	@Override
	public String toString()
	{
		return "BasicLocalFiltering: timelimit="+timelimit+" latlimit="+latlimit+" lonlimit="+lonlimit;
	}
}
