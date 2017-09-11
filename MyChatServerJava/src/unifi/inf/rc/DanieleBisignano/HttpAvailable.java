package unifi.inf.rc.DanieleBisignano;

public class HttpAvailable implements HttpProtocol, statusChanger {
	private int loginResult;
	private Room room;

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		loginResult = 12;
		return clientHandler.acceptVisit(this);
		
	}

	@Override
	public int getLoginResult() {

		return this.loginResult;
	}

	public Room getRoom() {
		return room;
	}

}
