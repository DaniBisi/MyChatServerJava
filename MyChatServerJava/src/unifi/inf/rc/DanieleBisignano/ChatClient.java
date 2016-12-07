package unifi.inf.rc.DanieleBisignano;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class ChatClient {

	private String string;
	private int port;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	public ChatClient(String string, int port) {
		this.string = string;
		this.port = port;
		// TODO Auto-generated constructor stub
	}

	public boolean connectServer() {
		this.socket = new Socket(); // accetta anche new Socket(this.string,this.port) e non c'è bisogno di connect
		boolean connectionStatus = true;
		try {
			this.socket.connect(new InetSocketAddress(this.string,this.port));
			 this.in = socket.getInputStream ();
			 this.out = socket.getOutputStream ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("unlucky");
			connectionStatus = false;
		}
		
		return connectionStatus;
	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	public void sendMsg(String msg) {
		// TODO Auto-generated method stub
		try {
			this.out.write(msg.getBytes("latin1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String receiveMsg(){
		// TODO Auto-generated method stub

		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String msg = "";
		byte streamIn[] = new byte[2048];
		try {
			
				int lenghtProv = this.in.read(streamIn); //uscirà quando ci sarà un errore di stream
				//streamIn[lenghtProv] = "\0";
				msg = msg + (new String(streamIn, StandardCharsets.UTF_8)).substring(0,lenghtProv);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(msg);
		return msg;
		/*String msg="";
		try {
			int prov2;
			while ((prov2 = this.in.read()) != -1) {

				char ch = (char) prov2;
				msg = msg + String.valueOf(ch); 
				String prov = "";
				if (msg.length() > 2)
					prov = msg.substring(msg.length() - 2, msg.length());
				if (prov.compareTo("\r\n") == 0) {
					System.out.println("sto eseguendo un comando");
					System.out.println(msg);
					// ESEGUO LA SPLIT PER TROVARE I COMANDI.
					String Response = "";
					String command = msg.split("\r\n")[0];
					// for (String command : msg.split("\r\n")) {
					

					return msg;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "";*/
	}

	public String receiveMsg( int a){
		// TODO Auto-generated method stub

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String msg = "";
		byte streamIn[] = new byte[2048];
		try {
			
				int lenghtProv = this.in.read(streamIn); //uscirà quando ci sarà un errore di stream
				//streamIn[lenghtProv] = "\0";
				msg = msg + (new String(streamIn, StandardCharsets.UTF_8)).substring(0,lenghtProv);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(msg);
		return msg;
		/*String msg="";
		try {
			int prov2;
			while ((prov2 = this.in.read()) != -1) {

				char ch = (char) prov2;
				msg = msg + String.valueOf(ch); 
				String prov = "";
				if (msg.length() > 2)
					prov = msg.substring(msg.length() - 2, msg.length());
				if (prov.compareTo("\r\n") == 0) {
					System.out.println("sto eseguendo un comando");
					System.out.println(msg);
					// ESEGUO LA SPLIT PER TROVARE I COMANDI.
					String Response = "";
					String command = msg.split("\r\n")[0];
					// for (String command : msg.split("\r\n")) {
					

					return msg;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "";*/
	}
	public void closeSocket() {
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
