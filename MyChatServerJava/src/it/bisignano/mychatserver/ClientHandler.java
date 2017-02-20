package it.bisignano.mychatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ClientHandler extends Thread implements Visitable {

	private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);
	private Socket client;
	private InputStream in;
	private OutputStream out;
	private int loginStatus;
	private String userName;


	public ClientHandler(Socket client) {
		BasicConfigurator.configure();
		this.client = client;
		this.loginStatus = 0;
		try {
			this.in = this.client.getInputStream();
			this.out = this.client.getOutputStream();
		} catch (IOException e) {
			LOGGER.error(e);
			LOGGER.info("STREAM FAILURE... " + e.toString());
		}
	}
	private void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
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
				if (sb.length() > 1){
					prov = sb.substring(sb.length() - 2, sb.length());
				}
				if (prov.compareTo("\r\n") == 0) {
					String response = execute(sb.toString());
					this.out.write(response.getBytes("latin1"));
					sb.setLength(0); // svuoto lo String builder
				}
			}
		}catch (Exception e) {
			LOGGER.error(e);
			LOGGER.info("SOMETHING UNEXPECTED... " + e.toString());
		}
	}

	private String execute(String msg) {
		String response = "";
		String command = msg.replace("\r\n", "");
		try {
			IHttpProtocol commandR;
			commandR = FactoryHttpCommand.getHtmlProtocol(command, this.loginStatus);
			response = response + commandR.execute(this);
		} catch (Exception e) {
			LOGGER.error(e + " " + msg);
			response = response + "KO\r\n";
		}
		return response;
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
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e);
			LOGGER.info("UNSUPPORTED ENCODING... " + e.toString());
		} catch (IOException e) {

			LOGGER.error(e);
			LOGGER.info("INPUT EXCEPTION... " + e.toString());
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

}
