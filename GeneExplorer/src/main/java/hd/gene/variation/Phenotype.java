package hd.gene.variation;

import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Phenotype implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int phenotypeId;
	private String name;
	private String description;
	private double overalPValue;
	
	private List<PhenotypeFeature> features;
	
	public int getPhenotypeId() {
		return phenotypeId;
	}
	public void setPhenotypeId(int phenotypeId) {
		this.phenotypeId = phenotypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PhenotypeFeature> getFeatures() {
		return features;
	}
	public void setFeatures(List<PhenotypeFeature> features) {
		this.features = features;
	}
	public double getOverallPValue() {
		return overalPValue;
	}
	public void setOverallPValue(double overalPValue) {
		this.overalPValue = overalPValue;
	}

	/**
	 * Calculate pValue of all features related to a Phenotype.
	 * 
	 * @param features
	 * @return
	 */
	public void calculateOverallPValue() {
		double result = 0;
		if (this.features != null){
			for (PhenotypeFeature f : features) {
				result += f.getpValue();
			}
		}

		overalPValue = result;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Id: ").append(phenotypeId).append("\n");
		sb.append("Name: ").append(name).append("\n");
		sb.append("Description: ").append(description).append("\n");
		sb.append("Overall pValue: ").append(overalPValue).append("\n");
		
		for (int i = 0; i < features.size(); i++){
			sb.append("\t").append(i+1).append(". ").append(features.get(i).toString()).append("\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Load phenotype name and description.
	 * @return
	 */
	private static HashMap<Integer, Phenotype> LoadPhenotypeNameDescriptions(){
		String resource = "hd/gene/variation/mybatis_config.xml";
		InputStreamReader reader = new InputStreamReader(Phenotype.class.getClassLoader().getResourceAsStream(resource));

		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sqlSessionFactory.openSession();
		PhenotypeNameDescriptionMapper mapper = session.getMapper(PhenotypeNameDescriptionMapper.class);
		List<Phenotype> phenotypes = mapper.retrievePhenotypeNameDescriptions();
			
		HashMap<Integer, Phenotype> result = new HashMap<Integer, Phenotype>();
		for (Phenotype i : phenotypes){
			result.put(i.getPhenotypeId(), i);
		}
		
		return result;
	}
	
	
	/**
	 * The constant phenotype data which is retreived from database, and then stored in local file.
	 */
	private static List<Phenotype> _phenotypes = null;
	private static final String phenotypeFile = "D:\\steven\\gitsrc\\hd\\GeneExplorer\\src\\main\\java\\hd\\gene\\variation\\phenotypes";
	
	/**
	 * Load global phenotype data from either local file or remote database.
	 * @return
	 */
	public static List<Phenotype> getPhenotypes(){
		if (_phenotypes == null){
			_phenotypes = (List<Phenotype>) Serialization.readFromFile(phenotypeFile);
			
			if (_phenotypes == null){
				_phenotypes = PhenotypeFeatureAttribute.loadPhenotypes();
				
				// Get all snp names
				if (_phenotypes != null){
					HashSet<String> snpNames = getSnpNames(_phenotypes);
					
					String[] snpNameArr = new String[snpNames.size()];
					String[] temp = snpNames.toArray(snpNameArr);
					
					HashMap<String, SnpGene> snpGenes = SnpGene.LoadSnpGenes(temp);
					HashMap<Integer, Phenotype> phenotypeNames = Phenotype.LoadPhenotypeNameDescriptions();
					
					for (Phenotype p : _phenotypes){
						Phenotype nameDesc = phenotypeNames.get(p.getPhenotypeId());
						p.setName(nameDesc.getName());
						p.setDescription(nameDesc.getDescription());
						for (PhenotypeFeature pf : p.getFeatures()){
							if (snpGenes.containsKey(pf.getSnpId())){
								pf.setGeneName(snpGenes.get(pf.getSnpId()).getGeneName());
							}
						}
					}
					
					Serialization.saveToFile(_phenotypes, phenotypeFile);
				}
			}
		}
		
		return _phenotypes;
	}
	
	/**
	 * Retrieve a set of SNP names from the given phenotype list.
	 * @param phenotypes
	 * @return
	 */
	public static HashSet<String> getSnpNames(List<Phenotype> phenotypes) {
		HashSet<String> snpNames = new HashSet<String>();
		for (Phenotype p : phenotypes){
			for (PhenotypeFeature pf : p.getFeatures()){
				snpNames.add(pf.getSnpId());
			}
		}
		
		return snpNames;
	}

	   
	/**
	 * According to given personal SNP data and clinical significances, filter the global phenotype data.
	 * @param snps
	 * @param keepCliValues
	 * @return
	 */
	public static List<Phenotype> filterPersonalPhenotypes(HashMap<String, PersonalSNP> snps, int keepCliValues) {
		List<Phenotype> phenotypes = Phenotype.getPhenotypes();
		List<Phenotype> filterList = new ArrayList<Phenotype>();

		for (Phenotype pt : phenotypes) {
			List<PhenotypeFeature> pfList = new ArrayList<PhenotypeFeature>();
			for (PhenotypeFeature f : pt.getFeatures()) {
				if ((ClinicalSignificance.realValue(f.getCliSigValues()) & keepCliValues) == 0) // We do not care these clinical significances
					continue;
				
				if (snps.containsKey(f.getSnpId())) {
					PersonalSNP snp = snps.get(f.getSnpId());
					if (snp.getGenotype().indexOf(f.getRiskAllele()) != -1) {
						PhenotypeFeature temp = new PhenotypeFeature(f);
						temp.setGenotype(snp.getGenotype()); // We do not keep genotype and chromosome information in the phenotype.
						temp.setChromosome(snp.getChromosome()); // so get them from the 23andme SNP data format.
						pfList.add(temp);
					}
				}
			}

			if (pfList.size() > 0) {
				PhenotypeFeature.sort(pfList);
				Phenotype personalPT = new Phenotype();
				personalPT.setPhenotypeId(pt.getPhenotypeId());
				personalPT.setName(pt.getName());
				personalPT.setDescription(pt.getDescription());
				personalPT.setFeatures(pfList);
				personalPT.calculateOverallPValue();
				filterList.add(personalPT);
			}
		}

		Phenotype.sort(filterList);
		
		return filterList;
	}
	

	/**
	 * Sort the phenotype list according to overall pValue, descending.
	 * @param phenotypes
	 */
	private static void sort(List<Phenotype> phenotypes){
		phenotypes.sort(new PhenotypePriorityComparer());
	}
	
	private static class PhenotypePriorityComparer implements Comparator<Phenotype> {
	
		@Override
		public int compare(Phenotype pt1, Phenotype pt2) {
			if (pt1.getOverallPValue() < pt2.getOverallPValue()){
				return 1;
			}
			else if (pt1.getOverallPValue() > pt2.getOverallPValue()){
				return -1;
			} else {
				return 0;
			}
		}
		
	}

}
