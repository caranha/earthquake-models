package jp.ac.tsukuba.cs.conclave.earthquake.gui.old;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.faultmodel.FMBase;
import jp.ac.tsukuba.cs.conclave.earthquake.faultmodel.FMStatModel;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.GeoUtils;

import org.joda.time.Duration;
import org.joda.time.format.ISODateTimeFormat;

public class ModelPanel implements ActionListener,ListSelectionListener {

	// main panel
	JPanel pane;

	// subpanels for grouping
	JPanel quakeselector;
	JPanel datadisplay;
	JPanel imagedisplay;

	// quakeselector data elements;
	JTextField minmag;
	JTextField maxmag;
	JButton searchquake;
	
	JList<String> list;
	JTextField time;
	JComboBox<String> timetype;
	JButton calculateModel;

	// data display data fields
	JTextArea origininfo;
	JTextArea modeldata;
	JList<String> aftershockdata;
	
	// Graphical Data
	EarthQuakeDrawable mappane;
	
	
	public ModelPanel()
	{
		pane = new JPanel();
		pane.setLayout(new BoxLayout(pane,BoxLayout.X_AXIS));

		
		quakeselector = new JPanel();
		quakeselector.setLayout(new BoxLayout(quakeselector,BoxLayout.Y_AXIS));
		
		initQuakeFilterButtons();
		quakeselector.add(new JSeparator());
		initQuakeSelectionButtons();
		
		datadisplay = new JPanel();
		datadisplay.setLayout(new BoxLayout(datadisplay,BoxLayout.Y_AXIS));
		initTextDisplay();
		initImageDisplay();
		
		pane.add(quakeselector);
		pane.add(new JSeparator(JSeparator.VERTICAL));
		pane.add(datadisplay);
	}
	
	private void initImageDisplay() {
	
		imagedisplay = new JPanel();
		imagedisplay.setLayout(new BoxLayout(imagedisplay,BoxLayout.X_AXIS));
		
		mappane = new EarthQuakeDrawable();
		imagedisplay.add(mappane);
		
		datadisplay.add(imagedisplay);
	}

	private void initTextDisplay() {
		Border space = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		
		JPanel basedata = new JPanel();
		
		origininfo = new JTextArea(10,40);
		origininfo.setEditable(false);
		JScrollPane originscroll = new JScrollPane(origininfo);
		originscroll.setBorder(space);
		
		modeldata = new JTextArea(10,40);
		modeldata.setEditable(false);
		JScrollPane modelscroll = new JScrollPane(modeldata);
		modelscroll.setBorder(space);
		
		
		aftershockdata = new JList<String>();
		aftershockdata.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		aftershockdata.setLayoutOrientation(JList.VERTICAL);
		aftershockdata.setVisibleRowCount(10);
		aftershockdata.addListSelectionListener(this);
		JScrollPane asdscroll = new JScrollPane(aftershockdata);
		asdscroll.setBorder(space);
		
		
		
		
		basedata.add(originscroll);
		basedata.add(modelscroll);
		datadisplay.add(basedata);
		
		datadisplay.add(new JLabel("Time   Mag   Dist1   Dist2   Model"));
		datadisplay.add(asdscroll);
		datadisplay.add(new JSeparator());
	}

