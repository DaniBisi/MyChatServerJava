package MyChatServer.MyChatServer;
import java.util.Set;

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
		if(command.equalsIgnoreCase("USER")&& (loginStatus == 0 || loginStatus >1)){
			return new HttpUser(params);
		}
		else if(command.equalsIgnoreCase("GET") && loginStatus >1){
			return new HttpGet(params);
		}
		else if(command.equalsIgnoreCase("PASS") && loginStatus == 1){
			return new HttpPass(params);
		}
		else if(command.equalsIgnoreCase("NEW") && loginStatus >1){
			return new HttpNew(params);
		}
		else if(command.equalsIgnoreCase("MESSAGE") && loginStatus >1){
			return new HttpMessage(params);
		}
		else if(command.equalsIgnoreCase("LIST") && loginStatus >1){
			return new HttpList(params);
		}

		else if(command.equalsIgnoreCase("REPLY") && loginStatus >1){
			return new HttpReply(params);
		}

		else if(command.equalsIgnoreCase("CONV") && loginStatus >1){
			return new HttpConv(params);
		}
		else if(command.equalsIgnoreCase("REGISTER") && loginStatus >1){
			return new HttpRegister(params);
		}
		else if(command.equalsIgnoreCase("UNREGISTER") && loginStatus >1){
			return new HttpUnregister(params);
		}
		else if(command.equalsIgnoreCase("SUBSCRIBE") && loginStatus >1){
			
			return new HttpSubscribe(params,(loginStatus >2));
		}
		else if(command.equalsIgnoreCase("UNSUBSCRIBE") && loginStatus >1){
			return new HttpUnSubscribe(params,(loginStatus >2));
		}
		
		
		else {
			throw new IllegalArgumentException();
		}
		
	}
}
