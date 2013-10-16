package jp.ac.tsukuba.cs.conclave.earthquake.gui.datalist;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.joda.time.format.ISODateTimeFormat;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;


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
public class DataListFrame extends JInternalFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2499175237283592904L;

	// InternalData
	ArrayList<EarthquakeFocusListener> focusListeners;
	DataList originalData;
	DataList filteredData;
	DataPoint focusEarthquake;

	// Display Elements
	JButton resetButton;
	JButton displayButton;
	JButton focusButton;
	
	JPanel focusDisplay;
	JList<String> filterList;
	
	
	public DataListFrame(DataList d)
	{
		super("Event List", false, false, false, true);
		originalData = d;
		filteredData = d;
		focusEarthquake = null;
		
		
		// Setting the layout of the InternalPane;
		JPanel aux = new JPanel();
		aux.setSize(new Dimension(200,600));
		aux.setLayout(new BoxLayout(aux, BoxLayout.Y_AXIS));
		aux.add(initButtons());
		aux.add(initFilterList());
		
				
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
		
		return ret;
	}
	
	private JScrollPane initFilterList()
	{
		filterList = new JList<String>();
		filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filterList.setLayoutOrientation(JList.VERTICAL);
		//filterList.setVisibleRowCount(10);
		
		
		filterList.setListData(getFilteredListString(filteredData));
		JScrollPane ret = new JScrollPane(filterList);		
		return ret;
	}
	
	/**
	 * Gets a String[] of the basic info from all the filtered
	 * @param d
	 * @return
	 */
	String[] getFilteredListString(DataList d)
	{
		if (d == null)
			return new String[0];
					
		String[] ret = new String[d.size()];
		Iterator<DataPoint> it = d.iterator();
		int i = 0;
		
		while (it.hasNext())
		{
			String aux = "";
			
			DataPoint DPaux = it.next();
			
			aux = DPaux.time.toString(ISODateTimeFormat.date()) + " | ";
			aux+= DPaux.time.toString(ISODateTimeFormat.hourMinuteSecond()) + " | ";
			aux+= "M"+DPaux.magnitude;
			
			ret[i] = aux;
			i++;
		}
		return ret;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == "Reset List")
		{
			filteredData = originalData;
			return;
		}
		
	}

}
