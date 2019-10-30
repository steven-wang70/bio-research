package bioinfo.markov;

public class SerializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String _msg;
	SerializationException(int line, String msg) {
		_msg = msg;
	}
	
	@Override
	public String getMessage() {
		return _msg;
	}
}
