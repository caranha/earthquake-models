package jp.ac.tsukuba.cs.conclave.earthquake.filtering;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/**
 * Defines the interface for an earthquake filter, which is used by CompositeEarthquakeFilter
 * 
 * @author caranha
 *
 */
public interface EarthquakeFilter {

	/** 
	 * Tests if the data passed matches the filter. Returns true if the data 
	 * matches, or false if it doesn't match.
	 * 
	 * @param p
	 * @param l
	 * @return
	 */
	public boolean testData(DataPoint p, DataList l);
	
	public EarthquakeFilter testNOP();
	
}
