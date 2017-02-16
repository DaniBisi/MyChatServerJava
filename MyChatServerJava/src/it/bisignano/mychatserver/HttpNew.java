package it.bisignano.mychatserver;

public class HttpNew implements IHttpProtocol {

	private String[] params;

	public HttpNew(String[] params) {
		this.params = params;

	}

	@Override
	public String execute(ClientHandler clientHandler) {
		String response = "KO\r\n";
		if(this.params.length == 1){
			int topicId = MyChatServer.addTopic(params[0]);
			response = "OK "+topicId +"\r\n";
		}
		return response;
		
	}
}
