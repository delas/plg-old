package it.unipd.math.plg.test;

import it.unipd.math.plg.models.PlgObservation;
import it.unipd.math.plg.models.PlgProcess;
import it.unipd.math.plg.models.PlgProcess.COUNTER_TYPES;

import java.io.IOException;
import java.util.Iterator;

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
			 * A -> B -> C -> D -> E
			 * `----'
			 */
			/*PlgActivity A = p.askNewActivity();
			PlgActivity B = p.askNewActivity();
			PlgActivity C = p.askNewActivity();
			A.addNext(B).addNext(C);
			B.addLoop(A);*/

			
			p.randomize(3);
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
			
			
//			p.saveAsNewLog("/home/delas/desktop/prova.zip", 10);
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
