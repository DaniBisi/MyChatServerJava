package it.bisignano.mychatserver;

public class HttpNew implements IHttpProtocol {

	private String[] params;

	public HttpNew(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}

	//@Override
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response = "KO\r\n";
		if(this.params.length == 1){
			int topicId = MyChatServer.addTopic(params[0]);
			response = "OK "+topicId +"\r\n";
		}
		
		return response;
		
	}
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return "";
	}
*/
}
