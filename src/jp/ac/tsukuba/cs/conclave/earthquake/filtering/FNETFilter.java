package jp.ac.tsukuba.cs.conclave.earthquake.filtering;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/**
 * If this filter is set to true, it will filter out earthquakes without 
 * Fault models.
 * 
 * If this filter is set to false, it will do nothing.
 * 
 * @author caranha
 *
 */
public class FNETFilter implements EarthquakeFilter {

	boolean filtermodel = false;
	
	public void setFilter(boolean f)
	{
		filtermodel = f;
	}
	
	@Override
	public boolean testData(DataPoint p, DataList l) {
		if (filtermodel)
		{
			return p.hasFaultModel();
		}
		else
			return true;
	}

	@Override
	public EarthquakeFilter testNOP() {
		if (filtermodel)
			return this;
		else	
			return null;
	}

}
