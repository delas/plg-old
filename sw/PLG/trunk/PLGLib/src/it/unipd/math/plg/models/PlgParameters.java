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
	
	/**
	 * This enum describe the set of all possible patterns
	 */
	public static enum PATTERN {
		/** Single activity pattern */
		SINGLE,
		/** Sequence activities pattern */
		SEQUENCE,
		/** AND pattern */
		AND,
		/** XOR pattern */
		XOR,
		/** Empty: no activity pattern */
		EMPTY
	}
	
	
	/**
	 * This method generates boolean values with respect to the given
	 * probability
	 * 
	 * @param successPercent the percent probability of success
	 * @return true with probability successPercent/100, otherwise false
	 */
	public static boolean randomFromPercent(int successPercent) {
		return (successPercent > PlgProcess.generator.nextInt(101));
	}
	
	
	/**
	 * Class constructor. To build a parameters configuration, all parameters
	 * must be given
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
	 * Set the AND branches parameter
	 * 
	 * @param andBranches the maximum number of AND branches
	 */
	public void setAndBranches(int andBranches) {
		ANDBranches = (andBranches > 1)? andBranches : 2;;
	}
	
	
	/**
	 * Get the AND branches parameter
	 * 
	 * @return the maximum number of AND branches
	 */
	public int getAndBranches() {
		return ANDBranches;
	}
	
	
	/**
	 * Set the XOR branches parameter
	 * 
	 * @param xorBranches the maximum number of XOR branches
	 */
	public void setXorBranches(int xorBranches) {
		XORBranches = (xorBranches > 1)? xorBranches : 2;
	}
	
	
	/**
	 * Get the XOR branches parameter
	 * 
	 * @return the maximum number of XOR branches
	 */
	public int getXorBranches() {
		return XORBranches;
	}
	
	
	/**
	 * Set the loop percentual parameter
	 * 
	 * @param loopPercent
	 */
	public void setLoopPercent(int loopPercent) {
		this.loopPercent = (loopPercent >= 0 && loopPercent <= 100)? loopPercent : 20;
	}
	
	
	/**
	 * Get the current value of the loop percent parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getLoopPercent() {
		return loopPercent;
	}
	
	
	/**
	 * Set the single activity percentual parameter
	 * 
	 * @param singleActivityPercent
	 */
	public void setSingleActivityPercent(int singleActivityPercent) {
		this.singleActivityPercent = (singleActivityPercent >= 0 && singleActivityPercent <= 100)? singleActivityPercent : 50;
	}
	
	
	/**
	 * Get the current value of the single activity percent parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getSingleActivityPercent() {
		return singleActivityPercent;
	}
	
	
	/**
	 * Set the sequence percent parameter
	 * 
	 * @param sequencePercent
	 */
	public void setSequencePercent(int sequencePercent) {
		this.sequencePercent = (sequencePercent >= 0 && sequencePercent <= 100)? sequencePercent : 50;
	}
	
	
	/**
	 * Get the current value of the sequence percent parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getSequencePercent() {
		return sequencePercent;
	}
	
	
	/**
	 * Set the AND percent parameter
	 * 
	 * @param andPercent
	 */
	public void setANDPercent(int andPercent) {
		ANDPercent = (andPercent >= 0 && andPercent <= 100)? andPercent : 50;
	}
	
	
	/**
	 * Get the current value of the AND percent parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getANDPercent() {
		return ANDPercent;
	}
	
	
	/**
	 * Set the XOR percent parameter
	 * 
	 * @param xorPercent
	 */
	public void setXORPercent(int xorPercent) {
		XORPercent = (xorPercent >= 0 && xorPercent <= 100)? xorPercent : 50;
	}
	
	
	/**
	 * Get the current value of the XOR percent parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getXORPercent() {
		return XORPercent;
	}
	
	
	/**
	 * Set the empty pattern percent parameter
	 * 
	 * @param emptyPercent
	 */
	public void setEmptyPercent(int emptyPercent) {
		this.emptyPercent = (emptyPercent >= 0 && emptyPercent <= 100)? emptyPercent : 30;
	}
	
	
	/**
	 * Get the current value of the empty percent parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getEmptyPercent() {
		return emptyPercent;
	}
	
	
	/**
	 * Set the maximum deepanche  parameter
	 * 
	 * @param deep
	 */
	public void setDeep(int deep) {
		this.deep = (deep > 0)? deep : 1;
	}
	
	
	/**
	 * Get the current value of the maximum deep parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getDeep() {
		return deep;
	}
	
	
	/**
	 * This method returns a random pattern randomly selected between:
	 * <ul>
	 * 	<li>Sequence pattern</li>
	 * 	<li>AND pattern</li>
	 * 	<li>XOR pattern</li>
	 * </ul>
	 * 
	 * The selection is done according to the given probabilities
	 * 
	 * @return the random pattern
	 */
	public PATTERN getRandomActionInSeqAndXor() {
		PATTERN toReturn;
		int totPercent = sequencePercent + ANDPercent + XORPercent;
		int nextAction = PlgProcess.generator.nextInt(totPercent + 1);
		
		if (nextAction <= sequencePercent) {
			toReturn = PATTERN.SEQUENCE;
		} else if (nextAction <= sequencePercent + ANDPercent) {
			toReturn = PATTERN.AND;
		} else {
			toReturn = PATTERN.XOR;
		}
		return toReturn;
	}
	
	
	/**
	 * This method returns a random pattern randomly selected between:
	 * <ul>
	 * 	<li>Single activity</li>
	 * 	<li>Sequence pattern</li>
	 * 	<li>AND pattern</li>
	 * 	<li>XOR pattern</li>
	 * 	<li>Empty pattern</li>
	 * </ul>
	 * 
	 * The selection is done according to the given probabilities
	 * 
	 * @return the random pattern
	 */
	public PATTERN getRandomActionInAloneSeqAndXorEmpty() {
		PATTERN toReturn;
		int totPercent = singleActivityPercent + sequencePercent + ANDPercent + XORPercent + emptyPercent;
		int nextAction = PlgProcess.generator.nextInt(totPercent + 1);
		if (nextAction <= singleActivityPercent) {
			toReturn = PATTERN.SINGLE;
		} else if (nextAction <= singleActivityPercent + sequencePercent) {
			toReturn = PATTERN.SEQUENCE;
		} else if (nextAction <= singleActivityPercent + sequencePercent + ANDPercent) {
			toReturn = PATTERN.AND;
		} else if (nextAction <= singleActivityPercent + sequencePercent + ANDPercent + XORPercent) {
			toReturn = PATTERN.XOR;
		} else {
			toReturn = PATTERN.EMPTY;
		}
		return toReturn;
	}
	
	
	/**
	 * This method returns a random pattern randomly selected between:
	 * <ul>
	 * 	<li>Single activity</li>
	 * 	<li>Sequence pattern</li>
	 * 	<li>AND pattern</li>
	 * 	<li>XOR pattern</li>
	 * </ul>
	 * 
	 * The selection is done according to the given probabilities
	 * 
	 * @return the random pattern
	 */
	public PATTERN getRandomActionInAloneSeqAndXor() {
		PATTERN toReturn;
		int totPercent = singleActivityPercent + sequencePercent + ANDPercent + XORPercent;
		int nextAction = PlgProcess.generator.nextInt(totPercent + 1);
		if (nextAction <= singleActivityPercent) {
			toReturn = PATTERN.SINGLE;
		} else if (nextAction <= singleActivityPercent + sequencePercent) {
			toReturn = PATTERN.SEQUENCE;
		} else if (nextAction <= singleActivityPercent + sequencePercent + ANDPercent) {
			toReturn = PATTERN.AND;
		} else {
			toReturn = PATTERN.XOR;
		}
		return toReturn;
	}
}
