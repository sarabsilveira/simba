package summarization;

import java.util.Collection;

import simplification.PostProcessedText;
import core.Sentence;
import core.Text;

/**
 * <p>
 * This class represents a summary.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Summary extends Text {
	
	/**
	 * <p>Constructor.</p>
	 * <p>
	 * Builds a summary from a simplified text.
	 * </p>
	 * 
	 * @param simplifiedText, the simplified text to be transformed into a summary.
	 */
	public Summary(PostProcessedText simplifiedText){
		super(simplifiedText);
	}
	
	/**
	 * <p>Constructor.</p>
	 * <p>Builds a new text summary.</p>
	 */
	public Summary(){
		super();
	}
	
	/**
	 * <p>Constructor.</p>
	 * <p>Builds a new Summary based on a given collection of sentences.</p>
	 * 
	 * @param sentences the collection of sentences to create the summary.
	 */
	public Summary(Collection<Sentence> sentences){
		super();
		this.setSentences(sentences);
		this.buildTextFromSentences();
		this.computeTextProperties();
		this.computeScore();
	}
	
	public String toString(){
		String summary = "";
		
		for (Sentence s : this.sentences){
			
			if (s.isTitle())
				summary += "\n" + s.getSentence() + "\n";
			else
				summary += s.getSentence() + "\n";
		}
		
		return summary;
		
	}	
}
