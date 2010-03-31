package it.unipd.math.plg.test;

import it.unipd.math.plg.metrics.PlgProcessMeasures;
import it.unipd.math.plg.models.PlgActivity;
import it.unipd.math.plg.models.PlgObservation;
import it.unipd.math.plg.models.PlgProcess;
import it.unipd.math.plg.models.PlgProcess.COUNTER_TYPES;
import org.processmining.analysis.petrinet.structuredness.*;
import org.processmining.framework.models.ModelGraphVertex;
import org.processmining.framework.models.bpel.util.Pair;
import org.processmining.framework.models.bpel.util.Quadruple;
import org.processmining.framework.models.petrinet.Marking;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.State;
import org.processmining.framework.models.petrinet.StateSpace;
import org.processmining.framework.models.petrinet.algorithms.CoverabilityGraphBuilder;
import org.processmining.framework.models.petrinet.algorithms.InitialPlaceMarker;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.processmining.lib.mxml.LogException;

/**
 * Test case class
 * 
 * @author Andrea Burattin
 */
public class PlgTest {

	
	/**
	 * Main test case method
	 * 
	 * @param args command-line arguments provided by the user
	 */
	public static void main(String[] args) {
		try {
			
			PlgProcess p = new PlgProcess("test");

			/* *****************************************************************
			 * 
			 * A --> E --> C --> D --> B
			 *             `-----------'
			 */
			/*PlgActivity A = p.askNewActivity();
			PlgActivity B = p.askNewActivity();
			PlgActivity C = p.askNewActivity();
			PlgActivity D = p.askNewActivity();
			PlgActivity E = p.askNewActivity();
			A.addNext(E).addNext(C).addNext(D).addNext(B);
			B.addLoop(C);*/
			
			/* *****************************************************************
			 *
			 *         ,--> C --.
			 * A --> B            E --> F
			 *       | `--> D --' |
			 *        `-----------'
			 */
			/*PlgActivity A = p.askNewActivity();
			PlgActivity B = p.askNewActivity();
			PlgActivity C = p.askNewActivity();
			PlgActivity D = p.askNewActivity();
			PlgActivity E = p.askNewActivity();
			PlgActivity F = p.askNewActivity();
			p.setFirstActivity(A);
			p.setLastActivity(F);
			A.addNext(B);
			B.inXorUntil(E);
			E.addLoop(B);
			E.addNext(F);
			B.addNext(C).addNext(E);
			B.addNext(D).addNext(E);*/
			
			/* *****************************************************************
			 *
			 * A -> C -> B
			 *      |
			 */
			/*PlgActivity A = p.askNewActivity();
			PlgActivity B = p.askNewActivity();
			PlgActivity C = p.askNewActivity();
			A.addNext(C);
			C.addLoop(C);
			C.addNext(B);*/
			
			/* *****************************************************************
			 *
			 *        ,--> C --> G --> H --.
			 * A --> F     `-----------'    E --> B
			 *        `-----> D -----------'
			 */
			/*PlgActivity A = p.askNewActivity();
			PlgActivity B = p.askNewActivity();
			PlgActivity C = p.askNewActivity();
			PlgActivity D = p.askNewActivity();
			PlgActivity E = p.askNewActivity();
			PlgActivity F = p.askNewActivity();
			PlgActivity G = p.askNewActivity();
			PlgActivity H = p.askNewActivity();
			A.addNext(F);
			F.inXorUntil(E);
			E.addNext(B);
			F.addNext(C).addNext(G).addNext(H).addNext(E);
			F.addNext(D).addNext(E);
			H.addLoop(C);*/
			
			/* *****************************************************************
			 *
			 * A -> B -> C -> D
			 * `----'
			 */
//			PlgActivity A = p.askNewActivity();
//			PlgActivity B = p.askNewActivity();
//			PlgActivity C = p.askNewActivity();
//			PlgActivity D = p.askNewActivity();
//			A.addNext(B).addNext(C).addNext(D);
//			C.addLoop(A);
			
			
//			PlgActivity A = p.askNewActivity();
//			PlgActivity B = p.askNewActivity();
//			PlgActivity C = p.askNewActivity();
//			PlgActivity D = p.askNewActivity();
//			PlgActivity E = p.askNewActivity();
//			A.addNext(B).addNext(C);
//			A.inAndUntil(E);
//			A.addNext(B).addNext(E);
//			A.addNext(C).addNext(E);
//			A.addNext(D).addNext(E);
//			B.addLoop(B);

			p.randomize(3);
			
			String file = "/home/delas/desktop/asd.tpn";
			java.io.FileOutputStream o = new java.io.FileOutputStream(file);
			org.processmining.framework.plugin.ProvidedObject po = new org.processmining.framework.plugin.ProvidedObject("net", p.getPetriNet());
			org.processmining.exporting.petrinet.TpnExport e = new org.processmining.exporting.petrinet.TpnExport();
//			org.processmining.exporting.petrinet.PnmlExport e = new org.processmining.exporting.petrinet.PnmlExport();
			e.export(po, o);
			o.close();

			p.saveHeuristicsNetAsDot("/home/delas/desktop/prova.dot");
			p.savePetriNetAsDot("/home/delas/desktop/prova.petri.dot");
			String[] dotCmd = {"/bin/sh", "-c", "dot -Tpdf /home/delas/desktop/prova.dot > /home/delas/desktop/prova.pdf && dot -Tpdf /home/delas/desktop/prova.petri.dot > /home/delas/desktop/prova.petri.pdf"};
			Process dotExec = Runtime.getRuntime().exec(dotCmd);
			dotExec.waitFor();
			
			System.out.println("Process statistics");
			System.out.println("==================");
			System.out.println("       Max nested patters: " + p.getMaxDepth());
			System.out.println("             AND patterns: " + p.getPatternsCounter(COUNTER_TYPES.AND));
			System.out.println("             XOR patterns: " + p.getPatternsCounter(COUNTER_TYPES.XOR));
			System.out.println("        Sequence patterns: " + p.getPatternsCounter(COUNTER_TYPES.SEQUENCE));
			System.out.println(" Single activity patterns: " + p.getPatternsCounter(COUNTER_TYPES.ALONE));
			System.out.println("           Empty patterns: " + p.getPatternsCounter(COUNTER_TYPES.EMPTY));
			System.out.println("                    Loops: " + p.getPatternsCounter(COUNTER_TYPES.LOOP));
			System.out.println("         Max AND branches: " + p.getPatternsCounter(COUNTER_TYPES.MAX_AND_BRANCHES));
			System.out.println("         Max XOR branches: " + p.getPatternsCounter(COUNTER_TYPES.MAX_XOR_BRANCHES));
			System.out.println("         Total activities: " + p.getActivityList().size());
			System.out.println("Process hash: " + p.hashCode());
			
			
//			System.out.println("\nADJACENCY MATRIX");
//			System.out.println(  "================");
//			org.processmining.framework.models.heuristics.HeuristicsNet h = p.getHeuristicsNet();
//			System.out.print("  ");
//			for (int i = 0; i < h.size(); i++) {
//				System.out.print(h.getLogEvents().get(i).getModelElementName() + " ");
//			}
//			System.out.println("");
//			
//			boolean[][] m = p.getAdjacencyMatrix();
//			for (int i = 0; i < m.length; i++) {
//				System.out.print(h.getLogEvents().get(i).getModelElementName() + " ");
//				for (int j = 0; j < m.length; j++) {
//					if (m[i][j])
//						System.out.print("1 ");
//					else
//						System.out.print("0 ");
//				}
//				System.out.println("");
//			}
			
			
			
//			HeuristicsNetFromFile hnff = new HeuristicsNetFromFile(new FileInputStream("/home/delas/desktop/semantic.hn"));
//			FileWriter fw = new FileWriter("/home/delas/desktop/prova.dot");
//			hnff.getNet().writeToDot(fw);
//			fw.close();
//			
//			FileWriter fw2 = new FileWriter("/home/delas/desktop/prova.petri.dot");
//			ProvidedObject po = new ProvidedObject("net", hnff.getNet());
//			HNetToPetriNetConverter converter = new HNetToPetriNetConverter();
//			PetriNet petri = ((PetriNetResult) converter.convert(po)).getPetriNet();
//			petri.writeToDot(fw2);
//			fw2.close();
			
			
//			try {
//				p.saveAsNewLog("/home/delas/desktop/prova.zip", 1, 100, 0);
//			} catch (LogException e) {
//				e.printStackTrace();
//			}
			/*File temp = File.createTempFile("pattern", ".suffix");
			FileWriter fw = new FileWriter("/home/delas/desktop/test.hn");
			p.getHeuristicsNetFile(fw);
			fw.close();
			
			HeuristicsNetFromFile obj = new HeuristicsNetFromFile(new FileInputStream("/home/delas/desktop/test.hn"));
			HeuristicsNet net = obj.getNet();
			
			ProvidedObject po = new ProvidedObject("net", net);
			HNetToPetriNetConverter converter = new HNetToPetriNetConverter();
			PetriNet petri = ((PetriNetResult) converter.convert(po)).getPetriNet();
			
			FileWriter fw1 = new FileWriter("/home/delas/desktop/test-hn.dot");
			net.writeToDot(fw1);
			fw1.close();
			
			FileWriter fw2 = new FileWriter("/home/delas/desktop/test.dot");
			petri.writeToDot(fw2);
			fw2.close();*/
			
			/* ************************************************************** */
			System.out.println("\nPROCESS COMPLEXITY");
			System.out.println(  "==================");
		
			PlgProcessMeasures measures = null;
//			try {
				measures = p.getProcessMeasures();
				System.out.println("Cardoso Metric: " + measures.getCardosoMetric());
				System.out.println("Cyclomatic Metric: " + measures.getCyclomaticMetric());
//				System.out.println("My Metric: " + measures.getMyMetric());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			
			/* ************************************************************** */
//			System.out.println("\nPROCESS INSTANCES");
//			System.out.println(  "=================");
//			for (Iterator<PlgObservation> i = p.getFirstActivity().generateInstance(0).iterator(); i.hasNext();) {
//				PlgObservation o = i.next();
//				System.out.println(o);
//			}
			
//			p.saveAsDot("/home/delas/desktop/test.dot");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
