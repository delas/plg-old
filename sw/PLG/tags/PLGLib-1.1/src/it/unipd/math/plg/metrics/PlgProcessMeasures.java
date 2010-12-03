package it.unipd.math.plg.metrics;

import it.unipd.math.plg.models.PlgProcess;

/**
 * This class is used to collect some metric about the process. In particual, at
 * this moment, the metric calculated are both described in the paper
 * "Complexity Metrics for Workflow Nets" by Kristian Bisgaard Lassen and Wil
 * M.P. van der Aalst. These are:
 * <dl>
 * 	<dt>Extended Cardoso Metric</dt>
 * 		<dd>As described in the Section 3.1 of the paper</dd>
 * 	<dt>Extended Cyclomatic Metric</dt>
 * 		<dd>As described in the Section 3.2 of the paper</dd>
 * </dl>
 * 
 * @see PlgMetricCalculator
 * @author Andrea Burattin
 * @version 0.3
 */
public class PlgProcessMeasures {

	private final int cyclomaticMetric;
	private final int cardosoMetric;
	private PlgProcess originalProcess;

	
	/**
	 * The basic class constructor
	 * 
	 * @param originalProcess the origial process
	 * @param cyclomaticMetric the Extended Cyclomatic Metric
	 * @param cardosoMetric the Extended Cardoso Metric
	 */
	public PlgProcessMeasures(PlgProcess originalProcess,
			int cyclomaticMetric, int cardosoMetric) {
		this.originalProcess = originalProcess;
		this.cyclomaticMetric = cyclomaticMetric;
		this.cardosoMetric = cardosoMetric;
	}
	
	
	/**
	 * This method to get the Extended Cardoso Metric value
	 * 
	 * @return the metric value
	 */
	public int getCardosoMetric() {
		return cardosoMetric;
	}
	
	
	/**
	 * This method to get the Extended Cyclomatic Metric value
	 * 
	 * @return the metric value
	 */
	public int getCyclomaticMetric() {
		return cyclomaticMetric;
	}
	
	
	/**
	 * This method to get the representation of the original process
	 * 
	 * @return the representation of the original process
	 */
	public PlgProcess getOriginalProcess() {
		return originalProcess;
	}
}
