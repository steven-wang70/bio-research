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

public class TargetGenesLoader {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<TargetGene> _targetGenes = new ArrayList<TargetGene>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public List<TargetGene> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			return extractData();
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
		}
		
		return null;
	}

	private List<TargetGene> extractData() {
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				String[] items = line.split(";");
				if (items.length == 4) {
					_targetGenes.add(new TargetGene(items[0], Utils.UnifiedGeneName(items[1]), Integer.parseInt(items[2]), items[3]));
				}
			}
		} catch (Exception e) {
			_targetGenes.clear();
			_err.println(String.format("Fatal error at line: %1$d", lineCounter));
			_err.println(e.getMessage());
			e.printStackTrace(_err);
		} finally {
			sc.close();
		}
		
		return _targetGenes;
	}
}
