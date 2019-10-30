package bioinfo.ea;

import bioinfo.markov.Gate;
import bioinfo.markov.MarkovException;
import bioinfo.markov.MarkovNetwork;

public class Individual {
	private boolean[] _dna;
	private MarkovNetwork _mkn;
	
	public Individual(MarkovNetwork mkn) throws MarkovException, EAException {
		_mkn = mkn;
	}
	
	public void reset(boolean[] dna) throws EAException, MarkovException {
		_dna = dna;
		_mkn.resetStates();
		
		int dnaIndex = 0;
		for (int i = 0; i < _mkn.getGates().size(); i++ ) {
			Gate g = _mkn.getGates().get(i);
			int length = g.getTransitionLength();
			
			boolean[] transitionVector = new boolean[length];
			System.arraycopy(_dna,  dnaIndex, transitionVector, 0, length);
			g.setTransitionVector(transitionVector);
			
			dnaIndex += length;
		}
	}
	
	public MarkovNetwork getMKN() {
		return _mkn;
	}
	
	public boolean[] getDNA() {
		return _dna;
	}
}
