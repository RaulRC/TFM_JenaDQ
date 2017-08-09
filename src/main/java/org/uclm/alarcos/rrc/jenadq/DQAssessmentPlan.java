package org.uclm.alarcos.rrc.jenadq;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class creates and handles Assessment Plans. An AssessmentPlan is a set
 * of Assessments.
 * 
 * @author Ra�l Reguillo Carmona
 * 
 */
public class DQAssessmentPlan {

	private boolean isPublic;
	private LinkedList<DQAssessment> assessmentList;
	private Model finalModel;
	private ArrayList<MeasurementResult> mRes;

	/**
	 * Empty constructor
	 */
	public DQAssessmentPlan() {

	}

	@Deprecated
	public boolean isIsPublic() {
		return this.isPublic;
	}

	@Deprecated
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * Answer a list of assessment
	 * 
	 * @return lista de evaluaciones
	 */
	public LinkedList<DQAssessment> getAssessmentList() {
		return this.assessmentList;
	}

	/**
	 * Set the assessment list
	 * 
	 * @param dqalist
	 *            lista de evaluaciones
	 */
	public void setAssessmentList(LinkedList<DQAssessment> dqalist) {
		this.assessmentList = dqalist;
	}

	/**
	 * Answer a Model as result of the plan execution. un �nico modelo de
	 * resultado.
	 * 
	 * @return Model Jena Model
	 */
	public Model executePlan() {
		Model m = ModelFactory.createDefaultModel();
		mRes = new ArrayList<MeasurementResult>();

		for (DQAssessment dqassess : this.getAssessmentList()) {
			m = m.union(dqassess.executeAssessment());
			mRes.addAll(dqassess.getmRes());
		}

		this.setFinalModel(m);
		System.out.println(mRes.toString());
		return m;
	}

	/**
	 * Answer the final Model
	 * 
	 * @return finalModel el modelo de resultados final
	 */
	public Model getFinalModel() {
		return finalModel;
	}

	/**
	 * 
	 * @param finalModel
	 *            Model
	 */
	public void setFinalModel(Model finalModel) {
		this.finalModel = finalModel;
	}

	// API OPERATIONS

	/**
	 * Add an assessment to this plan
	 * 
	 * @param assessment
	 *            DQAssessment
	 */
	public void addDQAssessment(DQAssessment assessment) {
		this.getAssessmentList().add(assessment);
	}

	/**
	 * Answer the MeasurementResult ArrayList.
	 * 
	 * @return <code>ArrayList<MeasurementResult></code>
	 */
	public ArrayList<MeasurementResult> getmRes() {
		return mRes;
	}

	/**
	 * 
	 * @param mRes
	 *            <code>ArrayList<MeasurementResult></code>
	 */
	public void setmRes(ArrayList<MeasurementResult> mRes) {
		this.mRes = mRes;
	}

}