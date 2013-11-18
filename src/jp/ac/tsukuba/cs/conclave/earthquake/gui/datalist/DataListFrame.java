package jp.ac.tsukuba.cs.conclave.earthquake.gui.datalist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import org.joda.time.format.ISODateTimeFormat;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.EarthquakeDisplayModel;
import jp.ac.tsukuba.cs.conclave.earthquake.image.DrawEarthquakeList;


/**
 * The Data List Frame holds all the earthquake data.
 * 
 * It shows a sub-list from all the earthquakes.
 * It allows double clicking this list to select one earthquake
 * And it announces the doubleclicking of the one earthquake to all subscribed methods
 * 
 * @author caranha
 *
 */
public class DataListFrame extends JInternalFrame implements ActionListener,Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2499175237283592904L;

	static final int width = 200;
	static final int height = 600;

	EarthquakeDisplayModel model;
	
	// Display Elements
	JButton resetButton;
	JButton displayButton;
	JButton focusButton;
	
	JTextArea listSizeDisplay;
	JList<String> filterList;
	
	
	public DataListFrame(EarthquakeDisplayModel m)
	{
		super("Event List", false, false, false, true);
		model = m;
		model.addObserver(this);
		
		// Setting the layout of the InternalPane;
		JPanel aux = new JPanel();
		//aux.setSize(new Dimension(width,height));
		aux.setLayout(new BoxLayout(aux, BoxLayout.Y_AXIS));
		aux.add(initButtons());
		aux.add(Box.createRigidArea(new Dimension(0,5)));
		aux.add(initTextArea());
		aux.add(Box.createRigidArea(new Dimension(0,5)));
		aux.add(initFilterList());
		aux.add(focusButton);

		add(aux);
		pack();
	}
	
	private JPanel initButtons()
	{
		JPanel ret = new JPanel();
		resetButton = new JButton("Reset List");
		resetButton.addActionListener(this);
		displayButton = new JButton("Display All");
		displayButton.addActionListener(this);
		ret.add(resetButton);
		ret.add(displayButton);

		focusButton = new JButton("Change Focus");
		focusButton.addActionListener(this);
		focusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		return ret;
	}
	
	private JScrollPane initFilterList()
	{
		filterList = new JList<String>();
		filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filterList.setLayoutOrientation(JList.VERTICAL);
		filterList.setVisibleRowCount(15);
		
		
		filterList.setListData(getFilteredListString(model.getFilteredData()));
		JScrollPane ret = new JScrollPane(filterList);		
		return ret;
	}
	
	private JTextArea initTextArea()
	{
		listSizeDisplay = new JTextArea();
		listSizeDisplay.setText("Events Listed: "+Integer.toString(model.getFilteredData().size()));
		listSizeDisplay.setEditable(false);
		listSizeDisplay.setLineWrap(true);
		
		listSizeDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		return listSizeDisplay;
	}


	// TODO: Remove the next two functions from here, and replace them somewhere else
	
	/**
	 * Gets a String[] of the basic info from all the filtered
	 * @param d
	 * @return
	 */
	public String[] getFilteredListString(DataList d)
	{
		if (d == null)
			return new String[0];
					
		String[] ret = new String[d.size()];
		Iterator<DataPoint> it = d.iterator();
		int i = 0;
		
		while (it.hasNext())
		{
			ret[i] = getEarthquakeShortString(it.next());
			i++;
		}
		return ret;
	}
	
	private String getEarthquakeShortString(DataPoint d)
	{
		String aux = "";
		
		aux = d.time.toString(ISODateTimeFormat.date()) + " | ";
		aux+= d.time.toString(ISODateTimeFormat.hourMinuteSecond()) + " | ";
		aux+= "M"+d.magnitude;
		
		return aux;
	}

	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == "Reset List")
		{
			model.resetFilteredList();
			return;
		}
		if (arg0.getActionCommand() == "Change Focus")
		{
			
			int aux = filterList.getSelectedIndex();
			if (aux != -1)
			{
				// Fixme change DataList to allow indexed access
				model.setFocusEarthquake(model.getFilteredData().data.get(aux));
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Select an Earthquake First");
			}
			return;
		}
		if (arg0.getActionCommand() == "Display All")
		{
			DrawEarthquakeList aux = new DrawEarthquakeList(model.getFilteredData().iterator());
			aux.setMainColor(Color.RED);
			model.addDrawCommand(aux);
			return;
		}
		
		System.err.println("Unhandled ActionEvent: "+arg0.getActionCommand());
	}

	@Override
	public void update(Observable o, Object arg) {		
		if (arg != null && (String)arg == "Filtered List")
		
		filterList.clearSelection();
		filterList.setListData(getFilteredListString(model.getFilteredData()));
		listSizeDisplay.setText("Events Listed: "+Integer.toString(model.getFilteredData().size()));
	}


	
	
}
