package MyChatServer.MyChatServer;

import java.awt.print.Printable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.IllegalFormatException;

public class clientHandler extends Thread implements visitable {

	private Socket client;
	private InputStream in;
	private OutputStream out;
	private int loginStatus;
	private String userName;

	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
		// System.out.println("Loginstatus:" + this.loginStatus);
	}

	public int getLoginStatus(int loginStatus) {
		return this.loginStatus;
	}

	public clientHandler(Socket client) {
		this.client = client;
		this.loginStatus = 0;
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
			while ((prov2 = this.in.read()) != -1) {

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
						HttpProtocol commandR;
						Response = Response + (commandR = factoryHttpCommand.getHtmlProtocol(command, this.loginStatus)).execute(this);
						//Response = Response + commandR.visit(this);
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

	public String acceptVisit(HttpProtocol cmd) {
		// TODO Auto-generated method stub
		cmd.visit(this);
		return "";
	}

	public String acceptVisit(HttpMessage msg) {
		// TODO Auto-generated method stub
		String msgProv = "";
		try {
			int prov2;
			while ((prov2 = this.in.read()) != -1) {
				char ch = (char) prov2;
				msgProv = msgProv + String.valueOf(ch);
				String prov = "";
				if (msgProv.length() > 7)
					prov = msgProv.substring(msgProv.length() - 7, msgProv.length());
				if (prov.compareTo("\r\n.\r\n\r\n") == 0) {
					msgProv = msgProv.substring(0, msgProv.length() - 7);
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgProv;
	}

	public void setUser(String userName) {
		// TODO Auto-generated method stub
		this.userName = userName;

	}

	public String getUserName() {
		// TODO Auto-generated method stub
		return this.userName;
	}

}
