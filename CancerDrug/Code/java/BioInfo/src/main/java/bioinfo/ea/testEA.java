package bioinfo.ea;

import java.util.HashMap;
import java.util.Map;

import bioinfo.markov.Gate;
import bioinfo.markov.MarkovException;
import bioinfo.markov.MarkovNetwork;

public class testEA {
	
	public static void main(String[] args) {
		System.out.println(testEAOutputs().toString());
	}
	
	private static EAResult testEAOutputs() {
		boolean[][] scenarios = null;
		String[] expectedResults = null;
		
		scenarios = new boolean[][] {
				{false, false, false, false},
				{false, false, false, true},
				{false, false, true, false},
				{false, false, true, true},
				
				{false, true, false, false},
				{false, true, false, true},
				{false, true, true, false},
				{false, true, true, true},
				
				{true, false, false, false},
				{true, false, false, true},
				{true, false, true, false},
				{true, false, true, true},
				
				{true, true, false, false},
				{true, true, false, true},
				{true, true, true, false},
				{true, true, true, true}
		};
		
		expectedResults = new String[] {"0000", "1001", "1000", "1000", "0100", "1101", "1100", "1100", "0100", "1101", "1100", "1100", "0110", "1111", "1110", "1110"};
		
		try {
			EAResult eaResult = new EAResult(1000, 0.0001, 100, 0.99);
			EvolutionAlgorithm ea = new testEAOutputs(eaResult, scenarios, expectedResults);
			
			ea.evolute();
			return eaResult;
		} catch (EAException ex1) {
			
		} catch (MarkovException ex2) {
			
		}
		
		return null;
	}
	
	private static EAResult testEAAttractors() {
		boolean[][] scenarios = null;
		String[][] expectedResults = null;
		
		scenarios = new boolean[][] {
				{false, false, false, false},
				{false, false, false, true},
				{false, false, true, false},
				{false, false, true, true},
				
				{false, true, false, false},
				{false, true, false, true},
				{false, true, true, false},
				{false, true, true, true},
				
				{true, false, false, false},
				{true, false, false, true},
				{true, false, true, false},
				{true, false, true, true},
				
				{true, true, false, false},
				{true, true, false, true},
				{true, true, true, false},
				{true, true, true, true}
		};
		
		expectedResults = new String[][] {{"0000"}, {"0100"}, {"1110"}, {"1100", "0110"}};
		
		try {
			EAResult eaResult = new EAResult(1000, 0.0001, 100, 0.99);
			EvolutionAlgorithm ea = new testEAAttractors(eaResult, scenarios, expectedResults);
			
			ea.evolute();
			return eaResult;
		} catch (EAException ex1) {
			
		} catch (MarkovException ex2) {
			
		}
		
		return null;
	}

	private static EAResult analyzeDistribution() {
		Map<String, Integer> result = new HashMap<String, Integer>();
		EAResult ear = null;
		for (int i = 0; i < 3000; i++) {
			ear = testEAAttractors();
			System.out.println(ear.toString());
			if (!result.containsKey(ear.getLogic())) {
				result.put(ear.getLogic(), 0);
			}
			
			result.put(ear.getLogic(), result.get(ear.getLogic()) + 1);
		}
		
		for (Integer i : result.values()) {
			System.out.println(i);
		}
		
		return ear;
	}
	
	private static EAResult testEAInputsAttractors() {
		boolean[][] scenarios = null;
		String[][] expectedResults = null;
		
		scenarios = new boolean[][] {
				{false, false, false, false},
///				{false, false, false, true},
				{false, false, true, false},
///				{false, false, true, true},
				
//				{false, true, false, false},
				{false, true, false, true},
//				{false, true, true, false},
///				{false, true, true, true},
				
//				{true, false, false, false},
//				{true, false, false, true},
				{true, false, true, false},
///				{true, false, true, true},
				
//				{true, true, false, false},
//				{true, true, false, true},
//				{true, true, true, false},
//				{true, true, true, true}
		};
		
		expectedResults = new String[][] {
				{"0000"}, // 0000
///				{"1110"}, // 0001
				{"0100"}, // 0010
///				{"0100"}, // 0011
				
//				{"0100"}, // 0100
				{"1110"}, // 0101
//				{"1100", "0110"}, // 0110
///				{"1100", "0110"}, // 0111
				
//				{"0100"}, // 1000
//				{"1110"}, // 1001
				{"1100", "0110"}, // 1010
///				{"1100", "0110"}, // 1011
				
//				{"1100", "0110"}, // 1100
//				{"1110"}, // 1101
//				{"1110"}, // 1110
//				{"1110"}  // 1111
				};
		
		try {
			EAResult eaResult = null;
			eaResult = new EAResult(1000, 0.003, 500, 0.95);
			EvolutionAlgorithm ea = new testEAInputsAttractors(eaResult, scenarios, expectedResults, 0.1);
			
			ea.evolute();
			return eaResult;
		} catch (EAException ex1) {
			
		} catch (MarkovException ex2) {
			
		}
		
		return null;
	}
	
