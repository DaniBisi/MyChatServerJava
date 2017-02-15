package unifi.inf.rc.DanieleBisignano;
@FunctionalInterface
public interface HttpProtocol {
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException;
}


