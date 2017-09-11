package unifi.inf.rc.DanieleBisignano;

import java.util.Set;

import unifi.inf.rc.DanieleBisignano.HttpProtocol;

abstract public class factoryHttpCommand {

	public static HttpProtocol getHtmlProtocol(String param, int loginStatus) {
		// param = param.trim();
		String params[] = param.split(" ", 2);
		String command = params[0];
		// for(int i=1;i<params.length;i++){
		// if(params[i].equals("")){
		// params[i] = null;
		// }
		// }
		try {
			// params[params.length-1] = params[params.length-1].trim();
			System.out.println(command);
			params = params[1].split(" ");

		} catch (IndexOutOfBoundsException e) {
			params = null;
		}
		if (command.equals("USER") && loginStatus == 0 /* || loginStatus >1) */ && !params[0].equals("")) { // se
			// sessione.)
			return new HttpUser(params);
		} else if (command.equals("PASS") && loginStatus == 1 && params.length == 1) {
			return new HttpPass(params);
		} else if (command.equals("AVAILABLE") && loginStatus > 1 && params == null) {
			return new HttpAvailable();
		} else if (command.equals("MOVE") && loginStatus > 12 && params.length == 2) {
			return new HttpMove(params);
		} else if (command.equals("RANKING") && loginStatus > 1 && params == null) {
			return new HttpRanking();
		}else if (command.equals("SIGNUP") && loginStatus == 0 && params.length == 2) {
			return new HttpSignup(params);
		}else if(command.equals("EXIT")){
			return new HttpExit();
		}
		else {
			throw new IllegalArgumentException();
		}

	}
}
