package preferences;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Phrase;
import core.Sentence;
import core.SingleWord;
import core.Word;
import edu.stanford.nlp.trees.Tree;

/**
 * <p>Manages the Regex used by the system.</p>
 *  
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Regex {
	
	/** <p>Whitespace</p> **/
	public static final String WHITESPACE = "\\s+";
	/**<p>Non-whitespace</p> **/
	public static final String NONWHITESPACE_REGEX = "\\S+";
	public static final String SOMETHING_REGEX = ".*?";
	public static final String ANYTHING_REGEX = ".+";
	/** <p>Represents a sentence regex: everything between "<s>" and "</s>" is part of the sentence.</p> */
	public static final String PARAGRAPH_START_REGEX = "[<]p[>]";
	public static final String PARAGRAPH_START = "<p>";
	public static final String PARAGRAPH_END_REGEX = "[<][/]p[>]";
	public static final String PARAGRAPH_END = "</p>";
	public static final String SENTENCE_START = "<s>";
	public static final String SENTENCE_END = "</s>";
	public static final String SENTENCE_START_REGEX = "[<]s[>]";
	public static final String SENTENCE_END_REGEX = "[<][/]s[>]";
	public static final String SENTENCE = Regex.SENTENCE_START+ "(.*?)" + Regex.SENTENCE_END;
//	public static final String SENTENCE = "<s>(?: +)?(.*?)(?: +)?</s>";

	/** <p>Represents a lemma regex: everything between "/" and "/" is the word lemma.</p> */
	public static final String LEMMA = "/(.*?)/";
	/** <p>String used to split several lemmas for the same word.</p> **/
	public static final String LEMMA_SPLITTER = ",";
	
	/** <p>Word annotation regex</p> **/
	public static final String ANNOTATION = ".*/(\\S+|\\D+)$";
	/** <p>Word annotation splitter</p> **/
	public static final String ANNOTATION_SPLITTER = "/";
	
	/** <p>Word annotation regex</p> **/
	public static final String TOKEN = "(^.*?)/";
	
	public static final String LETTER = "[a-zA-ZÁáÉéÍíÓóÚúÀàÈèÌìÒòÙùÃãẼẽÕõÂâÊêÔôÇç]";
	public static final String UPPER_LETTER = "[A-ZÁÉÍÓÚÀÈÌÒÙÃẼÕÂÊÔÇ]";
	/** <p>Word regex.</p> **/
	public static final String WORD = LETTER+"+";
	
	/** <p>Punctuation annotation.</p> **/
	public static final String PUNCTUATION = "PNT";
	/** <p>Blank space.</p> **/
	public static final String BLANK_SPACE = " ";
	/** <p>Punctuation class.</p> **/
	public static final String PUNCTUATION_CLASS = "\\p{Punct}";
	/** <p>Currency class.</p> **/
	private static final String CURRENCY_CLASS = "\\p{Sc}";
	/** <p>Digit token.</p> **/
	private static final String DIGIT = "\\d+";
	/** <p>Ordinal symbols.</p> **/
	private static final String ORDINAL_SYMBOLS = "[ªº]|[.]o";
	/** <p>Ordinal symbols.</p> **/
	private static final String ORDINAL_TOKEN = DIGIT + ORDINAL_SYMBOLS;
	/** <p>Compound number regex.</p> **/
//	private static final String COMPOUND_NUMBER = DIGIT + "?" + "[,.]"; //+ DIGIT + "?";//+ "[./]" + DIGIT + "?";
	private static final String COMPOUND_NUMBER = DIGIT + "[.]" + DIGIT + "?";//+ "[./]" + DIGIT + "?";
	/** <p>Compound word regex.</p> **/
	private static final String COMPOUND_WORD = ORDINAL_TOKEN + "[-.]" + WORD + "|" + ORDINAL_TOKEN + "|" + WORD + "[-]" + "|" + WORD + "[-.]" + DIGIT; //"[-./]" + WORD;
