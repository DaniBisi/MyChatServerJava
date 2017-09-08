package unifi.inf.rc.DanieleBisignano;

import java.util.Arrays;

public class HttpUser implements HttpProtocol {

	private String[] params;
	private String userName;

	public HttpUser(String[] params) {
		this.params = params;
		this.userName = String.join(" ", params);
	}

	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		String response = "OK\r\n";
		clientHandler.acceptVisit(this);
		return response;
	}

	public String getUserName() {
		return this.userName;
	}

}
