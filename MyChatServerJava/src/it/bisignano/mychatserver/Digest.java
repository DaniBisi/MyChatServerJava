package it.bisignano.mychatserver;

import java.util.ArrayList;

public class Digest {

	private int k;
	private ArrayList<Integer> messageQueque;

	public Digest(int k) {
		this.k = k;
		this.messageQueque = new ArrayList<>();
	}
	
	public void setK(int k){
		this.k = k;
	}
	public int getK(){
		return this.k;
	}
	public void addMessage(int idMessage){
		this.messageQueque.add(idMessage);
	}
	public int getListSize(){
		return messageQueque.size();
	}
	
	public boolean timeToSend(){
		return messageQueque.size() >= k;
	}

	public ArrayList<Integer> getList() {
		return messageQueque;
	}

	public void clearList() {
		this.messageQueque.clear();
	}

}
