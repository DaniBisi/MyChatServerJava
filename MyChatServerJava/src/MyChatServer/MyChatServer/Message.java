package MyChatServer.MyChatServer;

import java.util.ArrayList;
import java.util.Iterator;

public class Message {
	private String Text;
	private ArrayList<Integer> TopicList;
	private String UserName;
	
	public Message(String text, ArrayList<Integer> topicList,String userName) {
		super();
		Text = text;
		TopicList = topicList;
		UserName = userName;
	}

	public boolean hasTopic(String[] params) {
		// TODO Auto-generated method stub
		boolean hasTopic = false;
		for (String string : params) {
			int i = Integer.parseInt(string);
			Iterator<Integer> itr = TopicList.iterator();
			while (itr.hasNext()) {
				if(i == itr.next()){
					hasTopic = true;
					break;
				}
			}
			if(hasTopic)break;
		}
		return hasTopic;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return this.Text;
	}

	public ArrayList<Integer> getTopicList() {
		// TODO Auto-generated method stub
		return this.TopicList;
	}

	public String getUserName() {
		// TODO Auto-generated method stub
		return this.UserName;
	}
	public String listToString() {
	    String result = "";
	    for (int i = 0; i < this.TopicList.size(); i++) {
	        result += " " + this.TopicList.get(i);
	    }
	    result = result.substring(1, result.length());
	    return result;
	}
}
