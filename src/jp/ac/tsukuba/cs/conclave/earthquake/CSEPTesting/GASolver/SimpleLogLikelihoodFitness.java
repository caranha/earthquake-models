package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class SimpleLogLikelihoodFitness extends CSEPFitness {

	CSEPModel events;
	CSEPModelFactory factory;
	
	public SimpleLogLikelihoodFitness(CSEPModel e, CSEPModelFactory f, double lambdamultiplier)
	{
		events = e;
		factory = f;
		lambda = ((double)events.getTotalEvents())/events.getTotalBins();
		lambda = lambda*lambdamultiplier;
	}
	
	@Override
	public void setup(Parameter param) {
		// no-op;
	}

	@Override
	public double evaluate(Genome g) {
		Double result = createModelFromGenome(g).calcLogLikelihood(events);

		if (result != null)
			return result;
		else
			return Double.NEGATIVE_INFINITY;
	}
}
