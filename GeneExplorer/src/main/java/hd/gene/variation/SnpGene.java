package hd.gene.variation;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SnpGene {
	private String snpName;
	private String geneName;
	
	public String getSnpName() {
		return snpName;
	}
	public void setSnpName(String snpName) {
		this.snpName = snpName;
	}
	public String getGeneName() {
		return geneName;
	}
	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}
	
	
	public static HashMap<String, SnpGene> LoadSnpGenes(String[] snpNames){
		HashMap<String, SnpGene> result = new HashMap<String, SnpGene>();
		
		if (snpNames != null && snpNames.length > 0) {
			String resource = "hd/gene/variation/mybatis_config.xml";
			InputStreamReader reader = new InputStreamReader(SnpGene.class.getClassLoader().getResourceAsStream(resource));

			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			SqlSession session = sqlSessionFactory.openSession();
			SnpGeneMapper mapper = session.getMapper(SnpGeneMapper.class);
			List<SnpGene> snpGenes = mapper.retrieveSnpGenes(snpNames);
			
			for (SnpGene i : snpGenes){
				result.put(i.getSnpName().toLowerCase(), i);
			}
		}
		
		return result;
	}
	
}
