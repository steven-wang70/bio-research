package bioinfo.challenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bioinfo.nn.DrugSynergy;

public class ActualSynergyLoader {
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
			
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				
				String[] items = line.split(",");
				
				if (items.length > 12) {
					synergies.add(new DrugSynergy(items[0].replace("\"", ""), items[1].replace("\"", ""), items[2].replace("\"", ""), Double.parseDouble(items[11])));
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
}
