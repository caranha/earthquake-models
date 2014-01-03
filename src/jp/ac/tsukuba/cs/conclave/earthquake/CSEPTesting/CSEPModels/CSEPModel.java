package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.image.MapImage;

public interface CSEPModel {

	public void addData(DataList d);
	public void initRandom(int eventN);
	
	public float calculatelogLikelihood(CSEPModel comp);
	public MapImage getAreaMap();
	public MapImage getEventMap();
}
