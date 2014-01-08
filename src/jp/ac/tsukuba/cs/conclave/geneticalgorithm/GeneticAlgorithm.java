package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * Implements a simple genetic algorithm
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 */
public class GeneticAlgorithm {

	Parameter param;
	Random dice;
	
	BreedingPipeline initOperator; // Generates the initial population	
	ArrayList<BreedingPipeline> breedingOperators;
	FitnessEvaluation eval;

	ArrayList<Genome> population;	
	Genome best;
	
	int maxgeneration;
	int currentgeneration;
	
	
	public GeneticAlgorithm(Parameter p, Random d)
	{
		param = p; 
		dice = d;
		maxgeneration = Integer.parseInt(p.getParameter("generation size", "10"));
		
		breedingOperators = new ArrayList<BreedingPipeline>();
		
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
	public void initializeRun()
	{
		currentgeneration = 0;
		
		int popsize = Integer.parseInt(param.getParameter("population size", null));
		int count = 0;
		
		initOperator.setup(param, dice);
		for (BreedingPipeline aux: breedingOperators)
			aux.setup(param, dice);
		
		
		List<Genome> newguys;
		initOperator.preGenerationHook();
		
		while (count < popsize)
		{
			newguys = initOperator.apply(null);
			for (Genome aux: newguys)
				population.add(aux);
			count += newguys.size();
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
	
	public void addInitializationOperator(BreedingPipeline op)
	{
		initOperator = op;
	}
	
	public void addSelectionOperator(BreedingPipeline op)
	{
		breedingOperators.add(op);
	}
	
	public boolean hasEnded()
	{
		return !(currentgeneration < maxgeneration);
	}
	
	
	//******** Private methods *********//
	
	void evaluateIndividuals()
	{
		for (Genome aux: population)
		{
			double result = eval.evaluate(aux);
			aux.setFitness(result);
		}
		
		Collections.sort(population);
	}
	
	void runOneGeneration()
	{
		ArrayList<Genome> newpop = new ArrayList<Genome>();
		for (BreedingPipeline aux:breedingOperators)
			aux.preGenerationHook();
		
		int count = 0;
		List<Genome> children;
		while (newpop.size() < population.size())
		{
			children = breedingOperators.get(count).apply(population);
			if (children != null)
				for (Genome aux:children)
					if (newpop.size() < population.size())
						newpop.add(aux);
			count = (count+1)%breedingOperators.size();
			// TODO: check for infinite loops of non-generation
		}

		population = newpop;
		evaluateIndividuals();
		if (best.compareTo(population.get(0)) > 0)
			best = population.get(0).clone();
		
		currentgeneration++;
	}
	
}
