package hd.gene.variation;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class PhenotypeFeature implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int featureId;
	private String snpId;
	private String riskAllele;
	private double pValue;
	private int cliSigValues = ClinicalSignificance.None;
	private String genotype;
	private String chromosome;
	private String geneName;
	
	public PhenotypeFeature(int featureId, String snpId,
			String riskAllele) {
		super();
		this.featureId = featureId;
		this.snpId = snpId;
		this.riskAllele = riskAllele;
	}

	public PhenotypeFeature(PhenotypeFeature rhs){
		super();
		this.featureId = rhs.getFeatureId();
		this.snpId = rhs.getSnpId();
		this.riskAllele = rhs.getRiskAllele();
		this.pValue = rhs.getpValue();
		this.cliSigValues = rhs.getCliSigValues();
		this.genotype = rhs.getGenotype();
		this.chromosome = rhs.getChromosome();
		this.geneName = rhs.getGeneName();
	}
	
	public double getpValue() {
		return pValue;
	}

	public void setpValue(double pValue) {
		this.pValue = pValue;
	}

	public int getFeatureId() {
		return featureId;
	}

	public String getSnpId() {
		return snpId;
	}

	public String getRiskAllele() {
		return riskAllele;
	}

	public int getCliSigValues() {
		return cliSigValues;
	}

	public void setCliSigValues(int cliSigValues) {
		this.cliSigValues = cliSigValues;
	}

	public String getGenotype() {
		return genotype;
	}

	public void setGenotype(String genotype) {
		this.genotype = genotype;
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public String getGeneName() {
		return geneName;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	public String toString(){
		return String.format("ID: %5$d SNP: %1$s/%2$s Genotype: %6$s pValue: %3$g CliSigs: %4$s CH:%7$s Gene:%8$s", 
				snpId, riskAllele, pValue, ClinicalSignificance.toString(cliSigValues), featureId, genotype, chromosome, geneName);
	}
	   
	/**
	 * If two features share the same snp and risk allele, merge them.
	 * @param features
	 */
	private static void mergeDuplicateFeatures(List<PhenotypeFeature> features){
		if (features.size() > 1){
			String[] keys = new String[features.size()];
			keys[0] = features.get(0).getSnpId() + "/" + features.get(0).getRiskAllele();
			for (int i = 1; i < features.size(); i++){
				String key = features.get(i).getSnpId() + "/" + features.get(i).getRiskAllele();
				boolean merged = false;
				for (int j = 0; j < i; j++){
					if (key.equals(keys[j])){
						merge2Features(features.get(j), features.get(i));
						features.set(i,  null);
						keys[i] = null;
						merged = true;
						break;
					}
				}
				
				if (!merged){
					keys[i] = key;
				}
			}
		}
	}
	
	/**
	 * merge content of the fromFeature to the toFeature.
	 * @param left
	 * @param right
	 */
	private static void merge2Features(PhenotypeFeature toFeature, PhenotypeFeature fromFeature){
		if (toFeature.getpValue() > 0 && fromFeature.getpValue() > 0){
			toFeature.setpValue(Math.sqrt(toFeature.getpValue() * fromFeature.getpValue()));
		} else {
			toFeature.setpValue(Math.max(toFeature.getpValue(), fromFeature.getpValue()));
		}
		
		toFeature.setCliSigValues(toFeature.getCliSigValues() | fromFeature.getCliSigValues());
	}
	
	/**
	 * Remove null feature and those feature with no clinical significance. First try to get clinical significances from snp data
	 * @param features
	 */
	public static void finalizeFeatures(List<PhenotypeFeature> features){
		mergeDuplicateFeatures(features);
		features.removeIf(new PhenotypeFeatureFilter(ClinicalSignificance.AllValues));
	}

	private static class PhenotypeFeatureFilter implements Predicate<PhenotypeFeature>{
		public PhenotypeFeatureFilter(int keepCliValues){
			this._keepCliValues = keepCliValues;
		}
		private int _keepCliValues = ClinicalSignificance.None;
		
		public boolean test(PhenotypeFeature var){
			if (var == null){
				return true;
			}
			
			if (var.getCliSigValues() == ClinicalSignificance.None){
				// Try to get this information from SNP data
				if (SnpClinicalSignificance.getSnpCliSigValues().containsKey(var.getSnpId())){
					var.setCliSigValues(SnpClinicalSignificance.getSnpCliSigValues().get(var.getSnpId()).getCliSigValues());
				}
			}
			
			if ((ClinicalSignificance.realValue(var.getCliSigValues()) & this._keepCliValues) != 0){
				return false;
			}
			
			return true;
		}
	}

	/**
	 * Sort features according to chromosome and gene names, ascending
	 * @param features
	 */
	public static void sort(List<PhenotypeFeature> features){
		features.sort(new PhenotypeFeatureComparer());
	}
	
	private static class PhenotypeFeatureComparer implements Comparator<PhenotypeFeature> {
	
		@Override
		public int compare(PhenotypeFeature f1, PhenotypeFeature f2) {
			int idx1 = getChIndex(f1.getChromosome());
			int idx2 = getChIndex(f2.getChromosome());
			if (idx1 > idx2){
				return 1;
			} else if (idx1 < idx2){
				return -1;
			} else {
				if (f1.getGeneName() == null && f2.getGeneName() != null){
					return 1;
				} else if (f2.getGeneName() == null && f1.getGeneName() != null){
					return -1;
				} else if (f1.getGeneName() == null && f2.getGeneName() == null){
					return 0;
				}

				return f1.getGeneName().compareTo(f2.getGeneName());
			}
		}
		
		/**
		 * Convert chromosome names to numbers so it could be sorted.
		 * @param s
		 * @return
		 */
		public static int getChIndex(String s) {
		    try { 
		        return Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		    	if (s.equals("X")){
		    		return 23;
		    	} else if (s.equals("Y")){
		    		return 24;
		    	} else if (s.equals("MT")){
		    		return 25;
		    	}
		        return 26; 
		    } catch(NullPointerException e) {
		        return 27;
		    }
		}
	}
	
}
