package it.unipd.math.plg.models.distributions;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Beta;
import cern.jet.random.Normal;


/**
 * 
 * @author Andrea Burattin
 * @version 0.1
 */
public class NormalDistribution extends PlgProbabilityDistribution {

	private Normal distr = new Normal(0, 1, cernGenerator);
	
	/**
	 * This constructor is used to create a standard Gaussian probability
	 * distribution object
	 */
	public NormalDistribution() {
		super();
	}
	
	
	@Override
	public Double nextDouble() {
		Double val = 0.;
		do {
			val = (distr.nextDouble() / 6)+.5;
		} while (val > 1.0 || val < -0.0);

		return val;
	}


	@Override
	public Double getValue(double x) {
		return distr.pdf(x);
	}


	@Override
	public Double[] getValuesForPlotting(int size) {
		Double[] toRet = new Double[size];
		int j = 0;
		for(int i = (int) - Math.floor(size/2.0); i < Math.ceil(size/2.0); i++) {
			toRet[j++] = getValue(i);
		}
		return toRet;
	}
}
