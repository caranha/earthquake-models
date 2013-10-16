package jp.ac.tsukuba.cs.conclave.earthquake.filtering;

import org.joda.time.DateTime;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

public class DateFilter implements EarthquakeFilter {
	/*
	 *  FIXME: Because earthquakes are sorted by date, filtering each earthquake 
	 *  in this fashion (instead of setting initial and final dates) is inefficient.
	 *  Do something about it.
	 */
	
	DateTime minimum;
	DateTime maximum;
	
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

}
