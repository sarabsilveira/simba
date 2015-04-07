package core;

import java.util.Collection;

import preferences.Preferences;
import preferences.Preferences.WordType;

public abstract class Word implements Comparable<Word> {

	/**
	 * <p>
	 * Word identification.
	 * </p>
	 **/
	protected int documentId;
	protected int position;
	protected int sentenceId;
	/**
	 * <p>
	 * Word type: SingleWord or NamedEntity.
	 * </p>
	 **/
	protected Preferences.WordType type;
	/**
	 * <p>
	 * Word score (considering the document in which the word occurs).
	 * </p>
	 **/
	protected double score;
	/**
	 * <p>
	 * Word frequency in the text.
	 * </p>
	 **/
	protected double frequency;
	/**
	 * <p>
	 * Word score (considering all the documents in the collection).
	 * </p>
	 **/
	protected double tfidf;
	/**
	 * <p>
	 * Word occurrences in the text.
	 * </p>
	 **/
	protected int occurrences;
	/**
	 * <p>
	 * Number of documents in which the word occurs.
	 * </p>
	 **/
	protected int numberOfDocs;
	/**
	 * <p>
	 * Extra score (used to give bonus to the word).
	 * </p>
	 **/
	protected double extraScore;

	public Word(Preferences.WordType type, double score) {
		this.type = type;
		this.score = 0;
		this.frequency = -1;
		this.tfidf = 0;
		this.occurrences = 1;
		this.numberOfDocs = 0;
		this.extraScore = 0;
	}

	// Method to be implemented.

	public abstract String toString();

	public abstract String toPrint();

	public abstract String representation();

	public abstract boolean equals(Object word);

	public abstract boolean same(Word word);

	public abstract boolean toUse();

	public abstract boolean toSkip();

	public abstract boolean isOpenClass();

	public abstract boolean toTitle();

	public abstract boolean represents(Word word);

	public abstract double getSentenceRelativePosition();

	public abstract void computeScore();

	public abstract void computeTFIDF();

	/**
	 * <p>
	 * Checks if the current word is a keyword.
	 * </p>
	 * 
	 * @param keywords
	 *            the collection of keywords.
	 * @return true if the word is a keyword; false otherwise.
	 */
	public boolean isKeyword(Collection<Word> keywords) {

		boolean found = false;

		for (Word word : keywords) {

			if ((this instanceof SingleWord && word instanceof SingleWord))
				found = ((SingleWord) this).represents((SingleWord) word);

			else if (this instanceof NamedEntity && word instanceof NamedEntity)
				found = ((NamedEntity) this).represents((NamedEntity) word);

			else if (this instanceof SingleWord && word instanceof NamedEntity)
				found = ((NamedEntity) word).containsWord((SingleWord) this);

			if (found)
				break;
		}
		return found;
	}

	public void updateExtraScore(double extraScore) {
		this.extraScore += extraScore;
		this.computeScore();
	}

	// Getters and Setters
	public WordType getType() {
		return type;
	}

	public double getScore() {
		return score;
	}

	public double getExtraScore() {
		return extraScore;
	}

	public double getFrequency() {
		return frequency;
	}

	public double getTFIDF() {
		return tfidf;
	}

	public int getOccurrences() {
		return occurrences;
	}

	public int getNumberOfDocs() {
		return numberOfDocs;
	}

	public int getDocumentId() {
		return documentId;
	}

	public int getSentenceId() {
		return sentenceId;
	}

	public int getPosition() {
		return position;
	}

	public void setSentenceId(int sentenceId) {
		this.sentenceId = sentenceId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public void setExtraScore(double extraScore) {
		this.extraScore = extraScore;
	}

	public void setTFIDF(double tfidf) {
		this.tfidf = tfidf;
	}

	public void setFrequency(double frequency) {
		this.frequency = (frequency == 0 ? 1 : frequency);
	}

	public void setOccurrences(int occurrences) {
		this.occurrences = occurrences;
	}

	public void setNumberOfDocs(int numberOfDocs) {
		this.numberOfDocs = numberOfDocs;
	}
}
