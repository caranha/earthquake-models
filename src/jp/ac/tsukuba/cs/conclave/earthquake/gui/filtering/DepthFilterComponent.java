package jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.EarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.DepthFilter;

public class DepthFilterComponent extends JPanel implements FilterComponent {

	JTextField minValue;
	JTextField maxValue;
	
	float minvalue = 0;
	float maxvalue = 0;
	
	String errormsg = "";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3450463150671890950L;

	public DepthFilterComponent()
	{
		super();
		this.add(new JLabel("Depth   Min:"));
		this.add(minValue = new JTextField(4));
		this.add(new JLabel(" Max:"));
		this.add(maxValue = new JTextField(4));
		
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
		DepthFilter aux = new DepthFilter();

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
		minValue.setText("");
		minvalue = 0;
		maxValue.setText("");
		maxvalue = 0;
	}

	
	public boolean emptyString(String s)
	{
		return (s == null || s.trim().length() == 0);		
	}
	
	@Override
	public boolean isEmpty() {
		return (emptyString(minValue.getText()) && 
				emptyString(maxValue.getText()));
	}

	@Override
	public boolean isCorrect() {
		if (!emptyString(minValue.getText()))
		{
			try {
				minvalue = Float.parseFloat(minValue.getText());
			} catch (Exception e)
			{
				errormsg = "Error in minimum Depth: "+e.getMessage();
				return false;
			}			
		}
		
		if (!emptyString(minValue.getText()))
		{
			try {
				maxvalue = Float.parseFloat(maxValue.getText());
			} catch (Exception e)
			{
				errormsg = "Error in maximum Depth: "+e.getMessage();
				return false;
			}
		}
		
		if (minvalue > 0 && maxvalue > 0 && minvalue > maxvalue)
		{
			errormsg = "Error in Depth: Minimum value above Maximum";
			return false;
		}
		
		errormsg = "no errors";
		return true;
	}
}
