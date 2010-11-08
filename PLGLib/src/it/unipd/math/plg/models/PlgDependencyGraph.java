package it.unipd.math.plg.models;

import it.unipd.math.plg.models.PlgActivity.RELATIONS;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;

//import org.processmining.framework.models.ModelGraph;

/**
 * This class is used for the generation of dependency graph.
 * 
 * @author Andrea Burattin
 * @version 0.1
 */
public class PlgDependencyGraph /*extends ModelGraph*/ {

//	PlgProcess p;
//	DecimalFormat twoDForm = new DecimalFormat(".##");
//	
//	
//	/**
//	 * Class constructor
//	 * 
//	 * @param graphName the name of the graph
//	 * @param process the process that the dependency graph represents
//	 */
//	public PlgDependencyGraph(String graphName, PlgProcess process) {
//		super(graphName);
//		this.p = process;
//	}
//	
//	
//	public void writeToDot(Writer bw) throws IOException {
//		nodeMapping.clear();
//
//		bw.write("digraph G {\n");
//		bw.write("\trankdir=\"LR\";\n");
//		bw.write("\tfontname=\"Arial\"; fontsize=\"14\";\n");
//		bw.write("\tnode [shape=\"none\"];\n");
//
//		for (PlgActivity s : p.getActivityList()) {
//			String extra = "";
//			for (PlgActivity d : s.getRelationsTo()) {
//				if (s.getRelationType() == RELATIONS.AND_SPLIT ||
//						s.getRelationType() == RELATIONS.XOR_SPLIT) {
//					Double weight = s.getProbabilityOfEdges().get(d);
//					extra = "[label=\"" + twoDForm.format(weight) + "\"]";
//				}
//				bw.write("\t" + s.getName() + " -> " + d.getName() + extra + ";\n");
//			}
//		}
//		bw.write("}\n");
//	}

}
