package it.unipd.math.plg.test;

import it.unipd.math.plg.models.PlgActivity;
import it.unipd.math.plg.models.PlgProcess;
import it.unipd.math.plg.models.distributions.PlgProbabilityDistribution;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * @author Andrea Burattin
 *
 */
public class PlgTest2 {
	
	/**
	 * @param arg
	 * @throws IOException
	 */
	public static void main(String[] arg) throws IOException {

		
//		PlgProcess p = new PlgProcess("test");
//		PlgActivity A = p.askNewActivity();
//		PlgActivity B = p.askNewActivity();
//		PlgActivity C = p.askNewActivity();
//		PlgActivity D = p.askNewActivity();
//		PlgActivity E = p.askNewActivity();
//		PlgActivity F = p.askNewActivity();
//		
//		A.inAndUntil(F);
//		A.addNext(B).addNext(F);
//		A.addNext(C).addNext(F);
//		A.addNext(D).addNext(F);
//		A.addNext(E).addNext(F);
////		A.setRandomWeights(PlgProbabilityDistribution.normalDistributionFactory());
//		
//		HashMap<PlgActivity, Double> prob = new HashMap<PlgActivity, Double>();
//		prob.put(B, 0.4);
//		prob.put(C, 0.4);
//		prob.put(D, 0.1);
//		prob.put(E, 0.1);
////		A.setProbabilityOfEdges(prob);
////		
////		System.out.println(A.getProbabilityOfEdges());
////		HashMap<String, Integer> counter = new HashMap<String, Integer>();
////		for (int i = 0; i < 1000; i++) {
////			ArrayList<PlgActivity> a = PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges());
////			String aName = a.get(0).getName();
////			if (counter.containsKey(aName)) 
////				counter.put(aName, counter.get(aName) + 1);
////			else
////				counter.put(aName, 1);
////		}
////		System.out.println(counter);
////		System.out.println(PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges()));
////		System.out.println(PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges()));
////		System.out.println(PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges()));
////		System.out.println(PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges()));
////		System.out.println(PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges()));
////		System.out.println(PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges()));
////		System.out.println(PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges()));
////		System.out.println(PlgProbabilityDistribution.getSortedActivities(A.getProbabilityOfEdges()));
//		System.out.println(A.generateInstance(0));
		
		
		java.util.HashMap<String, Integer> a = new java.util.HashMap();
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		
		PlgProbabilityDistribution pd = null;
		pd = PlgProbabilityDistribution.uniformDistributionFactory();
		for(int i = 0; i < 10000; i++){
			Integer v = pd.nextInt(0, 10);
			String d = twoDForm.format(v);
			if(a.containsKey(d)) {
				a.put(d, ((Integer)a.get(d)) + 1);
			} else {
				a.put(d, 1);
			}
		}
		
//		for(int i = 0; i < 10000; i++){
//			Double v;
//			do {
//				v = pd.nextDouble();
//			} while (v > 1.0 || v < 0.0);
//			String d = twoDForm.format(v);
//			if(a.containsKey(d)) {
//				a.put(d, ((Integer)a.get(d)) + 1);
//			} else {
//				a.put(d, 1);
//			}
//		}
		
		for (String g : a.keySet()) {
			System.out.println(g + "\t" + a.get(g));
		}
	}
	
}
