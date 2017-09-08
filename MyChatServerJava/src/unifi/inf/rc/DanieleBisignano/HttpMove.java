package unifi.inf.rc.DanieleBisignano;

public class HttpMove implements HttpProtocol, statusChanger {
	private int loginStatus;
	private String[] params;

	public HttpMove(String[] params) {
		this.params = params;
	}

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		Room r1 = clientHandler.getRoom();
		String msg = "";
		int response = r1.move(Integer.parseInt(params[0]), Integer.parseInt(params[1]), clientHandler.getUserName());
		switch (response) {
		case -1:
			msg = "Illegal move, retry\r\n";
			loginStatus = 13;
			break;
		case 0:
			msg = "OK\r\n";
			loginStatus = 12;
			break;
		case 1:
			msg = "YOU WIN\r\n";
			clientHandler.addVictory(clientHandler.getUserName());
			loginStatus = 2;
			break;
		case 2:
			msg = "IT'S A DRAW\r\n";
			loginStatus = 2;
			break;
		}

		clientHandler.acceptVisit(this);
		return msg;
	}

	@Override
	public int getLoginResult() {
		return loginStatus;
	}

}
