package jp.ac.tsukuba.cs.conclave.earthquake.filtering;

import java.util.ArrayList;
import java.util.Iterator;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

public class CompositeEarthquakeFilter {

	// List of Component Filters
	private ArrayList<EarthquakeFilter> filters;
	
	public CompositeEarthquakeFilter()
	{
		filters = new ArrayList<EarthquakeFilter>();
	}
	
	public void addFilter(EarthquakeFilter f)
	{
		filters.add(f);
	}
	
	
	/**
	 * Filters all the earthquakes in the list D using all the filters added to this object.
	 * Returns a new list with references to all earthquakes that survived the filter. 
	 * 
	 * NOTE: the new list contains only references, not copies. Modify data at your own risk!
	 * 
	 * @param d
	 * @return
	 */
	public DataList filter(DataList d)	
	{
		DataList ret = new DataList();
		
		Iterator<DataPoint> it = ret.iterator();

		while (it.hasNext())
		{
			DataPoint DPaux = it.next();
			
			Iterator<EarthquakeFilter> filterIT = filters.iterator();
			boolean aux = true;
			while (aux && filterIT.hasNext())
			{
				aux = aux && filterIT.next().testData(DPaux, d);
			}
			
			if (aux)
				ret.addData(DPaux);
		}		
		return ret;
	}
	
}
