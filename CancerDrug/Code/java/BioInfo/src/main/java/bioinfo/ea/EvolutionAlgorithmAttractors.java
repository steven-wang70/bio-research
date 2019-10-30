package bioinfo.ea;

import java.util.ArrayList;
import java.util.List;

import bioinfo.markov.MarkovException;
import bioinfo.markov.MarkovNetwork;
import bioinfo.utils.Utils;

public abstract class EvolutionAlgorithmAttractors extends EvolutionAlgorithm {
	protected boolean[][] _scenarios;
	protected String[][] _expectedAttractors; // Encoded with 0, 1 and X if not determined.

	public EvolutionAlgorithmAttractors(EAResult eaResult, boolean[][] scenarios, String[][] expectedAttractors) throws EAException, MarkovException {
		super(eaResult);

		_scenarios = scenarios;
		_expectedAttractors = expectedAttractors;
	}

	@Override
	protected void grow() throws EAException, MarkovException {
		getFitRatings()[getHeadCount()] = 0; // Reset the summary
		
		// Loop on each individual
		for (int i = 0; i < getPopulation().length; i++) {
			getFitRatings()[i] = 0; 
			MarkovNetwork mkn = getPopulation()[i].getMKN();
			
			String[][] attractors = new String[_scenarios.length][];
			// Loop on each scenario for an individual.
			for (int j = 0; j < _scenarios.length; j++) {
				mkn.setInitialStates(_scenarios[j]);
				List<String> trace = new ArrayList<String>();
				while (true) {
					mkn.moveNext();
					attractors[j] = checkAttractor(trace, mkn.getFullGateStates());
					if (attractors[j] != null) {
						break;
					}
				}
			}
			
			getFitRatings()[i] = rateIndividual(attractors);
			getFitRatings()[getHeadCount()] += getFitRatings()[i];
		}
	}

	private String[] checkAttractor(List<String> trace, String gateStates) {
		if (trace.size() == 0) {
			trace.add(gateStates);
			return null;
		}
		
		for (int i = trace.size() - 1; i >= 0; i--) {
			if (trace.get(i).equals(gateStates)) {
				String[] result = new String[trace.size() - i];
				for (int j = i; j < trace.size(); j++) {
					result[j - i] = trace.get(j);
				}
				return result;
			}
		}
		
		trace.add(gateStates);
		return null;
	}

	protected double rateIndividual(String[][] attractors) {
		double rating = 0.0;
		
		for (String[] expectedAttractor : _expectedAttractors) {
			int bestFitIndex = -1;
			double bestFitRating = 0.0;
			for (int i = 0; i < attractors.length; i++) {
				if (attractors[i] == null || attractors[i].length != expectedAttractor.length) {
					continue;
				}
				
				double fitRating = calculateFitRating(attractors[i], expectedAttractor);
				if (bestFitRating < fitRating) {
					bestFitRating = fitRating;
					bestFitIndex = i;
				}
			}
			
			if (bestFitIndex != -1) {
				// We found one
				attractors[bestFitIndex] = null;
				rating += bestFitRating;
			}
		}
		
		return rating;
	}
	
	protected double calculateFitRating(String[] attractor, String[] expected) {
		double bestMatch = 0.0;
		
		int len = attractor.length;
		for (int i = 0; i < len; i++) {
			double matchRating = 0.0;
			for (int j =0; j < len; j++) {
				matchRating += Utils.similarity(attractor[j], expected[(i + j) % len]);
			}
			
			if (bestMatch < matchRating) {
				bestMatch = matchRating;
			}
		}
		return bestMatch;
	}
}
