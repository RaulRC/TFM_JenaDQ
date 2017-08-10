package org.uclm.alarcos.rrc.independent;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;

public class testONT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	      OntModel m = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
	        String NS = "http://example.com/test#";

	        OntClass a = m.createClass( NS + "A" );
	        OntClass b = m.createClass( NS + "B" );

	        a.addSubClass( b );

	        OntProperty c = m.createOntProperty( NS + "c" );
	        c.addRange( a );

	        m.write( System.out, "RDF/XML-ABBREV" );

	}

}
