package unifi.inf.rc.DanieleBisignano;

public class HttpUnregister implements HttpProtocol {

	private String[] params;

	public HttpUnregister(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}

	//@Override
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if(params != null)throw new IllegalArgumentException();
		String response;
		if(MyChatServer.unRegister(clientHandler.getUserName())&& MyChatServer.unSubscribe(clientHandler.getUserName()) ) response = "OK\r\n";
		else response = "KO\r\n";
		return response;
	}
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
