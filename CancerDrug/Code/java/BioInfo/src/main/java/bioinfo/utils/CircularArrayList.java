package bioinfo.utils;

import java.util.ArrayList;
import java.util.Collection;


public class CircularArrayList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8636137746133434423L;
	
	private int _length;
	
	public CircularArrayList(int length) {
		super();
		_length = length < 2 ? 2 : length;
	}

	@Override
	public void add(int arg0, T arg1) {
	}

	@Override
	public boolean add(T arg0) {
		if (super.size() < _length) {
			return super.add(arg0);
		}
		
		super.remove(0);
		return super.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		return false;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		return false;
	}
	
}