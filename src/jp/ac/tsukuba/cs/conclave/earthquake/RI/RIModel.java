package jp.ac.tsukuba.cs.conclave.earthquake.RI;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import jp.ac.tsukuba.cs.conclave.earthquake.RawData;
import jp.ac.tsukuba.cs.conclave.earthquake.RawDataPoint;

import org.joda.time.DateTime;

public class RIModel {

	public RIBox eventgrid[][];
	
	public double boxsize; // size, in degrees, of a box
	public double startboxX;
	public double startboxY;
	public int boxsizeX;
	public int boxsizeY;
	
	
	public DateTime initTime,endTime; // instants to begin and end the training of the model
	public double minMag; // minimum magnitude for events to be considered
	public double magDelta; // steps to consider the magnitude
	
	public int[] highestCount; // highest event count from all cells, by magnitude
	
	
	/**
	 * Constructor using default parameter values. Receives a training data set as input.
	 * @param d
	 */
	public RIModel(RawData d)
	{
		// setting the box size
		boxsize = 0.05;
		startboxX = d.minlong;
		startboxY = d.minlat;
		boxsizeX = (int) Math.ceil((d.maxlong - d.minlong)/boxsize);
		boxsizeY = (int) Math.ceil((d.maxlat - d.minlat)/boxsize);
		magDelta = 0.1;
		
		eventgrid = new RIBox[boxsizeX][boxsizeY];
		for (int i = 0; i < boxsizeX; i++)
			for (int j = 0; j < boxsizeY; j++)
				eventgrid[i][j] = new RIBox(magDelta);
		
		initTime = d.mindate;
		endTime = d.maxdate;		
		minMag = 2.5;
		
		
		highestCount = new int[(int) Math.round(9/magDelta)+1];
		
		readData(d);
	}
	
	
	/**
	 * Transforms a rawdata into grid event data. Used after the parameters have been set.
	 * @param d
	 */
	void readData(RawData d)
	{
		Iterator<RawDataPoint> it = d.data.iterator();
		while (it.hasNext())
		{
			RawDataPoint t = it.next();
			if (t.magnitude >= minMag)
			{
				int px = (int)Math.floor((t.longitude - startboxX)/boxsize);
				int py = (int)Math.floor((t.latitude - startboxY)/boxsize);
				eventgrid[px][py].addEvent(t.magnitude,highestCount);
			}
		}		
	}
	
	
	/**
	 * Gets the maximum number of earthquakes in a single cell, of the indicated magnitude or above.
	 * @param minMag
	 * @return
	 */
	public int getHighCount(double minMag)
	{
		return highestCount[(int) Math.floor(minMag/magDelta)];
	}
	
	
	/**
	 * Creates a count image of earthquakes above magnitude "mag"
	 */
	public BufferedImage getHistImage(double mag)
	{
		// Opens an empty image and creates a graphics context to draw to it.
		BufferedImage img =
				  new BufferedImage(boxsizeX, boxsizeY,						  
						  BufferedImage.TYPE_INT_ARGB);
		float f[] = {1.0f,1.0f,1.0f,1};
		float p;
		float pmax = getHighCount(mag);
		
		for (int i = 0; i < boxsizeX; i++)
			for (int j = 0; j < boxsizeY; j++)
			{
				p = eventgrid[i][boxsizeY - (j+1)].getEvents(mag);
				if (p > 0)
					p = (p+pmax)/(pmax*2+1); // Transforms P into a value between 0.5 and 1
				else
					p = 0;
				
				Color c = new Color(p*f[0],p*f[1],p*f[2],f[3]);
				img.setRGB(i, j, c.getRGB());
			}
		return img;
	}
	
	public BufferedImage getIntensityImage()
	{
		// Opens an empty image and creates a graphics context to draw to it.
		BufferedImage img =
				  new BufferedImage(boxsizeX, boxsizeY,						  
						  BufferedImage.TYPE_INT_ARGB);

		float blue;
		float bluemax = getHighCount(0);
		float red = 0;
		float redmax = 9;
		
		for (int i = 0; i < boxsizeX; i++)
			for (int j = 0; j < boxsizeY; j++)
			{
				blue = eventgrid[i][j].getEvents(0);
				if (blue > 0)
					blue = (blue+bluemax)/(bluemax*2+1); // Transforms P into a value between 0.5 and 1
				else
					blue = 0;
				
				red = (float) eventgrid[i][j].getStrongest();
				red = red/redmax;

				Color c;
				if (blue == 0)
					c = Color.white;
				else
					c = new Color(red,0f,blue,1f);

				img.setRGB(i, boxsizeY - (j+1), c.getRGB());
			}
		return img;		
	}

	
	

}
