package bioinfo.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import bioinfo.utils.Utils;

public class ConcentrationLoader {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private Hashtable<String, Double> _concentrations = new Hashtable<String, Double>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public Hashtable<String, Double> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			return extractData();
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
		}
		
		return null;
	}

	private Hashtable<String, Double> extractData() {
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				String[] items = line.split(";");
				if (items.length == 2) {
					_concentrations.put(items[0], Double.parseDouble(items[1]));
				}
			}
		} catch (Exception e) {
			_concentrations.clear();
			_err.println(String.format("Fatal error at line: %1$d", lineCounter));
			_err.println(e.getMessage());
			e.printStackTrace(_err);
		} finally {
			sc.close();
		}
		
		return _concentrations;
	}
}
