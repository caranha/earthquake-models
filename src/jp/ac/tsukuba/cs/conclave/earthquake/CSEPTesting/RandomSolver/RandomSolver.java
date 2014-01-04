package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.RandomSolver;

import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.CSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels.GeographicalCSEPModel;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

public class RandomSolver {

	CSEPModel best;
	CSEPModel data;
	Parameter param;
	
	public void init(DataList events, Parameter param)
	{
		best = null;
		this.param = param;
	
		// TODO: create a factory to decouple CSEPModel implementation
		data = new GeographicalCSEPModel(Float.parseFloat(param.getParameter("start lon", null)), 
				Float.parseFloat(param.getParameter("start lat", null)),
				Float.parseFloat(param.getParameter("delta lon",null)),
				Float.parseFloat(param.getParameter("delta lat",null)),
				Integer.parseInt(param.getParameter("grid lon",null)),
				Integer.parseInt(param.getParameter("grid lat",null)));
		
		data.addData(events);
	}
	public void init (CSEPModel data, Parameter param)
	{
		best = null;
		this.param = param;
		this.data = data;
	}
	
	public void execute(int comparisons)
	{
		double baselon = Double.parseDouble(param.getParameter("start lon", null));
		double baselat = Double.parseDouble(param.getParameter("start lat", null));
		double deltalon = Double.parseDouble(param.getParameter("delta lon", null));
		double deltalat = Double.parseDouble(param.getParameter("delta lat", null));
		int binlon = Integer.parseInt(param.getParameter("grid lon",null));
		int binlat = Integer.parseInt(param.getParameter("grid lat",null));
		
		
		
		for (int i = 0; i < comparisons; i++)
		{
			
			// TODO: Decouple the model Implementation from the Random Solver
			GeographicalCSEPModel current = new GeographicalCSEPModel(baselon, baselat, deltalon, deltalat, binlon, binlat);
			current.initRandom(data.getTotalEvents());
			
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
