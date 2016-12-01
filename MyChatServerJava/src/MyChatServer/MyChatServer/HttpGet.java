package MyChatServer.MyChatServer;

import java.util.ArrayList;

public class HttpGet implements HttpProtocol {

	private String params[];
	public HttpGet(String params[]) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}
	
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response ="";
		if(this.params.length == 1){
			try {
				int msgId = Integer.parseInt(params[0]);
				Message msgP = MyChatServer.MessageList.get(msgId);
				response = "MESSAGE "+msgId+"\r\n"+"USER "+ msgP.getUserName() +"\r\n" +"TOPICS "+ msgP.listToString() + "\r\n"+msgP.getText()+"\r\n.\r\n\r\n";
				
			}catch (Exception e) {
				response = "KO\r\n";
			}
		}
		
		return response;
	}

	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return null;
	}

}
