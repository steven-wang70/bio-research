package bioinfo.markov;

public interface Node {
	int getID();
	boolean getState() throws MarkovException;
	boolean isStateReady();
	void moveNext();
	void reset();
}
