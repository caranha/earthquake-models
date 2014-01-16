package jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * Clamps the values of the genes to 
 * Minimum <= gene < maximum
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class RAClampValues implements BreedingPipeline {

	double clampmax;
	double clampmin;
	
	ArrayList<BreedingPipeline> upstream;
	
	public RAClampValues()
	{
		upstream = new ArrayList<BreedingPipeline>();
	}
	
	@Override
	public void setup(Parameter p, Random d) {
		clampmax = Double.parseDouble(p.getParameter("clamp maximum value", "0.999"));
		clampmin = Double.parseDouble(p.getParameter("clamp minimum value", "0"));
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
		ArrayList<Genome> ret = new ArrayList<Genome>();
		
		for (BreedingPipeline aux: upstream)
			ret.addAll(aux.apply(parents));

		for (Genome aux: ret)
		{
			RealArrayGenome foo = (RealArrayGenome) aux;
			for (int i = 0; i < foo.genes.length; i++)
			{
				if (foo.genes[i] < clampmin)
					foo.genes[i] = clampmin;
				if (foo.genes[i] > clampmax)
					foo.genes[i] = clampmax;
			}
		}		
		
		return ret;
	}

	@Override
	public void chainBreedingPipeline(BreedingPipeline bp) {
		upstream.add(bp);
	}

}
