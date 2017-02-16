package it.bisignano.mychatserver;

public class HttpUnregister implements IHttpProtocol {

	private String[] params;

	public HttpUnregister(String[] params) {
		this.params = params;

	}

	@Override
	public String execute(ClientHandler clientHandler) {

		if(params != null){
			throw new IllegalArgumentException();
		}
		String response;
		if(MyChatServer.unRegister(clientHandler.getUserName())&& MyChatServer.unSubscribe(clientHandler.getUserName()) ){
			response = "OK\r\n";
		}
		else response = "KO\r\n";
		return response;
	}

}
