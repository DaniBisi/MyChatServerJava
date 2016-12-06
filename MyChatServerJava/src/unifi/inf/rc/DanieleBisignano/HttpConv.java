package unifi.inf.rc.DanieleBisignano;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class HttpConv implements HttpProtocol {

	Set<Integer> convList = new TreeSet<Integer>();
	private String[] params;

	public HttpConv(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}

	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (params.length == 1 && !MyChatServer.checkMessageError(params)) {
			int startId = Integer.parseInt(params[0]);
			while(startId!=-1){//id di tutti i padri salvato.
				convList.add(startId);
				startId = MyChatServer.MessageList.get(startId).getFather();
			}
			
			
		}
		return null;
	}
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return null;
	}
*/
}
