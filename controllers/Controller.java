package controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import options.Option;
import preferences.Preferences;
import preferences.Regex;
import preferences.Utils;
import simplification.PostProcessedText;
import summarization.Summary;
import core.Document;
import core.Sentence;
import core.SingleWord;
import core.Text;
import core.Word;
import edu.stanford.nlp.trees.Tree;
import external.concurrency.ExternalToolsThread;

/**
 * <p>
 * This class defines the methods common to the controllers, which manage the
 * different application processes.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public abstract class Controller {

	/**
	 * <p>
	 * Options submitted by the user.
	 * </p>
	 **/
	protected Option options;
	/**
	 * <p>
	 * Collection of documents submitted.
	 * </p>
	 **/
	protected Collection<Document> documents;
	/**
	 * <p>
	 * Keywords from the collection of documents.
	 * </p>
	 **/
	protected Collection<Word> keywords;
	/**
	 * <p>
	 * Parse trees for the collection of sentences.
	 * </p>
	 **/
	protected HashMap<String, Collection<Tree>> parseTrees;
	/**
	 * <p>
	 * Pre-summary (text without simplification)
	 * </p>
	 **/
	protected Summary summary;
	/**
	 * <p>
	 * Output simplified text
	 * </p>
	 **/
	protected PostProcessedText postProcessedText;
	/**
	 * <p>
	 * Total number of documents.
	 * </p>
	 **/
	protected int totalDocuments;
	/**
	 * <p>
	 * Total number of sentences.
	 * </p>
	 **/
	protected int totalSentences;
	/**
	 * <p>
	 * Total number of words.
	 * </p>
	 **/
	protected int totalWords;
	/**
	 * <p>
	 * Log of the demonstration.
	 * </p>
	 **/
	protected String demoLog;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Controller.
	 * </p>
	 */
	public Controller() {
		this.documents = new LinkedList<Document>();
		this.keywords = new LinkedList<Word>();
		this.totalDocuments = 0;
	}

	/**
	 * <p>
	 * Submit the documents to the controller.
	 * </p>
	 */
	public void submitDocuments() {

		IOController io = new IOController();
		documents = io.manageInputDocuments(options.getDocumentsLocation());
		parseTrees = io.getParseTrees();
		totalDocuments = documents.size();

		// Sets summarizer span
		if (documents.size() == 1)
			options.setSpan(Preferences.Span.SINGLE);
		else
			options.setSpan(Preferences.Span.MULTI);
	}

	/**
	 * <p>
	 * Submit the options to the controller.
	 * </p>
	 * 
	 * @param options
	 *            , the selected options.
	 */
	public void submitOptions(Option options) {
		this.options = options;
	}

	/**
	 * <p>
	 * Manages the pre-processing of the documents.
	 * </p>
	 * <p>
	 * Updates the document collection:
	 * </p>
	 * <ul>
	 * <li>Splits text in sentences.</li>
	 * <li>Splits sentences in words.</li>
	 * </ul>
	 */
	public void manageDocuments() {
		int threadNumber = 0;

		HashMap<Document, ExternalToolsThread> threads = new HashMap<Document, ExternalToolsThread>();

		// Updates the document collection
		for (Document document : documents) {
			System.out.println("\tProcessing file: " + document.getFilename());
			Text text = document.getText();
			// Creates a new thread to annotate the current document text.
			ExternalToolsThread thread = new ExternalToolsThread(text);
			threads.put(document, thread);
			thread.start();
			threadNumber++;

			// We'll only create threads until a maximum of
			// Preferences.MAXIMUM_THREADS.
			// When we get to this number of threads, we'll wait until all of
			// them end,
			// in order to continue the processing.
			if (threadNumber <= Preferences.MAXIMUM_THREADS) {
				manageThreadEnd(threads);
				threadNumber = 0;
				threads = new HashMap<Document, ExternalToolsThread>();
			}
		}

		// Checks if there are no more documents to process, but there are
		// threads still running.
		if (threadNumber > 0)
			manageThreadEnd(threads);

		this.parseTrees = null;
	}

	/**
	 * <p>
	 * Computes document properties:
	 * <ul>
	 * <li>Number of documents in the collection.</li>
	 * <li>Number of sentences in the collection.</li>
	 * <li>Number of words in the collection.</li>
	 * <li>Maximum sentence length.</li>
	 * <li>Minimum sentence length.</li>
	 * <li>Word score.</li>
	 * <li>Sentence score.</li>
	 * </ul>
	 * </p>
	 */
	protected void computeGlobalProperties() {
		for (Document document : documents) {
			Text text = document.getText();

			Collection<Word> totalWords = new LinkedList<Word>();
			Collection<Word> totalNamedEntities = new LinkedList<Word>();

			for (Sentence sentence : text.getSentences()) {
				int absolutePosition = sentence.getAbsolutePosition();
				double relativePosition = (double) ((double) text
						.getTotalSentences() / (double) absolutePosition);
				double relativePositionRatio = relativePosition
						/ (double) this.totalSentences;
				sentence.setRelativePosition(relativePosition);
				sentence.setRelativePositionRatio(relativePositionRatio);

				for (Word word : sentence.getWords())
					((SingleWord) word)
							.setSentenceRelativePosition(relativePosition);

				Collection<Word> namedEntities = Utils
						.extractEntities(sentence);
				sentence.setNamedEntities(namedEntities);

				totalWords.addAll(sentence.getWords());
				totalNamedEntities.addAll(sentence.getNamedEntities());
			}

			text.setWords(totalWords);
			text.setNamedEntities(totalNamedEntities);
			text.computeTextProperties();

			document.setWords(text.getTotalWords());
			document.setSentences(text.getTotalSentences());
		}

		documents = this.computeTermFrequency(documents);
		documents = this.computeTFIDF(documents);

		for (Document document : documents) {
			Text text = document.getText();
			Collection<Word> totalNamedEntities = new LinkedList<Word>();

			for (Sentence sentence : text.getSentences()) {
				sentence.setScore(sentence.computeScore());

				// Computes the sentence frequency.
				double frequency = sentence.computeFrequency();
				sentence.setTotalFrequency(frequency);
			}

			text.setNamedEntities(totalNamedEntities);
		}

	}

	protected Collection<Document> computeRelevance(
			Collection<Document> originals) {
		Collection<Document> documents = new LinkedList<Document>(originals);
		documents = this.computeTermFrequency(documents);
		documents = this.computeTFIDF(documents);

		for (Document document : documents) {
			Text text = document.getText();
			Collection<Word> totalNamedEntities = new LinkedList<Word>();

			for (Sentence sentence : text.getSentences()) {
				// Computes the sentence score.
				sentence.setScore(sentence.computeScore());

				// Computes the sentence frequency.
				double frequency = sentence.computeFrequency();
				sentence.setTotalFrequency(frequency);

				// Identifies the named entities in the sentence.
				Collection<Word> namedEntities = Utils
						.extractEntities(sentence);
				sentence.setNamedEntities(namedEntities);
				totalNamedEntities.addAll(sentence.getNamedEntities());

			}

			text.setNamedEntities(totalNamedEntities);
		}

		return documents;
	}

	protected Collection<Document> computeTermFrequency(
			Collection<Document> originals) {

		Collection<Document> documents = new LinkedList<Document>(originals);

		Collection<Word> allWords = new LinkedList<Word>();

		for (Document document : documents) {
			allWords.addAll(document.getText().getWords());
		}

		computeWordOccurrencesInDocument(allWords);

		return documents;
	}

	private void computeWordOccurrencesInDocument(Collection<Word> words) {

		int totalWordsInDocument = words.size();

		for (Word firstTerm : words) {

			int occurrences = firstTerm.getOccurrences();

			for (Word secondTerm : words) {
				if (!firstTerm.equals(secondTerm)) {
					if (firstTerm.represents(secondTerm)) {
						occurrences++;
					}
				}
			}

			firstTerm.setOccurrences(occurrences);

			firstTerm.setFrequency((double) occurrences
					/ (double) totalWordsInDocument);
		}
	}

	/**
	 * <p>
	 * Updates sentence words score with each word tf*idf.
	 * </p>
	 * 
	 * @post Must be followed by computeScore(...) to update the sentence total
	 *       score.
	 * 
	 * @param documents
	 *            the collection of documents.
	 * @return the collection of documents with the tfidf scores of the words
	 *         updated.
	 */
	public Collection<Document> computeTFIDF(Collection<Document> documents) {

		int totalDocuments = documents.size();
		LinkedList<Word> words = new LinkedList<Word>();
		LinkedList<Word> allWords = new LinkedList<Word>();

		for (Document document : documents) {
			words.addAll(document.getText().getWords());
		}

		for (Word word : words) {

			int freqInDocuments = Utils
					.numberOfTermInDocuments(documents, word);

			double tf = word.getFrequency();
			double aux = (double) totalDocuments
					/ ((double) 1 + freqInDocuments);

			double idf = Math.abs(Math.log10(aux));
			double tfidf = Math.abs(tf * idf);

			word.setNumberOfDocs(freqInDocuments);
			word.setTFIDF(tfidf);
			word.computeScore();
		}

		Collections.sort(words, Preferences.COMPARE_WORD_TFIDF);

		return documents;
	}

	/**
	 * <p>
	 * Updates the collection score considering the keywords.
	 * </p>
	 * <p>
	 * Updates each sentence number of keywords.
	 * </p>
	 */
	protected void updateDocumentProperties() {

		double textScore = 0;
		// Updates text score
		// Updates sentence number of keywords
		for (Document document : documents) {
			Text text = document.getText();
			Collection<Sentence> sentences = text.getSentences();

			// Computes the sentence score.
			for (Sentence sentence : sentences) {
				textScore += sentence.getScore();
				// Computes the properties related to the keywords occurring in
				// this sentence.
				sentence.computeKeywordsProperties(this.keywords);
				// Computes the properties related to the named entities
				// occurring in this sentence.
				sentence.computeNamedEntitiesProperties(text
						.getTotalNamedEntities());
				// Computes the properties related to the relevant words
				// occurring in this sentence.
				sentence.computeRelevantWordsProperties();
			}

			document.getText().setScore(textScore);
			document.setScore(textScore);

			for (Sentence sentence : sentences)
				sentence.setDocumentScore(textScore);
		}
	}

	protected void updateKeywordsScore(Collection<Sentence> sentences) {

		for (Sentence sentence : sentences) {
			Collection<Word> updatedWords = Utils.updateKeywordsScore(
					sentence.getWords(), this.keywords);
			sentence.setWords(updatedWords);

			double sentenceScore = sentence.computeScore();
			sentence.setScore(sentenceScore);
		}
	}

	/**
	 * <p>
	 * Extracts keywords from documents.
	 * </p>
	 */
	protected void computeGlobalKeywords() {

		LinkedList<Sentence> allSentences = new LinkedList<Sentence>();
		LinkedList<Word> allWords = new LinkedList<Word>();
		for (Document document : documents) {
			// Computes the current documents keywords
			Collection<Sentence> sentences = document.getText().getSentences();
			allSentences.addAll(sentences);

			for (Sentence sentence : sentences)
				allWords.addAll(sentence.getWords());
		}

		Collections.sort(allWords, Preferences.COMPARE_WORD_SCORE);

		// Gets the collection keywords, based on all the sentences in the
		// documents.
		this.keywords = Utils.extractKeywords(allSentences);
	}

	/**
	 * <p>
	 * Manages the ending of the running threads.
	 * </p>
	 * 
	 * @param threads
	 *            the set of running threads.
	 */
	private void manageThreadEnd(HashMap<Document, ExternalToolsThread> threads) {
		boolean ended = false;

		while (!ended) {
			boolean allTrue = true;
			Set<Map.Entry<Document, ExternalToolsThread>> set = threads
					.entrySet();

			// Checks if all the threads have ended their processing.
			for (Entry<Document, ExternalToolsThread> entry : set) {
				Document doc = entry.getKey();
				ExternalToolsThread thread = entry.getValue();

				int sentencesNumber = thread.getText().getTotalSentences();
				allTrue = allTrue && thread.hasEnded();
				ended = allTrue;

				// If the current thread has ended, we'll update the document
				// information,
				// and split the annotated text in sentences.
				if (thread.hasEnded() && sentencesNumber < 0) {
					Text text = thread.getText();
					Collection<Sentence> sentences = splitSentences(doc, text);
					text.setSentences(sentences);
					doc.setText(text);
				}
			}
		}
	}

	/**
	 * <p>
	 * Splits the text in sentences.
	 * </p>
	 * 
	 * @param text
	 *            the text to be splitted.
	 * @return the collection of sentences retrieved.
	 */
	private Collection<Sentence> splitSentences(Document document, Text text) {
		String chunked = text.getChunked();
		String annotated = text.getPosTagged();
		String neAnnotation = text.getNamedEntityAnnotation();

		Matcher mc = Pattern.compile(Regex.SENTENCE).matcher(chunked);
		Matcher ma = Pattern.compile(Regex.SENTENCE).matcher(annotated);
		Matcher men = Pattern.compile(Regex.SENTENCE).matcher(neAnnotation);

		Collection<Sentence> sentences = new LinkedList<Sentence>();

		// Creates the sentences content aligned with the POS annotation and NER
		// annotation.
		for (int i = 0; mc.find() && ma.find() && men.find(); i++) {
			String main = mc.group(1), pos = ma.group(1), ner = men.group(1);
			if (!Regex.isCaption(main) && !Regex.isSection(main)) {
				Sentence sentence = new Sentence(document.getId(), i + 1,
						main.trim(), pos.trim(), ner);

				Collection<Word> words = splitSentenceWords(document.getId(),
						i + 1, sentence);
				sentence.setWords(words);
				sentences.add(sentence);
				this.totalWords += sentence.getTotalWords();
			}
		}

		this.totalSentences += sentences.size();

		return sentences;
	}

	/**
	 * <p>
	 * Splits sentence in words.
	 * </p>
	 * 
	 * @param docId
	 *            the document identification.
	 * @param sentenceId
	 *            the sentence identification.
	 * @param sentence
	 *            the sentence to be splitted.
	 * @return the sentence word collection.
	 */
	private Collection<Word> splitSentenceWords(int docId, int sentenceId,
			Sentence sentence) {

		String[] posTokens = sentence.getPosTagged().trim()
				.split(Regex.WHITESPACE + "+");
		String[] originalTokens = sentence.getSentence().trim()
				.split(Regex.WHITESPACE + "+");

		LinkedList<Word> words = new LinkedList<Word>();

		for (int i = 0, j = 0; i < originalTokens.length
				&& j < posTokens.length; i++, j++) {

			// Checks if the current token is composed only by punctuation.
			boolean punctuationToken = Regex
					.hasOnlyPunctuation(originalTokens[i]);

			// Gets the words properties.
			String original = originalTokens[i];
			String token = Regex.getToken(posTokens[j]);
			String lemmas = Regex.getLemma(posTokens[j]);
			String annotation = Regex.getAnnotation(posTokens[j]);

			// Gets the total number of non-word tokens in the original word
			// token...
			int nonWordTokens = Regex.numberNonWordTokens(originalTokens[i]);
			// ... the number of non-word tokens before the original word...
			int preTokens = Regex.numberPreNonWordTokens(originalTokens[i]);
			// ... the number of non-word tokens after the original word ...
			int postTokens = Regex.numberPostNonWordTokens(originalTokens[i]);
			// ... and the total number of tokens currently in the original
			// token.
			// NOTE: "+ 1" defines the current token.
			int numberOfTokens = preTokens + 1 + postTokens;

			// If this token has pre-non-word tokens...
			if (preTokens > 0 && !punctuationToken) {
				// ...we'll skip these tokens, and get...
				j += preTokens;
				// ... the word token...
				token = Regex.getToken(posTokens[j]);
				// ... its lemmas...
				lemmas = Regex.getLemma(posTokens[j]);
				// ... and its annotation.
				annotation = Regex.getAnnotation(posTokens[j]);
			}

			// Skips the token if it contains a contracted word and it is not
			// exclusively a punctuation token...
			if (Regex.isContraction(posTokens[j]) && !punctuationToken) {
				// NOTE: the contracted token hasn't yet been counted as a
				// non-word token, because it is in the annotated collection.
				// The non-word tokens counting is done in the original token.
				j++;
				numberOfTokens++;
				// If it is a contraction, keeps the token and the annotation
				// from the two parts of the word.
				if (j < posTokens.length) {
					token += Regex.getToken(posTokens[j]);
					annotation += " " + Regex.getAnnotation(posTokens[j]);
				}
			}

			if (j + 1 < posTokens.length) {
				// Skips the token if it contains a clitic and it is not
				// exclusively a punctuation token...
				if (Regex.hasClitic(posTokens[j + 1]) && !punctuationToken) {
					// If it is a clitic, keeps the token and the annotation
					// from the two parts of the word.
					j++;
					numberOfTokens++;
					token += Regex.getToken(posTokens[j]);
					annotation += " " + Regex.getAnnotation(posTokens[j]);
				}
			}

			// When the token is exclusively a punctuation token...
			if (punctuationToken) {
				// ... we'll only count the non-word tokens...
				numberOfTokens = nonWordTokens;
				// (the "- 1" is due to the cycle future iteration)
				j += nonWordTokens - 1;
			}
			// When the token is a compound word, that is, it has non-word
			// tokens between two words, and:
			else {
				if (nonWordTokens > 0
				// - it has no pre-tokens and no post-tokens, or
						&& ((preTokens == 0 && postTokens == 0)
						// - it has pre-tokens or post-tokens that must be the
						// same number as the nonWordTokens
						|| (nonWordTokens > (preTokens + postTokens)))) {
					// ... we'll count the number of non-word tokens plus 2
					// tokens that are
					// the word previous to the non-word token and the word post
					// the non-word token.
					numberOfTokens = nonWordTokens + 2;
					// (the "- 1" is due to the cycle future iteration)
					int endCycle = j + numberOfTokens - 1;

					token = "";
					annotation = "";
					while (j < posTokens.length && j < endCycle) {
						token += Regex.getToken(posTokens[j]);
						annotation += " " + Regex.getAnnotation(posTokens[j]);
						j++;
					}
				}
				// When the word is a common word...
				else
					j += postTokens;
			}

			// Ignores punctuation and whitespace tokens
			SingleWord currentWord = new SingleWord(docId, sentenceId, i + 1,
					numberOfTokens, original, token, lemmas, annotation);
			words.addLast(currentWord);
		}

		return words;
	}

	/**
	 * <p>
	 * Retrieves the document with the given id.
	 * </p>
	 * 
	 * @param id
	 *            the document id.
	 * @return the Document identified by id; null if not found.
	 */
	public Document getDocument(int id) {
		Document doc = null;

		for (Document current : this.documents) {
			if (current.getId() == id) {
				doc = current;
				break;
			}
		}

		return doc;
	}

	/** Getters & Setters **/
	public Option getOptions() {
		return options;
	}

	public Collection<Document> getDocuments() {
		return documents;
	}

	public Collection<Word> getKeywords() {
		return keywords;
	}

	public int getTotalDocuments() {
		return totalDocuments;
	}

	public int getTotalSentences() {
		return totalSentences;
	}

	public int getTotalWords() {
		return totalWords;
	}

	public PostProcessedText getPostProcessedText() {
		return postProcessedText;
	}

	public Summary getSummary() {
		return summary;
	}

	public String getDemoLog() {
		return demoLog;
	}

	public void setOptions(Option options) {
		this.options = options;
	}

	public void setDocuments(Collection<Document> documents) {
		this.documents = documents;
	}

	public void setKeywords(Collection<Word> keywords) {
		this.keywords = keywords;
	}

	public void setSimplifiedText(PostProcessedText simplifiedText) {
		this.postProcessedText = simplifiedText;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}

	public void setDemoLog(String demoLog) {
		this.demoLog = demoLog;
	}

}
