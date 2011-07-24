package it.unipd.math.plg.models;

import java.util.HashMap;
import java.util.HashSet;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;

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
	
	private static int globalCounter = 0;
	
	private int id;
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
		this.id = globalCounter++;
		
		head.setFrame(this);
		tail.setFrame(this);
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
	 * Set the head of the pattern
	 * 
	 * @param head the new head
	 */
	public void setHead(PlgActivity head) {
		this.head = head;
	}
	
	
	/**
	 * Get the tail of the pattern
	 * 
	 * @return the tail of the pattern
	 */
	public PlgActivity getTail() {
		return tail;
	}
	
	
	/**
	 * Set the tail of the pattern
	 * 
	 * @param tail the new tail
	 */
	public void setTail(PlgActivity tail) {
		this.tail = tail;
	}
	
	
	public String getID() {
		return new Integer(id).toString();
	}
}















