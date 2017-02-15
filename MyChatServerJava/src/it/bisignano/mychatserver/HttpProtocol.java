package it.bisignano.mychatserver;
@FunctionalInterface
public interface HttpProtocol {
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException;
}


