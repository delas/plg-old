package it.unipd.math.plg.models;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;

/**
 * This class describes the observation of an activity.
 * 
 * With this framework we have a log described as a vector of observations.
 * 
 * @author Andrea Burattin
 * @version 0.1
 */
public class PlgObservation {

	private PlgActivity activity;
	private int startingTime;
	private String originator = "Originator";
	
	
	/**
	 * Default class constructor
	 * 
	 * @param activity the observed activity
	 * @param startingTime the time the observed activity started
	 */
	public PlgObservation(PlgActivity activity, int startingTime) {
		this.setActivity(activity);
		this.setStartingTime(startingTime);
	}

	
	/**
	 * Set the activity observed
	 * 
	 * @param activity the observed activity
	 */
	public void setActivity(PlgActivity activity) {
		this.activity = activity;
	}

	
	/**
	 * Get the activity observed
	 * 
	 * @return the activity of this observation
	 */
	public PlgActivity getActivity() {
		return activity;
	}


	/**
	 * Set the activity starting time
	 * 
	 * @param startingTime the activity starting time
	 */
	public void setStartingTime(int startingTime) {
		this.startingTime = startingTime;
	}


	/**
	 * Get the activity starting time
	 * 
	 * @return the unix epoch timestamp for the starting time
	 */
	public int getStartingTime() {
		return startingTime;
	}
	
	
	/**
	 * Get the possible starting time for the next activity. This value is
	 * calculated as the sum of the current starting time, plus the activity
	 * duration, plus 1.
	 * 
	 * @return the unix epoch timestamp for the possible starting time of the 
	 * next activity 
	 */
	public int getNextPossibleStartingTime() {
		return getStartingTime() + getActivity().duration() + 1;
	}
	
	
	@Override
	public String toString() {
		return activity.toString() + " ["+ startingTime + "-" + 
			(startingTime + activity.duration()) +"]";
	}


	/**
	 * Get the activity originator
	 * 
	 * @return the activity originator
	 */
	public String getOriginator() {
		return originator;
	}


	/**
	 * Set the activity originator
	 * 
	 * @param originator the activity originator
	 */
	public void setOriginator(String originator) {
		this.originator = originator;
	}
	
	
//	/**
//	 * This method constructs a set of audit trail entries that describes the
//	 * activity observation as in MXML. 
//	 * 
//	 * @see AuditTrailEntry
//	 * @param asInterval if this parameter is set to false, than the complete
//	 * event coincides (as timestamp) with the starting one
//	 * @return an array with two audit trail entries, one for the starting event
//	 * and one for the complete event
//	 */
//	public AuditTrailEntry[] getAuditTrailEntry(boolean asInterval) {
//		AuditTrailEntry[] ate = {new AuditTrailEntry(), new AuditTrailEntry()};
//		Timestamp tStart = new Timestamp(Long.valueOf(startingTime) * 1000);
//		Timestamp tComplete = new Timestamp(Long.valueOf(startingTime + activity.duration()) * 1000);
//		
//		// starting event
//		ate[0].setEventType(EventType.START);
//		ate[0].setTimestamp(tStart);
//		ate[0].setOriginator(originator);
//		ate[0].setWorkflowModelElement(activity.getName());
//		// end event
//		ate[1].setEventType(EventType.COMPLETE);
//		if (asInterval) {
//			ate[1].setTimestamp(tComplete);
//		} else {
//			ate[1].setTimestamp(tStart);
//		}
//		ate[1].setOriginator(originator);
//		ate[1].setWorkflowModelElement(activity.getName());
//		
//		return ate;
//	}
	
	
	/**
	 * This method constructs a set of two events that describes the
	 * activity observation as in XES.
	 * 
	 * @see XEvent
	 * @param asInterval if this parameter is set to false, than the complete
	 * event coincides (as timestamp) with the starting one
	 * @return an array with two XEvent entries, one for the starting event
	 * and one for the complete event
	 */
	public XEvent[] getEvents(boolean asInterval) {
		long tStart = Long.valueOf(startingTime) * 1000;
		long tComplete = Long.valueOf(startingTime + activity.duration() * 1000);
		if (!asInterval) {
			tComplete = tStart;
		}
		
		XEvent e1 = activity.getBasicXEvent();
		XEvent e2 = activity.getBasicXEvent();

		// start activity
		e1.getAttributes().put("org:resource",
				PlgProcess.xesFactory.createAttributeLiteral(
						"org:resource",
						originator,
						PlgProcess.xesExtensionManager.getByName("Organizational")));
		e1.getAttributes().put("time:timestamp",
				PlgProcess.xesFactory.createAttributeTimestamp(
						"time:timestamp",
						tStart,
						PlgProcess.xesExtensionManager.getByName("Time")));
		e1.getAttributes().put("lifecycle:transition",
				PlgProcess.xesFactory.createAttributeLiteral(
						"lifecycle:transition",
						"start",
						PlgProcess.xesExtensionManager.getByName("Lifecycle")));
		
		// end activity
		e2.getAttributes().put("org:resource",
				PlgProcess.xesFactory.createAttributeLiteral(
						"org:resource",
						originator,
						PlgProcess.xesExtensionManager.getByName("Organizational")));
		e2.getAttributes().put("time:timestamp",
				PlgProcess.xesFactory.createAttributeTimestamp(
						"time:timestamp",
						tComplete,
						PlgProcess.xesExtensionManager.getByName("Time")));
		e2.getAttributes().put("lifecycle:transition",
				PlgProcess.xesFactory.createAttributeLiteral(
						"lifecycle:transition",
						"complete",
						PlgProcess.xesExtensionManager.getByName("Lifecycle")));
		
		
		return new XEvent[] { e1, e2 };
		
	}
}
