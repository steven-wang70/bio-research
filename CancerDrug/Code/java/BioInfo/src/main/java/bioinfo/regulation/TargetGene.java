package bioinfo.regulation;

import java.util.ArrayList;
import java.util.List;

import bioinfo.markov.MarkovException;

public class TargetGene {
	private String _specie;
	private String _geneName;
	private int _bits;
	private String _inputName;

	public TargetGene(String specie, String geneName, int bits, String inputName) throws MarkovException {
		this._specie = specie;
		this._geneName = geneName;
		this._bits = bits;
		this._inputName = inputName;
		
		if (bits <= 0) {
			throw new MarkovException();
		}
	}
	
	public String getSpecie() {
		return _specie;
	}
	public String getGeneName() {
		return _geneName;
	}
	public int getBits() {
		return _bits;
	}
	public String getInputName() {
		return _inputName;
	}
	public List<String> getInputIDs() {
		List<String> theList = new ArrayList<String>();
		
		for (int i = 0; i < _bits; i++) {
			theList.add(_inputName + "__" + Integer.toString(i));
		}
		
		return theList;
	}
}
