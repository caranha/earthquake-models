package jp.ac.tsukuba.cs.conclave.geneticalgorithm;

import java.util.List;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/***
 * 
 * This interface describes an operator that selects some 
 * individuals form a list, selects them following some criteria, and 
 * returns a (possibly modified) list of individuals.
 * 
 * @author Claus Aranha (caranha@cs.tsukuba,ac,jp)
 *
 */
public interface SelectionOperator {

	public void initialize(Parameter param);
	public void reset();
	public Genome[] select(List<Genome> population);	
	
}
