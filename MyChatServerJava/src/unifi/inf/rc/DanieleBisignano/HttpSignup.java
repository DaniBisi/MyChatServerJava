package unifi.inf.rc.DanieleBisignano;

public class HttpSignup implements HttpProtocol {
	private String[] params;

	public HttpSignup(String[] params) {
		this.params = params;
	}

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		if(MyChatServer.Signup(params[0],params[1])){
		return "OK\r\n";
		}
		else throw new IllegalArgumentException();
	}

}
