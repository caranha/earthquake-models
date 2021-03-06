package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver;

import java.util.Random;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPpredictor;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.GeneticAlgorithm;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RAClampValues;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RALinearMutation;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RALinearRandomGeneration;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RAUniformCrossover;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RAandx;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.selection.EliteSelection;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.selection.TournamentSelection;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class GASolver {

	GeneticAlgorithm ga;
	boolean verbose = false;
	CSEPModel best;
	
	CSEPFitness fittest;
	
	public void runGA(int rep)
	{
		ga.initializeRun();
		ga.setRepetition(rep);
		
		if (verbose)
			System.out.print("Running GA:");
		while (!ga.runGenerations(1))
		{
			ga.GAMOutput();
			// TODO: make periods proportional to 5% progress
			if (verbose && (ga.getCurrentGeneration()%10 == 0))
				System.out.print(".");
		}
		if (verbose)
			System.out.println();
	}
	
	public void configureGA(DataList data, Parameter p, Random d)
	{
		CSEPModelFactory factory = CSEPpredictor.getModelFactory();
		CSEPModel comparator = factory.modelFromData(data);
		p.addParameter("RAG gene size", Integer.toString(comparator.getTotalBins()));
		
		double lambdamult = Double.parseDouble(p.getParameter("lambda multiplier", "2"));

		String fitness = p.getParameter("fitness operator","simple");
		int fit = -1;
		if (fitness == "simulated") fit = 0;
		if (fitness == "timeslice") fit = 1;
		if (fitness == "simple") fit = 2;
		
		
		switch(fit)
		{
		case 0:
			fittest = new SimulatedLogLikelihoodFitness(CSEPpredictor.getTrainingData(), comparator, factory,lambdamult);
			break;
		case 1:
			fittest = new TimeWeightedLogLikelihoodFitness(CSEPpredictor.getTrainingData(), comparator, factory,lambdamult);
			break;
		case 2:
			fittest = new SimpleLogLikelihoodFitness(comparator, factory,lambdamult);	
			break;
		default:			
			System.err.println("Error: Crossover parameter not recognized, using uniform");
			System.exit(CSEPpredictor.EXIT_ERROR);
		}

		// Evolutionary Operators
		BreedingPipeline generator = new RALinearRandomGeneration();
		BreedingPipeline elitism = new EliteSelection();
		BreedingPipeline mutation = new RALinearMutation();
		
		String crossover = p.getParameter("crossover operator", "uniform");
		int xoveridx = -1;
		if (crossover == "andx") xoveridx = 0;
		if (crossover == "uniform") xoveridx = 1;
		BreedingPipeline xover;
		
		
		switch(xoveridx)
		{
		case 0:
			BreedingPipeline andx = new RAandx();
			int parents = Integer.parseInt(p.getParameter("andx parents","3"));
			for (int i = 0; i < parents; i++)
				andx.chainBreedingPipeline(new TournamentSelection());
			xover = new RAClampValues();
			xover.chainBreedingPipeline(andx);
			break;
		default:
			System.err.println("Error: Crossover parameter not recognized, using uniform");
		case 1:
			xover = new RAUniformCrossover();
			xover.chainBreedingPipeline(new TournamentSelection());
			xover.chainBreedingPipeline(new TournamentSelection());
			break;
		}
		
		mutation.chainBreedingPipeline(xover);
		
		ga = new GeneticAlgorithm(p, d);
		ga.addInitializationOperator(generator);
		ga.addBreedingOperator(elitism);
		ga.addBreedingOperator(mutation);
		ga.addFitnessMeasure(fittest);
	}
	
	public CSEPModel getBest()
	{
		// TODO: encapsulate this
		return fittest.createModelFromGenome(ga.getBestGenome());
	}
	
	public void setVerbose(Boolean b)
	{
		verbose = b;
	}	
}
