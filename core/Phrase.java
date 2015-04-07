package core;

import java.util.Collection;
import java.util.LinkedList;

import preferences.Preferences;
import preferences.Preferences.PhraseClassification;
import preferences.Regex;
import preferences.Utils;
import edu.stanford.nlp.trees.Tree;

/**
 * <p>
 * This class represents a clause.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Phrase implements Comparable {

	/**
	 * <p>
	 * Sentence phrase.
	 * </p>
	 **/
	private Sentence sentence;
	/**
	 * <p>
	 * Sentence tree.
	 * </p>
	 **/
	private Tree tree;
	/**
	 * <p>
	 * Node number.
	 * </p>
	 **/
	int nodeNumber;
	/**
	 * <p>
	 * Clause classification.
	 * </p>
	 **/
	private PhraseClassification classification;
	/**
	 * <p>
	 * Relevance.
	 * </p>
	 **/
	private double relevance;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new phrase based on its sentence and its parse tree.
	 * </p>
	 * 
	 * @param sentence
	 *            the clause sentence.
	 * @param tree
	 *            the phrase parse tree.
	 */
	public Phrase(Sentence sentence, Tree tree, int nodeNumber) {
		this.sentence = sentence;
		this.tree = tree;
		this.nodeNumber = nodeNumber;
		this.classification = classify();
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new phrase based on its sentence and its classification.
	 * </p>
	 * 
	 * @param sentence
	 *            the clause sentence.
	 * @param classification
	 *            the clause classification.
	 */
	public Phrase(Sentence sentence, PhraseClassification classification) {
		this.sentence = sentence;
		this.tree = null;
		this.classification = classification;
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new phrase based on its parse tree.
	 * </p>
	 * 
	 * @param tree
	 *            the phrase parse tree.
	 */
	public Phrase(Tree tree) {
		this.sentence = null;
		this.tree = tree;
	}

	/** Getters & Setters **/
	public Sentence getSentence() {
		return sentence;
	}

	public PhraseClassification getClassification() {
		return classification;
	}

	public Tree getPhraseTree() {
		return tree;
	}

	public int getNodeNumber() {
		return nodeNumber;
	}

	public double getRelevance() {
		return relevance;
	}

	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public void setClassification(PhraseClassification classification) {
		this.classification = classification;
	}

	public void setPhraseTree(Tree tree) {
		this.tree = tree;
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}

	/**
	 * @deprecated <p>
	 *             Classifies the current clause.
	 *             </p>
	 * 
	 * @return a string containing the clause classification.
	 */
	private PhraseClassification classify() {
		return null;
	}

	@Override
	public String toString() {
		return Utils.printTreeLeaves(tree);
	}

	/**
	 * <p>
	 * Computes the phrase relevance based on the global keywords.
	 * </p>
	 * 
	 * @param keywords
	 *            the global system keywords.
	 */
	public void computeRelevance(Collection<Word> keywords) {

		int occurrences = Utils.countWordsInSentence(keywords, this.sentence);
		relevance = (occurrences == 0 ? 0
				: (double) ((double) occurrences / (double) this.sentence
						.getWords().size()));

		sentence.setScore(this.sentence.computeScore());
	}

	/**
	 * <p>
	 * Checks if the current phrase is relevant.
	 * </p>
	 * 
	 * @return true if the phrase is relevant; false otherwise.
	 */
	public boolean isRelevant() {

		// If the phrase sentence is bigger than a minimum length, it is
		// relevant.
		// If the phrase sentence has a conjugated verb, it is relevant.
		// If the phrase sentence is only composed by proper names, it is
		// relevant.
		return this.sentence.getSentence().length() > Preferences.MINIMUM_PHRASE_LENGTH
				&& Regex.containsVerb(this.sentence);
	}

	@Override
	public int compareTo(Object arg0) {
		Phrase p = (Phrase) arg0;
		return (this.tree.equals(p.getPhraseTree()) ? 1 : -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		Phrase p = (Phrase) arg0;
		return this.tree.equals(p.getPhraseTree());
	}

	/**
	 * <p>
	 * Checks if two Phrases are the same.
	 * </p>
	 * 
	 * @param phrase
	 *            the Phrase to be compared
	 * @return true if the phrases are the same; false otherwise.
	 */
	public boolean same(Phrase phrase) {

		LinkedList<Word> currentWords = (LinkedList<Word>) this.sentence
				.getWords(), givenWords = (LinkedList<Word>) phrase
				.getSentence().getWords();

		if (currentWords.size() != givenWords.size())
			return false;

		boolean equals = true;

		for (int i = 0; i < currentWords.size() && equals; i++) {
			Word current = currentWords.get(i), given = givenWords.get(i);
			equals = current.represents(given) && equals;
		}

		return equals;
	}
}
