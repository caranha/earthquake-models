package jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray;

import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.geneticalgorithm.GeneratingOperator;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * Implements a uniform crossover operator
 * @author caranha
 *
 */
public class RAUniformCrossover implements GeneratingOperator {

	double crossoverchance;
	Random dice;
	
	@Override
	public void setup(Parameter p) {
		crossoverchance = Double.parseDouble(p.getParameter("crossover chance", "0.8"));
		dice = new Random(); // FIXME: random should come from an external source.
	}

	@Override
	public void preGenerationHook() {
	}

	@Override
	public Genome[] apply(Genome[] parents) {
		if (parents == null || parents.length < 2)
			return null;
		
		RealArrayGenome ret[] = new RealArrayGenome[2];
		ret[0] = (RealArrayGenome) parents[0].clone();
		ret[1] = (RealArrayGenome) parents[1].clone();
		
		if (dice.nextDouble() < crossoverchance)
			for (int i = 0; i < ret[0].genes.length; i++)
				if (dice.nextDouble() < 0.5)
				{
					double aux = ret[0].genes[i];
					ret[0].genes[i] = ret[1].genes[i];
					ret[1].genes[i] = aux;
				}
			
		return ret;
	}

}
