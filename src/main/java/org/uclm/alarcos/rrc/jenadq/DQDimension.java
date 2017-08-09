package org.uclm.alarcos.rrc.jenadq;



import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.rulesys.Rule;
import org.uclm.alarcos.rrc.dqmodel.DQModel;
import org.uclm.alarcos.rrc.jenadqexceptions.IdentifierException;
import org.uclm.alarcos.rrc.jenadqexceptions.ModelGenerationException;
import org.uclm.alarcos.rrc.jenadqexceptions.RuleException;
import org.uclm.alarcos.rrc.jenadqexceptions.URIException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * The main class of the Data Quality Model. This class can be extended
 * overwriting the abstract methods in order to add new Data Quality Dimensions.
 * 
 * @author Ra�l Reguillo Carmona
 * 
 */
public abstract class DQDimension {

	/**
	 * 
	 * @param targetmodel
	 *            <code>DQModel</code>
	 */
	public DQDimension(DQModel targetmodel) {
		super();
		this.targetModel = targetmodel;
	}

	/**
	 * Empty constructor
	 */
	public DQDimension() {
	}

	protected String dimName;
	protected DQModel targetModel;
	protected LinkedList<MeasurementResult> dimResults;
	protected List<Rule> useRules;
	protected List<Rule> contextualRules;
	protected int depth;

	protected String URI;
	protected String endpoint;
	protected String assessmentIdentifier;
	protected ArrayList<Double> assessmentResults;

	protected Model finalModel;

	// Results

	public ArrayList<Double> getAssessmentResults() {
		return assessmentResults;
	}

	public void setAssessmentResults(ArrayList<Double> assessmentResults) {
		this.assessmentResults = assessmentResults;
	}

	public String getAssessmentIdentifier() {
		return assessmentIdentifier;
	}

	public void setAssessmentIdentifier(String assessmentIdentifier) {
		this.assessmentIdentifier = assessmentIdentifier;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public List<Rule> getUseRules() {
		return useRules;
	}

	public void setUseRules(List<Rule> useRules) {
		this.useRules = useRules;
	}

	public List<Rule> getContextualRules() {
		return contextualRules;
	}

	public void setContextualRules(List<Rule> contextualRules) {
		this.contextualRules = contextualRules;
	}

	public Model getFinalModel() {
		return finalModel;
	}

	public void setFinalModel(Model finalModel) {
		this.finalModel = finalModel;
	}

	public ArrayList<MeasurementResult> getmRes() {
		return mRes;
	}

	public void setmRes(ArrayList<MeasurementResult> mRes) {
		this.mRes = mRes;
	}

	protected ArrayList<MeasurementResult> mRes;

	public String getDimName() {
		return this.dimName;
	}

	public void setDimName(String dimName) {
		this.dimName = dimName;
	}

	public DQModel getTargetModel() {
		return this.targetModel;
	}

	public void setTargetModel(DQModel targetModel) {
		this.targetModel = targetModel;
	}

	public LinkedList<MeasurementResult> getDimResults() {
		return this.dimResults;
	}

	public void setDimResults(LinkedList<MeasurementResult> dimResults) {
		this.dimResults = dimResults;
	}

	public LinkedList<MeasurementResult> executeMeasures() {
		return null;

	}

	public double calculateDQMeasure(double nNot, double nTot) {
		return (1 - (nNot / nTot));
	}

	/**
	 * Answer a DQModel usign query and endpoint
	 * 
	 * @param endpoint
	 *            address of the HTTP Service
	 * @param queryString
	 *            <code>String</code> with query
	 */
	@Deprecated
	public DQModel getResourceFromURI(String endpoint, String queryString) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint,
				query);
		DQModel dq = new DQModel();
		Model results = qexec.execConstruct();
		dq.setDqmodel((Model) results);
		// for ( ; results.hasNext() ; ) {
		// QuerySolution soln = results.nextSolution() ;
		// System.out.println(soln);
		// }
		return dq;
	}

	/**
	 * 
	 * Measurement of the DQDimension
	 * 
	 * @return Model
	 * @throws IdentifierException
	 * @throws RuleException
	 * @throws URIException
	 */
	public abstract Model _executeMeasurement() throws IdentifierException,
			RuleException, URIException;

	/**
	 * 
	 * Answer the final model as a result of the execution
	 * 
	 * @return Model
	 * @throws ModelGenerationException
	 */
	public abstract Model _getRDFModel() throws ModelGenerationException;

	/**
	 * 
	 * @return Model
	 */
	@Deprecated
	public Model _contextualFinalModel() {
		return null;
	}

	/**
	 * reset all results
	 */
	public void resetResults() {
		this.assessmentResults = new ArrayList<Double>();
	}

	@Deprecated
	public void generateMRES(ArrayList<Double> results) {
	}

	public String toString() {
		return "";
	}

	/**
	 * Return a validity report (is valid)
	 * 
	 * @param inf
	 *            Inference Model
	 * @return a Jena Validity Report
	 */
	protected ValidityReport validate(InfModel inf) {
		ValidityReport val = inf.validate();
		if (val.isValid()) {
			// System.out.println("OK");
		} else {
			System.out.println("Conflicts");
			for (Iterator<?> i = val.getReports(); i.hasNext();) {
				System.out.println(" - " + i.next());
			}
		}
		return val;
	}

}