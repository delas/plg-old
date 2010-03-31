package it.unipd.math.plg.models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

/**
 * Class that represents an activity (a transition in Petri Net).
 *
 * Each object of this type is an activity. Each activity has a set of
 * destination activity and these relations are typed (defining the type of
 * relation).
 * 
 * @author Andrea Burattin
 * @version 0.3
 */
public class PlgActivity {

	/**
	 * This enum describes the possible relations of an activity 
	 */
	public static enum RELATIONS {
		/** An undefined relation */
		UNDEF,
		/** A sequence relation */
		SEQUENCE,
		/** An AND split relation */
		AND_SPLIT,
		/** An XOR split relation */
		XOR_SPLIT
	};
	
	private String activityName;
	private Integer activityDuration;
	private RELATIONS relationType = RELATIONS.UNDEF;
	private HashSet<PlgActivity> relationsTo = new HashSet<PlgActivity>(5);
	private HashSet<PlgActivity> relationsFrom = new HashSet<PlgActivity>(5);
	private PlgActivity splitJoinOpposite;
	private PlgActivity inLoopTo;
	private PlgActivity inLoopFrom;
	private PlgProcess process;
	

	/**
	 * The default activity constructor
	 *
	 * @param process the process which the activity belongs to
	 * @param activityName the new activity name
	 * @param activityDuration the new activity duration
	 */
	public PlgActivity(PlgProcess process, String activityName, Integer activityDuration) {
		setProcess(process);
		setName(activityName);
		this.process.getActivityList().add(this);
		if (activityDuration.compareTo(-1) == 0) {
			randomActivityDuration();
		} else {
			duration(activityDuration);
		}
	}
	
	
	/**
	 * The default activity constructor.
	 *
	 * This method constructs a new activity with a random duration, as defined
	 * in the randomActivityDuration method.
	 *
	 * @see #randomActivityDuration
	 * @param process the process which the activity belongs to
	 * @param activityName the new activity name
	 */
	public PlgActivity(PlgProcess process, String activityName) {
		this(process, activityName, -1);
	}
	
	
	/**
	 * Sets the new activity name
	 * 
	 * @param activityName the new activity name
	 */
	public void setName(String activityName) {
		this.activityName = activityName;
	}
	
	
	/**
	 * Gets the activity name
	 * 
	 * @return the activity name
	 */
	public String getName() {
		return activityName;
	}
	
	
	/**
	 * Sets the activity duration
	 * 
	 * @param activityDuration the new activity duration
	 */
	public void duration(Integer activityDuration) {
		if (activityDuration.compareTo(0) > 0) {
			this.activityDuration = activityDuration;
		}
	}
	
	
	/**
	 * Gets the activity duration
	 * 
	 * @return the activity duration
	 */
	public Integer duration() {
		return activityDuration;
	}


	/**
	 * Sets the activity process
	 * 
	 * @param process the new activity process
	 */
	public void setProcess(PlgProcess process) {
		this.process = process;
	}


