package unifi.inf.rc.DanieleBisignano;

import java.awt.List;
import java.io.IOException;
import java.lang.Thread;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.SortingFocusTraversalPolicy;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import unifi.inf.rc.DanieleBisignano.clientHandler;

import java.util.Set;
import java.util.TreeSet;

/**
 * Hello world!
 *
 */

public class MyTrisServer implements Runnable {
	private ServerSocket server;
	private static String address;
	private static int port;
	private int backlog;
	private Database database;

	public MyTrisServer(Map<String, String> Dictionary, String address, int port) {
		this.database = new Database(Dictionary);
		try {
			this.server = new ServerSocket(port, 1000, InetAddress.getByName(address));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket client = server.accept();
				new clientHandler(client,database).start();

			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}

	}
	@Override
	protected void finalize() throws Throwable {
		this.server = null;
		this.database = null;
		super.finalize();
	}

}
