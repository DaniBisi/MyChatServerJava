package it.bisignano.mychatserver;

import java.util.ArrayList;

public class HttpMessage implements IHttpProtocol {

	protected String[] params;
	protected Boolean errorFound;
	protected int msgId;
	protected boolean logged;
	public HttpMessage(String[] params, boolean b) {
		this.params = params;
		this.errorFound = false;
		this.logged = b;
		msgId = -1;
	}

	@Override
	public String execute(ClientHandler clientHandler){

		// controllo che ci siano tutti gli id dei topi ai quali bisogna
		// rispondere
		this.errorFound = MyChatServer.checkTopicError(params);
		String msg = clientHandler.acceptVisit(this);
		StringBuilder response = new StringBuilder();
		if (!this.errorFound && this.logged) {
			ArrayList<Integer> topicList = new ArrayList<>();
			for (String s : this.params) {
				topicList.add(Integer.parseInt(s));
			}
			msgId = MyChatServer.addMessage(new Message(msg, topicList, clientHandler.getUserName()));
			response.append("OK ");
			response.append(msgId); 
			response.append("\r\n");
		} else {
			response.append("KO\r\n");
		}
		return response.toString();
	}


}
