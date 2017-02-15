package unifi.inf.rc.DanieleBisignano;

import java.io.IOException;
import java.net.InetAddress;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import unifi.inf.rc.DanieleBisignano.ClientHandler;

import java.util.TreeSet;

/**
 * Hello world!
 *
 */

public class MyChatServer extends Thread {
	private ServerSocket server;
	private String address;
	private int port;
	protected  static ArrayList<String> topicList;
	protected  static ArrayList<Message> messageList;
	private static Map<String, String> dictionary;
	protected  static Map<String, Pair<String, Integer>> register;
	protected  static Map<Integer, TreeSet<String>> subRegister;
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
			e.printStackTrace();
		}
	}

	public static synchronized int addTopic(String name) {
		MyChatServer.topicList.add(name);
		return MyChatServer.topicList.size() - 1;
	}

	public static String getUserPass(String username) {
		return dictionary.get(username);
	}

	public static boolean checkTopicError(String params[]) {
		boolean errorFound = false;
		for (String string : params) {
			String topicName = "";
			try {
				topicName = MyChatServer.topicList.get(Integer.parseInt(string));
				System.out.println("nome:" + topicName);
			} catch (Exception e) {
				errorFound = true;
				break;
			}
		}
		return errorFound;
	}

	public static boolean checkMessageError(String params[]) {
		boolean errorFound = false;
		for (String string : params) {
			try {
				MyChatServer.messageList.get(Integer.parseInt(string));

			} catch (Exception e) {
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
				e.printStackTrace();
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
				// lista di tutti gli utenti da allertare
				userSubscribed.addAll(MyChatServer.subRegister.get(a));
				// Map<Integer, TreeSet<String>>
			}
			MyChatServer.sendMessageToSubscribed(message, userSubscribed, idMessage);
		} catch (Exception e) {
			// non faccio niente. semplicemente non ci sono cose da fare
		}

		return idMessage;
	}

	public static void sendMessageToSubscribed(Message message, TreeSet<String> userSubscribed, int idMessage) {
		boolean timeToSend = false;
		String messages = "";
		for (String userName : userSubscribed) {
			Pair<String, Integer> entry = MyChatServer.register.get(userName);
			System.out.println("provo a connettere a: " + entry.getLeft() + entry.getRight());
			ChatClient sender = new ChatClient(entry.getLeft(), entry.getRight());
			if (MyChatServer.digestReg.containsKey(userName)) {
				Digest userDigest = MyChatServer.digestReg.get(userName);
				userDigest.addMessage(idMessage);
				if (userDigest.timeToSend()) {

					timeToSend = true;
					for (int idMessageP : userDigest.getList()) {
						Message msgP = MyChatServer.messageList.get(idMessageP);
						messages = messages + "MESSAGE " + idMessageP + "\r\n" + "TOPICS " + msgP.listToString()
								+ "\r\n" + msgP.getText() + "\r\n.\r\n\r\n";
					}
					userDigest.clearList();
				}
			} else {
				timeToSend = true;
				messages = "MESSAGE " + idMessage + "\r\n" + "TOPICS " + message.listToString() + "\r\n"
						+ message.getText() + "\r\n.\r\n\r\n";
			}
			if (timeToSend) {
				try {
					sender.connectServer();
					System.out.println(messages);
					sender.sendMsg(messages);
					sender.closeSocket();
				} catch (Exception e) {
					System.out.println("unlucky");
				}
			}
		}

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
			return false;
		}
		return !found;
	}

	public static synchronized boolean unRegister(String user) {
		try {
			if (MyChatServer.register.remove(user) == null)
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
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
			return false;
		}
		return false;
	}

	public void closeSocket() {
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean unSubscribe(String userName) {
		for (int i = 0; i < MyChatServer.topicList.size(); i++) {
			try {
				TreeSet<String> entry = MyChatServer.subRegister.get(i);
				entry.remove(userName);
			} catch (Exception e) {
			}
		}
		return true;
	}

	public static void setDigest(String userName, int k) {
		// digestReg.replace(key, value)
		if (!MyChatServer.digestReg.containsKey(userName)) {
			MyChatServer.digestReg.put(userName, new Digest(k));
		} else {
			Digest entry = MyChatServer.digestReg.get(userName);
			entry.setK(k); // si pu� fare perch� � un campo statico
		}
	}

}
