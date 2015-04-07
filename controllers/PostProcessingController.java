package controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import preferences.Preferences;
import preferences.Regex;
import preferences.Utils;
import simplification.PostProcessedText;
import summarization.Summary;
import aux.PhraseCluster;
import aux.PowerSet;
import aux.SentenceCluster;
import core.Connective;
import core.Document;
import core.ManageTrees;
import core.Paragraph;
import core.Phrase;
import core.Sentence;
import core.SingleWord;
import core.Word;
import edu.stanford.nlp.trees.Tree;
import external.ManageClassification;
import external.ManageExternalTools;

/**
 * <p>
 * This class manages the simplification process.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class PostProcessingController extends Controller {

	private Hashtable<String, List<Connective>> annotatedConnectives;
	private List<Connective> insertedConnectives;
	private int maximumCompression;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Simplification Controller.
	 * </p>
	 */
	public PostProcessingController() {
		super();
		this.annotatedConnectives = new Hashtable<String, List<Connective>>();
		this.insertedConnectives = new LinkedList<Connective>();
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Simplification Controller based on a previously built
	 * summary.
	 * </p>
	 * 
	 * @param text
	 *            the previously built summary.
	 */
	public PostProcessingController(Summary summary) {
		this();
		this.keywords = Utils.extractKeywords(summary.getSentences());
		this.setSummary(summary);
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Simplification Controller based on a previously built
	 * summary, and based on the previously defined keywords.
	 * </p>
	 * 
	 * @param summary
	 *            the previously built summary.
	 * @param keywords
	 *            the previously defined keywords.
	 */
	public PostProcessingController(Summary summary, Collection<Word> keywords) {
		this();
		this.keywords = new LinkedList<Word>(keywords);
		this.setSummary(summary);
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Simplification Controller based on a previously built
	 * summary, and based on the previously defined keywords.
	 * </p>
	 * 
	 * @param documents
	 *            the document set.
	 * @param summary
	 *            the previously built summary.
	 * @param keywords
	 *            the previously defined keywords.
	 * @param maximumCompression
	 *            the maximum compression of the simplified text.
	 */
	public PostProcessingController(Collection<Document> documents,
			Summary summary, Collection<Word> keywords) {
		this();
		this.documents = documents;
		this.keywords = new LinkedList<Word>(keywords);
		this.setSummary(summary);
	}

	public void setCompression(int compression) {
		this.maximumCompression = compression;
	}

	/**
	 * <p>
	 * Prepares the data to be used by the controller.
	 * </p>
	 */
	public void preprocessing() {
		submitDocuments();
		manageDocuments();
		computeGlobalProperties();

		LinkedList<Sentence> sentences = new LinkedList<Sentence>();
		this.computeGlobalKeywords();

		for (Document document : documents)
			sentences.addAll(document.getText().getSentences());

		this.summary = new Summary(sentences);
	}

	public void postprocessText() {

		if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.SIMPLIFIED)
			completePostProcessing();
		else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.ONLYPOSTPROCESSEDNOSUMMARY)
			completePostProcessing();
	}

	private void completePostProcessing() {

		LinkedList<Sentence> summarySentences = new LinkedList<Sentence>(
				this.summary.getSentences());
		LinkedList<Sentence> newSentences = (LinkedList<Sentence>) Utils
				.filterSentencesByWordCompression(summarySentences,
						this.maximumCompression);

		System.out.println("\tSimplifying sentences...");
		newSentences = (LinkedList<Sentence>) sentenceReduction(
				summarySentences, newSentences);

		System.out.println("\tArranging paragraphs...");
		newSentences = (LinkedList<Sentence>) arrangeSentencesInParagraphs(newSentences);

		System.out.println("\tInserting connectives...");
		newSentences = (LinkedList<Sentence>) insertConnectives(newSentences);

		this.postProcessedText = new PostProcessedText(newSentences);

		System.out.println("\t\t# summary sentences post-processed: "
				+ this.postProcessedText.getTotalSentences());
		System.out.println("\t\t# words in summary sentences: "
				+ this.postProcessedText.getTotalWords());

	}

	private Collection<Sentence> sentenceReduction(
			Collection<Sentence> originalSentences,
			Collection<Sentence> sentences2summary) {

		LinkedList<Sentence> summarySentences = new LinkedList<Sentence>(
				originalSentences);
		int index = sentences2summary.size();
		LinkedList<Sentence> newSentences = (LinkedList<Sentence>) simplifySentences(sentences2summary);
		int addedWords = Utils.countTotalWordsInCollection(newSentences);

		while (addedWords <= this.maximumCompression
				&& index < summarySentences.size()) {

			Collection<Sentence> moreSentences = new LinkedList<Sentence>();
			addedWords = Utils.countTotalWordsInCollection(newSentences);

			for (int i = index; i < summarySentences.size(); i++) {

				addedWords += summarySentences.get(i).getTotalWords();
				moreSentences.add(summarySentences.get(i));

				if (addedWords > this.maximumCompression)
					break;
			}

			if (moreSentences.size() > 0) {

				moreSentences = (LinkedList<Sentence>) simplifySentences(moreSentences);
				newSentences.addAll(moreSentences);
				index += moreSentences.size();
			}
		}

		return newSentences;
	}

	/**
	 * <p>
	 * Simplifies a sentence.
	 * </p>
	 * 
	 * @param sentence
	 *            a to be simplified.
	 * @return a simplified sentence.
	 */
	private Sentence simplifySentence(Sentence sentence) {

		sentence.setParseTree(ManageExternalTools.parse(sentence.getPosTagged()));

		Tree original = sentence.getParseTree().deepCopy();
		Sentence simplified = new Sentence(sentence);
		boolean hasBeenSimplified = false;

		if (original != null) {
			Collection<Phrase> removableTrees = ManageTrees
					.convertTrees2Phrases(ManageTrees
							.removablePassages(original));
			Map<Sentence, Set<Phrase>> candidateSentences = new HashMap<Sentence, Set<Phrase>>();

			if (removableTrees.size() > 0) {

				PowerSet<Phrase> powerSet = new PowerSet<Phrase>(
						new TreeSet<Phrase>(removableTrees));

				for (Set<Phrase> current : powerSet) {

					Tree removed = ManageTrees.removePhrases(original, current);

					if (removed != null) {

						Sentence subsentence = sentence
								.buildSubSentence(removed);

						if (subsentence != null
								&& subsentence.getTotalWords() > 0
								&& sentence.getTotalWords() != subsentence
										.getTotalWords())
							candidateSentences.put(subsentence, current);
					}
				}

				// The first sentence in the collection is the one with the
				// maximum score,
				// so that it will be chosen to replace the original one.
				if (candidateSentences.size() > 0) {

					LinkedList<Sentence> sentences = new LinkedList<Sentence>(
							candidateSentences.keySet());

					sentences.add(sentence);
					// The candidate sentences collection is sorted by the
					// sentence simplification score.
					Collections.sort(sentences,
							Preferences.COMPARE_SENTENCE_COMPLETE_SCORE);

					Sentence firstPlace = sentences.getFirst();

					if (firstPlace.getTotalWords() != sentence.getTotalWords()) {

						Set<Phrase> trees2remove = candidateSentences
								.get(firstPlace);
						Tree simplifiedTree = ManageTrees.removePhrases(
								original, trees2remove);
						simplified = sentence.buildSubSentence(simplifiedTree);
						hasBeenSimplified = true;
					}
				}
			}
		}

		return simplified;
	}

	private Sentence simplifyAllInSentence(Sentence sentence) {

		Tree parseTree = ManageExternalTools.parse(sentence.getPosTagged());
		sentence.setParseTree(parseTree);
		Sentence simplified = new Sentence(sentence);

		simplified.setParseTree(sentence.getParseTree().deepCopy());
		Tree original = simplified.getParseTree();

		Collection<Tree> trees2remove = ManageTrees.removablePassages(original);

		Tree simplifiedTree = ManageTrees.removeTrees(original, trees2remove);
		simplified = sentence.buildSubSentence(simplifiedTree);

		if (simplified == null) {
			return sentence;
		}

		return simplified;
	}

	public void simplifyByRemovingAll() {

		LinkedList<Sentence> newSentences = new LinkedList<Sentence>();
		Sentence finalSentence = null;

		for (Sentence sentence : summary.getSentences()) {

			if (sentence != null) {
				finalSentence = simplifyAllInSentence(sentence);

				if (finalSentence != null)
					newSentences.add(finalSentence);
			}
		}

		this.postProcessedText = new PostProcessedText(newSentences);
	}

	/**
	 * <p>
	 * Compresses the given collection of sentences.
	 * </p>
	 * <ul>
	 * <li>Obtains the main phrase of the sentence (Tree format: S NP VP).</li>
	 * <li>Compresses the NP tree by removing its extraneous information.</li>
	 * <li>Compresses the VP tree by removing its extraneous information.</li>
	 * </ul>
	 * 
	 * @param sentences
	 *            the sentences to be compressed.
	 * @return a collection of compressed sentences.
	 */
	private Collection<Sentence> simplifySentences(
			Collection<Sentence> sentences) {

		Collection<Sentence> newSentences = new LinkedList<Sentence>();

		for (Sentence sentence : sentences) {

			Sentence newSentence = simplifySentence(sentence);
			newSentences.add(newSentence);
		}

		return newSentences;
	}

	public void initialize() {
		annotatedConnectives = Preferences.loadAnnotatedConnectives();
	}

	/**
	 * <p>
	 * Inserts discourse connectors between sentences.
	 * </p>
	 */

	public Collection<Sentence> insertConnectives(Collection<Sentence> sentences) {

		initialize();

		List<Connective> connectives = Utils
				.convertCollection2List(annotatedConnectives.values());
		LinkedList<Sentence> orderedSentences = (LinkedList<Sentence>) Utils
				.removeAllConnectives(connectives, sentences), newSentences = new LinkedList<Sentence>();

		for (int i = 0; i < orderedSentences.size(); i++) {
			boolean added = false;
			Sentence second = orderedSentences.get(i);

			if (i == 0)
				newSentences.addLast(second);
			else {

				if (second.isTitle())
					newSentences.addLast(second);
				else {
					if (newSentences.size() > 1) {
						Sentence first = newSentences.removeLast();
						Sentence previous = first;

						if (!first.isTitle()) {

							ManageClassification mc = new ManageClassification(
									previous.getPosTagged(),
									second.getPosTagged());
							boolean areRelated = mc.haveRelation();

							if (areRelated) {

								String subtype = mc.classifyRelation();

								if (mc.isInverted()) {

									Sentence modifiedSentence = previous;
									// Se a relação fôr invertida, é como se não
									// tivesse relação...
									// portanto não insere nada.
									if (!previous.hasConnective())
										modifiedSentence = insertConnective(
												subtype, previous);

									if (first != previous)
										newSentences.add(first);

									newSentences.add(second);
									newSentences.add(modifiedSentence);

								} else {
									Sentence modifiedSentence = insertConnective(
											subtype, second);

									if (first != previous)
										newSentences.add(first);

									newSentences.add(previous);
									newSentences.add(modifiedSentence);

								}
							} else {
								newSentences.add(previous);

								if (first != previous)
									newSentences.add(first);

								newSentences.add(second);

							}
						} else {
							newSentences.add(first);
							newSentences.add(second);
						}
					} else
						newSentences.add(second);
				}
			}
		}

		return newSentences;
	}

	private Sentence insertConnective(String subtype, Sentence sentence) {

		Sentence modified = new Sentence(sentence);
		String classType = Preferences.getClassTypeForSubtype(subtype);

		// Obter os conectores possíveis.
		List<Connective> candidates = annotatedConnectives.get(classType);

		if (subtype != null)
			candidates = Utils.filterBySubtype(candidates, subtype);

		Connective connective = selectConnective(candidates,
				sentence.getPosTagged(), 0);

		String sentenceStr = "";
		// Inserir na frase
		if (connective == null)
			sentenceStr = modified.getSentence();
		else {
			sentenceStr = Regex.capitalizeFirst(connective.toPrint()) + ", "
					+ Regex.lowerFirst(sentence.getSentence());
			modified.insertConnective(sentenceStr, connective.getWords());
		}

		return modified;
	}

	private Connective selectConnective(List<Connective> candidates,
			String sentence, int number) {

		if (candidates.size() == insertedConnectives.size())
			insertedConnectives = new LinkedList<Connective>();

		LinkedList<Connective> connectives = new LinkedList<Connective>(
				candidates);

		Random randomGenerator = new Random();
		int randomConnective = randomGenerator.nextInt(candidates.size());

		Connective finalConnective = connectives.get(randomConnective);

		if (!insertedConnectives.contains(finalConnective)
				&& finalConnective.matchesRule(sentence))
			insertedConnectives.add(finalConnective);
		else {

			number++;

			if (number > Preferences.MAX_CONNECTIVES) {
				System.out.println("ERROR: Returning connective as null...");
				return null;
			}

			finalConnective = selectConnective(candidates, sentence, number);
		}

		return finalConnective;
	}

	/**
	 * <p>
	 * Arranges the text in meaningful paragraphs.
	 * </p>
	 */

	private Collection<Sentence> arrangeSentencesInParagraphs(
			Collection<Sentence> sentences) {

		LinkedList<Sentence> finalSummarySentences = new LinkedList<Sentence>();
		HashMap<Word, SentenceCluster> keywordClusters = new HashMap<Word, SentenceCluster>();
		LinkedList<Sentence> currentSentences = new LinkedList<Sentence>(
				sentences);

		Word nullKeyword = new SingleWord(" ", " ", " ", " ");

		for (Sentence sentence : currentSentences) {
			Word keyword = sentence.getKeywordsKey();

			if (keyword == null)
				keyword = nullKeyword;

			SentenceCluster cluster = keywordClusters.get(keyword);

			if (cluster == null) {
				LinkedList<Sentence> values = new LinkedList<Sentence>();
				values.add(sentence);
				keywordClusters.put(keyword, new SentenceCluster(0, sentence,
						values));
			} else
				cluster.addValue(sentence);
		}

		LinkedList<Paragraph> paragraphs = new LinkedList<Paragraph>();
		LinkedList<Sentence> aux = new LinkedList<Sentence>();

		Collections.sort(currentSentences,
				Preferences.COMPARE_SENTENCES_IN_PARAGRAPH);

		for (Sentence sentence : currentSentences) {

			Word keyword = sentence.getKeywordsKey();

			if (keyword == null)
				keyword = nullKeyword;

			Sentence titleSentence = Utils.buildNewLineSentence();

			if (keywordClusters.containsKey(keyword)) {
				aux.add(titleSentence);

				SentenceCluster cluster = keywordClusters.remove(keyword);
				LinkedList<Sentence> values = new LinkedList<Sentence>(
						cluster.getValues());
				paragraphs.add(new Paragraph(keyword, titleSentence, values));
				aux.addAll(values);
			}
		}

		return finalSummarySentences;
	}

	/** Getters & Setters **/
	public PostProcessedText getPostProcessedText() {
		return postProcessedText;
	}

	public void setSimplifiedText(PostProcessedText postProcessedText) {
		this.postProcessedText = postProcessedText;
	}

	public void setSimplifiedTextFromSummary(Summary summary) {
		this.postProcessedText = new PostProcessedText(summary);
	}
}
