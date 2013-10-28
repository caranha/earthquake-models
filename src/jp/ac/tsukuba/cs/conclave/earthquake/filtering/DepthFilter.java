package jp.ac.tsukuba.cs.conclave.earthquake.filtering;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

public class DepthFilter implements EarthquakeFilter {
	float maximum = 0;
	float minimum = 0;

	/**
	 * Sets the maximum depth for this filter (if no maximum is set, or 
	 * if maximum is set to 0, this filter will not test maximum depth
	 * 
	 * @param m
	 */
	public void setmax(float m)
	{
		maximum = m;
	}
	
	/**
	 * Sets the minimum depth for this filter (if no minimum is set, or 
	 * if minimum is set to 0, this filter will not test minimum depth
	 * 
	 * @param m
	 */
	public void setmin(float m)
	{
		minimum = m;
	}
	
	@Override
	public boolean testData(DataPoint p, DataList l) {
		if (minimum > 0 && p.depth < minimum)
			return false;
		if (maximum > 0 && p.depth > maximum)
			return false;
		
		return true;
	}

	@Override
	public EarthquakeFilter testNOP() {
		if (maximum == 0 && minimum == 0)
			return null;
		else
			return this;					
	}

}
