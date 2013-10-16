package jp.ac.tsukuba.cs.conclave.earthquake.gui.datalist;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/**
 * Allows listening to the doubleclick of an Earthquake in the DataListFrame
 * @author caranha
 *
 */
public interface EarthquakeFocusListener {
	public void focusChanged(DataPoint d);
	
	
	
}