	public JPanel getPanel()
	{
		return pane;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		DataContext c = DataContext.getInstance();
		
		// Search button pressed
		if (e.getSource() == searchquake)
		{
			c.setEventSelection(Double.parseDouble(minmag.getText()), Double.parseDouble(maxmag.getText()));
			
			String[] slist = new String[c.eventselection.size()];
			for (int i = 0; i < slist.length; i++)
			{
				slist[i] = c.fmdata.data.get(c.eventselection.get(i)).time.toString(ISODateTimeFormat.basicDateTimeNoMillis());
				slist[i] += " Mag: "+c.fmdata.data.get(c.eventselection.get(i)).magnitude;
			}
			list.setListData(slist);
		}
		
		// Calculate Model button pressed
		if (e.getSource() == calculateModel)
		{
			int select = list.getSelectedIndex();
			if (select == -1)
				return;
			
			c.origin = c.fmdata.data.get(c.eventselection.get(select));
			c.modelBase.init(c.origin);
			
			Duration timeLimit = null;
			switch(timetype.getSelectedIndex())
			{
			case 0:
				timeLimit = Duration.standardMinutes(Integer.parseInt(time.getText()));
				break;
			case 1:
				timeLimit = Duration.standardHours(Integer.parseInt(time.getText()));
				break;
			case 2:
				timeLimit = Duration.standardDays(Integer.parseInt(time.getText()));
				break;
			}
			
			c.modelBase.fillAfterShockList(c.total, timeLimit);
			
			String origindata = c.origin.dump()+"\n\n";
			origindata = origindata + "Aftershock distance: "+FMBase.getAftershockRadius(c.origin.magnitude);
			origindata = origindata + "\nNumber of Aftershocks: "+c.modelBase.getAfterShockList().size();
						
			origininfo.setText(origindata);
			
			String modelstring="";
			for (int i = 0; i < 2; i++)
			{
				c.models[i] = new FMStatModel(c.origin, c.origin.S[i],c.origin.D[i],c.origin.R[i]);
				c.models[i].loadAftershocks(c.modelBase.getAfterShockList());
				c.models[i].calcDistModel();
				c.models[i].calcMErrorModel();
				c.models[i].calcStrikeModel();
				
				modelstring = modelstring + "Model "+(i+1)+":\n";
			    modelstring = modelstring + "Average Distance: "+c.models[i].distanceAvg+" +-"+Math.sqrt(c.models[i].distanceDev)+"\n";
			    modelstring = modelstring + "Strike Error: "+c.models[i].strikeAvg+" +-"+Math.sqrt(c.models[i].strikeDev)+"\n";
			    modelstring = modelstring + "Model Error: "+c.models[i].modelAvg+" +-"+Math.sqrt(c.models[i].modelDev)+"\n";
			    modelstring = modelstring + "\n";
			}
			
		    modeldata.setText(modelstring);
		    

		    DataList aslist = c.modelBase.getAfterShockList();
		    String[] aftershocks = new String[aslist.size()];

		    
		    for(int i = 0; i < aslist.size();i++)	
		    {
		    	String tmps = "";
		    	DataPoint tmp = aslist.data.get(i);
		    	tmps += tmp.time.toString(ISODateTimeFormat.basicTimeNoMillis())+"  ";
		    	tmps += tmp.magnitude+"  ";
		    	tmps += GeoUtils.crossTrackDistance(c.origin.latitude, c.origin.longitude, 
		    											   c.origin.S[0], tmp.latitude, tmp.longitude)+"  ";
		    	tmps += GeoUtils.crossTrackDistance(c.origin.latitude, c.origin.longitude, 
		    											   c.origin.S[1], tmp.latitude, tmp.longitude)+"  ";
		    	if (tmp.FM)
		    	{
		    		tmps += "M1- S"+tmp.S[0]+" D"+tmp.D[0]+" R"+tmp.R[0] + 
		    					   " M2- S"+tmp.S[1]+" D"+tmp.D[1]+" R"+tmp.R[1];
		    	}
		    	aftershocks[i] = tmps;
		    }
		    aftershockdata.setListData(aftershocks);
		    
		    mappane.addOrigin(c.origin);
		    mappane.addAfterShocks(aslist);
		    mappane.repaint();
		    
		    
			
		}
		
		
	}

	
	private void initQuakeFilterButtons()
	{
		JPanel quakeselectorButtons = new JPanel(); // defaults to flow layout;		
		
		JLabel minl = new JLabel("Minimum Magnitude: ");
		minmag = new JTextField("6");
		minmag.setToolTipText("Minimum Magnitude for Events");
		minmag.setColumns(3);
		
		JLabel maxl = new JLabel("Maximum Magnitude: ");
		maxmag = new JTextField("7");
		maxmag.setToolTipText("Maximum Magnitude for Events");
		maxmag.setColumns(3);
		
		quakeselectorButtons.add(minl);
		quakeselectorButtons.add(minmag);
		quakeselectorButtons.add(maxl);
		quakeselectorButtons.add(maxmag);
		
		searchquake = new JButton("Search");
		searchquake.addActionListener(this);
		
		quakeselector.add(new JLabel("Base Event Selection:"));
		quakeselector.add(new JSeparator());
		quakeselector.add(quakeselectorButtons);
		quakeselector.add(searchquake);
	}
	
	private void initQuakeSelectionButtons()
	{
		list = new JList<String>(); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(100, 200));
		quakeselector.add(listScroller);
		


		time = new JTextField("1");
		time.setToolTipText("Time for aftershock search");
		time.setColumns(3);

		String[] timeStrings = { "Minutes", "Hours", "Days"};
		timetype = new JComboBox<String>(timeStrings);
		timetype.setSelectedIndex(0);

		JPanel timeselection = new JPanel();
		timeselection.add(new JLabel("Time"));
		timeselection.add(time);
		timeselection.add(timetype);
		quakeselector.add(timeselection);
		
		calculateModel = new JButton("Calculate Models");
		calculateModel.addActionListener(this);
		quakeselector.add(calculateModel);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
