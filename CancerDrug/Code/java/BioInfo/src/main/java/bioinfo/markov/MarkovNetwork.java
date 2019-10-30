package bioinfo.markov;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MarkovNetwork implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<InputPoint> inputPoints = new ArrayList<InputPoint>();
	private List<Gate> gates = new ArrayList<Gate>();
	private List<Gate> outputPoints = new ArrayList<Gate>();
	private HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
	private HashMap<Integer, String> nameIds = new HashMap<Integer, String>();
	private boolean _gatesSorted = false;
	
	public MarkovNetwork() {
	}
	
	List<InputPoint> getInputPoints() {
		return inputPoints;
	}
	
	public List<Gate> getGates() {
		return gates;
	}

	public HashMap<Integer, String> getNameIDs() {
		return nameIds;
	}
	
	public Node getNodeByID(int id) {
		if (this.nodes.containsKey(id)) {
			return this.nodes.get(id);
		} else {
			return null;
		}
	}
	
	public void addInputPoint(InputPoint ip) {
		inputPoints.add(ip);
		nodes.put(ip.getID(), ip);
		_gatesSorted = false;
	}
	
	public void addGate(Gate g, boolean isOutput) {
		nodes.put(g.getID(), g);
		gates.add(g);
		if (isOutput) {
			outputPoints.add(g);
		}
		_gatesSorted = false;
	}
	
	public boolean validate(PrintStream log) {
		boolean result = true;
		for (Gate g : gates) {
			result = result && g.validate(log);
		}
		
		return result;
	}
	
	public void setInitialStates(boolean[] states) throws MarkovException {
		if (states.length != gates.size()) {
			throw new MarkovException();
		}
		
		for (int i = 0; i < gates.size(); i++) {
			gates.get(i).presetState(states[i]);
		}
	}
	
	public void moveNext() throws MarkovException {
		for (Gate g : gates) {
			g.calculate();
		}
		
		for (Node sh : nodes.values()) {
			sh.moveNext();
		}
	}
	
	public String getOutputStates() {
		sortGates();
		StringBuffer sb = new StringBuffer();
		for (Gate g : outputPoints) {
			if (!g.isStateReady()) {
				sb.append('X');
			} else {
				try {
					if (g.getState()) {
						sb.append('1');
					} else {
						sb.append('0');
					}
				} catch (MarkovException e) {
				}
			}
		}
		
		return sb.toString();
	}
	
	public String getFullGateStates() {
		sortGates();
		StringBuffer sb = new StringBuffer();
		for (Gate g : gates) {
			if (!g.isStateReady()) {
				sb.append('X');
			} else {
				try {
					if (g.getState()) {
						sb.append('1');
					} else {
						sb.append('0');
					}
				} catch (MarkovException e) {
				}
			}
		}
		
		return sb.toString();
	}
	
	public void resetStates() {
		for (Node n : nodes.values()) {
			n.reset();
		}
	}

	void setOutput(Gate g) throws MarkovException {
		if (!gates.contains(g)) {
			throw new MarkovException();
		}
		
		if (!outputPoints.contains(g)) {
			outputPoints.add(g);
			_gatesSorted = false;
		}
	}
	
	void sortGates() {
		if (!_gatesSorted) {
			NodeComparator c = new NodeComparator();
			inputPoints.sort(c);
			gates.sort(c);
			outputPoints.sort(c);
			
			_gatesSorted = true;
		}
	}
	
	private static class NodeComparator implements Comparator<Node> {

		public int compare(Node n1, Node n2) {
			if (n1.getID() < n2.getID()) {
				return -1;
			} else if (n1.getID() > n2.getID()) {
				return 1;
			} else {
				return 0;
			}
		}
		
	}
	
	public List<Gate> getOutputNodes() {
		sortGates();
		return outputPoints;
	}
}
