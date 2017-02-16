package it.bisignano.mychatserver;

import java.util.Arrays;

public class HttpList implements IHttpProtocol {

	private String[] params;
	private int idMid; 
	public HttpList(String[] params) {
		idMid = Integer.parseInt(params[0]);
		
		if(params.length > 1)
			this.params = Arrays.copyOfRange(params, 1, params.length);
		else this.params = null;
	}

	@Override
	public String execute(ClientHandler clientHandler) {
		StringBuilder sb = new StringBuilder();
		if((params== null || !MyChatServer.checkTopicError(params)) && idMid < MyChatServer.messageList.size()){
			sb.append("MESSAGES\r\n");
			for(int i = idMid; i < MyChatServer.messageList.size();i++){
				Message msgP = MyChatServer.messageList.get(i); 
				if(params == null || msgP.hasTopic(params)){
					sb.append(i);
					sb.append(" ");
					sb.append(msgP.getUserName());
					sb.append(" ");
					sb.append(msgP.listToString());
					sb.append("\r\n");
				}
			}
			sb.append("\r\n");
		}
		else {
			sb.append("KO\r\n");
		}
		return sb.toString();
	}

}
