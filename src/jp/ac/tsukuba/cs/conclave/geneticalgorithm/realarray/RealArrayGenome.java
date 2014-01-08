package jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray;

import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;

public class RealArrayGenome extends Genome {

	double genes[];
	
	public RealArrayGenome(int size)
	{
		genes = new double[size];
	}
	
	@Override
	public int compareTo(Genome o) {
		if (this.equals(o))
			return 0;
		if (o.getFitness() > fitness)
			return -1;
		else
			return 1;
	}

	@Override
	public Genome clone() {
		RealArrayGenome ret = new RealArrayGenome(genes.length);
		for (int i = 0; i < genes.length; i++)
			ret.genes[i] = genes[i];
		ret.fitness = this.fitness.doubleValue();
		return ret;
	}

	public double[] getGenes()
	{
		return genes;
	}
	
}
