package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import jp.ac.tsukuba.cs.conclave.earthquake.image.MapController;

/**
 * This panel displays a map and its control buttons.
 * It holds a map controller to display.
 * @author caranha
 *
 */
public class MapPanel extends JPanel implements ActionListener{

	// CONSTANTS
	final static int buttonBarWidth = 150;
	
	// Attributes
	MapController map;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3396137586798855249L;
	
	public MapPanel(MapController m)
	{
		super();
		map = m;
		
		// Setting the layout of the panel;
		this.setMinimumSize(new Dimension(buttonBarWidth+200,200));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		// Temporary, replace with button panel
		this.add(Box.createRigidArea(new Dimension(buttonBarWidth,200))); 
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(new MapDisplayPanel(200,200,m.getImage()));		
	}
	
	
	
	public void setMapController(MapController m)
	{
		map = m;
	}
	public MapController getMapController()
	{
		return map;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
