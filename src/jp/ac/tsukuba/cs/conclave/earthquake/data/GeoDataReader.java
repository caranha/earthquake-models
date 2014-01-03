package jp.ac.tsukuba.cs.conclave.earthquake.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * Static Class to read files and create data objects
 * @author caranha
 *
 */
public class GeoDataReader {

	
	public static GeoLine[] readGeoLines(String filename)
	{
		BufferedReader r = null;
		try
		{	
			r = new BufferedReader(new FileReader(new File(filename)));
		}
		catch (Exception e)
		{
			System.err.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
		return readGeoLines(r);
	}
	
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
					String[] readline = line.split(" ");
					double x,y;
					x = Double.parseDouble(readline[0]);
					if (readline[1].length() == 0) // FIXME: ugly hack because "faults" and "coast" are different - ideally, I should eat any number of white spaces
						y = Double.parseDouble(readline[2]);
					else
						y = Double.parseDouble(readline[1]);
					tmp.addPoint(new GeoPoint(x,y));
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
