package org.uclm.alarcos.rrc.integrationtests.independent;

import junit.framework.TestCase;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Test;
import org.uclm.alarcos.rrc.utilities.UriUtil;

import java.util.ArrayList;

public class testNavigation extends TestCase {

	@Test
	public void testNav() {
		try{
			//		String endpoint = "http://lod.openlinksw.com/sparql";
			String endpoint = "http://dbpedia.org/sparql";
			String uri = "http://dbpedia.org/resource/La_Solana";
			int depth=2;

			ArrayList<ArrayList<RDFNode>> levels = new ArrayList<ArrayList<RDFNode>>();

			levels = UriUtil.getResourcesInDepth(endpoint, uri, depth);

			for(int i=0; i<depth; i++)
				System.out.print(i+":" +levels.get(i).size() + "\t" );
			assert(true);
		}
		catch(Exception e){
			assert(false);
		}
 	}
}
