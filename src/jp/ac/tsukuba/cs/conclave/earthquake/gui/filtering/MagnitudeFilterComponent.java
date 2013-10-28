package jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.EarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.MagnitudeFilter;

public class MagnitudeFilterComponent extends JPanel implements FilterComponent {

	JTextField minMag;
	JTextField maxMag;
	
	float minvalue = 0;
	float maxvalue = 0;
	
	String errormsg = "";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3450463150671890950L;

	public MagnitudeFilterComponent()
	{
		super();
		this.add(new JLabel("Magnitude   Min:"));
		this.add(minMag = new JTextField(4));
		this.add(new JLabel(" Max:"));
		this.add(maxMag = new JTextField(4));
		
	}
	
	
	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	/**
	 * You are supposed to call this after calling "isCorrect". Behavior might 
	 * be buggy if you do otherwise.
	 */
	public EarthquakeFilter getFilter() {
		MagnitudeFilter aux = new MagnitudeFilter();

		aux.setmin(minvalue);
		aux.setmax(maxvalue);
	
		return aux.testNOP();
	}

	@Override
	public String getErrorString() {
		return errormsg;
	}


	@Override
	public void setFocus(DataPoint d) {
		//NOOP
	}


	@Override
	public void reset() {
		minMag.setText("");
		minvalue = 0;
		maxMag.setText("");
		maxvalue = 0;
	}

	
	public boolean emptyString(String s)
	{
		return (s == null || s.trim().length() == 0);		
	}
	
	@Override
	public boolean isEmpty() {
		return (emptyString(minMag.getText()) && 
				emptyString(maxMag.getText()));
	}

	@Override
	public boolean isCorrect() {
		if (!emptyString(minMag.getText()))
		{
			try {
				minvalue = Float.parseFloat(minMag.getText());
			} catch (Exception e)
			{
				errormsg = "Error in minimum Magnitude: "+e.getMessage();
				return false;
			}			
		}
		
		if (!emptyString(maxMag.getText()))
		{
			try {
				maxvalue = Float.parseFloat(maxMag.getText());
			} catch (Exception e)
			{
				errormsg = "Error in maximum Magnitude: "+e.getMessage();
				return false;
			}
		}
		
		if (minvalue > 0 && maxvalue > 0 && minvalue > maxvalue)
		{
			errormsg = "Error in Magnitude: Minimum value above Maximum";
			return false;
		}
		
		errormsg = "no errors";
		return true;
	}
}
