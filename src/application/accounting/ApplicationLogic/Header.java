package application.accounting.ApplicationLogic;

public class Header {
	private String header;
	public Header(String h) {
		this.header = h;
	}
	
	
	public Header() {
	}
	
	@Override
	public String toString() {
		return this.header;
	}

}