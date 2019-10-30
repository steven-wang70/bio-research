package bioinfo.ea;

import org.apache.commons.lang3.SerializationUtils;

import bioinfo.markov.Gate;
import bioinfo.markov.MarkovException;
import bioinfo.markov.MarkovNetwork;
import bioinfo.markov.Serializer;
import bioinfo.utils.Utils;

public abstract class EvolutionAlgorithm {
	private Individual[] _population;
	private double[] _fitRatings;
	private EAResult _eaResult;
	
	private MarkovNetwork _templateMKN;
	private int _individualDNALength = -1;
	
	protected EAResult getEAResult() {
		return _eaResult;
	}
	
	protected int getHeadCount() {
		return _eaResult.getHeadCount();
	}
	
	protected Individual[] getPopulation() {
		return _population;
	}
	
	protected double[] getFitRatings(){
		return _fitRatings;
	}
	
	public EvolutionAlgorithm(EAResult eaResult) throws EAException, MarkovException {
		_eaResult = eaResult;
		_population = new Individual[_eaResult.getHeadCount()];
		_fitRatings = new double[_eaResult.getHeadCount() + 1]; // The last item is the summary.
	}
	
	protected abstract MarkovNetwork templateMKN();
	protected abstract void grow() throws EAException, MarkovException;

	private int getIndividualDNALength()
	{
		if (_individualDNALength == -1) {
			_individualDNALength = 0;
			for (Gate g: _templateMKN.getGates()) {
				_individualDNALength += g.getTransitionLength();
			}
		}
		
		return _individualDNALength;
	}

	private void createPopulation() throws EAException, MarkovException {
		_templateMKN = templateMKN();
		// Then apply new DNA to the population;
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			_population[i] = new Individual(SerializationUtils.clone(_templateMKN));
		}
	}
	
	private void resetPopulation(boolean[][] dnas) throws EAException, MarkovException {
		// Then apply new DNA to the population;
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			_population[i].reset(dnas[i]);
		}
	}

	public boolean evolute() throws EAException, MarkovException {
		createPopulation();
		
		// First randomly initialize the population
		int dnaLength = getIndividualDNALength();
		// Create DNA sequences for the 1st generation
		boolean[][] newDNAs = new boolean[_eaResult.getHeadCount()][dnaLength];
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			for (int j = 0; j < dnaLength; j++) {
				newDNAs[i][j] = (Math.random() < 0.5);
			}
		}
		
		resetPopulation(newDNAs);

		int generationCount = 1;
		for (; ; generationCount++) {
			grow();
			
			if (voteResult() || generationCount == _eaResult.getMaxGeneration()) {
				break;
			}
			System.out.println("Generation: " + Integer.toString(generationCount) + "; Consistency: " + Double.toString(_eaResult.getAverageBitVoteConsistencyRatio()));
			
			normalizeRatings();
			hybrid();
		}
		
		_population[0].reset(Utils.string2BooleanArray(_eaResult.getLogic()));
		_eaResult.setResult(Serializer.serialize(_population[0].getMKN()), generationCount, _eaResult.getAverageBitVoteConsistencyRatio());
		_eaResult.setResultSimilarity(_fitRatings[this.getHeadCount()] / this.getHeadCount());
		
		return _eaResult.isSuccess();
	}
	
	private void normalizeRatings() {
		double minValue = _fitRatings[_eaResult.getHeadCount()];
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			if (minValue > _fitRatings[i]) {
				minValue = _fitRatings[i];
			}
		}
		
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			_fitRatings[i] -= minValue;
		}

		_fitRatings[_eaResult.getHeadCount()] -= minValue * _eaResult.getHeadCount();
	}
	
	private void hybrid() throws EAException, MarkovException {
		int dnaLength = getIndividualDNALength();

		// First generate new DNA upon old DNA
		boolean[][] newDNAs = new boolean[_eaResult.getHeadCount()][dnaLength];
		int firstHalfLength = dnaLength / 2;
		int secondHalfLength = dnaLength - firstHalfLength;
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			int index1 = roulette();
			int index2 = roulette(index1);
			System.arraycopy(_population[index1].getDNA(), 0, newDNAs[i], 0, firstHalfLength);
			System.arraycopy(_population[index2].getDNA(), firstHalfLength, newDNAs[i], firstHalfLength, secondHalfLength);
		}
		
		// Then do mutation on new DNAs
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			for (int j = 0; j < dnaLength; j++) {
				if (Math.random() < _eaResult.getMutationRate()) {
					newDNAs[i][j] = !newDNAs[i][j];
				}
			}
		}
		
		resetPopulation(newDNAs);
	}
	
	private int roulette() {
		double randNumber = Math.random() * _fitRatings[_eaResult.getHeadCount()];
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			randNumber -= _fitRatings[i];
			if (randNumber < 0) {
				return i;
			}
		}
		
		return _eaResult.getHeadCount() - 1;
	}
	
	private int roulette(int excludeNumber) {
		double randNumber = Math.random() * (_fitRatings[_eaResult.getHeadCount()] - _fitRatings[0]);
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			if (i == excludeNumber) {
				continue;
			}
			
			randNumber -= _fitRatings[i];
			if (randNumber < 0) {
				return i;
			}
		}
		
		if (excludeNumber == _eaResult.getHeadCount() - 1) {
			return _eaResult.getHeadCount() - 2;
		} else {
			return _eaResult.getHeadCount() - 1;
		}
	}
	
	private boolean voteResult() {
		int dnaLength = _population[0].getDNA().length;
		int[] countOfTrue = new int[dnaLength];
		
		// Count number of the true value in each bit of DNA from individuals
		for (int i = 0; i < _eaResult.getHeadCount(); i++) {
			boolean[] ab = _population[i].getDNA();
			for (int j = 0; j < dnaLength; j++) {
				if (ab[j]) {
					countOfTrue[j]++;
				}
			}
		}
		
		// Count how many bits passed vote according to given threshold
		double halfCount = _eaResult.getHeadCount() / 2.0; 
		boolean[] voteResult = new boolean[dnaLength];
		int normCount = 0;
		for (int i = 0; i < dnaLength; i++) {
			if (countOfTrue[i] >= halfCount) { // This is true
				voteResult[i] = true;
				normCount += countOfTrue[i];
			} else { // This is false
				voteResult[i] = false;
				normCount += _eaResult.getHeadCount() - countOfTrue[i];
			}
		}

		double averageBitVoteConsistencyRatio = normCount * 1.0 / (dnaLength * _eaResult.getHeadCount());
		_eaResult.setResult(Utils.booleanArray2String(voteResult), 0, averageBitVoteConsistencyRatio);
		
		return _eaResult.isSuccess();
	}
}
