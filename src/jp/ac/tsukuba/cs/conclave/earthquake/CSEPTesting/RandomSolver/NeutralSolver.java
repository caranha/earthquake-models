package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.RandomSolver;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModelFactory;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class NeutralSolver {

	CSEPModel best;
	CSEPModel data;
	Parameter param;
	CSEPModelFactory factory;
	
	public void setup(DataList events, Parameter param)
	{
		this.param = param;
		factory = new CSEPModelFactory(param);
		data = factory.modelFromData(events);
	}
	
	public void execute(boolean verbose)
	{
		if (verbose) 
			System.out.print("Generating Neutral Model");
		best = factory.modelFromNeutral();	
	}
	
	public CSEPModel getBest()
	{
		return best;
	}
	
	
}
