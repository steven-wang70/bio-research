package bioinfo.nn;

import java.io.BufferedWriter;
import java.io.File;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.concurrency.EngineConcurrency;

import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class QuestionExecutor {
	private static void compute(String cellType) {
		String questionDataFile = Settings.instance().getSetting("ExpressionOnDrug", "QuestionDataFile");
		String defaultOutFolder =  Settings.instance().getSetting("General", "DefaultOutFolder");
		String saveFile = Settings.instance().getSetting("NeuroNetwork", "PersistenceFile");

		QuestionDataFileLoader qdfl = new QuestionDataFileLoader();
		qdfl.loadFromFile(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + questionDataFile);
		
		BasicNetwork network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + saveFile));
		
		String answerFile = Settings.instance().getSetting("NeuroNetwork", "AnswerFile");
		BufferedWriter bw = Utils.openFileToWrite(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + answerFile);
		
		Utils.println("CellLine,DrugName1,DrugName2,AdditionalSurvival,SynergySurvival", bw);
		
		QuestionData qd = null;
		double[] output = new double[1];
		do {
			qd = qdfl.loadData();
			if (qd != null) {
				network.compute(qd.Input, output);
				Utils.println(qd.CellLine + "," + qd.DrugName1 + "," + qd.DrugName2 + "," + Double.toString(qd.AdditionalSurvival) + "," + Double.toString(output[0]), bw);
			}
		} while (qd != null);

		Utils.close(bw);
	}
	
	public static void main(String[] args) {
		Settings.loadSettings(args[0]);
		
		String[] processCellTypes = Settings.instance().getSetting("General", "ProcessCellTypes").split(",");
		
		for (String cellType : processCellTypes) {
			compute(cellType);
		}
	}

}
