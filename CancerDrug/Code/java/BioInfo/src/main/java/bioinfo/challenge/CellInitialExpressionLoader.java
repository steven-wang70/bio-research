package bioinfo.challenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import bioinfo.regulation.CellGeneExpressions;
import bioinfo.utils.Utils;

public class CellInitialExpressionLoader {
	public static HashMap<String, HashMap<String, CellGeneExpressions>> loadInitialExpressions(String cellTypeFile, String attractorsFile) throws FileNotFoundException {
		// First load cell types
		HashMap<String, HashMap<String, CellGeneExpressions>> cellInitialExpressions = loadCellTypes(cellTypeFile);
		
		HashMap<String, HashMap<String, Boolean>> allSubCellTypeExpressions = new HashMap<String, HashMap<String, Boolean>>();
		for (HashMap<String, CellGeneExpressions> ss : cellInitialExpressions.values()) {
			for (String s : ss.keySet()) {
				allSubCellTypeExpressions.put(s,  ss.get(s));
			}
		}
		
		loadAllSubAttractors(allSubCellTypeExpressions, attractorsFile);
		
		// Remove empty expression
		List<String> keys = new ArrayList<String>();
		for (HashMap<String, CellGeneExpressions> cellAttractor : cellInitialExpressions.values()) {
			keys.clear();
			keys.addAll(cellAttractor.keySet());
			for (String s : keys) {
				if (cellAttractor.get(s).size() == 0) {
					cellAttractor.remove(s);
				}
			}
		}
		
		keys.clear();
		keys.addAll(cellInitialExpressions.keySet());
		for (String s : keys) {
			if (cellInitialExpressions.get(s).size() == 0) {
				cellInitialExpressions.remove(s);
			}
		}

		return cellInitialExpressions;
	}
	
	private static HashMap<String, HashMap<String, CellGeneExpressions>> loadCellTypes(String file) throws FileNotFoundException {
		HashMap<String, HashMap<String, CellGeneExpressions>> types = new HashMap<String, HashMap<String, CellGeneExpressions>>();
		
		FileInputStream cellTypes = new FileInputStream(new File(file));
		Scanner sc = new Scanner(cellTypes);
		
		try {
			sc.nextLine(); // The header line
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] items = line.split(",");
				if (items.length == 6) {
					String subType = items[0].replace("\"", "");
					String cellType = items[4].replace("\"", "");
					
					if (!types.containsKey(cellType)) {
						types.put(cellType, new HashMap<String, CellGeneExpressions>());
					}
					
					types.get(cellType).put(subType, new CellGeneExpressions());
				}
			}
		} catch (Exception e) {
		} finally {
			sc.close();
		}
		
		return types;
	}

	private static void loadAllSubAttractors(HashMap<String, HashMap<String, Boolean>> allSubAttractors, String attractorsFile) throws FileNotFoundException {
		FileInputStream attractorsStream = new FileInputStream(new File(attractorsFile));
		Scanner sc = new Scanner(attractorsStream);
		
		try {
			String[] subCellNames = parseHeaderForSubCellNames(sc.nextLine()); // The header line
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] items = line.split(",");
				if (items.length != subCellNames.length + 1) {
					throw new Exception();
				}
				
				String geneName = Utils.UnifiedGeneName(items[0]);
				for (int i = 0; i < subCellNames.length; i++) {
					allSubAttractors.get(subCellNames[i]).put(geneName, getBooleanFromString(items[ i + 1 ]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}
	
	private static boolean getBooleanFromString(String s) throws Exception {
		if (s.equals("1")) {
			return true;
		} else if (s.equals("0")) {
			return false;
		} else {
			throw new Exception();
		}
	}
	private static String[] parseHeaderForSubCellNames(String s) {
		String[] items = s.split(",");
		String[] names = new String[items.length - 1];
		
		for (int i = 0; i < names.length; i++) {
			names[i] = items[i+1];
		}
		
		return names;
		
	}

	public static void main(String[] args) {
		try {
			HashMap<String, HashMap<String, CellGeneExpressions>> attrs = loadInitialExpressions(args[0], args[1]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return;
	}

}
