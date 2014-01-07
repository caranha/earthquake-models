package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

public abstract class Genome implements Comparable<Genome> {

	Double fitness = null;
	

	
	public void setFitness(Double fit)
	{
		fitness = fit;
	}
	
	public Double getFitness()
	{
		return fitness;
	}
	
}
