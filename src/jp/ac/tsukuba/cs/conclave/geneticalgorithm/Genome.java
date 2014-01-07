package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

public abstract class Genome implements Comparable<Genome> {

	protected Double fitness = null;
	

	public abstract Genome clone();
	
	public void setFitness(Double fit)
	{
		fitness = fit;
	}
	
	public Double getFitness()
	{
		return fitness;
	}
	

	
}
