package jp.ac.tsukuba.cs.conclave.earthquake.gui;

import java.util.Observable;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapController;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapDrawCommand;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.GeoUtils;

public class EarthquakeDisplayModel extends Observable {

	DataList fullData; // List with all earthquakes we will use - will not change

	DataList filteredData; // List with filtered earthquakes
	DataList bookmarkData; // List with bookmarked quakes
	
	MapController m; // Holds the map image and its internal drawings
	
	DataPoint focusEarthquake; // Earthquake currently under focus
	Double aftershockdistance;

	public EarthquakeDisplayModel(DataList data, MapController map)
	{
		fullData = data;
		filteredData = data;
		bookmarkData = new DataList();
		
		m = map;
		
		focusEarthquake = null;
		aftershockdistance = 0.0;
	}
	
	/**
	 * Getters and setters -- I probably should return only interables;
	 */
	
	public DataList getFullData()
	{
		return fullData;
	}
	
	public DataList getFilteredData()
	{
		return filteredData;
	}
	
	public DataList getBookmarkData()
	{
		return bookmarkData;
	}

	// TODO: remove this, provide controlling methods
	public MapController getMapController()
	{
		return m;
	}
	
	
	
	
	public MapImage getMapImage()
	{
		return m.getImage();
	}
	

	public DataPoint getFocusEarthquake()
	{
		return focusEarthquake;
	}
	
	public double getAfterShockDistance()
	{
		return aftershockdistance;
	}



	public void resetFilteredList() {
		filteredData = fullData;
		setChanged();
		notifyObservers("Filtered List");
	}
	
	/**
	 * Changes the earthquake in focus by the model. 
	 * Also re-calculates the aftershock distance
	 * 
	 * @param p
	 */
	public void setFocusEarthquake(DataPoint p) {
		focusEarthquake = p;
		setChanged();
		notifyObservers("Focus Earthquake");
		
		if (p == null)
		{
			setAfterShockDistance(0.0);
		}
		else
		{
			setAfterShockDistance(GeoUtils.getAftershockRadius(p.magnitude));
		}

	}
	
	public void setAfterShockDistance(double d)
	{
		aftershockdistance = d;
		setChanged();
		notifyObservers("Aftershock Distance");
	}

	public void addDrawCommand(MapDrawCommand aux) {
		m.addDrawCommand(aux);
		setChanged();
		notifyObservers("Map Controller");
	}

	public void removeDrawCommand(MapDrawCommand aux)
	{
		m.removeDrawCommand(aux);
		setChanged();
		notifyObservers("Map Controller");
	}
	
	public void updateDrawCommand()
	{
		m.redrawMap();
		setChanged();
		notifyObservers("Map Controller");
	}

	public void filterData(CompositeEarthquakeFilter filter) {
		filteredData = filter.filter(filteredData);

		setChanged();
		notifyObservers("Filtered List");
	}

		

}
