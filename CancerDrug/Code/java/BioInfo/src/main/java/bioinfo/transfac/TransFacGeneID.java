package bioinfo.transfac;

import java.util.List;

public class TransFacGeneID {
	private String _transFacID;
	private List<String> _aliases;
	
	public String getTransFacID() {
		return _transFacID;
	}
	public void setTransFacID(String transFacID) {
		this._transFacID = transFacID;
	}
	public List<String> getAliases() {
		return _aliases;
	}
	public void setAliases(List<String> aliases) {
		this._aliases = aliases;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(_transFacID);
		sb.append(";");
		if (_aliases != null) {
			boolean alreadyHasOne = false;
			for (String a : _aliases) {
				if (alreadyHasOne) {
					sb.append(",");
				}
				
				sb.append(a);
				alreadyHasOne = true;
			}
		}
		
		return sb.toString();
	}
}
