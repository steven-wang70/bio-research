package bioinfo.regulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bioinfo.utils.Utils;

public class GeneIDLoader {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<String> _humanGeneIDs = new ArrayList<String>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public List<String> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			extractData();
			return _humanGeneIDs;
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
		}
		
		return null;
	}

	private void extractData() {
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				_humanGeneIDs.add(Utils.UnifiedGeneName(line.trim()));
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
