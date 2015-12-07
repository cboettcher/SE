package application.accounting;

import java.math.BigDecimal;

public class Transaction {
	private int day;
	private BigDecimal money;

	public Transaction(int d, BigDecimal m) {
		this.day = d;
		this.money = m;
	}

	public BigDecimal getMoney() {
		return this.money;
	}

	public int getDay() {
		return this.day;
	}
}
