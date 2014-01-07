package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;

import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * Implements a simple genetic algorithm
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 */
public class GeneticAlgorithm {

	Parameter param;
	
	GeneratingOperator genOp;
	FitnessEvaluation eval;
	
	ArrayList<SelectionOperator> selectOpList;
	ArrayList<Genome> population;
	
	Genome best;
	
	int maxgeneration;
	int currentgeneration;
	
	
	public GeneticAlgorithm(Parameter p)
	{
		selectOpList = new ArrayList<SelectionOperator>();
		population = new ArrayList<Genome>();
		param = p;
		maxgeneration = Integer.parseInt(p.getParameter("generation size", null));
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
		currentgeneration = 0;
		
		int popsize = Integer.parseInt(param.getParameter("population size", null));
		int count = 0;
		
		Genome[] newguys;
		while (count < popsize)
		{
			newguys = genOp.apply(null);
			for (int i = 0; i < newguys.length; i++)
				population.add(newguys[i]);
			count += newguys.length;
		}		
		evaluateIndividuals();
		
		best = population.get(0).clone();
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
		
		while (n != 0 || !hasEnded())
		{
			runOneGeneration();
			n--;
		}
		return hasEnded();
	}
	
	public Genome getBestGenome()
	{
		return best;
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
	
	public void addInitializationOperator(GeneratingOperator op)
	{
		genOp = op;
	}
	
	public void addSelectionOperator(SelectionOperator op)
	{
		selectOpList.add(op);
	}
	
	public boolean hasEnded()
	{
		return !(currentgeneration < maxgeneration);
	}
	
	
	//******** Private methods *********//
	
	void evaluateIndividuals()
	{
		for (Genome aux: population)
			eval.evaluate(aux);
		
		Collections.sort(population);
	}
	
	void runOneGeneration()
	{
		ArrayList<Genome> newpop = new ArrayList<Genome>();
		for (SelectionOperator aux:selectOpList)
			aux.preSelectionHook();
		
		int count = 0;
		while (newpop.size() < population.size())
		{
			Genome[] list = selectOpList.get(count).select(population);
			if (list != null)
				for (int i = 0; i < list.length; i++)
					if (newpop.size() < population.size())
						newpop.add(list[i]);
			count += count%selectOpList.size();
		}

		population = newpop;
		evaluateIndividuals();
		if (best.compareTo(population.get(0)) > 0)
			best = population.get(0).clone();
		
		currentgeneration++;
	}
	
}
