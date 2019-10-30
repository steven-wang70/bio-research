package bioinfo.ea;

import bioinfo.markov.MarkovException;
import bioinfo.markov.MarkovNetwork;
import bioinfo.utils.CircularArrayList;
import bioinfo.utils.Utils;

public abstract class EvolutionAlgorithmFuzzyAttractors extends EvolutionAlgorithm {
	protected boolean[][] _scenarios;
	private String[] _expectedAttractors = null;
	
	public EvolutionAlgorithmFuzzyAttractors(EAResult eaResult, boolean[][] scenarios, String[] expectedAttractors) throws EAException, MarkovException {
		super(eaResult);
		_scenarios = scenarios;
		_expectedAttractors = expectedAttractors;
	}

	@Override
	protected void grow() throws EAException, MarkovException {
		getFitRatings()[getHeadCount()] = 0; // Reset the summary
		
		// Loop on each individual
		for (int i = 0; i < getHeadCount(); i++) {
			grow(i);
		}
	}
	
	void grow(int individualIndex) {
		getFitRatings()[individualIndex] = 0; 
		try {
			getFitRatings()[individualIndex] = doGrow(individualIndex);
			getFitRatings()[getHeadCount()] += getFitRatings()[individualIndex];
		} catch (MarkovException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private double doGrow(int individualIndex) throws MarkovException {
		MarkovNetwork mkn = getPopulation()[individualIndex].getMKN();
		double rating = 0;
		
		String[] attractors = new String[_scenarios.length];
		// Loop on each scenario for an individual.
		for (int j = 0; j < _scenarios.length; j++) {
			mkn.setInitialStates(_scenarios[j]);
			CircularArrayList<String> trace = new CircularArrayList<String>(getEAResult().getFuzzyLength());
			int counter = 0;
			while (true) {
				if (counter > 10) {
					break;
				}
				counter++;
				mkn.moveNext();
				attractors[j] = checkAttractor(trace, mkn.getFullGateStates());
				if (attractors[j] != null) {
					break;
				}
			}
		}
		
		rating = rateIndividual(attractors);
		
		// For expected attractors, we also do the test
		for (int j = 0; j < _expectedAttractors.length; j++) {
			boolean[] state = Utils.string2BooleanArray(_expectedAttractors[j]);
			mkn.setInitialStates(state);
			CircularArrayList<String> trace = new CircularArrayList<String>(getEAResult().getFuzzyLength());
			trace.add(_expectedAttractors[j]);
			for (int k = 1; k < getEAResult().getFuzzyLength(); k++) {
				mkn.moveNext();
				trace.add(mkn.getFullGateStates());
			}
			
			// expected attractors have same weight as scenarios
			rating += (_scenarios.length > 0 ? _scenarios.length : 1) * Utils.leastSimilarity(trace) / _expectedAttractors.length;
		}
		
		return rating;
	}

	private String checkAttractor(CircularArrayList<String> trace, String gateStates) {
		trace.add(gateStates);
		if (trace.size() < getEAResult().getFuzzyLength()) {
			return null;
		}
		
		for (int i = 1; i < trace.size(); i++) {
			if (Utils.similarity(trace.get(i), trace.get(0)) < getEAResult().getFuzzyAttractorRatio()) {
				return null;
			}
		}
		
		return trace.get(0);
	}

	private double rateIndividual(String[] attractors) {
		double rating = 0.0;
		
		for (String expectedAttractor : _expectedAttractors) {
			int bestFitIndex = -1;
			double bestFitRating = 0.0;
			for (int i = 0; i < attractors.length; i++) {
				if (attractors[i] == null) {
					continue;
				}
				
				double fitRating = Utils.similarity(attractors[i], expectedAttractor);
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
}

