package options;

import preferences.Preferences;
import preferences.Preferences.EvaluationType;

/**
 * <p>
 * This class represents the evaluation options.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class EvalOptions extends Option {

	/**
	 * <p>
	 * Evaluation type
	 * </p>
	 **/
	private Preferences.EvaluationType type;

	/**
	 * <p>
	 * Automatic summaries location.
	 * </p>
	 **/
	private String automaticLocation;

	/**
	 * <p>
	 * Manual summaries location.
	 * </p>
	 **/
	private String manualLocation;

	/**
	 * <p>
	 * Defines if automatic summaries must be built.
	 * </p>
	 **/
	private boolean buildAutomaticSummaries;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Creates a new object with evaluation options.
	 * </p>
	 * 
	 * @param type
	 *            the evaluation type.
	 * @param buildAutomaticSummaries
	 *            if automatic summaries must be built.
	 */
	public EvalOptions(EvaluationType type, boolean buildAutomaticSummaries) {
		super();
		this.type = type;
		this.buildAutomaticSummaries = buildAutomaticSummaries;
	}

	/** Getters **/
	public Preferences.EvaluationType getType() {
		return type;
	}

	public boolean buildAutomaticSummaries() {
		return buildAutomaticSummaries;
	}

	public String getAutomaticLocation() {
		return automaticLocation;
	}

	public String getManualLocation() {
		return manualLocation;
	}

	/** Setters **/
	public void setAutomaticLocation(String automaticLocation) {
		this.automaticLocation = automaticLocation;
	}

	public void setManualLocation(String manualLocation) {
		this.manualLocation = manualLocation;
	}

	public boolean isBuildAutomaticSummaries() {
		return buildAutomaticSummaries;
	}

	public void setBuildAutomaticSummaries(boolean buildAutomaticSummaries) {
		this.buildAutomaticSummaries = buildAutomaticSummaries;
	}

	public void setType(Preferences.EvaluationType type) {
		this.type = type;
	}

}
