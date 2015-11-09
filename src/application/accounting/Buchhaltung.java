package application.accounting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class Buchhaltung {

	private static FileWriter outwriter = null;
	
	private static final Logger logger = Logger.getLogger(Buchhaltung.class.getName());

	public static void call_main(String args[]) throws IOException {
		String filestr = "";
		double p = 0.0; //zins
		File inFile = null;
		String outfileStr = "";
		File outFile = null;
		boolean help = false;
		
		//set log level
		logger.setLevel(Level.ALL);
		
		//get resource bundle
		String baseName = "Buchhaltung";
		File file = new File("./dist/data/lang/");
		ResourceBundle rb = null;
		
		try {
		  URL[] urls = {file.toURI().toURL()};
		  ClassLoader loader = new URLClassLoader(urls);
		  rb = ResourceBundle.getBundle(baseName, Locale.getDefault(), loader);
		}
		catch (MalformedURLException e) {
		  e.printStackTrace();
		}
		
		
		//get params
		if (args.length == 0) {
			Scanner sc = new Scanner(System.in);
			System.out.print("Reading Filename from stdin: ");
			filestr = sc.next();
			System.out.print("Reading interest from stdin: ");
			p = sc.nextDouble();
			System.out.print("Reading outFile from stdin: ");
			outfileStr = sc.next();
			
			sc.close();
			
		}
		else {
			ArgParser argP = new ArgParser(args);
 			
 			help = argP.getShowHelp();
 			filestr = argP.getInputFilename();
 			outfileStr = argP.getOutputFilename();
 			String logFileString = argP.getLogFilename();
 			String rest = argP.getNonOptions();
 			FileHandler fh = null;
 			
 			// user needs help
			if (help) {
				System.err.println("-i | --input-file <filename>\n-o | --output-file <filename>\n-l | --log-file <filename>\n");
				System.exit(0);
			}
 			
 			try {
				p = Double.parseDouble(rest);
			} catch(NumberFormatException e) {
				System.err.println(rest + " is no number!");
			}
			
			try {
				boolean append = true;
				fh = new FileHandler(logFileString, append);
				fh.setFormatter(new Formatter() {
					public String format(LogRecord rec) {
						StringBuffer buf = new StringBuffer(1000);
						buf.append(new java.util.Date()).append(' ');
						buf.append(rec.getLevel()).append(' ');
						buf.append(formatMessage(rec)).append('\n');
						
						return buf.toString();
					}
			});
			
			logger.addHandler(fh);
			} catch (IOException e) {
				logger.severe("Datei kann nicht geschrieben werden");
				e.printStackTrace();
			}
 			
		}
		
		//get infile
		try {
			inFile = new File(filestr);
			if (!inFile.exists()) {
				throw new FileNotFoundException();
			}
		} catch (NullPointerException | FileNotFoundException e) {
			System.err.println("File " + filestr + " does not exist.");
			System.exit(1);
		}
		String readinput_msg = rb.getString("readinput_msg");
		logger.info(readinput_msg + ": " + filestr);
		
		
		//get outfile
		try {
			outFile = new File(outfileStr);
		} catch (NullPointerException e) {
			System.err.println("File " + filestr + " not writeable.");
			System.exit(1);
		}
		logger.info("Schreibe in Datei: " + outfileStr);
		
		outwriter = new FileWriter(outFile);
			
		Scanner sc = null;
		try {
			sc = new Scanner(inFile);
		} catch (FileNotFoundException e) {
			// should not happen
			e.printStackTrace();
			System.exit(1);
		}
		
		logger.info("Using Interest: " + p + "%");

		// now we have all relevant data
		String curLine = "";
		String splits[];
		String id;
		String lastName;
		String firstName;
		int startingMoney = 0;
		int tmpMoney;
		int day;
		Sparer tmp;
		ArrayList<Sparer> sparer = new ArrayList<Sparer>();

		Sparer.setZins(p);

		boolean isHeader = true;
		String header = "";
		
		
		// while there are lines left
		while (sc.hasNext()) {
			curLine = sc.nextLine();
			logger.log(Level.FINE, "gelesene Zeile: " + curLine);

			// if line starts with '#' or has no content
			if (curLine.startsWith("#") || curLine.matches("[ ]*")) {
				if (isHeader) {
					header = header + "\n" + curLine;
				}
				continue;
			}
			isHeader = false;

			splits = curLine.split(";");
			if (splits.length < 4) {
				System.err
						.println("There are errors in the file.\n"
								+ "At least one line has not enough values.\n"
								+ "Please resolve these errors and re-run the program.");
				System.exit(2);
			}
			id = splits[0];
			lastName = splits[1];
			firstName = splits[2];
			try {
				startingMoney = getIntValue(splits[3]);
			} catch (NumberFormatException e) {
				System.err.println("'" + splits[3] + "' is not a number!\n"
				  + "Please resolve errors in the input file and try again");
				System.exit(1);
			}
			tmp = new Sparer(id, lastName, firstName, startingMoney);
			sparer.add(tmp);

			for (int i = 4; i < splits.length; i += 2) {
				try {
					day = Integer.parseInt(splits[i]);
					tmpMoney = getIntValue(splits[i + 1]);
					tmp.addTransaction(day, tmpMoney);
				} catch (NumberFormatException e1) {
					System.err.println(splits[i] + ";" + splits[i+1] + " is not a proper entry!");
					System.exit(1);
				} catch (ArrayIndexOutOfBoundsException e1) {
					System.err.println("A Line contains the wrong Number of entries. Please check your File for Errors");
					System.err.println(curLine);
					System.exit(1);
				}
			}
		}
		
		outwriter.write(header + "");
		sc.close();
		
		writeNewyear(sparer);
		
		
		outwriter.close();
		
	}

	public static int getIntValue(String number) {
		String split[] = number.split(",");
		int value = Integer.parseInt(split[0]) * 100;
		if (split.length == 2) {
			// get the value after the comma
			value += (split[1].length() == 2) ? (Integer.parseInt(split[1]))
					: (Integer.parseInt(split[1]) * 10);
		}

		return value;

	}

	public static void writeNewyear(ArrayList<Sparer> sparer) throws IOException{
		for (int i = 0; i < sparer.size(); i++) {
			sparer.get(i).calcNewYear();
			outwriter.write(sparer.get(i) + "");
			outwriter.write("\n");
		}
		outwriter.write("\n");
	}
}
