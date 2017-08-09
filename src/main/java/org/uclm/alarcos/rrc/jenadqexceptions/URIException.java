package org.uclm.alarcos.rrc.jenadqexceptions;

/**
 * Exception about URI
 * 
 * @author Ra�l Reguillo Carmona
 * 
 */
public class URIException extends AssessmentException {

	private static final long serialVersionUID = 1L;

	/**
	 * Not Valid Uri Exception
	 */
	public URIException() {
		super("Not valid URI");
	}
}
