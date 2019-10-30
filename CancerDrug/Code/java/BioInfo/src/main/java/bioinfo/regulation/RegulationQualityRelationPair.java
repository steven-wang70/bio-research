package bioinfo.regulation;

public class RegulationQualityRelationPair {
	private int _quality;
	private int _relation;
	
	public RegulationQualityRelationPair(int quality, int relation) {
		_quality = quality;
		_relation = relation;
	}
	public int getQuality() {
		return _quality;
	}
	public int getRelation() {
		return _relation;
	}
}
