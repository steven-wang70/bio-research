package bioinfo.transfac.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bioinfo.transfac.TransFacGeneID;

public class TransFacGeneIDLoader {
	
	private InputStream _in;
	private PrintStream _err = System.err;
	private List<TransFacGeneID> _transFacGeneIDs = new ArrayList<TransFacGeneID>();
	
	public void setInputStream(InputStream ins) {
		_in = ins;
	}
	
	public List<TransFacGeneID> loadFromFile(String path) {
		try {
			setInputStream(new FileInputStream(new File(path)));
			extractData();
			return _transFacGeneIDs;
		} catch (FileNotFoundException ex1) {
			System.err.println(ex1.getMessage());
			ex1.printStackTrace();
			return null;
		}
	}
	
	private static final String TransFacID = "AC";
	private static final String KeySynonyms = "SY";
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
				if (keyword.equals(TransFacID)) {
					processTransFacID(line);
				} else if (keyword.equals(KeySynonyms)) {
					processSynonyms(line);
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

	private TransFacGeneID tempData = new TransFacGeneID();
	
	/**
	 * If we got an valid data, process it, then reset the temp data
	 */
	private void processSeparator() {
		if (tempData.getTransFacID() != null && tempData.getAliases() != null) {
			_transFacGeneIDs.add(tempData);
		}
		
		tempData = new TransFacGeneID();
	}
	
	private void processTransFacID(String line) {
		tempData.setTransFacID(line.substring(3).trim());
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

	/**
	 * @param args
	 * Test the loader
	 */
	public static void main(String[] args) {
		TransFacGeneIDLoader loader = new TransFacGeneIDLoader();
		loader.setInputStream(System.in);
		loader.extractData();
		
		if (loader._transFacGeneIDs != null) {
			for (TransFacGeneID gid : loader._transFacGeneIDs) {
				System.out.println(gid.toString());
			}
		}
	}}
