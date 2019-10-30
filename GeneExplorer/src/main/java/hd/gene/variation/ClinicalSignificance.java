package hd.gene.variation;

import java.util.HashMap;
import java.util.Set;

public class ClinicalSignificance {
	public static final int None = 0; 				// No value, 
	public static final int NotProvided = 0x800; 	// not provided=87960, 
	public static final int Uncertain = 1; 			// uncertain significance=93556, 
	public static final int Pathogenic = 2; 		// pathogenic=69316, 
	public static final int Benign = 4; 			// benign=20826, 
	public static final int LikeBenign = 8; 		// likely benign=15374, 
	public static final int LikelyPathogenic = 0x10; //likely pathogenic=7839, 
	public static final int Other = 0x20; 			// other=5718, 
	public static final int RiskFactor = 0x40; 		// risk factor=1880
	public static final int Association = 0x80; 	// association=260, 
	public static final int DrugResponse = 0x100; 	// drug response=126, 
	public static final int Protective = 0x200; 	// protective=85, 		
	public static final int ConfersSensitivity = 0x400; // confers sensitivity=3, 		

	public static final int SNPBase = 0x10000; // The data comes from Variation table;
	
	private static final String sNotProvided = 	"not provided";
	private static final String sUncertain = 	"uncertain significance";
	private static final String sPathogenic = 	"pathogenic";
	private static final String sBenign = 		"benign";
	private static final String sLikeBenign = 	"likely benign";
	private static final String sLikelyPathogenic = "likely pathogenic";
	private static final String sOther = 		"other";
	private static final String sRiskFactor = 	"risk factor";
	private static final String sAssociation = 	"association";
	private static final String sDrugResponse = "drug response";
	private static final String sProtective = 	"protective";
	private static final String sConfersSensitivity = "confers sensitivity";
	
	private static HashMap<String, Integer> key2Values = null;
	private static HashMap<Integer, String> value2Keys = null;
	
	private static void createMapping(){
		if (key2Values == null){
			key2Values = new HashMap<String, Integer>();
			value2Keys = new HashMap<Integer, String>();
			
			key2Values.put(sNotProvided, NotProvided);
			value2Keys.put(NotProvided, sNotProvided);
			
			key2Values.put(sUncertain, Uncertain);
			value2Keys.put(Uncertain, sUncertain);
			
			key2Values.put(sPathogenic, Pathogenic);
			value2Keys.put(Pathogenic, sPathogenic);
			
			key2Values.put(sBenign, Benign);
			value2Keys.put(Benign, sBenign);
			
			key2Values.put(sLikeBenign, LikeBenign);
			value2Keys.put(LikeBenign, sLikeBenign);
			
			key2Values.put(sLikelyPathogenic, LikelyPathogenic);
			value2Keys.put(LikelyPathogenic, sLikelyPathogenic);
			
			key2Values.put(sOther, Other);
			value2Keys.put(Other, sOther);
			
			key2Values.put(sRiskFactor, RiskFactor);
			value2Keys.put(RiskFactor, sRiskFactor);
			
			key2Values.put(sAssociation, Association);
			value2Keys.put(Association, sAssociation);
			
			key2Values.put(sDrugResponse, DrugResponse);
			value2Keys.put(DrugResponse, sDrugResponse);
			
			key2Values.put(sProtective, Protective);
			value2Keys.put(Protective, sProtective);
			
			key2Values.put(sConfersSensitivity, ConfersSensitivity);
			value2Keys.put(ConfersSensitivity, sConfersSensitivity);
		}
	}
	private static HashMap<String, Integer> unrecognizedKeywords = new HashMap<String, Integer>();
	public static int parseSignificance(String s){
		if (s == null){
			return 0;
		}
		s = s.trim();
		if (s.length() == 0){
			return 0;
		}
		
		createMapping();
		
		int significance = 0;
		String[] keys = s.split(",");
		for (String key : keys){
			if (key2Values.containsKey(key)){
				significance |= key2Values.get(key);
			} else {
				if (unrecognizedKeywords.containsKey(key)){
					unrecognizedKeywords.put(key, unrecognizedKeywords.get(key) + 1);
				}
				else {
					unrecognizedKeywords.put(key, 1);
				}
			}
		}
		
		return significance;
	}
	
	public static String toString(int cliSign){
		if (cliSign == 0){
			return "None";
		}
		
		createMapping();
		
		StringBuffer sb = new StringBuffer();
		if ((cliSign & SNPBase) != 0){
			sb.append("G:");
		} else {
			sb.append("P:");
		}
		
		Set<Integer> values = value2Keys.keySet();
		boolean firstOne = true;
		for (int value : values){
			if ((value & cliSign) != 0){
				if (firstOne){
					firstOne = false;
					sb.append(value2Keys.get(value));
				} else {
					sb.append("," + value2Keys.get(value));
				}
			}
		}

		return sb.toString();
	}

	public static final int AllValues = NotProvided |
			Uncertain | Pathogenic | Benign | 
			LikeBenign | LikelyPathogenic | Other | 
			RiskFactor | Association | DrugResponse | 
			Protective | ConfersSensitivity;
	
	public static int combineThese(int[] keyValues){
		int value = 0;
		for (int i : keyValues){
			value |= i;
		}
		
		return value;
	}
	
	public static int combineExcludeThese(int[] keyValues){
		int value = AllValues;
		for (int i : keyValues){
			value &= ~i;
		}
		
		return value;
	}
	
	public static int realValue(int cliSign){
		return cliSign & 0xffff;
	}
}
