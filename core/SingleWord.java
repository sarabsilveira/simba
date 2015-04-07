package core;

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
public class SingleWord extends Word {

	/**
	 * <p>
	 * Relative position of the sentence in which the word appears.
	 * </p>
	 **/
	private double sentenceRelativePosition;
	/**
	 * <p>
	 * Number of tokens in this word.
	 * </p>
	 **/
	private int numberOfTokens;
	/**
	 * <p>
	 * Word token.
	 * </p>
	 **/
	protected String word;
	/**
	 * <p>
	 * Original word (without contractions).
	 * </p>
	 **/
	protected String original;
	/**
	 * <p>
	 * Lemma.
	 * </p>
	 **/
	protected String lemma;
	/**
	 * <p>
	 * Word annotation.
	 * </p>
	 **/
	protected String annotation;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new word based on its string.
	 * </p>
	 * 
	 * @param original
	 *            the original word string.
	 * @param word
	 *            the word string.
	 * @param lemma
	 *            the word lemmas.
	 * @param annotation
	 *            the word POS annotation.
	 */
	public SingleWord(String original, String word, String lemma,
			String annotation) {
		super(Preferences.WordType.SINGLE_WORD, 0);
		this.documentId = 0;
		this.sentenceId = 0;
		this.position = 0;
		this.tfidf = 0;
		this.score = 0;
		this.original = original;
		this.word = word;
		this.lemma = lemma;
		this.annotation = annotation;
		this.numberOfTokens = 1;
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new word based on another Word.
	 * </p>
	 * 
	 * @param word
	 *            the word to be copied.
	 */
	public SingleWord(SingleWord word) {
		this(word.getOriginal(), word.getWord(), word.getLemma(), word
				.getAnnotation());
		this.setFrequency(word.getFrequency());
		this.setDocumentId(word.getDocumentId());
		this.setSentenceId(word.getSentenceId());
		this.setPosition(word.getPosition());
		this.setTFIDF(word.getTFIDF());
		this.setNumberOfTokens(word.getNumberOfTokens());
		this.setScore(word.getScore());
		this.setExtraScore(word.getExtraScore());
		this.setOccurrences(word.getOccurrences());
		this.setNumberOfDocs(word.getNumberOfDocs());
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new word based on its string.
	 * </p>
	 * 
	 * @param documentId
	 *            the documents identification.
	 * @param sentenceId
	 *            the sentence identification.
	 * @param position
	 *            the word position in the sentence.
	 * @param numberOfTokens
	 *            the number of tokens that compose this word.
	 * @param original
	 *            the original word string.
	 * @param word
	 *            the word string.
	 * @param lemma
	 *            the word lemmas.
	 * @param annotation
	 *            the word POS annotation.
	 */
	public SingleWord(int documentId, int sentenceId, int position,
			int numberOfTokens, String original, String word, String lemma,
			String annotation) {
		this(original, word, lemma, annotation);
		this.setDocumentId(documentId);
		this.setSentenceId(sentenceId);
		this.setPosition(position);
		this.setNumberOfTokens(numberOfTokens);
	}

	/** Getters & Setters **/
	public String getWord() {
		return word;
	}

	public String getLemma() {
		return lemma;
	}

	public String getOriginal() {
		return original;
	}

	public String getAnnotation() {
		return annotation;
	}

	public int getNumberOfTokens() {
		return numberOfTokens;
	}

	public double getScore() {
		return score;
	}

	public double getSentenceRelativePosition() {
		return sentenceRelativePosition;
	}

	public void setSentenceRelativePosition(double sentenceRelativePosition) {
		this.sentenceRelativePosition = sentenceRelativePosition;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public void setNumberOfTokens(int numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}

	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * <p>
	 * Word string representation.
	 * </p>
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return documentId + "\t" + sentenceId + "\t" + position + "\t"
				+ numberOfTokens + "\t" + original + "\t" + word + "\t" + lemma
				+ "\t" + annotation + "\t" + score + "\t" + tfidf + "\t"
				+ extraScore;
	}

	@Override
	public String toPrint() {
		return Regex.cleanOtherThanWords(this.getOriginal());
	}

	/**
	 * <p>
	 * Checks if the two words are equal.
	 * </p>
	 * <p>
	 * Two words are equal if:
	 * </p>
	 * <p>
	 * Their word (if it has no lemma) or lemma is the same.
	 * </p>
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object word) {

		if (word instanceof NamedEntity)
			return false;

		return (
		// The word is the same and...
		this.original.equalsIgnoreCase(((SingleWord) word).getOriginal())

				// its position in the sentence, and in the document too.
				&& this.position == ((SingleWord) word).getPosition()
				&& this.documentId == ((SingleWord) word).getDocumentId()
				&& this.sentenceId == ((SingleWord) word).getSentenceId() && this.numberOfTokens == ((SingleWord) word)
					.getNumberOfTokens());
	}

	/**
	 * <p>
	 * Checks if this word is exactly the same as the given word.
	 * </p>
	 * 
	 * @param word
	 *            the given word.
	 * @return true if the words are the same; false otherwise.
	 */
	public boolean same(Word word) {
		return this.word.equalsIgnoreCase(((SingleWord) word).getWord())
				&& this.lemma.equals(((SingleWord) word).getLemma())
				&& this.annotation.equals(((SingleWord) word).getAnnotation());
	}

	/**
	 * <p>
	 * Checks if the current word can represent the given word, that is to say
	 * it is the same word or lemma and has the same frequency within the
	 * collection.
	 * </p>
	 * 
	 * @param word
	 *            the given word to be compared.
	 * @return true if the words are the same; false otherwise.
	 */
	public boolean represents(Word word) {

		if (word instanceof NamedEntity)
			return false;

		SingleWord newWord = (SingleWord) word;

		boolean represents = (this.lemma.equals("") || this.lemma == null ? (this.word
				.equalsIgnoreCase((newWord.getWord())) || this.original
				.equalsIgnoreCase(newWord.getOriginal()))
				: (this.containsLemma(newWord)
						|| this.word.equalsIgnoreCase(newWord.getWord()) || this.original
						.equalsIgnoreCase(newWord.getOriginal())));

		return represents;
	}

	/**
	 * <p>
	 * Checks if this is a word to be used as a keyword.
	 * </p>
	 * 
	 * @return true if the word must be used; false otherwise.
	 */
	public boolean toUse() {
		return Regex.isCommonName(annotation) || Regex.isProperName(annotation);
	}

	public boolean toTitle() {
		return Regex.isProperName(annotation);
	}

	/**
	 * <p>
	 * Checks if this a word to be skipped while counting.
	 * </p>
	 * 
	 * @return true if the word must be skipped; false otherwise.
	 */
	public boolean toSkip() {
		// Skips a word if it is a clitic or a contraction.
		return Regex.isClitic(this.annotation)
				|| Regex.isContraction(this.word)
				|| Regex.isPunctuation(this.word);
	}

	/**
	 * <p>
	 * Checks if this word is an open class word.
	 * </p>
	 * 
	 * @return true if the word is an open class word; false otherwise.
	 */
	public boolean isOpenClass() {
		return Regex.isOpenClassWord(this.annotation);
	}

	/**
	 * <p>
	 * Gets the representation of the current word.
	 * </p>
	 * <p>
	 * It can be the lemma or the word String.
	 * </p>
	 * 
	 * @return the current word representation String.
	 */
	public String representation() {
		return this.lemma.equals("") ? this.word : this.lemma;
	}

	/**
	 * <p>
	 * Checks if the current word contains the given words lemma.
	 * </p>
	 * 
	 * @param word
	 *            the word to be checked.
	 * @return true if the current word contains the word lemma; false
	 *         otherwise.
	 */
	public boolean containsLemma(Word word) {

		if (lemma.equalsIgnoreCase(((SingleWord) word).getLemma()))
			return true;
		if (lemma.equals("") || ((SingleWord) word).getLemma().equals(""))
			return false;

		String[] allLemmas = Regex.allLemmas(this);
		String[] currentLemma = Regex.allLemmas((SingleWord) word);

		boolean exists = false;

		for (int i = 0; i < allLemmas.length && !exists; i++) {
			if (!allLemmas[i].equals("")) {
				exists = false;
				for (int j = 0; j < currentLemma.length && !exists; j++)
					exists = !currentLemma[j].equals("")
							&& currentLemma[j].equalsIgnoreCase(allLemmas[i]);
			}

		}
		return exists;
	}

	/** Computes the word score. **/
	public void computeScore() {
		this.score = this.tfidf + this.extraScore;

	}

	/** Computes the tf-idf score. **/
	public void computeTFIDF() {
	}

	@Override
	public int compareTo(Word o) {

		if (o instanceof SingleWord) {
			if (this.score >= o.score)
				return 1;
			else
				return -1;

		} else
			return -1;
	}
}
