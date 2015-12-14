package application.accounting.ApplicationLogic;

import java.sql.Timestamp;

public class Header_Timestamp extends Header{
	private Header header;
	public Header_Timestamp(Header h) {
		super();
		this.header = h;
	}
	
	@Override
	public String toString() {
		return this.header + "\nZuletzt geaendert: " + Header_Timestamp.getTimeStamp() + "\n\n\n";
	}
	
	private static String getTimeStamp() {
		return (new Timestamp(System.currentTimeMillis())).toString();
	}

}