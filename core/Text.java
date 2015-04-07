package core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <p>
 * This class represents a text.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Text {

	/**
	 * <p>
	 * Text compression rate.
	 * </p>
	 **/
	protected double compressionRate;
	/**
	 * <p>
	 * Original text content.
	 * </p>
	 **/
	protected String original;
	/**
	 * <p>
	 * Chunked text.
	 * </p>
	 **/
	protected String chunked;
	/**
	 * <p>
	 * POS annotated text.
	 * </p>
	 **/
	protected String posTagged;
	/**
	 * <p>
	 * Named entity annotated text.
	 * </p>
	 **/
	protected String neAnnotation;
	/**
	 * <p>
	 * Text sentences.
	 * </p>
	 **/
	protected Collection<Sentence> sentences;
	/**
	 * <p>
	 * Words sentences.
	 * </p>
	 **/
	protected Collection<Word> words;
	/**
	 * <p>
	 * Named entities.
	 * </p>
	 **/
	protected Collection<Word> namedEntities;
	/**
	 * <p>
	 * Text score (based on its sentences score).
	 * </p>
	 **/
	protected double score;
	/**
	 * <p>
	 * Total number of sentences in the text.
	 * </p>
	 **/
	protected int totalSentences;
	/**
	 * <p>
	 * Total number of words in the text.
	 * </p>
	 **/
	protected int totalWords;
	/**
	 * <p>
	 * Total number of namedEntities in the text.
	 * </p>
	 **/
	protected int totalNamedEntities;
	/**
	 * <p>
	 * Maximum sentence length.
	 * </p>
	 **/
	protected int maximumSentenceLength;
	/**
	 * <p>
	 * Minimum sentence length.
	 * </p>
	 **/
	protected int minimumSentenceLength;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Text.
	 * </p>
	 */
	public Text() {
		this.sentences = new LinkedList<Sentence>();
		this.words = new LinkedList<Word>();
		this.maximumSentenceLength = -1;
		this.minimumSentenceLength = -1;
		this.totalSentences = -1;
		this.totalWords = 0;
		this.score = 0;
		this.totalNamedEntities = 0;
		this.chunked = null;
		this.posTagged = null;
		this.neAnnotation = null;
	}

	/**
	 * <p>
	 * Builds a new Text from another text.
	 * </p>
	 * 
	 * @param text
	 *            the text to be used to create the new text.
	 */
	public Text(Text text) {
		this.setCompressionRate(text.getCompressionRate());
		this.setOriginal(text.getOriginal());
		this.setChunked(text.getChunked());
		this.setPosTagged(text.getPosTagged());
		this.setNamedEntityAnnotation(text.getNamedEntityAnnotation());
		this.setSentences(text.getSentences());
		this.setWords(text.getWords());
		this.setScore(text.getScore());
		this.setTotalSentences(text.getTotalSentences());
		this.setTotalWords(text.getTotalWords());
		this.setMaximumSentenceLength(text.getMaximumSentenceLength());
		this.setMinimumSentenceLength(text.getMinimumSentenceLength());
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Text based on its string.
	 * </p>
	 * 
	 * @param original
	 *            the String containing the text.
	 */
	public Text(String original) {
		this();
		this.original = original;
	}

	public Text(Collection<Sentence> sentences) {

		setSentences(sentences);

		Collection<Word> words = new LinkedList<Word>();
		for (Sentence sentence : sentences)
			words.addAll(sentence.getWords());

		setWords(words);

	}

	/** Getters & Setters **/
	public double getCompressionRate() {
		return compressionRate;
	}

	public double getScore() {
		return score;
	}

	public String getOriginal() {
		return original;
	}

	public String getChunked() {
		return chunked;
	}

	public String getPosTagged() {
		return posTagged;
	}

	public String getNamedEntityAnnotation() {
		return neAnnotation;
	}

	public Collection<Sentence> getSentences() {
		return sentences;
	}

	public int getTotalSentences() {
		return totalSentences;
	}

	public int getTotalWords() {
		return totalWords;
	}

	public int getMaximumSentenceLength() {
		return maximumSentenceLength;
	}

	public int getMinimumSentenceLength() {
		return minimumSentenceLength;
	}

	public Collection<Word> getWords() {
		return words;
	}

	public Collection<Word> getNamedEntities() {
		return namedEntities;
	}

	public int getTotalNamedEntities() {
		return totalNamedEntities;
	}

	public void setCompressionRate(double compressionRate) {
		this.compressionRate = compressionRate;
	}

	public void setChunked(String chunked) {
		this.chunked = chunked;
	}

	public void setPosTagged(String posTagged) {
		this.posTagged = posTagged;
	}

	public void setNamedEntityAnnotation(String neAnnotation) {
		this.neAnnotation = neAnnotation;
	}

	public void setMaximumSentenceLength(int maximumSentenceLength) {
		this.maximumSentenceLength = maximumSentenceLength;
	}

	public void setMinimumSentenceLength(int minimumSentenceLength) {
		this.minimumSentenceLength = minimumSentenceLength;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public void setWords(Collection<Word> words) {
		this.words = words;
	}

	public void setNamedEntities(Collection<Word> namedEntities) {
		this.namedEntities = namedEntities;
		this.totalNamedEntities = this.namedEntities.size();
	}

	public void setScore(double score) {
		this.score = score;
	}

	public void setTotalSentences(int totalSentences) {
		this.totalSentences = totalSentences;
	}

	public void setTotalWords(int totalWords) {
		this.totalWords = totalWords;
	}

	public void setSentences(Collection<Sentence> sentences) {
		this.sentences = sentences;
		this.totalSentences = sentences.size();
	}

	public void setTotalNamedEntities(int totalNamedEntities) {
		this.totalNamedEntities = totalNamedEntities;
	}

	/**
	 * <p>
	 * Computes text properties.
	 * </p>
	 * <ul>
	 * <li>Total text sentences.</li>
	 * <li>Total text words.</li>
	 * <li>Maximum sentence length.</li>
	 * <li>Minimum sentence length.</li>
	 * </ul>
	 */
	public void computeTextProperties() {

		// Computes text properties
		for (Sentence sentence : sentences) {

			int currentTotalWords = sentence.getTotalWords();

			// Computes the maximum sentence length
			if (currentTotalWords > this.maximumSentenceLength)
				this.maximumSentenceLength = currentTotalWords;

			// Computes the minimum sentence length
			if (this.minimumSentenceLength < 0)
				this.minimumSentenceLength = currentTotalWords;
			else if (this.minimumSentenceLength > currentTotalWords)
				this.minimumSentenceLength = currentTotalWords;

			this.totalWords += currentTotalWords;
		}
	}

	/**
	 * <p>
	 * Checks if the given term is in the text.
	 * </p>
	 * 
	 * @param word
	 *            word to be searched in the collection.
	 * @return true if the word appears in the current document.
	 */
	public boolean containsTerm(Word word) {

		boolean exists = false;

		Iterator<Word> iw;

		if (word instanceof NamedEntity)
			iw = namedEntities.iterator();
		else
			iw = words.iterator();

		while (iw.hasNext() && !exists) {
			Word current = iw.next();
			exists = current.represents(word);
		}

		return exists;
	}

	public int numberContainsTerm(Word word) {

		int exists = 0;

		Iterator<Word> iw;

		if (word instanceof NamedEntity)
			iw = namedEntities.iterator();
		else
			iw = words.iterator();

		while (iw.hasNext()) {
			Word current = iw.next();
			if (current.represents(word))
				exists++;
		}

		return exists;
	}

	/**
	 * <p>
	 * Computes text score.
	 * </p>
	 */
	public void computeScore() {

		double score = 0;

		for (Sentence sentence : sentences)
			score += sentence.getScore();

		this.score = score / this.totalSentences;
	}

	/**
	 * <p>
	 * Based on the sentences, builds:
	 * </p>
	 * <ul>
	 * <li>The string text;</li>
	 * <li>The collection of words;</li>
	 * </ul>
	 */
	protected void buildTextFromSentences() {
		this.original = "";
		this.words = new LinkedList<Word>();

		for (Sentence sentence : this.sentences) {
			this.original += sentence.getSentence().trim() + "\n";
			this.words.addAll(sentence.getWords());
		}
	}
}
