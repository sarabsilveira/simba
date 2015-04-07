package core;

import java.util.Collection;

/**
 * <p>
 * This class represents a query.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Query {

	/**
	 * <p>
	 * Query string
	 * </p>
	 **/
	private String query;
	/**
	 * <p>
	 * Query words
	 * </p>
	 **/
	private Collection<SingleWord> words;

	/** Getters **/
	public String getQuery() {
		return query;
	}

	public Collection<SingleWord> getWords() {
		return words;
	}
}
