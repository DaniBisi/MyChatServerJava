package it.bisignano.mychatserver;

public class HttpUser implements IHttpProtocol{

	private String[] params;
	private String userName;
	public HttpUser(String[] params) {
		this.params = params;
		this.userName = params[0];
		// TODO Auto-generated constructor stub
	}

	public String execute(ClientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response = "KO\r\n";
		if(this.params.length == 1){
			response = "OK\r\n";
			clientHandler.acceptVisit(this);
		}
		
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
