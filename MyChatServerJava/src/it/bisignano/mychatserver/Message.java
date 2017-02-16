package it.bisignano.mychatserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Message {
	private String text;

	private List<Integer> topicList;
	private String userName;
	private int father;
	private List<Integer> child;

	public Message(String text, List<Integer> topicList, String userName) {
		super();
		setText(text);
		setTopicList(topicList);
		setUserName(userName);
		setChild(new ArrayList<>());
		setFather(-1);
	}

	public Message(String text, List<Integer> topicList, String userName, int father) {
		super();
		setText(text);
		setTopicList(topicList);
		setUserName(userName);
		setChild(new ArrayList<>());
		setFather(father);
	}

	public synchronized boolean addChild(int idChild) {
		child.add(idChild);
		return true;
	}

	public List<Integer> getChild() {
		return child;
	}

	public void setChild(List<Integer> child) {
		this.child = child;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTopicList(List<Integer> topicList) {
		this.topicList = topicList;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean hasTopic(String[] params) {
		boolean hasTopic = false;
		for (String string : params) {
			int i = Integer.parseInt(string);
			Iterator<Integer> itr = topicList.iterator();
			while (itr.hasNext()) {
				if (i == itr.next()) {
					hasTopic = true;
					break;
				}
			}
			if (hasTopic) {
				break;
			}
		}
		return hasTopic;
	}

	public String getText() {
		return this.text;
	}

	public List<Integer> getTopicList() {
		return this.topicList;
	}

	public String getUserName() {
		return this.userName;
	}

	public String listToString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < this.topicList.size(); i++) {
			result.append(" ");
			result.append(this.topicList.get(i));
		}
		return result.substring(1, result.length());
	}

	public void setFather(int father) {
		this.father = father;
	}

	public int getFather() {
		return father;
	}

	public List<Integer> getChildList() {
		return this.child;
	}
}
