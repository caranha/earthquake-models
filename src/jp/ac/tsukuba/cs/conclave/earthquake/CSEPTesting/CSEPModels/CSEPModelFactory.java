package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPpredictor;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;

public class CSEPModelFactory {

	CSEPModelFactoryMethod factory;
	
	public CSEPModelFactory(Parameter p)
	{
		String mode = p.getParameter("modeltype", null);
		
		switch (mode)
		{
		case "geographical":
			factory = new GeographicalCSEPModelFactory(p);
			break;
		default:
			System.err.println("Error: No valid modeltype parameter");
			System.exit(CSEPpredictor.EXIT_ERROR);
		}
	}
	
	public CSEPModel modelFromRandom(int event)
	{
		return factory.modelFromRandom(event);
	}

	public CSEPModel modelFromData(DataList data)
	{
		return factory.modelFromData(data);
	}
	
	public CSEPModel modelFromRealArray(double array[], double lambda)
	{
		return factory.modelFromRealArray(array, lambda);
	}
}
