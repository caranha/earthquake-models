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
		
		return (int) Math.signum(o.getFitness() - this.fitness);
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
	
	public String toString()
	{
		String ret = "[";
		for (int i = 0; i < genes.length; i++)
		{
			ret += String.format("%.3f", genes[i]);
			if (i < genes.length-1)
				ret += ",";
		}
		ret +="]";
		return ret;
	}
	
}
