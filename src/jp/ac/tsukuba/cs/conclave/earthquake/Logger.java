/***
 * This singleton(?) class handles logging. Higher log values equal to lower priority.
 * If no parameter is passed at initialization, everything is output to standard output.
 */

package jp.ac.tsukuba.cs.conclave.earthquake;

public class Logger {
	
	private static Logger instance;

	protected Logger() 
	{
	}
	
	public static synchronized Logger getInstance() 
	{
		if(instance == null) {
			instance = new Logger();
	      }	
		return instance;
	}

	public void log(String message)
	{
		log(message,0);
	}
	
	public void log(String message, int level)
	{
		//FIXME: Set up logging levels soon
		System.out.println(message);
	}
}
