package MyChatServer.MyChatServer;

import java.util.ArrayList;

public class HttpReply extends HttpMessage  implements HttpProtocol{

	public HttpReply(String[] params) {
		//i parametri qua sono diversi devo prima modificarli poi creare il messaggio
		super(params);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		this.errorFound = MyChatServer.checkMessageError(this.params);
		String msg = clientHandler.acceptVisit(this);
		String response;
		if (!this.errorFound) {
			ArrayList<Integer> topicList = new ArrayList<Integer>();
			for (String s : this.params) {
				topicList.add(Integer.parseInt(s));
			}
			int fatherId = Integer.parseInt(params[0]);
			msgId = MyChatServer.addMessage(new Message(msg,  MyChatServer.MessageList.get(fatherId).getTopicList(), clientHandler.getUserName(),fatherId));
			while(fatherId!=-1){
				MyChatServer.MessageList.get(fatherId).addChild(msgId);
				fatherId = MyChatServer.MessageList.get(fatherId).getFather();
			}
			response = "OK " + msgId + "\r\n";
		} else {
			response = "KO\r\n";
		}	
		 return response;
	}

}