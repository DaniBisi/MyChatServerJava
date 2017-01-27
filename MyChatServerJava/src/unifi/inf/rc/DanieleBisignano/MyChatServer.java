package unifi.inf.rc.DanieleBisignano;

import java.awt.List;
import java.io.IOException;
import java.lang.Thread;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.SortingFocusTraversalPolicy;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import unifi.inf.rc.DanieleBisignano.clientHandler;

import java.util.Set;
import java.util.TreeSet;

/**
 * Hello world!
 *
 */

public class MyChatServer implements Runnable {
	private ServerSocket server;
	private String address;
	private int port; 
	private int backlog;
	public protected ArrayList<String> TopicList;
	public protected ArrayList<Message> MessageList;
	public protected Map<String, String> Dictionary;
	public protected Map<String, Pair<String, Integer>> Register;
	public protected Map<Integer, TreeSet<String>> subRegister;
	public protected Map<String, Digest> digestReg;

	public MyChatServer(Map<String, String> Dictionary, String address, int port) {
		this.address = address;
		this.port = port;
		this.Dictionary = Dictionary;
		this.TopicList = new ArrayList<String>();
		this.MessageList = new ArrayList<Message>();
		this.Register = new HashMap<String, Pair<String, Integer>>(200);
		this.subRegister = new HashMap<Integer, TreeSet<String>>(200);
		this.digestReg = new HashMap<String, Digest>(200);

		try {
			// this.server= new ServerSocket();
			// this.server.setReuseAddress(true);
			// this.server.bind(new InetSocketAddress(address, port));
			this.server = new ServerSocket(port, 1000, InetAddress.getByName(address));
			// this.server.setReuseAddress(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized int addTopic(String name) {
		this.TopicList.add(name);
		return this.TopicList.size() - 1;
	}

	public boolean checkTopicError(String params[]) {
		boolean errorFound = false;
		for (String string : params) {
			String topicName = "";
			try {
				topicName = this.TopicList.get(Integer.parseInt(string));
			} catch (Exception e) {
				errorFound = true;
				break;
			}
		}
		return errorFound;
	}

	public boolean checkMessageError(String params[]) {
		boolean errorFound = false;
		for (String string : params) {
			Message message = null;
			try {
				message = this.MessageList.get(Integer.parseInt(string));

			} catch (Exception e) {
				errorFound = true;
				break;
			}
		}
		return errorFound;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Socket client = server.accept();
				// System.out.println("Accepted from " +
				// client.getInetAddress());
				//System.out.print(client.getInetAddress() + " " + client.getLocalPort());
				new clientHandler(client).start();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}

	}

	public synchronized int addMessage(Message message) {
		// TODO Auto-generated method stub
		this.MessageList.add(message);
		int idMessage = this.MessageList.size() - 1;
		TreeSet<String> userSubscribed = new TreeSet<String>();
		try {
			for (int a : message.getTopicList()) {
				// lista di tutti gli utenti da allertare
				userSubscribed.addAll(this.subRegister.get(a));
				// Map<Integer, TreeSet<String>>
			}
			this.sendMessageToSubscribed(message, userSubscribed, idMessage);
		} catch (Exception e) {
			// non faccio niente. semplicemente non ci sono cose da fare
		}

		return idMessage;
	}

	public void sendMessageToSubscribed(Message message, TreeSet<String> userSubscribed, int idMessage) {
		boolean timeToSend = false;
		String messages = "";
		for (String userName : userSubscribed) {
			Pair<String, Integer> entry = this.Register.get(userName);
			System.out.println("provo a connettere a: " + entry.getLeft() + entry.getRight());
			ChatClient sender = new ChatClient(entry.getLeft(), entry.getRight());
			if (this.digestReg.containsKey(userName)) {
				Digest userDigest = this.digestReg.get(userName);
				userDigest.addMessage(idMessage);
				if (userDigest.timeToSend()) {
					
					timeToSend = true;
					for (int idMessageP : userDigest.getList()) {
						Message msgP = this.MessageList.get(idMessageP);
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

	public synchronized boolean addRecord(String host, int port, String user) {
		boolean found = false;
		try {
			Pair<String, Integer> a = new Pair<String, Integer>(host, port);
			for (Map.Entry<String, Pair<String, Integer>> entry : this.Register.entrySet()) {
				if (entry.getValue().equals(a)) {
					found = true;
					break;
				}
			}
			if (!found){
				this.Register.remove(user);
				this.Register.put(user, a);
			}
			else{ // controllo se � il suo.
				Pair<String, Integer> b = this.Register.get(user);
				if(b.equals(a)){// l'ho trovato ed era il suo. quindi aggiorno
					found = false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return !found;
	}

	public synchronized boolean unRegister(String user) {
		try {
			if (this.Register.remove(user) == null)
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean checkRegisterError(String userName) {
		// TODO Auto-generated method stub
		Object a = this.Register.get(userName);
		if (a == null)
			return false;
		return true;
	}

	public boolean addSubscription(String[] params, String userName) {
		// TODO Auto-generated method stub
		for (String topicSubscribed : params) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = this.subRegister.get(idTopic);
			if (entry == null) {
				entry = new TreeSet<String>();
				this.subRegister.put(idTopic, entry);
			}
			entry.add(userName);
			// this.subRegister.put(,);
		}
		return true;
	}

	public boolean rmSubScription(String[] params, String userName) {
		for (String topicSubscribed : params) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = this.subRegister.get(idTopic);
			entry.remove(userName);
		}
		return true;
	}

	public boolean checkTopicSubscription(String userName, int idTopic) {
		try {
			TreeSet<String> entry = this.subRegister.get(idTopic);
			if (entry.contains(userName))
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public void closeSocket() {
		// TODO Auto-generated method stub
		try {
			this.server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean unSubscribe(String userName) {
		for (int i = 0; i < this.TopicList.size(); i++) {
			try {
				TreeSet<String> entry = this.subRegister.get(i);
				entry.remove(userName);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// TODO Auto-generated method stub
		return true;
	}

	public void setDigest(String userName, int k) {
		// TODO Auto-generated method stub
		// digestReg.replace(key, value)
		if (!this.digestReg.containsKey(userName)) {
			this.digestReg.put(userName, new Digest(k));
		} else {
			Digest entry = this.digestReg.get(userName);
			entry.setK(k); // si pu� fare perch� � un campo statico
		}
		// for (Map.Entry<String, String> entry : map.entrySet())
	}

}
