package bioinfo.challenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import bioinfo.ea.EAException;
import bioinfo.ea.EAResult;
import bioinfo.ea.EvolutionAlgorithmFuzzyAttractors;
import bioinfo.markov.MarkovException;
import bioinfo.markov.MarkovNetwork;
import bioinfo.markov.SerializationException;
import bioinfo.markov.Serializer;
import bioinfo.regulation.CellGeneExpressions;
import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class EACancerState extends EvolutionAlgorithmFuzzyAttractors {
	private MarkovNetwork _templateMKN;
	public EACancerState(EAResult eaResult, boolean[][] scenarios,
			String[] expectedAttractors, MarkovNetwork templateMKN) throws EAException, MarkovException {
		super(eaResult, scenarios, expectedAttractors);
		_templateMKN = templateMKN;
	}

	@Override
	protected MarkovNetwork templateMKN() {
		return _templateMKN;
	}

	public static void RunEA(String mknFile, String cellTypeFile, String cellExpressionFile, String cellName) {
		final int ScenarioCount = 0;
		
		try {
			// First load initial markov network;
			MarkovNetwork mkn = Serializer.deserialize(new FileInputStream(new File(mknFile)));
			// Load cell expressions
			HashMap<String, HashMap<String, CellGeneExpressions>> attractors = CellInitialExpressionLoader.loadInitialExpressions(cellTypeFile, cellExpressionFile);
			if (!attractors.containsKey(cellName)) {
				// Print all cell types, then return
				for (String cellType : attractors.keySet()) {
					System.out.println(cellType);
				}
				return;
			}
			
			// Build scenarios;
			boolean[][] scenarios = buildScenarios(mkn, ScenarioCount);
			
			// Build expected attractors
			HashMap<String, CellGeneExpressions> cellExpression = attractors.get(cellName);
			String[] expectedAttractors = buildExpectedAttractors(mkn, cellExpression);
			
			// Create EA
			int population = Settings.instance().getSetting("EvolutionAlgorithm", "Population", Integer.class);
			double mutationRate = Settings.instance().getSetting("EvolutionAlgorithm", "MutationRate", Double.class);
			int maxGeneration = Settings.instance().getSetting("EvolutionAlgorithm", "MaxGeneration", Integer.class);
			double averageBitVoteConsistencyThreshold = Settings.instance().getSetting("EvolutionAlgorithm", "AverageBitVoteConsistencyThreshold", Double.class);
			EAResult eaResult = new EAResult(population, mutationRate, maxGeneration, averageBitVoteConsistencyThreshold);
			int fuzzyLength = Settings.instance().getSetting("EvolutionAlgorithm", "FuzzyWindowLength", Integer.class);
			double fuzzyAttractorRatio = Settings.instance().getSetting("EvolutionAlgorithm", "FuzzyAttractorRatio", Double.class);
			eaResult.setFuzzyLength(fuzzyLength);
			eaResult.setFuzzyAttractorRatio(fuzzyAttractorRatio);
			EACancerState thisEA = new EACancerState(eaResult, scenarios, expectedAttractors, mkn);
			
			// Run EA
			thisEA.evolute();
			System.out.println(eaResult.toString());
			
			String defaultOutFolder = Settings.instance().getSetting("General", "DefaultOutFolder");
			String markovFile = Settings.instance().getSetting("EvolutionAlgorithm", "GeneratedMKNFile");
			Utils.saveToFile(defaultOutFolder + Utils.PathSeparator + cellName + Utils.PathSeparator  + markovFile, eaResult.getLogic());
		} catch (FileNotFoundException fnfe) {
			System.err.println(fnfe.getMessage());
			fnfe.printStackTrace();
		} catch (SerializationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (MarkovException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (EAException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

	private static boolean[][] buildScenarios(MarkovNetwork mkn, int count) {
		int len = mkn.getGates().size();
		boolean[][] scenarios = new boolean[count][len];
		
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < len; j++) {
				scenarios[i][j] = Math.random() > 0.5 ? true : false;
			}
		}
		
		return scenarios;
	}
	
	private static String[] buildExpectedAttractors(MarkovNetwork mkn, HashMap<String, CellGeneExpressions> cellExpression) {
		String[] expectedAttractors = new String[cellExpression.size()];
		int index = 0;
		for (HashMap<String, Boolean> expr : cellExpression.values()) {
			expectedAttractors[index++] = CellGeneExpressions.buildAttractorForMKN(mkn, expr);
		}
		
		return expectedAttractors;
	}

	public static void main(String[] args) {
		Settings.loadSettings(args[0]);

		String initialMKNFile = Settings.instance().getSetting("General", "InitialMKNFile");
		String cellInfoFile = Settings.instance().getSetting("General", "CellInfoFile");
		String geneExpressionFile = Settings.instance().getSetting("General", "GeneExpressionFile");
		String[] cellNames = Settings.instance().getSetting("General", "ProcessCellTypes").split(",");
		
		for (String cellName : cellNames) {
			RunEA(initialMKNFile, cellInfoFile, geneExpressionFile, cellName);
		}
	}
}
