package bioinfo.transfac.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bioinfo.transfac.BindingSite;
import bioinfo.transfac.TransFacData;

public class TransFacDataLoader {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<TransFacData> _transFacDatas = new ArrayList<TransFacData>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public List<TransFacData> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			extractData();
			return _transFacDatas;
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
			return null;
		}
	}
	
	private static final String KeyFactorName = "FA";
	private static final String KeySynonyms = "SY";
	private static final String KeyBindingSite = "BS";
	private static final String KeySeparator = "//";
	
	/**
	 * See the link for field explanation:
	 * http://www.gene-regulation.com/pub/databases/transfac/doc/factor2.html
	 * 
	 * @param in
	 * @param out
	 */
	private void extractData(){
		Scanner sc = new Scanner(_in);
		int lineCounter = 0;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				if (line.length() < 2) continue;
				
				String keyword = line.substring(0,  2);
				if (keyword.equals(KeyFactorName)) {
					processFactorName(line);
				} else if (keyword.equals(KeySynonyms)) {
					processSynonyms(line);
				} else if (keyword.equals(KeyBindingSite)) {
					processBindingSite(line);
				} else if (keyword.equals(KeySeparator)) {
					processSeparator();
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

	private TransFacData tempData = new TransFacData();
	
	/**
	 * If we got an valid data, process it, then reset the temp data
	 */
	private void processSeparator() {
		if (tempData.getGeneId() != null && tempData.getBindingSites() != null) {
			_transFacDatas.add(tempData);
		}
		
		tempData = new TransFacData();
	}
	
	private void processFactorName(String line) {
		tempData.setGeneId(GeneIDNormalizer.normalize(line.substring(3).trim())[0]);
	}
	
	
	private void processSynonyms(String line) {
		line = line.trim();
		if (line.endsWith(".")) {
			line = line.substring(0, line.length() - 1);
		}
		
		String[] aliases = line.substring(3).split(";");
		if (aliases != null && aliases.length > 0) {
			for (String s : aliases) {
				s = s.trim();
				if (s.length() > 0) {
					if (tempData.getAliases() == null) {
						tempData.setAliases(new ArrayList<String>());
					}
					
					String[] ids = GeneIDNormalizer.normalize(s);
					for (String id : ids) {
						tempData.getAliases().add(id);
					}
				}
			}
		}
	}
	
	
	private void processBindingSite(String line) {
		line = line.trim();
		if (line.endsWith(".")) {
			line = line.substring(0, line.length() - 1);
		}

		String[] bsFields = line.split(";");
		if (bsFields != null && bsFields.length >= 3) {
			String bindingGeneId = bsFields[1].trim();
			
			int quality = -1;
			try {
				quality = Integer.parseInt(bsFields[2].split(":")[1].trim());
			} catch (NumberFormatException e) {
				return;
			}
			
			String transFacId = null;
			if (bsFields.length > 3) {
				try {
					transFacId = bsFields[3].split(",")[1].trim();
				} catch (ArrayIndexOutOfBoundsException e) {
					return;
				}
				
			}
			
			if (tempData.getBindingSites() == null) {
				tempData.setBindingSites(new ArrayList<BindingSite>());
			}
			
			bindingGeneId = GeneIDNormalizer.normalize(bindingGeneId)[0];
			tempData.getBindingSites().add(new BindingSite(bindingGeneId, quality, transFacId));
		}
	}

	/**
	 * @param args
	 * Test the loader
	 */
	public static void main(String[] args) {
		TransFacDataLoader loader = new TransFacDataLoader();
		loader.setInputStream(System.in);
		loader.extractData();
		
		if (loader._transFacDatas != null) {
			for (TransFacData ts : loader._transFacDatas) {
				System.out.println(ts.toString());
			}
		}
	}
}
