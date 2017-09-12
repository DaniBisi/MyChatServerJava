package unifi.inf.rc.DanieleBisignano;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Observer;

public class Room {
	private ArrayList<clientHandler> observers;
	private char[][] chessBoard;
	private int moves;

	public Room(clientHandler u1) {
		moves = 0;
		observers = new ArrayList<clientHandler>();
		observers.add(u1);
		this.chessBoard = new char[3][3];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				chessBoard[x][y] = '0';
			}
		}
	}

	public boolean isFull() {
		return observers.size() == 2;
	}

	public int move(int x, int y, String user) {
		if (x > 2 || y > 2 || chessBoard[x][y] != '0')
			return -1;
		//FA CACAREEEEEEEEEEEEEEE RIFARE
		moves +=1;
		char symbol = 'y';
		int notActivePlayer = 0;
		if (user.equals(observers.get(0).getUserName())){
			symbol = 'x';
			notActivePlayer = 1;
			
		}	
		chessBoard[x][y] = symbol;
		if(checkWinner(symbol,x,y)){
			notify(notActivePlayer,"YOU LOSE\r\n",2);
			return 1;
			
		}
		else if(moves == 9){
			notify(notActivePlayer,"IT'S A DRAW\r\n",11);
			return 2;
		}
		else{
			notify(notActivePlayer,"IT'S YOUR TURN\n" + "Command available: \"MOVE x,y\"\n"+serializeChessBoard(),13);	
		}
		return 0;

	}

	private void notify(int i, String msg,int loginStatus) {
		observers.get(i).getUpdate(msg,loginStatus);
	}

	private boolean checkWinner(char symbol,int x,int y) {
		boolean result = checkColumn(y, symbol) || checkRow(x, symbol);
		if((x+y)%2 == 0 && !result){
			result = checkDiag(symbol);
			
		}
		return result;
	}
	
	private boolean checkDiag(char symbol) {
		return( (chessBoard[0][0] == symbol && chessBoard[1][1] == symbol && chessBoard[2][2] == symbol ) || (chessBoard[2][0] == symbol && chessBoard[1][1] == symbol && chessBoard[0][2] == symbol ) );
		
	}

	private boolean checkRow(int x, char symbol) {
		return(chessBoard[x][0] == symbol && chessBoard[x][1] == symbol && chessBoard[x][2] == symbol );
			
	}

	private boolean checkColumn(int y,char symbol){
		return(chessBoard[0][y] == symbol && chessBoard[1][y] == symbol && chessBoard[2][y] == symbol );
	
	}

	private String serializeChessBoard() {
		String msg = "";
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				msg = msg + chessBoard[x][y];
			}
			msg = msg + "\n";
		}
		msg = msg +"\r\n";
		return msg;
	}

	public void addUser(clientHandler clientHandler) {
		this.observers.add(clientHandler);
		notify(0,"Match Found! It's opponent turn, wait.\r\n",12);
		
	}
}
