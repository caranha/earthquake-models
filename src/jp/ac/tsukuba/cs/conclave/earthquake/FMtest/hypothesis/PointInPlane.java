package jp.ac.tsukuba.cs.conclave.earthquake.FMtest.hypothesis;

import java.util.Iterator;

import jp.ac.tsukuba.cs.conclave.earthquake.GeoUtils;
import jp.ac.tsukuba.cs.conclave.earthquake.FMtest.FaultModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class PointInPlane implements Hypothesis{

	@Override
	public boolean areModelsDifferent(FaultModel m1, FaultModel m2,
			Duration time, DataList data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Double getDifferencePValue(double minMag, DateTime start,
			DateTime end, DataList data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringAnalysis(FaultModel m1, Duration time, DataList data) {
		// part 1- select the sub-data
		DataList thisData = data.getEventsInInterval(m1.getTime(), m1.getTime().plus(time));
		Iterator<DataPoint> it = thisData.data.iterator();
		
		int pointTotal = 0;
		int pointInside = 0;
		double range = GeoUtils.getAftershockRadius(m1.getMag());
		
		// Number of total aftershocks in "radius*2" divided by the number of aftershocks in the rectangle
		while (it.hasNext())
		{
			DataPoint next = it.next();
			if (GeoUtils.haversineDistance(next.latitude, next.longitude, m1.getLat(), m1.getLon()) < range)
			{
				pointTotal += 1;
				if (GeoUtils.isPointInPlane(next.latitude, next.longitude, m1.getPlane()))
					pointInside += 1;
				// Test Point In Polygon
			}
		}
		
		return pointInside+"/"+pointTotal;
	}

	@Override
	public double getNumberAnalysis(FaultModel m1, Duration time, DataList data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEventValid(DataPoint e1, Duration time, DataList data) {
		// TODO Auto-generated method stub
		return false;
	}

}
