package bioinfo.transfac.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import bioinfo.regulation.GeneDictionary;
import bioinfo.regulation.GeneIDLoader;
import bioinfo.regulation.GeneRegulation;
import bioinfo.regulation.GeneRegulationAnalysis;
import bioinfo.transfac.BindingSite;
import bioinfo.transfac.TransFacData;
import bioinfo.transfac.TransFacGeneID;

public class GeneRegulationGenerator {
	private List<GeneRegulation> _geneRegulations = new ArrayList<GeneRegulation>();
	
	private List<TransFacData> _transFacDatas;
	private Map<String, TransFacGeneID> _transGeneIDs = new HashMap<String, TransFacGeneID>();
	private HashSet<String> _humanGeneIDs = new HashSet<String>();
	
	public GeneRegulationGenerator(List<TransFacData> transFacDatas, List<TransFacGeneID> transGeneIDs, GeneDictionary humandGeneIDs) {
		_transFacDatas = transFacDatas;
		
		for (TransFacGeneID id : transGeneIDs) {
			_transGeneIDs.put(id.getTransFacID(), id);
		}
		
		for (String id : humandGeneIDs.gteAllNames()) {
			_humanGeneIDs.add(id);
		}
	}
	
	private List<GeneRegulation> generate() {
		for (TransFacData tfd : _transFacDatas) {
			String regulatorGeneID = null;
			if (_humanGeneIDs.contains(tfd.getGeneId())) {
				regulatorGeneID = tfd.getGeneId();
			} else {
				if (tfd.getAliases() != null) {
					regulatorGeneID = uniqueHumanGeneID(tfd.getAliases());
				}
			}
			
			if (regulatorGeneID == null) {
				continue;
			}
			
			for (BindingSite bs : tfd.getBindingSites()) {
				String specie = null;
				String RegulateeGeneID = null;
				
				String defaultGeneID = bs.getBindingGendId();
				int index = defaultGeneID.indexOf('$');
				if (index >= 0) {
					specie = defaultGeneID.substring(0, index);
					defaultGeneID = defaultGeneID.substring(index + 1);
				}
				
				if (_transGeneIDs.containsKey(bs.getTransFacId())) {
					RegulateeGeneID = uniqueHumanGeneID(_transGeneIDs.get(bs.getTransFacId()).getAliases());
				}
				
				if (RegulateeGeneID == null) {
					RegulateeGeneID = uniqueHumanGeneID(defaultGeneID);
				}
				
				if (RegulateeGeneID != null) {
					_geneRegulations.add(new GeneRegulation(specie, regulatorGeneID, RegulateeGeneID, bs.getQuality()));
				}
			}
		}
		
		_geneRegulations = GeneRegulationAnalysis.normalize(_geneRegulations, GeneRegulationAnalysis.Filter_All, GeneRegulation.Q6_Unknown);
		
		return _geneRegulations;
	}
	
	private String uniqueHumanGeneID(String geneID) {
		if (_humanGeneIDs.contains(geneID)) {
			return geneID;
		}
		else {
			return null;
		}
	}
	
	private String uniqueHumanGeneID(List<String> geneIDs) {
		for (String id : geneIDs) {
			if (_humanGeneIDs.contains(id)) {
				return id;
			}
		}
		
		return null;
	}

	public static List<GeneRegulation> generateMapping(String transPath, String geneIDPath, String humanGeneIDPath) {
		TransFacDataLoader transFacDataLoader = new TransFacDataLoader();
		TransFacGeneIDLoader transFacGeneIDLoader = new TransFacGeneIDLoader();
		GeneIDLoader humanGeneIDLoader = new GeneIDLoader();
		
		List<TransFacData> transFacDatas = transFacDataLoader.loadFromFile(transPath);
		if (transFacDatas == null || transFacDatas.size() == 0) {
			System.err.println("Cannot load TransFac data.");
			return null;
		}
		
		List<TransFacGeneID> transGeneIDs = transFacGeneIDLoader.loadFromFile(geneIDPath);
		if (transGeneIDs == null || transGeneIDs.size() == 0) {
			System.err.println("Cannot load TransFac Gene IDs.");
			return null;
		}
		
		GeneDictionary humanGeneDictionary = new GeneDictionary(humanGeneIDLoader.loadFromFile(humanGeneIDPath));
		
		GeneRegulationGenerator generator = new GeneRegulationGenerator(transFacDatas, transGeneIDs, humanGeneDictionary);
		return generator.generate();
	}
	
	public static void main(String[] args) {
		List<GeneRegulation> grs = generateMapping(args[0], args[1], args[2]);
		
		for (GeneRegulation gr : grs) {
			System.out.println(gr.toString());
		}
	}
}
