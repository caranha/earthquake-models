package jp.ac.tsukuba.cs.conclave.earthquake.gui.focus;

/**
 * This component displays an earthquake that was focused 
 * by either the filter list or the bookmark list.
 * 
 * It shows detailed information about the earthquake.
 * 
 * It allows focused earthquakes to be added to the Bookmark component.
 * 
 * It also allows focused earthquakes to have some of their 
 * properties drawn (Fault Plane, Radius, etc)
 * 
 */

import javax.swing.JInternalFrame;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.datalist.EarthquakeFocusListener;

public class EarthQuakeFocusFrame extends JInternalFrame implements
		EarthquakeFocusListener {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5396128292212358111L;

	@Override
	public void focusChanged(DataPoint d) {
		// TODO Auto-generated method stub

	}

}
