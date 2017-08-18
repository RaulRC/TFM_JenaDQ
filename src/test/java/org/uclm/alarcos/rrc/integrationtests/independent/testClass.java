package org.uclm.alarcos.rrc.integrationtests.independent;

import junit.framework.TestCase;
import org.junit.Test;
import org.uclm.alarcos.rrc.dqmodel.DQModel;
import org.uclm.alarcos.rrc.jenadq.DQDimension;
import org.uclm.alarcos.rrc.jenadq._dimCompleteness;

import java.util.LinkedList;

public class testClass extends TestCase {

	@Test
	public void testModel() {

		try{
			String userURI = "http://datos.gijon.es/doc/medio-ambiente/areas-recreativas.ttl";
			DQModel m = new DQModel(userURI);

			LinkedList<DQDimension> l = new LinkedList<DQDimension>();
			_dimCompleteness dq1 = new _dimCompleteness(m);


			l.add(dq1);
			System.out.println(((_dimCompleteness) l.element()).m_interlinkingCompleteness().toString());

//		DQAssessment dq = new DQAssessment(l, m);
			// Get all the ontologies involved
			//System.out.println(m.getModel().getNsPrefixMap().toString());

/*
 * ENDPOINTS
 */
			String service = "http://dbpedia.org/sparql";
//	    String service = "http://querybuilder.dbpedia.org/";
//	    String service = "http://lod.openlinksw.com/sparql";
//		String service = "http://datos.gijon.es/page/12217-servicio-sparql";
//		String service = "es.dbpedia.org/sparql";
			String queryString2 =

					"CONSTRUCT {" +
							" <http://dbpedia.org/resource/The_Lord_of_the_Rings> ?p ?x} WHERE { " +
							" <http://dbpedia.org/resource/The_Lord_of_the_Rings> ?p ?x }";

			String queryString3 =
					"CONSTRUCT FROM <http://dbpedia.org/data/The_Lord_of_the_Rings.n3> WHERE { ?s ?p ?o } ";

			String queryString =

					"SELECT ?name ?birth ?death ?person WHERE {\n" +
							" ?person dbo:birthPlace :Spain .\n" +
							"?person dbo:birthDate ?birth .\n" +
							"?person foaf:name ?name .\n" +
							"?person dbo:deathDate ?death .\n" +

							"}\n LIMIT 10";

			DQModel dqq = new DQModel(service,  "http://dbpedia.org/resource/La_Solana");
			dqq.showModel();
			assert(true);
		}
		catch(Exception e){
			e.printStackTrace();
			assert(false);
		}
	}
}
