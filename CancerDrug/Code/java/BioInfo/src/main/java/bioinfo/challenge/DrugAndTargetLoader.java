package bioinfo.challenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bioinfo.utils.Utils;

public class DrugAndTargetLoader {
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<DrugAndTarget> _drugAndTargets = new ArrayList<DrugAndTarget>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public List<DrugAndTarget> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			extractData();
			return _drugAndTargets;
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
				
				String[] items = line.split("\t");
				if (items.length == 3) {
					processLine(items[0], items[1], items[2]);
				}
				
			}
		} catch (Exception e) {
			_err.println(String.format("Fatal error at line: %1$d", lineCounter));
			_err.println(e.getMessage());
			e.printStackTrace(_err);
		} finally {
			sc.close();
		}
	}

	private void processLine(String DrugName, String Targets, String activable) {
		DrugName = DrugName.trim();
		boolean activated = activable.trim().equals("1") ? true : false;
		
		Targets = Targets.replace("\"", "").replace("*", "");
		String[] items = Targets.split(",");
		for (int i = 0; i < items.length; i++) {
			items[i] = Utils.UnifiedGeneName(items[i].trim());
		}
		
		_drugAndTargets.add(new DrugAndTarget(DrugName, items, activated));
	}

}
