package it.unipd.math.plg.models;

/**
 * This class represents a frame containing a process pattern
 * 
 * With this class is possible to describe the bounds of a process pattern
 * instance, since it contains information on the `head' and the `tail' of the
 * pattern.
 * 
 * @author Andrea Burattin
 */
public class PlgPatternFrame {
	
	private PlgActivity head;
	private PlgActivity tail;
	
	
	/**
	 * The default constructor of the pattern frame
	 * 
	 * @param head the head of the pattern
	 * @param tail the tail of the pattern
	 */
	public PlgPatternFrame(PlgActivity head, PlgActivity tail) {
		this.head = head;
		this.tail = tail;
	}
	
	
	/**
	 * Get the head of the pattern
	 * 
	 * @return the head of the pattern
	 */
	public PlgActivity getHead() {
		return head;
	}
	
	
	/**
	 * Get the tail of the pattern
	 * 
	 * @return the tail of the pattern
	 */
	public PlgActivity getTail() {
		return tail;
	}
}
