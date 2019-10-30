package hd.gene.variation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GeneExplorer {

	public static void main(String[] args) {

		final String snpFile = "D:\\steven\\gitsrc\\hd\\GeneExplorer\\src\\main\\java\\hd\\gene\\variation\\genome_joseph_Xu_ZHOU_Full_20150223192325.txt";
		HashMap<String, PersonalSNP> snps = PersonalSNP.loadSNPData(snpFile);

		// Retrieve phenotypes that have clinical significance
		List<Phenotype> filterList = Phenotype.filterPersonalPhenotypes(snps, ClinicalSignificance.AllValues);
		
		print(filterList);

		return;
	}
 	
	private static void print(List<Phenotype> phenotypes) {
		HashSet<String> snpNames = Phenotype.getSnpNames(phenotypes);
		System.out.println();
		System.out.println("Phenotype Count: " + phenotypes.size());
		System.out.println("SNP Count: " + snpNames.size());
		System.out.println();
		for (Phenotype p : phenotypes) {
			System.out.println(p.toString());
		}
	}
}
