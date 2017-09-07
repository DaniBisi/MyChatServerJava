package unifi.inf.rc.DanieleBisignano;
import java.util.Set;

import unifi.inf.rc.DanieleBisignano.HttpProtocol;
abstract public class factoryHttpCommand {
	
	public static HttpProtocol getHtmlProtocol(String param, int loginStatus){
		//param = param.trim();
		String params[] = param.split(" ",2);
		String command = params[0];
//		for(int i=1;i<params.length;i++){
//			if(params[i].equals("")){
//				params[i] = null;
//			}
//		}
		try{
		//params[params.length-1] = params[params.length-1].trim();
			System.out.println(command);
			System.out.println(params);
		params = params[1].split(" ");
		}catch (IndexOutOfBoundsException e){
			params = null;
		}
		if(command.equals("USER")&& loginStatus == 0 /*|| loginStatus >1)*/ && !params[0].equals("")){ // se si vuole permettere un altro login (cambio sessione.)
			return new HttpUser(params);
		}
		else if(command.equals("GET") && loginStatus >1 && params.length == 1){
			return new HttpGet(params);
		}
		else if(command.equals("PASS") && loginStatus == 1){
			return new HttpPass(params);
		}
		else if(command.equals("NEW") && loginStatus >1){
			return new HttpNew(params);
		}
		else if(command.equals("TOPICS") && loginStatus >1 && params == null){
			return new HttpTopics(params);
		}
		else if(command.equals("MESSAGE")){
			return new HttpMessage(params,loginStatus>1);
			
				
		}
		else if(command.equals("LIST") && loginStatus >1){
			return new HttpList(params);
		}

		else if(command.equals("REPLY") && loginStatus >1){
			return new HttpReply(params, loginStatus >1);
		}

		else if(command.equals("CONV") && loginStatus >1){
			return new HttpConv(params);
		}
		else if(command.equals("REGISTER") && loginStatus >1){
			return new HttpRegister(params);
		}
		else if(command.equals("UNREGISTER") && loginStatus >1){
			return new HttpUnregister(params);
		}
		else if(command.equals("SUBSCRIBE") && loginStatus >2){
			
			return new HttpSubscribe(params,(loginStatus >3));
		}
		else if(command.equals("UNSUBSCRIBE") && loginStatus >2){
			return new HttpUnSubscribe(params,(loginStatus >3));
		}
		else if(command.equals("DIGEST") && loginStatus >3){
			return new cmdDigest(params,(loginStatus >4));
		}
		else if(command.equals("AVAILABLE") && loginStatus>1 && params == null){
			return new HttpAvailable();
		}
		else if(command.equals("MOVE") && loginStatus>12 && params.length == 2){
			return new HttpMove(params);
		}
		
		
		else {
			throw new IllegalArgumentException();
		}
		
	}
}
