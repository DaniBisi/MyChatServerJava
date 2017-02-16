package it.bisignano.mychatserver;

public class CmdDigest implements IHttpProtocol {

	private String[] params;
	private boolean alreadySubscribed;

	public CmdDigest(String[] params, boolean b) {
		this.params = params;
		this.alreadySubscribed = b;
	}

	@Override
	public String execute(ClientHandler clientHandler){
		if (params.length!=1){
			return "KO\r\n";
		}
		if(this.alreadySubscribed || MyChatServer.checkRegisterError(clientHandler.getUserName())){
			MyChatServer.setDigest(clientHandler.getUserName(),Integer.parseInt(params[0]));
			
		}
		return "OK\r\n";
	}

}
