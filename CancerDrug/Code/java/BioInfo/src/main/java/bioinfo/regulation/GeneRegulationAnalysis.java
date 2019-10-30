package bioinfo.regulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GeneRegulationAnalysis {
	public static final int Filter_HumanOnly = 1;
	public static final int Filter_HumanPlus = 2;
	public static final int Filter_All = 3;
	
	public static List<GeneRegulation> filter(List<GeneRegulation> regulations, int inclusive, int leastQuality) {
		return normalize(regulations, inclusive, leastQuality);
	}
	
	public static List<GeneRegulation> filterByIDs(List<GeneRegulation> regulations, List<String> ids) {
		regulations.removeIf(new RemoveUnknownGenePredicator(ids));
		
		return regulations;
	}

	private static class RemoveUnknownGenePredicator implements Predicate<GeneRegulation> {
		private List<String> _knownIDs;
		public RemoveUnknownGenePredicator(List<String> knownIDs) {
			_knownIDs = knownIDs;
		}
		
		public boolean test(GeneRegulation t) {
			if (!_knownIDs.contains(t.getFactorGene()) || !_knownIDs.contains(t.getBindingGene())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static List<GeneRegulation> normalize(List<GeneRegulation> grs, int inclusive, int leastQuality) {
		// First remove redundant data
		Map<String, Map<String, RegulationQualityRelationPair>> mapped = convertToMap(grs, inclusive);
		
		List<GeneRegulation> newGrs = new ArrayList<GeneRegulation>();
		for (String bindingGene : mapped.keySet()) {
			Map<String, RegulationQualityRelationPair> factorQualities = mapped.get(bindingGene);
			
			for (String factorAndSpecie : factorQualities.keySet()) {
				if (factorQualities.get(factorAndSpecie).getQuality() <= leastQuality) {
					String[] temp = factorAndSpecie.split(";");
					newGrs.add(new GeneRegulation(temp[0], temp[1], bindingGene, factorQualities.get(factorAndSpecie).getQuality(), factorQualities.get(factorAndSpecie).getRelation()));
				}
			}
		}

		// Then sort the data
		mapped = convertToMap(newGrs, Filter_All);
		int RemainCount = newGrs.size();
		
		newGrs = new ArrayList<GeneRegulation>();
		for (int regulatorNumber = 1; ; regulatorNumber++) {
			List<String> keysToBeRemoved = new ArrayList<String>();
			List<String> targetKeys = new ArrayList<String>();
			targetKeys.addAll(mapped.keySet());
			Collections.sort(targetKeys);
			for (String bindingGene : targetKeys) {
				Map<String, RegulationQualityRelationPair> factorQualities = mapped.get(bindingGene);
				if (factorQualities.size() == regulatorNumber) {
					List<String> factorKeys = new ArrayList<String>();
					factorKeys.addAll(factorQualities.keySet());
					Collections.sort(factorKeys);
					for (String factorAndSpecie : factorKeys) {
						String[] temp = factorAndSpecie.split(";");
						newGrs.add(new GeneRegulation(temp[0], temp[1], bindingGene, factorQualities.get(factorAndSpecie).getQuality(), factorQualities.get(factorAndSpecie).getRelation()));
						RemainCount--;
					}
					keysToBeRemoved.add(bindingGene);
				}
			}
			
			if (RemainCount == 0) {
				break;
			}
			
			if (keysToBeRemoved.size() > 0) {
				for (String k : keysToBeRemoved) {
					mapped.remove(k);
				}
			}
		}
		
		return newGrs;
	}

	static Map<String, Map<String, RegulationQualityRelationPair>> convertToMap(List<GeneRegulation> grs, int inclusive) {
		Map<String, Map<String, RegulationQualityRelationPair>> mapped = new HashMap<String, Map<String, RegulationQualityRelationPair>>();
		
		for (GeneRegulation gr : grs) {
			if (inclusive == Filter_HumanOnly && !gr.getSpecie().equals("HS")) {
				continue;
			}
			
			if (!mapped.containsKey(gr.getBindingGene())) {
				mapped.put(gr.getBindingGene(), new HashMap<String, RegulationQualityRelationPair>());
			}
			
			Map<String, RegulationQualityRelationPair> factorQualities = mapped.get(gr.getBindingGene());
			
			String key = gr.getSpecie() + ";" + gr.getFactorGene();
			if (factorQualities.containsKey(key)) {
				if (factorQualities.get(key).getQuality() > gr.getQuality()) {
					factorQualities.put(key, new RegulationQualityRelationPair(gr.getQuality(), gr.getRelation()));
				}
			} else {
				if (inclusive == Filter_All || inclusive == Filter_HumanOnly) {
					factorQualities.put(key, new RegulationQualityRelationPair(gr.getQuality(), gr.getRelation()));
				} else {
					boolean found = false;
					String replacedKey = null;
					for (String key2 : factorQualities.keySet()) {
						String[] temp = key2.split(";");
						if (!temp[1].equals(gr.getFactorGene())) {
							continue; // Not the same factor at all.
						}
						
						found = true;
						if (gr.getSpecie().equals("HS")) {
							replacedKey = key2;
						} else if (!temp[0].equals("HS")) {
							if (factorQualities.get(key2).getQuality() > gr.getQuality()) {
								replacedKey = key2;
							}
						}
						break;
					}
					
					if (found) {
						if (replacedKey != null) {
							factorQualities.remove(replacedKey);
							factorQualities.put(key, new RegulationQualityRelationPair(gr.getQuality(), gr.getRelation()));
						}
					} else {
						factorQualities.put(key, new RegulationQualityRelationPair(gr.getQuality(), gr.getRelation()));
					}
				}
			}
		}
		
		return mapped;
	}
	
	public static HashSet<String> stat(List<GeneRegulation> grs) {
		HashSet<String> genes = new HashSet<String>();
		for (GeneRegulation gr : grs) {
			genes.add(gr.getFactorGene());
			genes.add(gr.getBindingGene());
		}
		
		return genes;
	}
	
	private static List<List<GeneRegulation>> cluster(List<GeneRegulation> grs) {
		List<List<GeneRegulation>> clusters = new ArrayList<List<GeneRegulation>>();
		
		while (grs.size() > 0) {
			HashSet<String> keys = new HashSet<String>();
			List<GeneRegulation> foundList = new ArrayList<GeneRegulation>();
			
			// Move the first one to the found list
			keys.add(grs.get(0).getFactorGene());
			keys.add(grs.get(0).getBindingGene());
			
			foundList.add(grs.get(0));
			grs.remove(0);
			
			int foundCount = 1;
			while (foundCount > 0) {
				foundCount = 0; // Reset it
				List<GeneRegulation> temp = new ArrayList<GeneRegulation>();
				for (GeneRegulation gr : grs) {
					if (keys.contains(gr.getFactorGene()) || keys.contains(gr.getBindingGene())) {
						foundCount++;
						keys.add(gr.getFactorGene());
						keys.add(gr.getBindingGene());
						
						temp.add(gr);
					}
				}
				
				if (foundCount > 0) {
					foundList.addAll(temp);
					grs.removeAll(temp);
				}
			}
			
			clusters.add(foundList);
			foundList = null;
		}
		
		return clusters;
	}
	
	private static Map<String, SymbiosisGenes> cluster2(List<GeneRegulation> grs) {
		Map<String, SymbiosisGenes> twins = new HashMap<String, SymbiosisGenes>();
		Map<String, Map<String, RegulationQualityRelationPair>> map = convertToMap(grs, GeneRegulation.Q6_Unknown);
		
		// First sysbiosis count
		for (Map<String, RegulationQualityRelationPair> factors : map.values()) {
			if (factors.size() > 1) {
				List<String> keys = new ArrayList<String>();
				keys.addAll(factors.keySet());
				for (int i = 0; i < keys.size() - 1; i++) {
					for (int j = i + 1; j < keys.size(); j++) {
						String twinKey = null;
						if (keys.get(i).compareTo(keys.get(j)) < 0) {
							twinKey = keys.get(i) + "/" + keys.get(j);
						} else {
							twinKey = keys.get(j) + "/" + keys.get(i);
						}
						
						if (!twins.containsKey(twinKey)) {
							twins.put(twinKey, new SymbiosisGenes(twinKey));
						}
						
						twins.get(twinKey).increaseSymbiosis();
					}
				}
			}
		}
		
		// Then nonsysbiosis count
		for (SymbiosisGenes sgs : twins.values()) {
			for (Map<String, RegulationQualityRelationPair> factors : map.values()) {
				if (factors.containsKey(sgs.getGene1()) && !factors.containsKey(sgs.getGene2()) || 
					factors.containsKey(sgs.getGene2()) && !factors.containsKey(sgs.getGene1())) {
					sgs.increaseNonSymbiosis();
				}
			}
		}
		
		return twins;
	}

	/**
	 * @param args
	 * Test the loader
	 */
	public static void main(String[] args) {
		GeneRegulationLoader regLoader = new GeneRegulationLoader();
		List<GeneRegulation> regulations = regLoader.loadFromFile("D:\\steven\\gitsrc\\CancerDrug\\Data\\transfac\\geneFeatureSelect.csv");
		
		for (GeneRegulation gr : regulations) {
			System.out.println(gr.toString());
		}
	}
}


class SymbiosisGenes {
	private String _twinGenes;
	private String _gene1, _gene2;
	private int _symbiosisCount = 0;
	private int _nonSymbiosisCount = 0;
	
	public SymbiosisGenes(String twinGenes) {
		_twinGenes = twinGenes;
		String[] temp = _twinGenes.split("/");
		_gene1 = temp[0];
		_gene2 = temp[1];
		
	}
	
	public String getTwinGenes() {
		return _twinGenes;
	}
	
	public String getGene1() {
		return _gene1;
	}
	
	public String getGene2() {
		return _gene2;
	}
	
	public int getSymbiosisCount() {
		return _symbiosisCount;
	}
	public int getNonSymbiosisCount() {
		return _nonSymbiosisCount;
	}
	
	public void increaseSymbiosis() {
		_symbiosisCount++;
	}
	
	public void increaseNonSymbiosis() {
		_nonSymbiosisCount++;
	}
}

