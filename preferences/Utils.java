package preferences;


import java.io.IOException;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import summarization.Summary;

import aux.SentenceCluster;
import aux.WordCluster;

import core.Connective;
import core.Document;
import core.ManageTrees;
import core.NamedEntity;
import core.Sentence;
import core.SingleWord;
import core.Word;
import edu.stanford.nlp.ling.StringLabelFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreeReader;
import edu.stanford.nlp.trees.tregex.ParseException;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

/**
 * <p>
 * Manages methods common to different parts of the processing.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Utils {

	/**
	 * <p>
	 * Computes the similarity between the two given sentences.
	 * </p>
	 * 
	 * @param s1
	 *            the first sentence to be compared.
	 * @param s2
	 *            the second sentence to be compared.
	 * @return the similarity value between the two sentences.
	 */
	// @# EM ABERTO #@
	// The similarity concept used here includes two dimensions:
	// 1. The word sentence overlap; (common words shared by the two sentences)
	// 2. The word biggest subsequence common to both sentences (inspired by
	// ROUGE-L);
	public static double computeSentence2SentenceSimilarity(Sentence first, Sentence second) {
		// // DEBUG
//		 System.out.println("\n[Sentence] #1 " + s1.getSentence() +
//		 "\n#2 " + s2.getSentence());
		
		
		// (2013-08-12)
		//  Due to the way the cycle that computes the subsequences and the overlap is done,
		// the second sentence must be the biggest one.
		Sentence s1 = first, s2 = second;
		
		if (first.getTotalWords() > second.getTotalWords()){
			s2 = first;
			s1 = second;
		}
		
		
		LinkedList<Word> currentWords = new LinkedList<Word>(s1.getWords());
		LinkedList<Word> submittedWords = new LinkedList<Word>(s2.getWords());

		double avSubsequences = 0, newOverlap = 0;

		// Computes common subsequences between the two sentences.
		LinkedList<Integer> subsequences = new LinkedList<Integer>();

		int i = 0, j = 0, subsequence = 0;
		for (; i < s1.getTotalWords(); i++) {
			SingleWord currentWord = (SingleWord) currentWords.get(i);

			for (j = subsequence; j < s2.getTotalWords(); j++) {
				SingleWord submittedWord = (SingleWord) submittedWords.get(j);
				
				if (currentWord.represents(submittedWord)) {
					subsequence = 0;
					
					while (((i + subsequence) < s1.getTotalWords() && (j + subsequence) < s2.getTotalWords())
							&& currentWord.represents(submittedWord)) {

						currentWord = (SingleWord) currentWords.get(i + subsequence);
						submittedWord = (SingleWord) submittedWords.get(j + subsequence);
						
//						 System.out.println("[Utils] " +
//						 "[c_"+(i+subsequence)+"] "+currentWord.getOriginal()
//						 +
//						 "\t[s_"+(j+subsequence)+"] "+submittedWord.getOriginal());
//						  DEBUG

						if (currentWord.represents(submittedWord))
							subsequence++;
					}
					// subsequence--;
//					System.out.println("\tsubsequence: " + subsequence);
					if (subsequence > 1) {

						subsequences.add(new Integer(subsequence));
						// i += (subsequence-1);
						i += subsequence;
						
						// avSubsequences += subsequence;// / biggestSentence);
//						 System.out.println("[Sentence] " +
//						 "Subsequence: "+subsequence+"\tavgSub_"+avSubsequences);
						// //DEBUG
					} else
						i++;
					
//					newOverlap += subsequence;
				}
			}
		}
		
		double overlap = 0;

		if (subsequences.size() == 0)
			avSubsequences = 0;
		else {
			for (Integer subseq : subsequences) {
				double sequence = (double) subseq.intValue();

				avSubsequences += ((sequence / s1.getTotalWords()) + (sequence / s2.getTotalWords()));// / biggestSentence;
				// avSubsequences += (sequence / (this.totalWords +
				// sentence.getTotalWords()));
				
//				System.out.println("---- "+subsequence.intValue());
			}

			if (subsequences.size() == 1)
				avSubsequences = avSubsequences / 2;
			else
				avSubsequences = avSubsequences / subsequences.size();
		}
		
		
		int commonWords = 0;
		
		for (i = 0; i < s1.getTotalWords(); i++) {
			SingleWord currentWord = (SingleWord) currentWords.get(i);

			for (j = 0; j < s2.getTotalWords(); j++) {
				SingleWord submittedWord = (SingleWord) submittedWords.get(j);
				
				if (currentWord.represents(submittedWord)) {
					commonWords ++;
					break;
				}
			}
		}
		
		// (2013-08-15)
		// The overlap value was being computed in some other way.
		// It is the common words between the two sentences. 
		// (the previous algorithm would not recognize "O João ama a Maria" and "A Maria ama o João" 
		// has having 5 common words.)
		overlap = commonWords;
		
		// (deprecated)
		// Now, the overlap value is computed by considering the sum of all the subsequences 
		// found between s1 and s2 (including the subsequences with length 1).
		// overlap = newOverlap;
		
		
		
/**		
//		int i = 0, j = 0;
//		for (; i < s1.getTotalWords(); i++) {
//			SingleWord currentWord = (SingleWord) currentWords.get(i);
//
//			for (j = i; j < s2.getTotalWords(); j++) {
//				SingleWord submittedWord = (SingleWord) submittedWords.get(j);
//
//				if (currentWord.represents(submittedWord)) {
//					int subsequence = 0;
//
//					while (((i + subsequence) < s1.getTotalWords() && (j + subsequence) < s2.getTotalWords())
//							&& currentWord.represents(submittedWord)) {
//
//						currentWord = (SingleWord) currentWords.get(i + subsequence);
//						submittedWord = (SingleWord) submittedWords.get(j + subsequence);
//						
////						 System.out.println("[Sentence] " +
////						 "[c_"+(i+subsequence)+"] "+currentWord.getOriginal()
////						 +
////						 "\t[s_"+(j+subsequence)+"] "+submittedWord.getOriginal());
//						 // DEBUG
//
//						if (currentWord.represents(submittedWord))
//							subsequence++;
//					}
//					// subsequence--;
//
//					if (subsequence > 1) {
//
//						subsequences.add(new Integer(subsequence));
//						// i += (subsequence-1);
//						i += subsequence;
//						
//						// avSubsequences += subsequence;// / biggestSentence);
////						 System.out.println("[Sentence] " +
////						 "Subsequence: "+subsequence+"\tavgSub_"+avSubsequences);
//						// //DEBUG
//					} else
//						i++;
//					
////					if (subsequence == 1)
////						newOverlap ++;
//					
//					newOverlap += subsequence;
//				}
//			}
//		}

		// if (subsequences.size() > 0){
		// avSubsequences = avSubsequences / (double)subsequences.size();
		// // avSubsequences = avSubsequences / biggestSentence;
		// }

		// if (subsequences.size() > 0){
		// Collections.sort(subsequences);
		// avSubsequences = (double)subsequences.getLast().intValue() /
		// biggestSentence;
		// }
		// Computes the total overlapping words (considering repetitions)
		// between the two sentences.
		HashMap<String, Integer> overlapWords = new HashMap<String, Integer>();

		for (int k = 0; k < s1.getTotalWords(); k++) {
			SingleWord currentWord = (SingleWord) currentWords.get(k);
			String key = currentWord.representation();//.getWord().toLowerCase();

			for (int l = 0; l < s2.getTotalWords(); l++) {
				SingleWord submittedWord = (SingleWord) submittedWords.get(l);
//System.out.println("\n\ns1_"+currentWord+"\ns2_"+submittedWord);
//System.out.println("\tequals? "+(currentWord.equals(submittedWord))+"\trepresents?"+currentWord.represents(submittedWord)+"\tcontains?"+overlapWords.containsKey(key));
				if (!currentWord.equals(submittedWord)
//						if (!currentWord.equals(submittedWord) &&
						&& currentWord.represents(submittedWord)) {
					if (overlapWords.containsKey(key)) {
						int count = overlapWords.remove(key);
						count++;
						overlapWords.put(key, new Integer(count));
//						System.out.println("\t\tkey_"+key+"_"+count);
					} else
						overlapWords.put(key, new Integer(1));
					
				}
//				else{
//					System.out.println("\t\tvenho pra este blheck?!");
//				}

			}
		}
		
//		Set<Map.Entry<String, Integer>> set1 = overlapWords.entrySet();
//		
//		for (Entry<String, Integer> entry : set1) {
//			 
//			double count = (double) entry.getValue();
//			
//			System.out.println("[Sentence] key: " +entry.getKey()+" count_"+count);
//		}

		// Computes the words repeated in each sentence.
		HashMap<String, Integer> repeatedFirstSentence = computeRepetitions(currentWords);
		HashMap<String, Integer> repeatedSecondSentence = computeRepetitions(submittedWords);

		// Computes the overlapping words between the two sentences, without
		// considering repetitions.
		// int overlap = 0;
		Set<Map.Entry<String, Integer>> set = overlapWords.entrySet();

		for (Entry<String, Integer> entry : set) {
			Integer firstKey = repeatedFirstSentence.get(entry.getKey()), secondKey = repeatedSecondSentence.get(entry.getKey());
			// System.out.print("[Sentence] key: " +entry.getKey());
			double count = (double) entry.getValue();

			if (firstKey != null && secondKey != null) {
//				 System.out.print("\ts1_Reps: "+firstKey.intValue()+"\ts2_Reps:_"+secondKey.intValue());
				// // DEBUG
				count = Math.min(firstKey, secondKey);
			}

//			 System.out.println("\toriginalOverlap: "+entry.getValue().intValue()+"\tnewOverlap: "+new
//			 Double(count).intValue()); // DEBUG
			entry.setValue(new Integer((new Double(Math.round(count))).intValue()));
			overlap += entry.getValue().intValue();
		}
**/
		
		// The overlap value is computed using the Jaccard index.
		// The subsequences value is computed considering the average of the
		// subsequences in the sentence.
		double finalOverlap = ((double) overlap / ((double) s2.getTotalWords() + (double) s1.getTotalWords() - overlap));
		double finalSubsequences = ((double) avSubsequences);
		
		// The similarity value retrieved consists of the average of the two
		// similarity measures, if this value
		// is above the threshold; otherwise, it consists of the subsequences
		// value, since we consider it most
		// revelant.
		double similarity = (finalSubsequences + finalOverlap) / 2;
		
		// (2013-08-12)
		// Rounding the similarity to two decimal places to be evenly compared with the threshold.
		// (this way 0.7455 equals to 0.75)
		similarity = Math.round(similarity * 100);
		similarity = similarity / 100;

		/*
		// DEBUG
////		if (similarity >= Preferences.SENTENCE_SIMILIARITY_THRESHOLD){
		System.out.println("\nS1_"+s1.getSentence());
////		System.out.println("\nS1_"+s1);
		System.out.println("S2_"+s2.getSentence());
////		System.out.println("S2_"+s2);
		System.out.println("\t"+
		 "sim_"+similarity+"\tfinalOverlap_"+(finalOverlap)+"\tfinalSubseq_"+finalSubsequences+"\toverlap_"+overlap+"\ts2_"+s2.getTotalWords()+"\ts1_"+s1.getTotalWords());
////		}
 * */
		return similarity;
	}

	/**
	 * <p>
	 * Computes the words repeated in the same sentence.
	 * </p>
	 * 
	 * @param words
	 *            the list of words to be checked.
	 * @return an HashMap which key is the word and its value is the number of
	 *         times the words are repeated in the sentence.
	 */
	private static HashMap<String, Integer> computeRepetitions(
			LinkedList<Word> words) {
		HashMap<String, Integer> repetitions = new HashMap<String, Integer>();

		for (int i = 0; i < words.size(); i++) {
			SingleWord currentWord = (SingleWord) words.get(i);
			String key = currentWord.getWord().toLowerCase();

			if (!repetitions.containsKey(key))
				repetitions.put(key, new Integer(1));
			else {
				int count = repetitions.remove(key);
				count++;
				repetitions.put(key, new Integer(count));
			}
		}

		return repetitions;
	}

	/**
	 * <p>
	 * Selects the keywords from the given collection of sentences.
	 * </p>
	 * 
	 * @param sentences
	 *            the sentences to get the keywords.
	 * 
	 * @return the collection of keywords for the given sentences.
	 */
	public static Collection<Word> extractKeywords(
			Collection<Sentence> sentences) {

		LinkedList<Word> allWords = new LinkedList<Word>();
		LinkedList<Word> words = new LinkedList<Word>();
		LinkedList<Word> namedEntities = new LinkedList<Word>();
		LinkedList<Word> keywords = new LinkedList<Word>();

		for (Sentence sentence : sentences) {
			namedEntities.addAll(sentence.getNamedEntities());
			allWords.addAll(sentence.getWords());
		}

		namedEntities = new LinkedList<Word>(filterNamedEntities(namedEntities));

		Iterator<Word> iWords = allWords.iterator();
		while (iWords.hasNext()) {

			Word keyword = null;
			SingleWord currentWord = (SingleWord) iWords.next();

			if (!currentWord.toSkip()) {
				if (currentWord.toUse()) {
					// NamedEntity ne =
					// Utils.getContainingNamedEntity(namedEntities,
					// currentWord);
					//					
					// if (ne == null)
					keyword = currentWord;
					// else{
					// if (ne.toUse())
					// keyword = ne;
					// }
				}
			}

			if (keyword != null && !isWordInCollection(words, keyword))// &&
																		// !isInCollection(neKeywords,
																		// keyword))
				words.add(keyword);
		}

		Collections.sort(words, Preferences.COMPARE_WORD_SCORE);

//		LinkedList<Word> keywordsWithNamedEntities = new LinkedList<Word>(words);
		// sara@2013-08-05 deixou-se de usar as NEs como keywords por um tf.idf nelas não ser computável.
//		LinkedList<Word> keywordsWithNamedEntities = new LinkedList<Word>();
//
//		for (Word keyword : words) {
//			NamedEntity ne = Utils.getContainingNamedEntity(namedEntities,
//					keyword);
//
//			if (ne != null
//					&& !isWordInCollection(keywordsWithNamedEntities, ne)
//					&& !isWordInCollection(keywordsWithNamedEntities, keyword))
//				keywordsWithNamedEntities.add(ne);
//			else if (ne == null)
//				keywordsWithNamedEntities.add(keyword);
//		}
//		
//		Collections.sort(keywordsWithNamedEntities, Preferences.COMPARE_WORD_SCORE);

		// From the sorted collection of words, we'll only keep the predefined
		// number of keywords.
		for (int i = 0; i < Preferences.MAX_KEYWORDS
				&& i < words.size(); i++) {
			Word keyword = words.get(i);
			SingleWord newKeyword = new SingleWord((SingleWord)keyword);
			newKeyword.updateExtraScore(Preferences.KEYWORD_SCORE);
			keywords.add(newKeyword);
		}
		
		Collections.sort(keywords, Preferences.COMPARE_WORD_SCORE);

		return keywords;
	}
	
	
	
	public static Collection<Word> extractKeywordsWithoutNEs(
			Collection<Sentence> sentences) {

		LinkedList<Word> allWords = new LinkedList<Word>();
		LinkedList<Word> words = new LinkedList<Word>();
//		LinkedList<Word> namedEntities = new LinkedList<Word>();
		LinkedList<Word> keywords = new LinkedList<Word>();

		for (Sentence sentence : sentences) {
//			namedEntities.addAll(sentence.getNamedEntities());
			allWords.addAll(sentence.getWords());
		}

//		namedEntities = new LinkedList<Word>(filterNamedEntities(namedEntities));

		Iterator<Word> iWords = allWords.iterator();
		while (iWords.hasNext()) {

			Word keyword = null;
			SingleWord currentWord = (SingleWord) iWords.next();

			if (!currentWord.toSkip()) {
				if (currentWord.toUse()) {
					// NamedEntity ne =
					// Utils.getContainingNamedEntity(namedEntities,
					// currentWord);
					//					
					// if (ne == null)
					keyword = currentWord;
					// else{
					// if (ne.toUse())
					// keyword = ne;
					// }
				}
			}

			if (keyword != null && !isWordInCollection(words, keyword))// &&
																		// !isInCollection(neKeywords,
																		// keyword))
				words.add(keyword);
		}

		Collections.sort(words, Preferences.COMPARE_WORD_SCORE);

//		LinkedList<Word> keywordsWithNamedEntities = new LinkedList<Word>();
//
//		for (Word keyword : words) {
//			NamedEntity ne = Utils.getContainingNamedEntity(namedEntities,
//					keyword);
//
//			if (ne != null
//					&& !isWordInCollection(keywordsWithNamedEntities, ne)
//					&& !isWordInCollection(keywordsWithNamedEntities, keyword))
//				keywordsWithNamedEntities.add(ne);
//			else if (ne == null)
//				keywordsWithNamedEntities.add(keyword);
//		}
//		
//		Collections.sort(keywordsWithNamedEntities, Preferences.COMPARE_WORD_SCORE);

		// From the sorted collection of words, we'll only keep the predefined
		// number of keywords.
		for (int i = 0; i < Preferences.MAX_KEYWORDS
				&& i < words.size(); i++) {
			Word keyword = words.get(i);
			// Word keyword = words.get(i);
			// keyword.addExtraScore(Preferences.WORD_EXTRA_SCORE_DEFAULT_VALUE);
			// // É feito depois deste método acabar para todas as palavras
			keywords.add(keyword);
		}
		
		Collections.sort(keywords, Preferences.COMPARE_WORD_SCORE);

		return keywords;
	}

	/**
	 * <p>
	 * Filters all the possible named entities, and groups them into a
	 * collection of unique entities.
	 * </p>
	 * 
	 * @param namedEntities
	 *            the original collection of named entities.
	 * @return the filtered collection of named entities.
	 */
	public static Collection<Word> filterNamedEntities(Collection<Word> namedEntities) {

		LinkedList<Word> ordered = new LinkedList<Word>();
		LinkedList<Word> values = new LinkedList<Word>();
		HashMap<String, WordCluster> grouped = groupSameWords(namedEntities);

		for (WordCluster wc : grouped.values())
			values.add((Word) wc.getCentroid());

		Collections.sort(values, Preferences.COMPARE_WORD_SCORE);

		for (Word firstWord : values) {
			boolean found = false;

			for (Word secondWord : values) {

				if (!firstWord.equals(secondWord)) {

					double commonWords = ((NamedEntity) firstWord).commonWords(((NamedEntity) secondWord));
					double percentage = commonWords
							/ (Math.max(((NamedEntity) firstWord).getEntityWords().size(),
									((NamedEntity) secondWord).getEntityWords().size()));

					if (percentage >= Preferences.NAMED_ENTITIES_SIMILARITY_PERCENTAGE) {
						boolean secondIsBigger = 
							((NamedEntity) firstWord).getEntityWords().size() <= ((NamedEntity) secondWord).getEntityWords().size();

						if (secondIsBigger)
							if (!ordered.contains(secondWord))
								ordered.add(secondWord);
							else if (!ordered.contains(firstWord))
								ordered.add(firstWord);

						found = true;
						break;
					}
				}
			}

			if (!found && !ordered.contains(firstWord))
				ordered.add(firstWord);
		}

		return ordered;
	}

	/**
	 * <p>
	 * Groups the words that are the same, but that occur in different
	 * sentences.
	 * </p>
	 * 
	 * @param words
	 *            the collection of words to be grouped.
	 * @return the clustered words (considered the same).
	 */
	public static HashMap<String, WordCluster> groupSameWords(Collection<Word> words) {
		HashMap<String, WordCluster> grouped = new HashMap<String, WordCluster>();

		for (Word word : words) {
			NamedEntity ne = (NamedEntity) word;

			if (grouped.containsKey(ne.representation())) {

				WordCluster cluster = grouped.remove(ne.representation());
				cluster.addValue(ne);
				grouped.put(ne.representation(), cluster);
			} else {
				WordCluster cluster = new WordCluster(ne);
				cluster.addValue(ne);
				grouped.put(ne.representation(), cluster);
			}
		}
		return grouped;
	}

	/**
	 * <p>
	 * Updates scores of words which are global keywords.
	 * </p>
	 * 
	 * @param sentenceWords
	 *            the collection of words in the sentence.
	 * @param keywords
	 *            the collection of keywords.
	 * 
	 * @return the updated collection of words.
	 */
	public static Collection<Word> updateKeywordsScore(Collection<Word> sentenceWords, Collection<Word> keywords) {

		Collection<Word> words = new LinkedList<Word>(sentenceWords);

		for (Word word : words) {

			double score = word.getExtraScore();

			if (word.isKeyword(keywords))
				score += Preferences.KEYWORD_SCORE;

			word.updateExtraScore(score);
		}

		return words;
	}
	
	public static Collection<Word> updateWordsTFIDF(Collection<Word> allWords, Collection<Word> filteredWords) {

		Collection<Word> words = new LinkedList<Word>(allWords);

		for (Word filtered : filteredWords){
				
			for (Word word : words) {
				if (filtered.represents(word))
					word.setTFIDF(filtered.getTFIDF());
			}
		}

		return words;
	}

	/**
	 * <p>
	 * Checks if the given word is in the collection.
	 * </p>
	 * 
	 * @param words
	 *            the collection of words to be searched;
	 * @param word
	 *            the word to be verified.
	 * 
	 * @return true if the collection contains the word; false otherwise.
	 */
	public static boolean isWordInCollection(Collection<Word> words, Word word) {

		return Utils.retrieveWordFromCollection(words, word) != null;
	}

	/**
	 * <p>
	 * Retrieves from the collection the word referring to the given word.
	 * </p>
	 * 
	 * @param words
	 *            the collection of words to be searched;
	 * @param word
	 *            the word to be verified;
	 * 
	 * @return the Word in the collection; null if it does not exist.
	 */
	public static Word retrieveWordFromCollection(Collection<Word> words,
			Word word) {

		boolean found = false;
		Word wordFromCollection = null;

//		 System.out.println("\n\n=========> "+word.representation());
		for (Word currentWord : words) {

			if (currentWord instanceof SingleWord && word instanceof SingleWord)
				found = ((SingleWord) currentWord).equals((SingleWord) word)
						|| ((SingleWord) currentWord).represents((SingleWord) word)
						|| ((SingleWord) currentWord).same((SingleWord) word);

			else if (currentWord instanceof NamedEntity && word instanceof NamedEntity)
				found = ((NamedEntity) currentWord).equals((NamedEntity) word)
						|| ((NamedEntity) currentWord).represents((NamedEntity) word)
						|| ((NamedEntity) currentWord).same((NamedEntity) word);

			else if (currentWord instanceof NamedEntity && word instanceof SingleWord)
				found = ((NamedEntity) currentWord).containsWord((SingleWord) word);

//			 System.out.println("found_"+found+"\t"+currentWord.representation());
			if (found) {
				wordFromCollection = currentWord;
				break;
			}

			// else if (currentWord instanceof NamedEntity && word instanceof
			// NamedEntity){
			//				
			// if (currentWord.equals(word)
			// || currentWord.represents(word)){
			// found = true;
			// break;
			// }
			// }
		}

		return wordFromCollection;
	}

	/**
	 * <p>
	 * Retrieves a string word from a collection of words.
	 * </p>
	 * 
	 * @param words
	 *            the collection of words to be searched;
	 * @param word
	 *            the word to be verified;
	 * 
	 * @return the Word in the collection; null if it does not exist.
	 */
	public static Word retrieveWordFromCollection(Collection<Word> words, String word) {
		SingleWord current = new SingleWord(word, word, word, "");
		return retrieveWordFromCollection(words, current);
	}

	/**
	 * <p>
	 * Computes the number of documents where the given word appears.
	 * </p>
	 * 
	 * @param word
	 *            the word to be searched.
	 * 
	 * @return the number of documents where the given word occurs.
	 */
	public static int numberOfTermInDocuments(Collection<Document> documents, Word word) {

		int total = 0;

		for (Document document : documents) {
			if (document.getText().containsTerm(word))
				total ++;
//				total += document.getText().numberContainsTerm(word);
				
		}

		return total;
	}
	
	/**
	 * <p>
	 * Converts the complete tree into a printable tree.
	 * </p>
	 * 
	 * @param tree
	 *            the tree to be converted.
	 * 
	 * @return a cleaned tree.
	 */
	public static Tree convertTree2Search(Tree tree) {

		Tree parse = tree;
		try {
			// Constructs a new tree printed in one line and without extra
			// labels in the nodes.
			TreePrint tp = new TreePrint("oneline");
			Writer w = new StringWriter();
			tp.setPrintWriter(new PrintWriter(w));
			tp.printTree(parse);
			TreeReader reader = new PennTreeReader(new StringReader(w.toString()), new LabeledScoredTreeFactory(new StringLabelFactory()));
			parse = reader.readTree();
		} catch (IOException e) {
			System.out
					.println("============= ERROR in class [Utils.convertTree2Search].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return parse;
	}

	/**
	 * @deprecated 
	 * <p>
	 * Applies the string pattern given to the sentence parse tree.
	 * </p>
	 * <p>
	 * Retrieves a matcher containing all matches of the pattern in the parse tree.
	 * </p>
	 * 
	 * @param pattern the string pattern to be applied.
	 * @param parseTree the Tree in which the pattern will be applied.
	 * @return the TregexMatcher containing all matches of the pattern in the tree.
	 * 
	 * @throws ParseException
	 */
	public static TregexMatcher applyPattern(String pattern, Tree parseTree)
			throws ParseException {
		TregexPattern tregex = TregexPattern.compile(pattern);
		return tregex.matcher(parseTree);
	}

	/**
	 * <p>
	 * Retrieves a string containing the tree leafs.
	 * </p>
	 * (debug method)
	 * 
	 * @param tree
	 *            the tree to be printed.
	 * 
	 * @return a String containing the tree leafs.
	 */
	public static String printTreeLeaves(Tree tree) {

		String leafs2string = "";

		if (tree != null) {
			for (Tree leaf : tree.getLeaves())
				leafs2string += ManageTrees.getLeafNode(leaf.nodeString())
						+ " ";
		} else
			leafs2string = "NULL TREE";

		return leafs2string;
//		return tree.toString();
	}

	/**
	 * <p>
	 * Counts how many words from the collection are in the sentence.
	 * </p>
	 * 
	 * @param words
	 *            the collection of words.
	 * @param sentence
	 *            the sentence.
	 * 
	 * @return the number of words in the collection and in the sentence.
	 */
	public static int countWordsInSentence(Collection<Word> words,
			Sentence sentence) {

		int count = 0;

		for (Word sentenceWord : sentence.getWords()) {
			for (Word word : words) {
				if (word.represents(sentenceWord))
					count++;
			}
		}

		// // DEBUG
		// System.out.println("\n\n"+sentence.getSentence()+"\t"+count);
		// for (Word word : words) {
		// System.out.println("\t"+word.getWord());
		// }

		return count;
	}
	
	public static double getKeywordsScore(Collection<Word> words, Sentence sentence) {

		int numK = 0;//Utils.countKeywordsInSentence(words, sentence);
		double count = 0;

		for (Word sentenceWord : sentence.getWords()) {
			for (Word word : words) {
				if (word.represents(sentenceWord)){
					count += word.getScore();
					numK ++;
				}
			}
		}
		
//		count = count / numK;

		// // DEBUG
		// System.out.println("\n\n"+sentence.getSentence()+"\t"+count);
		// for (Word word : words) {
		// System.out.println("\t"+word.getWord());
		// }

		return count;
	}

	/**
	 * <p>
	 * Starts the time counting.
	 * </p>
	 */
	public static long startCountingTime() {
		return System.currentTimeMillis();
	}

	/**
	 * <p>
	 * Ends the time counting and prints the execution time.
	 * </p>
	 */
	public static void endCountingTime(long start) {

		long endTime = System.currentTimeMillis(), execution = endTime - start;

		DateFormat df = new SimpleDateFormat("HH:mm:ss:SS");
		df.setTimeZone(TimeZone.getTimeZone("GMT+0"));

		System.out.println("\tTime: " + df.format(new Date(execution)));
	}

	/**
	 * TODO later!
	 * <p>
	 * Extracts the keywords submitted from the query.
	 * </p>
	 * 
	 * @param query
	 *            , the query to extract the keywords
	 * 
	 * @return a <code>String[]</code> containing the query keywords.
	 */
	public static Collection<Word> extractKeywords(String query) {
		return null;
	}

	/**
	 * <p>
	 * Converts a double number representing three decimal places.
	 * </p>
	 * 
	 * @param number
	 *            number to be converted.
	 * @return String containing the converted number.
	 */
	public static String formatNumber(double number) {
		DecimalFormat formatter = new DecimalFormat("0.000");
		return formatter.format(number);
	}

	/**
	 * <p>
	 * Extracts the Named Entities in the sentence.
	 * </p>
	 * 
	 * @param neSentence
	 *            sentence annotated with named entities.
	 * @return the collection of named entities in the sentence.
	 */
	public static Collection<Word> extractEntities(Sentence sentence) {

		Collection<Word> namedEntities = new LinkedList<Word>();
		Collection<SingleWord> words = new LinkedList<SingleWord>();
		String pattern = "<NAMEX TYPE=\"(...)\">(.*?)</NAMEX>";
		Matcher ma = Pattern.compile(pattern).matcher(sentence.getNeAnnotation());

		while (ma.find()) {

			String type = ma.group(1), entity = ma.group(2);
			String commaSplitter = "(?:" + Regex.LEFT_SPACE + ")?"
					+ Regex.COMMA + Regex.RIGHT_SPACE
					+ Regex.ANNOTATION_SPLITTER + Regex.PUNCTUATION;

			// Correction of NERs error when it groups two entities separated by
			// a comma.
			if (entity.trim().matches(".+?" + commaSplitter + ".+")) {
				String[] entities = entity.split(commaSplitter);
				words = buildNamedEntityWords(entities[0], sentence.getWords());

				if (words.size() > 0) {
					NamedEntity ne = new NamedEntity(type, words);
//					ne.computeScore();
//					ne.computeTFIDF();
					namedEntities.add(ne);
				}

				words = buildNamedEntityWords(entities[1], sentence.getWords());

				if (words.size() > 0) {
					NamedEntity ne = new NamedEntity(type, words);
//					ne.computeScore();
//					ne.computeTFIDF();
					namedEntities.add(ne);
				}
			} else {

				words = buildNamedEntityWords(entity, sentence.getWords());

				if (words.size() > 0) {
					NamedEntity ne = new NamedEntity(type, words);
//					ne.computeScore();
//					ne.computeTFIDF();
					namedEntities.add(ne);
				}
			}
		}

		return namedEntities;
	}

	/**
	 * <p>
	 * Builds a collection of words considering the string passage and the
	 * sentence words collection.
	 * </p>
	 * 
	 * @param passage
	 *            the passage containing the named entity words.
	 * @param words
	 *            all the words in the sentence.
	 * @return the collection of words in the passage.
	 */
	private static Collection<SingleWord> buildNamedEntityWords(String passage, Collection<Word> words) {

		Collection<SingleWord> namedEntityWords = new LinkedList<SingleWord>();
		String[] tokens = passage.split(" +");

		LinkedList<Word> sentenceWords = new LinkedList<Word>(words);

		int contractions = Regex.numberOfExtraTokens(passage), 
		      totalWords = tokens.length - contractions;

		for (int i = 0; i < sentenceWords.size(); i++) {
			SingleWord sWord = (SingleWord) sentenceWords.get(i);
			String token = Regex.getToken(tokens[0]), 
			      lemmas = Regex.getLemma(tokens[0]), 
			  annotation = Regex.getAnnotation(tokens[0]);

			SingleWord firstWord = new SingleWord(token, token, lemmas, annotation);

			if (firstWord.represents(sWord)) {

				for (int j = 0; j < totalWords
						&& (j + i) < sentenceWords.size(); j++) {
					SingleWord word = (SingleWord) sentenceWords.get(j + i);
					namedEntityWords.add((SingleWord) word);
				}

				break;
			}
		}

		// // DEBUG
		// System.out.println("\n\nWORDS_FROM_NAMED_ENTITY_"+passage+"\t"+contractions);
		// for (Word string : namedEntityWords) {
		// System.out.println(string);
		// }

		return namedEntityWords;
	}

	/**
	 * <p>
	 * Corrects a bug in LX-NER annotation that puts a NAMEX tag before the
	 * sentence annotation tags (
	 * <p>
	 * and <s>).
	 * </p>
	 * 
	 * @param nerAnnotated
	 *            the String to be corrected.
	 * @return the corrected <code>String</code>.
	 */
	public static String correctInitialEntities(String nerAnnotated) {

		String corrected = nerAnnotated;

		String pattern = "^(<NAMEX TYPE=\"...\">)(<p> <s>) ";
		Matcher ma = Pattern.compile(pattern).matcher(corrected);

		while (ma.find()) {
			String namex = ma.group(1), sentenceTags = ma.group(2);
			corrected = (corrected.replaceFirst(pattern, "")).trim();
			corrected = sentenceTags + " " + namex + corrected;
		}

		return corrected;
	}

	/**
	 * <p>
	 * Checks if the given word is contained in the current named entity.
	 * </p>
	 * 
	 * @param namedEntity
	 *            the named entity to be checked.
	 * @return true if the given word is part of the current named entity; false
	 *         otherwise.
	 */
	public static NamedEntity getContainingNamedEntity(Collection<Word> namedEntities, Word word) {
		NamedEntity ne = null;

		Iterator<Word> itNamedEntities = namedEntities.iterator();

		// System.out.println("\n\n\n---> "+word);
		if (word instanceof SingleWord) {

			double higherPercentage = 0;

			while (itNamedEntities.hasNext()) {
				NamedEntity current = (NamedEntity) itNamedEntities.next();

				if (current.containsWord(word)) {

					double percentage = 1.0 / (double) current.getEntityWords().size();

					if (percentage > higherPercentage) {
						higherPercentage = percentage;
						ne = current;
					}
				}
			}
		}

		// System.out.println("\t"+ne);

		return ne;
	}

	/**
	 * <p>
	 * Counts the number of keywords in the sentence.
	 * </p>
	 * 
	 * @param sentence
	 *            the sentence in which will be counted the words.
	 * @return the number of keywords in the given sentence.
	 */
	public static int countKeywordsInSentence(Collection<Word> keywords, Sentence sentence) {
		int number = 0;

		for (Word keyword : keywords) {

			number += sentence.computeKeywordOccurrences(keyword);

			// if (keyword instanceof SingleWord){
			// SingleWord singleWord = (SingleWord) keyword;
			// if (sentence.containsWord(singleWord))
			// number ++;
			// }
			// else if (keyword instanceof NamedEntity){
			// Collection<SingleWord> namedEntitiesKeywords = ((NamedEntity)
			// keyword).getEntityWords();
			// // NamedEntity neKeyword = (NamedEntity) keyword;
			//				
			// for (SingleWord neWord : namedEntitiesKeywords)
			// for (Word word : sentence.getWords())
			// if (neWord.represents(word))
			// number ++;
			//					
			//				
			// // for (Word neWord : sentence.getNamedEntities().get)
			// // if (neWord.same(neKeyword))
			// // number ++;
			// }
		}

		return number;
	}

	/**
	 * <p>
	 * Computes the average score of the keywords that are in the given
	 * sentence.
	 * </p>
	 * 
	 * @param keywords
	 *            the collection of keywords.
	 * @param sentence
	 *            the sentence to be considered.
	 * @return the average of the scores of the keywords occurring in the
	 *         sentence.
	 */
	public static double keywordsInSentenceAverageScore(Collection<Word> keywords, Sentence sentence) {

		double averageScore = 0;
		// int totalKeywords = 0;

		for (Word keyword : keywords) {
			int occurrences = sentence.computeKeywordOccurrences(keyword);
			// totalKeywords += occurrences;

			// if (sentence.containsWord(keyword))
			if (occurrences > 0)
				averageScore += keyword.getScore();
		}

		if (sentence.getNumberOfKeywords() > 0)
			averageScore = averageScore / sentence.getNumberOfKeywords();// totalKeywords;

		return averageScore;
	}

	/**
	 * <p>
	 * Computes the average score of the named entities that are in the given
	 * sentence.
	 * </p>
	 * 
	 * @param sentence
	 *            the sentence to be considered.
	 * @return the average of the scores of the named entities occurring in the
	 *         sentence.
	 */
	public static double namedEntitiesInSentenceAverageScore(Sentence sentence) {

		double averageScore = 0;

		for (Word ne : sentence.getNamedEntities())
			averageScore += ne.getScore();

		if (sentence.getNamedEntities().size() > 0)
			averageScore = averageScore / sentence.getNumberOfNamedEntities();

		return averageScore;
	}

	public static Sentence mergeSentences(Tree complete, Sentence main, Sentence toComplete) {
		
//		System.out.println("\n\n\n\nFRASE_"+main.getSentence());
//		main.printPhrases();
		
		LinkedList<Word> words = new LinkedList<Word>(main.getWords());
		words.add(new SingleWord("e", "e", "", "CJ"));
		words.addAll((LinkedList<Word>)toComplete.getWords());
		String sentence = "";
		
		for (Word word : words)
			sentence += ((SingleWord)word).getOriginal() + " ";
		
		sentence = sentence.trim();
		
		
		// Builds the subsentence based on the original (complete) sentence.
		Sentence merged = new Sentence(main.getDocumentId(), main.getAbsolutePosition(), 
				sentence, null, null, 
				main.getSimilarityKey(), main.getKeywordsKey(),
				true);
		merged.setRelativePosition(main.getRelativePosition());
		merged.setWords(words);
		merged.setParseTree(complete);
		merged.setExtraScore(main.getExtraScore() + toComplete.getExtraScore());
		double subSentenceScore = merged.computeScore();
		merged.setScore(subSentenceScore);
		
		merged.setClustersSize(main.getClustersSize());
		merged.setKeywordClusterScore(main.getKeywordClusterScore());
		merged.setSimilarityClusterScore(main.getSimilarityClusterScore());
		merged.setDocumentScore(main.getDocumentScore());
		merged.setRelativePosition(main.getRelativePosition());
		merged.setRelativePositionRatio(main.getRelativePositionRatio());
		
		merged.setTotalFrequency(merged.computeFrequency());
		
		Collection<Word> subNamedEntities = merged.retrieveSubNamedEntities(words);
		merged.setNamedEntities(subNamedEntities);
		
		return merged;
	}

	/**
	 * <p>
	 * Completes the given sentence words (main) with the sentence words to
	 * complete (toComplete) and removes the parts to be removed (toRemove).
	 * </p>
	 * 
	 * @param main
	 * @param toComplete
	 * @return
	 */
	public static Sentence completeSentence(Sentence main, Sentence toComplete, Sentence toRemove, boolean toInsert) {

		// System.out.println("\n\n\n\nFRASE_"+main.getSentence());
		// main.printPhrases();

		Sentence finalSentence = new Sentence(main); // main.buildSubSentence(main.getParseTree());
		LinkedList<Word> sentenceWords = (LinkedList<Word>) finalSentence
				.getWords();

		LinkedList<Word> words2remove = (LinkedList<Word>) toRemove.getWords();
		SingleWord markingWord = null;

		if (toInsert)
			markingWord = (SingleWord) ((LinkedList<Word>) toRemove.getWords()).getLast();
		else
			markingWord = (SingleWord) ((LinkedList<Word>) toRemove.getWords()).getFirst();

		int previousIndex = -1;

		for (int i = 0; i < sentenceWords.size(); i++) {
			Word current = sentenceWords.get(i);

			if (current.represents(markingWord)) {

				if (!toInsert) {
					previousIndex = i;
					for (int j = 0; j < words2remove.size(); j++)
						sentenceWords.remove(i);
				} else
					previousIndex = i + 1;

				break;
			}
		}

		// System.out.println("FINAL_ANTES_"+finalSentence.getSentence());

		// LinkedList<Tree> leaves = new
		// LinkedList<Tree>(finalSentence.getParseTree().getLeaves());
		// int previousIndex = -1, totalLeaves = leaves.size();
		//		
		// for (int i = 0, j = 0; i < sentenceWords.size(); j++) {
		// String leafNode = Utils.getLeafNode(leaves.get(j).nodeString());
		//			
		// //
		// System.out.println("\tLEAF_"+leaves.get(j).nodeString()+"\ti_"+i+"\tj_"+j);
		//			
		// boolean relevant = !Regex.isContraction(leafNode) &&
		// !leafNode.matches("^"+Regex.PUNCTUATION_CLASS+"$");
		//			
		// if (relevant){
		// int index = Utils.getLeafIndex(leaves.get(j).nodeString());
		//			
		// if (j > 0 && j <= totalLeaves){
		// if (index == -1){
		// previousIndex = i-1;
		// break;
		// }
		// }
		// i++;
		// }
		// }

//		Tree correctedTree = ManageTrees.cleanTreeLeafs(finalSentence.getParseTree().deepCopy());
//		correctedTree = ManageTrees.updateLeavesIndexes(correctedTree);

		// sentenceWords.addAll(previousIndex+1, toComplete.getWords());
		sentenceWords.addAll(previousIndex, toComplete.getWords());
		String sentence = "";

		for (Word word : sentenceWords)
			sentence += ((SingleWord) word).getOriginal() + " ";

		// System.out.println("\n\n\nPREVIOUS_"+previousIndex+"\t"+sentence);
		// System.out.println("CENAS_"+main.getSentence()+"\tTOCOMPLETE_"+toComplete.getSentence()+"\nFINAL_"+finalSentence.getSentence()+"\n\tTREE_"+finalSentence.getParseTree());
//		Sentence newSentence = main.createSentence(correctedTree, sentenceWords, sentence);
		Sentence newSentence = main.createSentence(finalSentence.getParseTree().deepCopy(), sentenceWords, sentence);

		// System.out.println("CENAS_FINAL_"+newSentence.getSentence());

		return newSentence;
	}

	/**
	 * <p>
	 * Checks if a collection of sentences contains a given sentence.
	 * </p>
	 * 
	 * @param sentences
	 *            the collection of sentences to be searched.
	 * @param sentence
	 *            the sentence to be found.
	 * @return true if the sentence has been found; false otherwise.
	 */
	public static boolean containsSentence(Collection<Sentence> sentences, Sentence sentence) {

		boolean found = false;
		for (Sentence current : sentences) {

			found = current.representation().equals(sentence.representation());

			if (found)
				break;

		}

		return found;
	}
	
	/**
	 * <p>
	 * Checks if a collection of words contains a given word.
	 * </p>
	 * 
	 * @param words
	 *            the collection of word to be searched.
	 * @param word
	 *            the word to be found.
	 * @return true if the word has been found; false otherwise.
	 */
	public static boolean containsWord(Collection<Word> words, Word word) {

		boolean found = false;
		for (Word current : words) {

			found = current.representation().equals(word.representation());

			if (found)
				break;

		}

		return found;
	}

	public static boolean isAdverbialApposition(String nodeString) {
		String leafNode = ManageTrees.getLeafNode(nodeString);
		return leafNode.equals("VP") || leafNode.equals("AP");
	}

	/**
	 * <p>
	 * Removes the article before the entity if it exists.
	 * </p>
	 * 
	 * @param sentence the sentence to be corrected.
	 * @param entity the entity to be considered.
	 * @return a new corrected sentence.
	 */
	public static Sentence correctArticleBeforeEntity(Sentence sentence, Sentence entity) {

		Sentence finalSentence = sentence;
		Tree tree = retrieveArticleTree(sentence, entity);

		if (tree != null) {
			Tree removed = ManageTrees
					.removeTree(sentence.getParseTree(), tree);
			finalSentence = finalSentence.buildSubSentence(removed);
		}

		return finalSentence;
	}

	/**
	 * <p>
	 * Retrieves the article tree before the entity.
	 * </p>
	 * 
	 * @param sentence the sentence in which the article occurs.
	 * @param entity the entity sentence.
	 * @return a tree representing the article.
	 */
	public static Tree retrieveArticleTree(Sentence sentence, Sentence entity) {

		Tree entityInParseTree = sentence.retrieveSubTreeFromSentence(entity);
		LinkedList<Tree> entityLeaves = new LinkedList<Tree>(entityInParseTree.getLeaves());
		int artIndex = Regex.getTreeIndex(entityLeaves.getFirst().nodeString());
		artIndex = (artIndex != 0 ? artIndex - 1 : 0);

		LinkedList<Tree> list = new LinkedList<Tree>(sentence.getParseTree().getLeaves());

		Tree tree = list.get(artIndex).parent(sentence.getParseTree());
		return tree;
	}

	/**
	 * <p>
	 * Builds a sentence from a word.
	 * </p>
	 * 
	 * @param word the word to be converted into a sentence.
	 * @return the new Sentence.
	 */
	public static Sentence buildTitleSentenceFromWord(Word word) {

		Collection<Word> titleWords = new LinkedList<Word>();

		if (word instanceof SingleWord)
			titleWords.add(word);
		else if (word instanceof NamedEntity)
			titleWords.addAll(((NamedEntity) word).getEntityWords());

		Sentence titleSentence = new Sentence("\n\t" + word.toPrint().toUpperCase() + "\n", titleWords);
		double score = titleSentence.computeScore();
		titleSentence.setScore(score);
		titleSentence.setTitle(true);

		return titleSentence;
	}
	
	/**
	 * <p>
	 * Builds a line sentence from a word.
	 * </p>
	 * 
	 * @param word the word to be converted into a sentence.
	 * @return the new Sentence.
	 */
	public static Sentence buildNewLineSentence() {

		Collection<Word> titleWords = new LinkedList<Word>();
		titleWords.add(new SingleWord("","","",""));
//
//		if (word instanceof SingleWord)
//			titleWords.add(word);
//		else if (word instanceof NamedEntity)
//			titleWords.addAll(((NamedEntity) word).getEntityWords());

		Sentence titleSentence = new Sentence("\n", titleWords);
		double score = titleSentence.computeScore();
		titleSentence.setScore(score);
		titleSentence.setTitle(true);

		return titleSentence;
	}

	/**
	 * <p>
	 * Counts the total words in a given collection of sentences.
	 * </p>
	 * 
	 * @param sentences the collection of sentences which words will be counted.
	 * @return the number of words in the collection of sentences.
	 */
	public static int countTotalWordsInCollection(Collection<Sentence> sentences) {

		int totalWords = 0;

		for (Sentence s : sentences)
			totalWords += s.getTotalWords();

		return totalWords;
	}

	/**
	 * <p>
	 * Filters the given collection of sentences by the given compression, by
	 * considering the sentence words.
	 * </p>
	 * 
	 * @param sentences the collection of sentences to be filtered;
	 * @param compression the maximum number of words allowed;
	 * @return a Collection<Sentence> filtered based on the number of words in it.
	 */
	public static Collection<Sentence> filterSentencesByWordCompression(Collection<Sentence> sentences, int compression) {

		Collection<Sentence> newSentences = new LinkedList<Sentence>();
		int totalWords = 0;
		
		for (Sentence sentence : sentences) {

			totalWords += sentence.getTotalWords();
			
			// (2013-08-23) ALTERADO: a última frase é adicionada...
			newSentences.add(sentence);
			
			if (totalWords >= compression)
				break;
			
//			newSentences.add(sentence);
			
		}

		return newSentences;
	}

	/**
	 * <p>
	 * Computes the smoothed score of a collection of sentences.
	 * </p>
	 * 
	 * @param sentences the collection of sentences.
	 * @return the score of the collection of sentences.
	 */
	public static double computeSentencesScore(Collection<Sentence> sentences) {

		double score = 0;

		for (Sentence s : sentences)
			score += (s.getScore() / s.getTotalWords());

		return score;
	}

	/**
	 * <p>
	 * Filters a collection of sentences by removing the ones that are
	 * considered similar.
	 * </p>
	 * 
	 * @param sentences the collection of sentences to be filtered.
	 * @return the filtered collection of sentences.
	 */
	public static Collection<Sentence> filterSentencesBySimilarity(Collection<Sentence> sentences) {
		LinkedList<Sentence> filtered = new LinkedList<Sentence>();

		HashMap<String, SentenceCluster> clusters = new HashMap<String, SentenceCluster>();

		Iterator<Sentence> itOriginal = sentences.iterator();

		while (itOriginal.hasNext()) {
			Sentence sentence = itOriginal.next();

			Iterator<Sentence> it2Compare = sentences.iterator();

			while (it2Compare.hasNext()) {
				Sentence toCompare = it2Compare.next();

				if (!sentence.sameSentence(toCompare)){
				
					double similarity = Utils.computeSentence2SentenceSimilarity(sentence, toCompare);

					if (similarity >= Preferences.SIMPLIFIED_SENTENCE_SIMILIARITY_THRESHOLD) {

						Sentence centroid = sentence;
						Collection<Sentence> values = new LinkedList<Sentence>();
						SentenceCluster currentCluster = clusters.get(sentence.representation());

						if (currentCluster == null) {

							values.add(sentence);
							values.add(toCompare);

							if (sentence.getScore() < toCompare.getScore())
								centroid = toCompare;

							currentCluster = new SentenceCluster(0, centroid, values);
						} else {
							currentCluster = clusters.remove(sentence.representation());
							currentCluster.addValue(toCompare);

							if (sentence.getScore() < toCompare.getScore())
								currentCluster.setCentroid(toCompare);
						}

						clusters.put(sentence.representation(), currentCluster);
						break;
					}
				}
			}

			if (!clusters.containsKey(sentence.representation())) {

				Collection<Sentence> values = new LinkedList<Sentence>();
				values.add(sentence);
				SentenceCluster currentCluster = new SentenceCluster(0, sentence, values);
				clusters.put(sentence.representation(), currentCluster);
			}
		}

		Collection<SentenceCluster> values = clusters.values();

		for (SentenceCluster value : values)
			filtered.add((Sentence) value.getCentroid());

//		
//		if (sentences.size()!=filtered.size())
//			System.err.println("\n\nFILTREI POR SIMILARIDADE!!");
//		 System.err.println("\n\nFILTERED_PREV_"+sentences.size()+"\tFINAL_SIZE_"+filtered.size());

		return filtered;
	}

	/**
	 * <p>
	 * Filters a collection of sentences by removing the ones that are contained
	 * in others.
	 * </p>
	 * 
	 * @param sentences
	 *            the collection of sentences to be filtered.
	 * @return the filtered collection of sentences.
	 */
	public static Collection<Sentence> filterContainingSentences(
			Collection<Sentence> sentences) {
		LinkedList<Sentence> filtered = new LinkedList<Sentence>();

		HashMap<String, SentenceCluster> clusters = new HashMap<String, SentenceCluster>();

		Iterator<Sentence> itOriginal = sentences.iterator();

		while (itOriginal.hasNext()) {
			Sentence sentence = itOriginal.next();

			Iterator<Sentence> it2Compare = sentences.iterator();

			while (it2Compare.hasNext()) {
				Sentence toCompare = it2Compare.next();

				boolean isContained = sentence.containsSentence(toCompare);
				// System.out.println("\n\ns1_"+sentence.getSentence()+"\n\ts2_"+toCompare.getSentence()+"\tCONTAINED?"+isContained);

				if (isContained) {
					Sentence centroid = sentence;
					Collection<Sentence> values = new LinkedList<Sentence>();
					SentenceCluster currentCluster = clusters.get(sentence.representation());

					if (currentCluster == null) {

						values.add(sentence);
						values.add(toCompare);

						if (sentence.getScore() < toCompare.getScore())
							centroid = toCompare;

						currentCluster = new SentenceCluster(0, centroid, values);
					} else {
						currentCluster = clusters.remove(sentence.representation());
						currentCluster.addValue(toCompare);

						if (sentence.getScore() < toCompare.getScore())
							currentCluster.setCentroid(toCompare);
					}

					clusters.put(sentence.representation(), currentCluster);
					break;
				}

			}

			if (!clusters.containsKey(sentence.representation())) {

				Collection<Sentence> values = new LinkedList<Sentence>();
				values.add(sentence);
				SentenceCluster currentCluster = new SentenceCluster(0, sentence, values);
				clusters.put(sentence.representation(), currentCluster);
			}
		}

		Collection<SentenceCluster> values = clusters.values();

		for (SentenceCluster value : values)
			filtered.add((Sentence) value.getCentroid());

//		 System.out.print("\n\nFILTERED_PREV_"+sentences.size()+"\tFINAL_SIZE_"+filtered.size());

		return filtered;
	}

	public static LinkedList<Sentence> filterVerySmallSentences(Collection<Sentence> sentences) {
		LinkedList<Sentence> filtered = new LinkedList<Sentence>();
		
		for (Sentence sentence : sentences){
			
			if (!sentence.toIgnore())
				filtered.add(sentence);
		}
		
		
		return filtered;
	}

	/**
	 * <p>Corrects each summary sentences initial capital letters.</p>
	 * 
	 * @param summary the summary to be corrected.
	 * @return a corrected summary.
	 */
	public static Summary correctCapitals(Summary summary) {

		LinkedList<Sentence> sentences = new LinkedList<Sentence>();
		
		
		for (Sentence sentence : summary.getSentences()){
			String strSentence = sentence.getSentence();
			
			strSentence = Character.toUpperCase(strSentence.charAt(0)) + strSentence.substring(1);
			
			sentence.setSentence(strSentence);
			sentences.addLast(sentence);
		}
		
		
		return new Summary(sentences);
	}

	/**
	 * <p>Corrects each summary final punctuation token.</p>
	 * 
	 * @param summary the summary to be corrected.
	 * @return a corrected summary.
	 */
	public static Summary correctFinalPunctuation(Summary summary) {
		LinkedList<Sentence> sentences = new LinkedList<Sentence>();
		
		for (Sentence sentence : summary.getSentences()){
			
			if (!sentence.isTitle()){
			
				String strSentence = sentence.getSentence().trim();
				String lastChar = "" + strSentence.charAt(strSentence.length()-1);

				if (lastChar.matches(Regex.PUNCTUATION_CLASS))
					strSentence = strSentence.substring(0, strSentence.length()-1) + ".";
				else
					strSentence += ".";

				sentence.setSentence(strSentence);
			}
			sentences.addLast(sentence);
		}
		
		return new Summary(sentences);
	}
	
	/**** ---------------------- Managing WEKA stuff ----------------------- ****/

	public static LinkedList<String> getSplittedPropertiesWithPerson(String verb) {

		LinkedList<String> properties = new LinkedList<String>();

		if (verb.equals("")){
			properties.add("");
			properties.add("");
			properties.add("");
			properties.add("");
		}
		else{

			String[] splitted = verb.split("/");


			if (splitted.length == 3){

				splitted = splitted[2].split("#");

				if (splitted.length == 2){
					splitted = splitted[1].split("-");

					if (splitted.length == 2){

						if (!splitted[0].equalsIgnoreCase(Regex.INFINITIVE_VERB) && !splitted[0].equals(Regex.CONDITIONAL_VERB)){
							properties.add(splitted[0].substring(splitted[0].length()-1));
							properties.add(splitted[0].substring(0, splitted[0].length()-1));
							//							properties[2] = splitted[1];
						}
						else{
							properties.add("NULL");
							properties.add(splitted[0]);

						}
						//						System.out.println("\t---$"+verb+"$\t$"+splitted[0]+"$");
						properties.add(splitted[1].substring(0,splitted[1].length()-1));
						properties.add(splitted[1].substring(splitted[1].length()-1));

					}
					else{
						properties.add("NULL");
						properties.add("NULL");
						properties.add("NULL");
						properties.add("NULL");
					}
				}
			}
			else{
				if (!splitted[0].equalsIgnoreCase(Regex.INFINITIVE_VERB) && !splitted[0].equals(Regex.CONDITIONAL_VERB)){
					properties.add(splitted[0].substring(splitted[0].length()-1));
					properties.add(splitted[0].substring(0, splitted[0].length()-2));
					//					properties[2] = splitted[1];
				}
				else{
					properties.add("NULL");
					properties.add(splitted[0]);
					//					properties[2] = splitted[1];
				}

				properties.add(splitted[1].substring(0,splitted[1].length()-1));
				properties.add(splitted[1].substring(splitted[1].length()-1));
			}
		}
		return properties;
	}
	
	/**
	 * Gets the last <param>numberOfContext</param> words of the sentence.
	 * @param numberOfContext
	 * @param sentence
	 * @return
	 */
	public static LinkedList<String> getLastWords(int numberOfContext, String sentence) {

		LinkedList<String> words = new LinkedList<String>(Arrays.asList(sentence.split(Regex.BLANK_SPACE)));
		
		Iterator<String> it = words.descendingIterator();
		
		LinkedList<String> lastWords = new LinkedList<String>();
		int i = 0;//numberOfContext-1;
		
		while (it.hasNext()){
			String current = it.next();
			
			if (current.matches("^"+Regex.LETTER+"+.*?$")){
				lastWords.addFirst(current);
//				lastWords.add(Regex.getPOS(current));
				i ++;
			}
			
//			if (i < 0)
			if (i == numberOfContext)
				break;
		}
		
		if (i != numberOfContext){
			
			while (i!= numberOfContext){
				lastWords.addFirst("NULL");
				i++;
			}
		}
		
		return lastWords;
	}
	
	/**
	 * Gets the first <param>numberOfContext</param> words of the sentence.
	 * @param numberOfContext
	 * @param sentence
	 * @return
	 */
	public static LinkedList<String> getFirstWords(int numberOfContext, String sentence) {

		LinkedList<String> words = new LinkedList<String>(Arrays.asList(sentence.split(Regex.BLANK_SPACE)));
		
		Iterator<String> it = words.iterator();
		
		LinkedList<String> firstWords = new LinkedList<String>();
		int i = 0;
		
		while (it.hasNext()){
			
			String current = it.next();
			
			if (current.matches("^"+Regex.LETTER+"+.*?$")){
				firstWords.add(current);
//				firstWords.add(Regex.getPOS(current));
//				firstWords.add(Regex.getLemma(current));
				i ++;
			}
			
			if (i == numberOfContext)
				break;
		}
		
		if (i != numberOfContext){
			
			while (i!= numberOfContext){
				firstWords.add("NULL");
				i++;
			}
		}
		
		return firstWords;
	}
	
	public static String buildStringFromList(LinkedList<String> words){
		String line = " ";

		for (String word : words)
			line += word + " ";
		
		
		return line.trim();
	}
	
	public static LinkedList<String> getAllTokens(String pos, String sentence) {
		
		LinkedList<String> words = new LinkedList<String>(Arrays.asList(sentence.split(Regex.BLANK_SPACE)));

		LinkedList<String> poss = new LinkedList<String>();
		
		Iterator<String> it = words.iterator();
		
		while (it.hasNext()){
			String current = it.next();
			
			if (current.equals("NULL") || current.equals(""))
				poss.add(current);
			else{
				String currentPOS = Regex.getPOS(current);
				
				if (currentPOS.startsWith(pos))
					poss.add(Regex.getToken(current));
				
			}
		}
		
		return poss;
	}
	
	public static LinkedList<Sentence> replaceAllConnectivesByRelation(
			Collection<Connective> connectives,
			Collection<Sentence> sentences) {
		//  Vamos remover *todos* os conectores encontrados nos inícios das frases, para que não
		// tenhamos relações entre frases que não façam sentido.
		LinkedList<Sentence> modifiedSentences = new LinkedList<Sentence>();
		
		for (Sentence sentence : sentences){
			
			
//			System.out.println("Sentence_"+sentence.getPosTagged());
			Sentence newSentence = new Sentence(sentence);
			
			boolean found = false;

			if (!newSentence.isTitle()){

				for (Connective connective : connectives){

					if (!containsComplexConnective(sentence.getSentence(), connective, connectives)){

						String connective2Search = Regex.capitalizeFirst(connective.toPrint()) 
								+ "(?:"+Regex.COMMA+")"; //"|"+Regex.BLANK_SPACE+")";
						String pattern = "^"
								+ connective2Search 
								+ "(.*)$";
						Matcher m = Pattern.compile(pattern).matcher(sentence.getSentence());

//						System.out.println("\n\n\n\n"+pattern+"\n"+sentence.getSentence());
						// Se a frase começar com este conector
						if (m.find()){
							newSentence = new Sentence(sentence.getAbsolutePosition(), connective.getType() + connective.getSubtype());
							modifiedSentences.add(newSentence);
							found = true;
							break;
						}
						else{

							pattern = "^(.*?)" + Regex.COMMA + Regex.BLANK_SPACE + connective.toPrint() + Regex.COMMA + Regex.BLANK_SPACE + "?" + "(.*)$";
							m = Pattern.compile(pattern).matcher(sentence.getSentence());


							if (m.find()){
								
								newSentence = new Sentence(sentence.getAbsolutePosition(), connective.getType() + connective.getSubtype());
								modifiedSentences.add(newSentence);
								
								found = true;
								break;
							}
						}
					}
				}
			}
			
			if (!found){
				newSentence = new Sentence(sentence.getAbsolutePosition(), "NULL");
				modifiedSentences.add(newSentence);
			}
		}

		return modifiedSentences;
	}

	public static LinkedList<Sentence> removeAllConnectives(
			Collection<Connective> connectives,
			Collection<Sentence> sentences) {
		//  Vamos remover *todos* os conectores encontrados nos inícios das frases, para que não
		// tenhamos relações entre frases que não façam sentido.
		LinkedList<Sentence> modifiedSentences = new LinkedList<Sentence>();
		
		for (Sentence sentence : sentences){
			
			
//			System.out.println("Sentence_"+sentence.getSentence() + "\n\tisTitle "+sentence.isTitle());
			Sentence newSentence = new Sentence(sentence);
			
			boolean found = false;

			if (!newSentence.isTitle()){

				for (Connective connective : connectives){
					
//					System.out.println("\\tConnective_" + connective.toPrint());

					if (!containsComplexConnective(sentence.getSentence(), connective, connectives)){

						String connective2Search = Regex.capitalizeFirst(connective.toPrint()) 
								+ "(?:"+Regex.COMMA+")"; //"|"+Regex.BLANK_SPACE+")";
						String pattern = "^"
								+ connective2Search 
								+ "(.*)$";
						Matcher m = Pattern.compile(pattern).matcher(sentence.getSentence());

//						System.out.println("\n\n\n\n"+pattern+"\n"+sentence.getSentence());
						// Se a frase começar com este conector
						if (m.find()){
							String newString = m.group(1);
							pattern = "^" + "(?:" + Regex.capitalizeFirst(connective.searchVersion()) + "|" + connective.searchVersion() + ")"
									+ "(?: "+Regex.COMMA2SEARCH+")" //"|"+Regex.BLANK_SPACE+")"
									+ "(.*)$";
							
							System.out.println("\n\n"+pattern+"\n"+sentence.getPosTagged());
							
							m = Pattern.compile(pattern).matcher(sentence.getPosTagged());
							m.find();
							String pos = m.group(1);
							
							System.out.println("\t"+newString+"\n"+pos);

							newSentence.updateString(Regex.capitalizeFirst(newString), pos);
							modifiedSentences.add(newSentence);
							found = true;
							break;
						}
						else{

							pattern = "^(.*?)" + Regex.COMMA + Regex.BLANK_SPACE + connective.toPrint() + Regex.COMMA + Regex.BLANK_SPACE + "?" + "(.*)$";
							m = Pattern.compile(pattern).matcher(sentence.getSentence());


							if (m.find()){
								String newString = m.group(1) + Regex.BLANK_SPACE +  m.group(2);
								pattern = "^(.*?)" + Regex.BLANK_SPACE + Regex.COMMA2SEARCH + Regex.BLANK_SPACE + connective.searchVersion() + Regex.BLANK_SPACE + Regex.COMMA2SEARCH + Regex.BLANK_SPACE + "?" + "(.*)$";
								m = Pattern.compile(pattern).matcher(sentence.getPosTagged());
								m.find();
								String pos = m.group(1) + Regex.BLANK_SPACE + m.group(2);

//								System.out.println("\t"+newString+"\n"+pos);
								newSentence.updateString(Regex.capitalizeFirst(newString), pos);
								modifiedSentences.add(newSentence);
								found = true;
								break;
							}
						}
					}
				}
			}
			
			if (!found)
				modifiedSentences.add(newSentence);
		}

		return modifiedSentences;
	}
	
	public static Sentence removeConnective(Sentence sentence, Collection<Connective> connectives){
		//  Vamos remover *todos* os conectores encontrados nos inícios das frases, para que não
		// tenhamos relações entre frases que não façam sentido.
//		LinkedList<Sentence> modifiedSentences = new LinkedList<Sentence>();
		Sentence modifiedSentence = new Sentence(sentence);
		
//		for (Sentence sentence : sentences){
			Sentence newSentence = new Sentence(sentence);
			
			boolean found = false;

			if (!newSentence.isTitle()){

				for (Connective connective : connectives){

					if (!containsComplexConnective(sentence.getSentence(), connective, connectives)){

						String pattern = "^"
								+ Regex.capitalizeFirst(connective.toPrint()) 
									+ "(?:"+Regex.BLANK_SPACE +"|"+Regex.COMMA2SEARCH+")" 
								+ "(.*)$";
						Matcher m = Pattern.compile(pattern).matcher(sentence.getSentence());

//						System.out.println(pattern+"\n"+sentence.getSentence());

						// Se a frase começar com este conector
						if (m.find()){
							String newString = m.group(1);
							pattern = "^" + Regex.capitalizeFirst(connective.searchVersion()) + "(.*)$";
							m = Pattern.compile(pattern).matcher(sentence.getPosTagged());
							m.find();
							String pos = m.group(1);

							newSentence.updateString(Regex.capitalizeFirst(newString), pos);
							modifiedSentence = new Sentence(newSentence);
							found = true;
							break;
						}
						else{

							pattern = "^(.*?)" + Regex.COMMA + Regex.BLANK_SPACE + connective.toPrint() + Regex.COMMA + Regex.BLANK_SPACE + "?" + "(.*)$";
							m = Pattern.compile(pattern).matcher(sentence.getSentence());


							if (m.find()){
								String newString = m.group(1) + Regex.BLANK_SPACE +  m.group(2);
								pattern = "^(.*?)" + Regex.BLANK_SPACE + Regex.COMMA2SEARCH + Regex.BLANK_SPACE + connective.searchVersion() + Regex.BLANK_SPACE + Regex.COMMA2SEARCH + Regex.BLANK_SPACE + "?" + "(.*)$";
								m = Pattern.compile(pattern).matcher(sentence.getPosTagged());
								m.find();
								String pos = m.group(1) + Regex.BLANK_SPACE + m.group(2);

								//							System.out.println("\t"+newString+"\n"+pos);
								newSentence.updateString(Regex.capitalizeFirst(newString), pos);
								modifiedSentence = new Sentence(newSentence);
								found = true;
								break;
							}
						}
					}
				}
			}
			
//			if (!found)
//				modifiedSentences.add(newSentence);
//		}

		return modifiedSentence;
	}

	/**
	 * <p>Converts a collection of lists into a list.</p>
	 */
	public static List<Connective> convertCollection2List(Collection<List<Connective>> values) {
		
		List<Connective> list = new LinkedList<Connective>();
		
		for (List<Connective> value : values)
			list.addAll(value);
		
		return list;
	}

	/**
	 * <p>Filters a list of connectives based on their subtypes.</p>
	 * @param connectives complete list of connectives.
	 * @param subtype the subtype to be filtered.
	 * @return a list of connectives from the desired subtype.
	 */
	public static List<Connective> filterBySubtype(List<Connective> connectives, String subtype) {
		
		List<Connective> fromSubtype = new LinkedList<Connective>();
		
		for (Connective connective : connectives){
			if (connective.getSubtype().equals(subtype))
				fromSubtype.add(connective);
		}
		
		return fromSubtype.size() > 0 ? fromSubtype : connectives;
	}
	
	
	private static boolean containsComplexConnective(String sentence, Connective connective, Collection<Connective> connectives) {

		// TODO antes de vs. antes
		// Se o conector é composto por uma só palavra:
		// - verificar se a frase contém outro conector que o contenha...
		boolean found = false;

		String[] splitted = connective.getConnective().split(Regex.BLANK_SPACE);
		
//		System.out.println("\nCONNECTIVE_FIRST_"+connective.searchVersionWithoutAnnotation()+"\t"+splitted.length+"\n"+sentence);

		if (splitted != null && splitted.length == 1){

			for (Connective current : connectives){
				
				if (!current.searchVersionWithoutAnnotation().matches(connective.searchVersionWithoutAnnotation())){
					
					splitted = current.searchVersionWithoutAnnotation().split(Regex.BLANK_SPACE);

					if (splitted != null && splitted.length > 1){

						String pattern = ".*?" + Regex.BLANK_SPACE + connective.searchVersionWithoutAnnotation() + ".*?";
						Matcher ma = Pattern.compile(pattern).matcher(Regex.BLANK_SPACE + current.searchVersionWithoutAnnotation());
//						Matcher ma = Pattern.compile(pattern).matcher(Regex.WHITESPACE + current.searchVersionWithoutAnnotation());
//						System.out.println("\t\t"+current.searchVersionWithoutAnnotation());
//						System.out.println("\t\t"+pattern);
						
						// O conector actual contém o conector menor (p.e.: 'por causa de' contém 'por')
						if (ma.find()){

//							System.out.println("\t\tEu_"+current.toPrint().toUpperCase()+"_contenho o amigo_"+connective.toPrint().toUpperCase()+"\n\n");

							pattern = "^"
									+ "(?:" 
										+ ".*?" + Regex.BLANK_SPACE 
										+ current.searchVersionWithoutAnnotation() 
										+ "|" + Regex.capitalizeFirst(current.searchVersionWithoutAnnotation()) 
									+ ")"
									+ ".*?" + "$";
							ma = Pattern.compile(pattern).matcher(sentence);

							
							
							//  A frase contém o conector maior, por isso não vai deixar 
							// pesquisar nesta frase pelo menor
							found = ma.find();

//							System.out.println("PAT_____"+pattern+"\tfound_"+found);
//							System.out.println("sent_"+sentence);
//							if (found)
//								System.out.println("A frase_contém o amigo_"+current.toPrint()+"_por isso não vamos usar o amigo_"+connective.toPrint()+"\t"+found+"\n\n\n\n");
							if (found)
								break;
						}
					}
					
					
					// Este é um conector composto...
//					if (splitted != null && splitted.length > 1){
//					if (splitted != null && splitted.length > 1){
//
//						
//							String pattern = "^" + connective.searchVersionWithoutAnnotation() + ".*?";
//							Matcher ma = Pattern.compile(pattern).matcher(current.searchVersionWithoutAnnotation());
//
//							// O conector actual contém o conector menor (p.e.: 'por causa de' contém 'por')
//
//							found = ma.find();
//
//							if (found){
//									ma = Pattern.compile(pattern).matcher(Regex.WHITESPACE + current.searchVersionWithoutAnnotation());
////															System.out.println("\t\t"+current.searchVersionWithoutAnnotation());
//								//							System.out.println("\t\t"+pattern);
//
//								// O conector actual contém o conector menor (p.e.: 'por causa de' contém 'por')
//								if (ma.find()){
//								
//								
//								System.out.println("\t\tEu_"+current.toPrint().toUpperCase()+"_contenho o amigo_"+connective.toPrint().toUpperCase()+"\n\n");
//								break;
//								}
//							}
////						}
//					}
				}
			}
		}
		
		return found;
	}

	public static int lastSentenceIndex(Sentence title, LinkedList<Sentence> sentences) {
		
		int index = 0;
		
		Iterator<Sentence> it = sentences.iterator();
		
		while(it.hasNext()){//Sentence sentence : sentences){
			
			Sentence sentence = it.next();
			
			if (sentence.equals(title)){
				
				while (!sentence.isTitle()){
					sentence = it.next();
					index ++;
				}
				
				break;
				
			}
			index ++;
		}
		
		
		return index;
	}

	public static Word retrieveBestKeyword(LinkedList<Word> keywords,
			LinkedList<Sentence> sentences) {
		
		int maxOccurrences = 0;
		Word bestKeyword = null;
		
		for (Word keyword : keywords){
//			System.err.print("\t"+keyword);
			int currentOccurrences = 0;
			
			for (Sentence sentence : sentences){
				currentOccurrences += sentence.computeKeywordOccurrences(keyword);
//				System.err.print("\t_"+currentOccurrences);
			}
		
			System.err.println("\n----"+currentOccurrences+"\t"+keyword);
			if (currentOccurrences > maxOccurrences){
				maxOccurrences = currentOccurrences;
				bestKeyword = keyword;
			}
		}
		System.err.println("----"+maxOccurrences);
		
		if (bestKeyword == null)
			bestKeyword = keywords.getFirst();
		
		
		return bestKeyword;
	}
	
	public static Collection<Word> retrieveBestKeywords(LinkedList<Word> keywords, LinkedList<Sentence> sentences, int maxKeywords) {
		
		LinkedList<Word> bestKeywords = new LinkedList<Word>();
		
		
		
		
		for (Word keyword : keywords){
			int currentOccurrences = 0;
			
			for (Sentence sentence : sentences)
				currentOccurrences += sentence.computeKeywordOccurrences(keyword);
			
			keyword.setOccurrences(currentOccurrences);
		}
		
		
		
		
		for (Word keyword : keywords){
			
			boolean found = false;
			
			for (Word bestKeyword : bestKeywords){
				
				if (bestKeyword.equals(keyword)){
					found = true;
					break;
				}
			}
				
			if (!found)
				bestKeywords.add(keyword);
		}
		
//		System.err.println("---"+maxKeywords+"\t"+bestKeywords.size());
		
		Collections.sort(bestKeywords, Preferences.COMPARE_WORD_FREQUENCY);
//		bestKeywords = new LinkedList<Word>(bestKeywords.subList(0, maxKeywords));
		
//		for (Word k : bestKeywords)
//			System.err.println("---"+k);
		
		
		return bestKeywords;
	}

	public static int countTotalWords(Collection<Tree> trees) {
		// TODO Auto-generated method stub
		
		int leaves = 0;
		
		for (Tree t : trees)
			leaves += t.getLeaves().size();
		
		return leaves;
	}
	
	private static int countFromAnnotation(String annotation, LinkedList<String> words) {

		int total = 0;

		for (String word : words) {
			String pos = Regex.getPOS(word);
			if (pos.equals(annotation))
				total ++;
		}

		return total;
	}
	
	
	public static LinkedList<String> countProperties(String sentence) {

		LinkedList<String> splitted = new LinkedList<String>(Arrays.asList(sentence.split(Regex.WHITESPACE)));
		
		int totalWords = splitted.size();
		int advs = countFromAnnotation("ADV", splitted);
		advs += countFromAnnotation("LADV.", splitted);
		int adjs = countFromAnnotation("ADJ", splitted);
//		int vs = countFromAnnotation("V", splitted);
//		int vs = getAllVerbs(sentence).size();
		int cns = countFromAnnotation("CN", splitted);
		int pnms = countFromAnnotation("PNM", splitted);
		int cjs = countFromAnnotation("CJ", splitted);
		cjs += countFromAnnotation("LCJ.", splitted);
		int preps = countFromAnnotation("PREP", splitted);
		preps += countFromAnnotation("PREP.", splitted);
		int rels = countFromAnnotation("REL", splitted);
		int ords = countFromAnnotation("ORD", splitted);
		int qnts = countFromAnnotation("QNT", splitted);
		int dgts = countFromAnnotation("DGT", splitted);
		dgts += countFromAnnotation("DGTR", splitted);
		int cards = countFromAnnotation("CARD", splitted);
		int ppts = countFromAnnotation("PPT", splitted);
		ppts += countFromAnnotation("PPA", splitted);
		int mgts = countFromAnnotation("MGT", splitted);
		int dfrs = countFromAnnotation("DFR", splitted);
		dfrs += countFromAnnotation("LDFR.", splitted);
//		double relationCNV = 
		
		int digits = ords + qnts + dgts + cards + mgts + dfrs;
		
		LinkedList<String> counts = new LinkedList<String>();
//		counts.add((new Integer(totalWords)).toString());
		counts.add((new Integer(cns)).toString());
		counts.add((new Integer(pnms)).toString());
		counts.add((new Integer(adjs)).toString());
//		counts.add((new Integer(digits)).toString());
//		/**
//		counts.add((new Integer(ords)).toString());
//		counts.add((new Integer(qnts)).toString());
//		counts.add((new Integer(dgts)).toString());
//		counts.add((new Integer(cards)).toString());
//		counts.add((new Integer(mgts)).toString());
//		counts.add((new Integer(dfrs)).toString());
//		**/
//		counts.add((new Integer(vs)).toString());
//		counts.add((new Integer(advs)).toString());
//		counts.add((new Integer(cjs)).toString());
//		counts.add((new Integer(preps)).toString());
//		counts.add((new Integer(rels)).toString());
//				(new Integer(splitted.length)).toString(),
//				(new Integer(adverbs)).toString(),
//				(new Integer(adjectives)).toString(),
//				(new Integer(cns)).toString(),
//				(new Integer(pnms)).toString()
		return counts;
	}
	
}
