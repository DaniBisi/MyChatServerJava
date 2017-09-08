package unifi.inf.rc.DanieleBisignano;

public class HttpAvailable implements HttpProtocol,statusChanger {
	private int loginResult;
	private Room room;
	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		loginResult = 12;
		String msg="OK\r\n";
		room = MyChatServer.addPlayer(clientHandler);
		if(room.isFull()){
			loginResult = 13;
			msg="MATCH FOUND: command available: \"MOVE x,y\" , \"CONCEDE\"\r\n";
		}
		clientHandler.acceptVisit(this);
		return msg;
	}

	@Override
	public int getLoginResult() {
		
		return this.loginResult;
	}

	public Room getRoom() {
		return room;
	}

}
