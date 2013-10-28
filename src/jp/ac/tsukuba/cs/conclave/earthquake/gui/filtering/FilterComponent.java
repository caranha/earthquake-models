package jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering;

import javax.swing.JPanel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.EarthquakeFilter;

public interface FilterComponent {

	public JPanel getPanel();
	public EarthquakeFilter getFilter();
	public String getErrorString();
	
	public boolean isEmpty();
	public boolean isCorrect();
	
	public void setFocus(DataPoint d);
	public void reset();
}
