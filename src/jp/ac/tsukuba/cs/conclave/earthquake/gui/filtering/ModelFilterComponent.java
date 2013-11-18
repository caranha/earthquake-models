package jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.EarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.FNETFilter;

/**
 * Implements the filter for FNET models
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class ModelFilterComponent extends JPanel implements FilterComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7668168317194451577L;

	JCheckBox hasFaultMechanism;
	
	/**
	 * Prepares the panel with all its internal components
	 */
	public ModelFilterComponent()
	{
		super();		
		hasFaultMechanism = new JCheckBox("Only events with FM Data", false);
		this.add(hasFaultMechanism);
	}
	
	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public EarthquakeFilter getFilter() {
		FNETFilter ret = new FNETFilter();
		ret.setFilter(hasFaultMechanism.isSelected());
		return ret.testNOP();
	}

	@Override
	public String getErrorString() {
		return "Somehow you managed to cause an error on the ModelFilterComponent. Congratulations!";
	}

	@Override
	public boolean isEmpty() {
		return (!hasFaultMechanism.isSelected());
	}

	@Override
	/**
	 * Should always be correct.
	 */
	public boolean isCorrect() {
		return true;
	}

	@Override
	public void setFocus(DataPoint d) {
		// NOOP
	}

	@Override
	public void reset() {
		hasFaultMechanism.setSelected(false);
	}

}
