package jp.ac.tsukuba.cs.conclave.earthquake.filtering;

import org.joda.time.DateTime;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/**
 * Filters ONE earthquake based on date.
 * 
 * This is inefficient if we are filtering an Earthquake List sorted by time, 
 * since we can simply make a binary search of starting and ending dates.
 *  
 * @author caranha
 *
 */

public class UnitaryDateFilter implements EarthquakeFilter {
	
	DateTime minimum = null;
	DateTime maximum = null;
	
	/**
	 * Set minimum date for filters. Earthquakes before this date will be filtered. 
	 * If m is null, then no minimum date will be filtered.
	 * 
	 * @param m
	 */
	public void setMinimum(DateTime m)
	{
		minimum = m;
	}
	
	/**
	 * Set maximum date for filters. Earthquakes after this date will be filtered.
	 * If m is null, then no maximum date will be filtered.
	 * @param m
	 */
	public void setMaximum(DateTime m)
	{
		maximum = m;
	}
	
	@Override
	public boolean testData(DataPoint p, DataList l) {
		if (minimum != null && p.time.isBefore(minimum))
			return false;
		if (maximum != null && p.time.isAfter(maximum))
			return false;
		
		return true;
	}

	@Override
	public EarthquakeFilter testNOP() {
		
		if (minimum == null && maximum == null)
			return null;
		else 
			return this;
	}
}
