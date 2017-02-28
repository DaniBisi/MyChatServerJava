package it.bisignano.mychatserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MyChatData {

	private static final Logger LOGGER = LogManager.getLogger(MyChatServer.class);
	protected ArrayList<String> topicList;
	protected ArrayList<Message> messageList;
	private Map<String, String> dictionary;
	protected Map<String, Pair<String, Integer>> register;
	protected Map<Integer, TreeSet<String>> subRegister;
	protected Map<String, Digest> digestReg;
	private SubscribedHandler sHandler;

	public MyChatData() { 
		this.sHandler = new SubscribedHandler();
		this.dictionary = dictionary;
		this.topicList = new ArrayList();
		this.messageList = new ArrayList();
		this.register = new HashMap<String, Pair<String, Integer>>(200);
		this.subRegister = new HashMap(200);
		this.digestReg = new HashMap(200);

	}

	public List<String> getTopicList() {
		return topicList;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public Map<String, String> getDictionary() {
		return dictionary;
	}

	public synchronized int addTopic(String name) {
		this.topicList.add(name);
		return this.topicList.size() - 1;
	}

	protected String getUserPass(String username) {
		return dictionary.get(username);
	}

	public boolean checkTopicError(String[] idTopics) {
		boolean errorFound = false;
		if (idTopics == null) {
			errorFound = true;
		} else {

			for (String string : idTopics) {
				try {
					this.topicList.get(Integer.parseInt(string));
				} catch (Exception e) {
					errorFound = true;
					LOGGER.error(e);
					break;
				}
			}
		}
		return errorFound;
	}

	public boolean checkMessageError(String[] idMessage) {
		boolean errorFound = false;
		if (idMessage == null) {
			errorFound = true;
		} else {
			for (String string : idMessage) {
				try {
					this.messageList.get(Integer.parseInt(string));

				} catch (Exception e) {
					LOGGER.error(e);
					errorFound = true;
					break;
				}
			}
		}
		return errorFound;
	}

	public int addMessage(Message message) {
		this.messageList.add(message);
		int idMessage = this.messageList.size() - 1;
		sHandler.sendMessageToSubscribed();
		return idMessage;
	}

	public synchronized boolean addRecord(String host, int port, String user) {
		boolean found = false;
		Pair<String, Integer> a = new Pair(host, port);
		for (Map.Entry<String, Pair<String, Integer>> entry : this.register.entrySet()) {
			if (entry.getValue().equals(a)) {
				found = true;
				break;
			}
		}
		if (!found)
			this.register.put(user, a);

		return !found;
	}

	public synchronized boolean unRegister(String user) {
		if (this.register.remove(user) == null) {
			return false;
		}

		return true;
	}

	public boolean checkRegisterError(String userName) {
		Object a = this.register.get(userName);
		if (a == null) {
			return false;
		}
		return true;
	}

	public boolean addSubscription(String[] topicList, String userName) {
		for (String topicSubscribed : topicList) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = this.subRegister.get(idTopic);
			if (entry == null) {
				entry = new TreeSet<String>();
				this.subRegister.put(idTopic, entry);
			}
			entry.add(userName);
		}
		return true;
	}

	public boolean rmSubScription(String[] topicList, String userName) {
		for (String topicSubscribed : topicList) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = this.subRegister.get(idTopic);
			entry.remove(userName);
		}
		return true;
	}

	public boolean checkTopicSubscription(int idTopic, String userName) {
		try {
			TreeSet<String> entry = this.subRegister.get(idTopic);
			if (entry.contains(userName))
				return true;
		} catch (Exception e) {
			LOGGER.error(e);
			return false;
		}
		return false;
	}

	public boolean unSubscribe(String userName) {
		for (int i = 0; i < this.topicList.size(); i++) {
			TreeSet<String> entry = this.subRegister.get(i);
			entry.remove(userName);

		}
		return true;
	}

	public void setDigest(String userName, int k) {
		if (!this.digestReg.containsKey(userName)) {
			this.digestReg.put(userName, new Digest(k));
		} else {
			Digest entry = this.digestReg.get(userName);
			entry.setK(k); // si pu� fare perch� � un campo o
		}
	}

}
