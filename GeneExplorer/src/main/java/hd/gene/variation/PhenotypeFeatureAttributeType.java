package hd.gene.variation;

import java.io.InputStreamReader;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class PhenotypeFeatureAttributeType {
	private int attribTypeId;

	public int getAttribTypeId() {
		return attribTypeId;
	}

	public void setAttribTypeId(int attribTypeId) {
		this.attribTypeId = attribTypeId;
	}
	
	public static final int INVALID_ATTRIB_TYPE_ID = -1;
	
	private static boolean cached = false;
	private static int pValueId = INVALID_ATTRIB_TYPE_ID;
	private static int riskAlleleId = INVALID_ATTRIB_TYPE_ID;
	private static int clinvarClinSigId = INVALID_ATTRIB_TYPE_ID;
	
	private static void retrieveAttribTypeIds() {
		String resource = "hd/gene/variation/mybatis_config.xml";
		InputStreamReader reader = new InputStreamReader(PhenotypeFeatureAttributeType.class.getClassLoader().getResourceAsStream(resource));

		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sqlSessionFactory.openSession();
		PhenotypeFeatureAttributeTypeMapper mapper = session.getMapper(PhenotypeFeatureAttributeTypeMapper.class);
		
		List<PhenotypeFeatureAttributeType> attribTypes = mapper.retrieveAttributeType("risk_allele");
		if (attribTypes.size() == 1) {
			riskAlleleId = attribTypes.get(0).attribTypeId;
		}
		attribTypes = mapper.retrieveAttributeType("p_value");
		if (attribTypes.size() == 1) {
			pValueId = attribTypes.get(0).attribTypeId;
		}
		attribTypes = mapper.retrieveAttributeType("clinvar_clin_sig");
		if (attribTypes.size() == 1) {
			clinvarClinSigId = attribTypes.get(0).attribTypeId;
		}
		
		cached = true;
	}
	
	public static int getRiskAlleleId() {
		if (!cached) {
			retrieveAttribTypeIds();
		}
		
		return riskAlleleId;
	}
	
	public static int getPValueId() {
		if (!cached) {
			retrieveAttribTypeIds();
		}
		
		return pValueId;
	}
	
	public static int getClinvarClinSigId() {
		if (!cached) {
			retrieveAttribTypeIds();
		}
		
		return clinvarClinSigId;
	}
}
