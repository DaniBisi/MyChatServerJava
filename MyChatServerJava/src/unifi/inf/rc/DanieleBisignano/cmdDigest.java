package unifi.inf.rc.DanieleBisignano;

public class cmdDigest implements HttpProtocol {

	private String[] params;
	private boolean alreadySubscribed;

	public cmdDigest(String[] params, boolean b) {
		this.params = params;
		this.alreadySubscribed = b;
		// TODO Auto-generated constructor stub
	}

	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		if (params.length!=1)return "KO\r\n";
		if(this.alreadySubscribed || MyChatServer.checkRegisterError(clientHandler.getUserName())){
			MyChatServer.setDigest(clientHandler.getUserName(),Integer.parseInt(params[0]));
			
		}
		// TODO Auto-generated method stub
		return "OK\r\n";
	}

}
