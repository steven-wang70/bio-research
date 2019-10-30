package bioinfo.transfac.loader;

public class GeneIDNormalizer {
	private GeneIDNormalizer(){}
	
	public static String[] normalize(String id) {
		// Remove characters after ',', they are description
		int index = id.indexOf(',');
		if (index >= 0) {
			id = id.substring(0, index);
		}
		
		// Remove '-'
		index = id.indexOf('-');
		if (index >= 0) {
			id = id.replaceAll("-", "");
		}
		
		// Uppercase
		id = id.toUpperCase();
		
		// Split ids with '/'
		return id.split("/");
	}
}
