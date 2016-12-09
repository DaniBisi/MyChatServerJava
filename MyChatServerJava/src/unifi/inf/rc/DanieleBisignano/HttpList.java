package unifi.inf.rc.DanieleBisignano;

import java.util.Arrays;

public class HttpList implements HttpProtocol {

	private String[] params;
	private int idMid; 
	public HttpList(String[] params) {
		idMid = Integer.parseInt(params[0]);
		
		this.params = Arrays.copyOfRange(params, 1, params.length);
		if(this.params.length == 0)
			this.params =null;
		
	}

	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		String response;
		
		//System.out.println(params);
		if(/*params!= null && params.length>0 && */params== null || !MyChatServer.checkTopicError(params)){
			//int start = Integer.parseInt(params[0]);
			//params = Arrays.copyOfRange(params, 1, params.length);
			response = "MESSAGES\r\n";
			for(int i = idMid; i < MyChatServer.MessageList.size();i++){
				Message msgP = MyChatServer.MessageList.get(i); 
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
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
