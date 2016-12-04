package MyChatServer.MyChatServer;

public class HttpRegister implements HttpProtocol {
	
	
	private String[] params;

	public HttpRegister(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response = "";
		if(params == null || params.length != 2) throw new IllegalArgumentException();
		else{
			String host = params[0];
			int port = Integer.parseInt(params[1]);
			String user = clientHandler.getUserName();
			if(MyChatServer.addRecord(host, port, user)){
				response = "OK\r\n";
			}
			else response = "KO\r\n";
		}

		return response;
	}
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		clientHandler.acceptVisit(this);
		return null;
	}
*/
}