//	private static final String COMPOUND_WORD = ORDINAL_TOKEN + "[-.]" + WORD + "|" + ORDINAL_TOKEN + "|" + WORD + "[-]" + WORD + "[-]" + WORD + "|" + WORD + "[-]" + WORD + "|" + WORD + "[-.]" + DIGIT; //"[-./]" + WORD;
	/** <p>Contracted word regex.</p> **/
	private static final String CONTRACTED_WORD = WORD + "[_]" + WORD;
	/** <p>Non-word symbols pattern.</p> */
	private static final String NON_WORD_SYMBOL = PUNCTUATION_CLASS + "|" + ORDINAL_SYMBOLS + "|" + CURRENCY_CLASS;
	/** <p>Right whitespace annotation.</p> **/
	public static final String RIGHT_SPACE = "[*][/]";
	/** <p>Left whitespace annotation.</p> **/
	public static final String LEFT_SPACE = "[\\\\][*]";
	/** <p>Proper name annotation.</p> **/
	public static final String PROPER_NAME = "PNM";
	/** <p>Common name annotation.</p> **/
	public static final String COMMON_NAME = "CN";
	/** <p>Adjective annotation.</p> **/
	private static final String ADJECTIVE = "ADJ";
	/** <p>Conjugated verb annotation.</p> **/
	public static final String CONJUGATED_VERB = "V";
	/** <p>Past Participle annotation.</p> **/
	private static final String PAST_PARTICIPLE = "PPT";
	/** <p>Verb infinitive annotation.</p> **/
	private static final String INFINITIVE = "INF";
	/** <p>Gerund annotation.</p> **/
	private static final String GERUND = "GER";
	
	/** <p>Contraction identification (usually appears at the end of the word)</p>**/
	private static final String CONTRACTION = "_";
	/** <p>Clitic annotation</p>**/
	private static final String CLITIC = "CL#";
	/** <p>Symbol annotation.</p> **/
	public static final String SYMBOL = "SYB";
	/** <p>Starting caption regex.</p> **/
	public static final String STARTING_CAPTION = "(?:Figura|Tabela|Ilustração|Imagem) ?\\d+";
	/** <p>Starting section regex.</p> **/
	public static final String STARTING_SECTION = "(?:(?:Capítulo|Secção) ?\\d+)|(?:(?:Apêndice) ?\\S+)";
	
	/** <p>Comma.</p> **/
	public static final String COMMA = ",";
	/** <p>Dash.</p> **/
	public static final String DASH = "-";
	/** <p>Full stop.</p> **/
	public static final String FULLSTOP = ".";
	/** <p>Main quote used to replace all quotes.</p> **/
	public static final String QUOTE = "'";
	/** <p>Quotes.</p> **/
	private static final String[] QUOTES = {"\"", "\'", "«", "»"}; 
	
	/** <p>End of sentence regex.</p> **/
	public static final String END_SENTENCE = "[.!?:][.]?[.]?";
	/** <p>Null string.</p> **/
	public static final String NULL_STRING = "null";
	/** <p>Splitting marker used in the connectives file.</p> **/
	public static final String MARKER = "###";
	/** <p> Identifies contiguous connectives.</p> **/
	public static final String CONTIGUOUS = Regex.MARKER + "-" + Regex.MARKER;
	/** <p>Comma.</p> **/
	public static final String COMMA2SEARCH = ",[*]//PNT";
	/**
	 * <p>Brackets conversion.</p>
	 * <p>The tokens: ( ) [ ] { } become -LRB- -RRB- -RSB- -RSB- -LCB- -RCB-,
	 * respectively.</p> 
	 */
	public static final String[][] BRACKETS = 
		{ 
			{ "\\(",    "\\)",  "\\[",   "\\]",   "\\{",     "\\}" }, 
//			{ "-LRB-", "-RRB-", "-LSB-", "-RSB-", "-LCB-", "-RCB-"}
			{ "-lrb-", "-rrb-", "-lsb-", "-rsb-", "-lcb-", "-rcb-"}
		};
	
	/** <p>Leaf string splitter (used to add information to the tree leaf) </p> **/
	public static final String LEAF_STR_SPLITTER = "|";
	
	/** <p>Tree node annotation index regex.</p> **/
	public static final String TREE_NODE_INDEX = "\\/(?:[-])?\\d+";
	/** <p>Tree node syntactic annotation.</p> **/
	public static final String TREE_SYN_ANNOTATION = "\\\\\\S+?";
	
	/** <p>Tag to add to the tree nodes to be removed.</p> **/
	public static String TREE2REMOVE = "#TOREMOVE#";
	
	/** <p>Node string that represents a parse fail. </p> **/
	public static String FAILED_PARSE = "X";
	
	public static final String ANY_VERB = "(?:/V#\\S\\S+|/GER )";
	public static final String[] VERB_POS = 
		{ "V", "VAUX", "INF", "INFAUX", "GER", "GERAUX", "PPT"};
	
	public static final String INFINITIVE_VERB = "INF";
	public static final String CONDITIONAL_VERB = "c";
	
	public static boolean isStopWord(String annotation){
		
		return annotation.equals("CJ") // conjunctions
			|| annotation.equals("DA") // definite articles
			|| annotation.equals("IA") // indefinite articles
			|| annotation.equals("INT") // interrogatives
			|| annotation.equals("PREP") // prepositions
			|| annotation.equals("REL"); // relatives
//			|| annotation.equals("")
//			|| annotation.equals("")
//			|| annotation.equals("")
//			|| annotation.equals("");
	}
	
	public static boolean isOpenClassWord(String annotation){
		//@deprecated porque é indiferente...
//		return annotation.startsWith("CN") // common noun
//			|| annotation.equals("PNM") // proper noun
//			|| annotation.startsWith("V") || annotation.startsWith("INF") // verb
//			|| annotation.startsWith("ADJ") // adjective
//			|| annotation.startsWith("ADV"); // adverb
		
		return true;
	}
	
	/**
	 * <p>Checks if the given word is a contracted word.</p>
	 * 
	 * @param word the word to be tested.
	 * @return true if the word is contracted; false otherwise.
	 */
	public static final boolean isContraction(String word){
		return word.contains(CONTRACTION);
	}
	
	/**
	 * <p>Checks if the given word is a punctuation word.</p>
	 * 
	 * @param word the word to be tested.
	 * @return true if the word is punctuation; false otherwise.
	 */
	public static final boolean isPunctuation(String word){
		return word.contains(PUNCTUATION);
	}
	
	/**
	 * <p>Checks if the given word contains a punctuation symbol that ends the sentence.</p>
	 * 
	 * @param word the word to be tested.
	 * @return true if the word is punctuation symbol ending the sentence; false otherwise.
	 */
	public static final boolean endsSentence(String word){
		return word.matches(".*?" + END_SENTENCE +".*?");
	}
	
	/**
	 * <p>Checks if an unannotated word contains a punctuation mark.</p>
	 * 
	 * @param word the word to be tested.
	 * @return true if the word contains a punctuation mark; false otherwise.
	 */
	public static final boolean containsPunctuation(String word){
		return word.matches(".*?"+PUNCTUATION_CLASS+".*");
	}
	
	/**
	 * <p>Gets the number of non-word tokens in the original word (unannotated).</p>
	 *  
	 * @param word the word to be tested.
	 * @return the number of non-word tokens that the word contains.
	 */
	public static final int numberPostNonWordTokens(String word){
		int tokens = 0;
		String postTokens = "\\S+?((?:" + NON_WORD_SYMBOL + ")+)$",
		     compoundWord = "(?:" + "(?:" + ORDINAL_TOKEN + ")" + ")";

		Matcher ma = Pattern.compile(postTokens).matcher(word.trim());
		
		if (ma.find()){
			String group = ma.group(1);

			ma = Pattern.compile("(?:" + NON_WORD_SYMBOL + ")").matcher(group);

			while(ma.find())
				tokens ++;
		}
		
		
		ma = Pattern.compile(compoundWord).matcher(word.trim());

		while (ma.find())
			tokens --;
		
		return tokens;
	}
	
	/**
	 * <p>Gets the number of non-word tokens in the original word (unannotated).</p>
	 *  
	 * @param word the word to be tested.
	 * @return the number of non-word tokens that the word contains.
	 */
	public static final int numberPreNonWordTokens(String word){
		int tokens = 0;
		
		Matcher ma = Pattern.compile("^((?:" + NON_WORD_SYMBOL + ")+)\\S+").matcher(word.trim());
		
		if (ma.find()){
			String group = ma.group(1);

			ma = Pattern.compile("(?:" + NON_WORD_SYMBOL + ")").matcher(group);

			while(ma.find())
				tokens ++;
		}
		
		return tokens;
	}
	
	/**
	 * <p>Gets the number of non-word tokens in the original word (unannotated).</p>
	 *  
	 * @param word the word to be tested.
	 * @return the number of non-word tokens that the word contains.
	 */
	public static final int numberNonWordTokens(String word){
		int tokens = 0;

		String nonWord = "(?:" + "(?:" + LEFT_SPACE + ")?" + NON_WORD_SYMBOL + "(?:" + RIGHT_SPACE + ")?" + ")",
		  compoundWord = "(?:" + "(?:" + COMPOUND_WORD + ")" 
		  				 + "|" + "(?:" + ORDINAL_TOKEN + ")"
		  				 + "|" + "(?:" + COMPOUND_NUMBER + ")"
		  				 + ")";

		Matcher ma = Pattern.compile(nonWord).matcher(word.trim());
		
		while (ma.find())
			tokens ++;
		//  Removes the token "-" that was counted as a non-word token, because
		// it is part of the compound word, thus it should not be counted as a
		// non-word symbol.
		ma = Pattern.compile(compoundWord).matcher(word.trim());

		while (ma.find())
			tokens --;
		
		return tokens;
	}
	
	/**
	 * <p>Checks if an unannotated string contains a only punctuation marks.</p>
	 * 
	 * @param word the word to be tested.
	 * @return true if the word contains a punctuation mark; false otherwise.
	 */
	public static final boolean hasOnlyPunctuation(String word){
		return word.matches(PUNCTUATION_CLASS+"+");
	}
	
	
	/**
	 * <p>Checks if the current annotated word has an annotated whitespace.</p>
	 * 
	 * @param word the word to be tested.
	 * @return true if the word has a whitespace; false otherwise.
	 */
	public static final boolean hasWhiteSpaces(String word){
		return hasLeftWhiteSpace(word) && hasRightWhiteSpace(word);
	}
	
	/**
	 * <p>Checks if the current annotated word contains a left whitespace.</p>
	 * 
	 * @param word the word to be tested.
	 * @return true if the word contains a left whitespace; false otherwise.
	 */
	public static final boolean hasLeftWhiteSpace(String word){
		return word.matches(".*" + LEFT_SPACE + ".*");
	}
	
	/**
	 * <p>Checks if the current annotated word contains a right whitespace.</p>
	 * 
	 * @param word the word to be tested.
	 * @return true if the word contains a right whitespace; false otherwise.
	 */
	public static final boolean hasRightWhiteSpace(String word){
		return word.matches(".*" + RIGHT_SPACE + ".*");
	}
	
	/**
	 * <p>Checks if the given annotation is a clitic annotation.</p>
	 * 
	 * @param annotation the string to be tested.
	 * @return true if this is a clitic annotation; false otherwise.
	 */
	public static final boolean isClitic(String annotation){
		return annotation.startsWith(CLITIC);
	}
	
	/**
	 * <p>Checks if the given word is a symbol.</p>
	 * 
	 * @param word the string to be tested.
	 * @return true if this word is a symbol; false otherwise.
	 */
	public static final boolean isSymbol(String word){
		return word.contains(SYMBOL);
	}
	
	/**
	 * <p>Checks if the given annotation is a common name.</p>
	 * 
	 * @param annotation the string to be tested.
	 * @return true if this word is a common name; false otherwise.
	 */
	public static final boolean isCommonName(String annotation){
		return annotation.startsWith(COMMON_NAME);
	}
	
	/**
	 * <p>Checks if the given annotation is a proper name.</p>
	 * 
	 * @param annotation the string to be tested.
	 * @return true if this word is a proper name; false otherwise.
	 */
	public static final boolean isProperName(String annotation){
		return annotation.startsWith(PROPER_NAME);
	}
	
	/**
	 * <p>Checks if the given annotation is an adjective.</p>
	 * 
	 * @param annotation the string to be tested.
	 * @return true if this word is an adjective; false otherwise.
	 */
	public static final boolean isAdjective(String annotation){
		return annotation.startsWith(ADJECTIVE);
	}
	
	/**
	 * <p>Checks if the given String is the corresponding annotation of a verb.</p>
	 * 
	 * @param annotation the annotation to be tested.
	 * @return true if the word is a verb; false otherwise.
	 */
	public static final boolean isVerb(String annotation){
		return annotation.startsWith(CONJUGATED_VERB);
	}
	
	/**
	 * <p>Checks if the given String is the corresponding annotation of a verb form.</p>
	 * <p>Verb forms include:</p>
	 * <ul>
	 * <li>Conjugated verbs</li>
	 * <li>Verbs in the past participle</li>
	 * <li>Verbs in the infinitive</li>
	 * <li>Verbs in the gerund</li>
	 * </ul>
	 * 
	 * @param annotation the annotation to be tested.
	 * @return true if the word is any of these verb forms; false otherwise.
	 */
	public static final boolean isVerbForm(String annotation){
		return annotation.startsWith(CONJUGATED_VERB) 
			|| annotation.startsWith(PAST_PARTICIPLE) 
			|| annotation.startsWith(INFINITIVE)
			|| annotation.startsWith(GERUND);
	}
	
	/**
	 * <p>Checks if the given sentence has a conjugated verb.</p>
	 * 
	 * @param sentence the sentence to be verified.
	 * @return true if the given sentence has a conjugated verb form; false otherwise.
	 */
	public static final boolean containsVerb(Sentence sentence){
		Collection<Word> words = sentence.getWords();
		boolean found = false;
		
		for (Word word : words) {
			if (word instanceof SingleWord){
				SingleWord singleWord = (SingleWord) word;
				found = isVerb(singleWord.getAnnotation());
			}
			
			if (found)
				break;
		}
		
		return found;
	}
	
	/**
	 * <p>Checks if the given annotated word is a clitic.</p>
	 * 
	 * @param word the annotated word to be tested.
	 * @return true if this is a clitic; false otherwise.
	 */
	public static final boolean hasClitic(String word){
		return word.startsWith("-") && word.contains(CLITIC);
	}

	/**
	 * <p>Corrects a word contraction building the original word.</p>
	 * 
	 * @param contracted the contracted word.
	 * @param suffix the suffix word.
	 * @return a String containing the corrected word.
	 */
	public static final String correctContraction(String contracted, String suffix){
		return contracted.replaceAll(CONTRACTION, suffix);
	}
	
	/**
	 * <p>Corrects all quotes into the same quote character (').</p>
	 * 
	 * @param text the text to be corrected.
	 * @return the corrected text.
	 */
	public static final String replaceQuotes(String text){
		
		String corrected = text;
		
		for (int i = 0; i < QUOTES.length; i++)
			corrected = corrected.replaceAll(QUOTES[i], QUOTE); 
		
		return corrected;
	}
	

	/**
	 * <p>Corrects missing quotes at the beginning or at the end of the sentence.</p>
	 * <p>This missing quotes result from the sentence chunking phase.</p>
	 * 
	 * @param sentence the sentence string to be corrected;
	 * 
	 * @return a String which quotes have been corrected.
	 */
	public static String correctMissingQuotes(String sentence) {
		
		// pôr uma aspa no início se há aspas ímpares e se não há aspa no início.
		// pôr uma aspa no fim se há aspas ímpares e há aspa no início.
		int numberOfQuotes = getNumberOfQuotes(sentence);
		boolean oddQuotes = (numberOfQuotes % 2 == 1);
		String corrected = sentence;
		
		if (oddQuotes){
			
			if (sentence.startsWith(Regex.QUOTE))
				corrected += Regex.QUOTE;
			else if (!sentence.endsWith(Regex.QUOTE))
				corrected += Regex.QUOTE;
			else
				corrected = Regex.QUOTE + corrected;
		}

//		System.out.println("\n\n\nORIGINAL_"+sentence+"\nCORRECTED_"+corrected+"\t#_"+numberOfQuotes);
		return corrected.trim();
	}
	
	/**
	 * <p>Retrieves the number of quotes in the string.</p>
	 * 
	 * @param sentence the String in which will be counted the number of quotes.
	 * 
	 * @return the number of quotes in the given String.
	 */
	private static int getNumberOfQuotes(String sentence){
		
		int number = 0;
		
		String pattern = "(" + Regex.QUOTE + ")";
		Matcher ma = Pattern.compile(pattern).matcher(sentence);
		
		while (ma.find())
			number ++;

		return number;
	}
	
	/**
	 * TODO
	 * <p>Corrects commas between the subject and the verb, that can be in the sentence,
	 * after the simplification process.</p>
	 * 
	 * @param sentence the sentence to be verified.
	 * @return the corrected sentence.
	 */
	public static Sentence correctCommas(Phrase phrase, Sentence sentence){
		
		// Verifica se há vírgulas entre o sujeito e o predicado do verbo.
		// Isto pode (e talvez deva) ser feito na árvore. Caso não haja árvore,
		// ver se há um sujeito (composto por um artigo e um nome comum ou um 
		// nome próprio) seguido de um verbo, com uma vírgula pelo meio.
		
		
		//  Se a frase contém uma vírgula que é seguida pela phrase, há que remover essa vírgula.
//		LinkedList<Word> sentenceWords = new LinkedList<Word>(sentence.getWords());
//		LinkedList<Word> phraseWords = new LinkedList<Word>(phrase.getSentence().getWords());
//		
//		String sentenceStr = "";
//		
//		for (int i = 0; i < sentenceWords.size(); i++) {
//			
//			Word word = sentenceWords.get(i);
//			if (i+1 < sentenceWords.size()){
//				Word nextWord = sentenceWords.get(i+1);
//				if (Regex.containsPunctuation(nextWord.getAnnotation())){
////					System.out.println("\t\t\t\t#############"+nextWord);
////					int j = 0;
////					Word phraseWords = phraseWords.getFirst();
////					boolean equal = word.equals(phraseWords);
//					
//					
//					if (word.equals(phraseWords)){
//						System.out.println("\t\t\t\tPARA TIRAR A VÍRGULA!");
//					}
//				}
//			}
//		}
		
		return sentence;
	}
	
	/**
	 * <p>Checks if the given word is a bracket.</p>
	 * 
	 * @param word the text to be searched.
	 * 
	 * @return true if the word is a bracket; false otherwise.
	 */
	public static final boolean isBracket(String word){

		boolean found = false;
		for (int i = 0; i < BRACKETS[0].length && !found; i ++)
			found = word.equals(BRACKETS[0][i]);

		return found;
	}
	
	
	/**
	 * <p>Checks if the given word is an opening bracket.<p>
	 * 
	 * @param bracket the string to be tested.
	 * 
	 * @return true if the string is an opening bracket; false otherwise.
	 */
	public static boolean isOpeningBracket(String bracket){
		boolean found = false;
		
		for (int i = 0; i < BRACKETS[1].length && !found; i+=2)
			found = bracket.equals(BRACKETS[1][i]);
		
		return found;
	}
	
	/**
	 * <p>Checks if the given word is an closing bracket.<p>
	 * 
	 * @param bracket the string to be tested.
	 * 
	 * @return true if the string is an closing bracket; false otherwise.
	 */
	public static boolean isClosingBracket(String bracket){
		boolean found = false;
		
		for (int i = 1; i < BRACKETS[1].length && !found; i+=2)
			found = bracket.equals(BRACKETS[1][i]);
		
		return found;
	}
	
	/**
	 * <p>Converts brackets into a pattern recognized by the parser.</p>
	 * 
	 * @param text the original text.
	 * 
	 * @return the text with the brackets converted.
	 */
	public static final String convertBrackets(String text){
		String right = text;
		
		for (int i = 0; i < BRACKETS[0].length; i ++)
			right = right.replaceAll(BRACKETS[0][i], BRACKETS[1][i]);

		return right;
	}
	
	/**
	 * <p>Reverts bracket conversion.</p>
	 * 
	 * @param text the sentence to be reverted.
	 * 
	 * @return the corrected string.
	 */
	public static final String revertBrackets(String text){
		
		String right = text;

		for (int i = 0; i < BRACKETS[0].length; i ++)
			right = right.replaceAll(BRACKETS[1][i], BRACKETS[0][i]);

		return right;
	}
	
	/**
	 * <p>Retrieves a regex that contains opening brackets.</p> 
	 * 
	 * @pre before using this regex, the text must be converted with convertBrackets(..) method.
	 * 
	 * @return a regex containing the reference to all opening brackets.
	 */
	public static String openingBracketsRegex(){
		String regex = "";
		
		for (int i = 0; i < BRACKETS[1].length; i+=2) {
			regex += BRACKETS[1][i];
			if (i+2 < BRACKETS[1].length)
				regex += "|";
		}
		
		return regex + "(" + Regex.TREE_NODE_INDEX + ")?";
	}
	
	/**
	 * <p>Retrieves a regex that contains closing brackets.</p> 
	 * 
	 * @pre before using this regex, the text must be converted with convertBrackets(..) method.
	 * 
	 * @return a regex containing the reference to all closing brackets.
	 */
	public static String closingBracketsRegex(){
		String regex = "";
		
		for (int i = 1; i < BRACKETS[1].length; i+=2) {
			regex += BRACKETS[1][i];
			if (i+1 < BRACKETS[1].length)
				regex += "|";
		}
		
		return regex + "(" + Regex.TREE_NODE_INDEX + ")?";
	}
	
	
