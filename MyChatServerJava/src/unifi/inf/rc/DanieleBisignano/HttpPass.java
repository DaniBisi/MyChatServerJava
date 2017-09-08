package unifi.inf.rc.DanieleBisignano;

public class HttpPass implements HttpProtocol,statusChanger{

	private String[] params;
	private int loginResult;

	public HttpPass(String[] params) {
		this.params = params;
	}


	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		String response = clientHandler.acceptVisit(this,params[0]);
		return response;
	}
	

	public int getLoginResult() {
		return this.loginResult;
	}

}
