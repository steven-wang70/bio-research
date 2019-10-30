package bioinfo.ea;

public final class EAResult {
	// Input parameters
	private int _headCount;
	private double _mutationRate;
	private int _maxGeneration;
	private double _averageBitVoteConsistencyThreshold;
	
	// Results
	private boolean _success;
	private String _logic;
	private int _actualGeneration;
	private double _averageBitVoteConsistencyRatio;
	private double _resultSimilarity;
	
	public int getHeadCount() {
		return _headCount;
	}
	public double getMutationRate() {
		return _mutationRate;
	}
	public int getMaxGeneration() {
		return _maxGeneration;
	}
	public double getAverageBitVoteConsistencyThreshold() {
		return _averageBitVoteConsistencyThreshold;
	}
	public double getAverageBitVoteConsistencyRatio() {
		return _averageBitVoteConsistencyRatio;
	}
 	public boolean isSuccess() {
		return _success;
	}
	public String getLogic() {
		return _logic;
	}
	public int getActualGeneration() {
		return _actualGeneration;
	}
	public void setResultSimilarity(double sim) {
		_resultSimilarity = sim;
	}
	
	// Options
	private double _fuzzyAttractorRatio = 1.0;
	private int _fuzzyLength = 3;
	
	public void setFuzzyAttractorRatio(double v) {
		_fuzzyAttractorRatio = v > 1 ? 1 : (v < 0.5 ? 0.5 : v);
	}
	public double getFuzzyAttractorRatio() {
		return _fuzzyAttractorRatio;
	}
	public void setFuzzyLength(int v) {
		_fuzzyLength = v < 2 ? 2 : v;
	}
	public int getFuzzyLength() {
		return _fuzzyLength;
	}
	
	public EAResult(int headCount, double mutationRate, int maxGeneration, double averageBitVoteConsistencyThreshold) {
		_headCount = headCount;
		_mutationRate = mutationRate;
		_maxGeneration = maxGeneration;
		_averageBitVoteConsistencyThreshold = averageBitVoteConsistencyThreshold;
		
		_success = false;
	}
	
	public void setResult(String logic, int actualGeneration, double averageBitVoteConsistencyRatio) {
		_logic = logic;
		_actualGeneration = actualGeneration;
		_averageBitVoteConsistencyRatio = averageBitVoteConsistencyRatio;
		_success = false;
		
		if (averageBitVoteConsistencyRatio >= _averageBitVoteConsistencyThreshold) {
			_success = true;
		}
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("HeadCount=").append(_headCount).append('\n');
		sb.append("MutationRate=").append(_mutationRate).append('\n');
		sb.append("MaxGeneration=").append(_maxGeneration).append('\n');
		sb.append("AverageBitVoteConsistencyThreshold=").append(_averageBitVoteConsistencyThreshold).append('\n');
		sb.append("AverageBitVoteConsistencyRatio=").append(_averageBitVoteConsistencyRatio).append('\n');
		sb.append("ResultSimilarity=").append(_resultSimilarity).append('\n');
		sb.append("FuzzyAttractorRatio=").append(_fuzzyAttractorRatio).append('\n');
		sb.append("FuzzyLength=").append(_fuzzyLength).append('\n');
		sb.append("ActualGeneration=").append(_actualGeneration).append('\n');
		sb.append("Success=").append(_success).append('\n');
		
		return sb.toString();
	}
}
