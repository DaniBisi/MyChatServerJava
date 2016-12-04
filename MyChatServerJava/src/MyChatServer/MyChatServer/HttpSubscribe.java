package MyChatServer.MyChatServer;

public class HttpSubscribe implements HttpProtocol {

	private String[] params;
	private boolean alreadySubscribed;
	public HttpSubscribe(String[] params, boolean b) {
		this.params = params;
		alreadySubscribed = b;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		String response ="";
		// TODO Auto-generated method stub
		if(this.alreadySubscribed || MyChatServer.checkRegisterError(clientHandler.getUserName())){
			boolean c = MyChatServer.addSubscription(params,clientHandler.getUserName());
			response = "OK\r\n";
		}
		else response = "KO\r\n";
		
		return response;
	}
/*
	@Override
	public String visit(clientHandler clientHandler) {
		
		return null;
	}*/

}
