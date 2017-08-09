package org.uclm.alarcos.rrc.dqmodel;

/**
 * Auxiliar class used to show DQModels in various ways
 * 
 * @author Ra�l Reguillo Carmona
 * @link JenaDQ.src.DQModel.DQModel
 * 
 */
public class DataWriter {

	/**
	 * Empty constructor
	 */
	public DataWriter() {

	}

	/**
	 * Show model with it's format
	 * 
	 * @param dqmodel
	 *            <code>DQModel</code>
	 */
	public void showModel(DQModel dqmodel) {
		dqmodel.getModel().write(System.out, dqmodel.getFormat());
	}

	/**
	 * Shows a DQModel usign the given format
	 * 
	 * @param dqmodel
	 *            <code>DQModel</code>
	 * @param format
	 *            expected output format
	 */
	public void showModelWithFormat(DQModel dqmodel, String format) {
		dqmodel.getModel().write(System.out, format);
	}
}