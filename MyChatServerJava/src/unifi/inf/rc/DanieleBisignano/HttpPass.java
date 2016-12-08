package unifi.inf.rc.DanieleBisignano;

public class HttpPass implements HttpProtocol,statusChanger{

	private String[] params;
	private int loginResult;

	public HttpPass(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}


	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		String response = "KO\r\n";
		if(this.params.length == 1){
				//if(MyChatServer.Dictionary.containsKey(clientHandler.getUserName())){
			    String pass = MyChatServer.Dictionary.get(clientHandler.getUserName());
				if(pass!= null && pass.equals(params[0])){
					loginResult = 2;
					clientHandler.acceptVisit(this);
					response = "OK\r\n";
				}
				//}
				else{
					loginResult = 0;
					clientHandler.acceptVisit(this);
					response = "KO\r\n";
				}
		}
		
		return response;
	}
	
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		//clientHandler.setLoginStatus(2);
		return "";
	}
*/

	public int getLoginResult() {
		// TODO Auto-generated method stub
		return this.loginResult;
	}

}
