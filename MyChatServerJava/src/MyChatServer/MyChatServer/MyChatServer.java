package MyChatServer.MyChatServer;
import MyChatServer.MyChatServer.clientHandler;
import java.io.IOException;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Hello world!
 *
 */



public class MyChatServer extends Thread {
	private ServerSocket server;
	private String address;
	private int port;
	public static Map<String, String> Dictionary;

	public MyChatServer(String address, int port,Map<String, String> Dictionary) {
		this.address = address;
		this.port = port;
		MyChatServer.Dictionary = Dictionary;
		
		try {
			this.server = new ServerSocket(this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
