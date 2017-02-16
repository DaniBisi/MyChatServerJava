package it.bisignano.mychatserver;

public class HttpPass implements IHttpProtocol {

	private String[] params;
	private int loginResult;

	public HttpPass(String[] params) {
		this.params = params;

	}


	@Override
	public String execute(ClientHandler clientHandler) {
		String response = "KO\r\n";
		if(this.params.length == 1){
			    String pass = MyChatServer.getUserPass(clientHandler.getUserName());
				if(pass!= null && pass.equals(params[0])){
					loginResult = 2;
					clientHandler.acceptVisit(this);
					response = "OK\r\n";
				}
				else{
					loginResult = 0;
					clientHandler.acceptVisit(this);
					response = "KO\r\n";
				}
		}
		
		return response;
	}
	


	public int getLoginResult() {

		return this.loginResult;
	}

}
