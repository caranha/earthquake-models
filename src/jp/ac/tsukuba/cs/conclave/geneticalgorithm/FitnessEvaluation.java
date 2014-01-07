package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public interface FitnessEvaluation {

	public void setup(Parameter param);
	public double evaluate(Genome g);	
	
}
