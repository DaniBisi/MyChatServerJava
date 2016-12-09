package unifi.inf.rc.DanieleBisignano;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HttpMessage implements HttpProtocol {

	protected String[] params;
	protected Boolean errorFound;
	protected int msgId;
	protected boolean logged;

	public HttpMessage(String[] params, boolean b) {
		this.params = params;
		this.errorFound = false;
		this.logged = b;
		msgId = -1;
		// TODO Auto-generated constructor stub
	}

	// @Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		// controllo che ci siano tutti gli id dei topi ai quali bisogna
		// rispondere
		
		String msg = clientHandler.acceptVisit(this);
		String msg1 = msg.replaceAll("\r\n", " ");
		if (params == null || params[0].equals("")||msg1.equals("")) {
			this.errorFound = true;
		} else {
			this.errorFound = MyChatServer.checkTopicError(params);
		}
		System.out.println("messaggio:" + msg1);

		String response;
		if (!this.errorFound && this.logged) {
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
	/*
	 * @Override public String visit(clientHandler clientHandler) { // TODO
	 * Auto-generated method stub String msg = clientHandler.acceptVisit(this);
	 * String response; if (!this.errorFound) { ArrayList<Integer> topicList =
	 * new ArrayList<Integer>(); for (String s : this.params) {
	 * topicList.add(Integer.parseInt(s)); } int msgId =
	 * MyChatServer.addMessage(new Message(msg, topicList)); response = "OK " +
	 * msgId + "\r\n"; } else { response = "OK\r\n"; } return response; } return
	 * ""; }
	 */

}
