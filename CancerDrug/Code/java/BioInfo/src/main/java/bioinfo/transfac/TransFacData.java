package bioinfo.transfac;

import java.util.List;

public class TransFacData {

	private String _geneId;
	private List<String> _aliases;
	private List<BindingSite> _bindingSites;
	
	public String getGeneId() {
		return _geneId;
	}
	public void setGeneId(String geneId) {
		this._geneId = geneId;
	}
	public List<String> getAliases() {
		return _aliases;
	}
	public void setAliases(List<String> aliases) {
		_aliases = aliases;
	}
	public List<BindingSite> getBindingSites() {
		return _bindingSites;
	}
	public void setBindingSites(List<BindingSite> bindingSites) {
		this._bindingSites = bindingSites;
	}
	
	@Override
	public String toString() {
		StringBuffer sb1 = new StringBuffer();
		sb1.append(_geneId);
		sb1.append(";");
		if (_aliases != null) {
			boolean alreadyHasOne = false;
			for (String a : _aliases) {
				if (alreadyHasOne) {
					sb1.append(",");
				}
				
				sb1.append(a);
				alreadyHasOne = true;
			}
		}
		sb1.append(";");
		
		StringBuilder sb2 = new StringBuilder();
		boolean firstOne = true;
		for (BindingSite bs : _bindingSites) {
			if (!firstOne) {
				sb2.append("\n");
			}
			
			sb2.append(sb1.toString());
			if (bs.getTransFacId() == null) {
				sb2.append(String.format("%1$s;%2$d", bs.getBindingGendId(), bs.getQuality()));
			} else {
				sb2.append(String.format("%1$s;%2$d;%3$s", bs.getBindingGendId(), bs.getQuality(), bs.getTransFacId()));
			}
			
			firstOne = false;
		}
		
		return sb2.toString();
	}
}
