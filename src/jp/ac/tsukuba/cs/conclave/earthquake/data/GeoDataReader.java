package jp.ac.tsukuba.cs.conclave.earthquake.data;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * Static Class to read files and create data objects
 * @author caranha
 *
 */
public class GeoDataReader {

	/**
	 * Reads a file with pairs of coordinates in two columns, separated by a space
	 * @param reader An io buffer created from an internal or external file;
	 * @return
	 */
	public static GeoLine[] readGeoLines(BufferedReader reader)
	{
		Logger logger = Logger.getLogger(DataList.class.getName());
		
		ArrayList<GeoLine> buffer = new ArrayList<GeoLine>();
		GeoLine tmp = null;
		
		
		try {
			String line;			
			while ((line = reader.readLine()) != null)
			{
				if (tmp == null)
					tmp = new GeoLine();
				if (line.startsWith("i")) // end of current geoline ("inf inf")
				{
					if (tmp.getSize() > 0)
						buffer.add(tmp);
					tmp = null;
				}
				else // add another point
				{
					String[] readline = line.split("  ");
					tmp.addPoint(new GeoPoint(Double.parseDouble(readline[0]),Double.parseDouble(readline[1])));
				}
			}
			reader.close();
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "Error reading GeoLines");
			System.exit(1);
		}
		
		GeoLine[] ret = new GeoLine[1];	
		return buffer.toArray(ret);
	}
	
}
