package it.bisignano.mychatserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */

public class MyChatServer extends Thread {
	private static final Logger LOGGER = LogManager.getLogger(MyChatServer.class);
	private ServerSocket server;
	private String address;
	private int port;
	private MyChatData myDataStructure;
	private static Map<String, String> dictionary;

	public MyChatServer(Map<String, String> dictionary, String address, int port,MyChatData myDataStructure) {
		this.address = address;
		this.port = port;
		MyChatServer.dictionary = dictionary;
		this.myDataStructure = myDataStructure;
		try {
			this.server = new ServerSocket(this.port, 1000, InetAddress.getByName(this.address));
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public static Map<String, String> getDictionary() {
		return dictionary;
	}

	public boolean closeSocket() {
		boolean shutdown = true;
		try {
			this.server.close();
		} catch (IOException e) {
			LOGGER.error(e);
			shutdown = false;
		}
		return shutdown;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Socket client = server.accept();
				new ClientHandler(client,myDataStructure).start();

			} catch (IOException e) {
				LOGGER.error(e);
				break;
			} finally {
				this.closeSocket();
			}
		}

	}

	

}
