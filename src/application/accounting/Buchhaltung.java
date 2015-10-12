package application.accounting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;

public class Buchhaltung {

	private static FileWriter outwriter = null;

	public static void call_main(String args[]) throws IOException {
		String filestr = "";
		double p = 0.0; //zins
		File inFile = null;
		String outfileStr = "";
		File outFile = null;
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
 			
 			filestr = argP.getInputFilename();
 			outfileStr = argP.getOutputFilename();
 			String rest = argP.getNonOptions();
 			try {
				p = Double.parseDouble(rest);
			} catch(NumberFormatException e) {
				System.err.println(rest + " is no number!");
			}
 			
		}
		try {
			inFile = new File(filestr);
			if (!inFile.exists()) {
				throw new FileNotFoundException();
			}
		} catch (NullPointerException | FileNotFoundException e) {
			System.err.println("File " + filestr + " does not exist.");
			System.exit(1);
		}
		
		try {
			outFile = new File(outfileStr);
		} catch (NullPointerException e) {
			System.err.println("File " + filestr + " not readable.");
			System.exit(1);
		}
		
		outwriter = new FileWriter(outFile);
			
		Scanner sc = null;
		try {
			sc = new Scanner(inFile);
		} catch (FileNotFoundException e) {
			// should not happen
			e.printStackTrace();
			System.exit(1);
		}

		// now we have all relevant data
		String curLine = "";
		String splits[];
		String id;
		String lastName;
		String firstName;
		int startingMoney;
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
			startingMoney = getIntValue(splits[3]);
			tmp = new Sparer(id, lastName, firstName, startingMoney);
			sparer.add(tmp);

			for (int i = 4; i < splits.length; i += 2) {
				day = Integer.parseInt(splits[i]);
				tmpMoney = getIntValue(splits[i + 1]);
				tmp.addTransaction(day, tmpMoney);
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
