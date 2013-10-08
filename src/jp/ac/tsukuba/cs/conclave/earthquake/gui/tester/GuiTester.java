package jp.ac.tsukuba.cs.conclave.earthquake.gui.tester;


import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoDataReader;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoLine;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.map.MapPanel;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.map.MapViewerPanel;

public class GuiTester {

	
	public static void main(String[] args) {

		loadJapanMap();
		
		
        JFrame frame = new JFrame("Earthquake GUI Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        MapViewerPanel p1 = new MapViewerPanel();       

        frame.add(p1);
        frame.pack();	

        //Display the window.
        frame.setVisible(true);
	}
	
	static ArrayList<GeoLine> loadJapanMap()
	{
		BufferedReader reader = null;
		String filename = "/home/caranha/Desktop/Work/Earthquake_bogdan/data/coast_japan.m";
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GeoLine[] geolines = GeoDataReader.readGeoLines(reader);
		ArrayList<GeoLine> ret = new ArrayList<GeoLine>();
		
		for (int i = 0; i < geolines.length; i++)
			ret.add(geolines[i]);
		
		return ret;
	}
	
}



