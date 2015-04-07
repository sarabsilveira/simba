package core;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import preferences.Preferences;
import preferences.Regex;
import preferences.Utils;
import edu.stanford.nlp.ling.StringLabel;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;
import external.ManageExternalTools;

/**
 * <p>
 * This class represents a Sentence.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Sentence implements Comparable {

	/**
	 * <p>
	 * The sentence string.
	 * </p>
	 **/
	private String sentence;
	/**
	 * <p>
	 * The pos annotated sentence string.
	 * </p>
	 **/
	private String posTagged;
	/**
	 * <p>
	 * The named entity annotation sentence string.
	 * </p>
	 **/
	private String neAnnotation;
	/**
	 * <p>
	 * The sentence parse tree.
	 * </p>
	 **/
	private Tree parseTree;
	/**
	 * <p>
	 * Sentence length.
	 * </p>
	 **/
	private int length;
	/**
	 * <p>
	 * Sentence position in the text.
	 * </p>
	 **/
	private int absolutePosition;
	/**
	 * <p>
	 * Sum of the sentence words frequency.
	 * </p>
	 **/
	private double totalFrequency;
	/**
	 * <p>
	 * Words that compose the sentence.
	 * </p>
	 **/
	private Collection<Word> words;
	/**
	 * <p>
	 * Phrases in the current sentence.
	 * </p>
	 **/
	private Collection<Phrase> relativeClauses;
	/**
	 * <p>
	 * Parenthetical phrases in the current sentence.
	 * </p>
	 **/
	private Collection<Tree> appositions;
	/**
	 * <p>
	 * Parenthetical phrases in the current sentence.
	 * </p>
	 **/
	private Collection<Phrase> parentheticals;
	/**
	 * <p>
	 * Modifiers phrases in the current sentence.
	 * </p>
	 **/
	private Collection<Phrase> modifiers;
	/**
	 * <p>
	 * Named entities in the current sentence.
	 * </p>
	 **/
	private Collection<Word> namedEntities;
	/**
	 * <p>
	 * Number of words.
	 * </p>
	 **/
	private int totalWords;
	/**
	 * <p>
	 * Document id.
	 * </p>
	 **/
	private int docId;
	/**
	 * <p>
	 * Subsentence.
	 * </p>
	 **/
	// (indicates that this is a subsentence of the sentence retrieved from the
	// #docId.#position).
	private boolean subsentence;
	private boolean isTitle;
	/**
	 * <p>
	 * Sentence that represents the simplified version of this sentence.
	 * </p>
	 **/
	private Sentence simplifiedVersion;

	/**
	 * <p>
	 * Keys of the clusters to which the sentence has been added.
	 * </p>
	 **/
	private int similarityKey;
	private Word keywordsKey;
	private boolean hasConnective;

	/** -- Score values -- **/
	/**
	 * <p>
	 * Sentence score (considering all documents in the collection).
	 * </p>
	 **/
	private double score;
	/**
	 * <p>
	 * Extra score (used to give bonus to this sentence - computed in the
	 * clustering phase).
	 * </p>
	 **/
	private double extraScore;
	/**
	 * <p>
	 * Score of the document in which the sentence is.
	 * </p>
	 **/
	private double documentScore;
	/**
	 * <p>
	 * Simplification score (if the sentence is a subsentence)./p>
	 **/
	private double simplificationScore;
	/**
	 * <p>
	 * Cluster score.
	 * </p>
	 **/
	private double clustersSize;
	/**
	 * <p>
	 * Relative position of the sentence in the text.
	 * </p>
	 **/
	private double relativePosition;
	/**
	 * <p>
	 * Relative position of the sentence ratio in all texts.
	 * </p>
	 **/
	private double relativePositionRatio;
	/**
	 * <p>
	 * Number of system keywords in this sentence.
	 * </p>
	 **/
	private int numberOfKeywords;
	/**
	 * <p>
	 * Ratio of keywords words in the sentence.
	 * </p>
	 **/
	private double keywordsRatio;
	/**
	 * <p>
	 * Keywords cluster score (it is the score of the centroid of the cluster).
	 * </p>
	 **/
	private double keywordClusterScore;
	/**
	 * <p>
	 * Similarity cluster score (it is the score of the centroid of the
	 * cluster).
	 * </p>
	 **/
	private double similarityClusterScore;
	/**
	 * <p>
	 * Number of named entities in the sentence.
	 * </p>
	 **/
	private int numberOfNamedEntities;
	/**
	 * <p>
	 * Ratio of the namedEntities in the sentence.
	 * </p>
	 **/
	private double namedEntitiesRatio;
	/**
	 * <p>
	 * Average of the scores of the named entities in the sentence.
	 * </p>
	 **/
	private double namedEntitiesAverageScore;
	/**
	 * <p>
	 * Number of keywords normalized by the total number of keywords.
	 * </p>
	 **/
	private double keywordsAverageScore;
	/**
	 * <p>
	 * Similarity to the biggest sentence of the collection.
	 * </p>
	 **/
	private double similarity2biggest;
	/**
	 * <p>
	 * Number of CN or PNM (words not to ignore) in the sentence.
	 * </p>
	 **/
	private int numberOfRelevantWords;
	/**
	 * <p>
	 * Ratio of relevant words in the sentence.
	 * </p>
	 **/
	private double relevantWordsRatio;
	/**
	 * <p>
	 * Ratio of relevant words in the sentence.
	 * </p>
	 **/
	private double relevantWordsAverageScore;
	/**
	 * <p>
	 * Number of NPs in the tree sentence.
	 * </p>
	 **/
	private int numberOfNPs;
	/**
	 * <p>
	 * Sentence position in the text.
	 * </p>
	 **/
	private double avgPositionSimCluster;
	private double simClusterSize;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Sentence.
	 * </p>
	 */
	public Sentence() {
		this.score = 0;
		this.extraScore = 0;
		this.clustersSize = 0;
		this.simplificationScore = 0;
		this.setSimilarityKey(-1);
		this.setKeywordsKey(null);
		this.length = -1;
		this.absolutePosition = -1;
		this.relativePosition = -1;
		this.words = new LinkedList<Word>();
		this.relativeClauses = new LinkedList<Phrase>();
		this.appositions = new LinkedList<Tree>();
		this.parentheticals = new LinkedList<Phrase>();
		this.modifiers = new LinkedList<Phrase>();
		this.namedEntities = new LinkedList<Word>();
		this.setSubsentence(false);
		this.numberOfKeywords = 0;
		this.keywordClusterScore = 0;
		this.similarityClusterScore = 0;
		this.numberOfNamedEntities = 0;
		this.namedEntitiesAverageScore = 0;
		this.keywordsAverageScore = 0;
		this.similarity2biggest = 0;
		this.numberOfRelevantWords = 0;
		this.relevantWordsRatio = 0;
		this.relevantWordsAverageScore = 0;
		this.keywordsRatio = 0;
		this.namedEntitiesRatio = 0;
		this.numberOfNPs = 0;
		this.simplifiedVersion = null;
		this.isTitle = false;
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new sentence based on its string.
	 * </p>
	 * 
	 * @param absolutePosition
	 *            the sentence position (its identification).
	 * @param relativePosition
	 *            of the sentence in the text.
	 * @param sentence
	 *            the String containing the original sentence.
	 */
	public Sentence(int absolutePosition, String sentence) {
		this();
		this.absolutePosition = absolutePosition;
		this.sentence = sentence;
		this.length = sentence.length();
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new sentence based on its string.
	 * </p>
	 * 
	 * @param absolutePosition
	 *            the sentence position (its identification).
	 * @param relativePosition
	 *            of the sentence in the text.
	 * @param sentence
	 *            the String containing the original sentence.
	 */
	public Sentence(String sentence, Collection<Word> words) {
		this();
		this.sentence = sentence;
		this.setWords(words);
		this.length = sentence.length();
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new sentence based on its string, its position, its annotated
	 * string, and its named-entity annotation.
	 * </p>
	 * 
	 * @param docId
	 *            the document identification.
	 * @param absolutePosition
	 *            the sentence position.
	 * @param relativePosition
	 *            of the sentence in the text.
	 * @param sentence
	 *            the String containing the original sentence.
	 * @param posTagged
	 *            the sentence with pos tagging.
	 * @param neAnnotation
	 *            the sentence with named entity annotation.
	 */
	public Sentence(int docId, int absolutePosition, String sentence,
			String posTagged, String neAnnotation) {
		this();
		this.docId = docId;
		this.absolutePosition = absolutePosition;
		this.sentence = sentence;
		this.posTagged = posTagged;
		this.neAnnotation = neAnnotation;
		this.length = sentence.length();
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new sentence based on its string, its position, its annotated
	 * string, and its named-entity annotation.
	 * </p>
	 * 
	 * @param docId
	 *            the document identification.
	 * @param absolutePosition
	 *            the sentence position.
	 * @param relativePosition
	 *            of the sentence in the text.
	 * @param sentence
	 *            the String containing the original sentence.
	 * @param posTagged
	 *            the sentence with pos tagging.
	 * @param neAnnotation
	 *            the sentence with named entity annotation.
	 * @param subsentence
	 *            the indication if this is a subsentence.
	 */
	public Sentence(int docId, int absolutePosition, String sentence,
			String posTagged, String neAnnotation, int similarityKey,
			Word keywordsKey, boolean subsentence) {
		this();
		this.docId = docId;
		this.absolutePosition = absolutePosition;
		this.sentence = sentence;
		this.posTagged = posTagged;
		this.neAnnotation = neAnnotation;
		this.length = sentence.length();
		this.setSubsentence(subsentence);
		this.keywordsKey = keywordsKey;
		this.similarityKey = similarityKey;
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Sentence based on another sentence.
	 * </p>
	 * 
	 * @param newSentence
	 *            the new sentence to be used.
	 */
	public Sentence(Sentence newSentence) {
		this.sentence = newSentence.getSentence();
		this.posTagged = newSentence.getPosTagged();
		this.neAnnotation = newSentence.getNeAnnotation();
		this.parseTree = (newSentence.getParseTree() != null ? newSentence
				.getParseTree().deepCopy() : null);
		this.score = newSentence.getScore();
		this.extraScore = newSentence.getExtraScore();
		this.length = newSentence.getLength();
		this.absolutePosition = newSentence.getAbsolutePosition();
		this.relativePosition = newSentence.getRelativePosition();
		this.setWords(newSentence.getWords());
		this.setPhrases(newSentence.getPhrases());
		this.setAppositions(newSentence.getAppositions());
		this.setParentheticals(newSentence.getParentheticals());
		this.setModifiers(newSentence.getModifiers());
		this.setNamedEntities(newSentence.getNamedEntities());
		this.totalWords = newSentence.getTotalWords();
		this.docId = newSentence.getDocumentId();
		this.subsentence = newSentence.isSubsentence();
		this.simplificationScore = newSentence.getSimplificationScore();
		this.clustersSize = newSentence.getClustersSize();
		this.keywordsKey = newSentence.getKeywordsKey();
		this.similarityKey = newSentence.getSimilarityKey();
		this.numberOfKeywords = newSentence.getNumberOfKeywords();
		this.keywordClusterScore = newSentence.getKeywordClusterScore();
		this.similarityClusterScore = newSentence.getSimilarityClusterScore();
		this.numberOfNamedEntities = newSentence.getNumberOfNamedEntities();
		this.namedEntitiesAverageScore = newSentence
				.getNamedEntitiesAverageScore();
		this.keywordsAverageScore = newSentence.getKeywordsAverageScore();
		this.similarity2biggest = newSentence.getSimilarity2biggest();
		this.numberOfRelevantWords = newSentence.getNumberOfRelevantWords();
		this.relevantWordsRatio = newSentence.getRelevantWordsRatio();
		this.relevantWordsAverageScore = newSentence
				.getRelevantWordsAverageScore();
		this.keywordsRatio = newSentence.getKeywordsRatio();
		this.namedEntitiesRatio = newSentence.getNamedEntitiesRatio();
		this.isTitle = newSentence.isTitle();
	}

	/** Getters & Setters **/
	public String getSentence() {
		return sentence;
	}

	public String getPosTagged() {
		return posTagged;
	}

	public String getNeAnnotation() {
		return neAnnotation;
	}

	public double getScore() {
		return score;
	}

	public double getExtraScore() {
		return extraScore;
	}

	public int getLength() {
		return length;
	}

	public int getAbsolutePosition() {
		return absolutePosition;
	}

	public double getRelativePosition() {
		return relativePosition;
	}

	public int getDocumentId() {
		return docId;
	}

	public double getTotalFrequency() {
		return totalFrequency;
	}

	public Collection<Word> getWords() {
		return words;
	}

	public int getTotalWords() {
		return totalWords;
	}

	public Tree getParseTree() {
		return parseTree;
	}

	public Collection<Phrase> getPhrases() {
		return relativeClauses;
	}

	public Collection<Tree> getAppositions() {
		return appositions;
	}

	public Collection<Phrase> getParentheticals() {
		return parentheticals;
	}

	public Collection<Phrase> getModifiers() {
		return modifiers;
	}

	public boolean isSubsentence() {
		return subsentence;
	}

	public Sentence getSimplifiedVersion() {
		return simplifiedVersion;
	}

	public double getSimplificationScore() {
		return simplificationScore;
	}

	public double getClustersSize() {
		return clustersSize;
	}

	public int getSimilarityKey() {
		return similarityKey;
	}

	public Word getKeywordsKey() {
		return keywordsKey;
	}

	public int getNumberOfKeywords() {
		return numberOfKeywords;
	}

	public double getKeywordClusterScore() {
		return this.keywordClusterScore;
	}

	public double getSimilarityClusterScore() {
		return this.similarityClusterScore;
	}

	public int getNumberOfNamedEntities() {
		return numberOfNamedEntities;
	}

	public double getKeywordsAverageScore() {
		return keywordsAverageScore;
	}

	public Collection<Word> getNamedEntities() {
		return namedEntities;
	}

	public double getSimilarity2biggest() {
		return similarity2biggest;
	}

	public int getNumberOfRelevantWords() {
		return numberOfRelevantWords;
	}

	public double getRelevantWordsRatio() {
		return relevantWordsRatio;
	}

	public double getNamedEntitiesAverageScore() {
		return namedEntitiesAverageScore;
	}

	public double getRelevantWordsAverageScore() {
		return relevantWordsAverageScore;
	}

	public double getKeywordsRatio() {
		return keywordsRatio;
	}

	public double getNamedEntitiesRatio() {
		return namedEntitiesRatio;
	}

	public double getRelativePositionRatio() {
		return relativePositionRatio;
	}

	public Collection<Phrase> getRelativeClauses() {
		return relativeClauses;
	}

	public double getDocumentScore() {
		return documentScore;
	}

	public int getNumberOfNPs() {
		return numberOfNPs;
	}

	public boolean isTitle() {
		return isTitle;
	}

	public boolean hasConnective() {
		return hasConnective;
	}

	public double getAvgPositionSimCluster() {
		return avgPositionSimCluster;
	}

	public double getSimClusterSize() {
		return simClusterSize;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public void setPOSTagged(String posTagged) {
		this.posTagged = posTagged;
	}

	public void setNEAnnotation(String neAnnotation) {
		this.neAnnotation = neAnnotation;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public void setExtraScore(double extraScore) {
		this.extraScore = extraScore;
	}

	public void setTotalFrequency(double totalFrequency) {
		this.totalFrequency = totalFrequency;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setRelativePosition(double relativePosition) {
		this.relativePosition = relativePosition;
	}

	public void setParseTree(Tree parseTree) {
		this.parseTree = parseTree;
	}

	public void setWords(Collection<Word> words) {
		this.words = new LinkedList<Word>(words);
		this.totalWords = words.size();
	}

	public void setPhrases(Collection<Phrase> phrases) {
		this.relativeClauses = phrases;
	}

	public void setAppositions(Collection<Tree> appositions) {
		this.appositions = appositions;
	}

	public void setParentheticals(Collection<Phrase> parenthetical) {
		this.parentheticals = parenthetical;
	}

	public void setModifiers(Collection<Phrase> modifiers) {
		this.modifiers = modifiers;
	}

	public void setSubsentence(boolean subsentence) {
		this.subsentence = subsentence;
	}

	public void setSimplificationScore(double simplificationScore) {
		this.simplificationScore = simplificationScore;
	}

	public void setClustersSize(double clustersSize) {
		this.clustersSize = clustersSize;
	}

	public void setSimilarityKey(int similarityKey) {
		this.similarityKey = similarityKey;
	}

	public void setKeywordsKey(Word keywordsKey) {
		this.keywordsKey = keywordsKey;
	}

	public void setNumberOfKeywords(int numberOfKeywords) {
		this.numberOfKeywords = numberOfKeywords;
	}

	public void setKeywordClusterScore(double keywordClusterScore) {
		this.keywordClusterScore = keywordClusterScore;
	}

	public void setSimilarityClusterScore(double similarityClusterScore) {
		this.similarityClusterScore = similarityClusterScore;
	}

	public void setKeywordsAverageScore(double keywordsAverageScore) {
		this.keywordsAverageScore = keywordsAverageScore;
	}

	public void setNamedEntities(Collection<Word> namedEntities) {
		this.namedEntities = namedEntities;
		this.numberOfNamedEntities = this.namedEntities.size();
	}

	public void setSimilarity2biggest(double similarity2biggest) {
		this.similarity2biggest = similarity2biggest;
	}

	public void setNumberOfRelevantWords(int numberOfRelevantWords) {
		this.numberOfRelevantWords = numberOfRelevantWords;
	}

	public void setRelevantWordsRatio(double relevantWordsRatio) {
		this.relevantWordsRatio = relevantWordsRatio;
	}

	public void setNamedEntitiesAverageScore(double namedEntitiesAverageScore) {
		this.namedEntitiesAverageScore = namedEntitiesAverageScore;
	}

	public void setRelevantWordsAverageScore(double relevantWordsAverageScore) {
		this.relevantWordsAverageScore = relevantWordsAverageScore;
	}

	public void setKeywordsRatio(double keywordsRatio) {
		this.keywordsRatio = keywordsRatio;
	}

	public void setNamedEntitiesRatio(double namedEntitiesRatio) {
		this.namedEntitiesRatio = namedEntitiesRatio;
	}

	public void setRelativePositionRatio(double relativePositionRatio) {
		this.relativePositionRatio = relativePositionRatio;
	}

	public void setRelativeClauses(Collection<Phrase> relativeClauses) {
		this.relativeClauses = relativeClauses;
	}

	public void setDocumentScore(double documentScore) {
		this.documentScore = documentScore;
	}

	public void setNumberOfNPs(int numberOfNPs) {
		this.numberOfNPs = numberOfNPs;
	}

	public void setSimplifiedVersion(Sentence simplified) {
		this.simplifiedVersion = simplified;
	}

	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	public void setAvgPositionSimCluster(double minPositionInCluster) {
		this.avgPositionSimCluster = minPositionInCluster;
	}

	public void setSimClusterSize(double simClusterSize) {
		this.simClusterSize = simClusterSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "\n" + docId + "\t" + absolutePosition + "\t" + relativePosition
				+ "\t" + sentence + "\n" + printWords() + "\n\tScore: " + score
				+ "\tExtra: " + extraScore + "\tSimplification: "
				+ simplificationScore + "\tDocument: " + documentScore
				+ "\n\tClusterSize: " + clustersSize + "\tKeywordScore:"
				+ keywordClusterScore + "\tSimilarityScore: "
				+ similarityClusterScore + "\n\tPosRatio: "
				+ relativePositionRatio + "\tkeywordRatio: " + keywordsRatio
				+ "\tneRatio: " + namedEntitiesRatio + "\trelRatio: "
				+ relevantWordsRatio + "\n\t#keywords: " + numberOfKeywords
				+ "\t#NEs: " + numberOfNamedEntities + "\t#relevant: "
				+ numberOfRelevantWords + "\n\tNEAvgScore: "
				+ namedEntitiesAverageScore + "\tKwAvgScore: "
				+ keywordsAverageScore + "\tRelAvgScore: "
				+ relevantWordsAverageScore + "\tSim2big: "
				+ similarity2biggest + "\n\tCompleteScore: "
				+ this.completeScore() + "\n\tKeywordKey: " + this.keywordsKey
				+ "\tSimiliarityKey: " + this.similarityKey;

	}

	/**
	 * <p>
	 * Computes the sentence score, based on each word frequency.
	 * </p>
	 * 
	 * @return the current sentence score.
	 */
	public double computeScore() {

		double score = 0, frequency = 0;

		for (Word word : words) {

			word.computeScore();
			score += word.getScore();
			frequency += word.getTFIDF();

		}

		frequency = frequency / (double) this.totalWords;
		score = score / (double) this.totalWords;

		this.simplificationScore = score;

		return score;
	}

	public double computeWordExtraScore() {

		double extraScore = this.extraScore;

		for (Word word : words)
			extraScore += word.getExtraScore();

		return extraScore;
	}

	/**
	 * <p>
	 * Retrieves the sentence complete score, which includes the original
	 * sentence score and its extra score.
	 * </p>
	 * 
	 * @return the sentence complete score value.
	 */
	public double completeScore() {
		return score + extraScore;
	}

	public double orderingScore() {
		// return score + extraScore;
		return ((score + extraScore + keywordClusterScore
				+ similarityClusterScore + relativePosition) * 0.8)
				+ ((numberOfKeywords + numberOfNamedEntities) * 0.2);
	}

	/**
	 * // * @deprecated
	 * <p>
	 * Computes the sentence cluster score.
	 * </p>
	 * 
	 * @return
	 */
	private double computeClusterScore() {
		// return (keywordClusterScore) + (similarityClusterScore);
		// return (keywordClusterScore * 0.8) + (similarityClusterScore * 0.2);
		// // Em uso antes dos pesos dos scores no ficheiro de configuração
		return ((keywordClusterScore * Preferences.SENTENCE_KEYWORDCLUSTERSCORE) * 0.8 + (similarityClusterScore * Preferences.SENTENCE_SIMILARITYCLUSTERSCORE) * 0.2);
	}

	/**
	 * <p>
	 * Updates the extra score of this sentence by adding the given value.
	 * </p>
	 * 
	 * @param value
	 *            the value to be added to this sentence score.
	 */
	public void updateExtraScore(double value) {
		this.extraScore += value;
	}

	// /**
	// * <p>Retrieves the sentence words sorted by frequency.</p>
	// *
	// * @return the word collection.
	// */
	// public Collection<Word> getSortedWords(){
	// LinkedList<Word> words = new LinkedList<Word>(this.words);
	// Collections.sort(words, Preferences.COMPARE_WORD_FREQUENCY);
	// return words;
	// }
	//
	/**
	 * <p>
	 * Checks whether two sentences are the same.
	 * </p>
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.sentence.equals(((Sentence) obj).getSentence());
	}

	/**
	 * <p>
	 * Checks if the two sentences are the sentence occurring in the same
	 * document, and in the same position.
	 * </p>
	 * 
	 * @param sentence
	 *            the Sentence to be compared.
	 * @return true if the sentences are the same; false otherwise.
	 */
	public boolean sameSentence(Sentence sentence) {
		return this.docId == sentence.getDocumentId()
				&& this.absolutePosition == sentence.getAbsolutePosition();
	}

	/**
	 * <p>
	 * Computes the keyword occurences in the the current sentence.
	 * </p>
	 * 
	 * @param word
	 *            the word to be counted in the sentence.
	 * @return the number of occurrences of the given word in this sentence.
	 */
	public int computeKeywordOccurrences(Word word) {
		int occurrences = 0;

		// System.out.println("\n\n"+this.sentence);

		if (word instanceof SingleWord) {

			for (Word currentWord : words) {
				if (currentWord.represents(word))
					occurrences++;
			}
		} else if (word instanceof NamedEntity) {

			Collection<Word> thisNEWords = new LinkedList<Word>();
			Collection<Word> givenNEWords = new LinkedList<Word>(
					((NamedEntity) word).getEntityWords());
			//
			for (Word neWord : namedEntities) {
				// System.out.println("\t\t"+neWord);
				thisNEWords.addAll(((NamedEntity) neWord).getEntityWords());
			}

			for (Word neWord : givenNEWords) {

				// System.out.println("\t\t"+neWord);
				// for (Word currentWord : words)
				for (Word currentWord : givenNEWords)
					// if (!currentWord.equals(neWord))
					if (currentWord.represents(neWord))
						occurrences++;
			}
			// occurrences = (int) occurrences / givenNEWords.size();
		}

		// System.out.println("\t"+word+"\t______"+occurrences);

		return occurrences;
	}

	/**
	 * <p>
	 * Computes the similarity between this sentence and the given one.
	 * </p>
	 * <p>
	 * Computes the similarity between the simplified version of this sentence
	 * and the simplified version of the given one.
	 * </p>
	 * 
	 * @param sentence
	 *            the sentence to be compared.
	 * @return the highest similarity value. The one of the simplified sentence
	 *         or the original one.
	 */
	public double computeSimilarity(Sentence sentence) {

		double similarityOriginal = Utils.computeSentence2SentenceSimilarity(
				this, sentence), similaritySimplified = 0, similarity = similarityOriginal;

		// if (this.simplifiedVersion != null && sentence.getSimplifiedVersion()
		// != null)
		// similaritySimplified =
		// Utils.computeSentence2SentenceSimilarity(this.simplifiedVersion,
		// sentence.getSimplifiedVersion());
		//
		// if (similaritySimplified > similarityOriginal)
		// similarity = similaritySimplified;

		return similarity;
	}

	/**
	 * <p>
	 * Checks if this sentence is to be ignored.
	 * </p>
	 * 
	 * @return true if this sentence fits the criteria to be ignored; false
	 *         otherwise.
	 */
	public boolean toIgnore() {
		return this.totalWords <= Preferences.MIN_SENTENCE_LENGTH;
	}

	/**
	 * <p>
	 * Computes the total frequency of this sentence.
	 * </p>
	 * 
	 * @return the current sentence frequency.
	 */
	public double computeFrequency() {

		double frequency = 0;

		for (Word word : this.words) {

			if (word instanceof SingleWord) {
				SingleWord singleWord = (SingleWord) word;
				frequency += singleWord.getFrequency();
			}
		}

		frequency = (frequency / this.totalWords);

		return frequency;

	}

	/**
	 * <p>
	 * Gets the sentence word corresponding to the given word string.
	 * </p>
	 * 
	 * @param the
	 *            string word to be searched.
	 * 
	 * @return the corresponding Word, null if not found.
	 */

	public Word getWord(String strWord) {
		Word word = null;

		// First searches for the original word...
		for (Word current : this.words) {
			if (current instanceof SingleWord) {
				SingleWord singleWord = (SingleWord) current;
				if (singleWord.getOriginal().equals(strWord)

						|| singleWord.getWord().equals(strWord)
						|| singleWord.getWord().equals(
								Regex.getWordToken(strWord))) {
					word = singleWord;
					break;
				}
			}
		}

		return word;
	}

	/**
	 * <p>
	 * Checks if this sentence contains a given word.
	 * </p>
	 * 
	 * @param word
	 *            the word to be searched in the sentence.
	 * @return true if the sentence contains the word; false otherwise.
	 */
	public boolean containsWord(Word word) {

		boolean found = false;

		if (word instanceof SingleWord) {
			for (Word sentenceWord : this.words) {
				SingleWord singleWord = (SingleWord) sentenceWord;
				found = singleWord.same(word);

				if (found)
					break;
			}
		} else if (word instanceof NamedEntity) {
			for (Word namedEntity : this.namedEntities) {
				NamedEntity ne = (NamedEntity) namedEntity;
				found = ne.same(word);

				if (found)
					break;
			}

		}

		return found;
	}

	/**
	 * <p>
	 * Retrieves the representation of this sentence based on its words
	 * representation.
	 * </p>
	 * 
	 * @return a String that represents the sentence.
	 */
	public String representation() {

		String words = "";

		for (Word word : this.words)
			words += word.representation() + " ";

		return words.trim();
	}

	/**
	 * <p>
	 * Builds a new sentence based on the given tree and on the current sentence
	 * data.
	 * </p>
	 * 
	 * @param document
	 *            the document in which this sentence occurs.
	 * @param subtree
	 *            the output sentence parse tree.
	 * 
	 * @return a new sentence based on the current one, pruned based on the
	 *         sentence tree.
	 */
	public Sentence buildSubSentence(Tree subtree) {

		String[] leaves = Regex.getUntokenizedLeavesTokens(subtree);
		LinkedList<Word> phraseWords = new LinkedList<Word>();
		String sentence = "";

		for (int i = 0; i < leaves.length; i++) {

			SingleWord word = (SingleWord) this.getWord(leaves[i]);

			if (word != null) {
				phraseWords.add(word);
				sentence += word.getOriginal() + " ";
			}
		}

		if (sentence.equals(""))
			return null;

		Sentence subsentence = createSentence(subtree, phraseWords, sentence);

		return subsentence;
	}

	/**
	 * <p>
	 * Creates a sentence that differs from this on its tree, words and string
	 * sentence.
	 * </p>
	 * 
	 * @param tree
	 *            the new sentence subtree.
	 * @param words
	 *            the sentence words.
	 * @param sentence
	 *            the sentence string.
	 * @return a new Sentence.
	 */
	public Sentence createSentence(Tree tree, Collection<Word> words,
			String sentence) {

		// Builds the subsentence based on the original (complete) sentence.

		String posTagged = ManageExternalTools.posAnnotation(sentence);
		String ner = ManageExternalTools.namedEntityNERAnnotation(posTagged);
		Sentence subsentence = new Sentence(this.getDocumentId(),
				this.getAbsolutePosition(), sentence, posTagged, ner,
				this.similarityKey, this.keywordsKey, true);
		subsentence.setRelativePosition(this.relativePosition);
		subsentence.setWords(words);
		subsentence.setParseTree(tree.deepCopy());
		subsentence.setExtraScore(this.extraScore);
		subsentence.setScore(this.score);
		subsentence.setClustersSize(this.clustersSize);
		subsentence.setKeywordClusterScore(this.keywordClusterScore);
		subsentence.setNumberOfKeywords(this.numberOfKeywords);
		subsentence.setKeywordsAverageScore(this.keywordsAverageScore);
		subsentence.setDocumentScore(this.documentScore);
		subsentence.setRelativePosition(this.relativePosition);
		subsentence.setRelativePositionRatio(this.relativePositionRatio);
		subsentence.setKeywordsRatio(this.keywordsRatio);
		subsentence.setSimilarityClusterScore(this.similarityClusterScore);
		subsentence.setKeywordsKey(this.getKeywordsKey());

		subsentence.setTotalFrequency(subsentence.computeFrequency());

		Collection<Word> subNamedEntities = this
				.retrieveSubNamedEntities(words);
		subsentence.setNamedEntities(subNamedEntities);

		double subSentenceScore = subsentence.computeScore();
		subsentence.setScore(subSentenceScore);

		subsentence.computeRelevantWordsProperties();

		return subsentence;
	}

	/**
	 * <p>
	 * Retrieves the named entities of the subsentence given in the form of its
	 * words.
	 * </p>
	 * 
	 * @param phraseWords
	 *            the words of the subsentence.
	 * @return the collection of named entities occurring in the given
	 *         subsentence.
	 */
	public Collection<Word> retrieveSubNamedEntities(
			Collection<Word> phraseWords) {
		boolean found = false;
		Collection<Word> subNamedEntities = new LinkedList<Word>();

		for (Word ne : this.namedEntities) {

			Collection<SingleWord> neWords = ((NamedEntity) ne)
					.getEntityWords();

			for (SingleWord neWord : neWords) {

				found = Utils.isWordInCollection(phraseWords, neWord);

				if (found)
					break;
			}

			if (found)
				subNamedEntities.add(ne);

		}

		return subNamedEntities;
	}

	/**
	 * <p>
	 * Computes the number of relevant words in this sentence and its relevant
	 * words ratio.
	 * </p>
	 * <ul>
	 * <li>number of relevant words;</li>
	 * <li>relevant words ratio;</li>
	 * <li>relevant words average score;</li>
	 * </ul>
	 */
	public void computeRelevantWordsProperties() {

		this.numberOfRelevantWords = 0;
		this.relevantWordsAverageScore = 0;

		for (Word word : this.words) {
			if (word.toUse()) {
				numberOfRelevantWords++;
				relevantWordsAverageScore += word.getScore();
			}
		}

		this.relevantWordsRatio = (double) this.numberOfRelevantWords
				/ (double) this.totalWords;

		if (numberOfRelevantWords > 0)
			this.relevantWordsAverageScore = this.relevantWordsAverageScore
					/ this.numberOfRelevantWords;
	}

	/**
	 * <p>
	 * Computes the properties related to the keywords in this sentence:
	 * </p>
	 * <ul>
	 * <li>number of keywords;</li>
	 * <li>keywords ratio;</li>
	 * <li>keywords average score;</li>
	 * </ul>
	 * 
	 * @param keywords
	 *            the collection of keywords of all texts.
	 */
	public void computeKeywordsProperties(Collection<Word> keywords) {
		this.numberOfKeywords = Utils.countKeywordsInSentence(keywords, this);
		this.keywordsAverageScore = Utils.keywordsInSentenceAverageScore(
				keywords, this);
		this.keywordsRatio = (double) numberOfKeywords
				/ (double) keywords.size();
	}

	/**
	 * <p>
	 * Computes the properties related to the named entities in this sentence:
	 * </p>
	 * <ul>
	 * <li>NEs ratio;</li>
	 * <li>NEs average score;</li>
	 * </ul>
	 * 
	 * @param numberOfNamedEntitiesInText
	 *            number of NEs in the text where the sentence occurs.
	 */
	public void computeNamedEntitiesProperties(
			double numberOfNamedEntitiesInText) {
		this.namedEntitiesRatio = (double) this.numberOfNamedEntities
				/ numberOfNamedEntitiesInText;
		this.namedEntitiesAverageScore = Utils
				.namedEntitiesInSentenceAverageScore(this);
	}

	/**
	 * <p>
	 * Retrieves the tree that represents the passage that starts with the given
	 * first word and ends in the given last word.
	 * </p>
	 * 
	 * @param firstWord
	 *            the passage first word;
	 * @param lastWord
	 *            the passage last word;
	 * @return the tree that represents the passage; null if it does not exist.
	 */
	public Tree retrieveSubTreeFromWords(Word firstWord, Word lastWord) {

		List<Tree> leaves = this.parseTree.getLeaves();
		Tree firstLeaf = null, lastLeaf = null, parentTree = null;

		for (Tree tree : leaves) {

			if (tree.nodeString()
					.toLowerCase()
					.matches(
							((SingleWord) firstWord).getWord().toLowerCase()
									+ Regex.TREE_NODE_INDEX))
				firstLeaf = tree;

			if (firstLeaf != null
					&& tree.nodeString()
							.toLowerCase()
							.matches(
									((SingleWord) lastWord).getWord()
											.toLowerCase()
											+ Regex.TREE_NODE_INDEX)) {
				lastLeaf = tree;
				break;
			}
		}

		if (firstLeaf != null && lastLeaf != null) {

			if (firstLeaf.equals(lastLeaf)) {
				parentTree = this.parseTree.joinNode(firstLeaf, lastLeaf);
				parentTree = parentTree.parent(this.parseTree);
			} else
				parentTree = this.parseTree.joinNode(firstLeaf, lastLeaf);
		}

		return parentTree;
	}

	/**
	 * <p>
	 * Retrieves the subtree from a the given sentence.
	 * </p>
	 * 
	 * @param sentence
	 *            the sentence to be searched in the current sentence.
	 * @return the subtree in the current sentence that represents the given
	 *         sentence; null if it does not exist.
	 */
	public Tree retrieveSubTreeFromSentence(Sentence sentence) {

		SingleWord firstWord = (SingleWord) ((LinkedList<Word>) sentence
				.getWords()).getFirst(), lastWord = (SingleWord) ((LinkedList<Word>) sentence
				.getWords()).getLast();
		Tree subtree = retrieveSubTreeFromWords(firstWord, lastWord);

		return subtree;
	}

	/**
	 * <p>
	 * Checks if this sentence contains the given sentence.
	 * </p>
	 * 
	 * @param subsentence
	 *            the sentence to check if is contained in this sentence.
	 * @return true if this sentence contains the subsentence; false otherwise.
	 */
	public boolean containsSentence(Sentence subsentence) {

		boolean found = false;
		Word firstWord = (new LinkedList<Word>(subsentence.getWords()))
				.getFirst();

		for (int i = 0; i < this.words.size(); i++) {

			Word sentenceWord = (new LinkedList<Word>(this.words)).get(i);

			if (sentenceWord.represents(firstWord)) {
				found = true;

				for (int j = 0; j < subsentence.getWords().size()
						&& i < this.words.size(); j++, i++) {

					Word subsentenceWord = (new LinkedList<Word>(
							subsentence.getWords())).get(j);
					sentenceWord = (new LinkedList<Word>(this.words)).get(i);
					found = found && sentenceWord.represents(subsentenceWord);
				}
			}
			if (found)
				break;

		}

		return found;
	}

	/**
	 * <p>
	 * Checks if this sentence contains the given named entity.
	 * </p>
	 * 
	 * @param namedEntity
	 *            the named entity to be checked.
	 * @return true if this sentence contains the given named entity; false
	 *         otherwise.
	 */
	public boolean containsNamedEntity(NamedEntity namedEntity) {
		return Utils.isWordInCollection(this.namedEntities, namedEntity);
	}

	/**
	 * <p>
	 * Retrieves a named entity tree.
	 * </p>
	 * 
	 * @param namedEntity
	 *            the named entity to be retrieved.
	 * @return the tree corresponding to the named entity.
	 */
	public Tree retrieveEntityTree(Sentence namedEntity) {
		SingleWord firstWord = (SingleWord) ((LinkedList<Word>) namedEntity
				.getWords()).getFirst(), lastWord = (SingleWord) ((LinkedList<Word>) namedEntity
				.getWords()).getLast();

		Tree entityTree = this.retrieveSubTreeFromWords(firstWord, lastWord);

		return entityTree;
	}

	/**
	 * <p>
	 * Inserts, in this sentence, an apposition phrase after the entity
	 * expression.
	 * </p>
	 * 
	 * @param entity
	 *            the entity sentence
	 * @param apposition
	 *            the apposition sentence.
	 */
	public Sentence insertEntityAndApposition(Sentence entity,
			Sentence apposition) {

		Sentence finalSentence = new Sentence(this);

		Tree newTree = finalSentence.retrieveSubTreeFromSentence(entity);

		if (newTree != null) {

			Tree comma = new LabeledScoredTreeNode(new StringLabel("PNT"));
			comma.addChild(new LabeledScoredTreeNode(new StringLabel(",/-1"))); // Includes
																				// the
																				// leaf
																				// node
																				// number

			Tree phrase = apposition.getParseTree().deepCopy();
			Tree appTree = new LabeledScoredTreeNode(phrase.label());
			Tree entityTree = newTree.deepCopy();
			appTree.setChildren(new Tree[] { comma.deepCopy(), phrase,
					comma.deepCopy() });

			newTree.setValue("NP");
			newTree.setChildren(new Tree[] { entityTree, appTree });

			finalSentence = Utils.completeSentence(finalSentence, apposition,
					entity, true);

		}

		return finalSentence;

	}

	/**
	 * <p>
	 * Replaces the apposition by the named entity given in this sentence.
	 * </p>
	 * 
	 * @param apposition
	 *            the apposition sentence to be replaced.
	 * @param entity
	 *            the named entity to put in the sentence.
	 * @return the new sentence containing the named entity instead of the
	 *         apposition.
	 */
	public Sentence replaceApposition(Sentence entity, Sentence apposition) {

		Sentence finalSentence = new Sentence(this);

		Tree appTree = finalSentence.retrieveSubTreeFromSentence(apposition);

		if (appTree != null) {

			Tree neTree = entity.getParseTree().deepCopy();
			Tree firstLeaf = (new LinkedList<Tree>(neTree.getLeaves()))
					.getFirst();

			String newLabel = ManageTrees.getLeafNode(firstLeaf.nodeString())
					+ "/-1";
			firstLeaf.setLabel(new LabeledScoredTreeNode(new StringLabel(
					newLabel)));
			appTree.setValue(neTree.nodeString());
			appTree.setChildren(neTree.getLeaves());

			finalSentence = Utils.completeSentence(finalSentence, entity,
					apposition, false);

			finalSentence = Utils.correctArticleBeforeEntity(finalSentence,
					entity);
		}

		return finalSentence;
	}

	/**
	 * <p>
	 * Removes the apposition sentence from this sentence.
	 * </p>
	 * 
	 * @param value
	 *            the apposition sentence.
	 * 
	 * @return the new sentence without the apposition.
	 */
	public Sentence removeApposition(Sentence value) {

		Sentence finalSentence = this;

		Tree subtree = this.retrieveSubTreeFromSentence(value);

		if (subtree != null) {

			Set<Tree> toRemove = new HashSet<Tree>();

			Tree articleTree = Utils.retrieveArticleTree(this, value);
			toRemove.add(articleTree);
			toRemove.add(subtree);

			Tree removed = ManageTrees.removeTrees(this.parseTree, toRemove);
			finalSentence = this.buildSubSentence(removed);

		}
		return finalSentence;
	}

	public String printWords() {

		String wordsString = "";
		for (Word w : this.words)
			wordsString += "\t\t" + w + "\n";

		return wordsString;
	}

	public void insertConnective(String sentence,
			Collection<Word> connectiveWords) {

		updateString(sentence, ManageExternalTools.posAnnotation(sentence));
		LinkedList<Word> newWords = new LinkedList<Word>(this.words);
		newWords.addAll(0, connectiveWords);
		this.setWords(newWords);
		this.hasConnective = true;
	}

	public void updateString(String sentence, String pos) {
		this.setSentence(sentence);
		this.setPOSTagged(pos);
		this.setNEAnnotation(null);
	}

	@Override
	public int compareTo(Object o) {

		Sentence sentence = (Sentence) o;
		if (this.score > sentence.getScore())
			return 1;
		else if (this.score < sentence.getScore())
			return -1;

		return 0;
	}
}