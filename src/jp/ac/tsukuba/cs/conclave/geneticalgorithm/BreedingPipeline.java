package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.utils.Parameter;


/***
 * This describes either a Selector (that selects Genomes from a list)
 * or an Operator (which modifies Genomes from a list)
 *   
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 */
public interface BreedingPipeline {

	public void setup(Parameter p, Random d);
	public void chainBreedingPipeline(BreedingPipeline bp);
		
	public void preGenerationHook();
	public List<Genome> apply(List<Genome> inds);	
	
}
