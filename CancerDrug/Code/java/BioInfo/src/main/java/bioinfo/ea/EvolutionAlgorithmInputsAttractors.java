package bioinfo.ea;

import bioinfo.markov.MarkovException;

public abstract class EvolutionAlgorithmInputsAttractors extends EvolutionAlgorithmAttractors {
	private double _lengthWeight;
	public EvolutionAlgorithmInputsAttractors(EAResult eaResult, boolean[][] scenarios, String[][] expectedAttractors, double lengthWeight) throws EAException, MarkovException {
		super(eaResult, scenarios, expectedAttractors);
		_lengthWeight = lengthWeight;
	}

	@Override
	protected double rateIndividual(String[][] attractors) {
		double rating = 0.0;
		
		for (int i = 0; i < attractors.length; i++) {
			if (attractors[i].length == _expectedAttractors[i].length) {
				rating += _lengthWeight;
				int bitCount = attractors[i][0].length() * attractors[i].length;
				double fitRating = calculateFitRating(attractors[i], _expectedAttractors[i]);
				
				rating += (fitRating * 1.0) / bitCount;
			}
		}
		
		return rating;
	}
	
}
