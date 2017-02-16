package it.bisignano.mychatserver;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class HttpGet implements IHttpProtocol {
	private static final Logger LOGGER = LogManager.getLogger(HttpGet.class);
	private String[] params;
	public HttpGet(String[] params) {
		this.params = params;
	}
	@Override
	public String execute(ClientHandler clientHandler) {

		String response ="";
		if(this.params.length == 1){
			try {
				int msgId = Integer.parseInt(params[0]);
				Message msgP = MyChatServer.messageList.get(msgId);
				response = "MESSAGE "+msgId+"\r\n"+"USER "+ msgP.getUserName() +"\r\n" +"TOPICS "+ msgP.listToString() + "\r\n"+msgP.getText()+"\r\n.\r\n\r\n";
				
			}catch (Exception e) {
				LOGGER.info(e);
				response = "KO\r\n";
			}
		}
		
		return response;
	}


}
