package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import java.util.Iterator;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;

public abstract class CSEPModel implements Iterable<Integer> {

	Double loglikelihood = null;
	
	public final Double calcLogLikelihood(CSEPModel comp)
	{
		loglikelihood = 0.0;
		Iterator<Integer> lambdait = this.iterator();
		Iterator<Integer> omegait = comp.iterator();
		
		while (lambdait.hasNext())
		{
			int lambda = lambdait.next();
			int omega = omegait.next();
			
			if (lambda == 0)
			{
				if (omega == 0)
					loglikelihood += 1;
				else
				{
					// TODO: replace this with a log message
					//System.err.println("[WARNING] Log likelihood calculation: Lambda 0, Omega not 0");
					loglikelihood = null;
					return loglikelihood;
				}
			}
			else
			{
				int logFacOmega = 0;
				for (int k = 0; k < omega; k++)
					logFacOmega += Math.log(k+1);
				loglikelihood += -lambda + omega*Math.log(lambda)-logFacOmega;
			}			
		}
		
		return getLogLikelihood();
	}
	
	public final Double getLogLikelihood()
	{
		return loglikelihood;
	}
	
	public abstract MapImage getAreaMap();
	public abstract MapImage getEventMap();	
	public abstract int getTotalEvents();
	
}
