package org.uclm.alarcos.rrc.jenadq;

import DQModel.DQModel;
import JenaDQExceptions.IdentifierException;
import JenaDQExceptions.ModelGenerationException;
import JenaDQExceptions.RuleException;
import JenaDQExceptions.URIException;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.RDF;
import utilities.UriUtil;
import vocabulary.DQA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Extensi�n de <code>DQDimension</code> que eval�a la calidad de datos desde la
 * dimensi�n <code>Completeness</code>
 * 
 * @author Ra�l Reguillo Carmona
 * 
 */
public class _dimCompleteness extends DQDimension {

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
	public _dimCompleteness(DQModel targetmodel, List<Rule> useRules,
			List<Rule> contextualRuleList, int depth, String endpoint,
			String uri) {
		super(targetmodel);
		this.dimName = "Completeness";
		this.setRuleList(useRules);
		this.setDepth(depth);
		this.setEndpoint(endpoint);
		this.useRules = contextualRuleList;
		mRes = null;
	}

	/**
	 * 
	 * @param targetmodel
	 *            <code>DQModel</code>
	 * @param rules
	 *            List of rules
	 */
	public _dimCompleteness(DQModel targetmodel, List<Rule> rules) {
		super(targetmodel);
		this.dimName = "Completeness";
		this.setRuleList(rules);
		this.setDepth(0);
		mRes = null;
	}

	/**
	 * 
	 * @param targetmodel
	 *            <code>DQModel</code>
	 */
	public _dimCompleteness(DQModel targetmodel) {
		super(targetmodel);
		this.dimName = "Completeness";
	}

	/**
	 * Empty constructor
	 */
	public _dimCompleteness() {
		super();
		this.dimName = "Completeness";
	}

