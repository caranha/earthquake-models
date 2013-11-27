package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jp.ac.tsukuba.cs.conclave.earthquake.gui.GuiTester;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapDrawCommand;

public class DrawElementPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6402669155991569383L;

	MapDrawCommand element;
	JTextField name;
	JCheckBox visible;
	JButton mainColor;
	JButton secondaryColor;
	JButton close;
	
	
	public DrawElementPanel(MapDrawCommand e)
	{
		JPanel aux;
		element = e;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setMaximumSize(new Dimension(200,70));
		
		visible = new JCheckBox();
		visible.setActionCommand("visible");
		visible.setSelected(element.isDrawable());
		visible.addActionListener(this);
		
		name = new JTextField(13);
		name.setText(element.getName());
		name.setActionCommand("namechange");
		name.addActionListener(this);

		mainColor = new JButton("1st");
		mainColor.setBackground(element.getMainColor());
		mainColor.setActionCommand("maincolor");
		mainColor.addActionListener(this);
		
		secondaryColor = new JButton("2nd");
		secondaryColor.setBackground(element.getSubColor());
		secondaryColor.setActionCommand("secondarycolor");
		secondaryColor.addActionListener(this);
		
		close = new JButton("X");
		close.setActionCommand("close");
		close.addActionListener(this);
		
		aux = new JPanel();
		aux.add(visible);
		aux.add(name);
		this.add(aux);

		aux = new JPanel();
		aux.add(mainColor);
		aux.add(secondaryColor);
		aux.add(close);
		this.add(aux);
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == "close")
		{
			// TODO: should access and update the model, not the map controller
			GuiTester.model.removeDrawCommand(element);
			return;
		}
		if (arg0.getActionCommand() == "visible")
		{
			element.setDrawable(visible.isSelected());
			// FIXME: should update the parent instead;
			GuiTester.model.updateDrawCommand();
			return;
		}
		if (arg0.getActionCommand() == "namechange")
		{
			element.setName(name.getText());
			return;
		}
		if (arg0.getActionCommand() == "maincolor")
		{
			Color c = JColorChooser.showDialog(this,
					"Choose main color",
					element.getMainColor());
			if (c != null)
			{
				element.setMainColor(c);
				mainColor.setBackground(c);
				
			}
			// FIXME: should update the parent instead;
			GuiTester.model.getMapController().redrawMap();
			return;
		}
		if (arg0.getActionCommand() == "secondarycolor")
		{
			Color c = JColorChooser.showDialog(this,
					   "Choose secondary color",
					   element.getSubColor());
			if (c != null)
			{
				element.setSubColor(c);
				secondaryColor.setBackground(c);

			}
			// FIXME: should update the parent instead;
			GuiTester.model.getMapController().redrawMap();
			return;
		}

		System.err.println("Action not handled: "+arg0.getActionCommand() + " " + arg0.paramString());
	}
}
