package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.utils.MathUtils;


/***
 * Implement the Tests proposed in the CSEP Experiment Suite. 
 * The implementation of the N, L and R test are based on Schorlemmer 2007
 * 
 * FIXME: the JMA earthquake catalog has very low errors (private 
 * communications with Bogdan), which makes it hard to calculate variations 
 * of the observation catalog. Thus, we are assuming no variation in the 
 * observation catalog, which means the statistics calculated here will 
 * have variance equal to zero. This is not an ideal state for comparisons.
 * 
 * FIXME: in the simulation based testings, I should probably provide the 
 * simulations, to avoid randomness in the simulation generation affect
 * repeated testings.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class CSEPTests {

	static public double NTest(CSEPModel Reference, CSEPModel Test, int n)
	{
		double sum = 0;
		CSEPModel simulations[] = generateSimulations(Test, n);
		for (int i = 0; i < n; i++)
			if (simulations[i].getTotalEvents() <= Reference.getTotalEvents())
				sum += 1;
		return sum/n;
	}
	
	static public double LTest(CSEPModel Reference, CSEPModel Test, int n)
	{
		double sum = 0;
		double baselikelihood = Test.calcLogLikelihood(Reference);
		
		CSEPModel simulations[] = generateSimulations(Test, n);
		for (int i = 0; i < n; i++)
		{
			double simll = Test.calcLogLikelihood(simulations[i]);
			if (simll <= baselikelihood) 
				sum += 1;
		}
		return sum/n;
	}
	
	static public double RTest(CSEPModel Reference, CSEPModel H1, CSEPModel H2, int n)
	{
		// calculate base log likelihood ratio
		double sum = 0;
		double baselikelihoodratio = H2.calcLogLikelihood(Reference) - H1.calcLogLikelihood(Reference);
		
		// compare base log likelihood ratio with the LLR using simulated H2 as base
		CSEPModel simulations[] = generateSimulations(H2,n);
		for (int i = 0; i < n; i++)
		{
			double simllr = H2.calcLogLikelihood(simulations[i]) - H1.calcLogLikelihood(simulations[i]);
			if (simllr <= baselikelihoodratio)
				sum += 1;
		}
		
		return sum/n;
	}
	
	static CSEPModel[] generateSimulations(CSEPModel reference, int n)
	{
		CSEPModel[] ret = new CSEPModel[n];

		for (int i = 0; i < n; i++)
		{
			int values[] = new int[reference.getTotalBins()];
			int j = 0;
			for (Integer aux: reference)
			{
				values[j] = MathUtils.getPoisson(CSEPpredictor.dice.nextDouble(), aux);
				j++;
			}
			ret[i] = CSEPpredictor.getModelFactory().modelFromIntegerArray(values);
		}
		return ret;
	}
	
}
