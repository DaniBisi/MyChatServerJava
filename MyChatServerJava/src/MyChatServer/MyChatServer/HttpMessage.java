package MyChatServer.MyChatServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HttpMessage implements HttpProtocol {

	protected String[] params;
	protected Boolean errorFound;
	protected int msgId;
	public HttpMessage(String[] params) {
		this.params = params;
		this.errorFound = false;
		msgId = -1;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		// controllo che ci siano tutti gli id dei topi ai quali bisogna
		// rispondere
		this.errorFound = MyChatServer.checkTopicError(params);
		String msg = clientHandler.acceptVisit(this);
		String response;
		if (!this.errorFound) {
			ArrayList<Integer> topicList = new ArrayList<Integer>();
			for (String s : this.params) {
				topicList.add(Integer.parseInt(s));
			}
			msgId = MyChatServer.addMessage(new Message(msg, topicList, clientHandler.getUserName()));
			response = "OK " + msgId + "\r\n";
		} else {
			response = "KO\r\n";
		}
		return response;
	}

	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		/*String msg = clientHandler.acceptVisit(this);
		String response;
		if (!this.errorFound) {
			ArrayList<Integer> topicList = new ArrayList<Integer>();
			for (String s : this.params) {
				topicList.add(Integer.parseInt(s));
			}
			int msgId = MyChatServer.addMessage(new Message(msg, topicList));
			response = "OK " + msgId + "\r\n";
		} else {
			response = "OK\r\n";
		}
		return response;
	}*/
	return "";
	}

}
