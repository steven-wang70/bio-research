package bioinfo.simulation.ode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

import bioinfo.regulation.GeneRegulation;
import bioinfo.regulation.GeneRegulationAnalysis;
import bioinfo.utils.Utils;

public class ODEResultLoader {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<double[]> _odeResults = new ArrayList<double[]>();
	private int varCount;
	
	public ODEResultLoader(int varCount) {
		this.varCount = varCount;
	}
	
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public List<double[]> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			extractData();
			return _odeResults;
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
		}
		
		return null;
	}

	List<double[]> extractData() {
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				String[] items = line.split("\t");
				if (items.length != varCount) {
					throw new Exception();
				}
				
				double[] ode = new double[varCount];
				_odeResults.add(ode);
				
				for (int i = 0; i < varCount; i++) {
					ode[i] = Double.parseDouble(items[i]);
				}
			}
			
		} catch (Exception e) {
			_odeResults.clear();
			_err.println(String.format("Fatal error at line: %1$d", lineCounter));
			_err.println(e.getMessage());
			e.printStackTrace(_err);
		} finally {
			sc.close();
		}
		
		return _odeResults;
	}

	public static void main(String[] args) {
		analyzeODEResults();
	}
	
	private static void analyzeODEResults() {
		final int DIMENSION = 81;
		final int GRANULARITY = 100;
		ODEResultLoader loader = new ODEResultLoader(DIMENSION);
		List<double[]> results = loader.loadFromFile("D:\\steven\\gitsrc\\CancerDrug\\Data\\ode\\leukemia\\ode.txt");
		
		for (int i = 0; i < DIMENSION; i++) {
			int[] stat = new int[GRANULARITY];
			Iterator<double[]> iter = results.iterator();
			while (iter.hasNext()) {
				double v = iter.next()[i] * GRANULARITY;
				if (v < 0) {v = 0;}
				stat[(int)Math.floor(v)]++;
			}
			
//			Arrays.sort(stat);
//			ArrayUtils.reverse(stat);
			System.out.println(Utils.Array2String(stat));
		}
		
	}

}
