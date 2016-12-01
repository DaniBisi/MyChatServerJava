package MyChatServer.MyChatServer;

public class HttpPass implements HttpProtocol {

	private String[] params;


	public HttpPass(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}


	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		String response = "KO\r\n";
		if(this.params.length == 1){
			try {
				String pass = MyChatServer.Dictionary.get(clientHandler.getUserName());
				if(pass != null){
					clientHandler.setLoginStatus(2);
					response = "OK\r\n";
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return response;
	}
	

	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		clientHandler.setLoginStatus(2);
		return "";
	}

}
