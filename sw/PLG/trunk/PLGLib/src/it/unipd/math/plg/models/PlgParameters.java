package it.unipd.math.plg.models;

/**
 * This class describes the parameters of the generator.
 * 
 * With this class the user can control the generation of the processess.
 * 
 * @author Andrea Burattin
 */
public class PlgParameters {

	private int ANDBranches;
	private int XORBranches;
	private int loopPercent;
	private int singleActivityPercent;
	private int sequencePercent;
	private int ANDPercent;
	private int XORPercent;
	private int emptyPercent;
	private int deep;
	
	
	public static enum ACTIONS {
		SINGLE,
		SEQUENCE,
		AND,
		XOR,
		EMPTY
	}
	
	
	/**
	 * 
	 * @param ANDBranches the maximum number of AND branches (must be > 1)
	 * @param XORBranches the maximum number of XOR branches (must be > 1)
	 * @param loopPercent the loop probability (must be >= 0 and <= 100)
	 * @param singleActivityPercent the probability of single activity (must 
	 * be >= 0 and <= 100)
	 * @param sequencePercent he probability of sequence activity (must be 
	 * >= 0 and <= 100)
	 * @param ANDPercent the probability of AND split-join (must be >= 0 and 
	 * <= 100)
	 * @param XORPercent the probability of XOR split-join (must be >= 0 and 
	 * <= 100)
	 * @param emptyPercent the probability of an empty pattern (must be >= 0
	 * and <= 100)
	 * @param deep the maximum network deep
	 */
	public PlgParameters(int ANDBranches, int XORBranches, int loopPercent,
			int singleActivityPercent, int sequencePercent, int ANDPercent,
			int XORPercent, int emptyPercent, int deep) {
		setAndBranches(ANDBranches);
		setXorBranches(XORBranches);
		setLoopPercent(loopPercent);
		setSingleActivityPercent(singleActivityPercent);
		setSequencePercent(sequencePercent);
		setANDPercent(ANDPercent);
		setXORPercent(XORPercent);
		setEmptyPercent(emptyPercent);
		setDeep(deep);
	}
	
	
	/**
	 * @param andBranches
	 */
	public void setAndBranches(int andBranches) {
		ANDBranches = (andBranches > 1)? andBranches : 2;;
	}
	
	
	/**
	 * @return
	 */
	public int getAndBranches() {
		return ANDBranches;
	}
	
	
	/**
	 * @param xorBranches
	 */
	public void setXorBranches(int xorBranches) {
		XORBranches = (xorBranches > 1)? xorBranches : 2;
	}
	
	
	/**
	 * @return
	 */
	public int getXorBranches() {
		return XORBranches;
	}
	
	
	/**
	 * @param loopPercent
	 */
	public void setLoopPercent(int loopPercent) {
		this.loopPercent = (loopPercent >= 0 && loopPercent <= 100)? loopPercent : 20;
	}
	
	
	/**
	 * @return
	 */
	public int getLoopPercent() {
		return loopPercent;
	}
	
	
	/**
	 * @param singleActivityPercent
	 */
	public void setSingleActivityPercent(int singleActivityPercent) {
		this.singleActivityPercent = (singleActivityPercent >= 0 && singleActivityPercent <= 100)? singleActivityPercent : 50;
	}
	/**
	 * @return
	 */
	public int getSingleActivityPercent() {
		return singleActivityPercent;
	}
	
	
	/**
	 * @param sequencePercent
	 */
	public void setSequencePercent(int sequencePercent) {
		this.sequencePercent = (sequencePercent >= 0 && sequencePercent <= 100)? sequencePercent : 50;
	}
	
	
	/**
	 * @return
	 */
	public int getSequencePercent() {
		return sequencePercent;
	}
	
	
	/**
	 * @param andPercent
	 */
	public void setANDPercent(int andPercent) {
		ANDPercent = (andPercent >= 0 && andPercent <= 100)? andPercent : 50;
	}
	
	
	/**
	 * @return
	 */
	public int getANDPercent() {
		return ANDPercent;
	}
	
	
	/**
	 * @param xorPercent
	 */
	public void setXORPercent(int xorPercent) {
		XORPercent = (xorPercent >= 0 && xorPercent <= 100)? xorPercent : 50;
	}
	
	
	/**
	 * @return
	 */
	public int getXORPercent() {
		return XORPercent;
	}
	
	
	/**
	 * @param emptyPercent
	 */
	public void setEmptyPercent(int emptyPercent) {
		this.emptyPercent = (emptyPercent >= 0 && emptyPercent <= 100)? emptyPercent : 30;
	}
	
	
	/**
	 * @return
	 */
	public int getEmptyPercent() {
		return emptyPercent;
	}
	
	
	/**
	 * @param deep
	 */
	public void setDeep(int deep) {
		this.deep = (deep > 0)? deep : 1;
	}
	
	
	/**
	 * @return
	 */
	public int getDeep() {
		return deep;
	}
	
	
	/**
	 * @return
	 */
	public ACTIONS getRandomActionInSeqAndXor() {
		ACTIONS toReturn;
		int totPercent = sequencePercent + ANDPercent + XORPercent;
		int nextAction = PlgProcess.generator.nextInt(totPercent + 1);
		
		if (nextAction <= sequencePercent) {
			toReturn = ACTIONS.SEQUENCE;
		} else if (nextAction <= sequencePercent + ANDPercent) {
			toReturn = ACTIONS.AND;
		} else {
			toReturn = ACTIONS.XOR;
		}
		return toReturn;
	}
	
	
	public ACTIONS getRandomActionInAloneSeqAndXorEmpty() {
		ACTIONS toReturn;
		int totPercent = singleActivityPercent + sequencePercent + ANDPercent + XORPercent + emptyPercent;
		int nextAction = PlgProcess.generator.nextInt(totPercent + 1);
		if (nextAction <= singleActivityPercent) {
			toReturn = ACTIONS.SINGLE;
		} else if (nextAction <= singleActivityPercent + sequencePercent) {
			toReturn = ACTIONS.SEQUENCE;
		} else if (nextAction <= singleActivityPercent + sequencePercent + ANDPercent) {
			toReturn = ACTIONS.AND;
		} else if (nextAction <= singleActivityPercent + sequencePercent + ANDPercent + XORPercent) {
			toReturn = ACTIONS.XOR;
		} else {
			toReturn = ACTIONS.EMPTY;
		}
		return toReturn;
	}
	
	
	public ACTIONS getRandomActionInAloneSeqAndXor() {
		ACTIONS toReturn;
		int totPercent = singleActivityPercent + sequencePercent + ANDPercent + XORPercent;
		int nextAction = PlgProcess.generator.nextInt(totPercent + 1);
		if (nextAction <= singleActivityPercent) {
			toReturn = ACTIONS.SINGLE;
		} else if (nextAction <= singleActivityPercent + sequencePercent) {
			toReturn = ACTIONS.SEQUENCE;
		} else if (nextAction <= singleActivityPercent + sequencePercent + ANDPercent) {
			toReturn = ACTIONS.AND;
		} else {
			toReturn = ACTIONS.XOR;
		}
		return toReturn;
	}
}
