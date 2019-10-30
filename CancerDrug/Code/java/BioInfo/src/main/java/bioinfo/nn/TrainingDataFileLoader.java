package bioinfo.nn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import bioinfo.challenge.DrugResponse;

public class TrainingDataFileLoader {
	private InputStream _in;
	private PrintStream _err = System.err;

	List<double[]> InputDatas = new ArrayList<double[]>();
	List<Double> IdealResults = new ArrayList<Double>();
	
	List<double[]> pureVerificationDatas = new ArrayList<double[]>();
	List<Double> pureVerificationResults = new ArrayList<Double>();
	
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
				
				if (items.length > 10) {
					if (lineCounter % 2 == 0) {
						IdealResults.add(Double.parseDouble(items[items.length - 1]));
					} else {
						pureVerificationResults.add(Double.parseDouble(items[items.length - 1]));
					}
					
					double[] inputData = new double[items.length - 3];
					for (int i = 0; i < inputData.length; i++) {
						inputData[i] = Double.parseDouble(items[i + 2]);
					}
					
					if (lineCounter % 2 == 0) {
						InputDatas.add(inputData);
					} else {
						pureVerificationDatas.add(inputData);
					}
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
