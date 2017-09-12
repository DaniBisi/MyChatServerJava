package unifi.inf.rc.DanieleBisignano;

import java.util.HashMap;
import java.util.Map;

public class serverMain {

	public static void main(String[] args) {
		String address;
		int port;
		Map<String, String> Dictionary;

		port = 1039;
		address = "127.0.0.1";
		Dictionary = new HashMap<String, String>(200);
		Dictionary.put("dani", "ciao");
		Dictionary.put("giulio", "grima");
		Dictionary.put("marco", "bura");
		Dictionary.put("lore", "mari");
		MyTrisServer server = new MyTrisServer(Dictionary, address, port);
		server.run();
	}

}
