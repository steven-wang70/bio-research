package bioinfo.simulation.ode;

public interface NoiseGenerator {
	
	/**
	 * Noise region should be within [0, 1]
	 * @param t
	 * @param varIndex
	 * @return
	 */
	double getNoise(double t, int varIndex);
}
