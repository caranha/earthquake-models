package jp.ac.tsukuba.cs.conclave.earthquake.filtering;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

/***
 * Filters a Data List by the location of the earthquake, based on latitude and longitude.
 * "minimum-maximum" is defined in the SW-NE axis.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 */
public class LocationFilter implements EarthquakeFilter {

	Float minlat;
	Float minlon;
	Float maxlat;
	Float maxlon;
	
	@Override
	public boolean testData(DataPoint p, DataList l) {
		
		if (minlat != null && p.latitude < minlat)
			return false;
		if (minlon != null && p.longitude < minlon)
			return false;
		if (maxlat != null && p.latitude > maxlat)
			return false;
		if (maxlon != null && p.longitude > maxlon)
			return false;
		return true;
	}

	@Override
	public EarthquakeFilter testNOP() {
		if (minlat == null && minlon == null && maxlat == null && maxlon == null)
			return null;
		else
			return this;
	}
	
	public void setMinimum(float latitude, float longitude)
	{
		minlat = latitude;
		minlon = longitude;
	}
	
	public void setMaximum(float latitude, float longitude)
	{
		maxlat = latitude;
		maxlon = longitude;
	}

}
