package it.bisignano.mychatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ChatClient {

	private static final Logger LOGGER = LogManager.getLogger(ChatClient.class);
	private String string;
	private int port;
	private Socket socket;
	private InputStream in;
	private OutputStream out;

	public ChatClient(String string, int port) {
		this.string = string;
		this.port = port;
		BasicConfigurator.configure();

	}

	public boolean connectServer() {
		this.socket = new Socket();
		boolean connectionStatus = true;
		try {
			this.socket.connect(new InetSocketAddress(this.string, this.port));
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
		} catch (IOException e) {
			LOGGER.error(e);
			LOGGER.info("SERVER NOT READY");
			connectionStatus = false;
		}

		return connectionStatus;
	}

	public void sendMsg(String msg) {
		try {
			this.out.write(msg.getBytes("latin1"));
		} catch (Exception e) {
			LOGGER.error(e);
		}

	}

	public String receiveMsg() {
		String msg = "";
		byte[] streamIn = new byte[2048];
		try {
			int lenghtProv = this.in.read(streamIn);
			msg = msg + (new String(streamIn, StandardCharsets.UTF_8)).substring(0, lenghtProv);

		} catch (Exception e) {
			LOGGER.error(e);
		}
		return msg;

	}

	public String receiveMsg(int a) {
		String msg = "";
		byte[] streamIn = new byte[a];
		try {
			int lenghtProv = this.in.read(streamIn);
			msg = msg + (new String(streamIn, StandardCharsets.UTF_8)).substring(0, lenghtProv);

		} catch (Exception e) {
			LOGGER.error(e);
		}
		return msg;
	}

	public void closeSocket() {
		try {
			this.socket.close();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

}
