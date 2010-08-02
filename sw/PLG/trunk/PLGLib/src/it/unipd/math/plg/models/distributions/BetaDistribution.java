package it.unipd.math.plg.models.distributions;

import cern.jet.random.Beta;

/**
 * 
 * @author Andrea Burattin
 * @version 0.1
 */
public class BetaDistribution extends PlgProbabilityDistribution {

	Beta distr = null;
	
	
	/**
	 * This constructor is used to create a Beta probability distribution object
	 * 
	 * @param alpha the range
	 * @param beta the shape
	 */
	public BetaDistribution(double alpha, double beta) {
		super();
		alpha = (alpha < 1 ? 1 : alpha);
		beta = (beta < 1? 1 : beta);
		distr = new Beta(alpha, beta, cernGenerator);
	}
	
	
	@Override
	public Double nextDouble() {
		return distr.nextDouble();
	}


	@Override
	public Double getValue(double x) {
		return distr.pdf(x);
	}


	@Override
	public Double[] getValuesForPlotting(int size) {
		Double[] toRet = new Double[size];
		double tmpI = 0;
		for(int i = 0; i < size; i++) {
			tmpI = (double)i / (double)size;
			toRet[i] = getValue(tmpI) / 2.6;
			toRet[i] = (toRet[i] > 1? 1 : toRet[i]);
		}
		return toRet;
	}

}
