package controllers;

import java.util.ArrayList;
import java.util.Collection;

import options.EvalOptions;
import preferences.Preferences;

import evaluation.Metric;
import evaluation.Objective;
import evaluation.ROUGE;

/**
 * <p>
 * This class manages the evaluation process.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class EvalController extends Controller {

	/**
	 * <p>Evaluate system features.</p>
	 * 
	 * @return a collection of metrics which evaluate the summarizer.
	 */
	public Collection<Metric> evaluateFeatures() {
		
		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		if (this.options instanceof EvalOptions){
			
			EvalOptions options = (EvalOptions) this.options;

			if (options.getType() == Preferences.EvaluationType.BOTH){
				Objective objective = computeObjectiveMetrics();
				metrics.add(objective);
			
				ROUGE rouge = computeQualityMetrics();
				metrics.add(rouge);
			}
			else if (options.getType() == Preferences.EvaluationType.ROUGE){
				ROUGE rouge = computeQualityMetrics();
				metrics.add(rouge);
			}
			else{
				Objective objective = computeObjectiveMetrics();
				metrics.add(objective);
			}
		}
		
		return metrics;
	}

	/**
	 * <p>
	 * Computes summary quality metrics based on ROUGE.
	 * </p>
	 * 
	 * @return the ROUGE measures.
	 */
	private ROUGE computeQualityMetrics() {
		return null;
	}

	/**
	 * <p>
	 * Computes objective measures based on statistics computed over the
	 * summarization process.
	 * </p>
	 * 
	 * @return the Objective measures.
	 */
	private Objective computeObjectiveMetrics() {
		return null;
	}

	/**
	 * <p>
	 * Manages ideal and automatic summaries to be evaluated.
	 * </p>
	 */
	public void manageDocuments() {
	}

}
