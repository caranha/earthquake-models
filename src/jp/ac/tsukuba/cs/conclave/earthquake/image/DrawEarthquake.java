package jp.ac.tsukuba.cs.conclave.earthquake.image;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/**
 * Draws one earthquake. Size is proportional to the main image size;
 * 
 */

public class DrawEarthquake extends MapDrawCommand {

	DataPoint event;
	
	public DrawEarthquake(DataPoint e)
	{
		event = e;
		depth = 5;
	}
	
	public void setEarthquake(DataPoint e)
	{
		event = e;
	}
	public DataPoint getEarthquake()
	{
		return event;
	}
	
	/**
	 * Draws a single earthquake. The size of the event will be proportional to 
	 * the magnitude and the image's size (1% of image size * magnitude)
	 */
	@Override
	public void draw(MapImage img) 
	{
		int size = (int) Math.floor((img.getWidth()/400)*event.magnitude);
		if (size == 0)
			size = 1;
		img.drawEvent(event, mainColor, size);
	}

}
