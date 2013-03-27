package jp.ac.tsukuba.cs.conclave.earthquake;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import jp.ac.tsukuba.cs.conclave.earthquake.RI.RIModel;

public class TestMain {

	public static void main(String[] args) {
		RawData r = new RawData();
		//r.loadData("/home/caranha/Desktop/Work/Earthquake_bogdan/data/jma_cat_100l_test");
		r.loadData("/home/caranha/Desktop/Work/Earthquake_bogdan/data/jma_cat_2000_2012_Mth2.5_formatted.dat");

		RIModel tr = new RIModel(r);
		double mag = 2.5;
	
		System.out.println(tr.boxsizeX + " " + tr.boxsizeY);
		System.out.println(tr.getHighCount(mag)); // maximum number of events in a single grid

		BufferedImage hmap = tr.getIntensityImage();		
		File f = new File("himg.png");
	    
		try {
			ImageIO.write(hmap, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		int[] hist = frequencyArray(mag,tr);		
//		for (int i = 0; i < hist.length; i++)
//			if (hist[i] > 0)
//				System.out.println(i + " " + hist[i]);
		
		
	}
	
	
	static int[] frequencyArray(double minMag, RIModel r)
	{
		int[] ret = new int[r.getHighCount(minMag)+1];
		for (int i = 0; i < r.boxsizeX; i++)
			for (int j = 0; j < r.boxsizeY; j++)
				ret[r.eventgrid[i][j].getEvents(minMag)]++;
		return ret;
	}
}
