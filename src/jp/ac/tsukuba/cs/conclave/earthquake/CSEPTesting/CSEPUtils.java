package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting;

import java.awt.Color;

import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoDataReader;
import jp.ac.tsukuba.cs.conclave.earthquake.data.GeoLine;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;

/**
 * Static class with a bunch of random tools that will be useful for other classes.
 * @author caranha
 *
 */

public class CSEPUtils {
	
	static public MapImage getGeographicalMap(double base, double base2, double d, double e)
	{
		MapImage ret = new MapImage(800, 800, base, base2, d, e);
		GeoLine[] coast = GeoDataReader.readGeoLines(CSEPpredictor.getParameter().getParameter("coastfile", "data/coast.m"));
		
		for (int i = 0; i < coast.length; i++)
			ret.drawGeoLine(coast[i], Color.black);
		return ret;
	}
}
