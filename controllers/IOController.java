package controllers;

import io.ManageInput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import preferences.Preferences;
import preferences.Regex;
import summarization.Summary;
import core.Document;
import core.Text;
import edu.stanford.nlp.ling.StringLabelFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeReader;

/**
 * <p>
 * This class manages the input/output process.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class IOController extends Controller {

	/**
	 * <p>
	 * Parse trees for the collection of sentences.
	 * </p>
	 **/
	protected HashMap<String, Collection<Tree>> parseTrees;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 */
	public IOController() {
		parseTrees = new HashMap<String, Collection<Tree>>();
	}

	/**
	 * <p>
	 * Builds the system output.
	 * </p>
	 * 
	 * @param output
	 *            , the object retrieved by the process chosen.
	 */
	public void buildOutput(Object output) {

		if (output instanceof Summary) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						Preferences.OUTPUT_FILE));
				writer.write(((Summary) output).toString());
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <p>
	 * Builds the document collection from the documents present in the folder
	 * submitted by the user.
	 * </p>
	 * 
	 * @param documentsLocation
	 *            the folder containing the input documents.
	 * @return the document collection.
	 */
	public Collection<Document> manageInputDocuments(String documentsLocation) {

		Collection<Document> documents = new LinkedList<Document>();

		try {
			File homeDirectory = new File(documentsLocation);

			File[] files = homeDirectory.listFiles();

			Arrays.sort(files);

			if (files != null) {
				for (int j = 0; j < files.length; j++) {
					File current = preProcessFile(files[j]);

					String original = readInputFile(current);
					Text text = new Text(original);
					Document document = new Document(j, files[j].getName(),
							text);

					if (Preferences.EVALUATION_PROCEDURE)
						readParsesFile(files[j].getName());

					documents.add(document);

					current.delete();
				}
			}
		} catch (Exception e) {
			System.out.println("============= ERROR in class ["
					+ this.getClass()
					+ ".manageInputDocuments].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return documents;
	}

	/**
	 * <p>
	 * Reads the input file.
	 * </p>
	 * 
	 * @param current
	 *            the file to be read.
	 * @return a String containing the file content.
	 * @throws FileNotFoundException
	 *             if the file does not exist;
	 */
	private String readInputFile(File current) throws FileNotFoundException {
		Scanner input = new Scanner(new FileReader(current));
		String original = "";

		while (input.hasNextLine()) {
			String line = input.nextLine().trim();

			line = line.replaceAll("(.*?" + Regex.WORD + ")[.](" + Regex.WORD
					+ ".*?)", "$1. $2");
			line = line.trim();

			// If the line does not end with a point, we'll add the point at the
			// end of the sentence.
			if (!line.equals("")
					&& !line.matches(".*?(?:" + Regex.END_SENTENCE + ")$"))
				line += ".";

			if (line != null && !line.equals(""))
				original += line + "\n";

		}

		input.close();
		return original;
	}

	/**
	 * <p>
	 * Reads the file containing the sentence parse trees.
	 * </p>
	 * 
	 * @param fileLocation
	 *            the filename.
	 */
	private void readParsesFile(String fileLocation)
			throws FileNotFoundException {

		Scanner input = new Scanner(new FileReader(Preferences.PARSES_LOCATION
				+ fileLocation));
		LinkedList<Tree> trees = new LinkedList<Tree>();

		while (input.hasNextLine()) {
			String line = input.nextLine().trim();

			try {
				TreeReader reader = new PennTreeReader(new StringReader(line),
						new LabeledScoredTreeFactory(new StringLabelFactory()));
				trees.add(reader.readTree());
			} catch (java.io.IOException e) {
				System.err.println("Error creating phrase structure tree: "
						+ e.getMessage());
			}
		}

		parseTrees.put(fileLocation, trees);

		input.close();
	}

	/**
	 * <p>
	 * Pre-processes the file, by checking:
	 * <ul>
	 * <li>The extension to convert it to a text file.</li>
	 * <li>The encoding to convert it to UTF-8</li>
	 * </ul>
	 * </p>
	 * 
	 * @param file
	 *            the file to be processed.
	 * @return a temporary file to be used.
	 */
	private File preProcessFile(File file) {

		File tempFile = null;
		try {

			// Converts the file to a text file.
			if (!ManageInput.isTextFile(file))
				tempFile = ManageInput.createTextFile(file);

			// Converts the file content to UTF-8.
			tempFile = ManageInput.convert2utf8(file);
			tempFile.deleteOnExit();

		} catch (Exception e) {
			System.out.println("============= ERROR in class ["
					+ this.getClass() + ".preProcessFile].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return tempFile;
	}

	public HashMap<String, Collection<Tree>> getParseTrees() {
		return parseTrees;
	}
}
