package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import jp.ac.tsukuba.cs.conclave.earthquake.gui.EarthquakeDisplayModel;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapController;

/**
 * This panel displays a map and its control buttons.
 * It holds a map controller to display.
 * @author caranha
 *
 */
public class MapPanel extends JInternalFrame implements ActionListener, Observer{

	// CONSTANTS
	final static int buttonBarWidth = 100;
	final static int mapDisplayWidth = 400;
	final static int mapDisplayHeight = 400;
	
	// Subpanels
	MapDisplayPanel mapdisplaypanel;
	MapNavigator mapnavigatorpanel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3396137586798855249L;
	
	public MapPanel(EarthquakeDisplayModel model)
	{
		super("Map", false, false, false, true);
		model.addObserver(this);
		
		mapdisplaypanel = new MapDisplayPanel(mapDisplayWidth,mapDisplayHeight,model);
		mapnavigatorpanel = new MapNavigator(buttonBarWidth,mapDisplayHeight,this);
		
		// Setting the layout of the InternalPane;
		JPanel aux = new JPanel();
		aux.setSize(new Dimension(buttonBarWidth+mapDisplayWidth,mapDisplayHeight));
		aux.setLayout(new BoxLayout(aux, BoxLayout.X_AXIS));
		aux.add(mapnavigatorpanel); 
		aux.add(new JSeparator(SwingConstants.VERTICAL));
		aux.add(mapdisplaypanel);
		
		add(aux);
		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == "+")
		{
			mapdisplaypanel.multiplyZoom(1.1);
			mapdisplaypanel.repaint();
			return;
		}
		
		if (e.getActionCommand() == "-")
		{
			mapdisplaypanel.multiplyZoom(0.9);
			mapdisplaypanel.repaint();
			return;
		}
		
		if (e.getActionCommand() == "down")
		{
			mapdisplaypanel.moveMap(0, +1);
			mapdisplaypanel.repaint();
			return;
		}
		
		if (e.getActionCommand() == "up")
		{
			mapdisplaypanel.moveMap(0, -1);
			mapdisplaypanel.repaint();
			return;
		}
		
		if (e.getActionCommand() == "left")
		{
			mapdisplaypanel.moveMap(-1,0);
			mapdisplaypanel.repaint();
			return;
		}
		
		if (e.getActionCommand() == "right")
		{
			mapdisplaypanel.moveMap(1, 0);
			mapdisplaypanel.repaint();
			return;
		}
		
		System.out.println(e.getActionCommand());		
	}



	@Override
	public void update(Observable o, Object arg) {
		mapdisplaypanel.repaint();
	}

}
