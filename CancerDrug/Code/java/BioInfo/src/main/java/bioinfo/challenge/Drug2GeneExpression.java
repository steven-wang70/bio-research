package bioinfo.challenge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import bioinfo.markov.Gate;
import bioinfo.markov.MarkovException;
import bioinfo.markov.MarkovNetwork;
import bioinfo.markov.SerializationException;
import bioinfo.markov.Serializer;
import bioinfo.regulation.CellGeneExpressions;
import bioinfo.regulation.GeneDictionary;
import bioinfo.utils.CircularArrayList;
import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class Drug2GeneExpression {

	/**
	 * For a given cell type, calculate all expressions with single drug and sub cell type combination
	 * @param mkn
	 * @param drugTargets
	 * @param initialExpressions
	 */
	public static void SingleDrugExpressionForCell(MarkovNetwork mkn, List<DrugAndTarget> drugTargets, Map<String, DrugResponse> drugResponses, HashMap<String, CellGeneExpressions> initialExpressions, BufferedWriter bw) {
		DrugResponse.NormalizeLevel = Settings.instance().getSetting("ExpressionOnDrug", "TrainingNormalizeLevel", Integer.class);
		
		GeneDictionary dict = new GeneDictionary(mkn.getNameIDs());
		// Print header
		Utils.println(createHeader(mkn, 1, drugTargets, new String[]{"Survival"}, ","), bw);
		
		HashSet<String> cannotFindAttractorFor = new HashSet<String>();
		HashSet<String> cannotFindDrugResponseFor = new HashSet<String>();
		
		for (String cellName : initialExpressions.keySet()) {
			HashMap<String, Boolean> expr = initialExpressions.get(cellName);
			mkn.resetStates();
			boolean[] expression = Utils.string2BooleanArray(CellGeneExpressions.buildAttractorForMKN(mkn, expr));
			for (int i = 0; i < drugTargets.size(); i++) {
				mkn.resetStates();
				try {
					mkn.setInitialStates(expression);
				} catch (MarkovException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				applyDrugTargets(new DrugAndTarget[]{drugTargets.get(i)}, mkn, dict);
				String newAttractor = findNextAttractor(mkn, similarityPercent, similarityWindow, similarityMinSteps, similarityMaxSteps);
				if (newAttractor != null) {
					List<String> trainingData = createTrainingDataForSingleDrug(cellName, newAttractor, drugTargets, i, drugResponses, ",", cannotFindDrugResponseFor);
					if (trainingData != null) {
						for (String s : trainingData) {
							Utils.println(s, bw);
						}
					}
				} else {
					cannotFindAttractorFor.add(drugTargets.get(i).DrugName);
				}
			}
		}
		
		if (cannotFindAttractorFor.size() > 0) {
			for (String s : cannotFindAttractorFor) {
				System.err.println("Cannot find attractor for: " + s);
			}
		}
		
		if (cannotFindDrugResponseFor.size() > 0) {
			for (String s : cannotFindDrugResponseFor) {
				System.err.println("Cannot find drug response for drug/cell: " + s);
			}
		}
	}

	/**
	 * For a given cell type, calculate all expressions with two drugs and sub cell type combination
	 * @param mkn
	 * @param drugTargets
	 * @param initialExpressions
	 */
	public static void DoubleDrugsExpressionForCell(MarkovNetwork mkn, List<DrugAndTarget> drugTargets, Map<String, DrugResponse> drugResponses, HashMap<String, CellGeneExpressions> initialExpressions, BufferedWriter bw) {
		DrugResponse.NormalizeLevel = Settings.instance().getSetting("ExpressionOnDrug", "QuestionNormalizeLevel", Integer.class);
		
		GeneDictionary dict = new GeneDictionary(mkn.getNameIDs());
		// Print header
		Utils.println(createHeader(mkn, 2, drugTargets, new String[]{"AdditionalSurvival"}, ","), bw);

		HashSet<String> cannotFindAttractorFor = new HashSet<String>();
		HashSet<String> cannotFindDrugResponseFor = new HashSet<String>();

		for (String cellName : initialExpressions.keySet()) {
			HashMap<String, Boolean> expr = initialExpressions.get(cellName);
			mkn.resetStates();
			boolean[] expression = Utils.string2BooleanArray(CellGeneExpressions.buildAttractorForMKN(mkn, expr));
			for (int i = 0; i < drugTargets.size() - 1; i++) {
				for (int j = i + 1; j < drugTargets.size(); j++) {
					mkn.resetStates();
					try {
						mkn.setInitialStates(expression);
					} catch (MarkovException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					applyDrugTargets(new DrugAndTarget[]{drugTargets.get(i)}, mkn, dict);
					applyDrugTargets(new DrugAndTarget[]{drugTargets.get(j)}, mkn, dict);
					String newAttractor = findNextAttractor(mkn, similarityPercent, similarityWindow, similarityMinSteps, similarityMaxSteps);
					if (newAttractor != null) {
						List<String> questionData = createQuestionForDoubleDrugs(cellName, newAttractor, drugTargets, i, j, drugResponses, ",", cannotFindDrugResponseFor);
						if (questionData != null) {
							for (String s : questionData) {
								Utils.println(s, bw);
							}
						}
					} else {
						cannotFindAttractorFor.add(drugTargets.get(i).DrugName + "/" + drugTargets.get(j).DrugName);
					}
				}
			}
		}
		
		if (cannotFindAttractorFor.size() > 0) {
			for (String s : cannotFindAttractorFor) {
				System.err.println("Cannot find attractor for: " + s);
			}
		}
		
		if (cannotFindDrugResponseFor.size() > 0) {
			for (String s : cannotFindDrugResponseFor) {
				System.err.println("Cannot find drug response for drug/cell: " + s);
			}
		}
	}
	
	private static String createHeader(MarkovNetwork mkn, int drugCount, List<DrugAndTarget> dts, String[] additionals, String separator) {
		GeneDictionary dict = new GeneDictionary(mkn.getNameIDs());
		StringBuffer sb = new StringBuffer();
		
		// Print header
		sb.append("CellLine");
		
		for (int i = 0; i < drugCount; i++) {
			sb.append(separator);
			sb.append("DrugName" + Integer.toString(i));
		}
		
		for (Gate g : mkn.getOutputNodes()) {
			sb.append(separator);
			sb.append(dict.getNameByID(g.getID()));
		}
		
		for (DrugAndTarget dt : dts) {
			sb.append(separator);
			sb.append(dt.DrugName);
		}
		
		if (additionals != null) {
			for (String s : additionals) {
				sb.append(separator);
				sb.append(s);
			}
		}
		
		return sb.toString();
	}
	
	private static DrugResponse getDrugResponse(Map<String, DrugResponse> drugResponses, String cellName, List<DrugAndTarget> drugTargets, int index, HashSet<String> cannotFindDrugResponseFor) {
		DrugResponse dr = drugResponses.get(DrugResponse.ResponseDataKey(cellName,  drugTargets.get(index).DrugName));
		if (dr == null) {
			cannotFindDrugResponseFor.add(drugTargets.get(index).DrugName + "/" + cellName);
		}
		
		return dr;
	}
	
	private static List<String> createTrainingDataForSingleDrug(String cellName, String attractor, List<DrugAndTarget> drugTargets, int index, Map<String, DrugResponse> drugResponses, String separator, HashSet<String> cannotFindDrugResponseFor) {
		// Find the drug response
		DrugResponse dr = getDrugResponse(drugResponses, cellName, drugTargets, index, cannotFindDrugResponseFor);
		if (dr == null) {
			return null; // NO such data
		}
		
		ArrayList<String> trainingData = new ArrayList<String>();
		
		// First create expression data
		StringBuffer sb = new StringBuffer();
		
		sb.append(cellName);
		sb.append(separator);
		sb.append(drugTargets.get(index).DrugName);
		
		for (int i = 0; i < attractor.length(); i++) {
			sb.append(separator);
			char c = attractor.charAt(i);
			if (c == '1') {
				sb.append(c);
			} else if (c == '0') {
				sb.append("-1");
			}
		}
		
		String expr = sb.toString();
		
		// Then create dose and response
		for (int dose = 0; dose <= DrugResponse.NormalizeLevel; dose++) {
			sb = new StringBuffer();
			for (int i = 0; i < drugTargets.size(); i++) {
				sb.append(separator);
				if (i != index) {
					sb.append("0");
				} else {
					sb.append(dose * 1.0 / DrugResponse.NormalizeLevel);
				}
			}
			
			sb.append(separator);
			sb.append(dr.normalizedDose2Survival(dose));
			
			trainingData.add(expr + sb.toString());
		}
		
		return trainingData;
	}
	
	private static List<String> createQuestionForDoubleDrugs(String cellName, String attractor, List<DrugAndTarget> drugTargets, int index1, int index2, Map<String, DrugResponse> drugResponses, String separator, HashSet<String> cannotFindDrugResponseFor) {
		// Find the drug response
		DrugResponse dr1 = getDrugResponse(drugResponses, cellName, drugTargets, index1, cannotFindDrugResponseFor);
		if (dr1 == null) {
			return null; // NO such data
		}
		DrugResponse dr2 = getDrugResponse(drugResponses, cellName, drugTargets, index2, cannotFindDrugResponseFor);
		if (dr2 == null) {
			return null; // NO such data
		}
		
		ArrayList<String> trainingData = new ArrayList<String>();
		
		// First create expression data
		StringBuffer sb = new StringBuffer();
		
		sb.append(cellName);
		sb.append(separator);
		sb.append(drugTargets.get(index1).DrugName);
		sb.append(separator);
		sb.append(drugTargets.get(index2).DrugName);
		
		for (int i = 0; i < attractor.length(); i++) {
			sb.append(separator);
			char c = attractor.charAt(i);
			if (c == '1') {
				sb.append(c);
			} else if (c == '0') {
				sb.append("-1");
			}
		}
		
		String expr = sb.toString();
		
		// Then create dose and response
		for (int dose1 = 0; dose1 <= DrugResponse.NormalizeLevel; dose1++) {
			for (int dose2 = 0; dose2 <= DrugResponse.NormalizeLevel; dose2++) {
				sb = new StringBuffer();
				for (int i = 0; i < drugTargets.size(); i++) {
					sb.append(separator);
					if (i == index1) {
						sb.append(dose1 * 1.0 / DrugResponse.NormalizeLevel);
					} else if (i == index2) {
						sb.append(dose2 * 1.0 / DrugResponse.NormalizeLevel);
					} else {
						sb.append("0");
					}
				}
				
				sb.append(separator);
				sb.append(DrugResponse.additiveDose2Survival(dr1, dose1, dr2, dose2));
				
				trainingData.add(expr + sb.toString());
			}
		}
		
		return trainingData;
	}
	
	private static void applyDrugTargets(DrugAndTarget[] targets, MarkovNetwork mkn, GeneDictionary dict) {
		for (DrugAndTarget dt : targets) {
			for (String targetName: dt.TargetNames) {
				int nodeId = dict.getIDByName(targetName);
				if (nodeId != GeneDictionary.InvalidID) {
					Gate g = (Gate) mkn.getNodeByID(nodeId);
					g.presetState(dt.Activatable, true);
				} else {
//					System.err.println(dt.DrugName + " Target " + targetName + " does not exist");
				}
			}
		}
	}
	
	/**
	 * Find the next single point attractor with given criteria
	 * @param mkn
	 * @param similarity
	 */
	private static String findNextAttractor(MarkovNetwork mkn, double similarity, int windowLen, int initialDistanceFactor, int maxDistanceFactor) {
		if (windowLen < 2 || initialDistanceFactor < 2 || maxDistanceFactor < initialDistanceFactor) {
			return null;
		}
		
		CircularArrayList<String> trace = new CircularArrayList<String>(windowLen);
		
		int distance = 0;
		// First move a distance
		for (distance = 0; distance < windowLen * initialDistanceFactor; distance++) {
			if (!advanceMKN(mkn, trace)) {
				return null;
			}
		}
		
		do  {
			if (Utils.leastSimilarity(trace) > similarity) {
				return mkn.getFullGateStates();
			}
			if (!advanceMKN(mkn, trace)) {
				return null;
			}
			distance++;
		} while (distance < maxDistanceFactor);
		
		return null;
	}
	
	private static boolean advanceMKN(MarkovNetwork mkn, CircularArrayList<String> trace) {
		try {
			mkn.moveNext();
		} catch (MarkovException e) {
			e.printStackTrace();
			return false;
		}
		trace.add(mkn.getFullGateStates());
		return true;
	}

	private static double similarityPercent;
	private static int similarityWindow;
	private static int similarityMinSteps;
	private static int similarityMaxSteps;
	
	public static void main(String[] args) {
		Settings.loadSettings(args[0]);

		String cellInfoFile = Settings.instance().getSetting("General", "CellInfoFile");
		String geneExpressionFile = Settings.instance().getSetting("General", "GeneExpressionFile");
		String drugTargetFile = Settings.instance().getSetting("General", "DrugTargetFile");
		String drugResponseFile = Settings.instance().getSetting("General", "DrugResponseFile");
		String[] processCellTypes = Settings.instance().getSetting("General", "ProcessCellTypes").split(",");
		String defaultOutFolder =  Settings.instance().getSetting("General", "DefaultOutFolder");
		
		String markovFile = Settings.instance().getSetting("EvolutionAlgorithm", "GeneratedMKNFile");
		
		String trainingDataFile = Settings.instance().getSetting("ExpressionOnDrug", "TrainingDataFile");
		String questionDataFile = Settings.instance().getSetting("ExpressionOnDrug", "QuestionDataFile");
		
		similarityPercent = Settings.instance().getSetting("ExpressionOnDrug", "SimilarityPercent", Double.class);
		similarityWindow = Settings.instance().getSetting("ExpressionOnDrug", "SimilarityWindow", Integer.class);
		similarityMinSteps = Settings.instance().getSetting("ExpressionOnDrug", "SimilarityMinSteps", Integer.class);
		similarityMaxSteps = Settings.instance().getSetting("ExpressionOnDrug", "SimilarityMaxSteps", Integer.class);
		
		try {
			HashMap<String, HashMap<String, CellGeneExpressions>> initialExpressions = CellInitialExpressionLoader.loadInitialExpressions(cellInfoFile, geneExpressionFile);
			
			DrugAndTargetLoader dtLoader = new DrugAndTargetLoader();
			List<DrugAndTarget> dts = dtLoader.loadFromFile(drugTargetFile);
			
			DrugResponseLoader drl = new DrugResponseLoader();
			Map<String, DrugResponse> drugResponses = drl.loadFromFile(drugResponseFile);
			
			for (String cellType : processCellTypes) {
				String generatedMarkovFile = defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + markovFile;
				FileInputStream fis = new FileInputStream(new File(generatedMarkovFile));
				MarkovNetwork mkn = Serializer.deserialize(fis);
				
				BufferedWriter traingFileWriter = Utils.openFileToWrite(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + trainingDataFile);
				SingleDrugExpressionForCell(mkn, dts, drugResponses, initialExpressions.get(cellType), traingFileWriter);
				Utils.close(traingFileWriter);
				
				BufferedWriter questionFileWriter = Utils.openFileToWrite(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + questionDataFile);
				DoubleDrugsExpressionForCell(mkn, dts, drugResponses, initialExpressions.get(cellType), questionFileWriter);
				Utils.close(questionFileWriter);
			}

			return;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (SerializationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (MarkovException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
