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
		if(command.equalsIgnoreCase("GET")){
			return new HttpGet(params);
		}
		if(command.equalsIgnoreCase("USER")){
			return new HttpUser(params);
		}
		
		else {
			throw new IllegalArgumentException();
		}
		
	}
}
