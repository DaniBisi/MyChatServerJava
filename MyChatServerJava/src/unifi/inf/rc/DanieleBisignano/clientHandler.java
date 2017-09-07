package unifi.inf.rc.DanieleBisignano;

import java.awt.print.Printable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Observer;

public class clientHandler extends Thread implements visitable, observer {

	private Socket client;
	private InputStream in;
	private OutputStream out;
	private int loginStatus;
	private String userName;
	private int digest;
	private ArrayList<String> messageQueue;//#probabilmente anche questo andrà nel server.
	private Room r1;

	private void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
		// System.out.println("Loginstatus:" + this.loginStatus);
	}

	public int getLoginStatus() {
		return this.loginStatus;
	}

	public clientHandler(Socket client) {
		this.client = client;
		this.loginStatus = 0;
		try {
			this.in = this.client.getInputStream();
			this.out = this.client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	@Override
	public void run() {
		// System.out.println("im waiting for data...");
		String msg = "";
		// byte streamIn[] = new byte[2048];
		try {
			int prov2;
			while ((prov2 = this.in.read()) != -1) {
				char ch = (char) prov2;
				msg = msg + String.valueOf(ch);
				String prov = "";
				if (msg.length() > 1){
					prov = msg.substring(msg.length() - 2, msg.length());
				}
				if (prov.compareTo("\r\n") == 0) {
					String Response = "";
					String command = msg.replace("\r\n", "");
					try {
						HttpProtocol commandR;
						Response = Response + (commandR = factoryHttpCommand.getHtmlProtocol(command, this.loginStatus))
								.execute(this);
					} catch (Exception e) {
						Response = Response + "KO\r\n";
					}
					this.out.write(Response.getBytes("latin1"));
					msg = "";
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String acceptVisit(statusChanger cmd){
		this.setLoginStatus(cmd.getLoginResult());
		return "";
		
	}
	public String acceptVisit(HttpAvailable cmd){
		this.setRoom(cmd.getRoom());
		this.setLoginStatus(cmd.getLoginResult());
		return "";
		
	}
//
//	public String acceptVisit(HttpPass cmd) {
//		this.setLoginStatus(cmd.getLoginResult());
//		return "";
//	}
//
//	public String acceptVisit(HttpRegister reg) {
//		//this.setLoginStatus(3);
//		this.setLoginStatus(reg.getLoginResult());
//		return null;
//	}
//	public void acceptVisit(HttpSubscribe Sub) {
//		this.setLoginStatus(Sub.getLoginResult());
//		// TODO Auto-generated method stub
//		
//	}
	
	
	private void setRoom(Room room) {
		this.r1 = room;
		
	}

	public String acceptVisit(HttpUser cmd) {
		this.setLoginStatus(1);
		this.setUser(cmd.getUserName());
		return "";
	}

	public String acceptVisit(HttpMessage msg) {
		String msgProv = "";
		try {
			int prov2;
			while ((prov2 = this.in.read()) != -1) {
				char ch = (char) prov2;
				msgProv = msgProv + String.valueOf(ch);
				String prov = "";
				if (msgProv.length() > 6)
					prov = msgProv.substring(msgProv.length() - 7, msgProv.length());
				if (prov.compareTo("\r\n.\r\n\r\n") == 0) {
					msgProv = msgProv.substring(0, msgProv.length() - 7);
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msgProv;
	}

	public String acceptVisit(factoryHttpCommand fact) {
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msgProv;
	}

	public void setUser(String userName) {
		this.userName = userName;

	}

	public String getUserName() {
		return this.userName;
	}

	@Override
	public void getUpdate() {
		this.loginStatus = 13;
		
	}


}
