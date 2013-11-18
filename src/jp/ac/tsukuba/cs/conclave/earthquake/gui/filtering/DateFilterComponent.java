package jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.EarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.UnitaryDateFilter;

public class DateFilterComponent extends JPanel implements FilterComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 980139279360885175L;

	JTextField minDate;
	JTextField maxDate;
	
	//DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		.append(ISODateTimeFormat.date().getParser())
		.appendOptional(new DateTimeFormatterBuilder()
			.appendLiteral(' ')
			.append(ISODateTimeFormat.hourMinuteSecond()).toParser())
		.toFormatter();

	
	String errormsg ="";
	
	public DateFilterComponent()
	{
		super();
		JPanel aux;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		aux = new JPanel();
		aux.add(new JLabel("Date (YYYY-MM-DD [HH:[MM:[SS]]])"));
		this.add(aux);
		
				
		aux = new JPanel();
		minDate = new JTextField(12);
		maxDate = new JTextField(12);
		aux.add(new JLabel("Start: "));
		aux.add(minDate);
		aux.add(new JLabel(" End: "));
		aux.add(maxDate);
		this.add(aux);		
	}
	
	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	/**
	 * Assumes that the formatting is correct.
	 */
	public EarthquakeFilter getFilter() {
		UnitaryDateFilter ret = new UnitaryDateFilter();
		
		if (!emptyString(maxDate.getText()))
			ret.setMaximum(formatter.parseDateTime(maxDate.getText()));
		
		if (!emptyString(minDate.getText()))
			ret.setMinimum(formatter.parseDateTime(minDate.getText()));
		
		return ret.testNOP();
	}

	@Override
	public String getErrorString() {
		return errormsg;
	}

	boolean emptyString(String s)
	{
		return (s == null || s.trim().length() == 0);		
	}
	
	@Override
	public boolean isEmpty() {
		return (emptyString(maxDate.getText()) && 
				emptyString(minDate.getText()));
	}

	@Override
	public boolean isCorrect() {
		DateTime min = null;
		DateTime max = null;
		
		if (!emptyString(minDate.getText()))
		{
			minDate.setText(minDate.getText().trim());
			try {
				min = formatter.parseDateTime(minDate.getText());
			} catch (Exception e)
			{
				errormsg = "Error in minimum Time: "+e.getMessage();
				return false;
			}			
		}
		
		if (!emptyString(maxDate.getText()))
		{
			maxDate.setText(maxDate.getText().trim());
			try {
				max = formatter.parseDateTime(maxDate.getText());
			} catch (Exception e)
			{
				errormsg = "Error in maximum Depth: "+e.getMessage();
				return false;
			}
		}
		
		if (min != null && max != null && min.isAfter(max))
		{
			errormsg = "Error in Date: Minimum date after maximum";
			return false;
		}
		
		errormsg = "no errors";
		return true;
	}

	@Override
	public void setFocus(DataPoint d) {
		// No-oP
	}

	@Override
	public void reset() {
		minDate.setText("");
		maxDate.setText("");
	}

}
