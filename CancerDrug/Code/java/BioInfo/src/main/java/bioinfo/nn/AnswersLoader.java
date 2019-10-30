package bioinfo.nn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnswersLoader {
	private InputStream _in;
	private PrintStream _err = System.err;

	List<DrugSynergy> synergies = new ArrayList<DrugSynergy>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public void loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			extractData();
			return;
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
			return;
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
			
			String cellLineName = null;
			String drug1Name = null;
			String drug2Name = null;
			double summary = 0;
			int sampleCount = 0;
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				
				String[] items = line.split(",");
				
				if (items.length == 5) {
					if (items[0].equals(cellLineName) && items[1].equals(drug1Name) && items[2].equals(drug2Name)) {
						summary += Double.parseDouble(items[4]) - Double.parseDouble(items[3]);
						sampleCount++;
					} else {
						if (sampleCount > 0) {
							synergies.add(new DrugSynergy(cellLineName, drug1Name, drug2Name, summary / sampleCount));
						}
						cellLineName = items[0];
						drug1Name = items[1];
						drug2Name = items[2];
						summary = Double.parseDouble(items[4]) - Double.parseDouble(items[3]);
						sampleCount = 1;
					}
				}
			}
			
			if (cellLineName != null && drug1Name != null && drug2Name != null) {
				synergies.add(new DrugSynergy(cellLineName, drug1Name, drug2Name, summary / sampleCount));
			}
		} catch (Exception e) {
			_err.println(String.format("Fatal error at line: %1$d", lineCounter));
			_err.println(e.getMessage());
			e.printStackTrace(_err);
		} finally {
			sc.close();
		}
	}
}
