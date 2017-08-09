package org.uclm.alarcos.rrc.jenadqexceptions;

/**
 * Generic Exception for Assessment
 * 
 * @author Ra�l Reguillo Carmona
 * 
 */
public class AssessmentException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Empty constructor
	 */
	public AssessmentException() {

	}

	/**
	 * Set the message
	 * 
	 * @param message
	 *            String with the message
	 */
	public AssessmentException(String message) {
		super(message);
	}
}
