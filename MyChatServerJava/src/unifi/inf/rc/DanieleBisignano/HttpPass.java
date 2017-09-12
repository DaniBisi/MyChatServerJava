package unifi.inf.rc.DanieleBisignano;

public class HttpPass implements HttpProtocol,statusChanger{

	private String[] params;
	private int loginResult;

	public HttpPass(String[] params) {
		this.params = params;
	}


	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		System.out.println("param[0] = " + params[0]);
		
		
		String responce = "";
		String userName = clientHandler.getUserName();
		Database database = clientHandler.getDatabase();
		String userPass = database.getPassword(userName);
		//System.out.println("userpass = "+ userPass + "pass = "+ pass);
		if (userPass != null && params[0].equals(userPass)) {
			loginResult = 2;
			responce = "OK\r\n";
		} else {
			loginResult = 0;
			responce = "KO\r\n";
		}
		clientHandler.acceptVisit(this);
		return responce;

	}
	

	public int getLoginResult() {
		return this.loginResult;
	}

}
