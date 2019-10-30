package bioinfo.challenge;

import java.io.BufferedWriter;
import java.util.List;

import bioinfo.nn.AnswersLoader;
import bioinfo.nn.DrugSynergy;
import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class SynergyBenchmark {
	public static void main(String[] args) {
		Settings.loadSettings(args[0]);
		
		String[] processCellTypes = Settings.instance().getSetting("General", "ProcessCellTypes").split(",");
		
		for (String cellType : processCellTypes) {
			benchmark(cellType);
		}
	}
	
	private static void benchmark(String cellType) {
		String defaultOutFolder =  Settings.instance().getSetting("General", "DefaultOutFolder");
		String actualSynergyFile = Settings.instance().getSetting("General", "ActualSynergyFile");
		String computedSynergyFile = Settings.instance().getSetting("General", "ComputedSynergyFile");
		String benchmarkFile = Settings.instance().getSetting("General", "SynergyBenchmarkFile");
		
		ActualSynergyLoader asl = new ActualSynergyLoader();
		asl.loadFromFile(actualSynergyFile);
		List<DrugSynergy> actualSynergies = asl.synergies;
		
		ComputedSynergyLoader csl = new ComputedSynergyLoader();
		csl.loadFromFile(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + computedSynergyFile);
		List<DrugSynergy> computedSynergies = csl.synergies;
		
		BufferedWriter bw = Utils.openFileToWrite(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + benchmarkFile);
		Utils.println("CellLine,DrugName1,DrugName2,ActualSynergy,ComputedSynergy", bw);

		for (DrugSynergy as : actualSynergies) {
			for (DrugSynergy cs : computedSynergies) {
				if (as.CellLine.equals(cs.CellLine) &&
					as.Drug1.equals(cs.Drug1) &&
					as.Drug2.equals(cs.Drug2)) {
					Utils.println(toStringBenchmark(as, cs), bw);
					break;
				}
			}
		}
		
		Utils.close(bw);
	}
	
	private static String toStringBenchmark(DrugSynergy as, DrugSynergy cs) {
		StringBuffer sb = new StringBuffer();
		sb.append(as.CellLine).append(",").append(as.Drug1).append(",").append(as.Drug2).append(",").append(as.Synergy).append(",").append(cs.Synergy);
		return sb.toString();
	}
}
