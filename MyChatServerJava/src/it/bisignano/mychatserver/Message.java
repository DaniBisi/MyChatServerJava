package it.bisignano.mychatserver;

import java.util.ArrayList;
import java.util.Iterator;

public class Message {
	private String Text;
	private ArrayList<Integer> TopicList;
	private String UserName;
	private int Father;
	private ArrayList<Integer> Child;
	

	public Message(String text, ArrayList<Integer> topicList,String userName) {
		super();
		Text = text;
		TopicList = topicList;
		UserName = userName;
		Child = new ArrayList<>();
		Father = -1;
	}
	public Message(String text, ArrayList<Integer> topicList,String userName,int father) {
		super();
		Text = text;
		TopicList = topicList;
		UserName = userName;
		setFather(father);
		Child = new ArrayList<>();
	}
	
	public synchronized boolean addChild(int idChild){
		Child.add(idChild);
		return true;
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
		return this.Text;
	}

	public ArrayList<Integer> getTopicList() {
		return this.TopicList;
	}

	public String getUserName() {
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
	public void setFather(int father) {
		Father = father;
	}
	public int getFather() {
		return Father;
	}
	public ArrayList<Integer> getChildList() {
		return this.Child;
	}
}
