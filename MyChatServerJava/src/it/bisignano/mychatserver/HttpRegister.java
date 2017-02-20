package it.bisignano.mychatserver;

public class HttpRegister implements IHttpProtocol {
	
	private int loginResult;
	private String[] params;

	public HttpRegister(String[] params) {
		this.params = params;

	}

	//@Override
	public String execute(ClientHandler clientHandler) {

		String response;
		if(params == null || params.length != 2) {
			throw new IllegalArgumentException();
		}
		else{
			String host = params[0];
			int port = Integer.parseInt(params[1]);
			String user = clientHandler.getUserName();
			if(MyChatServer.addRecord(host, port, user)){
				this.loginResult = 3;
				clientHandler.acceptVisit(this);
				response = "OK\r\n";
			}
			else response = "KO\r\n";
		}
		
		return response;
	}
	public int getLoginResult() {

		return this.loginResult;
	}
}
