package bioinfo.regulation;

import java.util.HashMap;

import bioinfo.markov.Gate;
import bioinfo.markov.MarkovNetwork;

public class CellGeneExpressions extends HashMap<String, Boolean> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1332554194515729081L;

	public static String buildAttractorForMKN(MarkovNetwork mkn, HashMap<String, Boolean> cellExpression) {
		StringBuffer sb = new StringBuffer();
		for (Gate g: mkn.getGates()) {
			// STEVEN
			String geneName = mkn.getNameIDs().get(g.getID());
			if (!cellExpression.containsKey(geneName)) {
				System.err.println("No Expression for: " + geneName);
				continue;
			}
			sb.append(cellExpression.get(mkn.getNameIDs().get(g.getID())) ? '1' : '0');
		}
		
		return sb.toString();
	}

}
