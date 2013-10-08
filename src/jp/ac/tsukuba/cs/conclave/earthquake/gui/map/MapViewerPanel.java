package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class MapViewerPanel extends JPanel {

	final int buttonBarWidth = 150;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3396137586798855249L;
	
	public MapViewerPanel()
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		// Temporary, replace with button panel
		this.add(Box.createRigidArea(new Dimension(buttonBarWidth,200))); 
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(new MapPanel(200,200));		
	}
	

}
