package evaluation;

/**
 * <p>
 * This class represents the objective evaluation metrics.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Objective extends Metric {
	
	// System execution time
	private double executionTime;
	// Concerning the collection:
	// 1. Number of documents
	private double colNumberDocuments;
	// 2. Number of sentences
	private double colNumberSentences;
	// 3. Number of words
	private double colNumberWords;
	// 4. Number of removable expressions
	private double colNumberRemovableExpressions;
	// 5. Average of sentences per document
	private double colAvgSentencesDocument;
	// 6. Average of words per document
	private double colAvgWordsDocument;
	// 7. Average of words per sentence
	private double colAvgWordsSentence;
	// 8. Average of words per removable expression
	private double colAvgWordsRemovableExpression;
	// 9. Average of removable expressions per document
	private double colAvgRemovableExpressionsDocument;
	// 10. Average of removable expresions per sentence
	private double colAvgRemovableExpressionsSentence;
	// 11. Maximum sentence length
	private double colMaxSentenceLength;
	// 12. Minimum sentence length
	private double colMinSentenceLength;

	// Concerning the summary:
	// 13. Number of documents used
	private double sumNumberDocuments;
	// 14. Number of sentences
	private double sumNumberSentences;
	// 15. Number of words
	private double sumNumberWords;
	// 16. Number of removable expressions
	private double sumNumberRemovableExpressions;
	// 17. Number of expressions removed
	private double sumNumberExpressionsRemoved;
	// 18. Average of sentences per document
	private double sumAvgSentencesDocument;
	// 19. Average of words per document
	private double sumAvgWordsDocument;
	// 20. Average of words per sentence
	private double sumAvgWordsSentence;
	// 21. Average of words per removable expression
	private double sumAvgWordsRemovableExpression;
	// 22. Average of removable expressions per sentence
	private double sumAvgRemovableExpressionsSentence;
	// 23. Average of sentence original position in the document
	private double sumAvgSentencePosition;
	// 24. Maximum sentence length
	private double sumMaxSentenceLength;
	// 25. Minimum sentence length
	private double sumMinSentenceLength;
	// 26. Maximum sentence position chosen
	private double sumMaxSentencePositionChosen;
	// 27. Minimum sentence position chosen
	private double sumMinSentencePositionChosen;
	// 28. Difference between the compression rate with and whithout simplification
	private double sumDifferenceCompressions;
	
	/** Getters & Setters **/
	public double getExecutionTime() { return executionTime; }
	public void setExecutionTime(double executionTime) { this.executionTime = executionTime; }
	public double getColNumberDocuments() { return colNumberDocuments; }
	public void setColNumberDocuments(double colNumberDocuments) { this.colNumberDocuments = colNumberDocuments; }
	public double getColNumberSentences() { return colNumberSentences; }
	public void setColNumberSentences(double colNumberSentences) { this.colNumberSentences = colNumberSentences; }
	public double getColNumberWords() { return colNumberWords; }
	public void setColNumberWords(double colNumberWords) { this.colNumberWords = colNumberWords; }
	public double getColNumberRemovableExpressions() { return colNumberRemovableExpressions; }
	public void setColNumberRemovableExpressions(double colNumberRemovableExpressions) {
		this.colNumberRemovableExpressions = colNumberRemovableExpressions;
	}
	public double getColAvgSentencesDocument() { return colAvgSentencesDocument; }
	public void setColAvgSentencesDocument(double colAvgSentencesDocument) { 
		this.colAvgSentencesDocument = colAvgSentencesDocument;
	}
	public double getColAvgWordsDocument() { return colAvgWordsDocument; }
	public void setColAvgWordsDocument(double colAvgWordsDocument) { this.colAvgWordsDocument = colAvgWordsDocument; }
	public double getColAvgWordsSentence() { return colAvgWordsSentence; }
	public void setColAvgWordsSentence(double colAvgWordsSentence) { this.colAvgWordsSentence = colAvgWordsSentence; }
	public double getColAvgWordsRemovableExpression() { return colAvgWordsRemovableExpression; }
	public void setColAvgWordsRemovableExpression(double colAvgWordsRemovableExpression) {
		this.colAvgWordsRemovableExpression = colAvgWordsRemovableExpression;
	}
	public double getColAvgRemovableExpressionsDocument() { return colAvgRemovableExpressionsDocument; }
	public void setColAvgRemovableExpressionsDocument(double colAvgRemovableExpressionsDocument) {
		this.colAvgRemovableExpressionsDocument = colAvgRemovableExpressionsDocument;
	}
	public double getColAvgRemovableExpressionsSentence() { return colAvgRemovableExpressionsSentence; }
	public void setColAvgRemovableExpressionsSentence(double colAvgRemovableExpressionsSentence) {
		this.colAvgRemovableExpressionsSentence = colAvgRemovableExpressionsSentence;
	}
	public double getColMaxSentenceLength() { return colMaxSentenceLength;}
	public void setColMaxSentenceLength(double colMaxSentenceLength) {this.colMaxSentenceLength = colMaxSentenceLength;}
	public double getColMinSentenceLength() { return colMinSentenceLength; }
	public void setColMinSentenceLength(double colMinSentenceLength) { this.colMinSentenceLength = colMinSentenceLength; }
	public double getSumNumberDocuments() { return sumNumberDocuments; }
	public void setSumNumberDocuments(double sumNumberDocuments) { this.sumNumberDocuments = sumNumberDocuments; }
	public double getSumNumberSentences() {	return sumNumberSentences; }
	public void setSumNumberSentences(double sumNumberSentences) { this.sumNumberSentences = sumNumberSentences; }
	public double getSumNumberWords() { return sumNumberWords; }
	public void setSumNumberWords(double sumNumberWords) { this.sumNumberWords = sumNumberWords; }
	public double getSumNumberRemovableExpressions() { return sumNumberRemovableExpressions; }
	public void setSumNumberRemovableExpressions(double sumNumberRemovableExpressions) {
		this.sumNumberRemovableExpressions = sumNumberRemovableExpressions;
	}
	public double getSumNumberExpressionsRemoved() { return sumNumberExpressionsRemoved; }
	public void setSumNumberExpressionsRemoved(double sumNumberExpressionsRemoved) {
		this.sumNumberExpressionsRemoved = sumNumberExpressionsRemoved;
	}
	public double getSumAvgSentencesDocument() { return sumAvgSentencesDocument; }
	public void setSumAvgSentencesDocument(double sumAvgSentencesDocument) {
		this.sumAvgSentencesDocument = sumAvgSentencesDocument;
	}
	public double getSumAvgWordsDocument() { return sumAvgWordsDocument; }
	public void setSumAvgWordsDocument(double sumAvgWordsDocument) {
		this.sumAvgWordsDocument = sumAvgWordsDocument;
	}
	public double getSumAvgWordsSentence() { return sumAvgWordsSentence; }
	public void setSumAvgWordsSentence(double sumAvgWordsSentence) { this.sumAvgWordsSentence = sumAvgWordsSentence; }
	public double getSumAvgWordsRemovableExpression() { return sumAvgWordsRemovableExpression; }
	public void setSumAvgWordsRemovableExpression(double sumAvgWordsRemovableExpression) {
		this.sumAvgWordsRemovableExpression = sumAvgWordsRemovableExpression;
	}
	public double getSumAvgRemovableExpressionsSentence() { return sumAvgRemovableExpressionsSentence; }
	public void setSumAvgRemovableExpressionsSentence(double sumAvgRemovableExpressionsSentence) {
		this.sumAvgRemovableExpressionsSentence = sumAvgRemovableExpressionsSentence;
	}
	public double getSumAvgSentencePosition() { return sumAvgSentencePosition; }
	public void setSumAvgSentencePosition(double sumAvgSentencePosition) {
		this.sumAvgSentencePosition = sumAvgSentencePosition;
	}
	public double getSumMaxSentenceLength() { return sumMaxSentenceLength; }
	public void setSumMaxSentenceLength(double sumMaxSentenceLength) {
		this.sumMaxSentenceLength = sumMaxSentenceLength;
	}
	public double getSumMinSentenceLength() { return sumMinSentenceLength; }
	public void setSumMinSentenceLength(double sumMinSentenceLength) {
		this.sumMinSentenceLength = sumMinSentenceLength;
	}
	public double getSumMaxSentencePositionChosen() { return sumMaxSentencePositionChosen; }
	public void setSumMaxSentencePositionChosen(double sumMaxSentencePositionChosen) {
		this.sumMaxSentencePositionChosen = sumMaxSentencePositionChosen;
	}
	public double getSumMinSentencePositionChosen() { return sumMinSentencePositionChosen; }
	public void setSumMinSentencePositionChosen(double sumMinSentencePositionChosen) {
		this.sumMinSentencePositionChosen = sumMinSentencePositionChosen;
	}
	public double getSumDifferenceCompressions() { return sumDifferenceCompressions; }
	public void setSumDifferenceCompressions(double sumDifferenceCompressions) {
		this.sumDifferenceCompressions = sumDifferenceCompressions;
	}
}
