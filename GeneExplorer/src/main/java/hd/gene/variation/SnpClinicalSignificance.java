package hd.gene.variation;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SnpClinicalSignificance {
	private String snpName;
	private String cliSigs;
	private int cliSigValues = ClinicalSignificance.None;
	
	public String getSnpName() {
		return snpName;
	}
	public void setSnpName(String snpName) {
		this.snpName = snpName;
	}
	public String getCliSigs() {
		return cliSigs;
	}
	public void setCliSigs(String cliSigs) {
		this.cliSigs = cliSigs;
	}
	public int getCliSigValues() {
		return cliSigValues;
	}
	public void setCliSigValues(int cliSigValues) {
		this.cliSigValues = cliSigValues;
	}
	

	private static HashMap<String, SnpClinicalSignificance> _snpCliValues;

	public static HashMap<String, SnpClinicalSignificance> getSnpCliSigValues() {
		if (_snpCliValues == null){
			String resource = "hd/gene/variation/mybatis_config.xml";
			InputStreamReader reader = new InputStreamReader(SnpClinicalSignificance.class.getClassLoader().getResourceAsStream(resource));

			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			SqlSession session = sqlSessionFactory.openSession();
			SnpClinicalSignificanceMapper mapper = session.getMapper(SnpClinicalSignificanceMapper.class);
			List<SnpClinicalSignificance> snpCliSigValues = mapper.retrieveSnpClinicalSignificances();
			
			HashMap<String, SnpClinicalSignificance> result = new HashMap<String, SnpClinicalSignificance>();
			for (SnpClinicalSignificance i : snpCliSigValues){
				i.setCliSigValues(ClinicalSignificance.parseSignificance(i.cliSigs) | ClinicalSignificance.SNPBase);
				result.put(i.getSnpName().toLowerCase(), i);
			}
			
			_snpCliValues = result;
		}
		
		return _snpCliValues;
	}
}
