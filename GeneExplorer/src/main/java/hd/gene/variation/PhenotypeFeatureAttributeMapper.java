
package hd.gene.variation;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PhenotypeFeatureAttributeMapper {
	public List<PhenotypeFeatureAttribute> retrieveAttributes(@Param("riskAlleleId") int riskAlleleId, @Param("pValueId") int pValueId, @Param("clinvarClinSigId") int clinvarClinSigId);
}