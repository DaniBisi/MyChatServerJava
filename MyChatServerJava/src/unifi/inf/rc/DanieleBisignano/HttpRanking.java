package unifi.inf.rc.DanieleBisignano;

public class HttpRanking implements HttpProtocol{

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		return clientHandler.getRanking();
	}
	
}
