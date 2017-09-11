package unifi.inf.rc.DanieleBisignano;

public class HttpExit implements HttpProtocol,statusChanger {

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		clientHandler.acceptVisit(this);
		return "GOODBYE\r\n";
	}
	@Override
	public int getLoginResult() {
		return -1;
	}

}
