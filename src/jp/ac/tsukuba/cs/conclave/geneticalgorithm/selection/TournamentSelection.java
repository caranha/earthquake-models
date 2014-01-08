package jp.ac.tsukuba.cs.conclave.geneticalgorithm.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ac.tsukuba.cs.conclave.geneticalgorithm.BreedingPipeline;
import jp.ac.tsukuba.cs.conclave.geneticalgorithm.Genome;
import jp.ac.tsukuba.cs.conclave.utils.Parameter;

/***
 * 
 * Implements a deterministic tournament selection operator. It
 * selects one individual from the population.
 * 
 * Parameters:
 * "tournament size" - the number of individuals selected for the tournament
 * 
 * @author Claus Aranha (caranha@cs.tsukuba.ac.jp)
 */
public class TournamentSelection implements BreedingPipeline {

	int tournamentsize;
	Random dice;

	@Override
	public void setup(Parameter p, Random d) {
		tournamentsize = Integer.parseInt(p.getParameter("tournament size", "5"));
		dice = d;
	}

	@Override
	public void chainBreedingPipeline(BreedingPipeline bp) {
		System.err.println("TournamentSelection Warning: Should not chain pipelines here");
	}

	@Override
	public void preGenerationHook() {
		// no op
	}

	@Override
	public List<Genome> apply(List<Genome> inds) {
		ArrayList<Genome> ret = new ArrayList<Genome>();
		ArrayList<Genome> tournament = new ArrayList<Genome>();
		
		for(int i = 0; i < tournamentsize; i++)
		{
			Genome aux;
			do {
				aux = inds.get(dice.nextInt(inds.size()));
			} while (tournament.contains(aux));
			
			tournament.add(aux);
		}
				
		Collections.sort(tournament);
		ret.add(tournament.get(0).clone());
		
		return ret;
	}


}
