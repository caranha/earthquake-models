package jp.ac.tsukuba.cs.conclave.earthquake.directionclustering;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataList;
import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.CompositeEarthquakeFilter;
import jp.ac.tsukuba.cs.conclave.earthquake.filtering.MagnitudeFilter;

/**
 * Entry point for development functions for the directional clustering experiment
 * 
 * @author caranha
 *
 */

public class ClusteringDev {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// First Load all the data (Separate data for JMA and FNET)
		DataList fnetTotal = new DataList();
		DataList jmaTotal = new DataList();
		
		// FIXME: data location must be parametrized
		fnetTotal.loadData("assets/catalog_fnet_1997_20130429_f3.txt","Fnet");
		jmaTotal.loadData("assets/jma_cat_2000_2012_Mth2.5_formatted.dat","JMA");
				
		// Second Filter FNET quakes with M > 7
		CompositeEarthquakeFilter MagFilter = new CompositeEarthquakeFilter();
		MagnitudeFilter M7 = new MagnitudeFilter();
		M7.setmin(7);		
		MagFilter.addFilter(M7);
		DataList fnetM7 = MagFilter.filter(fnetTotal);
		
		for(DataPoint aux: fnetM7)
		{
			DataList tmp = new BasicLocalFiltering().filter(aux, jmaTotal);
			if (tmp.size() >= 5)
				System.out.println("M"+aux.magnitude+" "+aux.time + " -- Events in Window: "+tmp.size());
		}
		
	}

}
