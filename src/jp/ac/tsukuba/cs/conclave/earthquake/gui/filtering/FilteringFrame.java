package jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.EarthquakeDisplayModel;

public class FilteringFrame extends JInternalFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9032992447491057074L;
	
	static final int width = 300;
	static final int height = 600;

	EarthquakeDisplayModel model;
	
	CompositeEarthquakeFilter currentFilter;
	
	FilterComponent magFilter;
	FilterComponent depthFilter;
	FilterComponent modelFilter;
	FilterComponent dateFilter;
	
	
	public FilteringFrame(EarthquakeDisplayModel m)
	{
		super("Event Filter", false, false, false, true);
		model = m;
		
		currentFilter = null;
		
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
		JPanel ret = new JPanel();
		ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));
		
		magFilter = new MagnitudeFilterComponent();
		depthFilter = new DepthFilterComponent();
		modelFilter = new ModelFilterComponent();
		dateFilter = new DateFilterComponent();
		
		ret.add(magFilter.getPanel());
		ret.add(depthFilter.getPanel());
		ret.add(dateFilter.getPanel());
		ret.add(modelFilter.getPanel());
		
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
	
	
	
	void updateFilter()
	{
		if (currentFilter != null)
			model.filterData(currentFilter);
	}
	
	private void resetFields()
	{
		magFilter.reset();
		depthFilter.reset();
		modelFilter.reset();
		dateFilter.reset();
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
		
		if (!dateFilter.isEmpty())
		{
			if (!dateFilter.isCorrect())
			{
				JOptionPane.showMessageDialog(this, dateFilter.getErrorString());
				return null;
			}
			ret.addFilter(dateFilter.getFilter());
		}
		
		if (!modelFilter.isEmpty())
		{
			if (!modelFilter.isCorrect())
			{
				JOptionPane.showMessageDialog(this, modelFilter.getErrorString());
				return null;
			}
			ret.addFilter(modelFilter.getFilter());
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

}
