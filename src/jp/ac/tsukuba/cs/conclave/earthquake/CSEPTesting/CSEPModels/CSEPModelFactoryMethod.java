package jp.ac.tsukuba.cs.conclave.earthquake.CSEPTesting.CSEPModels;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;

public interface CSEPModelFactoryMethod {

	public CSEPModel modelFromRandom(int events);
	public CSEPModel modelFromData(DataList data);
	public CSEPModel modelFromRealArray(double[] array, double lambda);
	
}
