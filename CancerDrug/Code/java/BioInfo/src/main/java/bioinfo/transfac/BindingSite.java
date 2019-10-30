package bioinfo.transfac;

public class BindingSite {
	
	public BindingSite(String bindingGendId, int quality, String transFacId) {
		super();
		this.bindingGendId = bindingGendId;
		this.quality = quality;
		this.transFacId = transFacId;
	}
	
	private String bindingGendId;
	private int quality;
	private String transFacId;
	
	public String getBindingGendId() {
		return bindingGendId;
	}
	public int getQuality() {
		return quality;
	}
	public String getTransFacId() {
		return transFacId;
	}
	@Override
	public String toString() {
		if (transFacId == null) {
			return String.format("%1$s;%2$d", bindingGendId, quality);
		} else {
			return String.format("%1$s;%2$d;%3$s", bindingGendId, quality, transFacId);
		}
	}
	
	
}
