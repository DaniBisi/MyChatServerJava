package unifi.inf.rc.DanieleBisignano;

public class HttpAvailable implements HttpProtocol,statusChanger {
	private int loginResult;
	private Room r1;
	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		loginResult = 12;
		String msg="OK\n";
		r1 =MyChatServer.addPlayer(clientHandler);
		clientHandler.acceptVisit(this);
		if(r1.isFull()){
			loginResult = 13;
			msg="MATCH FOUND: command available: \"POSITION x,y\" , \"CONCEDE\"\n";
		}
		return msg;
	}

	@Override
	public int getLoginResult() {
		
		return this.loginResult;
	}

	public Room getRoom() {
		return r1;
	}

}
