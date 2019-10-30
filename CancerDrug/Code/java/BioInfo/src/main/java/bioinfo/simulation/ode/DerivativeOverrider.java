package bioinfo.simulation.ode;

public interface DerivativeOverrider {
	int getVariableIndex();
	boolean isOverrideDerivative(double t);
	double getDerivative(double t);
}
