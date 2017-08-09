package org.uclm.alarcos.rrc.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Ontology vocabulary
 * 
 * @author Ra�l Reguillo Carmona
 * 
 */
public class DQA {

	private static Model m_model = ModelFactory.createDefaultModel();
	// TODO DQA.owl
	public static final String NS = "http://www.dqassessment.org/ontologies/2014/9/DQA.owl#";

	/**
	 * <p>
	 * The namespace of the vocabulary as a string
	 * </p>
	 * 
	 * @see #NS
	 */
	public static String getURI() {
		return NS;
	}

	/**
	 * <p>
	 * The namespace of the vocabulary as a resource
	 * </p>
	 */
	public static final Resource NAMESPACE = m_model.createResource(NS);

	public static final Property CONTEXTUAL_RESULT = m_model.createProperty(NS
			+ "ContextualMeasure");
	public static final Property INURI = m_model.createProperty(NS + "InURI");
	public static final Property INLEVEL = m_model.createProperty(NS
			+ "InLevel");
	public static final Property INTIME = m_model.createProperty(NS
			+ "AssessmentDate");

	public static final Property COMPLETENESS = m_model.createProperty(NS
			+ "CompletenessAssessment");
	public static final Property COMPLETENESS_RESULT = m_model
			.createProperty(NS + "CompletenessResult");
	public static final Property COMPLETENESS_MEASUREMENT = m_model
			.createProperty(NS + "CompletenessMeasure");

	// public static final Property COMPLETENESS_CONTEXTUAL =
	// m_model.createProperty( NS + "ContextualCompleteness" );
	// public static final Property COMPLETENESS_LEVEL_CONTEXTUAL =
	// m_model.createProperty( NS + "ContextualCompletenessInLevel" );

	public static final Property ACCESSIBILITY = m_model.createProperty(NS
			+ "AccessibilityAssessment");
	public static final Property ACCESSIBILITY_RESULT = m_model
			.createProperty(NS + "AccessibilityResult");
	public static final Property ACCESSIBILITY_MEASUREMENT = m_model
			.createProperty(NS + "AccessibilityMeasure");

}
