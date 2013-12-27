package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting;

import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/**
 * This is the entry class for generating a CSEP prediction model using Genetic Algorithms.
 * 
 * It reads a file with the parameters for the experiment, and then generates a prediction 
 * model by Evolutionary Algorithm using those parameters.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class EvoCSEPpredictor {

	static final int EXIT_ERROR = 1;
	static Parameter param;
	
	/**
	 * @param args: an external file that dictates the parameters for this run.
	 */
	public static void main(String[] args) {

		
		// DONE: Read parameters from parameter file
		if (args == null)
		{
			System.err.println("Error: Parameter file not specified");
			System.exit(EXIT_ERROR);
		}
		
		readParameterFile(args[0]);
		
		
		
		// TODO: Read datafile
		// TODO: Set up evolutionary algorithm
		// TODO: Evolution and Evaluation
		
		
	}
	
	/**
	 * Populates a static global parameter variable with the 
	 * filename passed as a parameter.
	 * 
	 * @param location of the parameter file relative to the executable
	 */
	static void readParameterFile(String pfile)
	{
		param = new Parameter();
		try	{
			param.loadTextFile(pfile);
		} catch (Exception e)
		{
			System.err.println("Error reading parameter file: "+e.getMessage());
			System.exit(EXIT_ERROR);
		}
	}
	
	static public Parameter getParameter()
	{
		return param;
	}
}
