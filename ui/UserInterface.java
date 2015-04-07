package ui;

import io.ManageOutput;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import options.EvalOptions;
import options.Option;
import options.SimOptions;
import options.SumOptions;
import preferences.Preferences;
import relation.Relation;
import statistics.Statistic;
import summarization.Summary;
import controllers.EvalController;
import controllers.IOController;
import controllers.PostProcessingController;
import controllers.RelController;
import controllers.SumController;
import evaluation.Metric;

/**
 * <p>
 * This class defines the user interface.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class UserInterface {

	/**
	 * <p>
	 * System output
	 * </p>
	 **/
	private Object output;
	/**
	 * <p>
	 * System options
	 * </p>
	 **/
	private Option options;

	/**
	 * <p>
	 * Runs the Summarizer.
	 * </p>
	 */
	public void run() {
		menu();
		buildOutput();
	}

	/**
	 * <p>
	 * Builds output.
	 * </p>
	 */
	private void buildOutput() {
		IOController mo = new IOController();
		System.out.println("Print output file...");
		mo.buildOutput(output);
	}

	/**
	 * <p>
	 * Displays system menu.
	 * </p>
	 */
	private void menu() {
		System.out.println("\n\n=== Menu ===\n");
		System.out
				.println("1) Summarize\n2) Simplify\n3) Headline\n4) Relate\n5) Evaluate\n6) Exit");
		Scanner input = new Scanner(System.in);
		System.out.print("Option: ");
		int option = input.nextInt();
		System.out.println();

		switch (option) {
		case 1:
			buildSummary();
			break;
		case 2:
			simplifyText();
			break;
		case 3:
			generateHeadlines();
			break;
		case 4:
			relateDocuments();
			break;
		case 5:
			evaluateFeatures();
			break;
		case 6:
			System.out.println("Goodbye! Thank you for using "
					+ Preferences.NAME + "!");
			break;
		default:
			menu();
			break;
		}
	}

	/**
	 * <p>
	 * Manages the summary creation.
	 * </p>
	 */
	private void buildSummary() {
		displaySummarizationOptions();
		SumController sc = new SumController();
		askForDocuments();
		sc.submitOptions(options);
		sc.submitDocuments();
		Summary summary = sc.buildSummary();
		output = summary;
	}

	/**
	 * <p>
	 * Manages the text simplification.
	 * </p>
	 */
	private void simplifyText() {
		PostProcessingController sc = new PostProcessingController();
		options = new SimOptions();
		askForDocuments();
		sc.submitOptions(options);
		sc.submitDocuments();
		sc.postprocessText();
		output = sc.getPostProcessedText();
	}

	/**
	 * <p>
	 * Manages the headline generation.
	 * </p>
	 */
	private void generateHeadlines() {
		displayHeadlineOptions();
		SumController sc = new SumController();
		askForDocuments();
		sc.submitOptions(options);
		sc.submitDocuments();
		Summary summary = sc.generateHeadlines();
		output = summary;
	}

	/**
	 * <p>
	 * Manages the computation of relation between the collection of documents
	 * and the summary.
	 * </p>
	 */
	private void relateDocuments() {
		RelController rc = new RelController();
		options = new SumOptions();
		askForDocuments();
		rc.submitOptions(options);
		rc.submitDocuments();
		Collection<Relation> relations = rc.relateDocuments();
		output = relations;
	}

	/**
	 * <p>
	 * Manages the system evaluation.
	 * </p>
	 */
	private void evaluateFeatures() {
		EvalController ec = new EvalController();
		displayEvaluationOptions();

		if (((EvalOptions) options).buildAutomaticSummaries())
			askForAutomaticSummaries();
		else
			buildSummary();

		askForIdealSummaries();
		ec.submitOptions(options);
		ec.manageDocuments();
		Collection<Metric> metrics = ec.evaluateFeatures();
		output = metrics;
	}

	/**
	 * <p>
	 * Asks the user to submit documents.
	 * </p>
	 */
	private void askForDocuments() {

		Scanner input = new Scanner(System.in);
		System.out.print("Input documents location: ");
		String location = input.nextLine();

		options.setDocumentsLocation(location);
	}

	/**
	 * <p>
	 * Asks the user to submit the ideal summaries.
	 * </p>
	 */
	private void askForIdealSummaries() {

		Scanner input = new Scanner(System.in);
		System.out.print("\nManual summaries location: ");
		String manualLocation = input.nextLine();
		System.out.println();

		((EvalOptions) options).setManualLocation(manualLocation);
	}

	/**
	 * <p>
	 * Asks the user to submit automatic summaries already build.
	 * </p>
	 */
	private void askForAutomaticSummaries() {

		Scanner input = new Scanner(System.in);
		System.out.print("\nAutomatic summaries location: ");
		String automaticLocation = input.nextLine();

		((EvalOptions) options).setAutomaticLocation(automaticLocation);
	}

	/**
	 * <p>
	 * Displays and retrieves the summarization options, submitted by the user.
	 * </p>
	 */
	private void displaySummarizationOptions() {

		Scanner input = new Scanner(System.in);

		System.out.println(" === Summarization options === ");
		System.out.print("Query or keywords: ");
		String query = input.nextLine();
		System.out.print("Compression rate: ");
		double compressionRate = input.nextDouble();
		System.out.print("Output format: (T)ext or (L)ist? ");
		String outputType = input.next();
		System.out.println();

		if (outputType.equals("L"))
			options = new SumOptions(compressionRate,
					Preferences.OutputType.LIST, query);
		else
			options = new SumOptions(compressionRate,
					Preferences.OutputType.TEXT, query);
	}

	/**
	 * <p>
	 * Displays and retrieves the headline generation options, submitted by the
	 * user.
	 * </p>
	 */
	private void displayHeadlineOptions() {
		Scanner input = new Scanner(System.in);

		System.out.println(" === Summarization options === ");
		System.out.print("Query or keywords: ");
		String query = input.nextLine();
		System.out.print("Output format: (T)ext | (L)ist? ");
		String outputType = input.next();
		System.out.println();

		if (outputType.equals("L"))
			options = new SumOptions(Preferences.OutputType.LIST, query);
		else
			options = new SumOptions(Preferences.OutputType.TEXT, query);
	}

	/**
	 * <p>
	 * Displays and retrieves the evaluation options, submitted by the user.
	 * </p>
	 */
	private void displayEvaluationOptions() {

		Scanner input = new Scanner(System.in);

		System.out.println(" === Evaluation options === ");
		System.out
				.print("Type of evaluation: (R)ouge | (O)bjective | (B)oth ? ");
		String type = input.nextLine();
		System.out.print("Build automatic summaries: (Y)es | (N)o ? ");
		String buildSummaries = input.nextLine();
		System.out.println();

		if (type.equals("R"))
			options = new EvalOptions(Preferences.EvaluationType.ROUGE,
					(buildSummaries.equalsIgnoreCase("Y") ? false : true));
		else if (type.equals("B"))
			options = new EvalOptions(Preferences.EvaluationType.BOTH,
					(buildSummaries.equalsIgnoreCase("Y") ? false : true));
		else
			options = new EvalOptions(Preferences.EvaluationType.OBJECTIVE,
					(buildSummaries.equalsIgnoreCase("Y") ? false : true));
	}

	/**
	 * <p>
	 * Runs the summarization system with default values.
	 * </p>
	 * 
	 * <p>
	 * <b>Note:</b>Method to be used by the developer.
	 * </p>
	 */
	public void summarizeByDefault(String query, String type) {
		long startTime = System.currentTimeMillis();

		File homeDirectory = new File(Preferences.INPUT_DIR_LOCATION);
		File[] folders = homeDirectory.listFiles();

		Statistic stats = new Statistic();
		SumController sc = null;

		for (int i = 0; i < folders.length; i++) {
			System.out.println("[Processing folder: "
					+ folders[i].getAbsolutePath() + "]");
			sc = new SumController();

			options = new SumOptions(Preferences.DEFAULT_COMPRESSION_RATE,
					Preferences.OutputType.TEXT, query);
			options.setDocumentsLocation(folders[i].getAbsolutePath());
			Preferences.OUTPUT_FILE = Preferences.OUTPUT_DEFAULT_LOCATION
					+ folders[i].getName() + ".txt";
			sc.submitOptions(options);
			Summary summary = null;

			Preferences.setFinalSummaryType(type);

			if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.SIMPLIFIED)
				summary = sc.buildSummary();// DEVE RETORNAR UMA STRING COM A
											// LOCALIZAÇÃO DOS TEXTOS???
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.NONSIMPLIFIED)
				summary = sc.buildNonSimplifiedSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.NONPOSTPROCESSED)
				summary = sc.buildNonPostProcessedSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.RANDOMBASELINE)
				summary = sc.buidRandomSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.SIMPLIFICATIONBASELINE)
				summary = sc.buildSimplificationBaselineSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.PARAGRAPHBASELINE)
				summary = sc.buildParagraphsBaselineSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.CONNECTIVESBASELINE)
				summary = sc.buildConnectivesBaselineSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.ONLYPOSTPROCESSEDNOSUMMARY) {
				((SumOptions) options).setCompressionRate(1);
				sc.submitOptions(options);
				summary = sc.postProcessText();
			}

			output = summary;
			buildOutput();

			ManageOutput.printDemoLog(sc.getDemoLog());

			stats.setController(sc);
			stats.buildStatistics(folders[i].getName(), i + 1);
			sc.clear();
		}

		stats.finishStatistics();

		long endTime = System.currentTimeMillis(), execution = endTime
				- startTime;

		System.out.println("\n\n[Final time: " + new Date(execution).toString()
				+ "]");
	}

	/**
	 * <p>
	 * Runs the summarization system from the servlet.
	 * </p>
	 */
	public String summarize(String directory, String type, double compression) {
		String finalSummary = "";
		long startTime = System.currentTimeMillis();

		File homeDirectory = new File(directory);
		File[] folders = homeDirectory.listFiles();

		SumController sc = null;

		for (int i = 0; i < folders.length; i++) {
			System.out.println("[Processing folder: "
					+ folders[i].getAbsolutePath() + "]");
			sc = new SumController();

			options = new SumOptions(compression, Preferences.OutputType.TEXT,
					"");
			String absolutePath = folders[i].getAbsolutePath();
			options.setDocumentsLocation(absolutePath);
			sc.submitOptions(options);
			Summary summary = null;

			Preferences.setFinalSummaryType(type);

			if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.SIMPLIFIED)
				summary = sc.buildSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.NONSIMPLIFIED)
				summary = sc.buildNonSimplifiedSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.NONPOSTPROCESSED)
				summary = sc.buildNonPostProcessedSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.RANDOMBASELINE)
				summary = sc.buidRandomSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.SIMPLIFICATIONBASELINE)
				summary = sc.buildSimplificationBaselineSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.PARAGRAPHBASELINE)
				summary = sc.buildParagraphsBaselineSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.CONNECTIVESBASELINE)
				summary = sc.buildConnectivesBaselineSummary();
			else if (Preferences.SUMMARY_TYPE == Preferences.FINAL_SUMMARY_TYPE.ONLYPOSTPROCESSEDNOSUMMARY) {
				((SumOptions) options).setCompressionRate(1);
				sc.submitOptions(options);
				summary = sc.postProcessText();
			}

			output = summary;

			finalSummary = summary.toString();

			sc.clear();
		}

		long endTime = System.currentTimeMillis(), execution = endTime
				- startTime;

		System.out.println("\n\n[Final time: " + new Date(execution).toString()
				+ "]");
		return finalSummary;
	}

	public void baselineSummarizer() {
		this.summarizeByDefault("", "RANDOM-BASELINE");
	}

	public void baselineSimplifier() {
		this.summarizeByDefault("", "SIMPLIFICATION-BASELINE");
	}

	public void baselineParagraphs() {
		this.summarizeByDefault("", "PARAGRAPHS-BASELINE");
	}

	public void baselineConnectives() {
		this.summarizeByDefault("", "CONNECTIVES-BASELINE");
	}
}
