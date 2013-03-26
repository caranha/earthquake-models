package jp.ac.tsukuba.cs.conclave.earthquake;

import jp.ac.tsukuba.cs.conclave.earthquake.RI.RIModel;

public class TestMain {

	public static void main(String[] args) {
		RawData r = new RawData();
		//r.loadData("/home/caranha/Desktop/Work/Earthquake_bogdan/data/jma_cat_100l_test");
		r.loadData("/home/caranha/Desktop/Work/Earthquake_bogdan/data/jma_cat_2000_2012_Mth2.5_formatted.dat");

		RIModel tr = new RIModel(r);
	
	}
}
