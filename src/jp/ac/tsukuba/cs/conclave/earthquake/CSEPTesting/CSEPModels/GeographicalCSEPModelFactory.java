package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import java.util.Random;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPpredictor;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class GeographicalCSEPModelFactory implements CSEPModelFactoryMethod {

	Parameter param;
	Double startlon;
	Double deltalon;
	int binlon;
	
	Double startlat;
	Double deltalat;
	int binlat;
		
	public GeographicalCSEPModelFactory(Parameter param)
	{
		startlon = Double.parseDouble(param.getParameter("start lon", null)); 
		startlat = Double.parseDouble(param.getParameter("start lat", null));
		deltalon = Double.parseDouble(param.getParameter("delta lon",null));
		deltalat = Double.parseDouble(param.getParameter("delta lat",null));
		binlon = Integer.parseInt(param.getParameter("grid lon",null));
		binlat = Integer.parseInt(param.getParameter("grid lat",null));
	}
		
	@Override
	public CSEPModel modelFromRandom(int events) {

		Random dice = CSEPpredictor.dice;
		GeographicalCSEPModel ret = new GeographicalCSEPModel(startlon,startlat,deltalon,deltalat,binlon,binlat);
		
		// We need to guarantee that each bin has at least one event.
		for (int i = 0; i < ret.dimlength[0]; i++)
			for (int j = 0; j < ret.dimlength[1]; j++)
			{
				events -=1;
				ret.bins[i][j] += 1;
				ret.totalevents++;
			}

		for (int i = 0; i < events; i++)
		{
			int intx = dice.nextInt(ret.dimlength[0]);
			int inty = dice.nextInt(ret.dimlength[1]);
			ret.bins[intx][inty] += 1;
			if (ret.bins[intx][inty] > ret.maxevents)
				ret.maxevents = ret.bins[intx][inty];
			ret.totalevents++;
		}
		
		return ret;
	}

	@Override
	public CSEPModel modelFromData(DataList data) {
		
		GeographicalCSEPModel ret = new GeographicalCSEPModel(startlon,startlat,deltalon,deltalat,binlon,binlat);
		
		for (DataPoint aux: data)
			{
				int xindex = -1;
				int yindex = -1;
				
				for (int i = 0; i < ret.dimlength[0]; i++)
					if (aux.longitude >= ret.base[0] + ret.delta[0]*i && aux.longitude < ret.base[0] + ret.delta[0]*(i+1))
						xindex = i;
				
				for (int i = 0; i < ret.dimlength[1]; i++)
					if (aux.latitude >= ret.base[1] + ret.delta[1]*i && aux.latitude < ret.base[1] + ret.delta[1]*(i+1))
						yindex = i;
				if (xindex != -1 && yindex != -1)
				{
					ret.bins[xindex][yindex] += 1;
					if (ret.bins[xindex][yindex] > ret.maxevents)
						ret.maxevents = ret.bins[xindex][yindex];
					ret.totalevents++;
				}
			}
		return ret;
	}


	@Override
	public CSEPModel modelFromRealArray(double[] array, double lambda) {		
		GeographicalCSEPModel ret = new GeographicalCSEPModel(startlon,startlat,deltalon,deltalat,binlon,binlat);
		ret.maxevents = 0;
		for(int i = 0; i < binlon; i++)
			for(int j = 0; j < binlat; j++)
			{
				ret.bins[i][j] = getPoisson(array[i*binlat+j], lambda);
				if (ret.maxevents < ret.bins[i][j])
					ret.maxevents = ret.bins[i][j];
				ret.totalevents += ret.bins[i][j];
			}
		return ret;
	}
	
	/**
	 * Algorithm based on the random generation of Poissonian numbers,
	 * based on Knuth and Numerical Recipes (7.3.12).
	 * 
	 * In the sources above, prob is actually a uniform random number 
	 * between 0-1, sampled each iteration. I have to make sure that 
	 * my alteration for a fixed value (to replace gene values) is 
	 * correct...
	 * 
	 * @param lambda
	 * @return
	 */
	int getPoisson(double prob, double lambda)
	{
		double L = Math.exp(-lambda);
		int k = 0;
		double p = 1;

		do {
			k++;
			p = p*prob;			
		} while (p > L);
		
		return k-1;
	}
}
