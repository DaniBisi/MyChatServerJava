package it.bisignano.mychatserver;

import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SubscribedHandler {
	private TreeSet<String> userSubscribed;
	private int idMessage;
	private Message message;
	private Map<String, Digest> digestReg;
	private static final Logger LOGGER = LogManager.getLogger(SubscribedHandler.class);

	public SubscribedHandler(Message message, int idMessage, Map<Integer, TreeSet<String>> subRegister,Map<String, Digest> digestReg) {
		this.idMessage = idMessage;
		this.message = message;
		this.digestReg =digestReg;
		this.userSubscribed = new TreeSet();
		if (subRegister != null) {
			for (int a : this.message.getTopicList()) {
				this.userSubscribed.addAll(subRegister.get(a));
			}
		}
	}

	public void sendMessageToSubscribed() {
		boolean timeToSend = false;
		String messages = "";
		for (String userName : userSubscribed) {
			Pair<String, Integer> entry = MyChatServer.register.get(userName);
			ChatClient sender = new ChatClient(entry.getLeft(), entry.getRight());
			if (this.digestReg.containsKey(userName)) {
				Digest userDigest = this.digestReg.get(userName);
				userDigest.addMessage(idMessage);
				if (userDigest.timeToSend()) {
					timeToSend = true;
					messages = getDigestMessages(userDigest);
				}
			} else {
				timeToSend = true;
				messages = "MESSAGE " + idMessage + "\r\n" + "TOPICS " + this.message.listToString() + "\r\n"
						+ message.getText() + "\r\n.\r\n\r\n";
			}
			if (timeToSend) {
				try {
					sender.connectServer();
					sender.sendMsg(messages);
					sender.closeSocket();
				} catch (Exception e) {
					LOGGER.error(e);
				}
			}
		}

	}

	private static String getDigestMessages(Digest userDigest) {
		StringBuilder sb = new StringBuilder();
		for (int idMessageP : userDigest.getList()) {
			Message msgP = MyChatServer.messageList.get(idMessageP);
			sb.append("MESSAGE ");
			sb.append(idMessageP);
			sb.append("\r\n");
			sb.append("TOPICS ");
			sb.append(msgP.listToString());
			sb.append("\r\n");
			sb.append(msgP.getText());
			sb.append("\r\n.\r\n\r\n");
		}
		userDigest.clearList();
		return sb.toString();
	}
}
