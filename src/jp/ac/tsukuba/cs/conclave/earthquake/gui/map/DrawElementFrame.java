package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jp.ac.tsukuba.cs.conclave.earthquake.gui.EarthquakeDisplayModel;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapDrawCommand;

/**
 * 
 * This Frame holds a list of Drawing Elements currently existing in the 
 * map, and allows the user to interact with them. Supports operations 
 * such as "remove draw element", "change its color", etc.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class DrawElementFrame extends JInternalFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8242298617323037040L;

	EarthquakeDisplayModel model;

	JScrollPane scrollist;
	Box box;
	
	public DrawElementFrame(EarthquakeDisplayModel m)
	{
		super("Drawing Control", false, false, false, true);

		model = m;
		model.addObserver(this);
		
		scrollist = new JScrollPane();
		scrollist.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollist.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setPreferredSize(new Dimension(200,350));
        scrollist.setPreferredSize(new Dimension(200,300));
        box = Box.createVerticalBox();
        scrollist.getViewport().add(box);
		
		
		
		updateList();
		
		// Setting the layout of the InternalPane;
		JPanel aux = new JPanel();
		aux.setSize(new Dimension(100,30));
		aux.add(scrollist);
		add(aux);
		pack();
	}

	void updateList()
	{
		// TODO: The Scroll bar button does not appear automatically
		box.removeAll(); // There must be something better than this
		for (MapDrawCommand aux: model.getMapController())
		{
			box.add(new DrawElementPanel(aux));
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 != null && (String)arg1 == "Map Controller")
		{
			updateList();
			box.validate();
			repaint();
		}
	}


}
