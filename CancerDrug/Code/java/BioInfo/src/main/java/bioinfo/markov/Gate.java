package bioinfo.markov;

import java.io.PrintStream;

import bioinfo.utils.Utils;

public class Gate implements Node, java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	boolean state1, state2;
	boolean state1Ready, state2Ready;
	boolean stateFixed = false;
	private int[] inputIds;
	private int[] _relation = null;
	private boolean[] transitionVector;
	private MarkovNetwork _container;
	
	private static final int InvalidStateIndex = -1;
	
	public void setRelation(int[] s) {
		_relation = s;
	}
	public int[] getRelation() {
		return _relation;
	}
	
	public int getID() {
		return _id;
	}

	public int getInputCount() {
		if (inputIds == null) {
			return 0;
		}
		
		return inputIds.length;
	}
	
	public int getTransitionLength() {
		if (inputIds == null || inputIds.length == 0) {
			return 0;
		}
		
		return 1 << inputIds.length;
	}
	
	public boolean getState() throws MarkovException {
		if (!state1Ready) {
			throw new MarkovException();
		}
		
		return state1;
	}

	public void presetState(boolean v) {
		state1 = v;
		state1Ready = true;
		
		state2 = false;
		state2Ready = false;
	}

	public void presetState(boolean v, boolean fixed) {
		state1 = v;
		state1Ready = true;
		
		state2 = false;
		state2Ready = false;
		
		stateFixed = fixed;
	}
	
	public void reset() {
		state1 = false;
		state2 = false;
		state1Ready = false;
		state2Ready = false;
		stateFixed = false;
	}
	
	public boolean isStateReady() {
		return state1Ready;
	}

	public void moveNext() {
		if (state2Ready) {
			state1 = state2;
			state1Ready = state2Ready;
			
			state2 = false;
			state2Ready = false;
		}
	}

	
	public Gate(MarkovNetwork container, int id, int[] inputPoints) throws MarkovException {
		if (inputPoints != null && inputPoints.length > 31) {
			throw new MarkovException();
		}
		
		this._container = container;
		this._id = id;
		inputIds = inputPoints;
		
		state1Ready = false;
		state1 = false;
		state2Ready = false;
		state2 = false;
	}
	
	public boolean isInitialized() {
		if (transitionVector == null) {
			return false;
		}

		return true;
	}
	
	public boolean validate(PrintStream log) {
		if (inputIds == null) {
			return true;
		}
		
		boolean result = true;
		
		for (int id : inputIds) {
			result = result && (_container.getNodeByID(id) == null ? false : true);
		}
		
		return result;
	}
	
	public void setTransitionVector(boolean[] trans) throws MarkovException {
		if (trans.length != getTransitionLength()) {
			throw new MarkovException();
		}
		
		transitionVector = trans;
	}
	
	public boolean[] getTransitionVector() {
		return transitionVector;
	}
	
	private boolean isInputReady() {
		if (inputIds == null) {
			return true;
		}
		
		for (int id : inputIds) {
			if (!_container.getNodeByID(id).isStateReady()) {
				return false;
			}
		}
		
		return true;
	}
	
	private int generateInputStateIndex() throws MarkovException {
		if (inputIds == null) {
			return InvalidStateIndex;
		}
		
		int stateIndex = 0;
		
		for (int i = 0; i < inputIds.length; i++) {
			stateIndex <<= 1;
			stateIndex += (_container.getNodeByID(inputIds[i]).getState() ? 1 : 0);
		}
	
		return (transitionVector.length - 1) - stateIndex;
	}
	
	public void calculate() throws MarkovException {
		if (stateFixed) {
			return;
		}
		
		if (getInputCount() == 0) {
			return;
		}
		
		if (!isInitialized()) {
			throw new MarkovException();
		}
		
		if (!isInputReady()) {
			throw new MarkovException();
		}
		
		int stateIndex = generateInputStateIndex();
		state2 = transitionVector[stateIndex];
		state2Ready = true;
	}

	
	@Override
	public String toString() {
		return toString(Utils.booleanArray2String(transitionVector));
	}
	
	public String toString(String newTransition) {
		StringBuffer sb = new StringBuffer();
		sb.append("ID=").append(getID());
		
		if (inputIds != null) {
			sb.append(";InputNode=").append(Utils.intArray2String(inputIds));
			
			if (_relation != null) {
				sb.append(";Relation=").append(Utils.intArray2String(_relation));
			}
			
			if (newTransition != null && newTransition.length() > 0) {
				sb.append(";Logic=").append(newTransition);
			}
		}
		return sb.toString();
	}
}
