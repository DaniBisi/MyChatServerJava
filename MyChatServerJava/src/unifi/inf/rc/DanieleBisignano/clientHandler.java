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

public class clientHandler extends Thread implements observer {

	private Socket client;
	private InputStream in;
	private OutputStream out;
	private int loginStatus;
	private String userName;
	private Room r1;
	private Database database;

	private void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
		// System.out.println("Loginstatus:" + this.loginStatus);
	}

	public int getLoginStatus() {
		return this.loginStatus;
	}

	public clientHandler(Socket client, Database database) {
		this.client = client;
		this.database = database;
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
			while (loginStatus >= 0 && (prov2 = this.in.read()) != -1) {
				char ch = (char) prov2;
				msg = msg + String.valueOf(ch);
				String prov = "";
				if (msg.length() > 1) {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String acceptVisit(HttpPass cmd, String pass) {
		String responce = "";
		String userPass = database.getPassword(userName);
		System.out.println("userpass = "+ userPass + "pass = "+ pass);
		if (userPass != null && userPass.equals(pass)) {
			this.setLoginStatus(2);
			responce = "OK\r\n";
		} else {
			this.setLoginStatus(0);
			responce = "KO\r\n";
		}
		return responce;

	}

	public String acceptVisit(statusChanger cmd) {
		this.setLoginStatus(cmd.getLoginResult());
		return "";

	}

	public String acceptVisit(HttpAvailable cmd) {

		this.setRoom(database.addPlayer(this));
		this.setLoginStatus(cmd.getLoginResult());
		if (r1.isFull()) {
			this.setLoginStatus(13);
			return "MATCH FOUND: command available: \"MOVE x,y\" , \"CONCEDE\"\r\n";
		} else {
			this.setLoginStatus(12);
			return "OK\r\n";
		}

	}

	private void setRoom(Room room) {
		this.r1 = room;

	}

	public String acceptVisit(HttpUser cmd) {
		this.setLoginStatus(1);
		this.setUser(cmd.getUserName());
		return "";
	}

	public void setUser(String userName) {
		this.userName = userName;

	}

	public String getUserName() {
		return this.userName;
	}

	public Room getRoom() {
		return this.r1;
	}

	@Override
	public void getUpdate(String msg, int code) {
		if (code == 2) {// sconfitta
			database.addDefeat(userName);
		} else if (code == 11) {// pareggio
			this.loginStatus = 2;
		} else {// mossa normale
			this.loginStatus = code;
		}
		try {
			this.out.write(msg.getBytes("latin1"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean Register(String userName, String password) {
		return this.database.Signup(userName, password);
	}

	public String getRanking() {
		return database.getRanking();
	}

	public void addVictory(String userNameWinner) {
		database.addVictory(userNameWinner);

	}

}
