package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;

public interface CSEPModel {

	public Double getLogLikelihood(CSEPModel comp);
	public Double getLogLikelihood();
	
	public MapImage getAreaMap();
	public MapImage getEventMap();
	
	public int getTotalEvents();
	
}
