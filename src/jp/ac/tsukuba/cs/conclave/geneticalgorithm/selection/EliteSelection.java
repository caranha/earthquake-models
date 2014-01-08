package jp.ac.tsukuba.cs.conclave.geneticalgorithm.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/*** 
 * 
 * Selects a number of the best individuals in the population, and 
 * copy them downwards as is. Can be used only once per generation.
 * 
 * parameters:
 * "elite size" - number of elite individuals to be selected.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 *
 */
public class EliteSelection implements BreedingPipeline {

	int elitesize;
	int selectednumber;
	
	@Override
	public void setup(Parameter p, Random d) {
		elitesize = Integer.parseInt(p.getParameter("elite size", "1"));
		selectednumber = 0;
	}

	@Override
	public void chainBreedingPipeline(BreedingPipeline bp) {
		// no-op
	}

	@Override
	public void preGenerationHook() {
		selectednumber = elitesize;
	}

	@Override
	public List<Genome> apply(List<Genome> inds) {
		if (selectednumber > 0);
		ArrayList<Genome> ret = new ArrayList<Genome>();
		
		for (int i = 0; i < selectednumber; i++)
		{
			ret.add(inds.get(i).clone());
		}
		return ret;
	}

}
