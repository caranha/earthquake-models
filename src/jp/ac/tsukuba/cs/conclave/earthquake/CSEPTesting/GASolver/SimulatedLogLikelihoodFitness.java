package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class SimulatedLogLikelihoodFitness extends CSEPFitness {

	DataList data;
	
	CSEPModel mainEvent;
	CSEPModel[] simulatedEvents;
	
	CSEPModelFactory factory;
	
	public SimulatedLogLikelihoodFitness(DataList d, CSEPModel e, CSEPModelFactory f, double lambdamultiplier)
	{
		mainEvent = e;
		data = d;
		
		factory = f;
		lambda = ((double)mainEvent.getTotalEvents())/mainEvent.getTotalBins();
		lambda = lambda*lambdamultiplier;
	}
	
	@Override
	public void setup(Parameter param) {
		int simulations = Integer.parseInt(param.getParameter("fitness simulation number", "5"));
		simulatedEvents = new CSEPModel[simulations];
		simulatedEvents[0] = mainEvent;
		
		for (int i = 1; i < simulations; i++)
			simulatedEvents[i] = factory.simulationFromModel(mainEvent);
	}

	@Override
	public double evaluate(Genome g) {
		Double result = Double.POSITIVE_INFINITY;
				
		for (int i = 0; i < simulatedEvents.length; i++)
		{
			Double tmp = createModelFromGenome(g).calcLogLikelihood(simulatedEvents[i]);
			if (tmp == null)
				return Double.NEGATIVE_INFINITY;
			
			if (tmp < result)
				result = tmp;
		}
		return result;
	}

}
