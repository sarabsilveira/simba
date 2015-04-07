package run;

import preferences.Preferences;
import ui.UserInterface;
import external.ManageExternalTools;

/**
 * <p>
 * This class runs the summarizer.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class RunSummarizer {

	public RunSummarizer() {
		// Loads the system preferences.
		Preferences.load();
		ManageExternalTools.load();
	}

	/**
	 * <p>
	 * Runs the summarizer.
	 * </p>
	 * 
	 * @param args
	 *            , main arguments.
	 */
	public static void main(String[] args) {

		UserInterface ui = new UserInterface();

		if (args.length == 1) {

			if (args[0].startsWith("BASELINE")) {
				String whichBaseline = args[0].split("-")[1];

				if (whichBaseline.equals("SUMMARIZER"))
					ui.baselineSummarizer();
				else if (whichBaseline.equals("SIMPLIFIER"))
					ui.baselineSimplifier();
				else if (whichBaseline.equals("PARAGRAPHS"))
					ui.baselineParagraphs();
				else if (whichBaseline.equals("CONNECTIVES"))
					ui.baselineConnectives();
			}
		} else if (args.length == 2) {

			if (args[0].equals("SUM"))
				ui.summarizeByDefault("", args[1]);
		} else if (args.length == 3) {

			if (args[0].equals("SUM"))
				ui.summarizeByDefault(args[1], args[2]);
		} else
			ui.run();
	}

	public String run(String location, String type, String compression) {
		UserInterface ui = new UserInterface();
		return ui.summarize(location, type, Double.parseDouble(compression));
	}
}
