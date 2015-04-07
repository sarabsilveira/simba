package aux;

import java.util.Collection;
import java.util.LinkedList;

import core.Sentence;
import core.Word;

/**
 * <p>This class represents a word cluster.</p>
 *  
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class WordCluster extends Cluster{
	
	public WordCluster(Word word){
		super(word, new LinkedList<Word>());
	}
	
	public WordCluster(Word word, Collection<Word> words){
		super(word, words);
	}
	
	public void addValue(Word word){ ((Collection<Word>)this.values).add(word); }
	public void removeValue(Word word){ this.values.remove(word); }
	public boolean containsValue(Word word) { return this.values.contains(word); }
}
