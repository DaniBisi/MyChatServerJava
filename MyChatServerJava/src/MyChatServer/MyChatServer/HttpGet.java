package MyChatServer.MyChatServer;

import java.util.ArrayList;

public class HttpGet implements HttpProtocol {

	private String params[];
	public HttpGet(String params[]) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}
	
	public String execute() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (this.params.length != 1)throw new IllegalArgumentException();
		
		return "OK 1\r\n";
	}

}
