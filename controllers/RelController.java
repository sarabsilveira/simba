package controllers;

import java.util.ArrayList;
import java.util.Collection;

import relation.DocumentCollection;
import relation.DocumentSimplified;
import relation.DocumentSummary;
import relation.Relation;

/**
 * <p>
 * This class manages the relation document/sentence process.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class RelController extends Controller {
	
	/**
	 * <p>
	 * Builds the relations between the collection of documents and the summary.
	 * </p>
	 * 
	 * @return a set of relations computed.
	 */
	public Collection<Relation> relateDocuments() {
		
		SumController sc = new SumController();
		summary = sc.buildSummary();
		
		
		DocumentSummary relationDocSum = document2summary();
		DocumentSimplified relationDocSim = document2simplified();
		DocumentCollection relationCollection = documentCollection();
		
		ArrayList<Relation> relations = new ArrayList<Relation>(); 
		relations.add(relationDocSum);
		relations.add(relationDocSim);
		relations.add(relationCollection);
		
		return relations;
	}

	/**
	 * <p>
	 * Computes the relation between the collection of documents, and the
	 * sentences chosen to be part of the summary.
	 * </p>
	 * 
	 * @return the relation object.
	 */
	private DocumentSummary document2summary() {
		return null;
	}

	/**
	 * <p>
	 * Computes the relation between the complete collection of sentences, and
	 * the sentences that were simplified.
	 * </p>
	 * 
	 * @return the relation object.
	 */
	private DocumentSimplified document2simplified() {
		return null;
	}

	/**
	 * <p>
	 * Computes the relation between the complete collection of sentences, and the
	 * sentences chosen to be part of the summary. 
	 * </p>
	 * 
	 * @return the relation object.
	 */
	private DocumentCollection documentCollection() {
		return null;
	}

}
