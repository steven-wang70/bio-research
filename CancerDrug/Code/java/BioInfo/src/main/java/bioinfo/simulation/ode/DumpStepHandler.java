package bioinfo.simulation.ode;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

class DumpStepHandler implements StepHandler {
	public void init(double t0, double[] y0, double t) {
	}

	private static NumberFormat formatter = new DecimalFormat("#0.000000");
	public static void printValues(double t, double[] ys) {
		System.out.print(formatter.format(t));
        for (double y : ys) {
        	System.out.print("\t" + formatter.format(y));
        }
        System.out.println();
	}

	public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
		printValues(interpolator.getCurrentTime(), interpolator.getInterpolatedState());
	}
}