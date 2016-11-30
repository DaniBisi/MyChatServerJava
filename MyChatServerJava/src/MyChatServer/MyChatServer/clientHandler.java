package MyChatServer.MyChatServer;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.IllegalFormatException;

public class clientHandler extends Thread implements visitable{

	private Socket client;
	private InputStream in;
	private OutputStream out;
	private int loginStatus;

	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}

	public clientHandler(Socket client) {
		this.client = client;
		try {
			this.in = this.client.getInputStream();
			this.out = this.client.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("im waiting for data...");
		String msg = "";
		// byte streamIn[] = new byte[2048];
		try {
			int prov2;
			while ((prov2 = this.in.read())!=-1) {
				
				char ch = (char) prov2;
				msg = msg + String.valueOf(ch); // uscirà quando ci sarà un
												// errore di stream
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
					try {
						Response = Response + (factoryHttpCommand.getHtmlProtocol(command,this.loginStatus)).execute();
					} catch (IllegalArgumentException e) {
						// TODO: handle exception
						Response = Response + "KO\r\n";
					}

					this.out.write(Response.getBytes("latin1"));

					msg = "";
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void acceptVisit(factoryHttpCommand visitor) {
		// TODO Auto-generated method stub
		
	}

}
