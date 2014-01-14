package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import gam_writer.RWriter;
import jp.ac.tsukuba.cs.conclave.utils.MathUtils;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * Implements a simple genetic algorithm.
 * 
 * Parameters:
 * "generation size" (default value 10)
 * "population size" (default value 100)
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
	
	boolean gamVerbose;
	RWriter gamOutput;
	int repetition = 0;
	
	
	public GeneticAlgorithm(Parameter p, Random d)
	{
		param = p; 
		dice = d;
		maxgeneration = Integer.parseInt(p.getParameter("generation size", "10"));
		
		breedingOperators = new ArrayList<BreedingPipeline>();		
		population = new ArrayList<Genome>();
		param = p;
		
		gamVerbose = Boolean.parseBoolean(p.getParameter("gam output", "true"));
		gamOutput = new RWriter();
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
		population.clear();
		
		int popsize = Integer.parseInt(param.getParameter("population size", "100"));
		int count = 0;
		
		initOperator.setup(param, dice);
		for (BreedingPipeline aux: breedingOperators)
			aux.setup(param, dice);
		eval.setup(param);
		
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
		
		while (n > 0 && !hasEnded())
		{
			runOneGeneration();
			n--;
		}
		return hasEnded();		
	}
	
	public boolean hasEnded()
	{
		return (maxgeneration <= currentgeneration);
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
	
	public double[] getAllFitness()
	{
		double ret[] = new double[population.size()];
		int i = 0;
		for (Genome aux:population)
		{
			ret[i] = aux.getFitness();
			i++;
		}
		return ret;
	}
	
	public int getCurrentGeneration()
	{
		return currentgeneration;
	}
	
	public void addInitializationOperator(BreedingPipeline op)
	{
		initOperator = op;
	}
	
	public void addBreedingOperator(BreedingPipeline op)
	{
		breedingOperators.add(op);
	}
	
	public void clearBreedingOperators()
	{
		breedingOperators.clear();
	}
	
	public void addFitnessMeasure(FitnessEvaluation e)
	{
		eval = e;
	}
	
	public void setRepetition(int r)
	{
		repetition = r;
	}
	
	public void GAMOutput()
	{
		if (!gamVerbose)
			return;
		
		// TODO: Encapsulate the cleaning of infinite values from here.
		gamOutput.set_repetition_no(repetition);
		gamOutput.set_generation_no(currentgeneration);
		
		double[] genfit = getAllFitness();
		ArrayList<Double> avvie = new ArrayList<Double>();		
		double avg = 0;		
		for (int i = 0; i < genfit.length;i++)
			if (!Double.isInfinite(genfit[i]))
			{
				avvie.add(genfit[i]);
				avg += genfit[i];
			}
		Double[] cleanfit = avvie.toArray(new Double[1]);
		
		gamOutput.set_avg_fitness(avg/avvie.size());
		gamOutput.set_best_fitness(genfit[0]);
		
		if (cleanfit[0] != null)
			gamOutput.set_fitness_stdev(MathUtils.deviation(cleanfit));
		else
			gamOutput.set_fitness_stdev(0);

		gamOutput.set_diversity(0);
		
		gamOutput.write_line();
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
