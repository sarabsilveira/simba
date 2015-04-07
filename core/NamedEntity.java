package core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import preferences.Preferences;
import preferences.Regex;

/**
 * <p>
 * This class represents a word.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class NamedEntity extends Word {

	private Collection<SingleWord> entityWords;
	private String neType;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a blank named entity.
	 * </p>
	 * 
	 * @pos use setEntityWords to define the collection of words that define
	 *      this entity.
	 */
	public NamedEntity() {
		super(Preferences.WordType.NAMED_ENTITY, 0);
		this.entityWords = new LinkedList<SingleWord>();
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new named entity.
	 * </p>
	 */
	public NamedEntity(String type, Collection<SingleWord> entityWords) {
		this();
		this.setType(type);
		this.setEntityWords(entityWords);
	}

	/**
	 * <p>
	 * Constructor.
	 * <p>
	 * <p>
	 * Builds a new named entity based on another named entity.
	 * </p>
	 * 
	 * @param namedEntity
	 *            to be copied.
	 */
	public NamedEntity(NamedEntity namedEntity) {
		this();
		this.setType(namedEntity.getNEType());
		this.setEntityWords(namedEntity.getEntityWords());
		this.setScore(namedEntity.getScore());
	}

	// Getters & Setters
	public String getNEType() {
		return neType;
	}

	public double getSentenceRelativePosition() {
		return (new LinkedList<SingleWord>(entityWords)).getFirst()
				.getSentenceRelativePosition();
	}

	public Collection<SingleWord> getEntityWords() {
		return entityWords;
	}

	@Override
	public void setScore(double score) {
		this.score = score;
	}

	public void setEntityWords(Collection<SingleWord> entityWords) {
		this.entityWords = new LinkedList<SingleWord>(entityWords);
	}

	public void setType(String type) {
		this.neType = type;
	}

	@Override
	public String toString() {
		String words = "";

		for (SingleWord word : entityWords)
			words += word.getOriginal() + "\t";

		return neType + "\t" + score + "\t" + words;
	}

	/**
	 * <p>
	 * Named entity version to be printed.
	 * </p>
	 * 
	 * @return
	 */
	public String toPrint() {
		String inText = "";

		for (SingleWord word : entityWords)
			inText += word.getOriginal() + " ";

		return Regex.cleanOtherThanWords(inText.trim());

	}

	@Override
	public boolean represents(Word word) {

		boolean represents = false;

		if (word instanceof SingleWord)
			represents = entityWords.size() == 1
					&& word.represents(new LinkedList<SingleWord>(entityWords)
							.get(0));

		else if (word instanceof NamedEntity) {

			represents = this.representation().equals(word.representation());
		}

		return represents;
	}

	@Override
	public String representation() {

		String words = "";

		for (SingleWord word : entityWords)
			words += word.representation() + " ";

		return words.trim();
	}

	@Override
	public boolean equals(Object word) {

		if (word instanceof SingleWord)
			return false;

		LinkedList<SingleWord> neWords = (LinkedList<SingleWord>) ((NamedEntity) word)
				.getEntityWords();
		LinkedList<SingleWord> thisNeWords = (LinkedList<SingleWord>) this
				.getEntityWords();

		boolean equals = false;

		if (neWords.size() == thisNeWords.size()) {
			equals = true;

			for (int i = 0; i < thisNeWords.size() && equals; i++) {
				equals = equals && neWords.get(i).equals(thisNeWords.get(i));
			}
		}

		return equals;
	}

	@Override
	public boolean same(Word word) {

		LinkedList<SingleWord> neWords = (LinkedList<SingleWord>) ((NamedEntity) word)
				.getEntityWords();
		LinkedList<SingleWord> thisNeWords = (LinkedList<SingleWord>) this
				.getEntityWords();

		boolean same = false;

		if (neWords.size() == thisNeWords.size()) {
			for (int i = 0; i < thisNeWords.size() && !same; i++)
				same = same && neWords.get(i).same(thisNeWords.get(i));
		}

		return same;
	}

	@Override
	public boolean toSkip() {
		return !this.toUse();
	}

	@Override
	public boolean isOpenClass() {
		return true;
	}

	@Override
	public boolean toUse() {

		Word first = new LinkedList<Word>(entityWords).getFirst();
		Word last = new LinkedList<Word>(entityWords).getLast();

		boolean toUse = first.toUse() && last.toUse();

		return toUse;
	}

	public boolean toTitle() {
		return toUse();
	}

	public double similarityPercentage() {
		return 0;
	}

	/**
	 * <p>
	 * Checks if the given word is a part of the current named entity.
	 * </p>
	 * 
	 * @param word
	 *            the word to be checked.
	 * @return true if the given word is part of the current named entity; false
	 *         otherwise.
	 */
	public boolean containsWord(Word word) {
		boolean found = false;

		Iterator<SingleWord> iEntityWords = this.entityWords.iterator();

		while (iEntityWords.hasNext() && !found) {
			SingleWord nePart = iEntityWords.next();

			found = nePart.represents((SingleWord) word);
		}

		return found;
	}

	/**
	 * <p>
	 * Computes the number of common words between the current NamedEntity and
	 * the given word.
	 * </p>
	 * 
	 * @param word
	 *            the word to be compared.
	 * @return the number of common words between the two words.
	 */
	public double commonWords(Word word) {

		double commonWords = 0;

		if (word instanceof SingleWord) {

			for (SingleWord currentWord : this.entityWords) {

				if (word.same(currentWord))
					commonWords++;
			}

		} else if (word instanceof NamedEntity) {

			for (SingleWord firstWord : this.entityWords) {
				for (SingleWord secondWord : ((NamedEntity) word)
						.getEntityWords()) {

					if (firstWord.same(secondWord))
						commonWords++;
				}
			}
		}

		return commonWords;
	}

	/**
	 * <p>
	 * Checks if the given word is a part of the current named entity.
	 * </p>
	 * 
	 * @param word
	 *            the word to be checked.
	 * @return true if the given word is part of the current named entity; false
	 *         otherwise.
	 */
	public boolean startsNamedEntity(Word word) {
		boolean starts = false;

		if (this.entityWords.size() > 0) {
			SingleWord first = ((LinkedList<SingleWord>) this.entityWords)
					.getFirst();
			starts = first.represents((SingleWord) word);
		}

		return starts;
	}

	/**
	 * <p>
	 * Computes the score of the current NamedEntity based on the scores of the
	 * words composing it.
	 * </p>
	 */
	public void computeScore() {

		double wordsScore = 0;

		for (Word word : entityWords)
			wordsScore += word.getScore();

		this.score = (double) wordsScore / (double) entityWords.size();
	}

	/**
	 * <p>
	 * Gets the average tfidf of the terms in this named entity.
	 * </p>
	 */
	public void computeTFIDF() {

		for (SingleWord word : this.entityWords)
			this.tfidf += word.getTFIDF();

		this.tfidf = this.tfidf / (double) this.entityWords.size();

		System.out.println("NE:" + this.toPrint() + "\t" + this.tfidf);
	}

	/**
	 * <p>
	 * Checks if this entity is a proper name.
	 * </p>
	 * 
	 * @return true if the entity is a proper name.
	 */
	public boolean isProperName() {

		SingleWord first = ((LinkedList<SingleWord>) this.entityWords)
				.getFirst(), last = ((LinkedList<SingleWord>) this.entityWords)
				.getLast();

		return !first.equals(last) && Regex.isProperName(first.getAnnotation())
				&& Regex.isProperName(last.getAnnotation())
				&& first.getNumberOfTokens() != 3
				&& last.getNumberOfTokens() != 3;

	}

	@Override
	public void updateExtraScore(double extraScore) {

		for (SingleWord word : entityWords)
			word.updateExtraScore(extraScore);

	}

	@Override
	public int compareTo(Word o) {
		return -1;
	}
}
