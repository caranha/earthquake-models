package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.RandomSolver;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class RandomSolver {

	CSEPModel best;
	CSEPModel data;

	Parameter param;
	CSEPModelFactory factory;
	
	int repetitions;
	int suddendeath;
	
	public void setup(DataList events, Parameter param)
	{
		best = null;
		this.param = param;
		factory = new CSEPModelFactory(param);
		data = factory.modelFromData(events);
		
		repetitions = Integer.parseInt(param.getParameter("random search max","500000"));
		suddendeath = Integer.parseInt(param.getParameter("random death count","-1"));
	}
	
	public void execute(boolean verbose)
	{
		int sdcount = suddendeath;
		System.out.print("Running Random Solver");
		for (int i = 0; i < repetitions; i++)
		{
			CSEPModel current = factory.modelFromRandom(data.getTotalEvents());			
			Double fit = current.calcLogLikelihood(data);
			
			if (best == null || fit > best.getLogLikelihood())
			{
				best = current;
				sdcount = suddendeath;
			}
			else
				sdcount--;
			
			if (verbose && i%(repetitions/20) == 0)
				System.out.print(".");
			
			if (sdcount == 0)
			{
				if (verbose)
					System.out.println("sudden death!");
				return;
			}
		}
		System.out.println();
	}
	
	public CSEPModel getBest()
	{
		return best;
	}
	
	
}
