package it.bisignano.mychatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ClientHandler extends Thread implements Visitable {

	private final Logger logger;
	private Socket client;
	private InputStream in;
	private OutputStream out;
	private int loginStatus;
	private String userName;
	private String response;
	private int lastMethodInvocationLog;
	private MyChatData myDataStructure;


	public ClientHandler(Socket client,MyChatData myDataStructure) {
		this.logger = LogManager.getLogger(ClientHandler.class);
		this.myDataStructure = myDataStructure;
		lastMethodInvocationLog = 0;
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		this.client = client;
		this.loginStatus = 0;
		try {
			this.in = this.client.getInputStream();
			this.out = this.client.getOutputStream();
		} catch (IOException e) {
			lastMethodInvocationLog = 1;
			logger.error(e);
			logger.info("STREAM FAILURE... " + e.toString());
		}
	}
	private void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}

	public int getLoginStatus() {
		return this.loginStatus;
	}


	public boolean isConnected() {
		return client.isConnected();
	}

	@Override
	public void run() {
		StringBuilder sb = new StringBuilder();
		try {
			int prov2;
			while ((prov2 = this.in.read()) != -1) {
				char ch = (char) prov2;
				sb.append(String.valueOf(ch));
				String prov = "";
				int lenght = sb.length();
				if (lenght > 1){
					prov = sb.substring(lenght - 2, lenght);
				}
				if (prov.compareTo("\r\n") == 0) {
					this.response = execute(sb.toString());
					this.out.write(response.getBytes("latin1"));
					sb.setLength(0); // svuoto lo String builder
				}
			}
		}catch (Exception e) {
			this.response = "Error Reading..";
			logger.error(e);
			logger.info("SOMETHING UNEXPECTED... " + e.toString());
		}
	}

	public String getResponse(){
		return this.response;
	}
	private String execute(String msg) {
		this.response = "";
		String command = msg.replace("\r\n", "");
		try {
			IHttpProtocol commandR;
			commandR = FactoryHttpCommand.getHtmlProtocol(command, this.loginStatus);
			this.response = this.response + commandR.execute(this);
		} catch (Exception e) {
			logger.error(e + " " + msg);
			this.response = this.response + "KO\r\n";
		}
		return this.response;
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

	//@Override
	public String acceptVisit(HttpMessage msg) {
		StringBuilder sb = new StringBuilder();
		try {
			int prov2;
			while ((prov2 = this.in.read()) != -1) {
				char ch = (char) prov2;
				sb.append(String.valueOf(ch));
				String prov = "";
				if (sb.length() > 7){
					prov = sb.substring(sb.length() - 7, sb.length());
				}
				if (prov.compareTo("\r\n.\r\n\r\n") == 0) {
					sb.setLength(sb.length() - 7);
					break;
				}
			}
		}catch (IOException e) {

			logger.error(e);
			logger.info("INPUT EXCEPTION... " + e.toString());
		}
		return sb.toString();
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
	public int getLastMethodInvocationLog() {
		return lastMethodInvocationLog;
	}

}
