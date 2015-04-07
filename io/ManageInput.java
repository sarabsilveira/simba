package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import preferences.Preferences;

/**
 * <p>
 * This class manages the input of the summarizer.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class ManageInput {

	/**
	 * TODO
	 * <p>
	 * Retrieves (by guessing) the file encoding, using the external tool
	 * <code>CharsetToolkit</code>.
	 * </p>
	 * 
	 * @param file
	 *            the file which encode will be guessed.
	 * @return a String containing the file encoding.
	 */
	public static String getEncoding(File file) {
		try {
			return Preferences.UTF8;
		} catch (Exception e) {
			System.out
					.println("============= ERROR in class [ManageInput.getEncoding].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * Converts the input file to UTF-8 format.
	 * </p>
	 * 
	 * @param file
	 *            the input file.
	 * @return a temporary converted file.
	 */
	public static File convert2utf8(File file) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile(file.getName(), "");

			FileInputStream fis = new FileInputStream(file);
			byte[] contents = new byte[fis.available()];
			fis.read(contents, 0, contents.length);
			String asString = new String(contents,
					ManageInput.getEncoding(file));
			byte[] newBytes = asString.getBytes(Preferences.UTF8);
			FileOutputStream fos = new FileOutputStream(tempFile);
			fos.write(newBytes);
			fos.close();

			tempFile.deleteOnExit();
		} catch (Exception e) {
			System.out
					.println("============= ERROR in class [ManageInput.convert2utf8].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return tempFile;
	}

	/**
	 * <p>
	 * Checks if the file is a text file.
	 * </p>
	 * 
	 * @param file
	 *            the file to be checked.
	 * @return true if the file is a text file, otherwise false.
	 */
	public static boolean isTextFile(File file) {
		return file.getName().endsWith(".txt");
	}

	/**
	 * <p>
	 * Creates a text file from the input file.
	 * </p>
	 * 
	 * @param file
	 *            the file to be converted.
	 * @return the text file (in UTF).
	 */
	public static File createTextFile(File file) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile(file.getName(), "");

			tempFile.deleteOnExit();
		} catch (Exception e) {
			System.out
					.println("============= ERROR in class [ManageInput.createTextFile].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return tempFile;
	}

}
