package it.bisignano.mychatserver;

import java.io.IOException;
import java.net.InetAddress;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import it.bisignano.mychatserver.ClientHandler;

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
	private static Map<String, Digest> digestReg;

	public MyChatServer(Map<String, String> dictionary, String address, int port) {
		this.address = address;
		this.port = port;
		MyChatServer.dictionary = dictionary;
		MyChatServer.topicList = new ArrayList<>();
		MyChatServer.messageList = new ArrayList<>();
		MyChatServer.register = new HashMap<>(200);
		MyChatServer.subRegister = new HashMap<>(200);
		MyChatServer.digestReg = new HashMap<>(200);

		try {
			this.server = new ServerSocket(this.port, 1000, InetAddress.getByName(this.address));
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public static synchronized int addTopic(String name) {
		MyChatServer.topicList.add(name);
		return MyChatServer.topicList.size() - 1;
	}

	public static String getUserPass(String username) {
		return dictionary.get(username);
	}

	public static boolean checkTopicError(String[] params) {
		boolean errorFound = false;
		for (String string : params) {
			try {
				MyChatServer.topicList.get(Integer.parseInt(string));
			} catch (Exception e) {
				errorFound = true;
				LOGGER.error(e);
				break;
			}
		}
		return errorFound;
	}

	public static boolean checkMessageError(String[] params) {
		boolean errorFound = false;
		for (String string : params) {
			try {
				MyChatServer.messageList.get(Integer.parseInt(string));

			} catch (Exception e) {
				LOGGER.error(e);
				errorFound = true;
				break;
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
			}
		}

	}

	public static synchronized int addMessage(Message message) {
		MyChatServer.messageList.add(message);
		int idMessage = MyChatServer.messageList.size() - 1;
		TreeSet<String> userSubscribed = new TreeSet<>();
		try {
			for (int a : message.getTopicList()) {
				userSubscribed.addAll(MyChatServer.subRegister.get(a));
			}
			MyChatServer.sendMessageToSubscribed(message, userSubscribed, idMessage);
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return idMessage;
	}

	public static void sendMessageToSubscribed(Message message, SortedSet<String> userSubscribed, int idMessage) {
		boolean timeToSend = false;
		String messages = "";		
		for (String userName : userSubscribed) {
			Pair<String, Integer> entry = MyChatServer.register.get(userName);
			ChatClient sender = new ChatClient(entry.getLeft(), entry.getRight());
			if (MyChatServer.digestReg.containsKey(userName)) {
				Digest userDigest = MyChatServer.digestReg.get(userName);
				userDigest.addMessage(idMessage);
				if (userDigest.timeToSend()) {
					timeToSend = true;
					messages = getDigestMessages(userDigest);
				}
			} else {
				timeToSend = true;
				messages = "MESSAGE " + idMessage + "\r\n" + "TOPICS " + message.listToString() + "\r\n"
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
			sb.append("TOPICS " );
			sb.append(msgP.listToString());
			sb.append("\r\n"); 
			sb.append(msgP.getText());
			sb.append("\r\n.\r\n\r\n");
		}
		userDigest.clearList();
		return sb.toString();
	}

	public static synchronized boolean addRecord(String host, int port, String user) {
		boolean found = false;
		try {
			Pair<String, Integer> a = new Pair<>(host, port);
			for (Map.Entry<String, Pair<String, Integer>> entry : MyChatServer.register.entrySet()) {
				if (entry.getValue().equals(a)) {
					found = true;
					break;
				}
			}
			if (!found)
				MyChatServer.register.put(user, a);

		} catch (Exception e) {
			LOGGER.error(e);
			return false;
		}
		return !found;
	}

	public static synchronized boolean unRegister(String user) {
		if(MyChatServer.register.remove(user) == null) {
			return false;
		}

		return true;
	}

	public static boolean checkRegisterError(String userName) {
		Object a = MyChatServer.register.get(userName);
		if (a == null)
			return false;
		return true;
	}

	public static boolean addSubscription(String[] params, String userName) {
		for (String topicSubscribed : params) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = MyChatServer.subRegister.get(idTopic);
			if (entry == null) {
				entry = new TreeSet<>();
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
