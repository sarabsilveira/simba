package io;

import java.io.BufferedWriter;
import java.io.FileWriter;

import preferences.Preferences;

/**
 * <p>
 * This class manages the output of the summarizer.
 * </p>
 * 
 * @author Sara Botelho Silveira
 * @version 2.0
 */
public class ManageOutput {
	
	
	
	/**
	 * <p>Prints the demonstration log file.</p>
	 * 
	 * @param demo the text to be printed to the log.
	 */
	public static void printDemoLog(String demo){
		
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(Preferences.DEMO_FILE));
			writer.write(demo);
			writer.close();
		}catch (Exception e) {
			System.out.println("============= ERROR in class [ManageOutput.printDemoLog].\nException trace:");
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
	}

}
