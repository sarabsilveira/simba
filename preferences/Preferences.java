package preferences;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import core.Connective;
import core.Document;
import core.NamedEntity;
import core.Paragraph;
import core.Sentence;
import core.SingleWord;
import core.Word;
import external.ManageExternalTools;

/**
 * <p>
 * This class defines the system preferences.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Preferences {

	/**
	 * <p>
	 * Working directory path.
	 * </p>
	 */
	private static String PATH;

	/**
	 * <p>
	 * Configuration directory name.
	 * </p>
	 */
	private static final String PREFS_DIR = "preferences" + File.separator;

	/**
	 * <p>
	 * Configuration file name.
	 * </p>
	 */
	private static final String PREFS_FILE = "simba.prefs";

	/**
	 * <p>
	 * Connectives file name.
	 * </p>
	 */
	public static String CONNECTIVES_FILE;
	private static final String CONNECTIVES_FILE_SPLITTER = ";;";
	public static int MAX_CONNECTIVES;

	/**
	 * <p>
	 * Demo file.
	 * </p>
	 */
	public static String DEMO_FILE = "Demo.log";

	/**
	 * <p>
	 * String that represents ISO encoding
	 * </p>
	 **/
	public static final String ISO8859_1 = "ISO8859_1";
	/**
	 * <p>
	 * String that represents UTF encoding
	 * </p>
	 **/
	public static final String UTF8 = "UTF-8";

	/**
	 * <p>
	 * Sentence similarity threshold.
	 * </p>
	 **/
	public static final double SENTENCE_SIMILIARITY_THRESHOLD = 0.75; // Valor
																		// optimizado!
																		// Mas
																		// inexplicável!
																		// Também
																		// pode
																		// ser o
																		// anterior...
																		// não
																		// sei
																		// que
																		// tipo
																		// de
																		// erros
																		// dava
																		// para
																		// passar
																		// a
																		// usar
																		// este.
	// public static final double SENTENCE_SIMILIARITY_THRESHOLD = 0.75;
	public static final double SIMPLIFIED_SENTENCE_SIMILIARITY_THRESHOLD = 0.5;
	public static final double TOPIC_SIMILIARITY_THRESHOLD = 0.5;
	/**
	 * <p>
	 * Occurrence threshold.
	 * </p>
	 **/
	public static final double RELEVANCE_THRESHOLD = 0.1;// TODO PARA OPTIMIZAR
															// ESTE VALOR!!

	public static final int SENTENCES_PER_PARAGRAPH = 4;// TODO PARA OPTIMIZAR
														// ESTE VALOR!!

	/**
	 * <p>
	 * Phrase score threshold.
	 * </p>
	 **/
	public static final double PHRASE_SCORE_THRESHOLD = 0.025; // TODO PARA
																// OPTIMIZAR
																// ESTE VALOR!!
	/**
	 * <p>
	 * Minimum phrase length compared with the original sentence.
	 * </p>
	 **/
	public static final double MINIMUM_PHRASE_LENGTH_PERCENTAGE = 0.75; // Valor
																		// optimizado!
	/**
	 * <p>
	 * Value to be added or removed from the sentence score, in order to
	 * priorize the sentences.
	 * </p>
	 **/
	public static final double SENTENCE_EXTRA_SCORE_DEFAULT_VALUE = 0.5; // Valor
																			// optimizado!
	/**
	 * <p>
	 * Value to be added or removed from the word score.
	 * </p>
	 **/
	// public static final double WORD_EXTRA_SCORE_DEFAULT_VALUE = 1.0; // TODO
	// Valor por optimizar!
	public static final double WORD_EXTRA_SCORE_DEFAULT_VALUE = 0.25; // TODO
																		// Valor
																		// por
																		// optimizar!

	/**
	 * <p>
	 * Maximum number of keywords.
	 * </p>
	 */
	public static int MAX_KEYWORDS;
	/**
	 * <p>
	 * Minimum sentence length.
	 * </p>
	 */
	public static final int MIN_SENTENCE_LENGTH = 15;
	public static final int SIMPL_MIN_SENTENCE_LENGTH = 10;
	// public static final int MIN_SENTENCE_LENGTH = 10;
	/**
	 * <p>
	 * Minimum sentence score.
	 * </p>
	 */
	public static final double MIN_SENTENCE_SCORE = 1.0;

	/**
	 * <p>
	 * Maximum threads to perform annotation process.
	 * </p>
	 **/
	public static final int MAXIMUM_THREADS = 7;

	/**
	 * <p>
	 * Minimum length of a phrase to be relevant.
	 * </p>
	 **/
	public static final int MINIMUM_PHRASE_LENGTH = 5;

	/**
	 * <p>
	 * Minimum percentage of proper names in a sentence for it to be considered
	 * relevant.
	 * </p>
	 */
	public static final double MINIMUM_PNMS = 0.60;

	/**
	 * <p>
	 * Maximum tokens that the sentence can have to be parsed.
	 * </p>
	 **/
	public static final int MAX_WORDS_TO_PARSE = 50;

	/**
	 * <p>
	 * Keyword extra score.
	 * </p>
	 **/
	public static final double KEYWORD_SCORE = 0.5;// 0.25; // (2013-08-16)
													// Using the same score as
													// SENTENCE_EXTRA_SCORE_DEFAULT_VALUE
													// to justify it easily.

	/**
	 * <p>
	 * Named entities need to have this percentage of common words.
	 * </p>
	 **/
	public static final double NAMED_ENTITIES_SIMILARITY_PERCENTAGE = 0.5;

	/**
	 * <p>
	 * System name.
	 * </p>
	 **/
	public static String NAME;
	// Directories
	/**
	 * <p>
	 * External tools location.
	 * </p>
	 */
	private static String EXTERNAL_TOOLS;
	/**
	 * <p>
	 * LX-NER location.
	 * </p>
	 */
	public static String LXNER;
	/**
	 * <p>
	 * LX-NER development location.
	 * </p>
	 */
	private static String LXNER_DEV;
	/**
	 * <p>
	 * LX-Suite location.
	 * </p>
	 */
	public static String LXSUITE;
	/**
	 * <p>
	 * LX-Suite pos-annotation running file.
	 * </p>
	 */
	public static String LXSUITE_POSANNOTATION;
	/**
	 * <p>
	 * LX-Suite chunker running file.
	 * </p>
	 */
	public static String LXSUITE_CHUNKER;
	/**
	 * <p>
	 * LX-Suite running path.
	 * </p>
	 */
	public static String LXSUITE2RUN;

	/**
	 * <p>
	 * Constituency parser path.
	 * </p>
	 **/
	private static String CONSTITUENCY_PARSER;
	/**
	 * <p>
	 * Constituency parser model file name.
	 * </p>
	 **/
	public static String PARSER_MODEL_FILE;

	/**
	 * <p>
	 * Weka Classifier model file.
	 * </p>
	 **/
	public static String WEKA_CLASS_MODEL_FILE;
	/**
	 * <p>
	 * Weka location.
	 * </p>
	 **/
	public static String WEKA_LOCATION;
	/**
	 * <p>
	 * Weka subtype classifier model file.
	 * </p>
	 **/
	public static String WEKA_SUBTYPE_MODEL_FILE;
	/**
	 * <p>
	 * Weka binary classifier model file.
	 * </p>
	 **/
	public static String WEKA_BINARY_MODEL_FILE;

	/**
	 * <p>
	 * System default input documents location.
	 * </p>
	 **/
	public static String INPUT_DEFAULT_LOCATION;
	/**
	 * <p>
	 * Input directory location.
	 * </p>
	 **/
	public static String INPUT_DIR_LOCATION;
	/**
	 * <p>
	 * System default output documents location.
	 * </p>
	 **/
	public static String OUTPUT_DEFAULT_LOCATION;
	/**
	 * <p>
	 * System default output file.
	 * </p>
	 **/
	public static String OUTPUT_FILE;
	/**
	 * <p>
	 * System default statistics document location.
	 * </p>
	 **/
	public static String STATISTICS_LOCATION;
	/**
	 * <p>
	 * System default statistics filename.
	 * </p>
	 **/
	public static String STATISTICS_FILE;

	/**
	 * <p>
	 * System default compression rate.
	 * </p>
	 **/
	public static double DEFAULT_COMPRESSION_RATE;
	/**
	 * <p>
	 * Maximum number of words used when creating an headline.
	 * </p>
	 **/
	public static int HEADLINE_MAX_WORDS;

	/**
	 * <p>
	 * Separator used to mark, in the same String, different keywords submitted
	 * by the user.
	 * </p>
	 */
	public static String KEYWORD_SEPARATION;

	/**
	 * <p>
	 * Summary type.
	 * </p>
	 **/
	public static Preferences.FINAL_SUMMARY_TYPE SUMMARY_TYPE;

	/**
	 * <p>
	 * Final summary types.
	 * </p>
	 **/
	public static enum FINAL_SUMMARY_TYPE {
		SIMPLIFIED, NONSIMPLIFIED, NONPOSTPROCESSED, ONLYPOSTPROCESSEDNOSUMMARY, RANDOMBASELINE, SIMPLIFICATIONBASELINE, PARAGRAPHBASELINE, CONNECTIVESBASELINE, REPLACECONNECTIVES, REPLACEPARAGRAPHS;
	};

	public static enum GRAMTYPE {
		SUB, COORD, // SUB and COORD are conjunctional connectives
		ADV, VERBAL // Connectives that include a verb expression
	};

	public static enum RULETYPE {
		POSITIVE, NEGATIVE
	};

	// public static Hashtable<String, List<String>> DISCOURSE_RELATIONS = new
	// Hashtable<String, List<String>>();
	public static String[] DISCOURSE_RELATIONS = new String[] {
			"Temporal/Asynchronous/Precedence",
			"Temporal/Asynchronous/Succession", "Temporal/Synchronous",
			"Contingency/Cause/Reason", "Contingency/Cause/Result",
			"Contingency/Condition/Hypothetical",
			"Contingency/Condition/Factual",
			"Contingency/Condition/Contra-factual",
			"Comparison/Contrast/Opposition/",
			"Comparison/Concession/Expectation",
			"Comparison/Concession/Contra-expectation", "Expansion/Addition/",
			"Expansion/Instantiation/",
			"Expansion/Restatement/Specification-Equivalence",
			"Expansion/Restatement/Generalization",
			"Expansion/Alternative/Conjuntive-Disjunctive",
			"Expansion/Alternative/Chosen-alternative", "Expansion/Exception/" };
	public static String[] SUBTYPES = new String[] { "Precedence",
			"Succession", "Synchronous", "Reason", "Result", "Hypothetical",
			"Factual", "Contra-factual", "Opposition", "Expectation",
			"Contra-expectation", "Addition", "Instantiation",
			"Specification-Equivalence", "Generalization",
			"Conjuntive-Disjunctive", "Chosen-alternative", "Exception" };

	public static final String WEKA_BINARY_CLASSIFICATION_TYPE = "BINARY";
	public static final String WEKA_YES = "yes";
	public static final String WEKA_NO = "no";
	public static final String WEKA_OTHER = "Other";

	public static final int CONTEXT_WINDOW = 3;

	/**
	 * <p>
	 * NP retrieved types of phrases.
	 * </p>
	 */
	public enum PhrasesType {
		NP_ADVERB, VP_ADVERB, VP_CONJP, RELATIVE, PARENTHETICAL, PREPOSITIONAL, APPOSITION, ADJECTIVE
	}

	/**
	 * <p>
	 * Determines if the current run is an evaluation one.
	 * </p>
	 **/
	public static boolean EVALUATION_PROCEDURE;
	/**
	 * <p>
	 * Location of the sentence parses.
	 * </p>
	 **/
	public static String PARSES_LOCATION;

	/**
	 * <p>
	 * Sentence weighting scores.
	 * </p>
	 **/
	public static double SENTENCE_SCORE;
	public static double SENTENCE_EXTRASCORE;
	public static double SENTENCE_DOCUMENTSCORE;
	public static double SENTENCE_SIMPLIFICATIONSCORE;
	public static double SENTENCE_CLUSTERSSIZE;
	public static double SENTENCE_FREQUENCY;
	public static double SENTENCE_RELATIVEPOSITION;
	public static double SENTENCE_RELATIVEPOSITIONRATIO;
	public static double SENTENCE_NUMBEROFKEYWORDS;
	public static double SENTENCE_KEYWORDSRATIO;
	public static double SENTENCE_KEYWORDCLUSTERSCORE;
	public static double SENTENCE_SIMILARITYCLUSTERSCORE;
	public static double SENTENCE_NUMBEROFNAMEDENTITIES;
	public static double SENTENCE_NAMEDENTITIESRATIO;
	public static double SENTENCE_NAMEDENTITIESAVERAGESCORE;
	public static double SENTENCE_KEYWORDSAVERAGESCORE;
	public static double SENTENCE_SIMILARITY2BIGGEST;
	public static double SENTENCE_NUMBEROFRELEVANTWORDS;
	public static double SENTENCE_RELEVANTWORDSRATIO;
	public static double SENTENCE_RELEVANTWORDSAVERAGESCORE;

	/**
	 * <p>
	 * Sentence weighting score groups.
	 * </p>
	 **/
	public static double SENTENCE_SCORES_WEIGHT;
	public static double SENTENCE_RATIOS_WEIGHT;
	public static double SENTENCE_NUMBERS_WEIGHT;
	public static double SENTENCE_OTHERS_WEIGHT;

	/**
	 * <p>
	 * Word weighting scores.
	 * </p>
	 **/
	public static double WORD_TFIDF;
	public static double WORD_EXTRASCORE;
	public static double WORD_NUMBEROFDOCS;
	public static double WORD_SENTENCERELATIVEPOSITION;

	/**
	 * <p>
	 * Word String comparator
	 * </p>
	 **/
	public static Comparator<Word> COMPARE_WORDS = new Comparator<Word>() {

		@Override
		public int compare(Word o1, Word o2) {

			if (o1 instanceof SingleWord && o2 instanceof SingleWord)
				if (((SingleWord) o1).getWord().equalsIgnoreCase(
						((SingleWord) o2).getWord()))
					return 0;
				else
					return 1;
			else if (o1 instanceof NamedEntity && o2 instanceof NamedEntity)
				if (((NamedEntity) o1).equals(((NamedEntity) o2)))
					return 0;
				else
					return 1;
			else
				return 1;
		}
	};

	/**
	 * <p>
	 * Named entities comparator
	 * </p>
	 **/
	public static Comparator<Word> COMPARE_NAMED_ENTITIES = new Comparator<Word>() {

		@Override
		public int compare(Word o1, Word o2) {

			// if (o1 instanceof SingleWord && o2 instanceof SingleWord)
			// if
			// (((SingleWord)o1).getWord().equalsIgnoreCase(((SingleWord)o2).getWord()))
			// return 0;
			// else
			// return 1;
			if (o1 instanceof NamedEntity && o2 instanceof NamedEntity) {
				if (((NamedEntity) o1).same(((NamedEntity) o2)))
					return 1;
				else {
					double commonWords = ((NamedEntity) o1)
							.commonWords(((NamedEntity) o2));
					double percentage = commonWords
							/ (Math.max(((NamedEntity) o1).getEntityWords()
									.size(), ((NamedEntity) o2)
									.getEntityWords().size()));

					if (percentage >= Preferences.NAMED_ENTITIES_SIMILARITY_PERCENTAGE) {
						System.out.println("PERCENT_" + o1.toString() + "\t"
								+ o2.toString() + "\tcW_" + commonWords
								+ "\tPer_" + percentage);
						return 1;
					} else
						return -1;
				}
			} else
				return -1;
		}
	};

	/**
	 * <p>
	 * Word frequency comparator
	 * </p>
	 **/
	public static Comparator<Word> COMPARE_WORD_TFIDF = new Comparator<Word>() {
		@Override
		public int compare(Word o1, Word o2) {

			if (o1.getTFIDF() > o2.getTFIDF())
				return -1;
			else if (o1.getTFIDF() < o2.getTFIDF())
				return 1;
			else if (o1.getOccurrences() > o2.getOccurrences())
				return -1;
			else if (o1.getOccurrences() < o2.getOccurrences())
				return 1;
			return 0;
		}
	};

	/**
	 * <p>
	 * Word occurrences comparator
	 * </p>
	 **/
	public static Comparator<Word> COMPARE_WORD_OCCURRENCES = new Comparator<Word>() {
		@Override
		public int compare(Word o1, Word o2) {

			if (o1.getOccurrences() > o2.getOccurrences())
				return -1;
			else if (o1.getOccurrences() < o2.getOccurrences())
				return 1;
			return compareWordScores(o1, o2);
		}
	};

	/**
	 * <p>
	 * Word frequency comparator
	 * </p>
	 **/
	public static Comparator<Word> COMPARE_WORD_FREQUENCY = new Comparator<Word>() {
		@Override
		public int compare(Word o1, Word o2) {

			if (o1.getFrequency() > o2.getFrequency())
				return -1;
			else if (o1.getFrequency() < o2.getFrequency())
				return 1;
			return 0;
		}
	};

	// /** <p>Word frequency comparator</p> **/
	// public static Comparator<Word> COMPARE_WORD_OCCURRENCES = new
	// Comparator<Word>() {
	// @Override
	// public int compare(SingleWord o1, SingleWord o2) {
	// if (o1.getOccurrences() > o2.getOccurrences())
	// return -1;
	// else if (o1.getOccurrences() < o2.getOccurrences())
	// return 1;
	// return 0;
	// }
	// };

	/**
	 * <p>
	 * Word score comparator
	 * </p>
	 **/
	public static Comparator<Word> COMPARE_WORD_SCORE = new Comparator<Word>() {
		@Override
		public int compare(Word o1, Word o2) {
			return compareWordScores(o1, o2);
		}
	};

	/**
	 * Sorts a Map by value, by descending order.
	 */
	public static Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	private static int compareWordScores(Word o1, Word o2) {
		// o1.computeScore(); o2.computeScore();
		if (o1.getScore() > o2.getScore())
			return -1;
		else if (o1.getScore() < o2.getScore())
			return 1;
		// else if (o1.getScore() > o2.getScore())
		// return -1;
		// else if (o1.getScore() < o2.getScore())
		// return 1;
		else if (o1.getNumberOfDocs() > o2.getNumberOfDocs())
			return -1;
		else if (o1.getNumberOfDocs() < o2.getNumberOfDocs())
			return 1;
		else if (o1.getTFIDF() > o2.getTFIDF())
			return -1;
		else if (o1.getTFIDF() < o2.getTFIDF())
			return 1;
		// else if (o1.getFrequency() > o2.getFrequency())
		// return -1;
		// else if (o1.getFrequency() < o2.getFrequency())
		// return 1;
		return 0;
	}

	/**
	 * <p>
	 * Document score comparator
	 * </p>
	 **/
	public static Comparator<Document> COMPARE_DOCUMENT_SCORE = new Comparator<Document>() {
		@Override
		public int compare(Document o1, Document o2) {
			if (o1.getScore() > o2.getScore())
				return -1;
			else if (o1.getScore() < o2.getScore())
				return 1;
			return 0;
		}
	};

	/**
	 * <p>
	 * Sentence vs Document comparator
	 * </p>
	 **/
	public static Comparator<Sentence> SUMMARY_FINAL_ORDER = new Comparator<Sentence>() {
		@Override
		public int compare(Sentence o1, Sentence o2) {

			// TODO Produzir um algoritmo diferente:
			// Que tenha em conta na ordenação os seguintes critérios:
			// (- o score => NÃO!!! isto é para a compressão!!!)
			// - a ordem da frase no documento (e eventualmente das frases que
			// estão inseridas no mesmo cluster de similaridade)
			// - precedência (verificar se a frase corrente não está a ficar
			// atrás de alguma frase que a preceda no texto original)
			// - similaridade entre si (quanto maior for a similaridade entre
			// si, maior a probabilidade de duas frases ficarem perto uma da
			// outra)
			// (encontrar a ordem das frases que minimiza a distância entre
			// elas)
			// - ver outros possíveis critérios em: Barzilay, 2002 ou
			// Bollegalla, 2009

			// Caso as duas frases estejam no mesmo documento, compara-se a
			// posição absoluta e não se utiliza mais nenhum critério.
			if (o1.getDocumentId() == o2.getDocumentId()) {

				if (o1.getAbsolutePosition() < o2.getAbsolutePosition())
					return -1;
				else if (o1.getAbsolutePosition() > o2.getAbsolutePosition())
					return 1;
				else
					return 0;
			}
			// Caso as duas frases estejam em documentos diferentes...
			else {

				if (o1.getKeywordsKey() != null && o2.getKeywordsKey() != null) {

					// Se estão no mesmo cluster de keywords
					if (o1.getKeywordsKey().representation()
							.equals(o2.getKeywordsKey().representation())) {
						// int scores = compareMainScores(o1, o2);
						// if (o1.getTotalWords() > o2.getTotalWords())
						// return -1;
						// else if (o1.getTotalWords() < o2.getTotalWords())
						// return 1;
						// else
						// return 0;

						// double similarity = o1.computeSimilarity(o2);

						// System.out.println("s1_"+o1.getSentence());
						// System.out.println("\ts2_"+o2.getSentence());
						// System.out.println("\t\t\tSIM_"+similarity);

						// if (similarity >= 0.2) //
						// Preferences.SENTENCE_SIMILIARITY_THRESHOLD)
						// return 0;
						// else{

						if (o1.getNumberOfKeywords() > o2.getNumberOfKeywords())
							return -1;
						else if (o1.getNumberOfKeywords() < o2
								.getNumberOfKeywords())
							return 1;
						else {
							if (o1.getNumberOfNamedEntities() > o2
									.getNumberOfNamedEntities())
								return -1;
							else if (o1.getNumberOfNamedEntities() < o2
									.getNumberOfNamedEntities())
								return 1;
							else {
								if (o1.getNumberOfRelevantWords() > o2
										.getNumberOfRelevantWords())
									return -1;
								else if (o1.getNumberOfRelevantWords() < o2
										.getNumberOfRelevantWords())
									return 1;
								else {
									if (o1.getTotalWords() > o2.getTotalWords())
										return -1;
									else if (o1.getTotalWords() < o2
											.getTotalWords())
										return 1;
									else
										return 0;
								}
								// }
							}
						}
					} else
						return compareMainScores(o1, o2);
				} else if (o1.getKeywordsKey() != null
						&& o2.getKeywordsKey() == null)
					return -1;
				else if (o2.getKeywordsKey() != null
						&& o1.getKeywordsKey() == null)
					return 1;
				else
					return compareMainScores(o1, o2);
			}
		}
		// }
		// }
		//
		// // return compareMainScores(o1,o2) ;
		// }
	};

	public static Comparator<Sentence> COMPARE_SENTENCES_IN_PARAGRAPH = new Comparator<Sentence>() {
		@Override
		public int compare(Sentence o1, Sentence o2) {

			// TODO Produzir um algoritmo diferente:
			// Que tenha em conta na ordenação os seguintes critérios:
			// (- o score => NÃO!!! isto é para a compressão!!!)
			// - a ordem da frase no documento (e eventualmente das frases que
			// estão inseridas no mesmo cluster de similaridade)
			// - precedência (verificar se a frase corrente não está a ficar
			// atrás de alguma frase que a preceda no texto original)
			// - similaridade entre si (quanto maior for a similaridade entre
			// si, maior a probabilidade de duas frases ficarem perto uma da
			// outra)
			// (encontrar a ordem das frases que minimiza a distância entre
			// elas)
			// - ver outros possíveis critérios em: Barzilay, 2002 ou
			// Bollegalla, 2009

			// Caso as duas frases estejam no mesmo documento, compara-se a
			// posição absoluta e não se utiliza mais nenhum critério.
			if (o1.getDocumentId() == o2.getDocumentId()) {

				if (o1.getAbsolutePosition() < o2.getAbsolutePosition())
					return -1;
				else if (o1.getAbsolutePosition() > o2.getAbsolutePosition())
					return 1;
			}
			// else {
			// if (o1.getNumberOfKeywords() > o2.getNumberOfKeywords())
			// return -1;
			// else if (o1.getNumberOfKeywords() < o2.getNumberOfKeywords())
			// return 1;
			// }

			// else
			// if (o1.completeScore() > o2.completeScore())
			// return -1;
			// else if (o1.completeScore() < o2.completeScore())
			// return 1;
			// else{
			// if (o1.getScore() > o2.getScore())
			// return -1;
			// else if (o1.getScore() < o2.getScore())
			// return 1;
			// else{
			// if (o1.getExtraScore() > o2.getExtraScore())
			// return -1;
			// else if (o1.getExtraScore() < o2.getExtraScore())
			// return 1;
			// // else
			// }
			// // }
			// }

			// return -1;

			if (o1.getAbsolutePosition() < o2.getAbsolutePosition())
				return -1;
			else if (o1.getAbsolutePosition() > o2.getAbsolutePosition())
				return 1;
			else if (o1.getAvgPositionSimCluster() > o2
					.getAvgPositionSimCluster())
				return -1;
			else if (o1.getAvgPositionSimCluster() < o2
					.getAvgPositionSimCluster())
				return 1;
			else if (o1.getSimClusterSize() > o2.getSimClusterSize())
				return -1;
			else if (o1.getSimClusterSize() < o2.getSimClusterSize())
				return 1;

			// else{
			// if (o1.getExtraScore() > o2.getExtraScore())
			// return -1;
			// else if (o1.getExtraScore() < o2.getExtraScore())
			// return 1;
			// else{
			// if (o1.getAbsolutePosition() < o2.getAbsolutePosition())
			// return -1;
			// else if (o1.getAbsolutePosition() > o2.getAbsolutePosition())
			// return 1;
			// // return 0;
			// }
			// }

			System.out
					.println("Vamos para os scores... Nada do resto definiu nada...");
			return compareMainScores(o1, o2);
		}
	};

	public static Comparator<Sentence> SENTENCES_TO_REVIEW = new Comparator<Sentence>() {
		@Override
		public int compare(Sentence o1, Sentence o2) {

			// TODO Produzir um algoritmo diferente:
			// Que tenha em conta na ordenação os seguintes critérios:
			// (- o score => NÃO!!! isto é para a compressão!!!)
			// - a ordem da frase no documento (e eventualmente das frases que
			// estão inseridas no mesmo cluster de similaridade)
			// - precedência (verificar se a frase corrente não está a ficar
			// atrás de alguma frase que a preceda no texto original)
			// - similaridade entre si (quanto maior for a similaridade entre
			// si, maior a probabilidade de duas frases ficarem perto uma da
			// outra)
			// (encontrar a ordem das frases que minimiza a distância entre
			// elas)
			// - ver outros possíveis critérios em: Barzilay, 2002 ou
			// Bollegalla, 2009

			// Caso as duas frases estejam no mesmo documento, compara-se a
			// posição absoluta e não se utiliza mais nenhum critério.
			if (o1.getDocumentId() == o2.getDocumentId()) {

				if (o1.getAbsolutePosition() < o2.getAbsolutePosition())
					return -1;
				else if (o1.getAbsolutePosition() > o2.getAbsolutePosition())
					return 1;
			}

			if (o1.getKeywordClusterScore() > o2.getKeywordClusterScore())
				return 1;
			else if (o1.getKeywordClusterScore() < o2.getKeywordClusterScore())
				return -1;
			else {
				if (o1.getSimilarityClusterScore() > o2
						.getSimilarityClusterScore())
					return 1;
				else if (o1.getSimilarityClusterScore() < o2
						.getSimilarityClusterScore())
					return -1;
				else {
					if (o1.getClustersSize() > o2.getClustersSize())
						return 1;
					else if (o1.getClustersSize() < o2.getClustersSize())
						return -1;
					else {
						if (o1.getNumberOfKeywords() > o2.getNumberOfKeywords())
							return -1;
						else if (o1.getNumberOfKeywords() < o2
								.getNumberOfKeywords())
							return 1;
						else {
							if (o1.getNumberOfNamedEntities() > o2
									.getNumberOfNamedEntities())
								return -1;
							else if (o1.getNumberOfNamedEntities() < o2
									.getNumberOfNamedEntities())
								return 1;
							else {
								if (o1.getNumberOfRelevantWords() > o2
										.getNumberOfRelevantWords())
									return -1;
								else if (o1.getNumberOfRelevantWords() < o2
										.getNumberOfRelevantWords())
									return 1;
								else {
									// if (o1.getTotalWords() >
									// o2.getTotalWords())
									// return -1;
									// else if (o1.getTotalWords() <
									// o2.getTotalWords())
									// return 1;
									// else{
									if (o1.getAbsolutePosition() < o2
											.getAbsolutePosition())
										return -1;
									else if (o1.getAbsolutePosition() > o2
											.getAbsolutePosition())
										return 1;
									// }
								}
							}
						}
					}
				}
			}

			return compareMainScores(o1, o2);
		}
	};

	/******
	 * public static Comparator<Sentence> SENTENCES_TO_REVIEW = new
	 * Comparator<Sentence>() {
	 * 
	 * @Override public int compare(Sentence o1, Sentence o2) {
	 * 
	 *           // TODO Produzir um algoritmo diferente: // Que tenha em conta
	 *           na ordenação os seguintes critérios: // (- o score => NÃO!!!
	 *           isto é para a compressão!!!) // - a ordem da frase no documento
	 *           (e eventualmente das frases que estão inseridas no mesmo
	 *           cluster de similaridade) // - precedência (verificar se a frase
	 *           corrente não está a ficar atrás de alguma frase que a preceda
	 *           no texto original) // - similaridade entre si (quanto maior for
	 *           a similaridade entre si, maior a probabilidade de duas frases
	 *           ficarem perto uma da outra) // (encontrar a ordem das frases
	 *           que minimiza a distância entre elas) // - ver outros possíveis
	 *           critérios em: Barzilay, 2002 ou Bollegalla, 2009
	 * 
	 *           // Caso as duas frases estejam no mesmo documento, compara-se a
	 *           posição absoluta e não se utiliza mais nenhum critério. if
	 *           (o1.getDocumentId() == o2.getDocumentId()){
	 * 
	 *           if (o1.getAbsolutePosition() < o2.getAbsolutePosition()) return
	 *           -1; else if (o1.getAbsolutePosition() >
	 *           o2.getAbsolutePosition()) return 1; else return 0; } // Caso as
	 *           duas frases estejam em documentos diferentes... else {
	 * 
	 *           if (o1.getKeywordsKey() != null && o2.getKeywordsKey() !=
	 *           null){
	 * 
	 *           // Se estão no mesmo cluster de keywords if
	 *           (o1.getKeywordsKey()
	 *           .representation().equals(o2.getKeywordsKey()
	 *           .representation())){ // int scores = compareMainScores(o1, o2);
	 *           // if (o1.getTotalWords() > o2.getTotalWords()) // return -1;
	 *           // else if (o1.getTotalWords() < o2.getTotalWords()) // return
	 *           1; // else // return 0;
	 * 
	 *           double similarity = o1.computeSimilarity(o2);
	 * 
	 *           // System.out.println("s1_"+o1.getSentence()); //
	 *           System.out.println("\ts2_"+o2.getSentence()); //
	 *           System.out.println("\t\t\tSIM_"+similarity);
	 * 
	 *           if (similarity >= 0.2) //
	 *           Preferences.SENTENCE_SIMILIARITY_THRESHOLD) return 0; else{
	 * 
	 *           if (o1.getNumberOfKeywords() > o2.getNumberOfKeywords()) return
	 *           -1; else if (o1.getNumberOfKeywords() <
	 *           o2.getNumberOfKeywords()) return 1; else { if
	 *           (o1.getNumberOfNamedEntities() > o2.getNumberOfNamedEntities())
	 *           return -1; else if (o1.getNumberOfNamedEntities() <
	 *           o2.getNumberOfNamedEntities()) return 1; else { if
	 *           (o1.getNumberOfRelevantWords() > o2.getNumberOfRelevantWords())
	 *           return -1; else if (o1.getNumberOfRelevantWords() <
	 *           o2.getNumberOfRelevantWords()) return 1; else { if
	 *           (o1.getTotalWords() > o2.getTotalWords()) return -1; else if
	 *           (o1.getTotalWords() < o2.getTotalWords()) return 1; else{ if
	 *           (o1.getSimplificationScore() > o2.getSimplificationScore())
	 *           return -1; else if (o1.getSimplificationScore() <
	 *           o2.getSimplificationScore()) return 1; else return 0; } } } } }
	 *           } else{ if (o1.getSimplificationScore() >
	 *           o2.getSimplificationScore()) return -1; else if
	 *           (o1.getSimplificationScore() < o2.getSimplificationScore())
	 *           return 1; else return compareMainScores(o1, o2); } } else if
	 *           (o1.getKeywordsKey() != null && o2.getKeywordsKey() == null)
	 *           return -1; else if (o2.getKeywordsKey() != null &&
	 *           o1.getKeywordsKey() == null) return 1; else{ if
	 *           (o1.getSimplificationScore() > o2.getSimplificationScore())
	 *           return -1; else if (o1.getSimplificationScore() <
	 *           o2.getSimplificationScore()) return 1; else return
	 *           compareMainScores(o1,o2); } } } // } // } // //// return
	 *           compareMainScores(o1,o2) ; // } };
	 ****/

	/**
	 * <p>
	 * Sentence vs Document comparator
	 * </p>
	 **/
	public static Comparator<Sentence> COMPARE_SENTENCE_COMPLETE_SCORE = new Comparator<Sentence>() {
		@Override
		public int compare(Sentence o1, Sentence o2) {
			return compareMainScores(o1, o2);
		}
	};

	/**
	 * <p>
	 * Sentence vs Document comparator
	 * </p>
	 **/
	public static Comparator<Sentence> COMPARE_SENTENCE_BEST_WORDS = new Comparator<Sentence>() {
		@Override
		public int compare(Sentence o1, Sentence o2) {

			if (o1.orderingScore() > o2.orderingScore())
				return -1;
			else if (o1.orderingScore() < o2.orderingScore())
				return 1;
			else {

				if (o1.getNumberOfKeywords() > o2.getNumberOfKeywords())
					return -1;
				else if (o1.getNumberOfKeywords() < o2.getNumberOfKeywords())
					return 1;
				else {
					if (o1.getNumberOfNamedEntities() > o2
							.getNumberOfNamedEntities())
						return -1;
					else if (o1.getNumberOfNamedEntities() < o2
							.getNumberOfNamedEntities())
						return 1;
					else {
						// if (o1.getNumberOfRelevantWords() >
						// o2.getNumberOfRelevantWords())
						// return -1;
						// else if (o1.getNumberOfRelevantWords() <
						// o2.getNumberOfRelevantWords())
						// return 1;
						// else {
						if (o1.getTotalWords() > o2.getTotalWords())
							return -1;
						else if (o1.getTotalWords() < o2.getTotalWords())
							return 1;
						else {
							if (o1.getAbsolutePosition() < o2
									.getAbsolutePosition())
								return -1;
							else if (o1.getAbsolutePosition() > o2
									.getAbsolutePosition())
								return 1;
							else {
								return compareMainScores(o1, o2);
							}
						}
					}
				}
			}
		}
	};

	/**
	 * <p>
	 * Sentence score comparator.
	 * </p>
	 **/
	/** @deprecated... **/
	public static Comparator<Sentence> COMPARE_SENTENCE_SCORE = new Comparator<Sentence>() {
		@Override
		public int compare(Sentence o1, Sentence o2) {
			if (o1.getScore() > o2.getScore())
				return -1;
			else if (o1.getScore() < o2.getScore())
				return 1;
			else
				return compareMainScores(o1, o2);
		}
	};

	/**
	 * <p>
	 * Subsentence score comparator.
	 * </p>
	 **/
	public static Comparator<Sentence> COMPARE_SIMPLIFICATION_SCORE = new Comparator<Sentence>() {
		@Override
		public int compare(Sentence o1, Sentence o2) {
			// if (o1.getSimplificationScore() > o2.getSimplificationScore())
			// return -1;
			// else if (o1.getSimplificationScore() <
			// o2.getSimplificationScore())
			// return 1;
			// else{
			// if (o1.getTotalFrequency() > o2.getTotalFrequency())
			// return -1;
			// else if (o1.getTotalFrequency() < o2.getTotalFrequency())
			// return 1;
			// else
			return compareMainScores(o1, o2);
			// }
		}
	};

	public static Comparator<Paragraph> COMPARE_PARAGRAPH_SCORE = new Comparator<Paragraph>() {
		@Override
		public int compare(Paragraph o1, Paragraph o2) {
			if (o1.getScore() > o2.getScore())
				return -1;
			else if (o1.getScore() < o2.getScore())
				return 1;
			else if (o1.size() > o2.size())
				return -1;
			else if (o1.size() < o2.size())
				return 1;
			return -1;
		}
	};

	public static Comparator<Paragraph> COMPARE_PARAGRAPH_SIZE = new Comparator<Paragraph>() {
		@Override
		public int compare(Paragraph o1, Paragraph o2) {
			if (o1.size() > o2.size())
				return -1;
			else if (o1.size() < o2.size())
				return 1;
			else if (o1.getScore() > o2.getScore())
				return -1;
			else if (o1.getScore() < o2.getScore())
				return 1;
			return -1;
		}
	};

	/**
	 * <p>
	 * Compares the original scores of the sentences.
	 * </p>
	 * 
	 * @param o1
	 *            the first sentence.
	 * @param o2
	 *            the second sentence.
	 * @return
	 */
	protected static int compareMainScores(Sentence o1, Sentence o2) {
		if (o1.completeScore() > o2.completeScore())
			return -1;
		else if (o1.completeScore() < o2.completeScore())
			return 1;
		else {
			if (o1.getScore() > o2.getScore())
				return -1;
			else if (o1.getScore() < o2.getScore())
				return 1;
			else {
				if (o1.getExtraScore() > o2.getExtraScore())
					return -1;
				else if (o1.getExtraScore() < o2.getExtraScore())
					return 1;
				else {
					if (o1.getAbsolutePosition() < o2.getAbsolutePosition())
						return -1;
					else if (o1.getAbsolutePosition() > o2
							.getAbsolutePosition())
						return 1;
					return 0;
				}
			}
		}
	}

	public static Comparator<Sentence> COMPARE_SENTENCE_TOTAL_WORDS = new Comparator<Sentence>() {

		@Override
		public int compare(Sentence o1, Sentence o2) {

			if (o1.getTotalWords() == o2.getTotalWords())
				return 0;
			else if (o1.getTotalWords() > o2.getTotalWords())
				return -1;
			else
				return 1;
		}
	};

	public static Comparator<Sentence> COMPARE_SENTENCE_ABS_POSITION = new Comparator<Sentence>() {

		@Override
		public int compare(Sentence o1, Sentence o2) {

			if (o1.getAbsolutePosition() == o2.getAbsolutePosition())
				return 0;
			else if (o1.getAbsolutePosition() < o2.getAbsolutePosition())
				return -1;
			else
				return 1;
		}
	};

	// /** <p>Sentence comparison by all the possible scores.</p> **/
	// public static Comparator<Sentence> COMPARE_ALL_SCORES = new
	// Comparator<Sentence>() {
	// @Override
	// public int compare(Sentence o1, Sentence o2) {
	//
	// }
	// };

	/**
	 * <p>
	 * Summary span
	 * <ul>
	 * <li>Single-document</li>
	 * <li>Multi-document</li>
	 * </ul>
	 * </p>
	 */
	public static enum Span {
		SINGLE, MULTI
	}

	/**
	 * <p>
	 * Summary function
	 * <ul>
	 * <li>Indicative</li>
	 * <li>Informative</li>
	 * <li>Critical</li>
	 * </ul>
	 * </p>
	 */
	public static enum Function {
		INDICATIVE, INFORMATIVE, CRITICAL
	}

	/**
	 * <p>
	 * Summary type of output content
	 * <ul>
	 * <li>Generic (built using the document collection keywords)</li>
	 * <li>Query-driven (query must be submitted)</li>
	 * </ul>
	 * </p>
	 */
	public static enum Content {
		GENERIC, QUERY
	}

	/**
	 * <p>
	 * Type of evaluation
	 * <ul>
	 * <li>Objective</li>
	 * <li>Rouge</li>
	 * <li>Both</li>
	 * </ul>
	 * </p>
	 */
	public static enum EvaluationType {
		OBJECTIVE, ROUGE, BOTH
	}

	/**
	 * <p>
	 * Input documents format.
	 * <ul>
	 * <li>Summary generation</li>
	 * <li>Headline generation</li>
	 * </ul>
	 * </p>
	 */
	public static enum InputType {
		SUMMARY, HEADLINE
	}

	/**
	 * <p>
	 * System output format.
	 * <ul>
	 * <li>Text</li>
	 * <li>List</li>
	 * </ul>
	 * </p>
	 */
	public static enum OutputType {
		TEXT, LIST
	}

	/**
	 * <p>
	 * Phrase classification options.
	 * <ul>
	 * <li>causal</li>
	 * <li>comparative</li>
	 * <li>concessive</li>
	 * <li>conditional</li>
	 * <li>consecutive</li>
	 * <li>explicative</li>
	 * <li>restrictive</li>
	 * <li>assyndetic</li>
	 * <li>additive</li>
	 * <li>adversative</li>
	 * <li>alternative</li>
	 * <li>conclusive</li>
	 * <li>specifier</li>
	 * <li>final</li>
	 * <li>reduced</li>
	 * <li>parenthetical</li>
	 * <li>underlying</li>
	 * <li>attribution</li>
	 * </ul>
	 * 
	 */
	public static enum PhraseClassification {
		CAUSAL, COMPARATIVE, CONCESSIVE, CONDITIONAL, CONSECUTIVE, EXPLICATIVE, RESTRICTIVE, ASSYNDETIC, ADDITIVE, ADVERSATIVE, ALTERNATIVE, CONCLUSIVE, SPECIFIER, FINAL, REDUCED, PARENTHETICAL, UNDERLYING, ATTRIBUTION
	}

	/**
	 * <p>
	 * System output format.
	 * <ul>
	 * <li>Text</li>
	 * <li>List</li>
	 * </ul>
	 * </p>
	 */
	public static enum WordType {
		SINGLE_WORD, NAMED_ENTITY
	}

	/**
	 * <p>
	 * Loads preferences options.
	 * </p>
	 */
	public static void load() {
		try {
			String propertiesFileName = File.separator + "devconfig" + File.separator
					+ "simba" + File.separator + PREFS_FILE;
			Scanner s = new Scanner(new File(propertiesFileName));
			// Read preferences file
			while (s.hasNext()) {
				String line = s.nextLine();
				if (!line.startsWith("#")) {
					String[] splitted = line.split("=");
					if (splitted[0].equals("NAME"))
						NAME = splitted[1];
					else if (splitted[0].equals("HEADLINE_MAX_WORDS"))
						HEADLINE_MAX_WORDS = (new Integer(splitted[1]))
								.intValue();
					else if (splitted[0].equals("DEFAULT_COMPRESSION_RATE"))
						DEFAULT_COMPRESSION_RATE = (new Double(splitted[1]))
								.doubleValue();
					else if (splitted[0].equals("KEYWORD_SEPARATION"))
						KEYWORD_SEPARATION = splitted[1];
					else if (splitted[0].equals("EXTERNAL_TOOLS"))
						EXTERNAL_TOOLS = PATH + splitted[1] + File.separator;
					else if (splitted[0].equals("LXNER_DEV"))
						LXNER_DEV = splitted[1] + File.separator;
					else if (splitted[0].equals("LXNER"))
						LXNER = splitted[1] + File.separator + LXNER_DEV;
					else if (splitted[0].equals("LXSUITE_POS"))
						LXSUITE_POSANNOTATION = splitted[1];
					else if (splitted[0].equals("LXSUITE_CHUNK"))
						LXSUITE_CHUNKER = splitted[1];
					else if (splitted[0].equals("LXSUITE"))
						LXSUITE = splitted[1];
					else if (splitted[0].equals("INPUT_DEFAULT_LOCATION"))
						INPUT_DEFAULT_LOCATION = splitted[1];
					else if (splitted[0].equals("INPUT_DIR_LOCATION"))
						INPUT_DIR_LOCATION = splitted[1];
					else if (splitted[0].equals("OUTPUT_DEFAULT_LOCATION"))
						OUTPUT_DEFAULT_LOCATION = splitted[1];
					else if (splitted[0].equals("OUTPUT_FILE"))
						OUTPUT_FILE = OUTPUT_DEFAULT_LOCATION + splitted[1];
					else if (splitted[0].equals("CONSTITUENCY_PARSER"))
						CONSTITUENCY_PARSER = EXTERNAL_TOOLS + splitted[1]
								+ File.separator;
					else if (splitted[0].equals("PARSER_MODEL_FILE"))
						// PARSER_MODEL_FILE = CONSTITUENCY_PARSER +
						// splitted[1];
						PARSER_MODEL_FILE = splitted[1];
					else if (splitted[0].equals("STATISTICS_LOCATION")) {
						STATISTICS_LOCATION = splitted[1];
						DEMO_FILE = STATISTICS_LOCATION + DEMO_FILE;
					} else if (splitted[0].equals("STATISTICS_FILE"))
						STATISTICS_FILE = STATISTICS_LOCATION + splitted[1];
					else if (splitted[0].equals("EVALUATION_PROCEDURE"))
						EVALUATION_PROCEDURE = splitted[1].equals("TRUE");
					else if (splitted[0].equals("PARSES_LOCATION"))
						PARSES_LOCATION = splitted[1];
					else if (splitted[0].equals("WEKA_LOCATION"))
						WEKA_LOCATION = splitted[1] + File.separator;
					else if (splitted[0].equals("WEKA_CLASS_MODEL_FILE"))
						WEKA_CLASS_MODEL_FILE = splitted[1];
					else if (splitted[0].equals("WEKA_SUBTYPE_MODEL_FILE"))
						WEKA_SUBTYPE_MODEL_FILE = splitted[1];
					else if (splitted[0].equals("CONNECTIVES_FILE"))
						CONNECTIVES_FILE = splitted[1];
					else if (splitted[0].equals("WEKA_BINARY_MODEL_FILE"))
						WEKA_BINARY_MODEL_FILE = splitted[1];

					// else if (splitted[0].equals("SENTENCE_SCORE"))
					// SENTENCE_SCORE = new Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_EXTRASCORE"))
					// SENTENCE_EXTRASCORE = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_DOCUMENTSCORE"))
					// SENTENCE_DOCUMENTSCORE = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_SIMPLIFICATIONSCORE"))
					// SENTENCE_SIMPLIFICATIONSCORE = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_CLUSTERSSIZE"))
					// SENTENCE_CLUSTERSSIZE = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_FREQUENCY"))
					// SENTENCE_FREQUENCY = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_RELATIVEPOSITION"))
					// SENTENCE_RELATIVEPOSITION = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_RELATIVEPOSITIONRATIO"))
					// SENTENCE_RELATIVEPOSITIONRATIO = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_NUMBEROFKEYWORDS"))
					// SENTENCE_NUMBEROFKEYWORDS = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_KEYWORDSRATIO"))
					// SENTENCE_KEYWORDSRATIO = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_KEYWORDCLUSTERSCORE"))
					// SENTENCE_KEYWORDCLUSTERSCORE = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_SIMILARITYCLUSTERSCORE"))
					// SENTENCE_SIMILARITYCLUSTERSCORE = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_NUMBEROFNAMEDENTITIES"))
					// SENTENCE_NUMBEROFNAMEDENTITIES = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_NAMEDENTITIESRATIO"))
					// SENTENCE_NAMEDENTITIESRATIO = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_NAMEDENTITIESAVERAGESCORE"))
					// SENTENCE_NAMEDENTITIESAVERAGESCORE = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_KEYWORDSAVERAGESCORE"))
					// SENTENCE_KEYWORDSAVERAGESCORE = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_SIMILARITY2BIGGEST"))
					// SENTENCE_SIMILARITY2BIGGEST = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_NUMBEROFRELEVANTWORDS"))
					// SENTENCE_NUMBEROFRELEVANTWORDS = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_RELEVANTWORDSRATIO"))
					// SENTENCE_RELEVANTWORDSRATIO = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("SENTENCE_RELEVANTWORDSAVERAGESCORE"))
					// SENTENCE_RELEVANTWORDSAVERAGESCORE = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_SCORES_WEIGHT"))
					// SENTENCE_SCORES_WEIGHT = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_RATIOS_WEIGHT"))
					// SENTENCE_RATIOS_WEIGHT = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_NUMBERS_WEIGHT"))
					// SENTENCE_NUMBERS_WEIGHT = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("SENTENCE_OTHERS_WEIGHT"))
					// SENTENCE_OTHERS_WEIGHT = new
					// Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("WORD_TFIDF"))
					// WORD_TFIDF = new Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("WORD_EXTRASCORE"))
					// WORD_EXTRASCORE = new Double(splitted[1]).doubleValue();
					// else if (splitted[0].equals("WORD_NUMBEROFDOCS"))
					// WORD_NUMBEROFDOCS = new
					// Double(splitted[1]).doubleValue();
					// else if
					// (splitted[0].equals("WORD_SENTENCERELATIVEPOSITION"))
					// WORD_SENTENCERELATIVEPOSITION = new
					// Double(splitted[1]).doubleValue();

				}
			}

			// defineDiscourseRelations();

			s.close();
		} catch (Exception e) {
			System.out
					.println("============= ERROR in class [Preferences.load()].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	/*
	 * private static void defineDiscourseRelations() {
	 * 
	 * 
	 * List<String> subtypes = new LinkedList<String>();
	 * subtypes.add("Precedence"); subtypes.add("Succession");
	 * DISCOURSE_RELATIONS.put("Temporal/Asynchronous/", subtypes);
	 * 
	 * DISCOURSE_RELATIONS.put("Temporal/Synchronous/", new
	 * LinkedList<String>());
	 * 
	 * subtypes = new LinkedList<String>(); subtypes.add("Reason");
	 * subtypes.add("Result"); DISCOURSE_RELATIONS.put("Contingency/Cause/",
	 * subtypes);
	 * 
	 * subtypes = new LinkedList<String>(); subtypes.add("Hypothetical");
	 * subtypes.add("Factual"); subtypes.add("Contra-factual");
	 * DISCOURSE_RELATIONS.put("Contingency/Condition/", subtypes);
	 * 
	 * DISCOURSE_RELATIONS.put("Comparison/Contrast/", new
	 * LinkedList<String>());
	 * 
	 * subtypes = new LinkedList<String>(); subtypes.add("Expectation");
	 * subtypes.add("Contra-expectation");
	 * DISCOURSE_RELATIONS.put("Comparison/Concession/", subtypes);
	 * 
	 * DISCOURSE_RELATIONS.put("Expansion/Addition/", new LinkedList<String>());
	 * 
	 * DISCOURSE_RELATIONS.put("Expansion/Instantiation/", new
	 * LinkedList<String>());
	 * 
	 * subtypes = new LinkedList<String>();
	 * subtypes.add("Specification-Equivalence");
	 * subtypes.add("Generalization");
	 * DISCOURSE_RELATIONS.put("Expansion/Restatement/", subtypes);
	 * 
	 * subtypes = new LinkedList<String>();
	 * subtypes.add("Conjuntive-Disjunctive");
	 * subtypes.add("Chosen-alternative");
	 * DISCOURSE_RELATIONS.put("Expansion/Alternative/", subtypes);
	 * 
	 * DISCOURSE_RELATIONS.put("Expansion/Exception/", new
	 * LinkedList<String>()); }
	 */

	public static void setMaxKeywords(int totalWords) {
		// MAX_KEYWORDS = 50;
		Preferences.MAX_KEYWORDS = (new Double(
				Math.sqrt((double) totalWords / 2))).intValue();
	}

	public static void setFinalSummaryType(String type) {

		if (type.equals("SIMPLIFIED"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.SIMPLIFIED;
		else if (type.equals("NON-SIMPLIFIED"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.NONSIMPLIFIED;
		else if (type.equals("NON-POST-PROCESSED"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.NONPOSTPROCESSED;
		else if (type.equals("RANDOM-BASELINE"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.RANDOMBASELINE;
		else if (type.equals("SIMPLIFICATION-BASELINE"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.SIMPLIFICATIONBASELINE;
		else if (type.equals("PARAGRAPHS-BASELINE"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.PARAGRAPHBASELINE;
		else if (type.equals("CONNECTIVES-BASELINE"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.CONNECTIVESBASELINE;
		else if (type.equals("POST-PROCESS-WITHOUT-SUMMARY"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.ONLYPOSTPROCESSEDNOSUMMARY;
		else if (type.equals("REPLACECONNECTIVES"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.REPLACECONNECTIVES;
		else if (type.equals("REPLACEPARAGRAPHS"))
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.REPLACEPARAGRAPHS;
		else
			SUMMARY_TYPE = FINAL_SUMMARY_TYPE.SIMPLIFIED;
	}

	public static Hashtable<String, List<Connective>> loadAnnotatedConnectives() {

		Hashtable<String, List<Connective>> annotatedConnectives = new Hashtable<String, List<Connective>>();

		try {
			Scanner s = new Scanner(new File(CONNECTIVES_FILE));
			List<Connective> connectives = null;
			// Read preferences file
			while (s.hasNext()) {
				String line = s.nextLine(), currentClass = "";

				if (!line.startsWith("#") && !line.equals("")) {

					String[] tokens = line.split(CONNECTIVES_FILE_SPLITTER);
					currentClass = tokens[0] + Regex.ANNOTATION_SPLITTER
							+ tokens[1] + Regex.ANNOTATION_SPLITTER;
					connectives = new ArrayList<Connective>();

					// System.out.println("\n\nClass:" + currentClass);

					int totalConnectives = 0, currentMax = 0;

					while (s.hasNext()) {
						line = s.nextLine();
						if (!line.startsWith("#")) {

							if (line.equals(""))
								break;

							String[] types = line
									.split(Preferences.CONNECTIVES_FILE_SPLITTER);

							Connective connective = new Connective(types[0],
									types[1], types[2], types[3], currentClass,
									types[4]);
							connectives.add(connective);

							totalConnectives++;

							if (totalConnectives > currentMax)
								currentMax = totalConnectives;

						}

					}

					if (currentMax > MAX_CONNECTIVES)
						MAX_CONNECTIVES = currentMax;

					annotatedConnectives.put(currentClass, connectives);
				}

			}
			s.close();
		} catch (Exception e) {
			System.out
					.println("============= ERROR in class [Preferences.loadConnectives()].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return annotatedConnectives;
	}

	public static Set<String> getDiscourseRelationClasses() {

		Set<String> classes = new TreeSet<String>();

		for (int i = 0; i < DISCOURSE_RELATIONS.length; i++) {
			String[] splitted = DISCOURSE_RELATIONS[i].split("/");
			classes.add(splitted[0]);
		}

		return classes;
	}

	public static Set<String> getDiscourseBinaryClasses() {

		Set<String> classes = new TreeSet<String>();
		classes.add(WEKA_YES);
		classes.add(WEKA_NO);
		return classes;
	}

	public static Set<String> getDiscourseRelationTypes(String relation) {

		Set<String> types = new TreeSet<String>();

		for (int i = 0; i < DISCOURSE_RELATIONS.length; i++) {

			String[] splitted = DISCOURSE_RELATIONS[i].split("/");
			if (splitted[0].equals(relation))
				types.add(splitted[1]);
		}

		return types;
	}

	public static Set<String> getDiscourseRelationSubtypes(String type) {

		Set<String> subtypes = new TreeSet<String>();

		for (int i = 0; i < DISCOURSE_RELATIONS.length; i++) {
			String[] splitted = DISCOURSE_RELATIONS[i].split("/");

			if (splitted.length == 3)
				if (splitted[1].equals(type))
					subtypes.add(splitted[2]);
		}

		return subtypes;
	}

	/**
	 * Gets the class and type for the given subtype.
	 * 
	 * @param subtype
	 * @return
	 */
	public static String getClassTypeForSubtype(String subtype) {

		String classType = "";

		boolean found = false;

		for (int i = 0; i < DISCOURSE_RELATIONS.length; i++) {
			String[] splitted = DISCOURSE_RELATIONS[i].split("/");

			if (splitted.length == 3) {
				if (splitted[2].equals(subtype)) {
					classType = splitted[0] + "/" + splitted[1] + "/";
					found = true;
				}
			}

			if (!found) {
				if (splitted.length == 2) {
					if (splitted[1].equals(subtype)) {
						classType = splitted[0] + "/" + splitted[1] + "/";
						found = true;
					}
				}
			}

			if (found)
				break;
		}

		return classType;
	}

}