	/**
	 * 
	 * @return MeasurementResult InterlinkingCompleteness
	 */
	@Deprecated
	public JenaDQ.MeasurementResult m_interlinkingCompleteness() {
		MeasurementResult mRes = new MeasurementResult(
				"InterlinkingCompleteness", this.dimName);

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
		}
		if (total == 0)
			total = -1;
		result = this.calculateDQMeasure(countNoUri, total);
		mRes.setMResult(result);
		// System.out.println("RESULT: " + countNoUri+"/"+total +" = " +
		// result);
		return mRes;
	}

	/**
	 * schemaCompleteness method Chek if every class and property from Ont. are
	 * present in the model too
	 * 
	 * @return MeasurementResult schemaCompleteness
	 */
	@Deprecated
	public JenaDQ.MeasurementResult m_schemaCompleteness() {
		MeasurementResult mRes = new MeasurementResult("SchemaCompleteness",
				this.dimName);

		Iterator<String> prefixCollection = this.getTargetModel().getModel()
				.getNsPrefixMap().values().iterator();

		int total = 0;
		int notIn = 0;
		double result = 0;
		OntModel base = ModelFactory.createOntologyModel();
		List<OntProperty> ontList = new LinkedList<OntProperty>();

		while (prefixCollection.hasNext()) {
			try {
				base = ModelFactory.createOntologyModel();
				base.read(prefixCollection.next());
				ontList.addAll(base.listAllOntProperties().toList());
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		System.out.println(ontList.toString());
		total = ontList.size();
		StmtIterator modelPropList = this.getTargetModel().getModel()
				.listStatements();

		while (modelPropList.hasNext()) {
			if (!ontList.contains(modelPropList.next().getPredicate()))
				notIn++;
		}
		result = this.calculateDQMeasure(notIn, total);
		mRes.setMResult(result);
		return mRes;
	}

	/**
	 * ExecuteMeasurement Completeness
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

		long startTime = System.currentTimeMillis();
		ArrayList<ArrayList<RDFNode>> results = UriUtil
				.getResourcesInDepthQuery(getEndpoint(), this.getURI(),
						getDepth());
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("TIME ELAPSED WITH NODES: "
				+ (double) (estimatedTime / 1000.0));

		// for testing
		// for(ArrayList<RDFNode> ar:results)
		// System.out.println("Nodes: "+ar.size());

		// Results are gonna be here
		ArrayList<Double> resultsByLevel = new ArrayList<Double>();
		mRes = new ArrayList<MeasurementResult>();

		int total;
		int notExist;

		// LVL 0 - first graph
		// -----------------------------------------------------------
		DQModel dq = new DQModel(getEndpoint(), this.getURI());
		Reasoner reasoner = new GenericRuleReasoner(this.getUseRules());

		// DQ property to assess
		Resource o = ResourceFactory.createResource(DQA.NS + "NoCompleteness");
		Resource s = ResourceFactory.createResource(this.getURI());

		InfModel inf = ModelFactory.createInfModel(reasoner, dq.getModel());
		StmtIterator iter = null;

		if (getDepth() >= 0) {
			if (validate(inf).isValid()) {
				iter = inf.listStatements(s, null, o);
				if (iter.hasNext())
					resultsByLevel.add(0.0);
				else
					resultsByLevel.add(1.0);
			}
		}
		// LVL 0 - first graph
		// END--------------------------------------------------------
		total = 0;
		notExist = 0;
		// For each list and each node in list
		for (ArrayList<RDFNode> list : results) {
			total = list.size();
			notExist = 0;
			for (RDFNode node : list) {
				if (node.isURIResource()) {
					dq = new DQModel(getEndpoint(), node.toString());
					s = ResourceFactory.createResource(node.toString());
					inf = ModelFactory.createInfModel(reasoner, dq.getModel());

					if (validate(inf).isValid()) {
						iter = inf.listStatements(s, null, o);
						if (iter.hasNext()) {
							notExist += 1;
						}
					}
				}
			}
			resultsByLevel.add(calculateDQMeasure(notExist, total));
			notExist = 0;
		}

		// generate RDF result
		setAssessmentResults(resultsByLevel);
		// Reasoner -> apply the contextual rules here
		// generate final RDF with DQ assessment
		try {
			this.setFinalModel(this._getRDFModel());
		} catch (ModelGenerationException e) {
			e.printStackTrace();
		}

		generateMRES(resultsByLevel);

		return this.finalModel;
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
		for (int i = 0; i < results.size(); i++) {

			query = "PREFIX dqa: <http://www.dqassessment.org/ontologies/2014/9/DQA.owl#>\n"
					+ "SELECT ?x WHERE { "
					+ "?a dqa:InLevel ?y. \n"
					+ "?a dqa:ContextualMeasure ?x . "
					+ "FILTER ( ?y = "
					+ i
					+ ") } \n";

			q = QueryFactory.create(query);
			QueryExecution qExe = QueryExecutionFactory.create(q,
					getFinalModel());
			ResultSet resultsRes = qExe.execSelect();

			if (resultsRes.hasNext())
				mRes.add(new MeasurementResult("Lvl " + i, this.dimName,
						results.get(i), resultsRes.next().getResource("?x")
								.toString()));
			else
				mRes.add(new MeasurementResult("Lvl " + i, this.dimName,
						results.get(i)));
		}

	}

	public String toString() {
		return "Completeness";
	}

	/**
	 * Generates the final RDF Model with the rules
	 */
	public Model _getRDFModel() throws ModelGenerationException {

		Model m = ModelFactory.createDefaultModel();

		ArrayList<Model> mList = new ArrayList<Model>();

		Reasoner reasoner2 = new GenericRuleReasoner(this.getContextualRules());
		InfModel inf2 = null;

		Literal lResult = null;
		Literal lLevel = null;

		ArrayList<Model> result = new ArrayList<Model>();
		for (int i = 0; i < assessmentResults.size(); i++) {
			mList.add(ModelFactory.createDefaultModel());

			lResult = mList.get(i).createTypedLiteral(
					new Double(assessmentResults.get(i)));
			lLevel = mList.get(i).createTypedLiteral(new Integer(i));

			mList.get(i)
					.createResource(DQA.NS + this.assessmentIdentifier)
					.addProperty(RDF.type, DQA.COMPLETENESS)
					.addProperty(
							DQA.COMPLETENESS_RESULT,
							mList.get(i)
									.createResource()
									.addProperty(DQA.INURI, this.getURI())
									.addProperty(DQA.INLEVEL, lLevel)
									.addProperty(DQA.COMPLETENESS_MEASUREMENT,
											lResult));

			// inference here
			inf2 = ModelFactory.createInfModel(reasoner2, mList.get(i));
			// inf2.setNsPrefix("dqa", DQA.NS);
			validate(inf2);
			result.add(inf2);

		}
		// Time to compact all models generated in one
		for (Model mod : result)
			m = m.union(mod);
		return m;

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
}