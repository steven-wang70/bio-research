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

public class FixedGenesLoader {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<FixedGene> _fixedGenes = new ArrayList<FixedGene>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public List<FixedGene> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			return extractData();
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
		}
		
		return null;
	}

	private List<FixedGene> extractData() {
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				String[] items = line.split(";");
				if (items.length == 3) {
					_fixedGenes.add(new FixedGene(items[0], Utils.UnifiedGeneName(items[1]), items[2].equals("1") ? true : false));
				}
			}
		} catch (Exception e) {
			_fixedGenes.clear();
			_err.println(String.format("Fatal error at line: %1$d", lineCounter));
			_err.println(e.getMessage());
			e.printStackTrace(_err);
		} finally {
			sc.close();
		}
		
		return _fixedGenes;
	}
}
