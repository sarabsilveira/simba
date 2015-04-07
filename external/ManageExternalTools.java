package external;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.List;

import lxner.RuleBased.code.RuleBased_NER;
import lxner.Statistical.code.Statistical_NER;
import preferences.Preferences;
import preferences.Regex;
import preferences.Utils;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Test;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.WhitespaceTokenizer;
import edu.stanford.nlp.trees.Tree;

/**
 * <p>
 * This class manages the external tools used by the summarizer.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class ManageExternalTools {

	private static LexicalizedParser lexparser;

	/**
	 * <p>
	 * Loads the external tools preferences.
	 * </p>
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public static void load() {
		try {
			lexparser = new LexicalizedParser(Preferences.PARSER_MODEL_FILE);
		} catch (Exception e) {
			System.out
					.println("============= ERROR in class [ManagesExternalTools.load].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Performs LX-Suite POS annotation.
	 * </p>
	 * 
	 * @param text
	 *            the text to be annotated.
	 * @return the annotated text.
	 */
	public static String posAnnotation(String text) {
		String suite = runSuite(text, Preferences.LXSUITE
				+ Preferences.LXSUITE_POSANNOTATION);
		return suite;
	}

	/**
	 * <p>
	 * Performs LX-Suite sentence chunking.
	 * </p>
	 * 
	 * @param text
	 *            the text to be chunked.
	 * @return the chunked text.
	 */
	public static String chunk(String text) {
		return runSuite(text, Preferences.LXSUITE + Preferences.LXSUITE_CHUNKER);
	}

	/**
	 * <p>
	 * Executes LX-Suite.
	 * </p>
	 * 
	 * @param text
	 *            the text to be annotated.
	 * @param location
	 *            the location of the file to be used to run the suite.
	 * @return the annotated text.
	 */
	private static String runSuite(String text, String location) {
		String result = "";
		try {
			Process proc = Runtime.getRuntime().exec(location);

			BufferedWriter toTagger = new BufferedWriter(
					new OutputStreamWriter(proc.getOutputStream(), "UTF-8"));
			toTagger.write(text);
			toTagger.newLine();
			toTagger.close();

			BufferedReader fromTagger = new BufferedReader(
					new InputStreamReader(proc.getInputStream(), "UTF-8"));
			String line = fromTagger.readLine();

			while (line != null) {
				result += line;
				line = fromTagger.readLine();
			}

			fromTagger.close();

		} catch (Exception e) {
			System.out
					.println("============= ERROR in class [ManagesExternalTools.runSuite].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * <p>
	 * Performs LX-NER named entity statistical (name-based) annotation.
	 * </p>
	 * 
	 * @param text
	 *            the annotated text to be annotated with named entities.
	 * @return the NE annotated text.
	 */
	public static String namedEntityNERAnnotation(String text) {
		Statistical_NER st = new Statistical_NER(Preferences.LXNER,
				Preferences.LXSUITE);
		String ner = st.myrun(text, 1, true);
		return ner;
	}

	/**
	 * <p>
	 * Performs LX-NER named entity rule-based (number-based) annotation.
	 * </p>
	 * 
	 * @param text
	 *            the annotated text to be annotated with named entities.
	 * @return the NE annotated text.
	 */
	public static String ruleBasedNERAnnotation(String text) {
		RuleBased_NER rb = new RuleBased_NER(Preferences.LXNER,
				Preferences.LXSUITE);
		return rb.run(text, true);
	}

	/**
	 * <p>
	 * Parses the given text with the Stanford Parser trained for Portuguese.
	 * </p>
	 * 
	 * @param sentence
	 *            the annotated text to be parsed.
	 * @return the parsed text.
	 */
	public static Tree parse(String sentence) {

		String sentenceTokens = Regex.tokenized2parse(sentence);
		Tree parse = null;

		// Parses the given text.
		try {
			Tokenizer<Word> tokenizer = WhitespaceTokenizer
					.factory()
					.getTokenizer(
							new BufferedReader(new StringReader(sentenceTokens)));
			List<Word> listOfTokens = tokenizer.tokenize();

			Test.MAX_ITEMS = 500000;

			if (lexparser.parse(listOfTokens))
				parse = lexparser.getBestParse();
			else
				parse = lexparser.getBestPCFGParse();

			parse = Utils.convertTree2Search(parse);

		} catch (Exception e) {
			parse = null;
			System.gc();
		}

		return parse;
	}

	/**
	 * <p>
	 * Classifies the discourse relation between two sentences.
	 * </p>
	 * 
	 * @param firstSentence
	 *            the first sentence.
	 * @param secondSentence
	 *            the second sentence.
	 * @return the discourse relation shared by the two sentences; null if no
	 *         relation has been found.
	 */
	public static String[] classify(String firstSentence, String secondSentence) {
		ManageClassification mc = new ManageClassification(firstSentence,
				secondSentence);
		String relation = mc.classifyClass();
		String type = mc.classifyType(relation);
		String subtype = mc.classifySubtype(type);
		return new String[] { relation, type, subtype };
	}

}
