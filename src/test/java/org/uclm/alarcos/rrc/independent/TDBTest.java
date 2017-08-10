package org.uclm.alarcos.rrc.independent;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.uclm.alarcos.rrc.dqmodel.DQModel;

import java.util.Iterator;

public class TDBTest {

	public static void main (String [] args){
		// Make a TDB-backed dataset
		//		  String directory = "D:\\MyDatabases\\Dataset3" ;
		String directory = "D:\\MyDatabases\\DatasetResult"; 
		Dataset dataset = TDBFactory.createDataset(directory) ;

		Iterator<String> list = dataset.listNames();

		while (list.hasNext()){
			System.out.println(list.next().toString());
		}
		
		// THIS WORKS!
		Model tdbmodel = dataset.getDefaultModel();
		Model m = dataset.getNamedModel("http://localhost:3030/db/Led-Zeppelin");

		m.add((new DQModel("http://dbpedia.org/data/Led_Zeppelin.n3")).getModel());
		
		dataset.begin(ReadWrite.WRITE);
		
		tdbmodel.add(m); 
//		
//// HAVE TO USE TRANSACTIONS
//		dataset.end() ;
		Model m2 = dataset.getNamedModel("Led-Zeppelin");
		m2.write(System.out);
	}
}
