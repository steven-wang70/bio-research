package hd.gene.variation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class PersonalSNP {
	private String rsid;
	private String chromosome;
	private int position;
	private String genotype;
	
	public PersonalSNP(String rsid, String chromosome, int position,
			String genotype) {
		super();
		this.rsid = rsid;
		this.chromosome = chromosome;
		this.position = position;
		this.genotype = genotype;
	}
	
	public String getRsid() {
		return rsid;
	}
	public String getChromosome() {
		return chromosome;
	}
	public int getPosition() {
		return position;
	}
	public String getGenotype() {
		return genotype;
	}
	
	
	private static HashMap<String, PersonalSNP> loadSNPData(BufferedReader br){
		HashMap<String, PersonalSNP> snps = new HashMap<String, PersonalSNP>();
		
		try{
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0 || line.startsWith("#")){
					continue;
				}
				
				String[] elements = line.split("\t");
				if (elements.length != 4){
					continue;
				}
				
				snps.put(elements[0],  new PersonalSNP(elements[0], elements[1], Integer.parseInt(elements[2]), elements[3]));
			}
			
		} catch (IOException e){
		}
		
		return snps;
	}
	
	
	/**
	 * Load personal SNP data from 23andme file format.
	 * @param snpFilePath
	 * @return
	 */
	public static HashMap<String, PersonalSNP> loadSNPData(String snpFilePath){
		try{
		    File file = new File(snpFilePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			HashMap<String, PersonalSNP> snps = loadSNPData(br);
			br.close();
			return snps;
		} catch(FileNotFoundException e){
			return null;
		} catch (IOException e2){
			return null;
		}
	}
}
