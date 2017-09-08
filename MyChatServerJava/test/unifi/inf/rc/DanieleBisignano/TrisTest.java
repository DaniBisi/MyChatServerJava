package unifi.inf.rc.DanieleBisignano;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import unifi.inf.rc.DanieleBisignano.ChatClient;
import unifi.inf.rc.DanieleBisignano.Message;
import unifi.inf.rc.DanieleBisignano.MyChatServer;
import unifi.inf.rc.DanieleBisignano.Pair;

/**
 * Unit test for simple App.
 */

public class TrisTest {
	@Rule
	public TestName name = new TestName();

	private static boolean setUpIsDone = false;
	private MyChatServer myServer;
	private ChatClient client1;
	private String address;
	private int port;
	private Map<String, String> Dictionary;
	private String msg;
	private Thread thread;

	private ChatClient client2;

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public TrisTest() {
		this.port = 1035;
		this.address = "127.0.0.1";
		this.Dictionary = new HashMap<String, String>(200);
		this.Dictionary.put("dani", "bisi");
		this.Dictionary.put("giulio", "grima");
		this.Dictionary.put("marco", "bura");
		this.Dictionary.put("lore", "mari");
	}
	
	@Before
	public void setUp() {
		System.out.println(name.getMethodName());
		if (!setUpIsDone) {
			this.myServer = new MyChatServer(this.Dictionary, this.address, this.port);
			thread = new Thread(myServer);
			thread.start();
			setUpIsDone = true;
		}
		this.msg ="";
		this.client1 = new ChatClient("127.0.0.1", this.port);
		this.client1.connectServer();
		this.client2 = new ChatClient(this.address, this.port);
		this.client2.connectServer();
	}

