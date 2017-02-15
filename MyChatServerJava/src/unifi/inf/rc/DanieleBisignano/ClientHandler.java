package unifi.inf.rc.DanieleBisignano;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ClientHandler extends Thread implements visitable {

	private Socket client;
	private InputStream in;
	private OutputStream out;
	private int loginStatus;
	private String userName;

	private void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}

	public int getLoginStatus() {
		return this.loginStatus;
	}

	public ClientHandler(Socket client) {
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
		String msg = "";
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
						commandR = factoryHttpCommand.getHtmlProtocol(command, this.loginStatus);
						Response = Response + commandR.execute(this);
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

	

	public String acceptVisit(HttpPass cmd) {
		this.setLoginStatus(cmd.getLoginResult());
		return "";
	}
	
	public String acceptVisit(HttpUser cmd) {
		this.setLoginStatus(1);
		this.setUser(cmd.getUserName());
		return "";
	}

	@Override
	public String acceptVisit(HttpMessage msg) {
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

			System.out.println("unsupported encoding");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("log eccezione di input");
		}
		return msgProv;
	}

	
	public String acceptVisit(HttpRegister reg) {
		this.setLoginStatus(reg.getLoginResult());
		return null;
	}

	public void setUser(String userName) {
		this.userName = userName;

	}

	public String getUserName() {
		return this.userName;
	}

	public void acceptVisit(HttpSubscribe sub) {
		this.setLoginStatus(sub.getLoginResult());
		
	}

}
