package unifi.inf.rc.DanieleBisignano;

import java.util.HashMap;
import java.util.Map;

public class Database {

	private static Map<String, String> Dictionary;
	private HashMap<String, Integer> Victory;
	private HashMap<String, Integer> Defeat;
	private Room room = null;

	public Database() {
		this.Dictionary = new HashMap<String, String>(200);
		this.Victory = new HashMap<String, Integer>(200);
		this.Defeat = new HashMap<String, Integer>(200);
	}

	public int getVictory(String userName) {
		return Victory.get(userName);
	}

	public int getDefeat(String userName) {
		return Defeat.get(userName);
	}

	public synchronized void addVictory(String userName) {
		if (Victory.containsKey(userName)) {
			int app = Victory.get(userName).intValue();
			Victory.replace(userName, app, app + 1);
		} else {
			Victory.put(userName, 1);
		}
	}

	public synchronized void addDefeat(String userName) {
		if (Defeat.containsKey(userName)) {
			int app = Defeat.get(userName).intValue();
			Defeat.replace(userName, app, app + 1);
		} else {
			Defeat.put(userName, 1);
		}
	}

	public String getRanking() {
		String ranking = "Username \t Victory \t Defeat \t\n";
		for (String userName : Victory.keySet()) {
			ranking = ranking + userName + ": \t\t" + Victory.get(userName) + "\t\t";
			int defeat = 0;
			if (Defeat.containsKey(userName)) {
				defeat = Defeat.get(userName);
			}
			ranking = ranking + defeat + "\n";
		}
		ranking = ranking + "\r\n";
		System.out.println(ranking);
		return ranking;

	}

	public String getPassword(String userName) {
		if (Dictionary.containsKey(userName))
			return Dictionary.get(userName);
		else
			return null;
	}

	public synchronized Room addPlayer(clientHandler clientHandler) {
		Room app;
		if (room == null) {
			room = new Room(clientHandler);
			app = room;
		} else {
			app = room;
			app.addUser(clientHandler);
			room = null;
			System.out.println("questo è room " + room);
		}

		return app;
	}

	public synchronized boolean Signup(String userName, String password) {
		if (!Dictionary.containsKey(userName)) {
			Dictionary.put(userName, password);
			return true;
		}
		return false;

	}

}
