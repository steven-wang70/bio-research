package bioinfo.simulation.ode;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import bioinfo.regulation.GeneRegulation;
import bioinfo.regulation.GeneRegulationAnalysis;
import bioinfo.utils.Utils;

/**
 * @author steven
 * Convert the leukemia data from the thesis to our internal format.
 */
public class RegulationImporter {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<GeneRegulation> _geneRegulations = new ArrayList<GeneRegulation>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}

	private static String extractName(String s) throws Exception {
		int idx = s.indexOf('\"');
		if (idx == -1) {
			return null;
		}
		s = s.substring(idx + 1);
		
		idx = s.indexOf('\"');
		if (idx == -1) {
			throw new Exception();
		}
		return s.substring(0, idx);
	}
	
	private List<GeneRegulation> extractData() {
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine().trim();
				lineCounter++;
				String[] items = line.split(":");
				if (items.length != 2 && items.length != 3) {
					_geneRegulations.clear();
					_err.println(String.format("Fatal error at line: %1$d", lineCounter));
				}
				
				// The first one is target
				String targetGene = extractName(items[0]);
				
				// Second part is activators
				String activators[] = items[1].split(",");
				for (String a : activators) {
					String activator = extractName(a);
					if (activator != null) {
						_geneRegulations.add(new GeneRegulation("HS", activator, targetGene, GeneRegulation.Q1_FunctionallyConfirmed, GeneRegulation.RelationActivation ));
					}
				}
				
				// third part is inhibitors
				if (items.length == 3) {
					String inhibitors[] = items[2].split(",");
					for (String i : inhibitors) {
						String inhibitor = extractName(i);
						if (inhibitor != null) {
							_geneRegulations.add(new GeneRegulation("HS", inhibitor, targetGene, GeneRegulation.Q1_FunctionallyConfirmed, GeneRegulation.RelationInhibition ));
						}
					}
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
	
	public static void main(String[] args) {
		RegulationImporter importer = new RegulationImporter();
		importer.setInputStream(System.in);
		
		List<GeneRegulation> grs = importer.extractData();
		HashSet<String> genes = GeneRegulationAnalysis.stat(grs);
		
		List<String> sortedList = new ArrayList<String>(genes);
		Collections.sort(sortedList);
		for (String s : sortedList) {
			System.out.println(s);
		}
		
		for (GeneRegulation gr : grs) {
			System.out.println(gr);
		}
		
	}

}
