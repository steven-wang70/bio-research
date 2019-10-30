package bioinfo.simulation.ode;

import java.util.HashMap;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;

public abstract class CellSimulationBase implements FirstOrderDifferentialEquations {
	private HashMap<Integer, DerivativeOverrider> _overriders = new HashMap<Integer, DerivativeOverrider>();
	private NoiseGenerator _noiseGenerator;
	
	protected double n = 3.0; // Hill coefficient
	protected double a = 10.0;
	
	public void setCofficients(double n, double a) {
		this.n = n;
		this.a = a;
	}

	public abstract int getDimension();
	public abstract void computeDerivatives(double t, double[] y, double[] yDot);
	
	public void clearOverriders() {
		_overriders.clear();
	}
	
	public void addOverrider(DerivativeOverrider overrider) {
		_overriders.put(overrider.getVariableIndex(), overrider);
	}
	
	protected boolean isOverrideDerivative(double t, int varIndex) {
		if (_overriders.containsKey(varIndex)) {
			return _overriders.get(varIndex).isOverrideDerivative(t);
		} else {
			return false;
		}
	}
	
	protected double overrideDerivative(double t, int varIndex) {
		return _overriders.get(varIndex).getDerivative(t);
	}
	
	public void setNoiseGenerator(NoiseGenerator generator) {
		_noiseGenerator = generator;
	}
	
	protected double applyNoise(double initial, double t, int varIndex) {
		if (_noiseGenerator == null) {
			return initial;
		} else {
			if (initial <= 0) {
				initial  = 1e-10;
			} else if (initial >= 1) {
				initial  = 1 - 1e-10;
			}
			
			// The sigmoid function of the cencentration should be:
			// y = t / (t + 1); t >= 0
			double noise = _noiseGenerator.getNoise(t, varIndex);
			return (noise * initial) / (noise * initial - initial + 1);
		}
	}
	
	public final static double ABS_TOLERANCE = 1e-5;
	public final static double REL_TOLERANCE = 1e-5;
			
	public static void runSimulation(CellSimulationBase ode, double[] initials, double targetTime, StepHandler stepHandler) {
		FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0e-8, 100.0, ABS_TOLERANCE, REL_TOLERANCE);
		
		if (stepHandler != null) {
			dp853.addStepHandler(stepHandler);
		}
		
		dp853.integrate(ode, 0.0, initials, targetTime, initials);
	}
}
