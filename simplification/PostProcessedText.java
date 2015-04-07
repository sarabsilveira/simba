package simplification;

import java.util.Collection;

import summarization.Summary;
import core.Sentence;
import core.Text;

/**
 * <p>
 * This class represents a simplifed text.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class PostProcessedText extends Text {
	
	/**
	 * <p>Constructor.</p>
	 * <p>
	 * Builds a simplified text from a summary.
	 * </p>
	 * 
	 * @param summary, the summary to be transformed into a simplified text.
	 */
	public PostProcessedText(Summary summary){
		super(summary);
	}
	
	/**
	 * <p>Constructor.</p>
	 * <p>
	 * Builds a simplified text from a set of sentences.
	 * </p>
	 * 
	 * @param sentences, the sentences that form the text.
	 */
	public PostProcessedText(Collection<Sentence> sentences){
		super();
		this.setSentences(sentences);
		this.buildTextFromSentences();
		this.computeTextProperties();
		this.computeScore();
	}
}
