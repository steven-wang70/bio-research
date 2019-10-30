package bioinfo.simulation.ode;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

public class RunManySimulations {
	public static void main(String[] args) {
		// The only argument is the number of samples
		int count = Integer.parseUnsignedInt(args[0]);
		runManySimulations(count);
	}
	
	private static double[] randGenerator(int count) {
		double[] ret = new double[count];
		for (int i = 0; i < count; i++) {
			ret[i] = Math.random();
		}
		
		return ret;
	}
	
	/**
	 * Run many simulations with random initial values
	 */
	private static void runManySimulations(int count) {
		LeukemiaODE ode = new LeukemiaODE();
		StopWatch sw = new StopWatch();
		
		sw.start();
		for (int i = 0; i < count; i++) {
			if (i % 1000 == 0) {
				System.err.println(i);
			}
			double[] initialValues = randGenerator(LeukemiaODE.GENE_COUNT);
			CellSimulationBase.runSimulation(ode,  initialValues,  150, new DumpConvergedStepHandler());
		}
		
		sw.stop();
		System.err.println("Used " + sw.getNanoTime() / 1000000000 + " seconds");
	}

	
	/**
	 * @author steven
	 * If the result is converged, print it to output
	 */
	static class DumpConvergedStepHandler implements StepHandler {
		
		public void init(double t0, double[] y0, double t) {}
		
		public void handleStep(StepInterpolator interpolator, boolean isLast)
				throws MaxCountExceededException {
			if (isLast) {
				double[] derivs = interpolator.getInterpolatedDerivatives();
				double ret = 0;
				for (double d : derivs) {
					 if (Math.abs(d) > ret) {
						 ret = Math.abs(d);
					 }
				}
				
				if (ret > CellSimulationBase.ABS_TOLERANCE * 100) {
					System.err.println("BAD*** " + bioinfo.utils.Utils.Array2String(interpolator.getInterpolatedState()));
				} else {
					System.out.println(bioinfo.utils.Utils.Array2String(interpolator.getInterpolatedState()));
				}
			}
		}
		
	}
}
