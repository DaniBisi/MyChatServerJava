package unifi.inf.rc.DanieleBisignano;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class HttpRegister implements HttpProtocol,statusChanger {
	
	protected int loginResult;
	protected String[] params;

	public HttpRegister(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}
	
	public boolean validateIPAddress( String ipAddress ){ 
		final Pattern ipAdd= Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"); 
		return ipAdd.matcher(ipAddress).matches();
	}
	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		String response = "";
		if(params == null || params.length != 2 || !validateIPAddress(params[0]) || Integer.parseInt(params[1])<0 || Integer.parseInt(params[1])>65535){
			throw new IllegalArgumentException();
		}
		else{
			String host = params[0];
			int port = Integer.parseInt(params[1]);
			String user = clientHandler.getUserName();
			if(MyChatServer.addRecord(host, port, user)){
				this.loginResult = 3;
				clientHandler.acceptVisit(this);
				response = "OK\r\n";
			}
			else response = "KO\r\n";
		}
		
		return response;
	}
	public int getLoginResult() {
		// TODO Auto-generated method stub
		return this.loginResult;
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
