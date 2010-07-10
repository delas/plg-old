package it.unipd.math.plg.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Beta;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

/**
 * This class describes a probability distribution with its parameters and
 * allows the generation of random number.
 * 
 * @author Andrea Burattin
 * @version 0.1
 */
public class PlgProbabilityDistribution {
	
	private DISTRIBUTION probabilityDistribution;
	private Vector<Double> parameters;
	private static RandomEngine cernGenerator;
	private static java.util.Random generator;
	
	
	/**
	 * These are the probability distributions supported for the generation of
	 * the elements
	 */
	public static enum DISTRIBUTION {
		/** Normal (aka Gaussian) distribution */
		NORMAL,
		/** Beta distribution */
		BETA,
		/** Uniform distribution (all elements are equiprobable) */
		UNIFORM
	}
	
	
	/**
	 * This method generates a random double according to the probability
	 * distribution
	 * 
	 * @return the new random number
	 */
	public Double nextDouble() {
		AbstractDistribution distr = null;
		Double val = 0.;
		if (probabilityDistribution.equals(DISTRIBUTION.NORMAL)) {
			do {
				val = (generator.nextGaussian() / 6)+.5;
			} while (val > 1.0 || val < -0.0);
		} else if (probabilityDistribution.equals(DISTRIBUTION.BETA)) {
			distr = new Beta(parameters.get(0), parameters.get(1), cernGenerator);
			val = distr.nextDouble();
		} else if (probabilityDistribution.equals(DISTRIBUTION.UNIFORM)) {
			distr = new Uniform(0, 1, cernGenerator);
			val = distr.nextDouble();
		}
		return val;
	}
	
	
	/**
	 * This method generates a new random number with respect to the current
	 * probability distribution and in a particular range
	 * 
	 * @param min the minimal value
	 * @param max the maximal value
	 * @return the random integer
	 */
	public Integer nextInt(int min, int max) {
		Float v = nextDouble().floatValue();
		int range = max - min;
		range = Math.round(range * v) + min;
		return range;
	}
	
	
	/**
	 * Private class constructor, used only to initialize the Colt generator
	 */
	private PlgProbabilityDistribution() {
		cernGenerator = new MersenneTwister(new Date());
		generator = new java.util.Random();
	}

	
	/**
	 * This method is required to sort the activities according to their
	 * probability
	 * 
	 * @param probabilityOfChoosing
	 * @return a sorted array (with respect to the weight of each activity)
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<PlgActivity> getSortedActivities(HashMap<PlgActivity, Double> probabilityOfChoosing) {
		
		HashMap<PlgActivity, Double> worker = (HashMap<PlgActivity, Double>)probabilityOfChoosing.clone();
		ArrayList<PlgActivity> sorted = new ArrayList<PlgActivity>(); 
		Double pTot = 1.;
		
		while (worker.size() > 0) {
			Double extracted = generator.nextDouble() * pTot;
			Double threshold = 0.0;
			for (PlgActivity a : worker.keySet()) {
				Double d = worker.get(a);
				threshold += d;
				if (threshold > extracted) {
					sorted.add(a);
					worker.remove(a);
					pTot -= d;
					break;
				}
			}
		}
		return sorted;
	}
	
	
	/**
	 * This static method is used to create a standard Gaussian probability
	 * distribution object
	 * 
	 * @return the generator based on the probability distribution 
	 */
	public static PlgProbabilityDistribution normalDistributionFactory() {
		PlgProbabilityDistribution pd = new PlgProbabilityDistribution();
		pd.probabilityDistribution = DISTRIBUTION.NORMAL;
		return pd;
	}
	
	
	/**
	 * This static method is used to create a beta probability distribution
	 * object
	 * 
	 * @param alpha the range
	 * @param beta the shape
	 * @return the generator based on the probability distribution 
	 */
	public static PlgProbabilityDistribution betaDistributionFactory(double alpha, double beta) {
		alpha = (alpha < 1 ? 1 : alpha);
		beta = (beta < 1? 1 : beta);
		PlgProbabilityDistribution pd = new PlgProbabilityDistribution();
		pd.probabilityDistribution = DISTRIBUTION.BETA;
		pd.parameters = new Vector<Double>(2);
		pd.parameters.add(alpha);
		pd.parameters.add(beta);
		return pd;
	}
	
	
	/**
	 * This static method is used to create a gamma probability distribution
	 * object
	 * 
	 * @param alpha the shape
	 * @param beta the scale
	 * @return the generator based on the probability distribution 
	 */
	public static PlgProbabilityDistribution uniformDistributionFactory() {
		PlgProbabilityDistribution pd = new PlgProbabilityDistribution();
		pd.probabilityDistribution = DISTRIBUTION.UNIFORM;
		return pd;
	}
	
}
