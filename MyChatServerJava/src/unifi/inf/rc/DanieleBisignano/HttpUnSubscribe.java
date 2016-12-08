package unifi.inf.rc.DanieleBisignano;

public class HttpUnSubscribe implements HttpProtocol,statusChanger {

	private String[] params;
	private boolean alreadySubscribed;

	public HttpUnSubscribe(String[] params, boolean b) {
		this.params = params;
		this.alreadySubscribed = b;
		// TODO Auto-generated constructor stub
	}

	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response;
		if (this.params.length>0 && (this.alreadySubscribed || MyChatServer.checkRegisterError(clientHandler.getUserName()))) {
			boolean c = MyChatServer.rmSubScription(params, clientHandler.getUserName());
			response = "OK\r\n";
		} else
			response = "KO\r\n";

		return response;
	}
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return null;
	}*/

	public int getLoginResult() {
		// TODO Auto-generated method stub
		return 3;
	}

}
