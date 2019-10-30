package bioinfo.regulation;

public class GeneRegulation {
	private String _specie;
	private String _factorGene;
	private String _bindingGene;
	private int _quality;
	private int _relation;
	/*
	1 Functionally confirmed transcription factor binding site
	2 Binding of pure protein (purified or recombinant)
	3 Immunologically characterized binding activity of a cellular extract
	4 Binding activity characterized via a known binding sequence
	5 Binding of uncharacterized extract protein to a bona fide element
	6 No quality assigned
	*/
	public static final int Q1_FunctionallyConfirmed = 1;
	public static final int Q2_PureProtein = 2;
	public static final int Q3_ImmunologicallyCharacterized = 3;
	public static final int Q4_CharacterizedBYBindingSequence = 4;
	public static final int Q5_BindingUncharacterizedProtein = 5;
	public static final int Q6_Unknown = 6;
	
	public static final int RelationUnknown = -1;
	public static final int RelationDephosphorylation = 0;
	public static final int RelationCompound = 1;
	public static final int RelationIndirectEffect = 2;
	public static final int RelationActivation = 3;
	public static final int RelationDissociation = 4;
	public static final int RelationBindingAssociation = 5;
	public static final int RelationInhibition = 6;
	public static final int RelationExpression = 7;
	public static final int RelationPhosphorylation = 8;
	public static final int RelationUbiquitination = 9;
	
	public GeneRegulation(String specie, String factorGene, String bindingGene, int quality) {
		super();
		this._specie = specie;
		this._factorGene = factorGene;
		this._bindingGene = bindingGene;
		this._quality = quality;
		this._relation = RelationUnknown;
	}
	
	public GeneRegulation(String specie, String factorGene, String bindingGene, int quality, int relation) {
		super();
		this._specie = specie;
		this._factorGene = factorGene;
		this._bindingGene = bindingGene;
		this._quality = quality;
		this._relation = relation;
	}
	
	public String getSpecie() {
		return _specie;
	}
	public String getFactorGene() {
		return _factorGene;
	}
	public String getBindingGene() {
		return _bindingGene;
	}
	public int getQuality() {
		return _quality;
	}
	public int getRelation() {
		return _relation;
	}
	@Override
	public String toString() {
		return _specie + ";" + _factorGene + ";" + _bindingGene + ";" + Integer.toString(_quality) + ";" + Integer.toString(_relation);
	}
}
