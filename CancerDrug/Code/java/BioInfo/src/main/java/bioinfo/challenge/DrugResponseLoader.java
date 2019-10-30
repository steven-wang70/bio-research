package bioinfo.challenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DrugResponseLoader {
	private InputStream _in;
	private PrintStream _err = System.err;
	private Map<String, DrugResponse> _drugResponses = new HashMap<String, DrugResponse>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public Map<String, DrugResponse> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			extractData();
			return _drugResponses;
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param in
	 * @param out
	 */
	private void extractData(){
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			sc.nextLine(); // Skip the header line
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				
				String[] items = line.split(",");
				processDrugResponse(items[0], items[1], items[3], items[5], items[6], items[7]);
				processDrugResponse(items[0], items[2], items[4], items[8], items[9], items[10]);
			}
		} catch (Exception e) {
			_err.println(String.format("Fatal error at line: %1$d", lineCounter));
			_err.println(e.getMessage());
			e.printStackTrace(_err);
		} finally {
			sc.close();
		}
	}

	private void processDrugResponse(String cellLine, String drugName, String maxCon, String ic50, String h, String einf) {
		cellLine = cellLine.replace("\"", "");
		drugName = drugName.replace("\"", "");
		maxCon = maxCon.replace("\"", "");
		ic50 = ic50.replace("\"", "");
		h = h.replace("\"", "");
		einf = einf.replace("\"", "");
		
		String id = DrugResponse.ResponseDataKey(cellLine,  drugName);
		if (!_drugResponses.containsKey(id)) {
			_drugResponses.put(id, new DrugResponse(cellLine, drugName, Double.parseDouble(maxCon), Double.parseDouble(ic50), Double.parseDouble(h), Double.parseDouble(einf) / 100));
		}
	}
}
