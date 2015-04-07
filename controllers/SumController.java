package controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import aux.SentenceCluster;

import options.SumOptions;

import preferences.Preferences;
import preferences.Utils;

import core.Document;
import core.NamedEntity;
import core.Sentence;
import core.SingleWord;
import core.Word;
import summarization.Summary;

/**
 * <p>
 * This class manages the summarization process.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class SumController extends Controller {

	/**
	 * <p>
	 * Similarity clusters.
	 * </p>
	 * .
	 **/
	private HashMap<Integer, SentenceCluster> similarityClusters;
	/**
	 * <p>
	 * Keyword clusters.
	 * </p>
	 * .
	 **/
	private HashMap<String, SentenceCluster> keywordClusters;
	/**
	 * <p>
	 * Sentences that have not been added to any cluster.
	 * </p>
	 **/
	private Collection<Sentence> nullSentences;
	/**
	 * <p>
	 * Sentences that were ignored because have similar sentences in the
	 * collection.
	 * </p>
	 **/
	private Collection<Sentence> similarSentences;
	/**
	 * <p>
	 * Final summary maximum total words.
	 * </p>
	 **/
	private int maximumCompression;
	/**
	 * <p>
	 * Simplification controller - to be used in the simplification phase.
	 * </p>
	 **/
	private PostProcessingController ppc;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 */
	public SumController() {
		nullSentences = new LinkedList<Sentence>();
		similarSentences = new LinkedList<Sentence>();
		demoLog = "++++++++++++++++++++++ DEMO LOG +++++++++++++++++++++++++++++++++\n\n";
	}

	public Summary buildSummary() {

		long start = Utils.startCountingTime();

		// Analysis summarization phase
		preprocessing();

		identification();
		// Transformation summarization phase
		mapping();

		// Synthesis summarization phase
		reduction();

		presentation();

		Utils.endCountingTime(start);

		return summary;
	}

	public Summary buildNonSimplifiedSummary() {
		Preferences.SUMMARY_TYPE = Preferences.FINAL_SUMMARY_TYPE.NONSIMPLIFIED;
		return buildSummary();
	}

	public Summary buildNonPostProcessedSummary() {
		Preferences.SUMMARY_TYPE = Preferences.FINAL_SUMMARY_TYPE.NONSIMPLIFIED;
		return buildSummary();
	}

	public Summary postProcessText() {
		Preferences.SUMMARY_TYPE = Preferences.FINAL_SUMMARY_TYPE.ONLYPOSTPROCESSEDNOSUMMARY;
		return buildPostProcessedText();
	}

	public Summary buildPostProcessedText() {

		preprocessing();
		identification();

		LinkedList<Sentence> sentences = new LinkedList<Sentence>();

		for (Document document : documents)
			sentences.addAll((LinkedList<Sentence>) document.getText()
					.getSentences());

		this.summary = new Summary(sentences);

		mapping();

		ppc = new PostProcessingController(this.documents, this.summary,
				this.keywords);
		ppc.setDemoLog(this.demoLog);
		ppc.setCompression(maximumCompression);
		ppc.postprocessText();
		this.postProcessedText = ppc.getPostProcessedText();
		this.summary = new Summary(ppc.getPostProcessedText());
		this.demoLog = ppc.getDemoLog();

		presentation();

		return summary;
	}

	public Summary buidRandomSummary() {

		// Analysis of the collection of documents
		preprocessing();
		identification();

		randomizedSummary();

		return summary;
	}

	/**
	 * <p>
	 * Generates a randomized summary.
	 * </p>
	 */
	private void randomizedSummary() {

		LinkedList<Sentence> sentences = new LinkedList<Sentence>();
		LinkedList<Sentence> summarySentences = new LinkedList<Sentence>();

		for (Document document : documents)
			sentences.addAll((LinkedList<Sentence>) document.getText()
					.getSentences());

		Random randomGenerator = new Random();
		int totalWords = 0, totalSentences = sentences.size();

		while (totalWords < this.maximumCompression) {

			int randomInt = randomGenerator.nextInt(totalSentences);
			System.out.print("...Randomizing..." + randomInt);
			Sentence current = sentences.get(randomInt);
			summarySentences.add(current);

			totalWords += current.getTotalWords();
		}

		System.out.println("\nAfter random: " + totalWords + " Maximum: "
				+ this.maximumCompression);

		summary = new Summary(summarySentences);
	}

	/**
	 * <p>
	 * Clears the controller.
	 * </p>
	 */
	public void clear() {
		similarityClusters = null;
		keywordClusters = null;
		nullSentences = null;
		similarSentences = null;
		documents = null;
		keywords = null;
		summary = null;
		postProcessedText = null;

		System.gc();
	}

	/**
	 * <p>
	 * Generates headlines for each document.
	 * </p>
	 * 
	 * @return a summary that represents the headline.
	 */
	public Summary generateHeadlines() {
		return buildSummary();
	}

	/**
	 * <p>
	 * Pre-processes each document:
	 * <ul>
	 * <li>Cleans the text from eventual noise.</li>
	 * </ul>
	 * </p>
	 */
	private void preprocessing() {
		System.out.println("[Preprocessing...]");
		long start = Utils.startCountingTime();
		submitDocuments();
		Utils.endCountingTime(start);
	}

	/**
	 * <p>
	 * Identifies the text parts:
	 * <ul>
	 * <li>Annotates the text with:</li>
	 * <ul>
	 * <li>POS tags</li>
	 * <li>Named entities</li>
	 * <li>Parse tree</li>
	 * <li>Dependency tree</li>
	 * </ul>
	 * <li>Define sentence and document numbers.</li>
	 * </ul>
	 * </p>
	 * 
	 */
	private void identification() {
		System.out.println("[Identification...]");
		long start = Utils.startCountingTime();
		manageDocuments();
		this.setMaximumWords();
		Utils.endCountingTime(start);
		System.out.println("\t\t# documents: " + this.totalDocuments);
		System.out.println("\t\t# sentences: " + this.totalSentences);
		System.out.println("\t\t# words: " + this.totalWords);
		System.out.println("\t\t# maximum words: " + this.maximumCompression);
	}

	/**
	 * <p>
	 * Maps and filters sentences:
	 * <ul>
	 * <li>Computes sentences scores.</li>
	 * <li>Extracts document collection keywords.</li>
	 * <li>Clusters sentences by similarity.</li>
	 * <li>Clusters sentences by keywords.</li>
	 * <li>Filters sentences by threshold.</li>
	 * </ul>
	 * </p>
	 */
	private void mapping() {
		System.out.println("[Mapping...]");

		long start = Utils.startCountingTime();

		System.out.println("\tComputing global properties...");
		computeGlobalProperties();
		Utils.endCountingTime(start);
		Preferences.setMaxKeywords(this.maximumCompression); // TODO to try
																// this...

		// ---------------------------------------------------
		// Demo log

		demoLog += "---------------- Annotation (total documents "
				+ this.totalDocuments + "; total sentences "
				+ this.totalSentences + "; total words " + this.totalWords
				+ ")\n\n";

		for (Document document : documents) {
			for (Sentence sentence : document.getText().getSentences()) {
				demoLog += sentence.getSentence() + "\t"
						+ Utils.formatNumber(sentence.getScore()) + "\n";
				demoLog += "\t" + sentence.getPosTagged() + "\n\n";
			}
		}

		demoLog += "\n\n\n\n\n-------------------------------------------------------------\n";

		// ---------------------------------------------------

		if (options instanceof SumOptions) {
			SumOptions sumOptions = (SumOptions) options;
			if (!sumOptions.hasKeywords()) {
				System.out.println("\tComputing global keywords...");
				start = Utils.startCountingTime();
				computeGlobalKeywords();
				Utils.endCountingTime(start);
			} else
				this.keywords = sumOptions.getKeywords();
		}

		// ------- Selects all the sentences to figure in the summary -----
		LinkedList<Sentence> allSentences = new LinkedList<Sentence>();

		// Gets all the sentences.
		for (Document document : documents)
			allSentences.addAll(document.getText().getSentences());

		this.summary = new Summary(allSentences);
		// ------- ------- ------- ------- ------- ------- ------- -------

		System.out.println("\tClustering sentences by similarity...");
		start = Utils.startCountingTime();
		clusterBySimilarity();
		Utils.endCountingTime(start);

		// ---------------------------------------------------
		// Demo log
		demoLog += "---------------- Keyword Clustering\n";
		demoLog += "\n\n---- Keywords:\n";

		for (Word keyword : keywords)
			demoLog += "\t\t" + keyword.representation() + "\n";

		demoLog += "\n";

		// ---------------------------------------------------

		allSentences = new LinkedList<Sentence>(this.summary.getSentences());
		Collections.sort(allSentences,
				Preferences.COMPARE_SENTENCE_COMPLETE_SCORE);

		for (Sentence sentence : allSentences)
			System.out.println((sentence.getDocumentId() + 1) + "\t"
					+ sentence.getAbsolutePosition() + "\t"
					+ sentence.getSentence() + "\t" + sentence.completeScore());

		updateKeywordsScore(this.summary.getSentences());
		updateDocumentProperties();

		allSentences = new LinkedList<Sentence>(this.summary.getSentences());

		Collections.sort(allSentences,
				Preferences.COMPARE_SENTENCE_COMPLETE_SCORE);

		System.out.println("\tClustering sentences by keyword...");
		start = Utils.startCountingTime();
		clusterByKeywords();
		Utils.endCountingTime(start);

		Collections.sort(allSentences,
				Preferences.COMPARE_SENTENCE_COMPLETE_SCORE);

		System.out.println("\t\t# summary sentences: "
				+ this.summary.getTotalSentences());
		System.out.println("\t\t# words in summary sentences: "
				+ this.summary.getTotalWords());
	}

	/**
	 * <p>
	 * Reduces sentence content, by performing text simplification. Arranges the
	 * summary-to-be in meaningful paragraphs.
	 * </p>
	 */
	private void reduction() {
		System.out.println("[Reduction...]");
		long start = Utils.startCountingTime();

		System.out.println("\tSorting sentences...");
		sorting2compress();

		if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.NONSIMPLIFIED) {
			System.out.println("\tCompressing...");
			compress();
		} else {

			System.out.println("\tPost-processing text...");

			System.out.println("--[STATISTICS] " + Preferences.OUTPUT_FILE);

			ppc = new PostProcessingController(this.documents, this.summary,
					this.keywords);
			ppc.setDemoLog(this.demoLog);
			ppc.setCompression(maximumCompression);
			ppc.postprocessText();
			this.postProcessedText = ppc.getPostProcessedText();
			this.summary = new Summary(ppc.getPostProcessedText());
			this.demoLog = ppc.getDemoLog();
		}

		Utils.endCountingTime(start);

		System.out.println("\t\t# summary sentences: "
				+ this.summary.getTotalSentences());
		System.out.println("\t\t# words in summary sentences: "
				+ this.summary.getTotalWords());
	}

	/**
	 * <p>
	 * Formats the presentation of the final summary:
	 * </p>
	 * <ul>
	 * <li>Arranges text in paragraphs.</li>
	 * </ul>
	 */
	private void presentation() {

		System.out.println("[Format summary...]");
		this.summary = Utils.correctFinalPunctuation(this.summary);
		this.summary = Utils.correctCapitals(this.summary);

		// ---------------------------------------------------
		// Demo log
		demoLog += "---------------- Final summary (sentences: "
				+ this.summary.getSentences().size()
				+ "; words: "
				+ Utils.countTotalWordsInCollection(this.summary.getSentences())
				+ "; maximum words: " + this.maximumCompression + "):\n";

		for (Sentence sentence : this.summary.getSentences())
			demoLog += "\t\t" + sentence.getSentence() + "\n";

		// ---------------------------------------------------

		System.out.println("\t\t# summary sentences: "
				+ this.summary.getTotalSentences());
		System.out.println("\t\t# words in summary sentences: "
				+ this.summary.getTotalWords());
	}

	/**
	 * <p>
	 * Cluster all the sentences based on its similarity.
	 * </p>
	 * <p>
	 * Filters similar sentences.
	 * </p>
	 * 
	 * @return the collection of sentences clustered by similarity.
	 */

	private void clusterBySimilarity() {

		LinkedList<Sentence> allSentences = new LinkedList<Sentence>(
				this.summary.getSentences());

		similarityClusters = new HashMap<Integer, SentenceCluster>();
		int numberOfSentences = 0, key = 0, clustersNumber = similarityClusters
				.size();

		for (Sentence firstSentence : allSentences) {
			// 1. Computes the similarity between the current sentence and all
			// sentences
			// and saves it in an array of similarities.
			// 2. Sorts the similarities array and retrieves the first sentence,
			// the
			// one with which the current sentence has the highest similarity.
			// 3. If this similarity is above the threshold, adds this sentence
			// to
			// this sentence similarity cluster.
			// 4. If this similarity is below the threshold, adds the first
			// sentence
			// to a new cluster.

			Collection<SentenceCluster> currentClusters = similarityClusters
					.values();

			boolean found = false;

			for (SentenceCluster cluster : currentClusters) {
				if (firstSentence.equals(cluster.getCentroid())) {
					cluster.addValue(firstSentence);
					found = true;
				} else {
					Collection<Sentence> values = (Collection<Sentence>) cluster
							.getValues();
					LinkedList<Double> similarities = new LinkedList<Double>();

					// Gets the higher similarity value for this cluster.
					for (Sentence sentence : values) {
						double similarity = Utils
								.computeSentence2SentenceSimilarity(
										firstSentence, sentence);
						similarities.add(new Double(similarity));
					}

					Collections.sort(similarities);

					double higherSim = similarities.getLast().doubleValue();

					// If the similarity value is above the similarity
					// threshold,
					// it means that the current sentence must be in this
					// cluster.
					if (higherSim >= Preferences.SENTENCE_SIMILIARITY_THRESHOLD) {

						// If the current sentence score is higher than the
						// sentence centroid
						// score, the current sentence is set as the cluster
						// centroid.

						if (((Sentence) cluster.getCentroid()).getScore() < firstSentence
								.getScore()) {

							cluster.setCentroid(firstSentence);
							cluster.setValue(higherSim);
						}

						// The sentence is added to the cluster.
						firstSentence.setSimilarityKey(key);
						cluster.addValue(firstSentence);
						found = true;
					}
				}

				if (found)
					break;
			}

			// If the similarity cluster is not found, creates a new cluster.
			if (!found) {
				LinkedList<Sentence> values = new LinkedList<Sentence>();
				firstSentence.setSimilarityKey(key);
				values.addFirst(firstSentence);
				SentenceCluster newCluster = new SentenceCluster(0,
						firstSentence, values);
				similarityClusters.put(new Integer(key), newCluster);
				clustersNumber++;
				key++;
			}

			numberOfSentences++;

		}

		// ---------------------------------------------------
		// Demo log

		demoLog += "---------------- Similarity Clustering\n";
		// ---------------------------------------------------

		// Clusters the sentences based on the document set keywords.
		// While there are sentences that weren't clustered, the keywords are
		// re-computed based on the sentences already clustered, and the
		// unclustered
		// sentences are then reclustered, until there are no changes in the
		// unclustered
		// set of sentences.
		Collection<Sentence> sentences = new LinkedList<Sentence>();

		double totalClusters = similarityClusters.size();
		Set<Map.Entry<Integer, SentenceCluster>> clustersSet = similarityClusters
				.entrySet();

		for (Map.Entry<Integer, SentenceCluster> entry : clustersSet) {

			SentenceCluster cluster = entry.getValue();
			LinkedList<Sentence> values = new LinkedList<Sentence>(
					cluster.getValues());

			// Sets the current cluster value as the number of values normalized
			// by the total number of clusters.
			// This value denotes the proportion of the size of the cluster. The
			// higher the number of values
			// the cluster contains, the more important this cluster is.
			cluster.setValue(values.size() / totalClusters);

			double valuesScore = 0, avgPosition = 0;

			for (Sentence sentence : values) {
				valuesScore += sentence.getScore();
				avgPosition += sentence.getAbsolutePosition();
			}

			avgPosition = avgPosition / values.size();
			valuesScore = valuesScore / values.size();

			System.out.print("\n\n\t\t[SumController] cluster#"
					+ entry.getKey() + "\tAvgPositions: " + avgPosition); // DEBUG
			demoLog += "---- Cluster#" + entry.getKey(); // DEMO
			demoLog += "\n\t\tCentroid:"
					+ ((Sentence) cluster.getCentroid()).getSentence(); // DEMO
			System.out.println("\t" + values.size() + "\t\tCentroid:"
					+ ((Sentence) cluster.getCentroid()).getDocumentId() + " "
					+ ((Sentence) cluster.getCentroid()).getSentence()); // DEBUG

			for (Sentence sentence : values) {
				// Sets for each sentence of the cluster its cluster score which
				// is the score of the cluster centroid sentence.

				double similarityClusterScore = (((Sentence) cluster
						.getCentroid()).getScore() + valuesScore) / 2;

				sentence.setSimilarityClusterScore(similarityClusterScore);
				sentence.setClustersSize(cluster.getValues().size());
				sentence.setSimClusterSize(cluster.getValues().size());
				sentence.setAvgPositionSimCluster(avgPosition);

				demoLog += "\n\t\t" + sentence.getSentence() + "\n";
			}

			demoLog += "\n";
		}

		for (SentenceCluster cluster : this.similarityClusters.values()) {
			Sentence centroid = new Sentence((Sentence) cluster.getCentroid());
			centroid.updateExtraScore(Preferences.SENTENCE_EXTRA_SCORE_DEFAULT_VALUE);
			sentences.add(centroid);
		}

		// ---------------------------------------------------
		// Demo log
		demoLog += "\n\n---- Sentences kept (sentences: " + sentences.size()
				+ "; words: " + Utils.countTotalWordsInCollection(sentences)
				+ "):\n";

		for (Sentence sentence : sentences)
			demoLog += "\t" + sentence.getSentence() + "\n";

		// ---------------------------------------------------

		this.summary = new Summary(sentences);

		// Keeping the sentences ignored in the similarity clustering.
		Set<Map.Entry<Integer, SentenceCluster>> similarities = similarityClusters
				.entrySet();

		this.similarSentences = new LinkedList<Sentence>();
		for (Entry<Integer, SentenceCluster> entry : similarities) {
			for (Object obj : entry.getValue().getValues()) {
				Sentence sentence = (Sentence) obj;
				if (!sentence.sameSentence((Sentence) entry.getValue()
						.getCentroid()))
					this.similarSentences.add(sentence);
			}
		}

		// ---------------------------------------------------
		// Demo log
		demoLog += "\n\n---- Sentences Ignored (sentences: "
				+ this.similarSentences.size() + "; words: "
				+ Utils.countTotalWordsInCollection(this.similarSentences)
				+ "):\n";

		for (Sentence sentence : this.similarSentences)
			demoLog += "\t" + sentence.getSentence() + "\n";

		demoLog += "\n\n\n\n\n-------------------------------------------------------------\n";
		// ---------------------------------------------------
	}

	/**
	 * <p>
	 * Clusters all the sentences based on the (document or query) keywords.
	 * </p>
	 * 
	 * @return the collection of clusters.
	 */
	private void clusterByKeywords() {

		LinkedList<Sentence> sentences = new LinkedList<Sentence>(
				this.summary.getSentences());

		this.keywordClusters = new HashMap<String, SentenceCluster>();
		this.nullSentences = null;

		int previous = 0;

		while (nullSentences == null || nullSentences.size() != previous) {

			// If there are no unclustered sentences, we'll use the document set
			// keywords
			// to form the clusters and add the all the sentences to cluster.
			if (nullSentences == null) {
				nullSentences = new LinkedList<Sentence>(sentences);
				previous = nullSentences.size();
			}

			// Adds the keywords to the cluster set.
			for (Word word : keywords) {

				String key = word.representation();
				SentenceCluster cluster = keywordClusters.get(key);

				if (cluster == null)
					keywordClusters.put(key, new SentenceCluster());
			}

			// Control variable:
			// If in the previous iteration of the cycle the set of unclustered
			// sentences
			// maintains the same size, it means that the clusters won't be
			// changed, that
			// is, this set of sentences cannot be added to the clusters with
			// the current
			// keywords.
			previous = nullSentences.size();

			// Adds the sentences to the clusters based on the keywords.
			nullSentences = addSentences2Clusters(nullSentences,
					keywordClusters, keywords);
		}

		Set<Map.Entry<String, SentenceCluster>> set = keywordClusters
				.entrySet();
		LinkedList<Sentence> allSentences = new LinkedList<Sentence>();
		double totalClusters = keywordClusters.size();

		// ---------------------------------------------------
		// Demo log
		demoLog += "\n\n---- Clustering:\n";

		for (Entry<String, SentenceCluster> entry : set) {

			demoLog += "\t" + entry.getKey() + "\n";

			SentenceCluster cluster = entry.getValue();
			// Score the final centroid as being more important...
			((Sentence) cluster.getCentroid())
					.updateExtraScore(Preferences.SENTENCE_EXTRA_SCORE_DEFAULT_VALUE);

			LinkedList<Sentence> values = new LinkedList<Sentence>(
					cluster.getValues());

			// Sets the current cluster value as the number of values normalized
			// by the total number of clusters.
			// This value denotes the proportion of the size of the cluster. The
			// higher the number of values
			// the cluster contains, the more important this cluster is.
			cluster.setValue(values.size() / totalClusters);

			// Computes the average score of the sentences in this cluster
			double valuesScore = 0;

			for (Sentence sentence : values)
				valuesScore += sentence.getScore();

			valuesScore = valuesScore / values.size();

			for (Sentence sentence : values) {
				// Sets for each sentence of the cluster its cluster score which
				// is the score of the cluster centroid sentence.
				// Gets the previous value. Adds this value to the current
				// cluster value and divides by 2
				// (since we perform 2 clustering methods).
				double newClusterScore = cluster.getValues().size();

				// The final keyword cluster score is an average of the score
				// of:
				double finalKeywordClusterScore = (sentence
						.getKeywordClusterScore() // - the keyword that
													// represents the cluster
						+ ((Sentence) cluster.getCentroid()).getScore() // - the
																		// score
																		// of
																		// the
																		// cluster
																		// centroid
				+ valuesScore // - the average of the scores of the sentences in
								// this cluster
				) / 3;

				sentence.setKeywordClusterScore(finalKeywordClusterScore);
				int numberKeywords = Utils.countWordsInSentence(keywords,
						sentence);
				sentence.updateExtraScore(numberKeywords);
				sentence.setNumberOfKeywords(numberKeywords);

				demoLog += "\t\t" + sentence.getSentence() + "\n";
			}
			allSentences.addAll(entry.getValue().getValues());
			demoLog += "\n";

		}

		this.summary = new Summary(allSentences);

		// ---------------------------------------------------
		// Demo log
		demoLog += "\n\n---- Null cluster (sentences: "
				+ this.nullSentences.size() + "; words: "
				+ Utils.countTotalWordsInCollection(this.nullSentences)
				+ "):\n";

		for (Sentence sentence : this.nullSentences)
			demoLog += "\t" + sentence.getSentence() + "\n";

		demoLog += "\n\n---- Remaining sentences (sentences: "
				+ this.summary.getSentences().size()
				+ "; words: "
				+ Utils.countTotalWordsInCollection(this.summary.getSentences())
				+ "):\n";

		for (Sentence sentence : this.summary.getSentences())
			demoLog += "\t" + sentence.getSentence() + "\n";

		demoLog += "\n\n\n\n\n-------------------------------------------------------------\n";
		// ---------------------------------------------------
	}

	/**
	 * <p>
	 * Add sentences to the predefined clusters, based on maximum keyword
	 * occurrences.
	 * </p>
	 * 
	 * @param sentences
	 *            the sentences to eventually be added.
	 * @param clusters
	 *            the clusters already defined.
	 * @param keywords
	 *            the already clustered sentence keywords.
	 * @return the collection of sentences that were not added to the clusters.
	 */
	private Collection<Sentence> addSentences2Clusters(
			Collection<Sentence> sentences,
			HashMap<String, SentenceCluster> clusters, Collection<Word> keywords) {

		Collection<Sentence> nullSentences = new LinkedList<Sentence>();

		// Counts the occurrences of each keyword in the sentence.
		// The sentence is added to the cluster where its keyword is most
		// frequent in the sentence.
		for (Sentence currentSentence : sentences) {
			int maxOccurrences = -1, maxSize = -1, maxKeywords = -1;
			double maxScore = -1;
			Word keyword = null;

			Map<Word, Integer> keywordOccurrences = new TreeMap<Word, Integer>();

			for (Word word : keywords) {

				// Counts the occurrences of this keyword in the current
				// sentence.
				int occurrences = currentSentence
						.computeKeywordOccurrences(word);
				keywordOccurrences.put(word, new Integer(occurrences));
			}

			keywordOccurrences = Preferences.sortByValue(keywordOccurrences);

			Iterator entries = keywordOccurrences.entrySet().iterator();

			double higherSim = -1;

			while (entries.hasNext()) {
				Entry<Word, Integer> thisEntry = (Entry<Word, Integer>) entries
						.next();
				Word word = thisEntry.getKey();
				int value = thisEntry.getValue().intValue();

				if (value != 0) {
					if (value > maxOccurrences) {
						maxOccurrences = value;

						if (word instanceof SingleWord)
							keyword = (SingleWord) word;
						else
							keyword = (NamedEntity) word;
					} else if (value == maxOccurrences) {

						int numberKeywords = Utils.countWordsInSentence(
								this.keywords, currentSentence);

						if (numberKeywords > maxKeywords) {
							maxKeywords = numberKeywords;

							if (word instanceof SingleWord)
								keyword = (SingleWord) word;
							else
								keyword = (NamedEntity) word;
						} else {

							SentenceCluster cluster = clusters.get(word
									.representation());

							if (cluster.size() > maxSize) {
								maxSize = cluster.size();

								if (word instanceof SingleWord)
									keyword = (SingleWord) word;
								else
									keyword = (NamedEntity) word;

							} else {

								if (word.getScore() > maxScore) {
									maxScore = word.getScore();

									if (word instanceof SingleWord)
										keyword = (SingleWord) word;
									else
										keyword = (NamedEntity) word;

								}
							}
						}
					}
				}
			}

			// Heurística de empate: a frase é adicionada ao cluster da primeira
			// keyword com maior ocorrência.
			// Eventualmente, a heurística de empate pode ser outra:
			// - ser escolhida aleatoriamente de entre o conjunto de possíveis
			// keywords.
			// - definir outra heurística que resolva o caso de empate.
			if (keyword != null) {
				// Adds the current sentence to the keyword cluster, which
				// keyword is more
				// frequent in the sentence.
				String key = keyword.representation();
				SentenceCluster cluster = clusters.get(key);
				currentSentence.setKeywordClusterScore(keyword.getScore());
				currentSentence.setKeywordsKey(keyword);
				cluster.addValue(currentSentence);
				// If its frequency is higher than the centroid's frequency
				// value,
				// updates the cluster centroid with the current sentence.
				if (maxOccurrences > cluster.getValue()) {
					cluster.setValue(maxOccurrences);
					cluster.setCentroid(currentSentence);
				}

				clusters.put(key, cluster);
			}
			// If the sentence does not have any keyword, it is added to the
			// group of null cluster sentences.
			else {
				if (!nullSentences.contains(currentSentence)) {
					nullSentences.add(currentSentence);
					// // This is a sentence to be ignored, so its score will be
					// decreased.
					currentSentence
							.updateExtraScore(-Preferences.SENTENCE_EXTRA_SCORE_DEFAULT_VALUE); // PARA
																								// MUDAR
				}
			}
		}

		return nullSentences;
	}

	/**
	 * <p>
	 * Sort the sentences to be compressed.
	 * </p>
	 */
	private void sorting2compress() {

		// Updates all sentences properties to ensure that all variables that
		// are part of the
		// complete score are updated.

		LinkedList<Sentence> allSentences = new LinkedList<Sentence>(
				this.summary.getSentences());

		Collections.sort(allSentences,
				Preferences.COMPARE_SENTENCE_COMPLETE_SCORE);

		LinkedList<Sentence> tmp = new LinkedList<Sentence>(this.nullSentences);
		Collections.sort(tmp, Preferences.COMPARE_SENTENCE_COMPLETE_SCORE);
		this.nullSentences = new LinkedList<Sentence>(tmp);

		tmp = new LinkedList<Sentence>(this.similarSentences);
		Collections.sort(tmp, Preferences.COMPARE_SENTENCE_COMPLETE_SCORE);
		this.similarSentences = new LinkedList<Sentence>(tmp);

		this.summary = new Summary(allSentences);
	}

	/**
	 * <p>
	 * Removes the sentences until the previously defined compression rate.
	 * </p>
	 */
	private void compress() {

		LinkedList<Sentence> newSentences = (LinkedList<Sentence>) Utils
				.filterSentencesByWordCompression(this.summary.getSentences(),
						this.maximumCompression);
		this.summary = new Summary(newSentences);
	}

	/**
	 * <p>
	 * Sort sentences to determine the final summary order.
	 * </p>
	 */
	private void sorting2review() {

		// Updates all sentences properties to ensure that all variables that
		// are part of the
		// complete score are updated.
		updateDocumentProperties();

		LinkedList<Sentence> allSentences = new LinkedList<Sentence>(
				this.summary.getSentences());

		// Sorts sentences by order in the document.
		Collections.sort(allSentences, Preferences.SENTENCES_TO_REVIEW);

		this.summary = new Summary(allSentences);
	}

	/**
	 * <p>
	 * Completes the summary with new sentences after the reduction phase.
	 * </p>
	 */
	private void completeSummary() {

		int totalSummaryWords = this.summary.getTotalWords(), currentSummaryWords = totalSummaryWords;

		if (totalSummaryWords < this.maximumCompression) {
			LinkedList<Sentence> newSummarySentences = new LinkedList<Sentence>();
			LinkedList<Sentence> listNullSentences = new LinkedList<Sentence>(
					this.nullSentences);
			LinkedList<Sentence> listSimilarSentences = new LinkedList<Sentence>(
					this.similarSentences);
			newSummarySentences.addAll(this.summary.getSentences());

			Collections.sort(listNullSentences,
					Preferences.COMPARE_SENTENCE_SCORE);
			Collections.sort(listSimilarSentences,
					Preferences.COMPARE_SENTENCE_SCORE);

			LinkedList<Sentence> sentences2be = new LinkedList<Sentence>();
			sentences2be.addAll(listNullSentences);
			sentences2be.addAll(listSimilarSentences);

			for (Sentence sentence : sentences2be) {

				newSummarySentences.add(sentence);
				currentSummaryWords += sentence.getTotalWords();
				if (currentSummaryWords >= this.maximumCompression)
					break;

			}

			this.summary = new Summary(newSummarySentences);
		}
	}

	/**
	 * <p>
	 * Sets the maximum number of summary words.
	 * </p>
	 */
	public void setMaximumWords() {
		double compression = ((SumOptions) options).getCompressionRate() != 0 ? ((SumOptions) options)
				.getCompressionRate() : Preferences.DEFAULT_COMPRESSION_RATE;
		this.maximumCompression = Math
				.round((int) (((double) this.totalWords) * ((double) compression)));
	}

	public Summary buildSimplificationBaselineSummary() {
		Preferences.SUMMARY_TYPE = Preferences.FINAL_SUMMARY_TYPE.SIMPLIFICATIONBASELINE;
		return buildSummary();
	}

	public Summary buildParagraphsBaselineSummary() {

		Preferences.SUMMARY_TYPE = Preferences.FINAL_SUMMARY_TYPE.PARAGRAPHBASELINE;
		return buildSummary();
	}

	public Summary buildConnectivesBaselineSummary() {

		Preferences.SUMMARY_TYPE = Preferences.FINAL_SUMMARY_TYPE.CONNECTIVESBASELINE;
		return buildSummary();
	}
}