	@Test
	public void testSignup(){
		msg = "SIGNUP daniele bisi\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\n", msg);
	}
	@Test
	public void testSignupAndLogin(){
		msg = "SIGNUP daniele bisi\r\nUSER daniele\r\nPASS bisi\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\n", msg);
	}
	@Test
	public void testSignupAlreadyDone(){
		msg = "SIGNUP daniele bisi\r\nSIGNUP daniele ciao\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nKO\r\n", msg);
	}
	@Test
	public void testMessageIllegalArgument() {	
		msg = "GET 1 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}

	@Test
	public void testIllegalMessage() {		
		msg = "UNKNOWNMESSAGE\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}
	@Test
	public void testNullMessage() {		
		msg = "\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}

	@Test
	public void testUser() {		
		msg = "USER dani\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\n", msg);
	}
	@Test
	public void testUserUnCorrect() {		
		msg = "USER lolo\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\n", msg);
	}
	@Test
	public void testUserUnCorrectPassword() {		
		msg = "USER lolo\r\nPASS MIAO\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nKO\r\n", msg);
	}
	@Test
	public void testEmptyUser() {		
		msg = "USER miao bao\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\n", msg);
	}

	@Test
	public void testUserPassword() {	
		msg = "USER dani\r\nPASS bisi\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\n", msg);
	}

	@Test
	public void testUserWrongPassword() {	
		msg = "USER dani\r\nPASS CIAO\r\nPASS bisi\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nKO\r\nKO\r\n", msg);
	}

	@Test
	public void testUserEmptyPassword() {
		msg = "USER dani\r\nPASS \r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nKO\r\nKO\r\n", msg);
	}
	@Test
	public void testUserWrongArgomentNumberPassword() {
		msg = "USER dani\r\nPASS 2 3\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nKO\r\n", msg);
	}
	@Test
	public void testWrongUserWrongArgomentNumberPassword() {
		msg = "USER pippo\r\nPASS 2 3\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nKO\r\nKO\r\n", msg);
	}
	@Test
	public void testUserUncorrect() {
		msg = "USER danielino\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\n", msg);
	}


	
	@Test
	public void testAvailableUserWaiting() {
		
		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = msgS + client1.receiveMsg();
		client2.sendMsg(msg2);
		msg = client2.receiveMsg();
		assertEquals("OK\r\nOK\r\nMATCH FOUND: command available: \"MOVE x,y\" , \"CONCEDE\"\r\n", msg);
	}
	
	@Test
	public void testAvailable() {
		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\n", msg);
	}
	@Test
	public void testMove() {
		
		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = msgS + client1.receiveMsg();
		client2.sendMsg(msg2);
		msg = client2.receiveMsg();
		client2.sendMsg("MOVE 0 2\r\n");
		msg = msg + client2.receiveMsg();
		assertEquals("OK\r\nOK\r\nMATCH FOUND: command available: \"MOVE x,y\" , \"CONCEDE\"\r\nOK\r\n", msg);
	}
	
	@Test
	public void testMatchDraw() {
		
		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = client1.receiveMsg();
		client2.sendMsg(msg2);
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg(); // riceve la notifica di match iniziato
		
		client2.sendMsg("MOVE 0 0\r\n");
		msg = client2.receiveMsg(); //riceve la risposta dal server "OK"
		msgS = client1.receiveMsg(); //riceve la notifica della mossa con la scacchiera
		
		client1.sendMsg("MOVE 0 1\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		client2.sendMsg("MOVE 0 2\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		client1.sendMsg("MOVE 1 1\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		
		client2.sendMsg("MOVE 1 0\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 1 2\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		client2.sendMsg("MOVE 2 1\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 2 0\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		
		client2.sendMsg("MOVE 2 2\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		assertEquals("IT'S A DRAW\r\nIT'S A DRAW\r\n", msg+msgS);
	}
	
	@Test
	public void testMatchWinner() {
		
		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = client1.receiveMsg();
		client2.sendMsg(msg2);
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg(); // riceve la notifica di match iniziato
		
		client2.sendMsg("MOVE 0 0\r\n");
		msg = client2.receiveMsg(); //riceve la risposta dal server "OK"
		msgS = client1.receiveMsg(); //riceve la notifica della mossa con la scacchiera
		
		client1.sendMsg("MOVE 0 1\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		client2.sendMsg("MOVE 0 2\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 1 1\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		
		client2.sendMsg("MOVE 1 0\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 1 2\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		client2.sendMsg("MOVE 2 0\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		assertEquals("YOU WIN\r\nYOU LOSE\r\n", msg+msgS);
	}
	@Test
	public void testMatchWinnerReport() {
		
		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = client1.receiveMsg();
		client2.sendMsg(msg2);
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg(); // riceve la notifica di match iniziato
		
		client2.sendMsg("MOVE 0 0\r\n");
		msg = client2.receiveMsg(); //riceve la risposta dal server "OK"
		msgS = client1.receiveMsg(); //riceve la notifica della mossa con la scacchiera
		
		client1.sendMsg("MOVE 0 1\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		client2.sendMsg("MOVE 0 2\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 1 1\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		
		client2.sendMsg("MOVE 1 0\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 1 2\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		client2.sendMsg("MOVE 2 0\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		

		assertEquals(MyChatServer.getDefeat("dani"), 1);
		assertEquals(MyChatServer.getVictory("giulio"), 1);
		MyChatServer.getRanking();
	}
	
	@Test
	public void testMatchWinnerRanking() {
		
		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = client1.receiveMsg();
		client2.sendMsg(msg2);
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg(); // riceve la notifica di match iniziato
		
		client2.sendMsg("MOVE 0 0\r\n");
		msg = client2.receiveMsg(); //riceve la risposta dal server "OK"
		msgS = client1.receiveMsg(); //riceve la notifica della mossa con la scacchiera
		
		client1.sendMsg("MOVE 0 1\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		client2.sendMsg("MOVE 0 2\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 1 1\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		
		client2.sendMsg("MOVE 1 0\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 1 2\r\n");
		msgS = client1.receiveMsg();
		msg = client2.receiveMsg();
		
		client2.sendMsg("MOVE 2 0\r\n");
		msg = client2.receiveMsg();
		msgS = client1.receiveMsg();
		
		//################# fine prima partita ###################
		msgS = "AVAILABLE\r\n";
		msg2 = "AVAILABLE\r\n";
		client2.sendMsg(msgS);
		msgS = client2.receiveMsg();
		client1.sendMsg(msg2);
		msg = client1.receiveMsg();
		msgS = client2.receiveMsg(); // riceve la notifica di match iniziato
		
		client1.sendMsg("MOVE 0 0\r\n");
		msg = client1.receiveMsg(); //riceve la risposta dal server "OK"
		msgS = client2.receiveMsg(); //riceve la notifica della mossa con la scacchiera
		
		client2.sendMsg("MOVE 0 1\r\n");
		msgS = client2.receiveMsg();
		msg = client1.receiveMsg();
		
		client1.sendMsg("MOVE 0 2\r\n");
		msg = client1.receiveMsg();
		msgS = client2.receiveMsg();
		
		
		client2.sendMsg("MOVE 1 1\r\n");
		msgS = client2.receiveMsg();
		msg = client1.receiveMsg();
		
		
		client1.sendMsg("MOVE 1 0\r\n");
		msg = client1.receiveMsg();
		msgS = client2.receiveMsg();
		
		
		client2.sendMsg("MOVE 1 2\r\n");
		msgS = client2.receiveMsg();
		msg = client1.receiveMsg();
		
		client1.sendMsg("MOVE 2 0\r\n");
		msg = client1.receiveMsg();
		msgS = client2.receiveMsg();

		client1.sendMsg("RANKING\r\n");
		msg = client1.receiveMsg();
		assertEquals(MyChatServer.getRanking(), msg);
	}
	@After
	public void tearDown() {
		msg = msg.replaceAll("\r\n", " ");
		System.out.println(msg);
		this.client1.closeSocket();
		this.client2.closeSocket();
		MyChatServer.Dictionary = Dictionary;
		MyChatServer.TopicList = new ArrayList<String>();
		MyChatServer.MessageList = new ArrayList<Message>();
		MyChatServer.register = new HashMap<String, Pair<String, Integer>>(200);
		MyChatServer.subRegister = new HashMap<Integer, TreeSet<String>>(200);
		MyChatServer.room = null;
		// this.myServer.stop();
		
	}
	

}
