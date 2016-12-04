package MyChatServer.MyChatServer;

public class HttpUnSubscribe implements HttpProtocol {

	private String[] params;
	private boolean alreadySubscribed;

	public HttpUnSubscribe(String[] params, boolean b) {
		this.params = params;
		this.alreadySubscribed = b;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response;
		if (this.alreadySubscribed || MyChatServer.checkRegisterError(clientHandler.getUserName())) {
			boolean c = MyChatServer.rmSubScription(params, clientHandler.getUserName());
			response = "OK\r\n";
		} else
			response = "KO\r\n";

		return response;
	}

	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return null;
	}

}
