package core;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preferences.Preferences.GRAMTYPE;
import preferences.Regex;

public class Connective {

	private String connective;
	private String subtype; // Annotation subtype
	private String annotated;
	private String connective2search;
	private GRAMTYPE gramType; // Grammatical type of the connective (ADV or CJ)
	private String type;
	private String sentenceVersion;
	private String rule;
	private Collection<Word> words;

	public Connective(String connective, String annotated, String subtype,
			String gramType, String classType, String rule) {

		this.connective = connective;

		if (annotated.endsWith(Regex.NULL_STRING))
			this.annotated = connective;
		else
			this.annotated = annotated;

		if (subtype.equals(Regex.NULL_STRING))
			this.subtype = "";
		else
			this.subtype = subtype;

		if (classType.equals(Regex.NULL_STRING))
			this.setType("");
		else
			this.setType(classType);

		if (gramType.equals("ADV"))
			this.gramType = GRAMTYPE.ADV;
		else if (gramType.equals("SUB"))
			this.gramType = GRAMTYPE.SUB;
		else if (gramType.equals("COORD"))
			this.gramType = GRAMTYPE.COORD;
		else if (gramType.equals("VERBAL"))
			this.gramType = GRAMTYPE.VERBAL;

		if (rule.equals(Regex.NULL_STRING))
			this.rule = null;
		else
			this.rule = rule;

		this.connective2search = manageSearch(true);
		setWords(buildWords());
	}

	private Collection<Word> buildWords() {

		Collection<Word> words = new LinkedList<Word>();

		String[] splitted = connective.split(Regex.MARKER);

		for (int i = 0; i < splitted.length; i++) {
			Word connectiveWord = new SingleWord(splitted[i], splitted[i], "",
					"");
			words.add(connectiveWord);
		}

		return words;
	}

	public String getConnective() {
		return connective;
	}

	public String getSubtype() {
		return subtype;
	}

	public GRAMTYPE getGramType() {
		return gramType;
	}

	public String getSentenceVersion() {
		return sentenceVersion;
	}

	public void setSentenceVersion(String sentenceVersion) {
		this.sentenceVersion = sentenceVersion;
	}

	public Collection<Word> getWords() {
		return words;
	}

	public void setWords(Collection<Word> words) {
		this.words = new LinkedList<Word>(words);
	}

	public String toPrint() {
		return new String(
				connective.replaceAll(Regex.MARKER, Regex.BLANK_SPACE)).trim();
	}

	public String toAnnotate() {

		String printVersion = connective;
		printVersion = printVersion.replaceAll(Regex.MARKER, Regex.BLANK_SPACE);

		return printVersion;
	}

	public String searchVersion() {
		return connective2search;
	}

	public String searchVersionWithoutAnnotation() {
		return manageSearch(false);
	}

	private String manageSearch(boolean withAnnotation) {

		String connective2search = "";
		connective2search = searchWithAnnotation(this.annotated, null);

		return connective2search.trim();
	}

	private String searchWithAnnotation(String connectiveStr,
			String annotationStr) {

		String finalConnective = "";
		String connective = connectiveStr, annotation = annotationStr;

		if (annotation != null) {
			String[] annotationTokens = annotation.trim().split(Regex.MARKER);

			if (annotationTokens.length > 1) {
				String[] connectiveTokens = connective.trim().split(
						Regex.MARKER);

				for (int i = 0; i < connectiveTokens.length; i++) {

					if (annotationTokens[i].equals(Regex.NULL_STRING))
						annotationTokens[i] = annotationTokens[i].replaceAll(
								Regex.NULL_STRING, "")
								+ Regex.ANNOTATION_SPLITTER
								+ Regex.NONWHITESPACE_REGEX;

					finalConnective += connectiveTokens[i]
							+ annotationTokens[i] + Regex.BLANK_SPACE;
				}
			} else {
				annotation = annotation.replaceAll(Regex.MARKER, "");
				finalConnective = connective.replaceAll(Regex.MARKER, "")
						+ annotation;
			}
		} else {

			String[] connectiveTokens = connective.split(Regex.MARKER);

			if (connectiveTokens.length > 1) {

				for (int i = 0; i < connectiveTokens.length; i++)
					finalConnective += connectiveTokens[i]
							+ Regex.ANNOTATION_SPLITTER
							+ Regex.NONWHITESPACE_REGEX + Regex.BLANK_SPACE;

			} else {
				finalConnective = connective.replaceFirst(Regex.MARKER, "")
						+ Regex.ANNOTATION_SPLITTER + Regex.NONWHITESPACE_REGEX;
			}
		}

		return finalConnective.trim();
	}

	public boolean matchesRule(String sentence) {

		if (this.rule == null)
			return true;

		String verb = Regex.getFirstVerb(sentence);

		String pattern = ".*?" + this.rule + ".+";
		Matcher ma = Pattern.compile(pattern).matcher(verb);
		return ma.find();

	}

	public String toString() {
		return connective + "\t" + subtype + "\t" + connective2search + "\t"
				+ gramType.toString() + "\t" + rule;

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
