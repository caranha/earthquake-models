package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import java.util.List;

import jp.ac.tsukuba.cs.conclave.utils.Parameter;


/***
 * This Interface describes an operator that generates and/or modifies
 * a Genome, possibly based on a list of genomes.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 */
public interface GeneratingOperator {

	public void setup(Parameter p);
	public void preGenerationHook();
	public Genome[] apply(Genome[] parents);
	
	
}
