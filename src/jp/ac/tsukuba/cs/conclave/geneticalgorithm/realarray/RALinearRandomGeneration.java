package jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * Generates a random RealArrayGenome from a random linear distribution.
 * Each Gene is set to a value between 0 and 1.
 * 
 * @author caranha
 *
 */
public class RALinearRandomGeneration implements BreedingPipeline {

	Random dice;
	int genesize;
	
	@Override
	public void setup(Parameter p, Random d) {
		dice = d;
		genesize = Integer.parseInt(p.getParameter("RAG gene size", "100"));
	}

	@Override
	public void chainBreedingPipeline(BreedingPipeline bp) {
		System.err.println("RA Random Generation Warning: can't chain Pipeline here");
		// TODO: send this to a log
	}

	@Override
	public void preGenerationHook() {
		// no-op
	}

	@Override
	public List<Genome> apply(List<Genome> inds) {
		ArrayList<Genome> ret = new ArrayList<Genome>();
		
		RealArrayGenome aux = new RealArrayGenome(genesize);
		for (int i = 0; i < genesize; i++)
			aux.genes[i] = dice.nextDouble();
		
		return ret;
	}

}
