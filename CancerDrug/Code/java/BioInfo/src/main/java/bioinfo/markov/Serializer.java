package bioinfo.markov;

import java.io.InputStream;
import java.util.Scanner;

import bioinfo.utils.Utils;

public class Serializer {
	
	public static final String nodeIDsHeader = "[NodeIDs]";
	public static final String inputPointsHeader = "[InputPoints]";
	public static final String gatesHeader = "[Gates]";
	public static final String initialStatesHeader = "[InitialStates]";
	public static final String outputGatesHeader = "[OutputGates]";
	
	public static MarkovNetwork deserialize(InputStream is) throws SerializationException, MarkovException {
		MarkovNetwork mkn = new MarkovNetwork();
		
		Scanner sc = new Scanner(is);
		int lineCounter = 0;
		
		String currentSection = null;
		
		try {
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				lineCounter++;
				
				// Remove the comment
				if (line.indexOf("//") >= 0) {
					line = line.substring(0, line.indexOf("//"));
				}
				
				line = line.trim();
				if (line.length() == 0) {
					continue;
				}
				
				if (line.equals(nodeIDsHeader)) {
					currentSection = nodeIDsHeader;
				} else if (line.equals(inputPointsHeader)) {
					currentSection = inputPointsHeader;
				} else if (line.equals(gatesHeader)) {
					currentSection = gatesHeader;
				} else if (line.equals(initialStatesHeader)) {
					currentSection = initialStatesHeader;
				} else if (line.equals(outputGatesHeader)) {
					currentSection = outputGatesHeader;
				} else {
					try {
						if (currentSection.equals(nodeIDsHeader)) {
							parseNameID(mkn, lineCounter, line);
						} else if (currentSection.equals(inputPointsHeader)) {
							parseInputPoint(mkn, lineCounter, line);
						} else if (currentSection.equals(gatesHeader)) {
							parseGate(mkn, lineCounter, line);
						} else if (currentSection.equals(initialStatesHeader)) {
							parseInitialStates(mkn, lineCounter, line);
						} else if (currentSection.equals(outputGatesHeader)) {
							parseOutputGate(mkn, lineCounter, line);
						} else {
							throw new SerializationException(lineCounter, "Data out of section");
						}
					} catch (SerializationException sex) {
						throw sex;
					} catch (MarkovException mex) {
						throw mex;
					} catch (Exception ex) {
						throw new SerializationException(lineCounter, "Parsing error");
					}
				}
			}
		} finally {
			sc.close();
		}
		
		
		mkn.validate(System.err);
		
		return mkn;
	}
	
	private static void parseNameID(MarkovNetwork mkn, int lineCount, String line) {
		String[] segs = line.split(";");
		int id = Integer.parseInt(segs[0].split("=")[1]);
		String name = segs[1].split("=")[1].trim();
		
		mkn.getNameIDs().put(id, name);
	}
	
	private static void parseInputPoint(MarkovNetwork mkn, int lineCount, String line) {
		String[] segs = line.split(";");
		int id = Integer.parseInt(segs[0].split("=")[1]);
		boolean state = segs[1].split("=")[1].trim().equals("1") ? true : false;
		
		mkn.addInputPoint(new ConstInputPoint(id, state));
	}
	
	private static void parseGate(MarkovNetwork mkn, int lineCount, String line) throws MarkovException {
		String[] segs = line.split(";");
		int id = Integer.parseInt(segs[0].split("=")[1]);
		
		int[] inputNodes = null;
		if (segs.length > 1) {
			inputNodes = Utils.string2IntArray(segs[1].split("=")[1].trim());
		}
		Gate g = new Gate(mkn, id, inputNodes);
		mkn.addGate(g, false);
		
		if (segs.length > 2) {
			String[] pair = segs[2].split("=");
			if (pair[0].trim().equals("Relation")) {
				g.setRelation(Utils.string2IntArray(pair[1]));
			} else {
				g.setTransitionVector(Utils.string2BooleanArray(pair[1]));
			}
		}
		
		if (segs.length > 3) {
			String logic = segs[3].split("=")[1].trim();
			g.setTransitionVector(Utils.string2BooleanArray(logic));
		}
	}
	
	private static void parseOutputGate(MarkovNetwork mkn, int lineCount, String line) throws MarkovException {
		Node n = mkn.getNodeByID(Integer.parseInt(line.split("=")[1]));
		mkn.setOutput((Gate)n);
	}
	
	private static void parseInitialStates(MarkovNetwork mkn, int lineCount, String line) {
		String[] segs = line.split(";");
		int id = Integer.parseInt(segs[0].split("=")[1]);
		boolean value = segs[1].split("=")[1].trim().equals("1") ? true : false;
		
		Node n = mkn.getNodeByID(id);
		((Gate)n).presetState(value);
	}
	
	public static String serialize(MarkovNetwork mkn) throws MarkovException {
		StringBuffer sb = new StringBuffer();
		sb.append(nodeIDsHeader).append("\n");
		for (Integer id : mkn.getNameIDs().keySet()) {
			sb.append("ID=").append(id).append(";Name=").append(mkn.getNameIDs().get(id)).append("\n");
		}
		sb.append("\n");
		
		sb.append(inputPointsHeader).append("\n");
		for (InputPoint ip : mkn.getInputPoints()) {
			sb.append("ID=").append(ip.getID()).append(";State=").append(ip.getState() ? '1' : '0').append("\n");
		}
		sb.append("\n");
		
		sb.append(gatesHeader).append("\n");
		for (Gate g : mkn.getGates()) {
			sb.append(g.toString()).append("\n");
		}
		sb.append("\n");
		
		sb.append(outputGatesHeader).append("\n");
		for (Gate g : mkn.getOutputNodes()) {
			sb.append("ID=").append(g.getID()).append("\n");
		}
		sb.append("\n");
		
		return sb.toString();
	}
}
