package org.uclm.alarcos.rrc.independent;

import org.apache.jena.rdf.model.RDFNode;
import org.uclm.alarcos.rrc.utilities.UriUtil;

import java.util.ArrayList;

public class testNavigation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//		String endpoint = "http://lod.openlinksw.com/sparql";
		String endpoint = "http://dbpedia.org/sparql";
		String uri = "http://dbpedia.org/resource/Metallica";
		int depth=2;

		ArrayList<ArrayList<RDFNode>> levels = new ArrayList<ArrayList<RDFNode>>();

		levels = UriUtil.getResourcesInDepth(endpoint, uri, depth);
		

		
		for(int i=0; i<depth; i++)
			System.out.print(i+":" +levels.get(i).size() + "\t" );
		
		
	}
}
