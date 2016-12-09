package unifi.inf.rc.DanieleBisignano;

import java.util.Arrays;

public class HttpUser implements HttpProtocol{

	private String[] params;
	private String userName;
	public HttpUser(String[] params) {
		this.params = params;
		this.userName = String.join(" ", params);
		// TODO Auto-generated constructor stub
	}

	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response = "OK\r\n";
		
		//if(this.params.length == 1){
		//	response = "OK\r\n";
			clientHandler.acceptVisit(this);
		//}
		
		return response;
	}
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return "";
	}*/

	public String getUserName() {
		// TODO Auto-generated method stub
		return this.userName;
	}


}
