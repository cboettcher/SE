package application.accounting.ApplicationLogic;

import java.util.ArrayList;
import java.math.BigDecimal;

public class Sparer {

	//public static double zins;
	public static BigDecimal zins;
	private String id;
	private String last_name;
	private String first_name;
	private BigDecimal starting_money;
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	public String getID() {
		return this.id;
	}

	public static void setZins(BigDecimal newZins) {
		zins = newZins;
	}

	public Sparer(String id, String last_name, String first_name,
			BigDecimal starting_money) {
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
	public BigDecimal getTransaction(int day) {
		for (int i = 0; i < this.transactions.size(); i++) {
			if (transactions.get(i).getDay() == day) {
				return transactions.get(i).getMoney();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param day
	 *            day of transaction
	 * @param value
	 *            value of transaction
	 */
	public void addTransaction(int day, BigDecimal value) {
		transactions.add(new Transaction(day, value));
	}

	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(id + ';');
		ret.append(this.last_name + ';');
		ret.append(this.first_name + ';');
		ret.append(this.starting_money.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
		// we are assuming that there are no transactions left, when "sparer" is
		// printed
		return ret.toString();
	}

	/**
	 * clears the transactions and calculates the new starting money
	 */
	public void calcNewYear() {
		BigDecimal newMoney = new BigDecimal(0);
		BigDecimal tmp = null;
		for (int i = 0; i < this.transactions.size(); i++) {
			tmp = Sparer.interestAmount(this.transactions.get(i).getMoney(), Sparer.zins, this.transactions.get(i).getDay());
			newMoney = newMoney.add(tmp).add(this.transactions.get(i).getMoney());
		}
		this.transactions.clear();
		newMoney = newMoney.add(this.starting_money.multiply(((zins.divide(new BigDecimal(100))).add(new BigDecimal(1)))));

		this.starting_money = newMoney;
	}
	
	public static BigDecimal interestAmount(BigDecimal amount, BigDecimal interestRate , int days) {
		BigDecimal ret = new BigDecimal(amount.doubleValue());
		ret = ret.multiply(interestRate.divide(new BigDecimal(100)));
		ret = ret.multiply(new BigDecimal(((360 - days) / 360) + 1));
		
		return ret;
	}
	
}