	/**
	 * Gets the activity process
	 * 
	 * @return the activity process
	 */
	public PlgProcess getProcess() {
		return process;
	}

	
	/**
	 * This method adds a new, general, relation to this activity. Actually this
	 * method shuld not be used by the user, see addNext instead.
	 * 
	 * @see #addNext(PlgActivity)
	 * @param relationType the new relation type
	 * @param destination the activity destination of the relation
	 * @return the destination activity
	 */
	private PlgActivity addRelation(RELATIONS relationType, PlgActivity destination) {
		this.relationType = relationType;
		// if this is a sequence relation, add this
		if (relationType == RELATIONS.SEQUENCE) {
			// add the backlink connection
			destination.relationsFrom.add(this);
			// add the only activity to successor
			relationsTo.clear();
			relationsTo.add(destination);
			return destination;
		}
		// if this is an and/xor split add 
		if (relationType == RELATIONS.AND_SPLIT ||
				relationType == RELATIONS.XOR_SPLIT) {
			splitJoinOpposite = destination;
			destination.splitJoinOpposite = this;
			return destination;
		}
		return null;
	}
	
	
	/**
	 * This method adds the correct successor activity.
	 * 
	 * If the current node has a simple direct succession, than the destination
	 * activity is the next one; if it's an AND-split, than the destination will
	 * be one of the AND branches and the same if it's an XOR-split.
	 * 
	 * @param destination the destination activity
	 * @return the destination activity
	 */
	public PlgActivity addNext(PlgActivity destination) {
		PlgActivity toReturn = null;
		if (relationType == RELATIONS.XOR_SPLIT) {
			relationsTo.add(destination);
			toReturn = destination;
			// add the backlink connection
			destination.relationsFrom.add(this);
		} else if (relationType == RELATIONS.AND_SPLIT) {
			relationsTo.add(destination);
			toReturn = destination;
			// add the backlink connection
			destination.relationsFrom.add(this);
		} else {
			toReturn = addRelation(RELATIONS.SEQUENCE, destination);
		}
		// process cache cleaning
		process.cleanModelCache();
		return toReturn;
	}
	
	
	/**
	 * This method inserts a new loop between two activities
	 * 
	 * @param destination the loop destination
	 * @return the invoking activity
	 */
	public PlgActivity addLoop(PlgActivity destination) {
		inLoopTo = destination;
		destination.inLoopFrom = this;
		return this;
	}
	
	
	/**
	 * Inserts in the process a new XOR split-join, between the current activity
	 * and the destination activity. Example:
	 * <pre>
	 *   Process p = new Process("test");
	 *   Activity a1 = p.askNewActivity();
	 *   Activity a2 = p.askNewActivity();
	 *   Activity a3 = p.askNewActivity();
	 *   Activity a4 = p.askNewActivity();
	 *   
	 *   a1.inXorUntil(a4);
	 *   a1.addNext(a2).addNext(a4);
	 *   a1.addNext(a3).addNext(a4);
	 * </pre>
	 * 
	 * @param destination the XOR split activity
	 * @return the destination activity
	 */
	public PlgActivity inXorUntil(PlgActivity destination) {
		return addRelation(RELATIONS.XOR_SPLIT, destination);
	}
	
	
	/**
	 * Inserts in the process a new AND split-join, between the current activity
	 * and the destination activity. Example:
	 * <pre>
	 *   Process p = new Process("test");
	 *   Activity a1 = p.askNewActivity();
	 *   Activity a2 = p.askNewActivity();
	 *   Activity a3 = p.askNewActivity();
	 *   Activity a4 = p.askNewActivity();
	 *   
	 *   a1.inAndUntil(a4);
	 *   a1.addNext(a2).addNext(a4);
	 *   a1.addNext(a3).addNext(a4);
	 * </pre>
	 * 
	 * @param destination the AND split activity
	 * @return the destination activity
	 */
	public PlgActivity inAndUntil(PlgActivity destination) {
		return addRelation(RELATIONS.AND_SPLIT, destination);
	}
	
	
	/**
	 * Checks if the current activity is a XOR-join
	 * 
	 * @return true if the activity is a XOR-join, false otherwise
	 */
	private boolean isXorJoin() {
		if (splitJoinOpposite != null && 
				splitJoinOpposite.relationType == RELATIONS.XOR_SPLIT) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Checks if the current activity is an AND-join
	 * 
	 * @return true if the activity is an AND-join, false otherwise
	 */
	private boolean isAndJoin() {
		if (splitJoinOpposite != null && 
				splitJoinOpposite.relationType == RELATIONS.AND_SPLIT) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * This method sets the duration of the current activity to a random value
	 * 
	 */
	private void randomActivityDuration() {
		activityDuration = PlgProcess.generator.nextInt(15) + 5;
//		return 10;
	}
	
	
	/**
	 * This method returns the possible time between the execution of two
	 * activities
	 * 
	 * @return a random number of seconds
	 */
	private int timeBetweenActivities() {
		return PlgProcess.generator.nextInt(11);
//		return 0;
	}
	
	
	@Override
	public String toString() {
		return activityName;
	}
	
	
	/**
	 * This method generates an instance of the the activity.
	 * 
	 * This method is used to generate a process instance, by calling this
	 * method on the first process' activity. This method, than, calls itself on
	 * all the other activities recursively.
	 * 
	 * @param startingTime the starting time of the first activity
	 * @return a vector of observations as a process execution
	 * @throws IOException
	 */
	public Vector<PlgObservation> generateInstance(int startingTime) throws IOException {
		return generateInstance(new Vector<PlgObservation>(), startingTime, new Stack<PlgActivity>());
	}
	
	
	/**
	 * This method generates an instance of the the activity.
	 * 
	 * @param v the vector of observations collected until now
	 * @param startingTime the starting time of the first activity
	 * @param untilActivities a stack of activity that indicates when to stop
	 * the process execution
	 * @return a vector of observations as a process execution
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private Vector<PlgObservation> generateInstance(
			Vector<PlgObservation> v, 
			int startingTime, 
			Stack<PlgActivity> untilActivities) 
			throws IOException {
		
		// be sure the vector is not null
		if (v == null) {
			v = new Vector<PlgObservation>();
		}
		
		// check the current stack of "until activities"
		if (untilActivities.size() == 0) {
			v.add(new PlgObservation(this, startingTime));
		} else {
			PlgActivity topUntil = untilActivities.pop();
			if (this.equals(topUntil)) {
				return v;
			} else {
				v.add(new PlgObservation(this, startingTime));
				untilActivities.push(topUntil);
			}
		}
		
		int nextNotBefore = startingTime + activityDuration + 1;
		
		// is this a loop?
		if (inLoopTo != null) {
			if (PlgProcess.generator.nextBoolean()) {
				v.addAll(inLoopTo.generateInstance(null, nextNotBefore, untilActivities));
				return v;
			}
		}
		
		if (relationType == RELATIONS.SEQUENCE) {
			
			// next activity yust in sequence relation
			nextNotBefore += timeBetweenActivities();
			v.addAll(relationsTo.iterator().next().generateInstance(null, nextNotBefore, untilActivities));
			
		} else if (relationType == RELATIONS.XOR_SPLIT) {
			
			// this is a xor split, we have to choose between some branches
			nextNotBefore += timeBetweenActivities();
			ArrayList<PlgActivity> xorBranches = new ArrayList<PlgActivity>(relationsTo);
			Collections.shuffle(xorBranches);
			v.addAll(xorBranches.get(0).generateInstance(null, nextNotBefore, untilActivities));
			
		} else if (relationType == RELATIONS.AND_SPLIT) {
			
			// this is an and split, all branches in parallel
			PlgActivity andJoin = splitJoinOpposite;
			untilActivities.push(andJoin);
			
			Vector<PlgObservation> nextCalls = null;
			HashMap<Integer, Vector<PlgObservation>> tracks = new HashMap<Integer, Vector<PlgObservation>>();
			Vector<Integer> indexes = new Vector<Integer>();
			int index = 0;
			for (Iterator<PlgActivity> i = relationsTo.iterator(); i.hasNext();) {
				nextNotBefore += timeBetweenActivities();
				PlgActivity to = i.next();
				nextCalls = to.generateInstance(null, nextNotBefore, (Stack<PlgActivity>)untilActivities.clone());
				tracks.put(index, nextCalls);
				for (int j = 0; j < nextCalls.size(); j++) {
					indexes.add(index);
				}
				index++;
			}
			Collections.shuffle(indexes);
			int lastStartingTime = -1;
			for (int i = 0; i < indexes.size(); i++) {
				PlgObservation o = tracks.get(indexes.get(i)).remove(0);
				v.add(o);
				int startTime = o.getNextPossibleStartingTime();
				if (lastStartingTime < startTime) {
					lastStartingTime = startTime;
				}
			}
			lastStartingTime += timeBetweenActivities();
			
			untilActivities.pop();
			v.addAll(andJoin.generateInstance(null, lastStartingTime, untilActivities));
			
		}
		
		return v;
	}
	
	
	/**
	 * This method returns an id for the current activity
	 * 
	 * @return the activity progressive id
	 */
	public String getActivityId() {
		String toRet = new Integer(process.getActivityList().indexOf(this)).toString();
		return toRet;
	}


	/**
	 * This method writes the Heuristics Net file into the file writer stream
	 * 
	 * @param fw 
	 * @param untilActivities
	 * @throws IOException
	 */
	public void getHeuristicsNetFile(FileWriter fw, 
			Stack<PlgActivity> untilActivities) throws IOException {
		
		// check the current stack of "until activities"
		if (untilActivities.size() > 0 && untilActivities.get(0).equals(this)) {
			return;
		}
		
		boolean isXorJoin = isXorJoin();
		boolean isAndJoin = isAndJoin();
		
		// activity id
		String toRet = getActivityId() + "@";
		
		// activity origin
		if (process.getFirstActivity().equals(this)) {
			if (inLoopFrom == null) {
				toRet += ".";
			}
		} else if (isXorJoin || isAndJoin) {
			int parsed = 0;
			for (Iterator<PlgActivity> i = relationsFrom.iterator(); i.hasNext();) {
				PlgActivity c = i.next();
				toRet += c.getActivityId();
				parsed++;
				if (parsed < relationsFrom.size()) {
					if (isXorJoin) {
						toRet += "|";
					} else {
						toRet += "&";
					}
				}
			}
		} else {
			if (relationsFrom.iterator().hasNext()) {
				toRet += relationsFrom.iterator().next().getActivityId();
			}
		}
		if (inLoopFrom != null) {
			toRet += "|" + inLoopFrom.getActivityId();
		}
		toRet += "@";
		
		// activity destination
		if (inLoopTo != null) {
			toRet += inLoopTo.getActivityId() + "|";
		}
		if (relationType == RELATIONS.SEQUENCE) {
			// a sequence
			if (relationsTo.iterator().hasNext()) {
				PlgActivity c = relationsTo.iterator().next();
				toRet += c.getActivityId();
				c.getHeuristicsNetFile(fw, untilActivities);
			}
			toRet += "\n";
			fw.write(toRet);
		} else if (relationType == RELATIONS.AND_SPLIT || relationType == RELATIONS.XOR_SPLIT) {
			// a split
			int parsed = 0;
			for (Iterator<PlgActivity> i = relationsTo.iterator(); i.hasNext();) {
				PlgActivity c = i.next();
				toRet += c.getActivityId();
				parsed++;
				if (parsed < relationsTo.size()) {
					if (relationType == RELATIONS.XOR_SPLIT) {
						toRet += "|";
					} else {
						toRet += "&";
					}
				}
			}
			toRet += "\n";
			fw.write(toRet);
			
			// recursive call into the branches
			untilActivities.push(splitJoinOpposite);
			for (Iterator<PlgActivity> i = relationsTo.iterator(); i.hasNext();) {
				i.next().getHeuristicsNetFile(fw, untilActivities);
			}
			untilActivities.pop();
			splitJoinOpposite.getHeuristicsNetFile(fw, untilActivities);
		} else {
			// this is the last activity
			if (inLoopTo == null) {
				toRet += ".";
			}
			toRet += "\n";
			fw.write(toRet);
		}
	}
}
