package MyChatServer.MyChatServer;

import java.util.Arrays;

public class HttpList implements HttpProtocol {

	private String[] params;

	public HttpList(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response;
		if(!MyChatServer.checkTopicError(params)){
			int start = Integer.parseInt(params[0]);
			params = Arrays.copyOfRange(params, 1, params.length);
			response = "MESSAGES\r\n";
			for(int i = start; i < MyChatServer.MessageList.size();i++){
				Message msgP = MyChatServer.MessageList.get(i); 
				if(params.length == 0 || msgP.hasTopic(params)){
					response = response +i+" "+ msgP.getUserName() +" " + msgP.listToString() + "\r\n";
				}
			}
		}
		else {
			response = "KO\r\n";
		}
		return response;
	}

	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return null;
	}

}
