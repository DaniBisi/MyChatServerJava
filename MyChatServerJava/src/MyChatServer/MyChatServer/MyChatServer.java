package MyChatServer.MyChatServer;
import MyChatServer.MyChatServer.clientHandler;

import java.awt.List;
import java.io.IOException;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

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

	public MyChatServer(String address, int port,Map<String, String> Dictionary) {
		this.address = address;
		this.port = port;
		MyChatServer.Dictionary = Dictionary;
		MyChatServer.TopicList = new ArrayList<String>();
		MessageList = new ArrayList<Message>(); 
		
		try {
			this.server = new ServerSocket(this.port);
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
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {

				Socket client = server.accept();
				System.out.println("Accepted from " + client.getInetAddress());
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
}
