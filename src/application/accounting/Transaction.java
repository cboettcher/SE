package application.accounting;

public class Transaction {
	private int day;
	private int money;

	public Transaction(int d, int m) {
		this.day = d;
		this.money = m;
	}

	public int getMoney() {
		return this.money;
	}

	public int getDay() {
		return this.day;
	}
}
