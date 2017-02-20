package it.bisignano.mychatserver;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class HttpConv implements IHttpProtocol {

	private Set<Integer> convList;
	private String[] params;

	public HttpConv(String[] params) {
		this.params = params;
		convList = new TreeSet();
	}

	//@Override
	public String execute(ClientHandler clientHandler) {
		StringBuilder sb = new StringBuilder();
		sb.append("MESSAGES\r\n");
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
				sb.append(integer.toString());
				sb.append(" ");
				sb.append(msg.getUserName());
				sb.append(" ");
				sb.append(msg.listToString());
				sb.append("\r\n");
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}

	private TreeSet<Integer> dig(int startId) {

		TreeSet<Integer> childList = new TreeSet();
		List<Integer> childListP;
		childListP = MyChatServer.messageList.get(startId).getChildList();
		if (childListP != null) {
			childList.addAll(childListP);
		} else {
			return  new TreeSet();
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
