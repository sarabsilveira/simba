package evaluation;

/**
 * <p>This class represents a text.</p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class ROUGE extends Metric {
	
	/** <p>F-measure of the summary comparing to the ideal summary</p> **/
	private double fmeasure;
	/** <p>Precision of the summary comparing to the ideal summary</p> **/
	private double precision;
	/** <p>Recall of the summary comparing to the ideal summary</p> **/
	private double recall;

	/** Getters **/
	public double getFmeasure() {return fmeasure;}
	public double getPrecision() {return precision;}
	public double getRecall() {return recall;}
}
