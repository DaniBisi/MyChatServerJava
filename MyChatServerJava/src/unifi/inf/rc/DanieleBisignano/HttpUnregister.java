package unifi.inf.rc.DanieleBisignano;

public class HttpUnregister implements HttpProtocol,statusChanger {

	private String[] params;

	public HttpUnregister(String[] params) {
		this.params = params;
//		if(this.params != null && (this.params.length == 0 || this.params[0].equals(null) || this.params[0].equals("")) ){
//			this.params =null;
//		}
		// TODO Auto-generated constructor stub
	}

	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if(params != null)throw new IllegalArgumentException();
		String response;
		if(MyChatServer.unRegister(clientHandler.getUserName())&& MyChatServer.unSubscribe(clientHandler.getUserName()) ){
			response = "OK\r\n";
			clientHandler.acceptVisit(this);
		}
		else response = "KO\r\n";
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
		return 2;
	}

}
