package unifi.inf.rc.DanieleBisignano;

public class HttpTopics implements HttpProtocol {

	private String[] params;

	public HttpTopics(String[] params) {
		this.params = params;
		// TODO Auto-generated constructor stub
	}

	//@Override
	public String execute(clientHandler clientHandler) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String response="TOPIC_LIST\r\n";

		int i = 0;
		for (String topicName : MyChatServer.TopicList) {
			if(MyChatServer.checkTopicSubscription(clientHandler.getUserName(), i))response = response +"*";
			response = response +i+ " "+ topicName +"\r\n" ;
			i+=1;
		}
		response = response +"\r\n";
		return response;
	}
/*
	@Override
	public String visit(clientHandler clientHandler) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
