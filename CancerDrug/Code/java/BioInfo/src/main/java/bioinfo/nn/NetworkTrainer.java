package bioinfo.nn;

import java.io.BufferedWriter;
import java.io.File;
import java.util.List;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.concurrency.EngineConcurrency;

import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class NetworkTrainer {
	private static void training(String cellType) {
		String trainingDataFile = Settings.instance().getSetting("ExpressionOnDrug", "TrainingDataFile");
		String defaultOutFolder =  Settings.instance().getSetting("General", "DefaultOutFolder");
		String saveFile = Settings.instance().getSetting("NeuroNetwork", "PersistenceFile");

		double trainingError = Settings.instance().getSetting("NeuroNetwork", "TrainingError", Double.class);
		
		TrainingDataFileLoader tdfl = new TrainingDataFileLoader();
		tdfl.loadFromFile(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + trainingDataFile);
		TrainingDataSet tds = new TrainingDataSet(tdfl.InputDatas, tdfl.IdealResults);
		
		BasicNetwork network = new BasicNetwork();
		
		network.addLayer(new BasicLayer(null, true, tds.getInputSize()));
		
		String[] itemCounts =Settings.instance().getSetting("NeuroNetwork", "HiddenLayersItems").split(",");
		for (int i = 0; i < itemCounts.length; i++) {
			int itemCount =Integer.parseInt(itemCounts[i]);
			System.out.println("Hidden Layer: " + Integer.toString(i) + " Items: " +  Integer.toString(itemCount));
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, itemCount));
		}
		
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		
		network.getStructure().finalizeStructure();
		network.reset();
		
		MLTrain train = new ResilientPropagation(network, tds);
		
		int round = 1;
		do {
			train.iteration();
			System.out.println("Round: " + round + " Error: " + train.getError());
			round++;
		} while (train.getError() > trainingError);
		train.finishTraining();
		EngineConcurrency.getInstance().shutdown(1); // Close all theads in the pool after 1 second
		
		System.out.println("Network trained to error:" + network.calculateError(tds));
		EncogDirectoryPersistence.saveObject(new File(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + saveFile), network);
		verify(network, tds, tdfl.pureVerificationDatas, cellType);
	}
	
	private static void verify(BasicNetwork network, TrainingDataSet dataset, List<double[]> pureVerification, String cellType) {
		String defaultOutFolder =  Settings.instance().getSetting("General", "DefaultOutFolder");
		String verificationFile = Settings.instance().getSetting("NeuroNetwork", "VerificationFile");
		
	
		BufferedWriter bw = Utils.openFileToWrite(defaultOutFolder + Utils.PathSeparator + cellType + Utils.PathSeparator  + verificationFile);
		
		Utils.println("Ideal, Computed, Verification", bw);
		
		double[] output = new double[1];
		double[] outputPureVerification = new double[1];
		for (int i = 0; i < dataset.getRecordCount(); i++) {
			network.compute(dataset.get(i).getInputArray() , output);
			network.compute(pureVerification.get(i) , outputPureVerification);
			Utils.println(Double.toString(dataset.get(i).getIdealArray()[0]) + "," + Double.toString(output[0]) + "," + Double.toString(outputPureVerification[0]), bw);
		}

		Utils.close(bw);
	}
	
	public static void main(String[] args) {
		Settings.loadSettings(args[0]);
		
		String[] processCellTypes = Settings.instance().getSetting("General", "ProcessCellTypes").split(",");
		
		for (String cellType : processCellTypes) {
			training(cellType);
		}
	}

}
