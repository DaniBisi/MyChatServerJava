package it.bisignano.mychatserver;

public class HttpUnSubscribe implements HttpProtocol {

	private String[] params;
	private boolean alreadySubscribed;

	public HttpUnSubscribe(String[] params, boolean b) {
		this.params = params;
		this.alreadySubscribed = b;
	}

	@Override
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException {
		
		String response;
		if (this.params.length>0 && (this.alreadySubscribed || MyChatServer.checkRegisterError(clientHandler.getUserName()))) {
			MyChatServer.rmSubScription(params, clientHandler.getUserName());
			response = "OK\r\n";
		} else
			response = "KO\r\n";

		return response;
	}

}
