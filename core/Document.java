package core;

import java.util.Collection;
import java.util.LinkedList;

import preferences.Utils;

/**
 * <p>
 * This class represents a document.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Document {

	/**
	 * <p>
	 * Document id.
	 * </p>
	 **/
	private int id;
	/**
	 * <p>
	 * The document file name.
	 * </p>
	 **/
	private String filename;
	/**
	 * <p>
	 * Number of words in this document.
	 * </p>
	 **/
	private int words;
	/**
	 * <p>
	 * Number of sentences in this document.
	 * </p>
	 **/
	private int sentences;
	/**
	 * <p>
	 * Document text.
	 * </p>
	 **/
	private Text text;
	/**
	 * <p>
	 * Document keywords.
	 * </p>
	 **/
	private Collection<Word> keywords;
	/**
	 * <p>
	 * Document score.
	 * </p>
	 **/
	private double score;

	public Document() {
		this.id = -1;
		this.setScore(0);
		this.words = 0;
		this.sentences = 0;
		this.keywords = new LinkedList<Word>();
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Builds a new Document object.
	 * </p>
	 * 
	 * @param id
	 *            the document identification.
	 * @param filename
	 *            the document filename.
	 * @param text
	 *            the document text.
	 */
	public Document(int id, String filename, Text text) {
		this();
		this.id = id;
		this.filename = filename;
		this.text = text;
	}

	public Document(Document document) {
		super();
		this.id = document.getId();
		this.filename = document.getFilename();
		this.words = document.getWords();
		this.sentences = document.getSentences();
		this.text = new Text(document.getText());
		this.keywords = new LinkedList<Word>(document.getKeywords());
		this.score = document.getScore();
	}

	/** Getters & Setters **/
	public int getId() {
		return id;
	}

	public String getFilename() {
		return filename;
	}

	public int getWords() {
		return words;
	}

	public int getSentences() {
		return sentences;
	}

	public Text getText() {
		return text;
	}

	public double getScore() {
		return this.score;
	}

	public Collection<Word> getKeywords() {
		return keywords;
	}

	public void setWords(int words) {
		this.words = words;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public void setSentences(int sentences) {
		this.sentences = sentences;
	}

	public void setKeywords(Collection<Word> keywords) {
		this.keywords = keywords;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return this.filename + "\t#Words: " + words + "\t#Sentences: "
				+ sentences + "\tScore:" + score;
	}

	/**
	 * <p>
	 * Computes document keywords.
	 * </p>
	 */
	public void computeKeywords() {
		this.keywords = Utils.extractKeywords(this.text.getSentences());
	}

	/**
	 * 
	 */
	public void computeKeywordsWithoutNEs() {
		this.keywords = Utils.extractKeywords(this.text.getSentences());
	}

	@Override
	public boolean equals(Object document) {
		return this.id == ((Document) document).getId();
	}
}
