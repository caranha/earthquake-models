package jp.ac.tsukuba.cs.conclave.earthquake.gui.tester;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JFrame;

import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoDataReader;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoLine;
import jp.ac.tsukuba.cs.conclave.earthquake.gui.map.MapPanel;
import jp.ac.tsukuba.cs.conclave.earthquake.image.DrawGeography;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapController;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;

public class GuiTester {

	
	public static void main(String[] args) {

		DrawGeography drawjapanmap = new DrawGeography();
		drawjapanmap.setMap(loadJapanMap());
		
		MapController m = new MapController(MapImage.JapaneseMapFactory());
		m.addDrawCommand(drawjapanmap);
		
        JFrame frame = new JFrame("Earthquake GUI Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MapPanel p1 = new MapPanel(m);       

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



