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
	
	public void init(DataList events, Parameter param)
	{
		best = null;
		this.param = param;
		factory = new CSEPModelFactory(param);
		data = factory.modelFromData(events);
	}
	
	public void execute(int comparisons)
	{
		for (int i = 0; i < comparisons; i++)
		{
			CSEPModel current = factory.modelFromRandom(data.getTotalEvents());
			Double fit = current.getLogLikelihood(data);
			
			if (best == null || fit > best.getLogLikelihood(data))
				best = current;
		}
	}
	
	public CSEPModel getBest()
	{
		return best;
	}
	
	
}
