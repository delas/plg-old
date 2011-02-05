package it.unipd.math.plg.models.distributions;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;

/**
 * Class for the representation of a uniform distribution
 * 
 * @author Andrea Burattin
 * @version 0.1
 */
public class UniformDistribution extends PlgProbabilityDistribution {

	
	/**
	 * This constructor is used to create a uniform probability distribution
	 * object
	 * 
	 */
	public UniformDistribution() {
		super();
	}
	
	
	@Override
	public Double nextDouble() {
		AbstractDistribution distr = null;
		distr = new Uniform(0, 1, cernGenerator);
		return distr.nextDouble();
	}


	@Override
	public Double getValue(double x) {
		return .5;
	}


	@Override
	public Double[] getValuesForPlotting(int size) {
		Double[] toRet = new Double[size];
		for(int i = 0; i < size; i++) {
			toRet[i] = .5;
		}
		return toRet;
	}

}
