package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPpredictor;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.FitnessEvaluation;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RealArrayGenome;

public abstract class CSEPFitness implements FitnessEvaluation {

	double lambda;
	
	public final CSEPModel createModelFromGenome(Genome g)
	{
		RealArrayGenome aux = (RealArrayGenome) g;
		return CSEPpredictor.getModelFactory().modelFromRealArray(aux.getGenes(),lambda);
	}
	
}
