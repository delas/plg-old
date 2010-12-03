package it.unipd.math.plg.test;

import static java.util.Arrays.asList;
import it.unipd.math.plg.metrics.PlgProcessMeasures;
import it.unipd.math.plg.models.PlgParameters;
import it.unipd.math.plg.models.PlgProcess;
import it.unipd.math.plg.models.PlgProcess.COUNTER_TYPES;
import it.unipd.math.plg.models.distributions.PlgProbabilityDistribution;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.processmining.exporting.petrinet.TpnExport;
import org.processmining.framework.plugin.ProvidedObject;
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
			
//			PlgProcess p = new PlgProcess("test");

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
			/*PlgActivity A = p.askNewActivity();
			PlgActivity B = p.askNewActivity();
			PlgActivity C = p.askNewActivity();
			PlgActivity D = p.askNewActivity();
			A.addNext(B).addNext(C).addNext(D);
			B.addLoop(A);*/
			
//			it.unipd.math.plg.models.PlgActivity A = p.askNewActivity();
//			it.unipd.math.plg.models.PlgActivity B = p.askNewActivity();
//			it.unipd.math.plg.models.PlgActivity C = p.askNewActivity();
//			it.unipd.math.plg.models.PlgActivity D = p.askNewActivity();
//			it.unipd.math.plg.models.PlgActivity E = p.askNewActivity();
//			it.unipd.math.plg.models.PlgActivity F = p.askNewActivity();
//			it.unipd.math.plg.models.PlgActivity G = p.askNewActivity();
//			it.unipd.math.plg.models.PlgActivity H = p.askNewActivity();
//			it.unipd.math.plg.models.PlgActivity I = p.askNewActivity();
			
//			A.addNext(B);
//			B.inAndUntil(E);
//				B.addNext(C).addNext(E);
//				B.addNext(D).addNext(E);
//			E.addNext(F);
//			F.inXorUntil(B);
//				F.addNext(G);
//				F.addNext(H).addNext(B);

			PlgParameters parameters = new PlgParameters(
					5,  // max and branches
                    PlgProbabilityDistribution.normalDistributionFactory(),
                    5,  // max xor branches
                    PlgProbabilityDistribution.normalDistributionFactory(),
                    20, // loop prob
                    60, // single act prob
                    70, // sequence act prob
                    35, // and prob
                    35, // xor prob
                    3, // deep
                    PlgProbabilityDistribution.normalDistributionFactory(),
                    PlgProbabilityDistribution.normalDistributionFactory()
			);
			PlgProcess p = new PlgProcess("aa");
			p.randomize(parameters);
