package bioinfo.nn;

import java.util.List;

import org.encog.ml.data.basic.BasicMLDataSet;

public class TrainingDataSet extends BasicMLDataSet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6715875570803602755L;
	
	public TrainingDataSet(List<double[]> inputDatas, List<Double> idealResults) {
		super(inputsList2Array(inputDatas), idealResultsList2Array(idealResults));
	}
	
	private static double[][] inputsList2Array(List<double[]> inputDatas) {
		double[][] inputDataArray = new double[inputDatas.size()][];
		inputDatas.toArray(inputDataArray);
		
		return inputDataArray;
	}
	
	private static double[][] idealResultsList2Array(List<Double> idealResults) {
		double[][] idealResultArray = new double[idealResults.size()][1];
		for (int i = 0; i < idealResults.size(); i++) {
			idealResultArray[i][0] = idealResults.get(i);
		}
		
		return idealResultArray;
	}
}
