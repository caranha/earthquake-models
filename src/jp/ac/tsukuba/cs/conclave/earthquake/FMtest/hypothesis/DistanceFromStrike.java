package jp.ac.tsukuba.cs.conclave.earthquake.FMtest.hypothesis;

import jp.ac.tsukuba.cs.conclave.earthquake.FMtest.FaultModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class DistanceFromStrike implements Hypothesis{

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
		// TODO Auto-generated method stub
		return null;
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
