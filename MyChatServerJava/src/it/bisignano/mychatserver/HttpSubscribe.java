package it.bisignano.mychatserver;

public class HttpSubscribe implements IHttpProtocol {

	private int loginResult;
	private String[] params;
	private boolean alreadySubscribed;
	public HttpSubscribe(String[] params, boolean b) {
		this.params = params;
		alreadySubscribed = b;

	}

	@Override
	public String execute(ClientHandler clientHandler) {
		String response;

		if(this.alreadySubscribed || MyChatServer.checkRegisterError(clientHandler.getUserName())){
			MyChatServer.addSubscription(params,clientHandler.getUserName());
			this.loginResult = 4;
			clientHandler.acceptVisit(this);
			response = "OK\r\n";
		}
		else response = "KO\r\n";
		
		return response;
	}

	public int getLoginResult() {

		return this.loginResult;
	}

}
