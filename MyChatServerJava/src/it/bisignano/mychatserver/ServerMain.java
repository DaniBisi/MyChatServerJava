package it.bisignano.mychatserver;

import java.util.HashMap;
import java.util.Map;

public class ServerMain {

	private ServerMain(){}
	public static void main(String[] args) {
		int port;
		Map<String, String> dictionary;
		port = 1039;
		dictionary = new HashMap<String, String>(200);
		dictionary.put("dani", "ciao");
		dictionary.put("giulio", "grima");
		dictionary.put("marco", "bura");
		dictionary.put("lore", "mari");
		MyChatServer server = new MyChatServer(dictionary, "127.0.0.1", port);
		server.run();
	}

}