//			p.saveProcessAs("/home/delas/desktop/asd.plg");
//			PlgProcess p = PlgProcess.loadProcessFrom("/home/delas/desktop/asd.plg");
			
			String baseOutputPath1 = System.getProperty("user.dir") + "/test/";
			String finalOutputPath1 = baseOutputPath1.concat("test");//baseOutputPath1.concat(new Integer(p.hashCode()).toString());
			p.saveHeuristicsNetAsDot(finalOutputPath1 + ".hn.dot");
			p.savePetriNetAsDot(finalOutputPath1 + ".pn.dot");
			p.saveDependencyGraphAsDot(finalOutputPath1 + ".dg.dot");
			String[] dotCmd1 = {"/bin/sh", "-c",
					"dot -Tpdf " + finalOutputPath1 + ".hn.dot > " + finalOutputPath1 + ".hn.pdf && " +
					"dot -Tpdf " + finalOutputPath1 + ".pn.dot > " + finalOutputPath1 + ".pn.pdf && " +
					"dot -Tpdf " + finalOutputPath1 + ".dg.dot > " + finalOutputPath1 + ".dg.pdf"};
			Process dotExec1 = Runtime.getRuntime().exec(dotCmd1);
			dotExec1.waitFor();
			System.out.println("\nOUTPUT GENERATION");
			System.out.println(  "=================");
			System.out.println("        Petri Net file: " + finalOutputPath1 + ".tpn");
			System.out.println("     Dot for Petri Net: " + finalOutputPath1 + ".pn.dot");
			System.out.println("Dot for Heuristics Net: " + finalOutputPath1 + ".hn.dot");
			System.out.println("      PDF of Petri Net: " + finalOutputPath1 + ".pn.pdf");
			System.out.println(" PDF of Heuristics Net: " + finalOutputPath1 + ".hn.pdf");
			System.exit(0);
			
			
			/* ****************************************************************
			 * Own process generation
			 */
			OptionParser parser = new OptionParser() {
				{
					acceptsAll(asList("a", "adjacency-matrix"),
							"Print the process as an adjacency matrix");
					acceptsAll(asList("c", "complexity"))
							.withRequiredArg().ofType(Integer.class)
							.describedAs("The generated process complexity")
							.defaultsTo(1);
					acceptsAll(asList("h", "?"),
							"Show this help");
					acceptsAll(asList("i", "instances"))
							.withRequiredArg().ofType(Integer.class)
							.describedAs("The number of instances to generate")
							.defaultsTo(100);
					acceptsAll(asList("m", "metric"),
							"Print some metrics of the generated process");
					acceptsAll(asList("o", "output-to-file"),
							"With this parameter specified, some output files are produces");
					acceptsAll(asList("s", "stats"),
							"Print some statistics of the generated process");
					
				}
			};
			OptionSet options = parser.parse(args);
			int complexity = ((Integer)options.valueOf("c")).intValue();
			boolean printHelp = options.has("?");
			boolean printStats = options.has("s");
			boolean printAdjMatrix = options.has("a");
			boolean printMetrics = options.has("m");
			boolean outputToFile = options.has("o");
			boolean generateInstances = options.has("i");
			
	        if (printHelp || args.length == 0) {
	        	System.out.println("PLGLib TESTING TOOL");
				System.out.println("===================");
				System.out.println("");
				System.out.println("Version: " + PlgProcess.version);
				System.out.println("");
				System.out.println("This software has been written just as a `proof of concept' for the PLGLib");
				System.out.println("library. For a complete documentation see: http://www.processmining.it.");
				System.out.println("");
	            parser.printHelpOn(System.out);
	            System.exit(0);
	        }

	        System.out.print("Generating process... ");
			p.randomize(complexity);
			System.out.println("done!");
			
			if (outputToFile) {
				String baseOutputPath = System.getProperty("user.dir") + "/test/";
				File f = new File(baseOutputPath);
				if (!f.exists()) {
					f.mkdir();
				}
				baseOutputPath = baseOutputPath.concat(new Integer(p.hashCode()).toString());
				// save the process file
				boolean processSaved = p.saveProcessAs(baseOutputPath + ".plg");
				// save the Petri Net file
				FileOutputStream o = new FileOutputStream(baseOutputPath + ".tpn");
				ProvidedObject po = new ProvidedObject("net", p.getPetriNet());
				TpnExport e = new TpnExport();
				e.export(po, o);
				o.close();
				// save the dot of the HN and of the PN
				p.saveHeuristicsNetAsDot(baseOutputPath + ".hn.dot");
				p.savePetriNetAsDot(baseOutputPath + ".pn.dot");
				String[] dotCmd = {"/bin/sh", "-c",
						"dot -Tpdf " + baseOutputPath + ".hn.dot > " + baseOutputPath + ".hn.pdf && " +
						"dot -Tpdf " + baseOutputPath + ".pn.dot > " + baseOutputPath + ".pn.pdf"};
				Process dotExec = Runtime.getRuntime().exec(dotCmd);
				int returnOfDot = dotExec.waitFor();
				System.out.println("\nOUTPUT GENERATION");
				System.out.println(  "=================");
				if (processSaved) {
					System.out.println("          Process file: " + baseOutputPath + ".plg");
				}
				System.out.println("        Petri Net file: " + baseOutputPath + ".tpn");
				System.out.println("     Dot for Petri Net: " + baseOutputPath + ".pn.dot");
				System.out.println("Dot for Heuristics Net: " + baseOutputPath + ".hn.dot");
				if (returnOfDot == 0) {
					System.out.println("      PDF of Petri Net: " + baseOutputPath + ".pn.pdf");
					System.out.println(" PDF of Heuristics Net: " + baseOutputPath + ".hn.pdf");
				}
			}
			if (generateInstances) {
				int instances = ((Integer)options.valueOf("i")).intValue();
				String baseOutputPath = System.getProperty("user.dir") + "/test/";
				File f = new File(baseOutputPath);
				if (!f.exists()) {
					f.mkdir();
				}
				baseOutputPath = baseOutputPath.concat(new Integer(p.hashCode()).toString());
				try {
					p.saveAsNewLog(baseOutputPath + ".log.zip", instances);
					if (!outputToFile) {
						System.out.println("\nOUTPUT GENERATION");
						System.out.println(  "=================");
					}
					System.out.println("  ZIPped instances log: " + baseOutputPath + ".log.zip");
				} catch (LogException e) {
					e.printStackTrace();
				}
			}

			if (printStats) {
				System.out.println("\nPROCESS STATISTICS");
				System.out.println(  "==================");
				System.out.println("             AND patterns: " + p.getPatternsCounter(COUNTER_TYPES.AND));
				System.out.println("             XOR patterns: " + p.getPatternsCounter(COUNTER_TYPES.XOR));
				System.out.println("                    Loops: " + p.getPatternsCounter(COUNTER_TYPES.LOOP));
				System.out.println("         Max AND branches: " + p.getPatternsCounter(COUNTER_TYPES.MAX_AND_BRANCHES));
				System.out.println("         Max XOR branches: " + p.getPatternsCounter(COUNTER_TYPES.MAX_XOR_BRANCHES));
				System.out.println("         Total activities: " + p.getActivityList().size());
				System.out.println("        Process hash code: " + p.hashCode());
			}
			
			if (printAdjMatrix) {
				System.out.println("\nADJACENCY MATRIX");
				System.out.println(  "================");
				org.processmining.framework.models.heuristics.HeuristicsNet h = p.getHeuristicsNet();
				System.out.print("  ");
				for (int i = 0; i < h.size(); i++) {
					System.out.print(h.getLogEvents().get(i).getModelElementName() + " ");
				}
				System.out.println("");
				boolean[][] m = p.getAdjacencyMatrix();
				for (int i = 0; i < m.length; i++) {
					System.out.print(h.getLogEvents().get(i).getModelElementName() + " ");
					for (int j = 0; j < m.length; j++) {
						if (m[i][j])
							System.out.print("1 ");
						else
							System.out.print("0 ");
					}
					System.out.println("");
				}
			}
			
			if (printMetrics) {
				PlgProcessMeasures measures = p.getProcessMeasures();
				System.out.println("\nPROCESS COMPLEXITY");
				System.out.println(  "==================");
				System.out.println("   Cardoso Metric: " + measures.getCardosoMetric());
				System.out.println("Cyclomatic Metric: " + measures.getCyclomaticMetric());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
