package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.GASolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.FitnessEvaluation;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.GeneticAlgorithm;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RALinearMutation;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RALinearRandomGeneration;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RAUniformCrossover;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.realarray.RealArrayGenome;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.selection.EliteSelection;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.selection.TournamentSelection;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class GASolver {

	// Read necessary parameters
	// Set the parameters for the genome size (random creator)
	// Setup operators
	// Execute Evolutionary Computation
	// Extract results
	
	public static void simpleGAEvolution(DataList data, Parameter p, Random d)
	{
		// Problem Operators: Factory and Fitness
		CSEPModelFactory factory = new CSEPModelFactory(p);
		CSEPModel comparator = factory.modelFromData(data);
		comparator.getEventMap().saveToFile("base.png");
		p.addParameter("RAG gene size", Integer.toString(comparator.getTotalBins()));
		SimpleLogLikelihoodFitness fittest = 
				new SimpleLogLikelihoodFitness(comparator, factory);
		
		// Evolutionary Operators
		BreedingPipeline generator = new RALinearRandomGeneration();
		BreedingPipeline elitism = new EliteSelection();
		BreedingPipeline mutation = new RALinearMutation();
		BreedingPipeline crossover = new RAUniformCrossover();
		crossover.chainBreedingPipeline(new TournamentSelection());
		crossover.chainBreedingPipeline(new TournamentSelection());
		mutation.chainBreedingPipeline(crossover);
		
		GeneticAlgorithm ga = new GeneticAlgorithm(p, d);
		ga.addInitializationOperator(generator);
		ga.addBreedingOperator(elitism);
		ga.addBreedingOperator(mutation);
		ga.addFitnessMeasure(fittest);
		
		ga.initializeRun();
		while (!ga.runGenerations(100))
		{
			System.out.println(ga.getBestFitness());
			CSEPModel bestresult = factory.modelFromRealArray(((RealArrayGenome) ga.getBestGenome()).getGenes(), fittest.lambda);
			bestresult.getEventMap().saveToFile(String.format("generation%03d", ga.getCurrentGeneration())+".png");
			System.out.print(".");
		}

	}
	
	
	
	
	public static void codeTester(DataList data, Parameter p, Random d)
	{
		BreedingPipeline test;
		BreedingPipeline test2;
		FitnessEvaluation fittest;
		
		CSEPModelFactory factory = new CSEPModelFactory(p);
		CSEPModel comp = factory.modelFromData(data);
		
		test = new RAUniformCrossover();
		test.chainBreedingPipeline(new RALinearRandomGeneration());
		test.chainBreedingPipeline(new RALinearRandomGeneration());
		
		test2 = new TournamentSelection();
		
		
		fittest = new SimpleLogLikelihoodFitness(comp, factory);
		p.addParameter("RAG gene size", Integer.toString(comp.getTotalBins()));
		
		ArrayList<Genome> aux = new ArrayList<Genome>();		
		test.setup(p, d);
		test2.setup(p, d);
		
		
		for (int i = 0; i < 20; i++)
			aux.addAll(test.apply(null));
		
		for (Genome foo:aux)
		{
			double fit = fittest.evaluate(foo);
			foo.setFitness(fit);
		}
		
		Collections.sort(aux);
		for (Genome foo:aux)
			System.out.print(foo.getFitness()+" ");
		System.out.println();
		

		List<Genome> aux2;
		
		test2.preGenerationHook();
		aux2 = test2.apply(aux);
		System.out.println(aux2.get(0).getFitness());
		
		
	}
}
