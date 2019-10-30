package bioinfo.simulation.ode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

public class FindAttractors {

	public static void main(String[] args) {
		final int DIMENSION = 81;
		final double EPS = 0.01;
		final int MIN_PTS = 2; 
		
		ODEResultLoader loader = new ODEResultLoader(DIMENSION);
		List<double[]> results = loader.loadFromFile("D:\\steven\\gitsrc\\CancerDrug\\Data\\ode\\leukemia\\ode.txt");
		
		List<SimulationResultPoint> points = new ArrayList<SimulationResultPoint>();
		for (double[] v : results) {
			points.add(new SimulationResultPoint(v));
		}
		
		ClusterLayeredResult clr = findAttractors(points, EPS, MIN_PTS, 0, DIMENSION);
		
		List<List<SimulationResultPoint>> attractors = new ArrayList<List<SimulationResultPoint>>();
		clr.getFinalResults(attractors);

		List<ClusterResult> res =  new ArrayList<ClusterResult>(attractors.size());
		// output the clusters
		for (int i=0; i<attractors.size(); i++) {
			res.add(processACluster(attractors.get(i)));
		}
		
		// Sort the result list
		Collections.sort(res, new Comparator<ClusterResult>(){
			public int compare(ClusterResult arg0, ClusterResult arg1) {
				return arg1._count - arg0._count;
			}});;
		for (ClusterResult r : res) {
			System.out.println(r.toString()); 
		}
		
	}

	private static ClusterLayeredResult findAttractors(List<SimulationResultPoint> points, double eps, int minPts, int dimIndex, int totalDimension ) {
		for (SimulationResultPoint srp : points) {
			srp.setDimIndex(dimIndex);
		}
		
		SimpleClusterer cluster = new SimpleClusterer(eps, minPts);
		List<List<SimulationResultPoint>> result = cluster.cluster(points);
		ClusterLayeredResult thisLayerResult = new ClusterLayeredResult(totalDimension, dimIndex);
		if (dimIndex == totalDimension - 1) {
			thisLayerResult.thisLayerResult = result;
		} else {
			thisLayerResult.nextLayerResult = new ArrayList<ClusterLayeredResult>(result.size());
			for (List<SimulationResultPoint> r : result) {
				thisLayerResult.nextLayerResult.add(findAttractors(r, eps, minPts, dimIndex + 1, totalDimension));
			}
		}
		
		return thisLayerResult;
	}
	
	/**
	 * Process the final cluster result.
	 * @param cluster
	 * @return
	 */
	private static ClusterResult processACluster(List<SimulationResultPoint> cluster) {
		int count = cluster.size();
		int dim = cluster.get(0).odeResult().length;
		double[] centralPoint = new double[dim];
		
		for (int i = 0; i < count; i++) {
			for (int d = 0; d < dim; d++) {
				centralPoint[d] += cluster.get(i).odeResult()[d];
			}
		}
		for (int d = 0; d < dim; d++) {
			centralPoint[d] /= count;
		}
		
		double maxDistance = 0;
		for (int i = 0; i < count; i++) {
			double p2distance = 0;
			for (int d = 0; d < dim; d++) {
				double p1distance = cluster.get(i).odeResult()[d] - centralPoint[d];
				p2distance += p1distance * p1distance;
			}
			p2distance = Math.sqrt(p2distance);
			if (p2distance > maxDistance) {
				maxDistance = p2distance;
			}
		}
		
		return new ClusterResult(centralPoint, maxDistance, count);
	}
	
	
	/**
	 * @author steven
	 * The final result for users that include central point, count and radius of the data.
	 */
	static class ClusterResult {
		public double[] _centralPoint;
		public double _radius;
		public int _count;
		
		public ClusterResult(double[] centralPoint, double radius, int count) {
			_centralPoint = centralPoint;
			_radius = radius;
			_count = count;
		}

		@Override
		public String toString() {
			NumberFormat formatter = new DecimalFormat("#0.00");
			StringBuilder sb = new StringBuilder();
			sb.append("Count: ").append(_count).append("\n");
			sb.append("Radius: ").append(_radius).append("\n");
			sb.append("Point: ").append(bioinfo.utils.Utils.Array2String(_centralPoint, formatter)).append("\n");
			
			return sb.toString();
		}
	}
	 
	/**
	 * @author steven
	 * Since the ode result converges to some points, we will use the clustering dimension 
	 * by dimension for better performance.
	 */
	static class SimulationResultPoint{
		private double[] _odeResult;
		private double _point;
		public SimulationResultPoint(double[] odeResult) {
			_odeResult = odeResult;
		}
		
		private void setDimIndex(int dimIndex) {
			_point = _odeResult[dimIndex];
		}
		
		public double getPoint() {
			return _point;
		}
		
		public double[] odeResult() {
			return _odeResult;
		}
		
	}
	
	static class ClusterLayeredResult {
		public List<List<SimulationResultPoint>> thisLayerResult;
		public List<ClusterLayeredResult> nextLayerResult;
		public int dimIndex;
		public int totalDimension;
		
		public ClusterLayeredResult(int totalDimension, int dimIndex){
			this.totalDimension = totalDimension;
			this.dimIndex = dimIndex;
		}
		
		public void getFinalResults(List<List<SimulationResultPoint>> attractors) {
			if (dimIndex == totalDimension - 1) {
				for (List<SimulationResultPoint> r : this.thisLayerResult) {
					attractors.add(r);
				}
			} else {
				for (ClusterLayeredResult clr : this.nextLayerResult) {
					clr.getFinalResults(attractors);
				}
			}
		}
	}

	static class SimpleClusterer {
		private double _eps;
		private int _minPts;
		public SimpleClusterer(double eps, int minPts) {
			_eps = eps;
			_minPts = minPts;
		}
		
		public List<List<SimulationResultPoint>> cluster(List<SimulationResultPoint> points) {
			// First sort the list
			Collections.sort(points, new Comparator<SimulationResultPoint>(){
				public int compare(SimulationResultPoint arg0, SimulationResultPoint arg1) {
					if (arg0.getPoint() < arg1.getPoint()) {
						return -1;
					} else if (arg0.getPoint() > arg1.getPoint()) {
						return 1;
					} else {
						return 0;
					}
				}});
			
			List<List<SimulationResultPoint>> result = new ArrayList<List<SimulationResultPoint>>();
			
			Iterator<SimulationResultPoint> iter = points.iterator();
			
			List<SimulationResultPoint> potentialCluster = new ArrayList<SimulationResultPoint>();
			SimulationResultPoint ref = null;
			while (iter.hasNext()) {
				SimulationResultPoint v = iter.next();
				if (potentialCluster.size() == 0) {
					potentialCluster.add(v);
					ref = v;
				} else {
					if (v.getPoint() - ref.getPoint() <= _eps) {
						potentialCluster.add(v);
						ref = v;
					} else {
						if (potentialCluster.size() >= _minPts) {
							result.add(potentialCluster);
						}
						potentialCluster = new ArrayList<SimulationResultPoint>();
						potentialCluster.add(v);
						ref = v;
					}
				}
			}
			if (potentialCluster.size() >= _minPts) {
				result.add(potentialCluster);
			}
			
			return result;
		}
	}
}
