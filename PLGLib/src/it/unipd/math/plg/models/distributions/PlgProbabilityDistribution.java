package it.unipd.math.plg.models.distributions;

import it.unipd.math.plg.models.PlgActivity;

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
public abstract class PlgProbabilityDistribution {
	
	protected static RandomEngine cernGenerator;
	protected static java.util.Random generator;
	
	
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
	 * This method to convert the distribution enumerations to human readable
	 * name
	 * 
	 * @param d the distribution
	 * @return the "human name" for the distribution
	 */
	public static String distributionToName(DISTRIBUTION d) {
		if (d.equals(DISTRIBUTION.NORMAL)) {
			return "Standard Normal (Gaussian) distribution";
		} else if (d.equals(DISTRIBUTION.BETA)) {
			return "Beta distribution";
		} else {
			return "Uniform distribution";
		}
	}
	
	
	/**
	 * This method to convert the distribution name into enumerations 
	 * 
	 * @param name the name of the distribution
	 * @return the distribution enumeration
	 */
	public static DISTRIBUTION nameToDistribution(String name) {
		if (name.equals("Standard Normal (Gaussian) distribution")) {
			return DISTRIBUTION.NORMAL;
		} else if (name.equals("Beta distribution")) {
			return DISTRIBUTION.BETA;
		} else if (name.equals("Uniform distribution")) {
			return DISTRIBUTION.UNIFORM;
		} else {
			return null;
		}
	}
	
	
	/**
	 * This method generates a random double according to the probability
	 * distribution
	 * 
	 * @return the new random number
	 */
	public abstract Double nextDouble();
	
	
	/**
	 * Returns the function value
	 * 
	 * @param x the x-value
	 * @return the value (between 0 and 1)
	 */
	public abstract Double getValue(double x);
	
	
	/**
	 * Method for obtaining a set of values to be plotted
	 * 
	 * @param size the number of values to generate
	 * @return an array of values to be plotted
	 */
	public abstract Double[] getValuesForPlotting(int size);
	
	
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
	protected PlgProbabilityDistribution() {
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


	public static PlgProbabilityDistribution normalDistributionFactory() {
		return new NormalDistribution();
	}

	
	public static PlgProbabilityDistribution uniformDistributionFactory() {
		return new UniformDistribution();
	}
	
	
	public static PlgProbabilityDistribution betaDistributionFactory(double alpha, double beta) {
		return new BetaDistribution(alpha, beta);
	}
	
}
