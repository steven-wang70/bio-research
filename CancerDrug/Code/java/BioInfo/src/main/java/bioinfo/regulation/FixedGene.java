package bioinfo.regulation;

public class FixedGene {
	private String _specie;
	private String _geneName;
	private boolean _fixedState;
	
	public FixedGene(String specie, String geneName, boolean fixedState) {
		this._specie = specie;
		this._geneName = geneName;
		this._fixedState = fixedState;
	}
	
	public String getSpecie() {
		return _specie;
	}
	public String getGeneName() {
		return _geneName;
	}
	public boolean getFixedState() {
		return _fixedState;
	}
}
