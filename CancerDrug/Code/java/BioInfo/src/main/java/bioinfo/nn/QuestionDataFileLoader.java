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

public class QuestionDataFileLoader {
	private InputStream _in;
	private PrintStream _err = System.err;
	private Scanner scanner;
	int lineCounter = 0;
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public void loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			this.scanner = new Scanner(_in);
			this.scanner.nextLine(); // Skip the header line
			this.lineCounter = 1;

			return;
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
			return;
		}
	}

	public QuestionData loadData() {
		if (!this.scanner.hasNextLine()) {
			return null;
		}
		
		String line = this.scanner.nextLine();
		lineCounter++;
		
		String[] items = line.split(",");
		
		if (items.length > 10) {
			String cellLine = items[0];
			String drugName1 = items[1];
			String drugName2 = items[2];
			Double additionalSurvival = Double.parseDouble(items[items.length - 1]);
			
			double[] input = new double[items.length - 4];
			for (int i = 0; i < input.length; i++) {
				input[i] = Double.parseDouble(items[i + 3]);
			}
			return new QuestionData(cellLine, drugName1, drugName2, input, additionalSurvival);
		}
		
		return null;
	}
}
