package bioinfo.ea;

import bioinfo.markov.MarkovException;
import bioinfo.markov.MarkovNetwork;
import bioinfo.utils.Utils;

public abstract class EvolutionAlgorithmOutputs extends EvolutionAlgorithm {
	private boolean[][] _scenarios;
	private String[] _expectedResults; // Encoded with 0, 1 and X if not determined.

	public EvolutionAlgorithmOutputs(EAResult eaResult,	boolean[][] scenarios, String[] expectedResults) throws EAException, MarkovException {
		super(eaResult);

		_scenarios = scenarios;
		_expectedResults = expectedResults;
	}

	@Override
	protected void grow() throws EAException, MarkovException {
		getFitRatings()[getHeadCount()] = 0; // Reset the summary
		
		// Loop on each individual
		for (int i = 0; i < getPopulation().length; i++) {
			getFitRatings()[i] = 0; 
			MarkovNetwork mkn = getPopulation()[i].getMKN();
			
			// Loop on each scenario for an individual.
			for (int j = 0; j < _scenarios.length; j++) {
				mkn.setInitialStates(_scenarios[j]);
				mkn.moveNext();
				
				// Rating
				String states = mkn.getOutputStates();
				if (states.length() != _expectedResults[j].length()) {
					throw new EAException();
				}
				
				getFitRatings()[i] += Utils.similarity(states, _expectedResults[j]);
			}
			
			getFitRatings()[getHeadCount()] += getFitRatings()[i];
		}
	}
}
