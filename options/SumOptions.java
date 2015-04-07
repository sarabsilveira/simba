package options;

import java.util.Collection;

import preferences.Preferences;
import preferences.Utils;
import core.Word;

/**
 * <p>
 * This class represents the summarization options.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class SumOptions extends Option {

	/**
	 * <p>
	 * User-defined compression rate.
	 * </p>
	 **/
	private double compressionRate;
	/**
	 * <p>
	 * Input documents format.
	 * </p>
	 **/
	private Preferences.InputType inputType;
	/**
	 * <p>
	 * Output format.
	 * </p>
	 **/
	private Preferences.OutputType outputType;
	/**
	 * <p>
	 * Query or keywords submitted.
	 * </p>
	 **/
	private String query;
	/**
	 * <p>
	 * Keywords obtained from the query.
	 * </p>
	 **/
	private Collection<Word> keywords;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Creates a new object with summarization options.
	 * </p>
	 */
	public SumOptions() {
		super();
		this.compressionRate = -1;
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Creates a new object with summarization options.
	 * </p>
	 * 
	 * @param compressionRate
	 *            , the user defined summary compression rate
	 * @param outputType
	 *            , the user defined summary output type
	 * @param query
	 *            , the user submitted keywords
	 */
	public SumOptions(double compressionRate,
			Preferences.OutputType outputType, String query) {
		super();
		this.inputType = Preferences.InputType.SUMMARY;
		this.compressionRate = compressionRate;
		this.outputType = outputType;
		this.query = query;

		if (query != null)
			this.keywords = Utils.extractKeywords(query);
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Creates a new object with headline generation options.
	 * </p>
	 * 
	 * @param outputType
	 *            , the user defined summary output type
	 * @param query
	 *            , the user submitted keywords
	 */
	public SumOptions(Preferences.OutputType outputType, String query) {
		super();
		this.inputType = Preferences.InputType.HEADLINE;
		this.compressionRate = Preferences.DEFAULT_COMPRESSION_RATE;
		this.outputType = outputType;
		this.query = query;

		if (query != null)
			this.keywords = Utils.extractKeywords(query);
	}

	/** Getters **/
	public double getCompressionRate() {
		return compressionRate;
	}

	public Preferences.InputType getInputType() {
		return inputType;
	}

	public Preferences.OutputType getOutputType() {
		return outputType;
	}

	public String getQuery() {
		return query;
	}

	public Collection<Word> getKeywords() {
		return keywords;
	}

	/** Setters **/
	public void setCompressionRate(double compressionRate) {
		this.compressionRate = compressionRate;
	}

	public void setInputType(Preferences.InputType inputType) {
		this.inputType = inputType;
	}

	public void setOutputType(Preferences.OutputType outputType) {
		this.outputType = outputType;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setKeywords(Collection<Word> keywords) {
		this.keywords = keywords;
	}

	/**
	 * <p>
	 * Checks if the user has submitted keywords.
	 * </p>
	 * 
	 * @return true if the user the collection of keywords is not null; false
	 *         otherwise.
	 */
	public boolean hasKeywords() {
		return keywords != null;
	}
}
