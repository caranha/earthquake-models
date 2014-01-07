package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import java.util.ArrayList;

import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class GeneticAlgorithm {

	Parameter param;
	
	GeneratingOperator genOp;
	ArrayList<SelectionOperator> selectOpList;
	ArrayList<Genome> population;
	
	
	GeneticAlgorithm(Parameter p)
	{
		selectOpList = new ArrayList<SelectionOperator>();
		population = new ArrayList<Genome>();
		param = p;
	}
	
	
	/**
	 * Initializes all operators of the genetic algorithm, and 
	 * creates the initial population. The initial population 
	 * is evaluated.
	 * 
	 * @param param
	 */
	public void initialize()
	{
		// TODO: Stub
	}	
	
	/**
	 * Runs up to "N" generations. Stops before N if a 
	 * parameter-defined stop condition is reached. If N is 
	 * negative, run indefinitely until a stop condition is reached.
	 * 
	 * @param n: Maximum number of generations to run.
	 * @return True if the stop condition has been reached (can't run anymore)
	 */
	public boolean runGenerations(int n)
	{
		// TODO: Stub
		return false;
	}
	
	public Genome getBestGenome()
	{
		return population.get(0);
	}
	
	public Genome[] getAllGenome()
	{		
		Genome[] ret = new Genome[population.size()];
		return population.toArray(ret);
	}
	
	public Double getBestFitness()
	{
		return getBestGenome().getFitness();
	}
	
	public Double[] getAllFitness()
	{
		Double ret[] = new Double[population.size()];
		int i = 0;
		for (Genome aux:population)
		{
			ret[i] = aux.getFitness();
			i++;
		}
		return ret;
	}
	
	public void setGenerator(GeneratingOperator op)
	{
		genOp = op;
	}
	
	public void addSelection(SelectionOperator op)
	{
		op.initialize(param);
		selectOpList.add(op);
	}
	
	/**
	 * Return true if the end condition has been reached
	 * @return
	 */
	public boolean endCondition()
	{
		return true;
	}
	
	
	// Private methods
	
	
	
}
