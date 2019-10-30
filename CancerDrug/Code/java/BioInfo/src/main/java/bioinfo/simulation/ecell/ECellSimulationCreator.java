package bioinfo.simulation.ecell;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import bioinfo.simulation.ConcentrationLoader;
import bioinfo.regulation.GeneRegulation;
import bioinfo.regulation.GeneRegulationLoader;
import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class ECellSimulationCreator {

	public ECellSimulationCreator(List<GeneRegulation> regulations, Hashtable<String, Double> concentrations) {
		_regulations = regulations;
		_concentrations = concentrations;
	}
	
	private List<GeneRegulation> _regulations;
	private Hashtable<String, Double> _concentrations;
	
	private void generateECellSimulationScript(BufferedWriter writer, boolean includeInputNode) {
		Hashtable<String, List<GeneRegulation>>  regulatedGenes = new Hashtable<String, List<GeneRegulation>>();
		HashSet<String> allGenes = new HashSet<String>();
		
		for (GeneRegulation gr : _regulations) {
			if (!regulatedGenes.containsKey(gr.getBindingGene())){
				regulatedGenes.put(gr.getBindingGene(), new ArrayList<GeneRegulation>());
			}
			
			regulatedGenes.get(gr.getBindingGene()).add(gr);
			
			allGenes.add(gr.getFactorGene());
			allGenes.add(gr.getBindingGene());
		}
		
		// %matplotlib inline
		// import numpy
		// from ecell4 import *
		Utils.println("%matplotlib inline", writer);
		Utils.println("import numpy", writer);
		Utils.println("from ecell4 import *", writer);
		Utils.println("", writer);
		
		int coefficientsCount = 0;
		// Generate coefficients
		for (String geneName : allGenes) {
			if (includeInputNode || regulatedGenes.keySet().contains(geneName)) {
				Utils.println(coef_d(geneName) + " = " + Integer.toString(coefficientsCount++), writer);
			}
			
			if (regulatedGenes.keySet().contains(geneName)) {
				Utils.println(coef_s(geneName) + " = " + Integer.toString(coefficientsCount++), writer);
				
				List<GeneRegulation> factors = regulatedGenes.get(geneName);
				for (GeneRegulation gr : factors) {
					Utils.println(coef_x(geneName, gr.getFactorGene()) + " = " + Integer.toString(coefficientsCount++), writer);
				}
			}
		}
		Utils.println("k_count = " + Integer.toString(coefficientsCount), writer);
		Utils.println("", writer);

		int initialCount = 0;
		for (String geneName : allGenes) {
			Utils.println("init_" + geneName + " = " + Integer.toString(initialCount++), writer);
		}
		Utils.println("init_count = " + Integer.toString(initialCount), writer);
		Utils.println("", writer);

		Utils.println("def start_simulation(factors, initials, length, steps):", writer);
		
		// Generate rules
		// with reaction_rules():
		Utils.println("\twith reaction_rules():", writer);
		for (String geneName : allGenes) {
			if (regulatedGenes.keySet().contains(geneName)) {
				StringBuilder sb = new StringBuilder();
			    // ~G3 > G3 | k_3s * k_31 * G1 * k_32 * G2 / (1 + k_31 * G1) * (1 + k_32 * G2) - k_3d * G3
				sb.append("\t\t~").append(geneName).append(" > ").append(geneName).append(" | factors[").append(coef_s(geneName)).append("]");
				
				List<GeneRegulation> factors = regulatedGenes.get(geneName);
				for (GeneRegulation gr : factors) {
					sb.append("*").append("(");
					
					if (gr.getRelation() == GeneRegulation.RelationActivation) {
						sb.append("factors[").append(coef_x(geneName, gr.getFactorGene())).append("]*").append(gr.getFactorGene());
					} else {
						sb.append("1");
					}
					
					sb.append("/");
					sb.append("(");
					sb.append("1+factors[").append(coef_x(geneName, gr.getFactorGene())).append("]*").append(gr.getFactorGene());
					sb.append(")");
					sb.append(")");
				}
				
				sb.append("-factors[").append(coef_d(geneName)).append("]*").append(geneName);
				Utils.println(sb.toString(), writer);
			} else if (includeInputNode) {
				StringBuilder sb = new StringBuilder();
			    // ~G3 > G3 |  - k_3d * G3
				sb.append("\t\t~").append(geneName).append(" > ").append(geneName).append(" | -factors[").append(coef_d(geneName)).append("]*").append(geneName);
				Utils.println(sb.toString(), writer);
			}
		}
		
		Utils.println("\t", writer);

		// Generate observer
		// obs1 = FixedIntervalNumberObserver((toPoint - fromPoint) / steps, ('ERM', 'ERP', 'GFR', 'E2ER'))
		StringBuilder sb = new StringBuilder();
		sb.append("\tobs1 = FixedIntervalNumberObserver(length * 1.0 / steps, (");
		boolean firstOne = true;
		for (String geneName : allGenes) {
			if (!firstOne) {
				sb.append(",");
			}
			
			sb.append("\'").append(geneName).append("\'");
			firstOne = false;
		}
		sb.append("))");
		Utils.println(sb.toString(), writer);
		
		
		// Generate start simulation
		// y = run_simulation(
		// 	    numpy.linspace(0, 6, 1000), {'G1': 0, 'G2': 0, 'G3': 0}, solver='ode')
		sb = new StringBuilder();
		sb.append("\ty = run_simulation(");
		sb.append("numpy.linspace(0, length, steps), ");
		
		sb.append("{");
		firstOne = true;
		for (String geneName : allGenes) {
			if (!firstOne) {
				sb.append(",");
			}
			
			sb.append("\'").append(geneName).append("\':initials[init_").append(geneName).append("]");
			firstOne = false;
		}
		sb.append("}, solver=\'ode\', return_type=None, observers=(obs1))");
		Utils.println(sb.toString(), writer);
		Utils.println("\treturn obs1", writer);
	}
	
	/**
	 * This is the factor of degradation
	 * @param name
	 * @return
	 */
	private static String coef_d(String name) {
		return "k_d_" + name;
	}
	
	/**
	 * This is the factor on all activators/deactivators
	 * @param name
	 * @return
	 */
	private static String coef_s(String name) {
		return "k_s_" + name;
	}
	
	/**
	 * This the factor of activator/deactovator
	 * @param binder
	 * @param factor
	 * @return
	 */
	private static String coef_x(String binder, String factor) {
		return "k_x_" + binder + "_" + factor;
	}
	
	public static void main(String[] args) {
		Settings.loadSettings(args[0]);
		String regulationFile = Settings.instance().getSetting("ECellSimulation", "RegulationFile");
		String initialConcentrationFile = Settings.instance().getSetting("ECellSimulation", "InitialConcentrationFile");
		int period = Settings.instance().getSetting("ECellSimulation", "Period", Integer.class);
		int iteration = Settings.instance().getSetting("ECellSimulation", "Iteration", Integer.class);
		String ecellScriptFile = Settings.instance().getSetting("ECellSimulation", "ScriptFile");

		GeneRegulationLoader regLoader = new GeneRegulationLoader();
		List<GeneRegulation> regulations = regLoader.loadFromFile(regulationFile);
		
		ConcentrationLoader conLoader = new ConcentrationLoader();
		Hashtable<String, Double> concentrations = conLoader.loadFromFile(initialConcentrationFile);
		
		ECellSimulationCreator creator = new ECellSimulationCreator(regulations, concentrations);
		
		BufferedWriter scriptWriter = Utils.openFileToWrite(ecellScriptFile);
		creator.generateECellSimulationScript(scriptWriter, false);
		Utils.close(scriptWriter);
	}

}
