package application.accounting;

import java.util.ArrayList;

public class Sparer {

	public static double zins;
	private String id;
	private String last_name;
	private String first_name;
	private int starting_money;
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

	public static void setZins(double newZins) {
		zins = newZins;
	}

	public Sparer(String id, String last_name, String first_name,
			int starting_money) {
		this.id = id;
		this.last_name = last_name;
		this.first_name = first_name;
		this.starting_money = starting_money;
	}

	/**
	 * 
	 * @param day
	 * @return the value of the transaction performed on @day
	 */
	public int getTransaction(int day) {
		for (int i = 0; i < this.transactions.size(); i++) {
			if (transactions.get(i).getDay() == day) {
				return transactions.get(i).getMoney();
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param day
	 *            day of transaction
	 * @param value
	 *            value of transaction
	 */
	public void addTransaction(int day, int value) {
		transactions.add(new Transaction(day, value));
	}

	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(id + ';');
		ret.append(this.last_name + ';');
		ret.append(this.first_name + ';');
		ret.append(this.starting_money / 100 + "," + this.starting_money % 100);
		// we are assuming that there are no transactions left, when "sparer" is
		// printed
		return ret.toString();
	}

	/**
	 * clears the transactions and calculates the new starting money
	 */
	public void calcNewYear() {
		int newMoney = 0;
		for (int i = 0; i < this.transactions.size(); i++) {
			newMoney += (transactions.get(i).getMoney())
					* ((zins / 100)
							* ((360 - this.transactions.get(i).getDay()) / 360) + 1);
		}
		this.transactions.clear();
		newMoney += (this.starting_money * (1 + (zins / 100)));

		this.starting_money = newMoney;
	}
}
