package MyChatServer.MyChatServer;

import java.util.ArrayList;

public interface HttpProtocol {
	public String execute(clientHandler clientHandler) throws IllegalArgumentException;
	public String visit(clientHandler clientHandler);
}


