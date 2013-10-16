package jp.ac.tsukuba.cs.conclave.earthquake.filtering;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/**
 * Implements a filter based on magnitude. 
 * 
 * @author caranha
 *
 */
public class MagnitudeFilter implements EarthquakeFilter {

	float maximum = 0;
	float minimum = 0;

	/**
	 * Sets the maximum magnitude for this filter (if no maximum is set, or 
	 * if maximum is set to 0, this filter will not test maximum magnitude
	 * 
	 * @param m
	 */
	public void setmax(float m)
	{
		maximum = m;
	}
	
	/**
	 * Sets the minimum magnitude for this filter (if no minimum is set, or 
	 * if minimum is set to 0, this filter will not test minimum magnitude
	 * 
	 * @param m
	 */
	public void setmin(float m)
	{
		minimum = m;
	}
	
	@Override
	public boolean testData(DataPoint p, DataList l) {
		if (minimum > 0 && p.magnitude < minimum)
			return false;
		if (maximum > 0 && p.magnitude > maximum)
			return false;
		
		return true;
	}

}
