package org.uclm.alarcos.rrc.dqmodel;


import org.apache.jena.rdf.model.*;

import java.io.InputStream;

/**
 * 
 * 
 * Makes easier the model construction. If models are loaded from file, this
 * class distinguish the extension. If URI and Endpoint are specified, returns
 * the model or an exception.
 * 
 * @author Raul Reguillo Carmona
 * 
 */
public class DQModel {
	private Model model;
	private String format;

	/**
	 * Answer a DQModel
	 * 
	 * @param userURI
	 *            adress of the file by HTTP
	 * */
	public DQModel(String userURI) {
		DataPicker dp = new DataPicker();
		DQModel aux;
		aux = dp.getModel(userURI);
		this.model = aux.getModel();
		this.format = aux.getFormat();
	}

	/**
	 * Creates a new DQModel using Inputstream and a file format.
	 * 
	 * @param in
	 *            InputStream with the file
	 * @param format
	 *            file format
	 */
	public DQModel(InputStream in, String format) {
		DataPicker dp = new DataPicker();
		DQModel aux;
		aux = dp.getModel(in, format);
		this.model = aux.getModel();
		this.format = aux.getFormat();
	}

	/**
	 * Creates a new DQModel using URI and Endpoint
	 * 
	 * @param endpoint
	 *            address of HTTP service
	 * @param URI
	 *            URI of the model
	 */
	public DQModel(String endpoint, String URI) {
		DataPicker dp = new DataPicker();
		DQModel aux;
		aux = dp.getModel(endpoint, URI);
		this.model = aux.getModel();
		this.format = aux.getFormat();
	}

	/**
	 * Creates a DQModel filtering the subject
	 * 
	 * @param endpoint
	 *            address of HTTP service
	 * @param URI
	 *            URI of the model
	 * 
	 * @param includeSubject
	 *            <code>false</code> avoid the URI as a subject in the model
	 * 
	 */
	public DQModel(String endpoint, String URI, boolean includeSubject) {
		DataPicker dp = new DataPicker();
		DQModel aux;
		aux = dp.getModel(endpoint, URI, includeSubject);
		this.model = aux.getModel();
		this.format = aux.getFormat();
	}

	/**
	 * 
	 * @return Jena Model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * 
	 * @param dqmodel
	 *            Jena Model
	 */
	public void setDqmodel(Model dqmodel) {
		this.model = dqmodel;
	}

	/**
	 * 
	 * @return Sting with the format of the model (if any)
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 
	 * @param format
	 *            set the format of the loaded model.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Creates a <code>DQModel</code> from Model and format
	 * 
	 * @param model
	 *            Jena Model
	 * @param format
	 *            format by default
	 */
	public DQModel(Model model, String format) {
		super();
		this.model = model;
		this.format = format;
	}

	/**
	 * Empty constructor
	 */
	public DQModel() {
	}

	@Override
	public String toString() {
		return getModel().toString();
	}

	/**
	 * Show the model
	 */
	public void showModel() {
		DataWriter dw = new DataWriter();
		dw.showModel(this);
	}

	/**
	 * Show the model usign the format specified
	 * 
	 * @param format
	 *            format of the model
	 */
	public void showModelWithFormat(String format) {
		DataWriter dw = new DataWriter();
		dw.showModelWithFormat(this, format);
	}

	/**
	 * Answer a model comparison using: <code>(A v B) - (A ^ B)</code>
	 * 
	 * @param m
	 *            Jena Model which is compared with this
	 * @return Model
	 */
	public Model compareModelWith(Model m) {
		DQModel dq = new DQModel(m, "N3");
		return compareModelWith(dq);
	}

	/**
	 * Answer a model comparison using: <code>(A v B) - (A ^ B)</code>
	 * 
	 * @param m
	 *            DQModel which is compared with this
	 * @return Model
	 */
	public Model compareModelWith(DQModel m) {
		Model intersection = ModelFactory.createDefaultModel();
		Model result = ModelFactory.createDefaultModel();

		StmtIterator modelA = this.getModel().listStatements();

		Statement sta = null;
		while (modelA.hasNext()) {
			sta = modelA.next();
			if (m.getModel()
					.listStatements(
							new SimpleSelector(null, sta.getPredicate(), sta
									.getObject())).hasNext())
				intersection.add(sta);
		}
		// (A v B) - (A ^ B)
		result = (this.getModel().union(m.getModel())).difference(intersection);

		return result;
	}

	/**
	 * Answer an affinity percentage
	 * 
	 * @param m
	 *            <code>DQModel</code> to compare with
	 * @return Double affinity percentage
	 */
	public double affinity(DQModel m) {
		Model intersection = ModelFactory.createDefaultModel();
		double total = 0;
		double intersect = 0;
		double result = -1.0;

		StmtIterator modelA = this.getModel().listStatements();

		Statement sta = null;
		while (modelA.hasNext()) {
			sta = modelA.next();
			if (m.getModel()
					.listStatements(
							new SimpleSelector(null, sta.getPredicate(), sta
									.getObject())).hasNext())
				intersection.add(sta);
		}
		total = (this.getModel().union(m.getModel())).size();
		intersect = intersection.size();

		try {
			result = (intersect * 100) / total;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
