package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import java.util.List;

/***
 * This Interface describes an operator that generates and/or modifies
 * a Genome, possibly based on a list of genomes.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 */
public interface GeneratingOperator {

	public Genome[] apply(List<Genome> parents);
	
	
}
