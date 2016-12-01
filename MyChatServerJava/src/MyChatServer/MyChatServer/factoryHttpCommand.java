package MyChatServer.MyChatServer;
import MyChatServer.MyChatServer.HttpProtocol;
abstract public class factoryHttpCommand {
	
	public static HttpProtocol getHtmlProtocol(String param, int loginStatus){
		String params[] = param.split(" ",2);
		String command = params[0];
		try{
		params = params[1].split(" ");
		}catch (IndexOutOfBoundsException e){
			params = null;
		}
		if(command.equalsIgnoreCase("USER")&& (loginStatus == 0 || loginStatus ==2)){
			return new HttpUser(params);
		}
		else if(command.equalsIgnoreCase("GET") && loginStatus == 2){
			return new HttpGet(params);
		}
		else if(command.equalsIgnoreCase("PASS") && loginStatus == 1){
			return new HttpPass(params);
		}
		else if(command.equalsIgnoreCase("NEW") && loginStatus == 2){
			return new HttpNew(params);
		}
		else if(command.equalsIgnoreCase("MESSAGE") && loginStatus == 2){
			return new HttpMessage(params);
		}
		else if(command.equalsIgnoreCase("LIST") && loginStatus == 2){
			return new HttpList(params);
		}
		
		else {
			throw new IllegalArgumentException();
		}
		
	}
}
