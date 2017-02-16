package it.bisignano.mychatserver;

import java.util.ArrayList;

public class HttpReply extends HttpMessage  implements IHttpProtocol{

	public HttpReply(String[] params, boolean b) {
		//i parametri qua sono diversi devo prima modificarli poi creare il messaggio
		super(params,b);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException {
		this.errorFound = MyChatServer.checkMessageError(this.params);
		String msg = clientHandler.acceptVisit(this);
		String response;
		if (!this.errorFound && this.logged) {
			ArrayList<Integer> topicList = new ArrayList<Integer>();
			for (String s : this.params) {
				topicList.add(Integer.parseInt(s));
			}
			int fatherId = Integer.parseInt(params[0]);
			msgId = MyChatServer.addMessage(new Message(msg,  MyChatServer.messageList.get(fatherId).getTopicList(), clientHandler.getUserName(),fatherId));
			while(fatherId!=-1){
				MyChatServer.messageList.get(fatherId).addChild(msgId);
				fatherId = MyChatServer.messageList.get(fatherId).getFather();
			}
			response = "OK " + msgId + "\r\n";
		} else {
			response = "KO\r\n";
		}	
		 return response;
	}

}
