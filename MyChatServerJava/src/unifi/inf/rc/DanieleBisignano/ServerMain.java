package unifi.inf.rc.DanieleBisignano;

import java.util.HashMap;
import java.util.Map;

public class ServerMain {

	public static void main(String[] args) {
		String address;
		int port;
		Map<String, String> Dictionary;
		port = 1039;
		Dictionary = new HashMap<String, String>(200);
		Dictionary.put("dani", "ciao");
		Dictionary.put("giulio", "grima");
		Dictionary.put("marco", "bura");
		Dictionary.put("lore", "mari");
		MyChatServer server = new MyChatServer(Dictionary, "127.0.0.1", port);
		server.run();
	}

}
