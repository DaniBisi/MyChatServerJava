package unifi.inf.rc.DanieleBisignano;

import java.util.Arrays;

public class HttpList implements HttpProtocol {

	private String[] params;
	private int idMid; 
	public HttpList(String[] params) {
		idMid = Integer.parseInt(params[0]);
		
		if(params.length > 1)
			this.params = Arrays.copyOfRange(params, 1, params.length);
		else this.params = null;
	}

	@Override
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException {
		String response;
		if((params== null || !MyChatServer.checkTopicError(params)) && idMid < MyChatServer.messageList.size()){
			response = "MESSAGES\r\n";
			for(int i = idMid; i < MyChatServer.messageList.size();i++){
				Message msgP = MyChatServer.messageList.get(i); 
				if(params == null || msgP.hasTopic(params)){
					response = response +i+" "+ msgP.getUserName() +" " + msgP.listToString() + "\r\n";
				}
			}
			response = response +"\r\n";
		}
		else {
			response = "KO\r\n";
		}
		return response;
	}

}
