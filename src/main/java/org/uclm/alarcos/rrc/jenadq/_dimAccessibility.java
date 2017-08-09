package org.uclm.alarcos.rrc.jenadq;

import DQModel.DQModel;
import JenaDQExceptions.IdentifierException;
import JenaDQExceptions.ModelGenerationException;
import JenaDQExceptions.RuleException;
import JenaDQExceptions.URIException;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.RDF;
import vocabulary.DQA;

import java.util.ArrayList;
import java.util.List;

/**
 * An extension of <code>DQDimension</code> DQDim <code>Accessibility</code>
 * 
 * @author Ra�l Reguillo Carmona
 * 
 */
public class _dimAccessibility extends DQDimension {

	public ArrayList<MeasurementResult> getmRes() {
		return mRes;
	}

	public void setmRes(ArrayList<MeasurementResult> mRes) {
		this.mRes = mRes;
	}

	/**
	 * 
	 * @param targetmodel
	 *            <code>DQModel</code>
	 * @param useRules
	 *            List of Use Rules
	 * @param contextualRuleList
	 *            List of Contextual Rules
	 * @param depth
	 *            Assessment depth
	 * @param endpoint
	 *            Address of HTTP service
	 * @param uri
	 *            target URI
	 */
	public _dimAccessibility(DQModel targetmodel, List<Rule> useRules,
			List<Rule> contextualRuleList, int depth, String endpoint,
			String uri) {
		super(targetmodel);
		this.dimName = "Accesibility";
		this.setRuleList(useRules);
		this.setDepth(depth);
		this.setEndpoint(endpoint);
		this.useRules = contextualRuleList;
		mRes = new ArrayList<MeasurementResult>();

		this.assessmentResults = new ArrayList<Double>();
	}

	/**
	 * 
	 * @param targetmodel
	 *            <code>DQModel</code>
	 * @param rules
	 *            List of rules
	 */
	public _dimAccessibility(DQModel targetmodel, List<Rule> rules) {
		super(targetmodel);
		this.dimName = "Accesibility";
		this.setRuleList(rules);
		this.setDepth(0);
		mRes = new ArrayList<MeasurementResult>();

		this.assessmentResults = new ArrayList<Double>();
	}

	/**
	 * 
	 * @param targetmodel
	 *            <code>DQModel</code>
	 */
	public _dimAccessibility(DQModel targetmodel) {
		super(targetmodel);
		this.dimName = "Accesibility";
		mRes = new ArrayList<MeasurementResult>();

		this.assessmentResults = new ArrayList<Double>();
	}

	/**
	 * Empty constructor
	 */
	public _dimAccessibility() {
		super();
		this.dimName = "Accesibility";
		mRes = new ArrayList<MeasurementResult>();

		this.assessmentResults = new ArrayList<Double>();
	}

	/**
	 * Execute Measurement Accessibility
	 * 
	 * @throws IdentifierException
	 * @throws RuleException
	 * @throws URIException
	 */
	public Model _executeMeasurement() throws IdentifierException,
			RuleException, URIException {

		if (this.getAssessmentIdentifier().equals(""))
			throw new IdentifierException();

		if (this.getContextualRules().size() == 0)
			throw new RuleException();

		if (this.getURI().equals(""))
			throw new URIException();

		RDFNode rdfn;
		Statement st;
		StmtIterator iter = this.getTargetModel().getModel().listStatements();

		int countNoUri = 0;
		int total = 0;
		double result = 0;

		while (iter.hasNext()) {
			st = iter.next();
			total++;
			rdfn = st.getObject();
			if (!rdfn.isURIResource())
				countNoUri++;
			else {
				// de-reference URI and check errors
				// Check if is a file or something
				try {
					@SuppressWarnings("unused")
					DQModel dqm = new DQModel(getEndpoint(), rdfn.toString());
				} catch (Exception e) {
					countNoUri++;
				}
			}
		}
		if (total == 0)
			total = -1;
		result = this.calculateDQMeasure(countNoUri, total);
		System.out.println(total + " ---> " + result);

		this.assessmentResults.add(result);
		// Generate model
		try {
			this.setFinalModel(this._getRDFModel());
		} catch (ModelGenerationException e) {
			e.printStackTrace();
		}
		// Return model

		Model m = this.getFinalModel();
		generateMRES(assessmentResults);
		return m;
	}

	/**
	 * Generates the final RDF Model with the rules
	 */
	public Model _getRDFModel() throws ModelGenerationException {

		Model m = ModelFactory.createDefaultModel();

		Reasoner reasoner = new GenericRuleReasoner(this.getContextualRules());
		InfModel inf = null;

		Literal result = null;

		result = m.createTypedLiteral(new Double(assessmentResults.get(0)));
		// prefixes
		// mList.get(i).setNsPrefix("dqa", DQA.NS);
		m.createResource(DQA.NS + this.assessmentIdentifier)
				.addProperty(RDF.type, DQA.ACCESSIBILITY)
				.addProperty(
						DQA.ACCESSIBILITY_RESULT,
						m.createResource()
								.addProperty(DQA.INURI, this.getURI())
								.addProperty(DQA.ACCESSIBILITY_MEASUREMENT,
										result));

		// inference here
		inf = ModelFactory.createInfModel(reasoner, m);
		// inf2.setNsPrefix("dqa", DQA.NS);
		validate(inf);
		return inf;
	}

	/**
	 * Generates the <code>MeasurementResult</code> from the result model
	 * generated
	 */
	public void generateMRES(ArrayList<Double> results) {
		// Generating DQ results
		// Setting up the Data Structure no RDF for easily pick the results
		String query = "";
		Query q = null;

		query = "PREFIX dqa: <http://www.dqassessment.org/ontologies/2014/9/DQA.owl#> \n"
				+ "SELECT ?x WHERE { "
				+ "?a dqa:ContextualMeasure ?x. \n"
				+ " } \n";

		q = QueryFactory.create(query);
		QueryExecution qExe = QueryExecutionFactory.create(q, getFinalModel());
		ResultSet resultsRes = qExe.execSelect();

		if (resultsRes.hasNext()) {

			mRes.add(new MeasurementResult("Interlinking", this.dimName,
					results.get(0), resultsRes.next().getResource("?x")
							.toString()));
		}
	}

	public void setRuleList(List<Rule> ruleList) {
		this.useRules = ruleList;
	}

	public ArrayList<Double> getAssessmentResults() {
		return assessmentResults;
	}

	public void setAssessmentResults(ArrayList<Double> assessmentResults) {
		this.assessmentResults = assessmentResults;
	}

	public String toString() {
		return "Accessibility";
	}

}