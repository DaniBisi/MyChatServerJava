package it.bisignano.mychatserver;
@FunctionalInterface
public interface IHttpProtocol {
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException;
}


