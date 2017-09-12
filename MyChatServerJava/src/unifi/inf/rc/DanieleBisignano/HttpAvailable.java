package unifi.inf.rc.DanieleBisignano;

public class HttpAvailable implements HttpProtocol, statusChanger {
	private int loginResult;
	private Room room;

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		loginResult = 12;
		Database db = clientHandler.getDatabase();
		String userName = clientHandler.getUserName();
		this.room = db.addPlayer(clientHandler);
		String response = "";
		if (room.isFull()) {
			this.loginResult = 13;
			response = "MATCH FOUND: command available: \"MOVE x,y\" , \"CONCEDE\"\r\n";
		} else {
			this.loginResult = 12;
			response = "OK\r\n";
		}

		clientHandler.acceptVisit(this);
		return response;
	}

	@Override
	public int getLoginResult() {

		return this.loginResult;
	}

	public Room getRoom() {
		return room;
	}

}
