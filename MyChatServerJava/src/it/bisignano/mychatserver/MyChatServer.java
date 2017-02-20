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

/**
 * Hello world!
 *
 */

public class MyChatServer extends Thread {
	private static final Logger LOGGER = LogManager.getLogger(MyChatServer.class);
	private ServerSocket server;
	private String address;
	private int port;
	protected static ArrayList<String> topicList;
	protected static ArrayList<Message> messageList;
	private static Map<String, String> dictionary;
	protected static Map<String, Pair<String, Integer>> register;
	protected static Map<Integer, TreeSet<String>> subRegister;
	protected static Map<String, Digest> digestReg;

	public MyChatServer(Map<String, String> dictionary, String address, int port) {
		this.address = address;
		this.port = port;
		MyChatServer.dictionary = dictionary;
		MyChatServer.topicList = new ArrayList();
		MyChatServer.messageList = new ArrayList();
		MyChatServer.register = new HashMap(200);
		MyChatServer.subRegister = new HashMap(200);
		MyChatServer.digestReg = new HashMap(200);

		try {
			this.server = new ServerSocket(this.port, 1000, InetAddress.getByName(this.address));
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public ServerSocket getServer() {
		return server;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public static List<String> getTopicList() {
		return topicList;
	}

	public static List<Message> getMessageList() {
		return messageList;
	}

	public static Map<String, String> getDictionary() {
		return dictionary;
	}
/*
	public static Map<String, Pair<String, Integer>> getRegister() {
		return register;
	}

	public static Map<Integer, TreeSet<String>> getSubRegister() {
		return subRegister;
	}

	public static Map<String, Digest> getDigestReg() {
		return digestReg;
	}
*/
	public static synchronized int addTopic(String name) {
		MyChatServer.topicList.add(name);
		return MyChatServer.topicList.size() - 1;
	}

	public static String getUserPass(String username) {
		return dictionary.get(username);
	}

	public static boolean checkTopicError(String[] idTopics) {
		boolean errorFound = false;
		if (idTopics == null) {
			errorFound = true;
		} else {

			for (String string : idTopics) {
				try {
					MyChatServer.topicList.get(Integer.parseInt(string));
				} catch (Exception e) {
					errorFound = true;
					LOGGER.error(e);
					break;
				}
			}
		}
		return errorFound;
	}

	public static boolean checkMessageError(String[] idMessage) {
		boolean errorFound = false;
		if (idMessage == null) {
			errorFound = true;
		} else {
			for (String string : idMessage) {
				try {
					MyChatServer.messageList.get(Integer.parseInt(string));

				} catch (Exception e) {
					LOGGER.error(e);
					errorFound = true;
					break;
				}
			}
		}
		return errorFound;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket client = server.accept();
				new ClientHandler(client).start();

			} catch (IOException e) {
				LOGGER.error(e);
				break;
			} finally {
				this.closeSocket();
			}
		}

	}

	public static int addMessage(Message message) {
		SubscribedHandler sHandler;
		MyChatServer.messageList.add(message);
		int idMessage = MyChatServer.messageList.size() - 1;
			sHandler = new SubscribedHandler(message, idMessage, MyChatServer.subRegister, MyChatServer.digestReg);
			sHandler.sendMessageToSubscribed();
		
		return idMessage;
	}

	public static synchronized boolean addRecord(String host, int port, String user) {
		boolean found = false;
		Pair<String, Integer> a = new Pair(host, port);
		for (Map.Entry<String, Pair<String, Integer>> entry : MyChatServer.register.entrySet()) {
			if (entry.getValue().equals(a)) {
				found = true;
				break;
			}
		}
		if (!found)
			MyChatServer.register.put(user, a);

		return !found;
	}

	public static synchronized boolean unRegister(String user) {
		if (MyChatServer.register.remove(user) == null) {
			return false;
		}

		return true;
	}

	public static boolean checkRegisterError(String userName) {
		Object a = MyChatServer.register.get(userName);
		if (a == null){
			return false;
		}
		return true;
	}

	public static boolean addSubscription(String[] params, String userName) {
		for (String topicSubscribed : params) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = MyChatServer.subRegister.get(idTopic);
			if (entry == null) {
				entry = new TreeSet();
				MyChatServer.subRegister.put(idTopic, entry);
			}
			entry.add(userName);
		}
		return true;
	}

	public static boolean rmSubScription(String[] params, String userName) {
		for (String topicSubscribed : params) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = MyChatServer.subRegister.get(idTopic);
			entry.remove(userName);
		}
		return true;
	}

	public static boolean checkTopicSubscription(String userName, int idTopic) {
		try {
			TreeSet<String> entry = MyChatServer.subRegister.get(idTopic);
			if (entry.contains(userName))
				return true;
		} catch (Exception e) {
			LOGGER.error(e);
			return false;
		}
		return false;
	}

	public void closeSocket() {
		try {
			this.server.close();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public static boolean unSubscribe(String userName) {
		for (int i = 0; i < MyChatServer.topicList.size(); i++) {
			try {
				TreeSet<String> entry = MyChatServer.subRegister.get(i);
				entry.remove(userName);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
		return true;
	}

	public static void setDigest(String userName, int k) {
		if (!MyChatServer.digestReg.containsKey(userName)) {
			MyChatServer.digestReg.put(userName, new Digest(k));
		} else {
			Digest entry = MyChatServer.digestReg.get(userName);
			entry.setK(k); // si pu� fare perch� � un campo statico
		}
	}

}
