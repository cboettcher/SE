package application.accounting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Buchhaltung {
	public static void call_main(String args[]) {
		String filestr = "";
		double p = 0.0; //zins
		File inFile = null;
	
		if (args.length != 2) {
			System.err.println("Wrong argument number! " + args.length);
			Scanner sc = new Scanner(System.in);
			System.out.print("Reading Filename from stdin: ");
			filestr = sc.next();
			System.out.print("Reading interest from stdin: ");
			p = sc.nextDouble();
			
			sc.close();
			
		}
		else {
			filestr = args[0];
		
			try {
				inFile = new File(filestr);
				if (!inFile.exists()) {
					throw new FileNotFoundException();
				}
			} catch (NullPointerException | FileNotFoundException e) {
				System.err.println("File " + filestr + " does not exist.");
				System.exit(1);
			}

			p = 0.0; // Zinssatz
			try {
				p = Double.parseDouble(args[1]);
			} catch (NumberFormatException e) {
				System.err.println(args[1] + " is not a number");
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
		
		System.out.println(header);
		sc.close();
		writeNewyear(sparer);

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

	public static void writeNewyear(ArrayList<Sparer> sparer) {
		for (int i = 0; i < sparer.size(); i++) {
			sparer.get(i).calcNewYear();
			System.out.println(sparer.get(i));
		}
		System.out.println();
	}
}
