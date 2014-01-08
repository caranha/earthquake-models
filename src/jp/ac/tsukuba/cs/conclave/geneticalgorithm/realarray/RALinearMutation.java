package jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * Linear mutation takes each element from the parent, and
 * changes some of the genes for random linear values. 
 * 
 * Parameters: 
 * "mutation chance = 0.1"
 * "cumulative gene mutation = 0.5"
 * 
 * @author caranha
 *
 */
public class RALinearMutation implements BreedingPipeline {

	double mutationchance;
	double cumulativegenemutation;
	
	ArrayList<BreedingPipeline> upstream;
	Random dice;
	
	@Override
	public void setup(Parameter p, Random d) {
		mutationchance = Double.parseDouble(p.getParameter("mutation chance", "0.1"));
		cumulativegenemutation = Double.parseDouble(p.getParameter("cumulative gene mutation", "0.5"));
		dice = d;
	}

	@Override
	public void preGenerationHook() {
		for (BreedingPipeline aux:upstream)
			aux.preGenerationHook();
		// TODO: make BreedingPipeline an abstract type 
		// and encapsulate this.
	}

	@Override
	public List<Genome> apply(List<Genome> parents) {
		ArrayList<Genome> ret = new ArrayList<Genome>();
		
		for (BreedingPipeline aux: upstream)
			ret.addAll(aux.apply(parents));

		for (Genome aux: parents)
			if (dice.nextDouble() < mutationchance)
			{
				RealArrayGenome foo = (RealArrayGenome) aux;
				int genemutationchance = 1;
				while (dice.nextDouble() < genemutationchance)
				{
					int choice = dice.nextInt(foo.genes.length);
					foo.genes[choice] = dice.nextDouble();
					genemutationchance *= cumulativegenemutation;
				}
			}		
		return ret;
	}

	@Override
	public void chainBreedingPipeline(BreedingPipeline bp) {
		upstream.add(bp);
	}

}
