package statistics;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import options.SumOptions;

import controllers.SumController;

import preferences.Preferences;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import core.Document;
import core.Phrase;
import core.Sentence;

/**
 * <p>
 * This class aims to produce a set of statistics about the system, mainly the
 * summarization and simplification processes.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class Statistic {

	/**
	 * <p>
	 * Summarized documents.
	 * </p>
	 **/
	private Collection<Document> documents;
	/**
	 * <p>
	 * System main controller.
	 * </p>
	 **/
	private SumController controller;
	/**
	 * <p>
	 * The spreadsheet.
	 * </p>
	 **/
	private WritableWorkbook workbook;

	/**
	 * <p>
	 * Statistical values.
	 * </p>
	 **/
	private int totalDocuments, totalSentences, totalWords;
	private int totalSummarySentences, totalSimplifiedSentences,
			totalSummaryWords, totalSimplifiedWords;
	private double totalSummaryScore, totalSimplifiedScore;
	private int totalPhrases, totalAppositions, totalParentheticals,
			totalModifiers, totalNullParseTrees;
	private int totalPhrasesWords, totalAppositionsWords,
			totalParentheticalsWords, totalModifiersWords;
	private int totalDocumentBlocks;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * <p>
	 * Creates a new statistic.
	 * </p>
	 */
	public Statistic() {
		createWorkbook();
	}

	public void buildStatistics(String filename, int id) {

		// Criar uma folha de excel...
		// Número de documentos

		// Número de frases nos documentos
		// Número de palavras
		System.out.println("Printing statistics... " + filename + "\t" + id);
		this.documents = new LinkedList<Document>(controller.getDocuments());
		writeSheet(filename, id);
		totalDocumentBlocks++;
	}

	/**
	 * <p>
	 * Creates the Spreadsheet.
	 * </p>
	 * 
	 * @return false if an error occurs.
	 */
	private void createWorkbook() {

		try {
			// Creates a new spreadsheet file
			this.workbook = Workbook.createWorkbook(new File(
					Preferences.STATISTICS_FILE));

		} catch (Exception e) {
			System.err.println("\n[" + this.getClass() + ".createWorkbook] "
					+ "IO Exception occured.\n" + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Adds the current sheet to the spreadsheet.
	 * </p>
	 * 
	 * @param name
	 *            the sheet name.
	 * @param id
	 *            the sheet position.
	 */
	private void writeSheet(String name, int id) {
		try {
			WritableSheet sheet = workbook.createSheet(name, id);

			for (int i = 0; i < 10; i++)
				sheet.setColumnView(i, 25);

			WritableCellFormat format = new WritableCellFormat();
			format.setAlignment(Alignment.CENTRE);
			int row = 0, column = 0;

			Label cell = new Label(column, row, "Número Total de Documentos: ");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			int totalDocuments = controller.getTotalDocuments();
			cell = new Label(column, row,
					new Integer(totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column--;
			row++;
			cell = new Label(column, row, "Número Total de Frases: ");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			int totalSentences = controller.getTotalSentences();
			cell = new Label(column, row,
					new Integer(totalSentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column--;
			row++;
			cell = new Label(column, row, "Número Total de Palavras: ");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			int totalWords = controller.getTotalWords();
			cell = new Label(column, row, new Integer(totalWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column--;
			row++;
			cell = new Label(column, row, "Taxa de compressão: ");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double(
							((SumOptions) controller.getOptions())
									.getCompressionRate()).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column--;
			row++;
			cell = new Label(column, row, "Máximo palavras do sumário: ");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			double compression = (((SumOptions) controller.getOptions())
					.getCompressionRate() != 0 ? ((SumOptions) controller
					.getOptions()).getCompressionRate()
					: Preferences.DEFAULT_COMPRESSION_RATE);
			cell = new Label(column, row,
					new Integer(Math.round((int) (((double) controller
							.getTotalWords()) * ((double) compression))))
							.toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column--;
			row++;
			row++;

			column = 0;
			cell = new Label(column, row, "# Frases do Sumário");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Palavras do Sumário");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "Score do Sumário");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Frases do Simplificado");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Palavras do Simplificado");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "Score do Simplificado");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			row++;

			column = 0;
			int totalSummarySentences = controller.getSummary()
					.getTotalSentences();
			cell = new Label(column, row,
					new Integer(totalSummarySentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			int totalSummaryWords = controller.getSummary().getTotalWords();
			cell = new Label(column, row,
					new Integer(totalSummaryWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			double totalSummaryScore = controller.getSummary().getScore();
			cell = new Label(column, row,
					new Double(totalSummaryScore).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			int totalSimplifiedSentences = controller.getPostProcessedText()
					.getTotalSentences();
			cell = new Label(column, row,
					new Integer(totalSimplifiedSentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			int totalSimplifiedWords = controller.getPostProcessedText()
					.getTotalWords();
			cell = new Label(column, row,
					new Integer(totalSimplifiedWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			double totalSimplifiedScore = controller.getPostProcessedText()
					.getScore();
			cell = new Label(column, row,
					new Double(totalSimplifiedScore).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			row++;
			row++;

			this.totalDocuments += totalDocuments;
			this.totalSentences += totalSentences;
			this.totalWords += totalWords;
			this.totalSummarySentences += totalSummarySentences;
			this.totalSimplifiedSentences += totalSimplifiedSentences;
			this.totalSummaryWords += totalSummaryWords;
			this.totalSimplifiedWords += totalSimplifiedWords;
			this.totalSummaryScore += totalSummaryScore;
			this.totalSimplifiedScore += totalSimplifiedScore;

			column = 0;
			// Create table witch will contain document information.
			cell = new Label(column, row, "Nome do Ficheiro");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Frases do Texto");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Palavras do Texto");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "Score do texto");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Orações");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Palavras Orações");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Apostos");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Palavras Apostos");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Parentéticas");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Palavras Parentéticas");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Modificadores");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Palavras Modificadores");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, "# Árvores Nulas");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;

			for (Document document : documents) {
				cell = new Label(column, row, document.getFilename());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row, new Integer(
						document.getSentences()).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row,
						new Integer(document.getWords()).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row, new Double(document.getText()
						.getScore()).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;

				Collection<Sentence> sentences = controller
						.getPostProcessedText().getSentences();
				// Collection<Sentence> sentences =
				// document.getText().getSentences();
				// Collection<Sentence> sentences =
				// controller.getSummary().getSentences();
				int totalPhrasesWords = 0, totalAppositionsWords = 0, totalParentheticalsWords = 0, totalModifiersWords = 0;
				int phrases = 0, appositions = 0, parentheticals = 0, modifiers = 0, nullParseTree = 0;

				for (Sentence sentence : sentences) {

					Collection<Phrase> currentPhrases = sentence.getPhrases();

					if (currentPhrases != null) {

						for (Phrase phrase : currentPhrases)
							totalPhrasesWords += phrase.getSentence()
									.getTotalWords();

						phrases += currentPhrases.size();
					}
					currentPhrases = sentence.getParentheticals();

					if (currentPhrases != null) {
						for (Phrase parenthetical : currentPhrases)
							totalParentheticalsWords += parenthetical
									.getSentence().getTotalWords();

						parentheticals += sentence.getParentheticals().size();
					}

					currentPhrases = sentence.getModifiers();

					if (currentPhrases != null) {
						for (Phrase parenthetical : currentPhrases)
							totalModifiersWords += parenthetical.getSentence()
									.getTotalWords();

						modifiers += currentPhrases.size();
					}

					if (sentence.getParseTree() == null)
						nullParseTree++;
				}

				this.totalPhrases += phrases;
				this.totalAppositions += appositions;
				this.totalParentheticals += parentheticals;
				this.totalModifiers += modifiers;
				this.totalNullParseTrees += nullParseTree;

				this.totalPhrasesWords += totalPhrasesWords;
				this.totalAppositionsWords += totalAppositionsWords;
				this.totalParentheticalsWords += totalParentheticalsWords;
				this.totalModifiersWords += totalModifiersWords;

				cell = new Label(column, row, new Integer(phrases).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row,
						new Integer(totalPhrasesWords).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row,
						new Integer(appositions).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row,
						new Integer(totalAppositionsWords).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row,
						new Integer(parentheticals).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row, new Integer(
						totalParentheticalsWords).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row,
						new Integer(totalModifiersWords).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row, new Integer(modifiers).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;
				cell = new Label(column, row,
						new Integer(nullParseTree).toString());
				cell.setCellFormat(format);
				sheet.addCell(cell);
				column++;

				row++;
				column = 0;
			}

		} catch (Exception e) {
			System.err.println("\n[" + this.getClass() + ".writeSheet] "
					+ "IO Exception occured.\n" + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Finishes the statistics file.
	 * </p>
	 * <p>
	 * Builds the statistics summary.
	 * </p>
	 * <p>
	 * Writes the spreadsheet.
	 * </p>
	 */
	public void finishStatistics() {
		buildStatisticsSummary();
		closeWorkbook();
	}

	/**
	 * <p>
	 * Builds the statistics summary.
	 * </p>
	 */
	private void buildStatisticsSummary() {

		try {
			WritableSheet sheet = workbook.createSheet("Resumo",
					totalDocumentBlocks);

			for (int i = 0; i < 10; i++)
				sheet.setColumnView(i, 25);

			WritableCellFormat format = new WritableCellFormat();
			format.setAlignment(Alignment.CENTRE);
			int row = 0, column = 0;

			Label cell = new Label(column, row, "Número Total de Documentos: ");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalDocuments).toString());
			cell.setCellFormat(format);
			column++;
			sheet.addCell(cell);
			cell = new Label(column, row, new Double((double) totalDocuments
					/ (double) totalDocumentBlocks).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "Número Total de Frases: ");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalSentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Double((double) totalSentences
					/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "Número Total de Palavras: ");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Integer(totalWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Double((double) totalWords
					/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			row++;

			column = 0;
			cell = new Label(column, row, "# Frases do Sumário");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalSummarySentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double((double) totalSummarySentences
							/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Palavras do Sumário");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalSummaryWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Double((double) totalSummaryWords
					/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "Score do Sumário");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double(totalSummaryScore).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Double((double) totalSummaryScore
					/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Frases do Simplificado");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalSimplifiedSentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double((double) totalSimplifiedSentences
							/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Palavras do Simplificado");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalSimplifiedWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double((double) totalSimplifiedWords
							/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "Score do Simplificado");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double(totalSimplifiedScore).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double((double) totalSimplifiedScore
							/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			row++;

			column = 0;
			cell = new Label(column, row, "# Orações");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Integer(totalPhrases).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Double((double) totalPhrases
					/ (double) totalSentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Palavras Orações");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalPhrasesWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Double((double) totalPhrasesWords
					/ (double) totalPhrases).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Apostos");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalAppositions).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Double((double) totalAppositions
					/ (double) totalSentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Palavras Apostos");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalAppositionsWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double((double) totalAppositionsWords
							/ (double) totalAppositions).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Parentéticas");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalParentheticals).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double((double) totalParentheticals
							/ (double) totalSentences).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Palavras Parentéticas");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalParentheticalsWords).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row, new Double(
					(double) totalParentheticalsWords
							/ (double) totalParentheticals).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;
			cell = new Label(column, row, "# Árvores Nulas");
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Integer(totalNullParseTrees).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column++;
			cell = new Label(column, row,
					new Double((double) totalNullParseTrees
							/ (double) totalDocuments).toString());
			cell.setCellFormat(format);
			sheet.addCell(cell);
			column = 0;
			row++;

		} catch (Exception e) {
			System.err.println("\n[" + this.getClass() + ".writeSheet] "
					+ "IO Exception occured.\n" + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Writes the spreadsheet.
	 * </p>
	 */
	private void closeWorkbook() {

		try {
			workbook.write();
			// Closes the file.
			workbook.close();
		} catch (Exception e) {
			System.err.println("\n[" + this.getClass() + ".closeWorkbook] "
					+ "IO Exception occured.\n" + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}

	/** Getters & Setters **/
	public void setController(SumController controller) {
		this.controller = controller;
	}

}
