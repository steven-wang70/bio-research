package bioinfo.markov;

public class ConstInputPoint extends InputPoint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private boolean value;
	public ConstInputPoint(int id, boolean v) {
		this._id = id;
		value = v;
	}
	
	public int getID() {
		return _id;
	}
	
	public boolean getState() throws MarkovException {
		return value;
	}

	public boolean isStateReady() {
		return true;
	}

	public void moveNext() {
	}

	public void reset() {
	}

}
