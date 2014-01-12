package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver;

import java.util.Random;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.GeneticAlgorithm;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RALinearMutation;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RALinearRandomGeneration;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RAUniformCrossover;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.selection.EliteSelection;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.selection.TournamentSelection;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class GASolver {

	GeneticAlgorithm ga;
	boolean verbose = false;
	CSEPModel best;
	
	SimpleLogLikelihoodFitness fittest;
	
	public void runGA(int rep)
	{
		if (verbose)
			System.out.println("Initializing GA");
		ga.initializeRun();
		ga.setRepetition(rep);
		
		if (verbose)
			System.out.print("Running GA: ");
		while (!ga.runGenerations(1))
		{
			ga.GAMOutput();
			if (verbose && (ga.getCurrentGeneration()%20 == 0))
				System.out.print(".");
		}
		if (verbose)
			System.out.println();
		
	}
	
	public void configureGA(DataList data, Parameter p, Random d)
	{
		CSEPModelFactory factory = new CSEPModelFactory(p);
		CSEPModel comparator = factory.modelFromData(data);
		p.addParameter("RAG gene size", Integer.toString(comparator.getTotalBins()));
		
		double lambdamult = Double.parseDouble(p.getParameter("lambda multiplier", "2"));
		fittest = new SimpleLogLikelihoodFitness(comparator, factory,lambdamult);
		
		// Evolutionary Operators
		BreedingPipeline generator = new RALinearRandomGeneration();
		BreedingPipeline elitism = new EliteSelection();
		BreedingPipeline mutation = new RALinearMutation();
		BreedingPipeline crossover = new RAUniformCrossover();
		crossover.chainBreedingPipeline(new TournamentSelection());
		crossover.chainBreedingPipeline(new TournamentSelection());
		mutation.chainBreedingPipeline(crossover);
		
		ga = new GeneticAlgorithm(p, d);
		ga.addInitializationOperator(generator);
		ga.addBreedingOperator(elitism);
		ga.addBreedingOperator(mutation);
		ga.addFitnessMeasure(fittest);
	}
	
	public CSEPModel getBest()
	{
		return fittest.createModelFromGenome(ga.getBestGenome());
	}
	
	public void setVerbose(Boolean b)
	{
		verbose = b;
	}
	
}
