package it.unipd.math.plg.metrics;

import it.unipd.math.plg.models.PlgProcess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.processmining.framework.models.ModelGraphVertex;
import org.processmining.framework.models.bpel.util.Pair;
import org.processmining.framework.models.bpel.util.Quadruple;
import org.processmining.framework.models.petrinet.Marking;
import org.processmining.framework.models.petrinet.PNEdge;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.PetriNetNavigation;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.State;
import org.processmining.framework.models.petrinet.StateSpace;
import org.processmining.framework.models.petrinet.Transition;
import org.processmining.framework.models.petrinet.algorithms.CoverabilityGraphBuilder;
import org.processmining.framework.models.petrinet.algorithms.InitialPlaceMarker;

import att.grappa.Node;

/**
 * This class is used to fetch some metric about the process. In particual, at
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
 * @author Andrea Burattin
 * @version 0.3
 */
public class PlgMetricCalculator {

	private PlgProcess originalProcess;
	private PetriNet petriNet;
	// data required by calculateCyclomaticMetric method
	private static final int INFINITE_SS = -1, UNFINISHED_CG = -2;
	
	
	/**
	 * 
	 * @param originalProcess
	 * @throws IOException
	 */
	public PlgMetricCalculator(PlgProcess originalProcess) throws IOException {
		this.originalProcess = originalProcess;
		this.petriNet = originalProcess.getPetriNet();
	}
	
	
	/**
	 * This method does the real calculation of the metric
	 * 
	 * @return the metric container object
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public PlgProcessMeasures calculate() throws FileNotFoundException, Exception {
		PetriNet net = petriNet;
		
		// Cardoso Metric
		Pair<Integer, List<Pair<Place, Set<Set<Place>>>>> tmpResult1 = calculateCardosoMetric(net);
		int cardosoMetric = tmpResult1.first;
		
		// Cyclomatic Metric
		Pair<Integer, Quadruple<StateSpace, Integer, Integer, List<List<ModelGraphVertex>>>> tmpResult2 = calculateCyclomaticMetric(net);
		int cyclomaticMetric = tmpResult2.first;

		return new PlgProcessMeasures(originalProcess, cyclomaticMetric, cardosoMetric);
	}
	
	
	/**
	 * 
	 * @param net
	 * @return
	 */
	private Pair<Integer, List<Pair<Place, Set<Set<Place>>>>> calculateCardosoMetric(PetriNet net) {
		int result = 0;
		List<Pair<Place, Set<Set<Place>>>> calculation = new ArrayList<Pair<Place, Set<Set<Place>>>>();
		for (Place place : net.getPlaces()) {
			TreeSet<Set<Place>> nextStates = new TreeSet<Set<Place>>(
					new Comparator<Set<Place>>() {
						public int compare(Set<Place> arg0, Set<Place> arg1) {
							if (arg0.size() > arg1.size())
								return -1;
							else if (arg0.size() < arg1.size())
								return 1;
							else if (arg0.containsAll(arg1))
								return 0;
							else
								return 1;
						}
					});
			for (Transition transition : PetriNetNavigation
					.getOutgoingTransitions(place)) {
				Vector<Place> outgoingPlaces = PetriNetNavigation
						.getOutgoingPlaces(transition);
				nextStates.add(new LinkedHashSet<Place>(outgoingPlaces));
			}
			result += nextStates.size();
			calculation.add(Pair.create(place, (Set<Set<Place>>) nextStates));
		}
		return Pair.create(result, calculation);
	}
	
	
	/**
	 * 
	 * @param net
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Pair<Integer, Quadruple<StateSpace, Integer, Integer, List<List<ModelGraphVertex>>>>
		calculateCyclomaticMetric(PetriNet net) {
		Node source = net.getSource();
		Node sink = net.getSink();
		if (source instanceof Transition || sink instanceof Transition) {
			PetriNet clone = (PetriNet) net.clone();
			if (source instanceof Transition) {
				source = clone.getSource();
				Place dummy = new Place("dummySource", clone);
				clone.addPlace(dummy);
				clone.addAndLinkEdge(new PNEdge(dummy, (Transition) source),
						dummy, (Transition) source);
			}
			if (sink instanceof Transition) {
				sink = clone.getSink();
				Place dummy = new Place("dummySink", clone);
				clone.addPlace(dummy);
				clone.addAndLinkEdge(new PNEdge((Transition) sink, dummy),
						(Transition) sink, dummy);
			}
			net = clone;
		}
		InitialPlaceMarker.mark(net, 1);
		StateSpace ss = null;
		try {
			ss = CoverabilityGraphBuilder.build(net);
			for (ModelGraphVertex v : ss.getVerticeList()) {
				State s = (State) v;
				Marking m = s.getMarking();
				Iterator i = m.iterator();
				while (i.hasNext()) {
					Place p = (Place) i.next();
					if (m.getTokens(p) == Marking.OMEGA)
						return Pair.create(
								INFINITE_SS,
								(Quadruple<StateSpace, Integer, Integer, List<List<ModelGraphVertex>>>) null);
				}
			}
		} catch (OutOfMemoryError ome) {
			return Pair.create(
					UNFINISHED_CG,
					(Quadruple<StateSpace, Integer, Integer, List<List<ModelGraphVertex>>>) null);
		}
		
		List<List<ModelGraphVertex>> stronglyConnectedComponents = ss.getStronglyConnectedComponents();
		return Pair.create(ss.getEdges().size() - ss.getVerticeList().size()
				+ stronglyConnectedComponents.size(),
				Quadruple.create(
						ss,
						ss.getEdges().size(),
						ss.getVerticeList().size(),
						stronglyConnectedComponents));
	}
}
