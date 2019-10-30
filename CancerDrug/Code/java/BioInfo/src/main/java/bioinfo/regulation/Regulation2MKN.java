package bioinfo.regulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import bioinfo.markov.Serializer;
import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class Regulation2MKN {
	public static String Regulation2MarkovNetworkWithFixedAndTargetGenes(String pathGeneRegulation, String pathFixedGenes, String pathTargetGenes) {
		List<GeneRegulation> geneRegulations = null;
		List<FixedGene> fixedGenes = null;
		List<TargetGene> targetGenes = null;
		
		if (pathGeneRegulation == null) {
			return null;
		}
		geneRegulations = (new GeneRegulationLoader()).loadFromFile(pathGeneRegulation);
		if (geneRegulations == null || geneRegulations.size() == 0) {
			return null;
		}
		
		if (pathFixedGenes != null) {
			fixedGenes = (new FixedGenesLoader()).loadFromFile(pathFixedGenes);
			if (fixedGenes == null || fixedGenes.size() == 0) {
				return null;
			}
		}
		
		if (pathTargetGenes != null) {
			targetGenes = (new TargetGenesLoader()).loadFromFile(pathTargetGenes);
			if (targetGenes == null || targetGenes.size() == 0) {
				return null;
			}
		}
		
		if (fixedGenes != null && fixedGenes.size() > 0) {
			geneRegulations.removeIf(new RemoveBindingGenePredicator(fixedGenes));
			targetGenes.removeIf(new RemoveTargetGenePredicator(fixedGenes));
		}
		
		// Create ID dictionary
		GeneDictionary dict = createGeneIDDictionary(geneRegulations, fixedGenes, targetGenes);
		
		StringBuffer sb = new StringBuffer();
		sb.append(Serializer.nodeIDsHeader).append("\n");
		List<Integer> sortedList = new ArrayList<Integer>();
		sortedList.addAll(dict.gteAllIDs());
		Collections.sort(sortedList);
		for (Integer id : sortedList) {
			sb.append("ID=").append(id).append(";Name=").append(dict.getNameByID(id)).append("\n");
		}
		sb.append("\n");
		
		sb.append(Serializer.inputPointsHeader).append("\n");
		Map<Integer, String> inputPointStrings = new HashMap<Integer, String>();
		// input points
		if (targetGenes != null) {
			for (TargetGene tg : targetGenes) {
				for (String name : tg.getInputIDs()) {
					inputPointStrings.put(dict.getIDByName(name), "ID=" + Integer.toString(dict.getIDByName(name)) + ";State=0");
				}
			}
		}
		// Then fixed gene states
		if (fixedGenes != null) {
			for (FixedGene fg : fixedGenes) {
				inputPointStrings.put(dict.getIDByName(fg.getGeneName()), "ID=" + Integer.toString(dict.getIDByName(fg.getGeneName())) + ";State=" + (fg.getFixedState() ? "1" : "0"));
			}
		}
		
		sortedList.clear();
		sortedList.addAll(inputPointStrings.keySet());
		Collections.sort(sortedList);
		for (Integer id : sortedList) {
			sb.append(inputPointStrings.get(id)).append("\n");
		}
		sb.append("\n");
		
		sb.append(Serializer.gatesHeader).append("\n");
		Map<String, Map<String, RegulationQualityRelationPair>> mappedRegulation = GeneRegulationAnalysis.convertToMap(geneRegulations, GeneRegulationAnalysis.Filter_All);
		for (String bindingGene : mappedRegulation.keySet()) {
			sb.append("ID=").append(dict.getIDByName(bindingGene)).append(";InputNode=");
			Map<String, RegulationQualityRelationPair> node = mappedRegulation.get(bindingGene);
			boolean firstOne = true;
			for (String factorGene : node.keySet()) {
				if (firstOne) {
					firstOne = false;
				} else {
					sb.append(",");
				}
				sb.append(dict.getIDByName(factorGene.split(";")[1]));
			}
			
			if (targetGenes != null) {
				for (TargetGene tg : targetGenes) {
					if (tg.getGeneName().equals(bindingGene)) {
						for (String id : tg.getInputIDs()) {
							if (!firstOne) {
								sb.append(",");
								firstOne = false;
							}
							sb.append(dict.getIDByName(id));
						}
					}
				}
			}
			sb.append(";Relation=");
			
			firstOne = true;
			for (String factorGene : node.keySet()) {
				if (firstOne) {
					firstOne = false;
				} else {
					sb.append(",");
				}
				sb.append(node.get(factorGene).getRelation());
			}

			sb.append("\n");
		}
		sb.append("\n");
		
		sb.append(Serializer.outputGatesHeader).append("\n");
		Map<Integer, String> outputGeneStrings = new HashMap<Integer, String>();
		for (String bindingGene : mappedRegulation.keySet()) {
			outputGeneStrings.put(dict.getIDByName(bindingGene), "ID=" + Integer.toString(dict.getIDByName(bindingGene)));
		}
		sortedList.clear();
		sortedList.addAll(outputGeneStrings.keySet());
		Collections.sort(sortedList);
		for (Integer id : sortedList) {
			sb.append(outputGeneStrings.get(id)).append("\n");
		}
		sb.append("\n");
		
		return sb.toString();
	}
	public static String Regulation2MarkovNetwork(String pathGeneRegulation) {
		List<GeneRegulation> geneRegulations = null;
		
		if (pathGeneRegulation == null) {
			return null;
		}
		geneRegulations = (new GeneRegulationLoader()).loadFromFile(pathGeneRegulation);
		if (geneRegulations == null || geneRegulations.size() == 0) {
			return null;
		}
		
		// Create ID dictionary
		GeneDictionary dict = createGeneIDDictionary(geneRegulations, null, null);
		
		StringBuffer sb = new StringBuffer();
		sb.append(Serializer.nodeIDsHeader).append("\n");
		List<Integer> sortedList = new ArrayList<Integer>();
		sortedList.addAll(dict.gteAllIDs());
		Collections.sort(sortedList);
		for (Integer id : sortedList) {
			sb.append("ID=").append(id).append(";Name=").append(dict.getNameByID(id)).append("\n");
		}
		sb.append("\n");
		
		sb.append(Serializer.gatesHeader).append("\n");
		Map<String, Map<String, RegulationQualityRelationPair>> mappedRegulation = GeneRegulationAnalysis.convertToMap(geneRegulations, GeneRegulationAnalysis.Filter_All);
		for (int geneID : sortedList) {
			String geneName = dict.getNameByID(geneID);
			Map<String, RegulationQualityRelationPair> node = mappedRegulation.get(geneName);
			if (node == null) {
				sb.append("ID=").append(geneID).append("\n");
			} else {
				sb.append("ID=").append(geneID).append(";InputNode=");
				boolean firstOne = true;
				for (String factorGene : node.keySet()) {
					if (firstOne) {
						firstOne = false;
					} else {
						sb.append(",");
					}
					sb.append(dict.getIDByName(factorGene.split(";")[1]));
				}
				sb.append(";Relation=");
				
				firstOne = true;
				for (String factorGene : node.keySet()) {
					if (firstOne) {
						firstOne = false;
					} else {
						sb.append(",");
					}
					sb.append(node.get(factorGene).getRelation());
				}

				sb.append("\n");
			}
		}
		sb.append("\n");
		
		sb.append(Serializer.outputGatesHeader).append("\n");
		for (Integer id : sortedList) {
			sb.append("ID=").append(id).append("\n");
		}
		sb.append("\n");
		
		return sb.toString();
	}

	private static GeneDictionary createGeneIDDictionary(List<GeneRegulation> geneRegulations, List<FixedGene> fixedGenes, List<TargetGene> targetGenes) {
		List<String> ids = new ArrayList<String>();
		
		for (GeneRegulation gr : geneRegulations) {
			ids.add(gr.getFactorGene());
			ids.add(gr.getBindingGene());
		}
		
		if (fixedGenes != null) {
			for (FixedGene fg : fixedGenes) {
				ids.add(fg.getGeneName());
			}
		}
		
		if (targetGenes != null) {
			for (TargetGene tg : targetGenes) {
				ids.add(tg.getGeneName());
				ids.addAll(tg.getInputIDs());
			}
		}
		
		return new GeneDictionary(ids);
	}

	private static class RemoveBindingGenePredicator implements Predicate<GeneRegulation> {
		private List<FixedGene> _fixedGenes;
		public RemoveBindingGenePredicator(List<FixedGene> fixedGenes) {
			_fixedGenes = fixedGenes;
		}
		
		public boolean test(GeneRegulation t) {
			for (FixedGene fg : _fixedGenes) {
				if (fg.getGeneName().equals(t.getBindingGene())) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	private static class RemoveTargetGenePredicator implements Predicate<TargetGene> {
		private List<FixedGene> _fixedGenes;
		public RemoveTargetGenePredicator(List<FixedGene> fixedGenes) {
			_fixedGenes = fixedGenes;
		}

		public boolean test(TargetGene t) {
			for (FixedGene fg : _fixedGenes) {
				if (fg.getGeneName().equals(t.getGeneName())) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	public static void main(String[] args) {
		Settings.loadSettings(args[0]);
		
		String result = Regulation2MarkovNetwork(Settings.instance().getSetting("General", "RegulationFile"));
		Utils.saveToFile(Settings.instance().getSetting("General", "InitialMKNFile"), result);
	}
}

