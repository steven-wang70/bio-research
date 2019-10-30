package bioinfo.simulation.ode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

public class SimulationODE {
	public static void main(String[] args) {
		runManySimulations();
	}
	
	private static double[] randGenerator(int count) {
		double[] ret = new double[count];
		for (int i = 0; i < count; i++) {
			ret[i] = Math.random();
		}
		
		return ret;
	}
	/**
	 * Tune the simulation with fixed initials and continuous output
	 */
	private static void tuneASimulation() {
		double[] initialValues = new double[] {
				0.901929389583002,
				0.1827374280445525,
				0.5382852671952869,
				0.46064118966896184,
				0.926165549985491,
				0.20425669518382217,
				0.16727643906381606,
				0.9350905483603786,
				0.5649631034186734,
				0.47995146681640255,
				0.16502387365747884,
				0.2697612401824011,
				0.16288127994179946,
				0.44895388758103416,
				0.24736328502263194,
				0.5187939367595256,
				0.05750596554693854,
				0.15223962127722002,
				0.5254885597451953,
				0.02017509578298049,
				0.7365576196868362,
				0.36225664481115916,
				0.9042232394332897,
				0.22670782125185118,
				0.31287477074774517,
				0.8517265723127895,
				0.3751484179546043,
				0.3294299392027682,
				0.8708928684265045,
				0.19727902323264712,
				0.6395542027872146,
				0.8606432921108763,
				0.47749566096675056,
				0.1356496268634183,
				0.3135644578849601,
				0.7889143119025046,
				0.2956238372096426,
				0.7684926105043771,
				0.6914175870783849,
				0.09985021550513917,
				0.29078408407095824,
				0.8464295791996773,
				0.6727687977646585,
				0.49415165668300554,
				0.47860909672372287,
				0.9537758546396601,
				0.9960121558277671,
				0.6283870422300376,
				0.5258767527458502,
				0.6624565514700478,
				0.362003536152703,
				0.27412936197984883,
				0.7879050996227698,
				0.6269444788550236,
				0.3811983632601913,
				0.8775420090446542,
				0.5419969509154298,
				0.15162267732025903,
				0.5321009128986419,
				0.26419475533993875,
				0.2596160348932375,
				0.977794162422792,
				0.48005456516079203,
				0.6528368094457719,
				0.2492344493849159,
				0.909632542435075,
				0.1727856718147487,
				0.7170492588852518,
				0.8972989162388112,
				0.42668655225247887,
				0.10274591141930756,
				0.16514397834935401,
				0.03652105771253977,
				0.61512268700877,
				0.006181152187121897,
				0.19637201270132643,
				0.6787050823648418,
				0.5907100163780019,
				0.05498785158290731,
				0.23544941768898398,
				0.46358343908863153
				};
		
		LeukemiaODE ode = new LeukemiaODE();
		
		System.out.print("Time");
		for (String name : LeukemiaODE.index2name) {
			System.out.print("\t" + name);
		}
		System.out.println();
		
		DumpStepHandler.printValues(0, initialValues);
		StopWatch sw = new StopWatch();
		
		sw.start();
		for (int i = 0; i < 1; i++) {
			double[] temp = Arrays.copyOf(initialValues, LeukemiaODE.GENE_COUNT);
			CellSimulationBase.runSimulation(ode,  temp,  50, new DumpStepHandler());	
		}
		
		sw.stop();
		System.out.println(sw.getNanoTime() / 1000000);
	}
	
	/**
	 * Run many simulations with random initial values
	 */
	private static void runManySimulations() {
		LeukemiaODE ode = new LeukemiaODE();
		
		System.out.print("Time");
		for (String name : LeukemiaODE.index2name) {
			System.out.print("\t\"" + name + "\"");
		}
		System.out.println();
		
		StopWatch sw = new StopWatch();
		
		sw.start();
		for (int i = 0; i < 1000; i++) {
			double[] initialValues = randGenerator(LeukemiaODE.GENE_COUNT);
			CellSimulationBase.runSimulation(ode,  initialValues,  150, new AnalysisStepHandler());
		}
		
		sw.stop();
		System.out.print("Time used: ");
		System.out.print(sw.getNanoTime() / 1000000000);
		System.out.println(" seconds.");
		
		analyzeErrors();
	}
	
	private static void analyzeErrors() {
		double[] results = AnalysisStepHandler.results;
		double ref = 1.0;
		int remaining = 1000;
		
		while (remaining > 0) {
			int counter = 0;
			for (int i = 0; i < 1000; i++) {
				if (results[i] > ref) {
					results[i] = 0;
					counter++;
					remaining--; 
				}
			}
			
			System.out.println("> " + Double.toString(ref) + " : " + Integer.toString(counter));
			ref /= 2;
		}
		
				
	}
}

class AnalysisStepHandler implements StepHandler {
	private static int counter = 0;
	public static double[] results = new double[1000];
	
	public void init(double t0, double[] y0, double t) {
		// TODO Auto-generated method stub
		
	}
	
	public void handleStep(StepInterpolator interpolator, boolean isLast)
			throws MaxCountExceededException {
		// TODO Auto-generated method stub
		if (isLast) {
			double[] derivs = interpolator.getInterpolatedDerivatives();
			double ret = 0;
			for (double d : derivs) {
				 if (Math.abs(d) > ret) {
					 ret = Math.abs(d);
				 }
			}
			
			results[counter++] = ret;
			System.out.println(ret);
		}
	}
	
}
