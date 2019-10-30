package bioinfo.challenge;

public class DrugResponse {
	private String _drugName;
	private String _cellLine;
	private double _maxConcentration;
	private double _IC50;
	private double _H;
	private double _Einf; // The minimum survival
	
	public DrugResponse(String drugName, String cellLine, double maxConcentration, double ic50, double h, double einf) {
		_drugName = drugName;
		_cellLine = cellLine;
		_maxConcentration = maxConcentration;
		_IC50 = ic50;
		_H = h;
		_Einf = einf;
	}
	
	public String getDrugName() {
		return _drugName;
	}
	
	public String getCellLine() {
		return _cellLine;
	}
	
	public double getMaxConcentration() {
		return _maxConcentration;
	}
	
	public double getIC50 () {
		return _IC50;
	}
	
	public double getH() {
		return _H;
	}
	
	public double getEinf() {
		return _Einf;
	}
	
	public static int NormalizeLevel = 0;
	
	// E(a) = 100 + (Einf - 100) / (1 + (IC50 / a) ^ H)
	public double normalizedDose2Survival(int doseIndex) {
		if (doseIndex == 0) {
			return 1;
		} else if (doseIndex == NormalizeLevel) {
			return _Einf;
		} else {
			double dose = Math.tan((1.0 * doseIndex / NormalizeLevel) * (Math.PI / 2)); // This is a normalized value, when IC50, the value is 1.
			if (Double.isNaN(1 + (_Einf - 1) / (1 + Math.pow(1 + 1 / dose, _H)))) {
				System.err.println("Error");
			}
			return 1 + (_Einf - 1) / (1 + Math.pow(1 / dose, _H));
		}
	}
	
	public static double additiveDose2Survival(DrugResponse dr1, int doseIndex1, DrugResponse dr2, int doseIndex2) {
		return dr1.normalizedDose2Survival(doseIndex1) * dr2.normalizedDose2Survival(doseIndex2);
	}
	
	public static String ResponseDataKey(String cellLine, String drugName) {
		return cellLine + "/" + drugName;
	}
}
