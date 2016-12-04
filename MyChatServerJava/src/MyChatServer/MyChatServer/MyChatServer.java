package MyChatServer.MyChatServer;
import MyChatServer.MyChatServer.clientHandler;

import java.awt.List;
import java.io.IOException;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Hello world!
 *
 */



public class MyChatServer extends Thread {
	private ServerSocket server;
	private String address;
	private int port;
	public static ArrayList<String> TopicList;
	public static ArrayList<Message> MessageList;
	public static Map<String, String> Dictionary;
	public static Map<String, Pair<String,Integer>> Register;
	public static Map<Integer, TreeSet<String>> subRegister;

	public MyChatServer(Map<String, String> Dictionary,String address, int port) {
		this.address = address;
		this.port = port;
		MyChatServer.Dictionary = Dictionary;
		MyChatServer.TopicList = new ArrayList<String>();
		MyChatServer.MessageList = new ArrayList<Message>(); 
		MyChatServer.Register = new HashMap<String, Pair<String,Integer>>(200);
		MyChatServer.subRegister = new HashMap<Integer, TreeSet<String>>(200);
		
		try {
			this.server = new ServerSocket(this.port);
			//this.server.setReuseAddress(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized int addTopic(String name){
		MyChatServer.TopicList.add(name);
		return MyChatServer.TopicList.size()-1;
	}

	public static boolean checkTopicError(String params[]){
		boolean errorFound = false;
		for (String string : params) {
			String topicName = "";
			try{
			topicName = MyChatServer.TopicList.get(Integer.parseInt(string)); //essendo memorizzati in ordine provo a vedere se esiste
			}catch (Exception e) {
				errorFound  = true;
				break;
			}
		}
		return errorFound;
	}
	public static boolean checkMessageError(String params[]){
		boolean errorFound = false;
		for (String string : params) {
			Message message = null;
			try{
				message = MyChatServer.MessageList.get(Integer.parseInt(string)); //essendo memorizzati in ordine provo a vedere se esiste
			}catch (Exception e) {
				errorFound  = true;
				break;
			}
		}
		return errorFound;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Socket client = server.accept();
				//System.out.println("Accepted from " + client.getInetAddress());
				new clientHandler(client).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

		
	}

	public static synchronized int addMessage(Message message) {
		// TODO Auto-generated method stub
		MyChatServer.MessageList.add(message);
		return MyChatServer.MessageList.size()-1;
	}
	public static synchronized boolean addRecord(String host, int port, String user){
		boolean found = false;
		try{
			Pair<String, Integer> a = new Pair<String, Integer>(host, port);
			for (Map.Entry<String, Pair<String,Integer>> entry : MyChatServer.Register.entrySet()) {
				if(entry.getValue().equals(a)){
					found = true;
					break;
				}
			}
			if(!found)
				MyChatServer.Register.put(user, a);
			
		}catch(Exception e){
			return false;
		}
		return !found;
	}

	public static synchronized boolean unRegister(String user){
		try{
			if(MyChatServer.Register.remove(user) == null)return false;
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public static boolean checkRegisterError(String userName) {
		// TODO Auto-generated method stub
		Object a = MyChatServer.Register.get(userName);
		if (a==null)return false;
		return true;
	}

	public static boolean addSubscription(String[] params, String userName) {
		// TODO Auto-generated method stub
		for (String topicSubscribed : params) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = MyChatServer.subRegister.get(idTopic);
			if(entry == null){
				entry = new TreeSet<String>();
				MyChatServer.subRegister.put(idTopic, entry);
			}
			entry.add(userName);	
			//MyChatServer.subRegister.put(,);
		}
		return true;
	}
	public static boolean rmSubScription(String[] params, String userName ){
		for (String topicSubscribed : params) {
			int idTopic = Integer.parseInt(topicSubscribed);
			TreeSet<String> entry = MyChatServer.subRegister.get(idTopic);
			entry.remove(userName);	
		}
		return true;
	}
	public static boolean checkTopicSubscription(String userName, int idTopic){
		try{
		TreeSet<String> entry = MyChatServer.subRegister.get(idTopic);
		if(entry.contains(userName)) return true;
		}catch (Exception e) {
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

	public static boolean unSubscribe(String userName) {
		for (int i=0;i< MyChatServer.TopicList.size();i++) {
			try{
			TreeSet<String> entry = MyChatServer.subRegister.get(i);
			entry.remove(userName);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		// TODO Auto-generated method stub
		return true;
	}
	
}
