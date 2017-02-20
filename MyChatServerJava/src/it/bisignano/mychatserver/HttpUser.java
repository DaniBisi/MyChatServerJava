package it.bisignano.mychatserver;

public class HttpUser implements IHttpProtocol{

	private String[] params;
	private String userName;
	public HttpUser(String[] params) {
		this.params = params;
		this.userName = params[0];

	}
	//@Override
	public String execute(ClientHandler clientHandler) {

		String response = "KO\r\n";
		if(this.params.length == 1){
			response = "OK\r\n";
			clientHandler.acceptVisit(this);
		}
		
		return response;
	}

	public String getUserName() {
		return this.userName;
	}


}
