package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.GeographicalCSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.RandomSolver.RandomSolver;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.LocationFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.UnitaryDateFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * This is the entry class for generating a CSEP prediction model using Genetic Algorithms.
 * 
 * It reads a file with the parameters for the experiment, and then generates a prediction 
 * model by Evolutionary Algorithm using those parameters.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class CSEPpredictor {

	static final int EXIT_ERROR = 1;

	static Parameter param;
	
	static DataList training_data;
	static DataList testing_data;
	
	// This JODA time formart understands YYYY-MM-DD [HH:mm:ss] date strings
	static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		.append(ISODateTimeFormat.date().getParser())
		.appendOptional(new DateTimeFormatterBuilder()
			.appendLiteral(' ')
			.append(ISODateTimeFormat.hourMinuteSecond()).toParser())
		.toFormatter();
	
	/**
	 * @param args: an external file that dictates the parameters for this run.
	 */
	public static void main(String[] args) {

		
		// DONE: Read parameters from parameter file
		if (args.length == 0)
		{
			System.err.println("Error: Parameter file not specified.");
			System.exit(EXIT_ERROR);
		}
		
		readParameterFile(args[0]);
		loadDataFile();

		testRandomSolver();
	}

	
	static public void testRandomSolver()
	{
		RandomSolver r = new RandomSolver();
		r.init(training_data, param);

		CSEPModel m; 
		for (int i = 0; i < 100; i++)
		{
			r.execute(100000);
			m = r.getBest();
			m.getEventMap().saveToFile("map"+String.format("%03d", i)+".png");
			System.out.println(""+m.getLogLikelihood());
		}		
	}
	
	
	
	static public Parameter getParameter()
	{
		return param;
	}
	
	// TODO: I might want to replace DataList here with Models instead
	static public DataList getTrainingData()
	{
		return training_data;
	}
	
	static public DataList getTestingData()
	{
		return testing_data;
	}
	
	
	
	/**
	 * Loads the data file based on the parameter file. 
	 * Breaks down the data file into training data and test data,
	 * filters by time and space based on the parameters.
	 * 
	 * TODO: add proper error messages
	 */
	static void loadDataFile()
	{
		// Read the entire data list
		DataList data = new DataList();
		data.loadData(param.getParameter("datafile", null), param.getParameter("filetype", null));
		LocationFilter location = new LocationFilter();
		CompositeEarthquakeFilter locFilter = new CompositeEarthquakeFilter();
		
		double minlon = Double.parseDouble(param.getParameter("start lon", "139"));
		double minlat = Double.parseDouble(param.getParameter("start lat","37"));
		double maxlon = minlon + (Double.parseDouble(param.getParameter("delta lon", "0.2"))*
								  Integer.parseInt(param.getParameter("grid lon","20")));
		double maxlat = minlat + (Double.parseDouble(param.getParameter("delta lat","0.2"))*
								  Integer.parseInt(param.getParameter("drid lat","20")));
		
		location.setMinimum(minlon, minlat);
		location.setMaximum(maxlon, maxlat);
		locFilter.addFilter(location);

		data = locFilter.filter(data);
		
		// Create the filter for the training data
		CompositeEarthquakeFilter trainFilter = new CompositeEarthquakeFilter();		
		UnitaryDateFilter traindate = new UnitaryDateFilter();
		traindate.setMinimum(formatter.parseDateTime(param.getParameter("training start date", "2000-01-01")));
		traindate.setMaximum(formatter.parseDateTime(param.getParameter("training end date", "2001-01-01")));		
		trainFilter.addFilter(traindate);
		training_data = trainFilter.filter(data);
		System.out.println(training_data.size());
		
		// Create the filter for the testing data
		CompositeEarthquakeFilter testFilter = new CompositeEarthquakeFilter();
		UnitaryDateFilter testdate = new UnitaryDateFilter();
		testdate.setMinimum(formatter.parseDateTime(param.getParameter("testing start date", "2001-01-01")));
		testdate.setMaximum(formatter.parseDateTime(param.getParameter("testing end date", "2002-01-01")));		
		testFilter.addFilter(testdate);
		testing_data = testFilter.filter(data);		
		System.out.println(testing_data.size());
	}
	
	static void readParameterFile(String filename)
	{
		param = new Parameter();
		try	{
			param.loadTextFile(filename);
		} catch (Exception e)
		{
			System.err.println("Error reading parameter file: "+e.getMessage());
			System.exit(EXIT_ERROR);
		}
	}

}
