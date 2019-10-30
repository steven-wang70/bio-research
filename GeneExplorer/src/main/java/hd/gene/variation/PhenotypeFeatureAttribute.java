
package hd.gene.variation;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author steve_000
 *
 */
public class PhenotypeFeatureAttribute {
	private int phenotypeId;
	private int featureId;
	private String snpId;
	private int attributeType;
	private String attributeValue;
	
	public int getPhenotypeId() {
		return phenotypeId;
	}
	public void setPhenotypeId(int phenotypeId) {
		this.phenotypeId = phenotypeId;
	}
	public int getFeatureId() {
		return featureId;
	}
	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}
	public String getSnpId() {
		return snpId;
	}
	public void setSnpId(String snpId) {
		this.snpId = snpId;
	}
	public int getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(int attributeType) {
		this.attributeType = attributeType;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	public static List<Phenotype> loadPhenotypes(){
		List<PhenotypeFeatureAttribute> attribs = LoadFeatureAttributes();
		return processPFAttributes(attribs);
	}
	
	/**
	 * Load phenotype, features, and their attributes from database.
	 * @return
	 */
	private static List<PhenotypeFeatureAttribute> LoadFeatureAttributes(){
		int riskAlleleId = PhenotypeFeatureAttributeType.getRiskAlleleId();
		int pValueId = PhenotypeFeatureAttributeType.getPValueId();
		int clinvarClinSigId = PhenotypeFeatureAttributeType.getClinvarClinSigId();
		
		String resource = "hd/gene/variation/mybatis_config.xml";
		InputStreamReader reader = new InputStreamReader(PhenotypeFeatureAttribute.class.getClassLoader().getResourceAsStream(resource));

		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sqlSessionFactory.openSession();
		PhenotypeFeatureAttributeMapper mapper = session.getMapper(PhenotypeFeatureAttributeMapper.class);
		List<PhenotypeFeatureAttribute> attributes = mapper.retrieveAttributes(riskAlleleId, pValueId, clinvarClinSigId);
		
		return attributes;
	}
	
	/**
	 * Process pheontypes
	 * @param attribs
	 * @return
	 */
	private static List<Phenotype> processPFAttributes(List<PhenotypeFeatureAttribute> attribs){
		List<Phenotype> phenotypes = new ArrayList<Phenotype>();
		int phenotypeId = -1;
		List<PhenotypeFeatureAttribute> temp = new ArrayList<PhenotypeFeatureAttribute>();
		for (PhenotypeFeatureAttribute attrib : attribs){
			if (attrib.getPhenotypeId() != phenotypeId){
				processSinglePhenotype(phenotypes, temp);
				temp.clear();
				phenotypeId = attrib.getPhenotypeId();
			}
			
			temp.add(attrib);
		}
		
		// Process the last one
		processSinglePhenotype(phenotypes, temp);
		
		return phenotypes;
	}
	
	
	/**
	 * Process a single phenotype
	 * @param phenotypes
	 * @param attribs
	 */
	private static void processSinglePhenotype(List<Phenotype> phenotypes, List<PhenotypeFeatureAttribute> attribs){
		if (attribs.size() > 0){
			List<PhenotypeFeature> features = processFeatures(attribs);
			if (features.size() > 0){
				Phenotype p = new Phenotype();
				p.setPhenotypeId(attribs.get(0).getPhenotypeId());
				p.setFeatures(features);
				phenotypes.add(p);
			}
		}
	}
	
	/**
	 * Process features of a single phenotype.
	 * @param attribs
	 * @return
	 */
	private static List<PhenotypeFeature> processFeatures(List<PhenotypeFeatureAttribute> attribs){
		List<PhenotypeFeature> features = new ArrayList<PhenotypeFeature>();
		
		HashMap<Integer, PhenotypeFeatureAttribute> temp = new HashMap<Integer, PhenotypeFeatureAttribute>();
		int featureId = -1;
		for (PhenotypeFeatureAttribute attrib : attribs){
			if (attrib.getFeatureId() != featureId){
				processSingleFeature(features, temp);
				temp.clear();
				featureId = attrib.getFeatureId();
			}
			
			temp.put(attrib.getAttributeType(), attrib);
		}
		
		processSingleFeature(features, temp);
		
		PhenotypeFeature.finalizeFeatures(features);
		
		return features;
	}
	
	/**
	 * Filter out those features with risk allele, and together with pValue, clinical significance
	 * @param features
	 * @param attribs
	 */
	private static void processSingleFeature(List<PhenotypeFeature> features, HashMap<Integer, PhenotypeFeatureAttribute> attribs){
		if (attribs.size() == 0){
			return;
		}
		
		if (attribs.containsKey(PhenotypeFeatureAttributeType.getRiskAlleleId())){
			PhenotypeFeature feature = new PhenotypeFeature(attribs.get(PhenotypeFeatureAttributeType.getRiskAlleleId()).getFeatureId(), 
															attribs.get(PhenotypeFeatureAttributeType.getRiskAlleleId()).getSnpId(), 
															attribs.get(PhenotypeFeatureAttributeType.getRiskAlleleId()).getAttributeValue());
			
			if (attribs.containsKey(PhenotypeFeatureAttributeType.getPValueId())){
				feature.setpValue(Double.parseDouble(attribs.get(PhenotypeFeatureAttributeType.getPValueId()).getAttributeValue()));
			}
			
			if (attribs.containsKey(PhenotypeFeatureAttributeType.getClinvarClinSigId())){
				feature.setCliSigValues(ClinicalSignificance.parseSignificance(attribs.get(PhenotypeFeatureAttributeType.getClinvarClinSigId()).getAttributeValue()));
			}
			
			features.add(feature);
		}		
	}
}
