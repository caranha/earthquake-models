package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.UnitaryDateFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.utils.DateUtils;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class TimeWeightedLogLikelihoodFitness extends CSEPFitness {

	DataList data;
	
	CSEPModel mainEvent;
	CSEPModel[] trainModels;
	
	CSEPModelFactory factory;
	
	public TimeWeightedLogLikelihoodFitness(DataList d, CSEPModel e, CSEPModelFactory f, double lambdamultiplier)
	{
		data = d;
		mainEvent = e;
		factory = f;
		// Old Lambda calculation
//		lambda = ((double)mainEvent.getTotalEvents())/mainEvent.getTotalBins();
//		lambda = lambda*lambdamultiplier;
	}
	
	@Override
	public void setup(Parameter param) {
		int n = Integer.parseInt(param.getParameter("fitness time slices", "4"));
		trainModels = new CSEPModel[n];
		
		// Calculate time slices, 
		Duration totaltraintime = new Duration(DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("training start date", "2000-01-01")),
				DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("training end date", "2001-01-01")));
		long timeslice = totaltraintime.getMillis()/n;
		
		DateTime end = DateUtils.getDateTimeFormatter().parseDateTime(param.getParameter("training start date", "2000-01-01"));
		DateTime start;
		
		lambda = 0;
		for (int i = 0; i < n; i++)
		{
			start = end;
			end = start.plus(timeslice);
			CompositeEarthquakeFilter sliceFilter = new CompositeEarthquakeFilter();		
			UnitaryDateFilter slicedate = new UnitaryDateFilter();
			slicedate.setMinimum(start);
			slicedate.setMaximum(end);
			sliceFilter.addFilter(slicedate);
			
			trainModels[i] = factory.modelFromData(sliceFilter.filter(data));
			lambda = +((double)trainModels[i].getTotalEvents())/trainModels[i].getTotalBins();
		}
		
		lambda = lambda/4;
	}

	@Override
	public double evaluate(Genome g) {
		Double result = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < trainModels.length; i++)
		{
			Double tmp = createModelFromGenome(g).calcLogLikelihood(trainModels[i]);
			if (tmp == null)
				return Double.NEGATIVE_INFINITY;
			
			if (tmp < result)
				result = tmp;
		}
		return result;
				
	}
}
