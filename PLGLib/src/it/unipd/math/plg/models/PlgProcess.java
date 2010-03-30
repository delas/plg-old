package it.unipd.math.plg.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import org.processmining.converting.HNetToPetriNetConverter;
import org.processmining.framework.models.heuristics.HeuristicsNet;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.importing.heuristicsnet.HeuristicsNetFromFile;
import org.processmining.lib.mxml.AuditTrailEntry;
import org.processmining.lib.mxml.LogException;
import org.processmining.lib.mxml.writing.Process;
import org.processmining.lib.mxml.writing.ProcessInstance;
import org.processmining.lib.mxml.writing.ProcessInstanceType;
import org.processmining.lib.mxml.writing.impl.LogSetRandomImpl;
import org.processmining.lib.mxml.writing.persistency.LogPersistencyZip;
import org.processmining.mining.petrinetmining.PetriNetResult;

/**
 * This class describres a general process.
 * 
 * Actually a process contains its name and its first activity. With this
 * modelling, since each activity contains a set of related activities, we have
 * all the process described as a "linked list" where each node is an activity
 * and the edge is typed (describing the relation as sequence, AND split/join 
 * and XOR split/join).
 * 
 * @author Andrea Burattin
 * @version 0.3
 */
public class PlgProcess {

	/** This is the random number generator */
	public static Random generator = new Random();
	
	private String name;
	private PlgActivity firstActivity = null;
	private PlgActivity lastActivity = null;
	private Vector<PlgActivity> activityList = null;
	private int activityGenerator = 'A' - 1;
	private HeuristicsNet heuristicsNet = null;
	private int maxDepth = 0;
	private HashMap<COUNTER_TYPES, Integer> statsCounter;
	
	/**
	 * This enum describes the possible stats counter for the pattern an other
	 * process entities
	 */
	public static enum COUNTER_TYPES {
		/** This indicates the number of loops */
		LOOP,
		/** This indicates the number of single activities */
		ALONE,
		/** This indicates the number of sequence of activities */
		SEQUENCE,
		/** This indicates the number of AND splits */
		AND,
		/** This indicates the number of XOR splits */
		XOR,
		/** This indicates the maximum number of AND branches */
		MAX_AND_BRANCHES,
		/** This indicates the maximum number of XOR branches */
		MAX_XOR_BRANCHES,
		/** This indicates the number of empty patter */
		EMPTY
	}

	
	/**
	 * Default class constructor
	 * 
	 * @param name the new process name
	 * @param starting the first activity of the process
	 */
	public PlgProcess(String name, PlgActivity starting) {
		setName(name);
		this.firstActivity = starting;
		this.activityList = new Vector<PlgActivity>();
		this.statsCounter = new HashMap<COUNTER_TYPES, Integer>();
	}
	
	
	/**
	 * Default class constructor
	 * 
	 * @param name the new process name
	 */
	public PlgProcess(String name) {
		this(name, null);
	}

	
	/**
	 * Sets the process name
	 * 
	 * @param name the new process name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Gets the process name
	 * 
	 * @return the process name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Gets the first process activity
	 * 
	 * @return the first activity
	 */
	public PlgActivity getFirstActivity() {
		return firstActivity;
	}


	/**
	 * Sets the first process activity
	 * 
	 * @param firstActivity the new first process activity
	 */
	public void setFirstActivity(PlgActivity firstActivity) {
		this.firstActivity = firstActivity;
	}
	
	
	/**
	 * Gets the last process activity
	 * 
	 * @return the last activity
	 */
	public PlgActivity getLastActivity() {
		return lastActivity;
	}


