package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting;

import java.awt.Color;
import java.util.Random;
import java.util.logging.Logger;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver.GASolver;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.RISolver.RISolver;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.RandomSolver.RandomSolver;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.DepthFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.LocationFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.UnitaryDateFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.DateUtils;
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

	Logger log = Logger.getLogger(CSEPpredictor.class.getName());
	
	public static final int EXIT_ERROR = 1;
	public static Random dice;

	
	
	static Parameter param;
	static DataList training_data;
	static DataList testing_data;	
	static CSEPModelFactory factory;
	
	static String fileprefix;
	
	/**
	 * @param args: an external file that dictates the parameters for this run.
	 */
	public static void main(String[] args) {

		if (args.length == 0)
		{
			System.err.println("Error: Parameter file not specified.");
			System.exit(EXIT_ERROR);
		}
		
		readParameterFile(args[0]);
		factory = new CSEPModelFactory(param);
		
		seedRandomGenerator();
		loadDataFile();
		
		CSEPModel trainmodel = factory.modelFromData(training_data);
		trainmodel.getEventMap().saveToFile(fileprefix+"trainmodel.png");
		CSEPModel testmodel = factory.modelFromData(testing_data);
		testmodel.getEventMap().saveToFile(fileprefix+"testmodel.png");

		//CSEPModel random;
		//random = RandomSolver();		
		//testModel(random,fileprefix+"RandomModel");

		CSEPModel ri;
		ri = RIsolver();		
		testModel(ri,fileprefix+"RIModel");
		
		//CSEPModel ga;
		//ga = GAsolver();		
		//testModel(ga,fileprefix+"GAModel");
		
	}

	static void testModel(CSEPModel m, String modelname)
	{
		CSEPModel testmodel = factory.modelFromData(testing_data);
		CSEPModel trainmodel = factory.modelFromData(training_data);
				
		// Output LogLikelihood Value
		System.out.println("Log Likelihood for model "+modelname+" against train data: "+m.calcLogLikelihood(trainmodel));
		System.out.println("Log Likelihood for model "+modelname+" against test data: "+m.calcLogLikelihood(testmodel));
		
		
		
		// Draw Testing Events
		MapImage map = m.getEventMap();
		for (DataPoint aux: testing_data)
			map.drawEvent(aux, Color.BLUE, (float) aux.magnitude);
		map.saveToFile(modelname+".png");
		
		// CSEP Tests
	}
	
	static CSEPModel RIsolver()
	{
		RISolver ri = new RISolver();
		ri.setup(param, training_data);
		ri.execute();
		
		return ri.getBest();
	}
	
	static CSEPModel GAsolver()
	{
		GASolver gas = new GASolver();
		gas.configureGA(training_data, param, dice);
		gas.setVerbose(true);
		gas.runGA(0);
		
		return gas.getBest();		
	}
	
	static CSEPModel RandomSolver()
	{
		RandomSolver r = new RandomSolver();
		r.setup(training_data, param);
		r.execute(true);
		return r.getBest();
	}
	
	
	
	static public Parameter getParameter()
	{
		return param;
	}
	
	static public CSEPModelFactory getModelFactory()
	{
		return factory;
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
		DepthFilter depth = new DepthFilter();
		CompositeEarthquakeFilter locFilter = new CompositeEarthquakeFilter();
		
		double minlon = Double.parseDouble(param.getParameter("start lon", "139"));
		double minlat = Double.parseDouble(param.getParameter("start lat","37"));
		double maxlon = minlon + (Double.parseDouble(param.getParameter("delta lon", "0.2"))*
								  Integer.parseInt(param.getParameter("grid lon","20")));
		double maxlat = minlat + (Double.parseDouble(param.getParameter("delta lat","0.2"))*
								  Integer.parseInt(param.getParameter("grid lat","20")));
		double maxdepth = Double.parseDouble(param.getParameter("cutoff depth", "100"));
		
		location.setMinimum(minlon, minlat);
		location.setMaximum(maxlon, maxlat);
		depth.setmax(maxdepth);
		locFilter.addFilter(location);
		locFilter.addFilter(depth);

		data = locFilter.filter(data);
		
		// Create the filter for the training data
		CompositeEarthquakeFilter trainFilter = new CompositeEarthquakeFilter();		
		UnitaryDateFilter traindate = new UnitaryDateFilter();
		traindate.setMinimum(DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("training start date", "2000-01-01")));
		traindate.setMaximum(DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("training end date", "2001-01-01")));		
		trainFilter.addFilter(traindate);
		training_data = trainFilter.filter(data);
		
		// Create the filter for the testing data
		CompositeEarthquakeFilter testFilter = new CompositeEarthquakeFilter();
		UnitaryDateFilter testdate = new UnitaryDateFilter();
		testdate.setMinimum(DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("testing start date", "2001-01-01")));
		testdate.setMaximum(DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("testing end date", "2002-01-01")));		
		testFilter.addFilter(testdate);
		testing_data = testFilter.filter(data);		
	}
	
	static void readParameterFile(String filename)
	{
		param = new Parameter();
		try	{
			param.loadTextFile(filename);
			param.loadTextFile(param.getParameter("geofile", "base")+".geo");
			fileprefix = param.getParameter("fileprefix", ""); 

		} catch (Exception e)
		{
			System.err.println("Error reading parameter file: "+e.getMessage());
			System.exit(EXIT_ERROR);
		}
	}
	
	static void seedRandomGenerator()
	{
		dice = new Random();		
		String seed = param.getParameter("random seed", null);
		if (seed != null)	
			dice.setSeed(Long.parseLong(seed));
	}

}
