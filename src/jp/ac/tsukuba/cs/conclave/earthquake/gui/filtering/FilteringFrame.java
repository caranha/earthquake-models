package jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.MagnitudeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.datalist.EarthquakeFocusListener;

public class FilteringFrame extends JInternalFrame implements ActionListener, EarthquakeFocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9032992447491057074L;
	
	static final int width = 300;
	static final int height = 600;

	ArrayList<FilterListener> listeners;
	
	CompositeEarthquakeFilter currentFilter;
	DataPoint focusEarthquake;
	
	FilterComponent magFilter;
	FilterComponent depthFilter;
	
	JTextField minDate;
	JTextField maxDate;
	JCheckBox hasFaultMechanism;
	
	public FilteringFrame()
	{
		super("Event Filter", false, false, false, true);
		
		listeners = new ArrayList<FilterListener>();
		currentFilter = null;
		focusEarthquake = null;
		
		// Setting the layout of the InternalPane;
		JPanel aux = new JPanel();
		//aux.setSize(new Dimension(width,height));
		aux.setLayout(new BoxLayout(aux, BoxLayout.Y_AXIS));

		/////// Simple Filters:
		aux.add(initSimpleFilters());

		/////// Complex Filters
		// Min Number of Nearby Events
		// Max Number of Nearby Events
		// After X Time
		// Within Y km radius
		// Within the Fault Plane Projection
		// Within the Fault Plane Line
		
		/////// Focus Filters
		// Min/Max number of Nearby Evens
		// After X Time
		// Within Y km radius
		// Within the Fault Plane Projection
		// Within the Fault Plane Line

		// Reset and Filter Buttons
		aux.add(Box.createRigidArea(new Dimension(0,5)));
		aux.add(initButtons());

		add(aux);
		pack();
	}
	
	/**
	 * Initiates text boxes for Magnitude, Depth and Date
	 * @return
	 */
	private JPanel initSimpleFilters()
	{
		JPanel aux;
		JPanel ret = new JPanel();
		ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));
		
		magFilter = new MagnitudeFilterComponent();
		depthFilter = new DepthFilterComponent();
		
		ret.add(magFilter.getPanel());
		ret.add(depthFilter.getPanel());
		
		// Date
		aux = new JPanel();
		aux.add(new JLabel("Date (YYYY-MM-DD [HH:[MM:[SS]]])"));
		ret.add(aux);
		
		aux = new JPanel();
		
		minDate = new JTextField(12);
		maxDate = new JTextField(12);
		aux.add(new JLabel("Start: "));
		aux.add(minDate);
		aux.add(new JLabel(" End: "));
		aux.add(maxDate);
		ret.add(aux);
		
		// has Focal Mechanism
		aux = new JPanel();
		hasFaultMechanism = new JCheckBox("Only events with FM Data", false);
		aux.add(hasFaultMechanism);
		ret.add(aux);
		ret.add(new JSeparator(JSeparator.HORIZONTAL));
		
		return ret;
	}
	
	private JPanel initButtons()
	{
		JPanel ret = new JPanel();
		JButton aux1 = new JButton("Filter Events");
		JButton aux2 = new JButton("Reset Filter");
		aux1.addActionListener(this);
		aux2.addActionListener(this);
		ret.add(aux1);
		ret.add(aux2);
		return ret;
	}
	
	
	/**
	 * Adds a new listener that will be updated when the "Filter Results" button is clicked
	 * @param l
	 */
	public void addFilterListener(FilterListener l)
	{
		listeners.add(l);
	}
	
	void updateFilter()
	{
		if (currentFilter != null)
		for (FilterListener aux: listeners)
		{
			aux.filterChanged(currentFilter);
		}
	}
	
	
	
	private void resetFields()
	{
		magFilter.reset();
		depthFilter.reset();
				
		minDate.setText("");
		maxDate.setText("");
		hasFaultMechanism.setSelected(false);
	}
	
	/**
	 * Validates all the fields of the Filter pane, assembling the filter as it goes along.
	 * Empty fields are ignored
	 * @return
	 */
	private CompositeEarthquakeFilter validateFilter()
	{
		CompositeEarthquakeFilter ret = new CompositeEarthquakeFilter();

		// SimpleMagnitude;
		if (!magFilter.isEmpty())
		{
			if (!magFilter.isCorrect())
			{
				JOptionPane.showMessageDialog(this, magFilter.getErrorString());
				return null;
			}
			ret.addFilter(magFilter.getFilter());
		}		
		
		if (!depthFilter.isEmpty())
		{
			if (!depthFilter.isCorrect())
			{
				JOptionPane.showMessageDialog(this, depthFilter.getErrorString());
				return null;
			}
			ret.addFilter(depthFilter.getFilter());
		}
		
		
		
		
		if (ret.isEmpty())
			return null;
		else
			return ret;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getActionCommand() == "Reset Filter")
		{
			resetFields();
			return;
		}
		
		if (arg0.getActionCommand() == "Filter Events")
		{
			CompositeEarthquakeFilter aux = validateFilter();
			if (aux != null)
			{
				currentFilter = aux;
				updateFilter();				
			}
			return;
		}
		
		System.out.println(arg0.getActionCommand());
	}

	@Override
	public void focusChanged(DataPoint d) {
		focusEarthquake = d;
				
		// The focus earthquake changed, so I need to change the forms
		// regarding filtering for aftershocks
		// TODO: Change text in forms regarding Focus Earthquake
	}

}