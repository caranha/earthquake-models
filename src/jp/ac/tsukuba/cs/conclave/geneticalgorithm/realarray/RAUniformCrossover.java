package jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * Implements a uniform crossover operator
 * @author caranha
 *
 */
public class RAUniformCrossover implements BreedingPipeline {

	ArrayList<BreedingPipeline> upstream;
	double crossoverchance;
	Random dice;
	
	public RAUniformCrossover()
	{
		upstream = new ArrayList<BreedingPipeline>();
	}
	
	@Override
	public void setup(Parameter p, Random d) {
		crossoverchance = Double.parseDouble(p.getParameter("crossover chance", "0.8"));
		dice = d;
		for (BreedingPipeline aux: upstream)
			aux.setup(p, d);
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
		ArrayList<Genome> ret = new ArrayList<Genome>(2);
		ArrayList<Genome> ret2 = new ArrayList<Genome>();
		
		for (BreedingPipeline aux: upstream)
			ret2.addAll(aux.apply(parents));
		
		if (ret2.size() < 2)
		{
			System.err.println("RAUniformCrossover Error: Not enough elements for uniform Crossover (Put this on a log)");
			return null;
			// TODO: Put this on a log.
		}
		
		ret.addAll(ret2.subList(0, 2));
		
		if (dice.nextDouble() < crossoverchance)
		{
			RealArrayGenome p0 = (RealArrayGenome) ret.get(0);
			RealArrayGenome p1 = (RealArrayGenome) ret.get(1);
			for (int i = 0; i < p0.genes.length; i++)
				if (dice.nextDouble() < 0.5)
				{
					double aux = p0.genes[i];
					p0.genes[i] = p1.genes[i];
					p1.genes[i] = aux;
				}
		}
		
		return ret;
	}


	@Override
	public void chainBreedingPipeline(BreedingPipeline bp) {
		upstream.add(bp);
	}

}
