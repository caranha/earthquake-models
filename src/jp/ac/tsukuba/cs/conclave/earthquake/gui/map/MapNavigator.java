package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * This class implements navigation controls for the GUI map:
 * Zooming, moving up/down/left/right
 * @author caranha
 *
 */

public class MapNavigator extends JPanel {
	
	JButton zoomIn;
	JButton zoomOut;
	JButton moveUp;
	JButton moveDown;
	JButton moveLeft;
	JButton moveRight;
	
	public MapNavigator(int w, int h, ActionListener target)
	{
		super();
		this.setPreferredSize(new Dimension(w,h));
		createButtons(target);
		initLayout();		
	}
	
	/**
	 * Initialize the buttons on this navigator;
	 * @param target
	 */
	private void createButtons(ActionListener target)
	{
		zoomIn = new JButton("+");
		zoomIn.addActionListener(target);
		zoomOut = new JButton("-");
		zoomOut.addActionListener(target);
		
		moveUp = new BasicArrowButton(SwingConstants.NORTH);
		moveUp.setActionCommand("up");
		moveUp.addActionListener(target);
		
		
		moveDown = new BasicArrowButton(SwingConstants.SOUTH);
		moveDown.setActionCommand("down");
		moveDown.addActionListener(target);
		
		moveRight = new BasicArrowButton(SwingConstants.EAST);
		moveRight.setActionCommand("right");
		moveRight.addActionListener(target);

		moveLeft = new BasicArrowButton(SwingConstants.WEST);
		moveLeft.setActionCommand("left");
		moveLeft.addActionListener(target);
	}
	
	/**
	 * Sets the initial layout of this navigator
	 * 
	 * TODO: Set the sizes of the arrows to something fixed and a bit bigger.
	 * 
	 */
	private void initLayout()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel plusminus = new JPanel();
		plusminus.add(zoomIn);
		plusminus.add(zoomOut);
		this.add(plusminus);
		
		JPanel arrows = new JPanel();
		JPanel vertarrows = new JPanel();
		vertarrows.setLayout(new BoxLayout(vertarrows, BoxLayout.Y_AXIS));
		
		arrows.add(moveLeft);
		vertarrows.add(moveUp);
		vertarrows.add(moveDown);
		arrows.add(vertarrows);
		arrows.add(moveRight);
		this.add(arrows);

	}
	
}