//	public static String openParentheticalsRegex(){
//		return "/" + Regex.openingAppositionRegex() + "|" + Regex.openingBracketsRegex() + "$/";
//	}
//	
//	public static String closeParentheticalsRegex(){
//		return "/" + Regex.closingAppositionRegex() + "|" + Regex.closingBracketsRegex() + "$/";
//	}
//	

	/**
	 * <p>Retrieves a word token from a string word that may contain other tokens (e.g. punctuation).</p>
	 * 
	 * @param word the word string.
	 * @return a String containing the token; empty string if not found.
	 */
	public static String getWordToken(String word) {
		String token = "";
		
		Matcher ma = Pattern.compile("(" + CONTRACTED_WORD + "|" + COMPOUND_WORD + "|" + WORD + "|" + DIGIT + ")").matcher(word.trim());
		
		if (ma.find())
			token = ma.group(1);
		
//		System.out.println("word_"+word+"\ttoken_"+token+"$");
		
		
		return token;
		
	}
	
	/**
	 * <p>Retrieves a token from an annotated word.</p>
	 * 
	 * @param word an annotated word.
	 * @return the String containing the token; empty string if not found.
	 */
	public static final String getToken(String word){
		String token = "";
		
		Matcher ma = Pattern.compile(TOKEN).matcher(word.trim());
		
		if (ma.find())
			token = ma.group(1);
		
		return token;
	}
	
	/**
	 * <p>Retrieves the word annotation from an annotated word.</p>
	 * 
	 * @param word an annotated word.
	 * @return the String containing the annotation; empty string if not found.
	 */
	public static final String getAnnotation(String word){
		String annotation = "";
		
		Matcher ma = Pattern.compile(ANNOTATION).matcher(word.trim());
		
		if (ma.find())
			annotation = ma.group(1);
		
		return annotation;
	}
	
	/**
	 * <p>Retrieves the word lemma from an annotated word.</p>
	 * 
	 * @param word an annotated word.
	 * @return the String containing the lemma; empty string if not found.
	 */
	public static final String getLemma(String word){
		String lemma = "";
		
		Matcher ma = Pattern.compile(LEMMA).matcher(word.trim());
		
		if (ma.find())
			lemma = ma.group(1);
		
		return lemma;
	}
	
	/**
	 * <p>Retrieves all word lemmas.</p>
	 * 
	 * @param word the word containing the lemma.
	 * @return the String[] containing the word lemmas.
	 */
	public static final String[] allLemmas(SingleWord word){
		return (word.getLemma().trim()).split(LEMMA_SPLITTER);
	}

	/**
	 * <p>Tokenizes the annotated text and retrieves an array of tokens without annotation,
	 * and with the brackets corrected in order to be parsed by the LX-Parser.<p>
	 * 
	 * @param text the annotated text to be tokenized.
	 * @return an array containing all the text tokens.
	 */
	public static String[] tokens2parse(String text) {
		
		String[] tokens = (text.trim()).split(" +");
		
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].replaceAll("(?:\\*/|\\\\\\*)", "");
			tokens[i] = tokens[i].replaceFirst("/.*", "");
			tokens[i] = Regex.convertBrackets(tokens[i]);
		}
			
		return tokens;
	}
	
	public static String tokenized2parse(String text) {
		
		String sentence = "";
		String trimmed = (text.replaceAll("\\u00A0", "")).trim();
		
		String[] tokens = trimmed.split(" +");
		
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].replaceAll("(?:\\*/|\\\\\\*)", "");
			tokens[i] = tokens[i].replaceFirst("/.*", "");
			tokens[i] = Regex.convertBrackets(tokens[i]);
			sentence += tokens[i] + " ";
		}
			
		return sentence.trim();
	}

	
	/**
	 * <p>Tokenizes the text and retrieves a string with all tokens splitted by a whitespace.<p>
	 * 
	 * @param text the annotated text to be tokenized.
	 * @return the string containing all the tokens separated by a whitespace. 
	 */
	public static String tokenize(String text) {
		
		String tokenized = "";
		String[] tokens = text.split(" +");
		
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].replaceFirst("/.*", "");
			tokenized += tokens[i] + " ";
		}
		
		tokenized = tokenized.trim();
		
		return tokenized;
	}
	
	/**
	 * <p>Retrieves the word index from an index annotated node.</p>
	 * 
	 * @param node an annotated node.
	 * @return the index value; -1 if there is no index in the node.
	 */
	public static final int getTreeIndex(String node){
		int index = -1;
		String annotation = Regex.getAnnotation(node);
		
		if (!annotation.equals(""))
			index = (new Integer(annotation)).intValue();
		
		return index;
	}
	
	/**
	 * <p>Retrieves the word index from an index annotated node.</p>
	 * 
	 * @param node an annotated node.
	 * @return the index value; -1 if there is no index in the node.
	 */
	public static final String getTreeNodeString(String node){
		String nodeString = Regex.getToken(node);
		return nodeString;
	}
	
	
	/**
	 * <p>Checks if the given text is a comma.</p>
	 * 
	 * @param text the text to be tested.
	 * @return true if the text is a comma; false otherwise.
	 */
	public static boolean isComma(String text){
		return text.equals(COMMA);
	}
	
	/**
	 * <p>Checks if the given text is a comma.</p>
	 * 
	 * @param text the text to be tested.
	 * @return true if the text is a comma; false otherwise.
	 */
	public static boolean isQuote(String text){
		return text.equals(QUOTE);
	}
	
	/**
	 * <p>Retrieves a regex that contains opening apposition punctuation.</p> 
	 * 
	 * @return a regex containing the reference to all opening apposition punctuation.
	 */
	public static String openingAppositionRegex(){
		return "^" + Regex.COMMA + "|" + "^" + Regex.DASH + "(?:" + Regex.DASH + ")?" + "(" + Regex.TREE_NODE_INDEX + ")?";
	}
	
	/**
	 * <p>Retrieves a regex that contains closing apposition punctuation.</p> 
	 * 
	 * @return a regex containing the reference to all closing apposition punctuation.
	 */
	public static String closingAppositionRegex(){
		return Regex.COMMA + "|" + "^" + Regex.DASH + "{2}" + "(" + Regex.TREE_NODE_INDEX + ")?"; //+ Regex.DASH; //+ "|" + Regex.FULLSTOP;
	}
	
	/**
	 * <p>Checks if the given sentence is a caption.</p>
	 * 
	 * @param sentence the sentence to be checked;
	 * 
	 * @return true if the sentence is a caption; false otherwise.
	 */
	public static boolean isCaption(String sentence){
		return sentence.trim().matches("^" + STARTING_CAPTION + ".*");
	}
	
	/**
	 * <p>Checks if the given sentence is the start of a section.</p>
	 * 
	 * @param sentence the sentence to be checked;
	 * 
	 * @return true if the sentence is a section starter; false otherwise.
	 */
	public static boolean isSection(String sentence){
		return sentence.trim().matches("^" + STARTING_SECTION + ".*");
	}

	/**
	 * <p>Counts the number of proper names in this sentence.</p>
	 * 
	 * @param sentence the sentence to be checked.
	 * 
	 * @return the number of PNMs in this sentence.
	 */
	public static int numberOfPNMs(Sentence sentence) {
		Collection<Word> words = sentence.getWords();
		int numberPNMs = 0;
		
		for (Word word : words){
			if (word instanceof SingleWord){
				SingleWord singleWord = (SingleWord) word;
				if (Regex.isProperName(singleWord.getAnnotation()))
					numberPNMs ++;
			}
		}
		
		return numberPNMs;
	}
	
	/**
	 * <p>Checks if a sentence contains a passage.</p>
	 * 
	 * @param sentence the complete sentence.
	 * @param passage the passage to be checked in the complete sentence.
	 * 
	 * @return true if the complete sentence contains the passage; false otherwise. 
	 */
	public static boolean containsPassage(String sentence, String passage){
		Matcher ma = Pattern.compile(".*?" + passage + ".*?").matcher(sentence);
		
		return ma.find();
	}

	/**
	 * <p>Retrieves the given tree untokenized leaves tokens.</p>
	 * 
	 * @param leaves the tree to be represented as an array of string tokens.
	 * 
	 * @return a String[] containing all the tree untokenized tokens.
	 */
	public static String[] getUntokenizedLeavesTokens(Tree leaves) {
		
		String strLeaves = Utils.printTreeLeaves(leaves);
		
		strLeaves = untokenizeBrackets(strLeaves);
		strLeaves = Regex.revertBrackets(strLeaves);
		
		strLeaves = untokenizeCommas(strLeaves);
		strLeaves = untokenizeQuotes(strLeaves);
		strLeaves = untokenizeContractions(strLeaves);
		strLeaves = untokenizeClitics(strLeaves);
		strLeaves = untokenizeFinalPunctuation(strLeaves);
		
		
		return strLeaves.split(" +");
	}
	
	/**
	 * <p>Untokenizes final sentence punctuations in the given string.</p>
	 * 
	 * @param tokenized the tokenized string to be untokenized.
	 * 
	 * @return a String which final sentence punctuations are untokenized.
	 */
	private static String untokenizeFinalPunctuation(String tokenized) {
		
		String pattern = " (" + Regex.END_SENTENCE + ") ?";
		Matcher ma = Pattern.compile(pattern).matcher(tokenized);
		
		while (ma.find()){
			String group = ma.group(1);
			tokenized = tokenized.replaceFirst(pattern, group + " ");
		}
		
		return tokenized.trim();
	}

	/**
	 * <p>Untokenizes clitics in the given string.</p>
	 * 
	 * @param tokenized the tokenized string to be untokenized.
	 * 
	 * @return a String which clitics are untokenized.
	 */
	private static String untokenizeClitics(String tokenized) {
		
		String pattern = " (-\\S+?)";
		Matcher ma = Pattern.compile(pattern).matcher(tokenized);

		while (ma.find()){
			String word = ma.group(1);//, clitic = ma.group(2);
			tokenized = tokenized.replaceFirst(pattern, word);
		}
		return tokenized;
	}

	/**
	 * <p>Untokenizes contractions in the given string.</p>
	 * 
	 * @param tokenized the tokenized string to be untokenized.
	 * 
	 * @return a String which contractions are untokenized.
	 */
	private static String untokenizeContractions(String tokenized) {
		
		String pattern = "(" + Regex.CONTRACTION + ") ";
		Matcher ma = Pattern.compile(pattern).matcher(tokenized);
		
		while (ma.find()){
			String group = ma.group(1);
			tokenized = tokenized.replaceFirst(pattern, group);
		}
		
		return tokenized;
	}
	
	/**
	 * <p>Untokenizes quotes in the given string.</p>
	 * 
	 * @param tokenized the tokenized string to be untokenized.
	 * 
	 * @return a String which quotes are untokenized.
	 */
	private static String untokenizeQuotes(String tokenized) {
		
		String modified = tokenized;
		String pattern = " " + Regex.QUOTE + " " + "(.*?)" + " " + Regex.QUOTE + " ";
		Matcher ma = Pattern.compile(pattern).matcher(modified);
		
		
		while (ma.find()){
			String group = ma.group(1);
			modified = modified.replaceFirst(pattern, " " + Regex.QUOTE + group + Regex.QUOTE + " ");
		}
		
		if (modified.equals(tokenized)){
			
			pattern = "(\\S+)" +  " " + Regex.QUOTE + " ";
			ma = Pattern.compile(pattern).matcher(modified);

			if (ma.find()){
				String group = ma.group(1);
				modified = modified.trim().replaceFirst(pattern, group + Regex.QUOTE + " ");
			}

			pattern = " ?" + Regex.QUOTE + " " + "(\\S+)";
			ma = Pattern.compile(pattern).matcher(modified);
			if (ma.find()){
				String group = ma.group(1);
				modified = modified.trim().replaceFirst(pattern, " " + Regex.QUOTE + group);
			}
		}
			
		
		return modified;
	}
	

	/**
	 * <p>Untokenizes commas in the given string.</p>
	 * 
	 * @param tokenized the tokenized string to be untokenized.
	 * 
	 * @return a String which commas are untokenized.
	 */
	private static String untokenizeCommas(String tokenized) {
		
		String pattern = " (" + Regex.COMMA + ") ";
		Matcher ma = Pattern.compile(pattern).matcher(tokenized);
		
		while (ma.find()){
			String group = ma.group(1);
			tokenized = tokenized.replaceFirst(pattern, group + " ");
		}
		
		return tokenized;
	}

	/**
	 * <p>Untokenizes brackets in the given string.</p>
	 * 
	 * @param tokenized the tokenized string to be untokenized.
	 * 
	 * @return a String which brackets are untokenized.
	 */
	private static String untokenizeBrackets(String tokenized) {
		
		String pattern = " ?(" + Regex.openingBracketsRegex() + ") ";  
		Matcher ma = Pattern.compile(pattern).matcher(tokenized);
		
		while (ma.find()){
			String group = ma.group(1);
			tokenized = tokenized.replaceFirst(pattern, " " + group);
		}
		
		pattern = " (" + Regex.closingBracketsRegex() + ") ";  
		ma = Pattern.compile(pattern).matcher(tokenized);
		
		while (ma.find()){
			String group = ma.group(1);
			tokenized = tokenized.replaceFirst(pattern, group + " ");
		}
		
		return tokenized;
	}
	
	
	/**
	 * <p>Retrieves sentence POS annotated tokens.</p>
	 * 
	 * @param sentence the Sentence to be obtained; 
	 * @return a String[] containing all the tokens.
	 */
	public static String[] getSentenceTokens(Sentence sentence){
		return sentence.getPosTagged().split(" +");
	}

	/**
	 * <p>Corrects the given strings punctuation spaces.</p>
	 * 
	 * @param sentence the String to be corrected.
	 * 
	 * @return a String which punctuation spaces have been corrected.
	 */
	public static String correctPunctuationSpaces(String sentence) {
		
		String corrected = sentence;
		String punctuation = "(?: +)?(" + Regex.PUNCTUATION_CLASS + "+)(?: +)?";
		String pattern = punctuation + "(.*?)"; 
		Matcher ma = Pattern.compile(pattern).matcher(corrected);
		
		if (ma.find()){
			String group = ma.group(1),
				   remainder = ma.group(2);
			
			if (Regex.isOpeningBracket(group))
				corrected = corrected.replaceFirst(punctuation, " " + group);
			else if (Regex.isClosingBracket(group) 
					|| Regex.isComma(group)
					|| Regex.endsSentence(group))
				corrected = corrected.replaceFirst(punctuation, group + " ");
				
			correctPunctuationSpaces(remainder);
		}
		
		pattern = "( " + Regex.DASH + "+? +?" + Regex.DASH + "+? )";
		ma = Pattern.compile(pattern).matcher(corrected);
		
		while (ma.find()){
			String group = ma.group(1);
			corrected = corrected.replaceFirst(group, " ");
		}
		
		return corrected.trim();
	}
	
	/**
	 * <p>Counts the number of extra tokens (commas ' or contraction expressions) in a given passage or sentence.</p>
	 * 
	 * @param passage the passage to be tested.
	 * @return the number of extraneous tokens in the passage.
	 */
	public static final int numberOfExtraTokens(String passage){
		
		String[] tokens = passage.split(" +");
		int contractions = 0;
		
		for (int i = 0; i < tokens.length; i++) {
			
			if (Regex.isContraction(tokens[i]) || Regex.isPunctuation(tokens[i]))
				contractions ++;
		}
		
		
		return contractions;
	}


	/**
	 * TODO
	 * <p>Corrects punctuation errors in the given sentence.</p>
	 * @param sentence the sentence to be corrected.
	 * @return the corrected sentence string.
	 */
	public static String correctPunctuationErrors(String sentence) {
		
		// TODO verificar se já está a cobrir os casos todos...
		
		String corrected = sentence;
		corrected = Regex.correctPunctuationSpaces(corrected);	
		
		// Two commas in a row.
		String pattern = "(" + Regex.COMMA + " +?" + Regex.COMMA + ")";
		Matcher ma = Pattern.compile(pattern).matcher(corrected);
		
		while (ma.find()){
			String group = ma.group(1);
			corrected = corrected.replaceFirst(group, Regex.COMMA);
		}
		
		// Comma followed by an end of sentence token.
		pattern = Regex.COMMA + " +?(" + "(?:" + Regex.END_SENTENCE + ")+?)";
		ma = Pattern.compile(pattern).matcher(corrected);
		
		while (ma.find()){
			String group = ma.group(1);
			corrected = corrected.replaceFirst(pattern, group);
		}

		// Comma followed by a quote.
		pattern = Regex.COMMA + " +?(" + "(?:" + Regex.QUOTE + "))";
		ma = Pattern.compile(pattern).matcher(corrected);
		
		while (ma.find()){
			String group = ma.group(1);
			corrected = corrected.replaceFirst(pattern, group);
		}
		
		if (!corrected.matches("(?:" + Regex.END_SENTENCE + ")$"))
			corrected = corrected.trim() + Regex.FULLSTOP;
		
		
		corrected = Regex.correctMissingQuotes(corrected);
		return corrected;
	}
	
	
	/**
	 * <p>Removes from a passage punctuation tokens.</p>
	 * <p>Corrects whitespaces.</p>
	 * 
	 * @param passage the string to be corrected.
	 * @return the corrected string.
	 */
	public static String cleanOtherThanWords(String passage){
		
		String inText = passage;
		
		inText = inText.replaceAll("^"+Regex.PUNCTUATION_CLASS, "");
		inText = inText.replaceAll(Regex.PUNCTUATION_CLASS+"$", "");
		inText = inText.replaceAll(" +", " ");
		
				
		return inText;
	}
	
	/*** Managing WEKA stuff ***/
	public static String getFirstVerb(String sentence) {

//		System.out.println("FIRST_"+sentence);
		String verb = "";
		String pattern = 
				"(" + Regex.NONWHITESPACE_REGEX + Regex.ANY_VERB
				+ ")";
		Matcher ma = Pattern.compile(pattern).matcher(sentence);

		if (ma.find())
			verb = ma.group(1);
		
		return verb;
	}
	
	public static String getLastVerb(String sentence) {
		
//		System.out.println("LAST_"+sentence);
		String verb = "";
		String pattern = "(" + Regex.NONWHITESPACE_REGEX + Regex.ANY_VERB + ")";
		Matcher ma = Pattern.compile(pattern).matcher(sentence);

		while (ma.find())
			verb = ma.group(1);
		
		
		return verb;
	}
	
	public static LinkedList<String> addPrefix(LinkedList<String> properties, String prefix) {
		
		LinkedList<String> newList = new LinkedList<String>();
		
		for (String property : properties)
			newList.add(prefix + property);
		
		return newList;
	}
	
	public static String getPOS(String word) {

		String pos = "NULL";

		if (word!= null){
			String[] splitted = word.split("/");

			if (splitted.length == 3){

				splitted = splitted[2].split("#");
				pos = splitted[0];

			}
			else if (splitted.length > 1){
				pos = splitted[1];
				splitted = splitted[1].split("#");
				
				if (splitted.length == 2)
					pos = splitted[0];
			}
		}
		
		
		return pos;
	}
	
	/**
	 * <p>Capitalizes the first letter of a string.</p>
	 */
	public static final String capitalizeFirst(String text){
		return new String(text.substring(0,1).toUpperCase() + text.substring(1));
	}
	
	public static final String lowerFirst(String text){
		return new String(text.substring(0,1).toLowerCase() + text.substring(1));
	}

	public static String removeInitialStuff(String chunked) {
		String corrected = "";
		Matcher ma = Pattern.compile(Regex.SENTENCE).matcher(chunked);

		while (ma.find()){
			String sentence = ma.group(1);

			String startingPattern = 
					"^( (?:(?:(?:"+Regex.LETTER+"+ )+)?[(]"+Regex.LETTER+"+[)] [-]" + "|" +
							"(?:(?:"+Regex.LETTER+"+[, ])+) (?:"+Regex.LETTER+")+ [-]"+ "|"+ 
							"(?:"+Regex.LETTER+"+ )+[-]))";
			String pattern = startingPattern 
					+ " " + Regex.UPPER_LETTER + ".*?$"; 
			Matcher inside = Pattern.compile(pattern).matcher(sentence);

//			System.out.println("\n\n\nBEFORE_"+sentence);

			if (inside.find()){
				String toRemove = inside.group(1);
//				System.out.println("\tremoving....$$$"+toRemove+"$$$\tsent_"+sentence);
				
//				sentence = sentence.replaceFirst(toRemove, "");
				sentence = sentence.replace(toRemove, "");
			}
			
//			System.out.println("\nAFTER__"+sentence);

			corrected += Regex.SENTENCE_START + sentence + Regex.SENTENCE_END;
		}

		corrected = Regex.PARAGRAPH_START + corrected + Regex.PARAGRAPH_END;
		
		return corrected;
	}
}
