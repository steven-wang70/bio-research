package bioinfo.regulation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GeneDictionary {
	private HashMap<Integer, String> _id2names = new HashMap<Integer, String>();
	private HashMap<String, Integer> _name2ids = new HashMap<String, Integer>();
	public static final int InvalidID = -1;
	
	public GeneDictionary(List<String> names) {
		int counter = 1;
		Collections.sort(names);
		for (String name : names) {
			if (!_name2ids.containsKey(name)) {
				_name2ids.put(name, counter);
				_id2names.put(counter, name);
				counter++;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public GeneDictionary(HashMap<Integer, String> idNames) {
		_id2names = (HashMap<Integer, String>) idNames.clone();
		for (int key : _id2names.keySet()) {
			_name2ids.put(_id2names.get(key), key);
		}
		
	}
	
	public int getIDByName(String name) {
		if (_name2ids.containsKey(name)) {
			return _name2ids.get(name);
		} else {
			return InvalidID;
		}
	}
	
	public String getNameByID(int id) {
		if (_id2names.containsKey(id)) {
			return _id2names.get(id);
		} else {
			return null;
		}
	}
	
	public Collection<Integer> gteAllIDs() {
		return _name2ids.values();
	}
	
	public Collection<String> gteAllNames() {
		return _id2names.values();
	}
}
