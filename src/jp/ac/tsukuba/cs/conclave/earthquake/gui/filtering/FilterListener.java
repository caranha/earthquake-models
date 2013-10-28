package jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering;

import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;

public interface FilterListener {
	public void filterChanged(CompositeEarthquakeFilter filter);
	
}
