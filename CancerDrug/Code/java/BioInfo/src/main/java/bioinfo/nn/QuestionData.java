package bioinfo.nn;

public class QuestionData {
	String CellLine;
	String DrugName1;
	String DrugName2;
	
	double[] Input;
	double AdditionalSurvival;
	
	QuestionData(String cellLine, String drugName1, String drugName2, double[] input, double additionalSurvival) {
		CellLine = cellLine;
		DrugName1 = drugName1;
		DrugName2 = drugName2;
		Input = input;
		AdditionalSurvival = additionalSurvival;
	}
}
