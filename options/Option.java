package options;

import preferences.Preferences;

/**
 * <p>
 * This class defines the methods common to all sorts of options.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public abstract class Option {

	/**
	 * <p>
	 * Summary function.
	 * </p>
	 **/
	private Preferences.Function function;
	/**
	 * <p>
	 * Summary span.
	 * </p>
	 **/
	private Preferences.Span span;
	/**
	 * <p>
	 * Summary type of output content.
	 * </p>
	 **/
	private Preferences.Content content;
	/**
	 * <p>
	 * Documents location.
	 * </p>
	 **/
	private String documentsLocation;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new option object with default values.
	 * </p>
	 */
	public Option() {
		super();
		this.function = Preferences.Function.INFORMATIVE;
		this.span = Preferences.Span.MULTI;
		this.content = Preferences.Content.QUERY;
	}

	/** Getters **/
	public Preferences.Function getFunction() {
		return function;
	}

	public Preferences.Span getSpan() {
		return span;
	}

	public Preferences.Content getContent() {
		return content;
	}

	public String getDocumentsLocation() {
		return documentsLocation;
	}

	/** Setters **/
	public void setFunction(Preferences.Function function) {
		this.function = function;
	}

	public void setSpan(Preferences.Span span) {
		this.span = span;
	}

	public void setContent(Preferences.Content content) {
		this.content = content;
	}

	public void setDocumentsLocation(String documentsLocation) {
		this.documentsLocation = documentsLocation;
	}
}