	/**
	 * Sets the last process activity
	 * 
	 * @param lastActivity the new last process activity
	 */
	public void setLastActivity(PlgActivity lastActivity) {
		this.lastActivity = lastActivity;
	}
	
	
	/**
	 * Gets the activity list
	 * 
	 * @return the activity list
	 */
	public Vector<PlgActivity> getActivityList() {
		return activityList;
	}
	
	
	/**
	 * Asks the process for a new random activity. If the process has not a
	 * starting activity, the first call to this method will consider the new
	 * activity as the start of the process.
	 *
	 * @return a new activity with a random name
	 */
	public PlgActivity askNewActivity() {
		String actName = null;
		activityGenerator++;
		if (activityGenerator > 'Z') {
			double generator = activityGenerator - 'A';
			actName = new String();
			while (generator != 0) {
				int division = (int) Math.floor(generator / 26);
				int remainder = (int) (generator % 26);
				generator = division;
				Character c = (char)(remainder + 'A');
				actName = c + actName;
			}
		} else {
			actName = new String(new char[]{(char)activityGenerator});
		}
		
		PlgActivity a = new PlgActivity(this, actName);
		
		if (firstActivity == null) {
			setFirstActivity(a);
		} else if (lastActivity == null) {
			setLastActivity(a);
		}
		// refresh heuristics net
		heuristicsNet = null;
		
		return a;
	}

	
	/**
	 * Saves the current Petri Net process model as a Dot file
	 * 
	 * @param filename the destination filename
	 * @throws IOException
	 */
	public void savePetriNetAsDot(String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		getPetriNet().writeToDot(fw);
		fw.close();
	}

	
	/**
	 * Saves the current Petri Net process model as a Dot file
	 * 
	 * @param filename the destination filename
	 * @throws IOException
	 */
	public void saveHeuristicsNetAsDot(String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		getHeuristicsNet().writeToDot(fw);
		fw.close();
	}
	
	
	/**
	 * Generates and saves a new instance of the process. This method saves the
	 * log in a zip file format. This method can generate activitities as time
	 * interval or as time points (in this case)
	 * 
	 * @param filename the destination zip file (must finishes with .zip)
	 * @param cases the number of cases to generate
	 * @param percentAsInterval the percentual of activities as time interval
	 * @param percentErrors the percentage of log traces with errors
	 * @throws IOException
	 * @throws LogException
	 */
	public void saveAsNewLog(String filename, int cases, int percentAsInterval, int percentErrors) throws IOException, LogException {
		cases = (cases < 1)? 1 : cases;
		percentAsInterval = (percentAsInterval < 0 || percentAsInterval > 100)? 100 : percentAsInterval;
		percentErrors = (percentErrors < 0 || percentErrors > 100)? 0 : percentErrors;
		
		File file = new File(filename);
		LogPersistencyZip logFilter = new LogPersistencyZip(file);
		LogSetRandomImpl logSet = new LogSetRandomImpl(logFilter, "ProcessLogGenerator", "", 10);
		
		while (cases-- > 0) {
			Vector<PlgObservation> v = firstActivity.generateInstance(0);
			for (int i = 0; i < v.size(); i ++) {
				if (generator.nextInt(100) < percentErrors) {
					/* There must be an error! In this context, an error is the
					 * swap between two activities' times
					 */
					PlgObservation o1 = v.get(i);
					int randomIndex;
					do {
						randomIndex = generator.nextInt(v.size());
					} while (randomIndex != i);
					PlgObservation o2 = v.get(randomIndex);
					// swap starting time
					int o1StartingTime = o1.getStartingTime();
					o1.setStartingTime(o2.getStartingTime());
					o2.setStartingTime(o1StartingTime);
				}
				PlgObservation o = v.get(i);
				String processName = o.getActivity().getProcess().getName();
				String caseId = "instance_" + cases;
				boolean asInterval = generator.nextInt(101) <= percentAsInterval;
				Process proc = logSet.getProcess(processName);
				ProcessInstance pi = proc.getProcessInstance(caseId, ProcessInstanceType.ENACTMENT_LOG);
				AuditTrailEntry[] ate = o.getAuditTrailEntry(asInterval);
				pi.addAuditTrailEntry(ate[0]);
				pi.addAuditTrailEntry(ate[1]);
			}
		}
		
		logSet.finish();
	}
	
	
	/**
	 * Generates and saves a new instance of the process. This method saves the
	 * log in a zip file format. This method can generate activitities as time
	 * interval or as time points (in this case)
	 * 
	 * @param filename the destination zip file (must finishes with .zip)
	 * @param cases the number of cases to generate
	 * @param percentAsInterval the percentual of activities as time interval
	 * @throws IOException
	 * @throws LogException
	 */
	public void saveAsNewLog(String filename, int cases, int percentAsInterval) throws IOException, LogException {
		saveAsNewLog(filename, cases, percentAsInterval, 0);
	}
	
	
	/**
	 * Generates and saves a new instance of the process. This method saves the
	 * log in a zip file format. This method can generate activitities as time
	 * interval or as time points (in this case)
	 * 
	 * @param filename the destination zip file (must finishes with .zip)
	 * @param cases the number of cases to generate
	 * @throws IOException
	 * @throws LogException
	 */
	public void saveAsNewLog(String filename, int cases) throws IOException, LogException {
		saveAsNewLog(filename, cases, 100);
	}
	
	
	/**
	 * This method returns the Heuristics Net associated to the process
	 * 
	 * @return the Heuristics Net associated to the process
	 */
	public HeuristicsNet getHeuristicsNet() {
		
		if (heuristicsNet != null) {
			return heuristicsNet;
		}
		
		try {
			File tempFile = File.createTempFile("temporary-heuristics", ".dot");
			tempFile.deleteOnExit();
			FileWriter os = new FileWriter(tempFile);

			String separator = "/////////////////////\n";
			String file = "";
			String activityListString = "";
			String firstActivityString = "";
			String lastActivityString = "";

			// start and finish activities, and activity list
			for (int i = 0; i < activityList.size(); i++) {
				PlgActivity current = activityList.get(i);
				activityListString += current.getName() + ":@" + i + "&\n";
				if (current.equals(firstActivity)) {
					firstActivityString += i + "@\n";
				}
				if (current.equals(lastActivity)) {
					lastActivityString += i + "@\n";
				}
			}
			file = 
				separator + 
				firstActivityString + 
				separator + 
				lastActivityString +
				separator + 
				activityListString +
				separator;

			os.write(file);

			// activities sequence
			firstActivity.getHeuristicsNetFile(os, new Stack<PlgActivity>());
			os.close();

			HeuristicsNetFromFile hnff = new HeuristicsNetFromFile(new FileInputStream(tempFile));
			heuristicsNet = hnff.getNet();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return heuristicsNet;
	}
	
	
	/**
	 * This method returns the Petri Net associated to the process 
	 * 
	 * @return the Petri Net associated to the process
	 * @throws IOException
	 */
	public PetriNet getPetriNet() throws IOException {
		ProvidedObject po = new ProvidedObject("net", getHeuristicsNet());
		HNetToPetriNetConverter converter = new HNetToPetriNetConverter();
		PetriNet petri = ((PetriNetResult) converter.convert(po)).getPetriNet();
		return petri;
	}
	
	
	/**
	 * This method returns the number of instances per activity pattern
	 * 
	 * @return a hash map with the association pattern > counter
	 */
	public HashMap<COUNTER_TYPES, Integer> getPatternsCounter() {
		return statsCounter;
	}
	
	
	/**
	 * This method returns the number of instances of a particular pattern or,
	 * more generally some statistics about the process
	 * 
	 * @param pattern the name of the pattern
	 * @return the number of instances of a particular pattern
	 */
	public Integer getPatternsCounter(COUNTER_TYPES pattern) {
		Integer i = statsCounter.get(pattern);
		if (i == null) {
			return 0;
		}
		return i;
	}
	
	
	/**
	 * This method returns the maximum network depth
	 * 
	 * @return the maximum network depth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}
	
	
	@Override
	public int hashCode() {
		return getHeuristicsNet().hashCode();
	}
	
	
	/**
	 * This method sets the number of times a pattern is used or, more
	 * generally some statistics about the process
	 * 
	 * @param pattern the name of the pattern to increment
	 * @param counter the new value for the counter
	 */
	private void setPatternCounter(COUNTER_TYPES pattern, Integer counter) {
		statsCounter.put(pattern, counter);
	}
	
	
	/**
	 * This method increments the number of times a pattern is used or, more
	 * generally some statistics about the process
	 * 
	 * @param pattern the name of the pattern to increment
	 */
	private void incrementPatternCounter(COUNTER_TYPES pattern) {
		Integer counter = statsCounter.get(pattern);
		if (counter == null) {
			counter = new Integer(0);
		}
		counter++;
		statsCounter.put(pattern, counter);
	}
	
	
	/**
	 * This method decrements the number of times a pattern is used or, more
	 * generally some statistics about the process
	 * 
	 * @param pattern the name of the pattern to decrement
	 */
	@SuppressWarnings("unused")
	private void decrementPatternCounter(COUNTER_TYPES pattern) {
		Integer counter = statsCounter.get(pattern);
		if (counter == null || counter == 0) {
			counter = new Integer(0);
		} else {
			counter--;
			statsCounter.put(pattern, counter);
		}
	}
	
	
	/* ********************************************************************** */
	/*                             Randomization                              */
	/* ********************************************************************** */
	/**
	 * This method generates boolean values with respect to the given
	 * probability
	 * 
	 * @param successPercent the percent probability of success
	 * @return true with probability successPercent/100, otherwise false
	 */
	private boolean randomFromPercent(int successPercent) {
		return (successPercent > generator.nextInt(101));
	}
	
	
	/**
	 * This method populates the current process with some random activities.
	 * This is the "simple version" of this method, equals probability to each
	 * parameters and AND and XOR maximum branches to 3.
	 * 
	 * @param deep the maximum network deep
	 */
	public void randomize(int deep) {
		// data sanitization
		deep = (deep > 0)? deep : 1;
		// pack parameters
		int[] parameters = new int[] {
				4,  // max and branches
				4,  // max xor branches
				20, // loop prob
				10, // single act prob
				30, // sequence act prob
				30, // and prob
				30, // xor prob
				25  // empty activity prob
		};
		randomize(parameters, deep);
	}
	
	
	/**
	 * This method populates the current process with some random activities
	 * 
	 * @param ANDBranches the maximum number of AND branches (must be > 1)
	 * @param XORBranches the maximum number of XOR branches (must be > 1)
	 * @param loopPercent the loop probability (must be >= 0 and <= 100)
	 * @param singleActivityProbability the probability of single activity (must 
	 * be >= 0 and <= 100)
	 * @param sequenceProbability the probability of sequence activity (must be 
	 * >= 0 and <= 100)
	 * @param ANDProbability the probability of AND split-join (must be >= 0 and 
	 * <= 100)
	 * @param XORProbability the probability of XOR split-join (must be >= 0 and 
	 * <= 100)
	 * @param emptyProbability the probability of an empty pattern (must be >= 0
	 * and <= 100)
	 * @param deep the maximum network deep
	 */
	public void randomize(int ANDBranches, int XORBranches, int loopPercent,
			int singleActivityProbability, int sequenceProbability,
			int ANDProbability, int XORProbability, int emptyProbability, int deep) {
		// data sanitization
		ANDBranches = (ANDBranches > 1)? ANDBranches : 2;
		XORBranches = (XORBranches > 1)? XORBranches : 2;
		loopPercent = (loopPercent >= 0 && loopPercent <= 100)? loopPercent : 20;
		singleActivityProbability = (singleActivityProbability >= 0 && singleActivityProbability <= 100)? singleActivityProbability : 50;
		sequenceProbability = (sequenceProbability >= 0 && sequenceProbability <= 100)? sequenceProbability : 50;
		ANDProbability = (ANDProbability >= 0 && ANDProbability <= 100)? ANDProbability : 50;
		XORProbability = (XORProbability >= 0 && XORProbability <= 100)? XORProbability : 50;
		emptyProbability = (emptyProbability >= 0 && emptyProbability <= 100)? emptyProbability : 30;
		deep = (deep > 0)? deep : 1;
		// pack parameters
		int[] parameters = new int[] {
				ANDBranches, 
				XORBranches, 
				loopPercent, 
				singleActivityProbability, 
				sequenceProbability,
				ANDProbability, 
				XORProbability,
				emptyProbability
		};
		randomize(parameters, deep);
	}
	
	
	/**
	 * This method populates the current process with some random activities
	 * 
	 * @param parameters an integer array with these ordered parameters:
	 * <ul>
	 *  	<li>the maximum number of AND branches</li>
	 *  	<li>the maximum number of XOR branches</li>
	 *  	<li>the loop probability</li>
	 *  	<li>the probability of single activity</li>
	 *  	<li>the probability of sequence activity</li>
	 *  	<li>the probability of AND split-join</li>
	 *  	<li>the probability of XOR split-join</li>
	 *  	<li>the probability of en empty pattern</li>
	 * </ul>
	 * @param deep the maximum network deep
	 */
	private void randomize(int[] parameters, int deep) {
		int totPercent = parameters[4] + parameters[5] + parameters[6];
		int nextAction = generator.nextInt(totPercent + 1);
		
		if (nextAction <= parameters[4]) {
			getPatternSequence(parameters, deep, null, 0);
		} else if (nextAction <= parameters[4] + parameters[5]) {
			getPatternAnd(parameters, deep, null, 1);
		} else {
			getPatternXor(parameters, deep, null, 1);
		}
	}


	
	/**
	 * This method generates a random activity pattern
	 *
	 * @param parameters an integer array with these ordered parameters:
	 * <ul>
	 *  	<li>the maximum number of AND branches</li>
	 *  	<li>the maximum number of XOR branches</li>
	 *  	<li>the loop probability</li>
	 *  	<li>the probability of single activity</li>
	 *  	<li>the probability of sequence activity</li>
	 *  	<li>the probability of AND split-join</li>
	 *  	<li>the probability of XOR split-join</li>
	 *  	<li>the probability of en empty pattern</li>
	 * </ul>
	 * @param deep the current deep
	 * @param to the activity to point the last internal activity
	 * @param maxNested
	 * @return the head activity
	 */
	private PlgActivity askInternalPattern(int[] parameters, int deep, PlgActivity to, int maxNested) {
		return askInternalPattern(parameters, deep, to, maxNested, true);
	}


	
	/**
	 * This method generates a random activity pattern
	 *
	 * @param parameters an integer array with these ordered parameters:
	 * <ul>
	 *  	<li>the maximum number of AND branches</li>
	 *  	<li>the maximum number of XOR branches</li>
	 *  	<li>the loop probability</li>
	 *  	<li>the probability of single activity</li>
	 *  	<li>the probability of sequence activity</li>
	 *  	<li>the probability of AND split-join</li>
	 *  	<li>the probability of XOR split-join</li>
	 *  	<li>the probability of en empty pattern</li>
	 * </ul>
	 * @param deep the current deep
	 * @param to the activity to point the last internal activity
	 * @param maxNested
	 * @param allowEmpty if the empty pattern is allowed
	 * @return the head activity
	 */
	private PlgActivity askInternalPattern(int[] parameters, int deep, PlgActivity to, int maxNested, boolean allowEmpty) {
		deep = deep - 1;
		if (maxNested > maxDepth) {
			maxDepth++;
		}
		if (deep == 0) {
			return getPatternAlone(parameters, deep, to);
		} else {
			if (allowEmpty) {
				int totPercent = parameters[3] + parameters[4] + parameters[5] + parameters[6] + parameters[7];
				int nextAction = generator.nextInt(totPercent + 1);
				
				if (nextAction <= parameters[3]) {
					return getPatternAlone(parameters, deep, to);
				} else if (nextAction <= parameters[3] + parameters[4]) {
					return getPatternSequence(parameters, deep, to, maxNested);
				} else if (nextAction <= parameters[3] + parameters[4] + parameters[5]) {
					return getPatternAnd(parameters, deep, to, maxNested + 1);
				} else if (nextAction <= parameters[3] + parameters[4] + parameters[5] + parameters[6]) {
					return getPatternXor(parameters, deep, to, maxNested + 1);
				} else {
					return getEmptyPatter(parameters, deep, to, maxNested);
				}
			} else {
				int totPercent = parameters[3] + parameters[4] + parameters[5] + parameters[6];
				int nextAction = generator.nextInt(totPercent + 1);
				
				if (nextAction <= parameters[3]) {
					return getPatternAlone(parameters, deep, to);
				} else if (nextAction <= parameters[3] + parameters[4]) {
					return getPatternSequence(parameters, deep, to, maxNested);
				} else if (nextAction <= parameters[3] + parameters[4] + parameters[5]) {
					return getPatternAnd(parameters, deep, to, maxNested + 1);
				} else {
					return getPatternXor(parameters, deep, to, maxNested + 1);
				}
			}
		}
	}


	
	/**
	 * This method generates a random activity, with this structure:
	 * <pre>
	 *   A
	 * </pre>
	 *
	 * @param parameters an integer array with these ordered parameters:
	 * <ul>
	 *  	<li>the maximum number of AND branches</li>
	 *  	<li>the maximum number of XOR branches</li>
	 *  	<li>the loop probability</li>
	 *  	<li>the probability of single activity</li>
	 *  	<li>the probability of sequence activity</li>
	 *  	<li>the probability of AND split-join</li>
	 *  	<li>the probability of XOR split-join</li>
	 *  	<li>the probability of en empty pattern</li>
	 * </ul>
	 * @param deep the current deep
	 * @param to the activity to point the last internal activity
	 * @return the head activity
	 */
	private PlgActivity getPatternAlone(int[] parameters, int deep, PlgActivity to) {
		PlgActivity a = askNewActivity();
		// loop stuff
		if (!a.equals(this.firstActivity)) {
			if (randomFromPercent(parameters[2])) {
				incrementPatternCounter(COUNTER_TYPES.LOOP);
				a.addLoop(a);
			}
		}
		if (to != null) {
			a.addNext(to);
		}
		
		// counter update
		incrementPatternCounter(COUNTER_TYPES.ALONE);
		
		return a;
	}


	
	/**
	 * This method generates random activities, with this structure:
	 * <pre>
	 *   o -> A -> ? -> B -> o
	 * </pre>
	 *
	 * @param parameters an integer array with these ordered parameters:
	 * <ul>
	 *  	<li>the maximum number of AND branches</li>
	 *  	<li>the maximum number of XOR branches</li>
	 *  	<li>the loop probability</li>
	 *  	<li>the probability of single activity</li>
	 *  	<li>the probability of sequence activity</li>
	 *  	<li>the probability of AND split-join</li>
	 *  	<li>the probability of XOR split-join</li>
	 *  	<li>the probability of en empty pattern</li>
	 * </ul>
	 * @param deep the current deep
	 * @param to the activity to point the last internal activity
	 * @return the head activity
	 */
	private PlgActivity getPatternSequence(int[] parameters, int deep, PlgActivity to, int maxNested) {
		PlgActivity a = askNewActivity();
		PlgActivity b = askNewActivity();
		a.addNext(askInternalPattern(parameters, deep, b, maxNested));
		
		// loop stuff
		if (!a.equals(this.firstActivity) && !b.equals(this.lastActivity)) {
			if (randomFromPercent(parameters[2])) {
				incrementPatternCounter(COUNTER_TYPES.LOOP);
				b.addLoop(a);
			}
		}
		if (to != null) {
			b.addNext(to);
		}
		
		// counter update
		incrementPatternCounter(COUNTER_TYPES.SEQUENCE);
		
		return a;
	}


	
	/**
	 * This method generates random activities, with this structure:
	 * <pre>
	 *      .-> o -> ? -> o -.
	 *  o-> A                  B -> o
	 *      `-> o -> ? -> o -'
	 * </pre>
	 *
	 * @param parameters an integer array with these ordered parameters:
	 * <ul>
	 *  	<li>the maximum number of AND branches</li>
	 *  	<li>the maximum number of XOR branches</li>
	 *  	<li>the loop probability</li>
	 *  	<li>the probability of single activity</li>
	 *  	<li>the probability of sequence activity</li>
	 *  	<li>the probability of AND split-join</li>
	 *  	<li>the probability of XOR split-join</li>
	 *  	<li>the probability of en empty pattern</li>
	 * </ul>
	 * @param deep the current deep
	 * @param to the activity to point the last internal activity
	 * @return the head activity
	 */
	private PlgActivity getPatternAnd(int[] parameters, int deep, PlgActivity to, int maxNested) {
		PlgActivity a = askNewActivity();
		PlgActivity b = askNewActivity();
		a.inAndUntil(b);
		int totFork = 2 + generator.nextInt(parameters[0] - 1);
		int totEmpty = 0;
		// at least one activity must be in one and branch 
		a.addNext(askInternalPattern(parameters, deep, b, maxNested, false));
		for (int i = 0; i < totFork - 1; i++) {
			PlgActivity inner = askInternalPattern(parameters, deep, b, maxNested);
			// we need to check if this is an empty pattern
			if (inner.equals(b)) {
				totEmpty++;
			} else {
				a.addNext(inner);
			}
		}
		// we can have an empty activity for each branch; in this case we have
		// to connect a with b
		if (totEmpty == totFork) {
			a.addNext(b);
		}
		// loop stuff
		if (!a.equals(this.firstActivity) && !b.equals(this.lastActivity)) {
			if (randomFromPercent(parameters[2])) {
				incrementPatternCounter(COUNTER_TYPES.LOOP);
				b.addLoop(a);
			}
		}
		if (to != null) {
			b.addNext(to);
		}
		
		// counter update
		if (totFork-totEmpty > 1) {
			// check current number of branches
			int currentAndBranches = totFork-totEmpty;
			int maxAndBranches = getPatternsCounter(COUNTER_TYPES.MAX_AND_BRANCHES);
			if (currentAndBranches > maxAndBranches) {
				setPatternCounter(COUNTER_TYPES.MAX_AND_BRANCHES, currentAndBranches);
			}
			// this is actually an AND pattern
			incrementPatternCounter(COUNTER_TYPES.AND);
		}
		
		return a;
	}


	
	/**
	 * This method generates random activities, with this structure:
	 * <pre>
	 *             .-> ? -.
	 *   o -> A -> o       -> o -> B -> o
	 *             `-> ? -'
	 * </pre>
	 *
	 * @param parameters an integer array with these ordered parameters:
	 * <ul>
	 *  	<li>the maximum number of AND branches</li>
	 *  	<li>the maximum number of XOR branches</li>
	 *  	<li>the loop probability</li>
	 *  	<li>the probability of single activity</li>
	 *  	<li>the probability of sequence activity</li>
	 *  	<li>the probability of AND split-join</li>
	 *  	<li>the probability of XOR split-join</li>
	 *  	<li>the probability of en empty pattern</li>
	 * </ul>
	 * @param deep the current deep
	 * @param to the activity to point the last internal activity
	 * @return the head activity
	 */
	private PlgActivity getPatternXor(int[] parameters, int deep, PlgActivity to, int maxNested) {
		PlgActivity a = askNewActivity();
		PlgActivity b = askNewActivity();
		a.inXorUntil(b);
		int totFork = 2 + generator.nextInt(parameters[1] - 1);
		int totEmpty = 0;
		// at least one activity must be in one and branch 
		a.addNext(askInternalPattern(parameters, deep, b, maxNested, false));
		for (int i = 0; i < totFork - 1; i++) {
			PlgActivity inner = askInternalPattern(parameters, deep, b, maxNested);
			// we need to check if this is an empty pattern
			if (inner.equals(b)) {
				totEmpty++;
			} else {
				a.addNext(inner);
			}
		}
		// we can have an empty activity for each branch; in this case we have
		// to connect a with b
		if (totEmpty == totFork) {
			a.addNext(b);
		}
		// loop stuff
		if (!a.equals(this.firstActivity) && !b.equals(this.lastActivity)) {
			if (randomFromPercent(parameters[2])) {
				incrementPatternCounter(COUNTER_TYPES.LOOP);
				b.addLoop(a);
			}
		}
		if (to != null) {
			b.addNext(to);
		}
		
		// counter update
		if (totFork-totEmpty > 1) {
			// check current number of branches
			int currentXorBranches = totFork-totEmpty;
			int maxXorBranches = getPatternsCounter(COUNTER_TYPES.MAX_XOR_BRANCHES);
			if (currentXorBranches > maxXorBranches) {
				setPatternCounter(COUNTER_TYPES.MAX_XOR_BRANCHES, currentXorBranches);
			}
			// this is actually a XOR pattern
			incrementPatternCounter(COUNTER_TYPES.XOR);
		}
		
		return a;
	}
	
	
	
	/**
	 * This method generates an empty activity pattern
	 *
	 * @param parameters an integer array with these ordered parameters:
	 * <ul>
	 *  	<li>the maximum number of AND branches</li>
	 *  	<li>the maximum number of XOR branches</li>
	 *  	<li>the loop probability</li>
	 *  	<li>the probability of single activity</li>
	 *  	<li>the probability of sequence activity</li>
	 *  	<li>the probability of AND split-join</li>
	 *  	<li>the probability of XOR split-join</li>
	 *  	<li>the probability of en empty pattern</li>
	 * </ul>
	 * @param deep the current deep
	 * @param to the activity to point the last internal activity
	 * @return the head activity
	 */
	private PlgActivity getEmptyPatter(int[] parameters, int deep, PlgActivity to, int maxNested) {
		// counter update
		incrementPatternCounter(COUNTER_TYPES.EMPTY);
		
		return to;
	}
	
	/* ********************************************************************** */
}
