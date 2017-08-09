package org.uclm.alarcos.rrc.jenadqexceptions;

/**
 * Missing Identifier Exception
 * 
 * @author Ra�l Reguillo Carmona
 * 
 */
public class IdentifierException extends AssessmentException {

	private static final long serialVersionUID = 1L;

	/**
	 * Missing Assessment Identifier Exception
	 */
	public IdentifierException() {
		super("Missing Assessment Identifier");
	}

}
