package it.unipd.math.plg.models;

import it.unipd.math.plg.models.PlgActivity.RELATIONS;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.processmining.framework.util.Pair;
import org.deckfour.xes.classification.XEventClass;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramImpl;
import org.processmining.models.graphbased.directed.bpmn.elements.Activity;

//import org.processmining.framework.models.ModelGraph;

/**
 * This class is used for the generation of dependency graph.
 * 
 * @author Andrea Burattin
 * @version 0.2
 */
public class PlgDependencyGraph extends BPMNDiagramImpl {

	PlgProcess p;
	DecimalFormat twoDForm = new DecimalFormat(".##");
	
	
	/**
	 * Class constructor
	 * 
	 * @param graphName the name of the graph
	 * @param process the process that the dependency graph represents
	 */
	public PlgDependencyGraph(String graphName, PlgProcess process) {
		super(graphName);
		this.p = process;
		build();
	}
	
	
	private void build() {
		Vector<PlgActivity> activityList = p.getActivityList();
		XEventClass[] activities = new XEventClass[activityList.size()];
		for (int i = 0; i < activityList.size(); i++) {
			activities[i] = new XEventClass(activityList.get(i).getName(), i);
		}
		Activity[] graphActivities = new Activity[activities.length];
		Set<Pair<Activity, Activity>> existingFlows = new HashSet<Pair<Activity, Activity>>();
		
		// activities
		for (int i = 0; i < activities.length; i++) {
			//adding the activities
			graphActivities[i] = addActivity(activities[i].toString(), false, false, false, false, false);
		}
		
		// connections
		for (int i = 0; i < activities.length; i++) {
			// for every activity, get its output subsets and add a flow to each
			// element in output
//			HNSet outputActivitiesSet = net.getOutputSet(activityIndex);
			Set<PlgActivity> outputActivitiesSet = activityList.get(i).getRelationsTo();
			for(PlgActivity subset : outputActivitiesSet) {
//			for (int iOut = 0; iOut < outputActivitiesSet.size(); iOut++) {
//				HNSubSet subset = outputActivitiesSet.get(iOut);
//				for (int outputActivityIndex = 0; outputActivityIndex < subset.size(); outputActivityIndex++) {
					Pair<Activity, Activity> pair = new Pair<Activity, Activity>(graphActivities[i],
							graphActivities[1]);
					if (!existingFlows.contains(pair)) {
						addFlow(graphActivities[i],
								graphActivities[1], null);
						existingFlows.add(pair);
					}
//				}
			}

		}
	}
	
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
