package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.FitnessEvaluation;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RealArrayGenome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class TimeWeightedLogLikelihoodFitness implements FitnessEvaluation {

	DataList data;
	
	CSEPModel mainEvent;
	CSEPModel[] trainModels;
	
	CSEPModelFactory factory;
	double lambda;
		
	public TimeWeightedLogLikelihoodFitness(DataList data, CSEPModel e, CSEPModelFactory f, double lambdamultiplier)
	{
		mainEvent = e;
		factory = f;
		lambda = ((double)mainEvent.getTotalEvents())/mainEvent.getTotalBins();
		lambda = lambda*lambdamultiplier;
	}
	
	@Override
	public void setup(Parameter param) {
		int n = Integer.parseInt(param.getParameter("fitness time slices", "4"));
		trainModels = new CSEPModel[n];
		
		
		
	}

	@Override
	public double evaluate(Genome g) {
		Double result = createModelFromGenome(g).calcLogLikelihood(events);

		if (result != null)
			return result;
		else
			return Double.NEGATIVE_INFINITY;
	}

	// FIXME: encapsulate this
	CSEPModel createModelFromGenome(Genome g)
	{
		RealArrayGenome aux = (RealArrayGenome) g;
		return factory.modelFromRealArray(aux.getGenes(),lambda);
	}
}
