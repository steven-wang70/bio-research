package bioinfo.markov;

public class SequenceInputPoint extends InputPoint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private boolean[] values;
	private int index;
	
	public SequenceInputPoint(int id, boolean[] v) {
		this._id = id;
		values = v;
		index = 0;
	}
	
	public int getID() {
		return _id;
	}

	public boolean getState() throws MarkovException {
		if (index >= values.length) {
			throw new MarkovException();
		}
		
		return values[index];
	}

	public boolean isStateReady() {
		if (index < values.length) {
			return true;
		} else {
			return false;
		}
	}

	public void moveNext() {
		index++;
	}

	public void reset() {
		index = 0;
	}

}
