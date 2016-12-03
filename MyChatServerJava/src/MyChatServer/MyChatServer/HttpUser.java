package MyChatServer.MyChatServer;

public class HttpUser implements HttpProtocol{

	private String[] params;
	private String userName;
	public HttpUser(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}

	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response = "KO\r\n";
		if(this.params.length == 1){
			try {
				if(MyChatServer.Dictionary.get(params[0]) != null){
					response = "OK\r\n";
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			clientHandler.acceptVisit(this);
			clientHandler.setUser(params[0]);
		}
		
		return response;
	}

	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return "";
	}


}