	private static EAResult analyzeEAInputsAttractors() {
		boolean[][] scenarios = null;
		String[][] expectedResults = null;
		
		scenarios = new boolean[][] {
				{false, false, false, false},
///				{false, false, false, true},
				{false, false, true, false},
///				{false, false, true, true},
				
//				{false, true, false, false},
				{false, true, false, true},
//				{false, true, true, false},
///				{false, true, true, true},
				
//				{true, false, false, false},
//				{true, false, false, true},
				{true, false, true, false},
///				{true, false, true, true},
				
//				{true, true, false, false},
//				{true, true, false, true},
//				{true, true, true, false},
//				{true, true, true, true}
		};
		
		expectedResults = new String[][] {
				{"0000"}, // 0000
///				{"1110"}, // 0001
				{"0100"}, // 0010
///				{"0100"}, // 0011
				
//				{"0100"}, // 0100
				{"1110"}, // 0101
//				{"1100", "0110"}, // 0110
///				{"1100", "0110"}, // 0111
				
//				{"0100"}, // 1000
//				{"1110"}, // 1001
				{"1100", "0110"}, // 1010
///				{"1100", "0110"}, // 1011
				
//				{"1100", "0110"}, // 1100
//				{"1110"}, // 1101
//				{"1110"}, // 1110
//				{"1110"}  // 1111
				};
		
		try {
			EAResult eaResult = null;
			int tryCount = 200;
			String expectedLogic = "ID=1;Inputs=3,4;Transition=0111\nID=2;Inputs=1,2;Transition=0111\nID=3;Inputs=1,2;Transition=0001\nID=4;Inputs=3,4;Transition=0100\n";
			double[] mutationRates = new double[] {0.003, 0.0033, 0.0036};
			double[] lengthWeights = new double[] {0.06, 0.08, 0.1, 0.12, 0.14};
			int[] results = new int[lengthWeights.length];
			int[] generations = new int[lengthWeights.length];
			for (int m = 0; m < mutationRates.length; m++) {
				System.out.println("MutationRate=" + Double.toString(mutationRates[m]));
				for (int i = 0; i < lengthWeights.length; i++) {
					for (int j = 0; j < tryCount; j++) {
						eaResult = new EAResult(300, mutationRates[m], 500, 0.95);
						EvolutionAlgorithm ea = new testEAInputsAttractors(eaResult, scenarios, expectedResults, lengthWeights[i]);
						
						ea.evolute();
						if (eaResult.getLogic().equals(expectedLogic)) {
//							System.out.println(Integer.toString(i) + " " + Integer.toString(j));
							results[i]++;
							generations[i] += eaResult.getActualGeneration();
						}
					}
				}
				
				for (int i = 0; i < lengthWeights.length; i++) {
					if (results[i] == 0) {
						continue;
					}
					System.out.println("Weight=" + Double.toString(lengthWeights[i]) + " Success=" + Integer.toString(results[i]) + " Generation=" + Integer.toString(generations[i] / results[i]));
				}
			}
			
			return eaResult;
		} catch (EAException ex1) {
			
		} catch (MarkovException ex2) {
			
		}
		
		return null;
	}
	
}

class testEAOutputs extends EvolutionAlgorithmOutputs {

	public testEAOutputs(EAResult eaResult,	boolean[][] scenarios, String[] expectedResults) throws EAException, MarkovException {
		super(eaResult, scenarios, expectedResults);
	}

	@Override
	protected MarkovNetwork templateMKN() {
		try {
			MarkovNetwork mkn = new MarkovNetwork();
			Gate g1 = new Gate(mkn, 1, new int[]{3,4});
			mkn.addGate(g1, true);
			
			Gate g2 = new Gate(mkn, 2, new int[]{1,2});
			mkn.addGate(g2, true);
			
			Gate g3 = new Gate(mkn, 3, new int[]{1,2});
			mkn.addGate(g3, true);
			
			Gate g4 = new Gate(mkn, 4, new int[]{3,4});
			mkn.addGate(g4, true);
			
			return mkn;
		} catch (MarkovException ex2) {
			
		}
		
		return null;
	}
	
}

class testEAAttractors extends EvolutionAlgorithmAttractors {

	public testEAAttractors(EAResult eaResult,	boolean[][] scenarios, String[][] expectedResults) throws EAException, MarkovException {
		super(eaResult, scenarios, expectedResults);
	}

	@Override
	protected MarkovNetwork templateMKN() {
		try {
			MarkovNetwork mkn = new MarkovNetwork();
			Gate g1 = new Gate(mkn, 1, new int[]{3,4});
			mkn.addGate(g1, true);
			
			Gate g2 = new Gate(mkn, 2, new int[]{1,2});
			mkn.addGate(g2, true);
			
			Gate g3 = new Gate(mkn, 3, new int[]{1,2});
			mkn.addGate(g3, true);
			
			Gate g4 = new Gate(mkn, 4, new int[]{3,4});
			mkn.addGate(g4, true);
			
			return mkn;
		} catch (MarkovException ex2) {
			
		}
		
		return null;
	}
	
}

class testEAInputsAttractors extends EvolutionAlgorithmInputsAttractors {

	public testEAInputsAttractors(EAResult eaResult, boolean[][] scenarios, String[][] expectedResults, double lengthWeight) throws EAException, MarkovException {
		super(eaResult, scenarios, expectedResults, lengthWeight);
	}

	@Override
	protected MarkovNetwork templateMKN() {
		try {
			MarkovNetwork mkn = new MarkovNetwork();
			Gate g1 = new Gate(mkn, 1, new int[]{3,4});
			mkn.addGate(g1, true);
			
			Gate g2 = new Gate(mkn, 2, new int[]{1,2});
			mkn.addGate(g2, true);
			
			Gate g3 = new Gate(mkn, 3, new int[]{1,2});
			mkn.addGate(g3, true);
			
			Gate g4 = new Gate(mkn, 4, new int[]{3,4});
			mkn.addGate(g4, true);
			
			return mkn;
		} catch (MarkovException ex2) {
			
		}
		
		return null;
	}
	
}