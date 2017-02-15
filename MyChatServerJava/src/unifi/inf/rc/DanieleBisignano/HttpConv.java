package unifi.inf.rc.DanieleBisignano;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class HttpConv implements HttpProtocol {

	Set<Integer> convList = new TreeSet<Integer>();
	private String[] params;

	public HttpConv(String[] params) {
		this.params = params;
	}

	@Override
	public String execute(ClientHandler clientHandler) throws IllegalArgumentException {
		
		String response = "MESSAGES\r\n";
		if (params.length == 1 && !MyChatServer.checkMessageError(params)) {
			int startId = Integer.parseInt(params[0]);
			int startIdB = startId;
			while (startId != -1) {// id di tutti i padri salvato.
				convList.add(startId);
				startId = MyChatServer.messageList.get(startId).getFather();
			}
			convList.addAll(this.dig(startIdB));
			for (Integer integer : convList) {
				Message msg = MyChatServer.messageList.get(integer);
				response =response+integer+ " " +msg.getUserName() +" " + msg.listToString()+"\r\n";
				System.out.println(msg);
			}
			response =response+"\r\n";
		}
		return response;
	}

	private TreeSet<Integer> dig(int startId) {

		TreeSet<Integer> childList = new TreeSet<>();
		ArrayList<Integer> childListP = new ArrayList<>();
		childListP = MyChatServer.messageList.get(startId).getChildList();
		if (childListP != null) {
			childList.addAll(childListP);
		} else {
			return  new TreeSet<>();
		}

		for (Integer nextStartId : childListP) {
			childList.addAll(dig(nextStartId));
		}
		return childList;
		// #print MyChatServer.reply
		// #print lista
		// for i in range(len(lista)):
		// listaT = self.__Dig(str(lista[i]))
		// for k in range(len(listaT)):
		// lista.append(listaT[k])
		// return lista

	}
}
