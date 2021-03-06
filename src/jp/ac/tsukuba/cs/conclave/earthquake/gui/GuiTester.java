package jp.ac.tsukuba.cs.conclave.earthquake.gui;


import java.beans.PropertyVetoException;
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
import jp.ac.tsukuba.cs.conclave.earthquake.gui.filtering.FilteringFrame;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.focus.EarthquakeFocusFrame;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.map.DrawElementFrame;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.map.MapFrame;
import jp.ac.tsukuba.cs.conclave.earthquake.image.DrawGeography;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapController;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;

public class GuiTester {

	
	public static EarthquakeDisplayModel model;	
	
	public static void main(String[] args) {
		
		// Loading Initial Data (Probably want to encapsulate this)
		DrawGeography drawjapanmap = new DrawGeography();
		drawjapanmap.setMap(loadJapanMap());
		drawjapanmap.setName("Japan Map");

		MapController mapa = new MapController(MapImage.JapaneseMapFactory());
		mapa.addDrawCommand(drawjapanmap);

		model = new EarthquakeDisplayModel(loadAllEarthquakes(),mapa);
		

		// Setting up the Swing Environment:
		
		// Main frame and Desktop Pane
		JFrame frame = new JFrame("Earthquake GUI Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setVisible(true);

        JDesktopPane mainwindow = new JDesktopPane();
		frame.setContentPane(mainwindow);
		
		// Map Internal Pane
		JInternalFrame map = new MapFrame(model);       
        map.setVisible(true);
        
        // Earthquake list Pane
        DataListFrame list = new DataListFrame(model);
        list.setVisible(true);
        list.setLocation(520, 0);
        
        // Filtering list Pane
        FilteringFrame filter = new FilteringFrame(model);
        filter.setVisible(true);
        filter.setLocation(760,0);
        
        // Earthquake focus frame
        EarthquakeFocusFrame focus = new EarthquakeFocusFrame(model);
        focus.setVisible(true);
        focus.setLocation(760,220);
        
        // Draw Element Controller Frame
        DrawElementFrame drawer = new DrawElementFrame(model);
        drawer.setVisible(true);
        drawer.setLocation(760,0);
        
        
        
        // Adding everything to the Desktop Pane
        mainwindow.add(map);
        mainwindow.add(list);
        mainwindow.add(filter);
        mainwindow.add(focus);
        mainwindow.add(drawer);

        try {
			drawer.setIcon(true);
			filter.setIcon(true);
			focus.setIcon(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}
	
	static ArrayList<GeoLine> loadJapanMap()
	{
		BufferedReader reader = null;
		String filename = "data/coast_japan.m";
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.err.println("Failed to load: "+filename);
			System.err.println(e.getMessage());
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



