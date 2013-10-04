package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import javax.swing.JPanel;

/**
 * This class packages a MapImage and a MapController for display in the GUI;
 * @author caranha
 *
 */
public class MapPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5205387903071024831L;

	MapImage image;
	
	public MapPanel()
	{
		super();
		// TODO: Change this constructor to add padding between the image and the pane
		image = new MapImage(this.getWidth(), this.getHeight(), 20,120,21,121);
	}
	
	
}
