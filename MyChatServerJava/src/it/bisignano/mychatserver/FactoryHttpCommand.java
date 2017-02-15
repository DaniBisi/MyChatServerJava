package it.bisignano.mychatserver;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import it.bisignano.mychatserver.HttpProtocol;
public abstract class FactoryHttpCommand {
	private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);
	private FactoryHttpCommand(){
		throw new IllegalAccessError();
	}
	public static HttpProtocol getHtmlProtocol(String param, int loginStatus){
		String paramT = param.trim();
		String[] params = paramT.split(" ",2);
		String command = params[0];
		try{
		params = params[1].split(" ");
		}catch (IndexOutOfBoundsException e){
			LOGGER.error(e);
			params = null;
		}
		if("USER".equalsIgnoreCase(command)&& (loginStatus == 0 || loginStatus >1)){
			return new HttpUser(params);
		}
		else if("GET".equalsIgnoreCase(command) && loginStatus >1){
			return new HttpGet(params);
		}
		else if("PASS".equalsIgnoreCase(command) && loginStatus == 1){
			return new HttpPass(params);
		}
		else if("NEW".equalsIgnoreCase(command) && loginStatus >1){
			return new HttpNew(params);
		}
		else if("TOPICS".equalsIgnoreCase(command) && loginStatus >1 && params == null){
			return new HttpTopics();
		}
		else if("MESSAGE".equalsIgnoreCase(command)){
			return new HttpMessage(params,loginStatus>1);	
		}
		else if("LIST".equalsIgnoreCase(command) && loginStatus >1){
			return new HttpList(params);
		}

		else if("REPLY".equalsIgnoreCase(command)){
			return new HttpReply(params, loginStatus >1);
		}

		else if("CONV".equals(command) && loginStatus >1){
			return new HttpConv(params);
		}
		else if("REGISTER".equalsIgnoreCase(command) && loginStatus >1){
			return new HttpRegister(params);
		}
		else if("UNREGISTER".equalsIgnoreCase(command) && loginStatus >1){
			return new HttpUnregister(params);
		}
		else if("SUBSCRIBE".equalsIgnoreCase(command) && loginStatus >2){
			
			return new HttpSubscribe(params,loginStatus >3);
		}
		else if("UNSUBSCRIBE".equalsIgnoreCase(command) && loginStatus >2){
			return new HttpUnSubscribe(params,loginStatus >3);
		}
		else if("DIGEST".equalsIgnoreCase(command) && loginStatus >3){
			return new cmdDigest(params,loginStatus >4);
		}
		else {
			throw new IllegalArgumentException();
		}
		
	}
}
