package unifi.inf.rc.DanieleBisignano;
import java.util.Set;

import unifi.inf.rc.DanieleBisignano.HttpProtocol;
abstract public class factoryHttpCommand {
	
	public static HttpProtocol getHtmlProtocol(String param, int loginStatus){
		param = param.trim();
		String params[] = param.split(" ",2);
		String command = params[0];
//		for(int i=1;i<params.length;i++){
//			if(params[i].equals("")){
//				params[i] = null;
//			}
//		}
		try{
		//params[params.length-1] = params[params.length-1].trim();
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
		else if(command.equalsIgnoreCase("TOPICS") && loginStatus >1){
			return new HttpTopics(params);
		}
		else if(command.equalsIgnoreCase("MESSAGE")){
			return new HttpMessage(params,loginStatus>1);
			
				
		}
		else if(command.equalsIgnoreCase("LIST") && loginStatus >1){
			return new HttpList(params);
		}

		else if(command.equalsIgnoreCase("REPLY")){
			return new HttpReply(params, loginStatus >1);
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
		else if(command.equalsIgnoreCase("SUBSCRIBE") && loginStatus >2){
			
			return new HttpSubscribe(params,(loginStatus >3));
		}
		else if(command.equalsIgnoreCase("UNSUBSCRIBE") && loginStatus >2){
			return new HttpUnSubscribe(params,(loginStatus >3));
		}
		else if(command.equalsIgnoreCase("DIGEST") && loginStatus >3){
			return new cmdDigest(params,(loginStatus >4));
		}
		
		
		else {
			throw new IllegalArgumentException();
		}
		
	}
}
