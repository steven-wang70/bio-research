package bioinfo.challenge;

public class DrugAndTarget {
	public String DrugName;
	public String[] TargetNames;
	public boolean Activatable;
	
	public DrugAndTarget(String drugName, String[] targetNames, boolean activatable) {
		DrugName = drugName;
		TargetNames = targetNames;
		Activatable = activatable;
	}
}
