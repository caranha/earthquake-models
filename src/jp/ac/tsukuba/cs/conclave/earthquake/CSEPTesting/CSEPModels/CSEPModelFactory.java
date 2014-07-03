package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPpredictor;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;

public class CSEPModelFactory {

	CSEPModelFactoryMethod factory;
	
	public CSEPModelFactory(Parameter p)
	{
		String mode = p.getParameter("modeltype", null);
		
		if (mode == "geographical")
		{
			factory = new GeographicalCSEPModelFactory(p);
		}
		else
		{
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
	
	public CSEPModel modelFromIntegerArray(int array[])
	{
		return factory.modelFromIntegerArray(array);
	}
	
	public CSEPModel simulationFromModel(CSEPModel reference)
	{
		return factory.simulationFromModel(reference);
	}
	
	public CSEPModel modelFromTextFile(String filename)
	{
		int[] array = null;
		
		BufferedReader reader;
    	String line = null;
    	try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			
			line = reader.readLine(); // first line: identifier
			line = reader.readLine(); // second line: number of elements;
			int bins = Integer.parseInt(line.split(" ")[0]);
			array = new int[bins];
			for (int i = 0; i < bins; i++)
				array[i] = Integer.parseInt(reader.readLine());
		} catch (IOException e) {
			System.err.println("Error Trying to load a model file: "+filename+" Error: "+e.getMessage());
			e.printStackTrace();
		}
		
		return factory.modelFromIntegerArray(array);
	}
}
