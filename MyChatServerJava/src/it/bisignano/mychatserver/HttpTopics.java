package it.bisignano.mychatserver;

public class HttpTopics implements IHttpProtocol {
	
	@Override
	public String execute(ClientHandler clientHandler) {
		StringBuilder sb = new StringBuilder();
		sb.append("TOPIC_LIST\r\n");

		int i = 0;
		for (String topicName : MyChatServer.topicList) {
			if(MyChatServer.checkTopicSubscription(clientHandler.getUserName(), i)){
				sb.append("*");
			}
			sb.append(i);
			sb.append(" ");
			sb.append(topicName);
			sb.append("\r\n") ;
			i+=1;
		}
		sb.append("\r\n");
		return sb.toString();
	}

}
