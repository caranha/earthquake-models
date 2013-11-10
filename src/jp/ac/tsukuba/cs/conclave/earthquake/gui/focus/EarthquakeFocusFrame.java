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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.joda.time.format.ISODateTimeFormat;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.EarthquakeDisplayModel;
import jp.ac.tsukuba.cs.conclave.earthquake.image.DrawBothFaultPlanes;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapDrawCommand;

public class EarthquakeFocusFrame extends JInternalFrame implements Observer, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5396128292212358111L;
	
	static final int width = 300;
	static final int height = 600;

	DataPoint focus;
	EarthquakeDisplayModel model;
	
	JTextArea quakeDetails;
	JTextArea quakeRadius;
	
	
	JButton bSetDistance;
	JButton bDisplayRadius;
	JButton bDisplayPlane;
	JButton bBookMark;
	
	
	
	public EarthquakeFocusFrame(EarthquakeDisplayModel m)
	{
		// TODO: Fix alignment of Components in this window
		super("Earthquake Focus", false, false, false, true);
		model = m;
		m.addObserver(this);
		
		// Setting the layout of the InternalPane;
		JPanel aux = new JPanel();
		aux.setLayout(new BoxLayout(aux, BoxLayout.Y_AXIS));
		JPanel foo;
		
		// Add the earthquake info
		quakeDetails = new JTextArea();
		quakeDetails.setEditable(false);
		quakeDetails.setLineWrap(true);
		quakeDetails.setRows(10);
		
		
		aux.add(quakeDetails);
		
		// Add the aftershock radius textbox
		foo = new JPanel();
		JLabel bar = new JLabel("Aftershock Radius (km): ");
		quakeRadius = new JTextArea();
		quakeRadius.setColumns(5);
		bSetDistance = new JButton("Set");
		bSetDistance.addActionListener(this);
		
		
		foo.add(bar);
		foo.add(quakeRadius);
		foo.add(bSetDistance);
		aux.add(foo);
		
		// Add the display radius/plane buttons
		foo = new JPanel();
		bDisplayRadius = new JButton("Display Aftershock Radius");
		bDisplayRadius.addActionListener(this);
		
		
		bDisplayPlane = new JButton("Display Fault Plane");
		bDisplayPlane.addActionListener(this);

		
		foo.add(bDisplayRadius);
		foo.add(bDisplayPlane);
		aux.add(foo);
		
		// Add bookmark button
		bBookMark = new JButton("Bookmark Event");
		bBookMark.addActionListener(this);
		aux.add(bBookMark);
		
		
		add(aux);
		pack();
	}

	/**
	 * Changes the earthquake currently focused by this Frame
	 * 
	 * @param dataPoint 
	 */
	private void setFocusQuake(DataPoint f) {
		focus = f;
		
		if (focus != null)
		{
			setFocusText(focus);
		}
		else
			quakeDetails.setText("");
				
	}
	
	private void setFocusText(DataPoint f)
	{
		// TODO this is too tied down to the DataPoint Internals
		// Change it so that the Data Point provides its data in human readable format
		
		String focusText = "";
		
		focusText += "Date: "+ 
				ISODateTimeFormat.date().print(f.time)+"  "+
				ISODateTimeFormat.hourMinuteSecond().print(f.time)+"\n\n";
		
		focusText += "Longitude: "+f.longitude + "  " +
				"Latitude: "+f.latitude+"\n";

		focusText += "Magnitude: "+f.magnitude+ "   ";
		focusText += "Depth: "+f.depth+"\n\n";

		if (f.hasFaultModel())
		{
			focusText += "Fault Mechanism:\n";
			focusText += "  Model 1 - Strike: "+f.S[0]+" Dip: "+f.D[0]+" Rack: "+f.R[0]+"\n";
			focusText += "  Model 2 - Strike: "+f.S[1]+" Dip: "+f.D[1]+" Rack: "+f.R[1]+"\n";
		}
		else
		{
			focusText += "No Fault Mechanism Information (Probably JMA)";
		}
		
		
		quakeDetails.setText(focusText);
	}
	
	private void setQuakeRadius(Double d) {
		quakeRadius.setText(String.format( "%.2f", d));
	}

	

	@Override
	public void update(Observable o, Object arg)
	{
		if (arg != null && (String)arg == "Focus Earthquake")
		{
			setFocusQuake(model.getFocusEarthquake());
		}
		if (arg != null && (String)arg == "Aftershock Distance")
		{
			setQuakeRadius(model.getAfterShockDistance());
		}
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getActionCommand()=="Set")
		{
			// TODO: Data validation!
			try {
				model.setAfterShockDistance(Double.parseDouble(quakeRadius.getText()));
			} catch(Exception e)
			{
				JOptionPane.showMessageDialog(this, "Invalid Value - please enter a number");
			}
			return;
		}

		// TODO: Event - Display Radius
		if (arg0.getActionCommand()=="Display Aftershock Radius")
		{
			
		}
			
		if (arg0.getActionCommand()=="Display Fault Plane")
		{
			if (focus != null && focus.hasFaultModel() == true)
			{				
				MapDrawCommand aux = new DrawBothFaultPlanes(focus,model.getAfterShockDistance());
				aux.setMainColor(Color.GREEN);
				aux.setSubColor(Color.BLUE);
				model.addDrawCommand(aux);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Select a non-JMA Earthquake First!");
			}
			return;
		}

		// TODO: Event - Set Bookmark
		if (arg0.getActionCommand()=="Bookmark Event")
		{
			
		}
			
		System.out.println("Action not handled: "+arg0.getActionCommand());
	}

}
