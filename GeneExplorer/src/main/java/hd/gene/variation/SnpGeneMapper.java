package hd.gene.variation;


import java.util.List;

public interface SnpGeneMapper {
	public List<SnpGene> retrieveSnpGenes(String[] snpNames);
}