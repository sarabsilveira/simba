package core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import preferences.Preferences;

public class Paragraph implements Comparable {

	private Sentence title;
	private Word keyword;
	private Collection<Sentence> sentences;
	private double score;

	public Paragraph() {
		this.title = null;
		this.sentences = new LinkedList<Sentence>();
	}

	public Paragraph(Sentence title) {
		this();
		this.title = title;
	}

	public Paragraph(Word keyword, Sentence title,
			Collection<Sentence> sentences) {
		this();
		this.keyword = keyword;
		this.title = new Sentence(title);
		this.sentences = new LinkedList<Sentence>(sentences);
		sortParagraph();
		this.score = computeScore();
	}

	public Paragraph(Collection<Sentence> sentences) {
		this();
		this.sentences = sentences;
		this.score = computeScore();
	}

	public Sentence getTitle() {
		return title;
	}

	public double getScore() {
		return score;
	}

	public void setTitle(Sentence title) {
		this.title = title;
	}

	public Collection<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(Collection<Sentence> sentences) {
		this.sentences = sentences;
		this.score = computeScore();
	}

	private double computeScore() {

		double score = 0;

		for (Sentence sentence : this.sentences) {
			score += sentence.completeScore();
		}

		score = score / size();

		return score;
	}

	public void addSentence(Sentence sentence) {
		this.sentences.add(sentence);
	}

	public Collection<Sentence> toList() {
		Collection<Sentence> list = new LinkedList<Sentence>();
		list.add(this.title);
		list.addAll(this.sentences);
		return list;
	}

	public int size() {
		return this.sentences.size();
	}

	public boolean hasTitle() {
		return title == null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paragraph other = (Paragraph) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	//
	@Override
	public int compareTo(Object arg0) {

		if (arg0 instanceof Sentence) {
			if (arg0.equals(title))
				return 0;
			else
				return -1;
		}

		return -1;
	}

	public String toString() {

		String output = "\n" + title.getSentence() + "\t" + score + "\n\n";
		//
		for (Sentence sentence : this.sentences)
			output += sentence.getSentence() + "\n";

		return output;
	}

	private void sortParagraph() {
		LinkedList<Sentence> ordered = new LinkedList<Sentence>(sentences);
		Collections.sort(ordered, Preferences.COMPARE_SENTENCES_IN_PARAGRAPH);
		sentences = new LinkedList<Sentence>(ordered);
	}

}
