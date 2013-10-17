package jp.ac.tsukuba.cs.conclave.earthquake.gui;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoDataReader;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoLine;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.datalist.DataListFrame;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.map.MapPanel;
import jp.ac.tsukuba.cs.conclave.earthquake.image.DrawGeography;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapController;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;

public class GuiTester {

	
	// Model Attributes -- things that I should have only one of:
	static MapController m; // Creates and Controls a map
	static DataList data;
	
	
	
	public static void main(String[] args) {
		
		// Loading Initial Data (Probably want to encapsulate this)
		DrawGeography drawjapanmap = new DrawGeography();
		drawjapanmap.setMap(loadJapanMap());
		
		data = loadAllEarthquakes();
		
		
		// Initializing global variables (Probably want to encapsulate this too)
		m = new MapController(MapImage.JapaneseMapFactory());
		m.addDrawCommand(drawjapanmap);
		
		
		
		
		// Setting up the Swing Environment:
		
		// Main frame and Desktop Pane
		JFrame frame = new JFrame("Earthquake GUI Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        JDesktopPane mainwindow = new JDesktopPane();
		frame.setContentPane(mainwindow);
		
		// Map Internal Plane
		JInternalFrame map = new MapPanel(m);       
        map.setVisible(true);
        
        // Earthquake list Plane
        JInternalFrame list = new DataListFrame(data);
        list.setVisible(true);
        list.setLocation(520, 0);
        
        // 
        
        
        // Adding everything to the Desktop Pane
        mainwindow.add(map);
        mainwindow.add(list);

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
	
	static DataList loadAllEarthquakes()
	{
		DataList aux = new DataList();
		aux.loadData("data/jma_cat_2000_2012_Mth2.5_formatted.dat","jma");
		aux.loadData("data/catalog_fnet_1997_20130429_f3.txt","fnet");
		return aux;
	}
	
}



