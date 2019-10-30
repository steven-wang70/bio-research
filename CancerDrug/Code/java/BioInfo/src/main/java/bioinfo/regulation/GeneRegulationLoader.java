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

public class GeneRegulationLoader {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<GeneRegulation> _geneRegulations = new ArrayList<GeneRegulation>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public List<GeneRegulation> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			extractData();
			return _geneRegulations;
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
		}
		
		return null;
	}

	List<GeneRegulation> extractData() {
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				String[] items = line.split(";");
				if (items.length == 4) {
					_geneRegulations.add(new GeneRegulation(items[0], Utils.UnifiedGeneName(items[1]), Utils.UnifiedGeneName(items[2]), Integer.parseInt(items[3])));
				} else if (items.length == 5) {
					_geneRegulations.add(new GeneRegulation(items[0], Utils.UnifiedGeneName(items[1]), Utils.UnifiedGeneName(items[2]), Integer.parseInt(items[3]), 
							items[4].equals("NA") ? GeneRegulation.RelationUnknown : Integer.parseInt(items[4])));
				}
			}
			
			_geneRegulations = GeneRegulationAnalysis.normalize(_geneRegulations, GeneRegulationAnalysis.Filter_All, GeneRegulation.Q6_Unknown);
		} catch (Exception e) {
			_geneRegulations.clear();
			_err.println(String.format("Fatal error at line: %1$d", lineCounter));
			_err.println(e.getMessage());
			e.printStackTrace(_err);
		} finally {
			sc.close();
		}
		
		return _geneRegulations;
	}
}
