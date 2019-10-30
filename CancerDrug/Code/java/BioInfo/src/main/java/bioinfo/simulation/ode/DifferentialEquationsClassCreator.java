package bioinfo.simulation.ode;


import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import bioinfo.simulation.ConcentrationLoader;
import bioinfo.regulation.GeneRegulation;
import bioinfo.regulation.GeneRegulationLoader;
import bioinfo.utils.Settings;
import bioinfo.utils.Utils;

public class DifferentialEquationsClassCreator {

	public DifferentialEquationsClassCreator(List<GeneRegulation> regulations) {
		_regulations = regulations;
	}
	
	private List<GeneRegulation> _regulations;
	
	private static String createSubExpr(List<String> regulators, HashMap<String, Integer> allGenes) {
		StringBuilder sb = new StringBuilder();
		boolean firstOne = true;
		
		for (String s : regulators) {
			if (!firstOne) {
				sb.append(" + ");
			}
			firstOne = false;
			
			sb.append("Math.pow(applyNoise(y[").append(allGenes.get(s)).append("], t, ").append(allGenes.get(s)).append("), n)");
		}
		
		return sb.toString();
	}
	
	private void generateDifferentialEquationsClass(BufferedWriter writer, boolean includeInputNode, String packageName, String className) {
		Hashtable<String, List<GeneRegulation>>  regulatedGenes = new Hashtable<String, List<GeneRegulation>>();
		HashMap<String, Integer> allGenes = new HashMap<String, Integer>();
		
		for (GeneRegulation gr : _regulations) {
			if (!regulatedGenes.containsKey(gr.getBindingGene())){
				regulatedGenes.put(gr.getBindingGene(), new ArrayList<GeneRegulation>());
			}
			
			regulatedGenes.get(gr.getBindingGene()).add(gr);
			
			allGenes.put(gr.getFactorGene(), 0);
			allGenes.put(gr.getBindingGene(), 0);
		}

		// Assign each gene a unique index
		int geneIndex = 0;
		for (String key : allGenes.keySet()) {
			allGenes.put(key, geneIndex++);
		}
		
		Utils.println("// Auto-generated", writer);
		Utils.println("package " + packageName + ";", writer);
		Utils.println("", writer);
		Utils.println("import java.util.HashMap;", writer);
		Utils.println("import java.util.Map;", writer);
		Utils.println("", writer);
		
		Utils.println("public class " + className + " extends CellSimulationBase {", writer);
		Utils.println("\tpublic static final int GENE_COUNT = " + Integer.toString(geneIndex++) + ";", writer);
		Utils.println("", writer);

		// Generate index to name mapping
		Utils.println("\tpublic static final String[] index2name = new String[] {", writer);
		geneIndex = 0;
		for (String geneName : allGenes.keySet()) {
			if (geneIndex == allGenes.size() - 1) {
				Utils.println("\t\t\"" + geneName + "\" \t// " + Integer.toString(geneIndex++), writer);
			} else {
				Utils.println("\t\t\"" + geneName + "\", \t// " + Integer.toString(geneIndex++), writer);
			}
		}
		Utils.println("\t};", writer);
		Utils.println("", writer);
		
		// Generate name to index mapping
		Utils.println("\tpublic static final Map<String, Integer> name2index;", writer);
		Utils.println("\tstatic {", writer);
		Utils.println("\t\tname2index = new HashMap<String, Integer>();", writer);
		for (String geneName : allGenes.keySet()) {
			// name2index.put("a", 0);
			Utils.println("\t\tname2index.put(\"" + geneName + "\", " + Integer.toString(allGenes.get(geneName)) + ");", writer);
		}
		Utils.println("\t}", writer);
		Utils.println("", writer);
		
	    // @Override
		// public int getDimension() {
	    //     return 2;
	    // }
		Utils.println("\t@Override", writer);
		Utils.println("\tpublic final int getDimension() {", writer);
		Utils.println("\t\treturn GENE_COUNT;", writer);
		Utils.println("\t}", writer);
		Utils.println("", writer);
		
	    // @Override
	    // public void computeDerivatives(double t, double[] y, double[] yDot) {
	    //     yDot[0] = omega * (c[1] - y[1]);
	    //     yDot[1] = omega * (y[0] - c[0]);
	    // }
		Utils.println("\t@Override", writer);
		Utils.println("\tpublic final void computeDerivatives(double t, double[] y, double[] yDot) {", writer);
		Utils.println("\t\tdouble activators;", writer);
		Utils.println("\t\tdouble inhibitors;", writer);

		for (String geneName : allGenes.keySet()) {
			List<String> activators = new ArrayList<String>();
			List<String> inhibitors = new ArrayList<String>();
			if (regulatedGenes.keySet().contains(geneName)) {
				List<GeneRegulation> factors = regulatedGenes.get(geneName);
				for (GeneRegulation gr : factors) {
					if (gr.getRelation() == GeneRegulation.RelationActivation) {
						activators.add(gr.getFactorGene());
					} else {
						inhibitors.add(gr.getFactorGene());
					}
				}
			}
			
			Utils.println("", writer);
			Utils.println("\t\t// " + geneName, writer);
			String geneIndexString = Integer.toString(allGenes.get(geneName));
			Utils.println("\t\tif(isOverrideDerivative(t, " + geneIndexString + ")) {", writer);
			Utils.println("\t\t\tyDot[" + geneIndexString + "] = overrideDerivative(t, " + geneIndexString + ");", writer);
			Utils.println("\t\t} else { ", writer);
			
			if (activators.size() > 0) {
				Utils.println("\t\t\t// activators : " + String.join(", ", activators), writer);
				Utils.println("\t\t\tactivators = " + createSubExpr(activators, allGenes) + ";", writer);
			} else {
				Utils.println("\t\t\tactivators = 0;", writer);
			}
			
			if (inhibitors.size() > 0) {
				Utils.println("\t\t\t// inhibitors : " + String.join(", ", inhibitors), writer);
				Utils.println("\t\t\tinhibitors = " + createSubExpr(inhibitors, allGenes) + ";", writer);
			} else {
				Utils.println("\t\t\tinhibitors = 0;", writer);
			}
			
			Utils.println("\t\t\tyDot[" + geneIndexString + "] = (a * activators / (1 + a * activators)) * (1 / (1 + a * inhibitors)) - " + "y[" + Integer.toString(allGenes.get(geneName)) + "];", writer);
			Utils.println("\t\t}", writer);
		}

		Utils.println("\t}", writer);
		Utils.println("", writer);

		Utils.println("}", writer);
	}
	
	public static void main(String[] args) {
		Settings.loadSettings(args[0]);
		String regulationFile = Settings.instance().getSetting("ODESimulation", "RegulationFile");
		String packageName = Settings.instance().getSetting("ODESimulation", "ODEClassPackage");
		String className = Settings.instance().getSetting("ODESimulation", "ODEClassName");
		String classFile = Settings.instance().getSetting("ODESimulation", "ODEClassFile");

		GeneRegulationLoader regLoader = new GeneRegulationLoader();
		List<GeneRegulation> regulations = regLoader.loadFromFile(regulationFile);
		
		DifferentialEquationsClassCreator creator = new DifferentialEquationsClassCreator(regulations);
		
		BufferedWriter scriptWriter = Utils.openFileToWrite(classFile);
		creator.generateDifferentialEquationsClass(scriptWriter, false, packageName, className);
		Utils.close(scriptWriter);
	}

}

