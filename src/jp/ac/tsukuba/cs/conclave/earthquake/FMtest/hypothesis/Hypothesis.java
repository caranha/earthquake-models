/**
 * 
 */
package jp.ac.tsukuba.cs.conclave.earthquake.FMtest.hypothesis;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.faultmodel.FaultModel;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * @author caranha
 * This interface defines an hypothesis that may be tested on 
 * two models of the same seismic event.
 */
public interface Hypothesis {

	
	
	/**
	 * Get a decision variable on whether the models are different, based on this hypothesis over the 
	 * time specified.
	 * 
	 * @param m1 First Fault Model
	 * @param m2 Second Fault Model
	 * @param time How long to consider events for this hypothesis
	 * @return True if events can be differentiated under this hypothesis, False if not, or if the models/time are not valid.
	 */
	public boolean areModelsDifferent(FaultModel m1, FaultModel m2, Duration time, DataList data);
	
	/**
	 * Calculate the p-value of the alternate hypothesis that Fault Models m1 and m2 are different, based on the criteria defined in this class.
	 * @param m1 First Fault Model
	 * @param m2 Second Fault Model
	 * @param time How long to consider events for this hypothesis
	 * @return The p-value of the alternate hypothesis m1 != m2. Null if the FMs are invalid.
	 */
	public Double getDifferencePValue(double minMag, DateTime start, DateTime end, DataList data);
	
	/**
	 * Get some information about what values are calculated in this hypothesis (for example, how many 
	 * earthquakes fall within an area, average distance, etc).
	 * 
	 * @param m1 Fault Model to test
	 * @param time Time interval
	 * @return A string (with linebreaks) with the formatted info
	 */
	public String getStringAnalysis(FaultModel m1, Duration time, DataList data);

	/**
	 * Same as above, but a simpler, numeric response is returned, in order to quickly analyze events.
	 * @param m1
	 * @param time
	 * @return
	 */
	public double getNumberAnalysis(FaultModel m1, Duration time, DataList data);
	
	/**
	 * Tests if an event can generate valid Fault models for this hypothesis. At its simplest level, 
	 * this will test if there are "enough" aftershocks for the hypothesis to make sense.
	 * @param e1 A "DataPoint" event
	 * @param time Time interval where the hypothesis will be tested
	 * @return boolean with validity of the event
	 */
	public boolean isEventValid(DataPoint e1, Duration time, DataList data);
	
	
}
