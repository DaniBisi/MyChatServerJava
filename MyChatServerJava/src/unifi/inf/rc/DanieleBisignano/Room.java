package unifi.inf.rc.DanieleBisignano;

import java.lang.reflect.Array;

public class Room {
	private String user1;
	private String user2;
	private int[][] chessBoard ;
	public Room(String u1) {
		this.user1 = u1;
		this.user2 = null;
		this.chessBoard = new int [3][3];
	}

	public boolean isFull() {
		return user2 != null;
	}

}
