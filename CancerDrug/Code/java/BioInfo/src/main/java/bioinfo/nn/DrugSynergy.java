package bioinfo.nn;

import java.io.BufferedWriter;

import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class DrugSynergy {
	public String CellLine;
	public String Drug1;
	public String Drug2;
	public double Synergy;
	
	public DrugSynergy(String cellLine, String drug1, String drug2, double synergy) {
		CellLine = cellLine;
		
		if (drug1.compareTo(drug2) > 0) {
			Drug1 = drug2;
			Drug2 = drug1;
		} else {
			Drug1 = drug1;
			Drug2 = drug2;
		}
		
		Synergy = synergy;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(CellLine).append(",").append(Drug1).append(",").append(Drug2).append(",").append(Synergy);
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		Settings.loadSettings(args[0]);
		
		String[] processCellTypes = Settings.instance().getSetting("General", "ProcessCellTypes").split(",");
		
		for (String cellType : processCellTypes) {
			summary(cellType);
		}
	}
	
	private static void summary(String cellType) {
		String defaultOutFolder =  Settings.instance().getSetting("General", "DefaultOutFolder");
		String answerFile = Settings.instance().getSetting("NeuroNetwork", "AnswerFile");
		String computedSynergyFile = Settings.instance().getSetting("General", "ComputedSynergyFile");
		
		AnswersLoader al = new AnswersLoader();
		al.loadFromFile(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + answerFile);
		
		BufferedWriter bw = Utils.openFileToWrite(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + computedSynergyFile);
		Utils.println("CellLine,DrugName1,DrugName2,Synergy", bw);

		for (DrugSynergy ds : al.synergies) {
			Utils.println(ds.toString(), bw);
		}
		
		Utils.close(bw);
	}
}
