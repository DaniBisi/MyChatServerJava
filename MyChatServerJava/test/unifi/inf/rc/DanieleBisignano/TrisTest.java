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
import unifi.inf.rc.DanieleBisignano.MyTrisServer;

/**
 * Unit test for simple App.
 */

public class TrisTest {
	@Rule
	public TestName name = new TestName();

	private static boolean setUpIsDone = false;
	private MyTrisServer myServer;
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
	public void setUp() throws InterruptedException {
		System.out.println(name.getMethodName());
		if (!setUpIsDone) {
			this.myServer = new MyTrisServer(this.Dictionary, this.address, this.port);
			thread = new Thread(myServer);
			thread.start();
			setUpIsDone = true;
		}
		this.msg = "";
		this.client1 = new ChatClient("127.0.0.1", this.port);
		this.client1.connectServer();
		this.client2 = new ChatClient(this.address, this.port);
		this.client2.connectServer();
	}

	@Test
	public void testExit() {
		msg = "EXIT\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(1);
		assertEquals("GOODBYE\r\n", msg);
	}

	@Test
	public void testSignup() {
		msg = "SIGNUP daniele bisi\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(1);
		assertEquals("OK\r\n", msg);
	}

	@Test
	public void testSignupAndLogin() {
		msg = "SIGNUP lucia lol\r\nUSER lucia\r\nPASS lol\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(3);
		assertEquals("OK\r\nOK\r\nOK\r\n", msg);
	}

	@Test
	public void testSignupAlreadyDone() {
		msg = "SIGNUP chiara boh\r\nSIGNUP chiara ciao\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(2);
		assertEquals("OK\r\nKO\r\n", msg);
	}

	
	@Test
	public void testIllegalMessage() {
		msg = "UNKNOWNMESSAGE\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(1);
		assertEquals("KO\r\n", msg);
	}

	@Test
	public void testNullMessage() {
		msg = "\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(1);
		assertEquals("KO\r\n", msg);
	}

	@Test
	public void testUser() {
		msg = "USER dani\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(1);
		assertEquals("OK\r\n", msg);
	}

	@Test
	public void testUserUnCorrect() {
		msg = "USER lolo\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(1);
		assertEquals("OK\r\n", msg);
	}

	@Test
	public void testUserUnCorrectPassword() {
		msg = "USER lolo\r\nPASS MIAO\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(2);
		assertEquals("OK\r\nKO\r\n", msg);
	}

	@Test
	public void testEmptyUser() {
		msg = "USER miao bao\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(1);
		assertEquals("OK\r\n", msg);
	}

	@Test
	public void testUserPassword() {
		msg = "USER dani\r\nPASS bisi\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(2);
		assertEquals("OK\r\nOK\r\n", msg);
	}

	@Test
	public void testUserWrongPassword() {
		msg = "USER dani\r\nPASS CIAO1\r\nPASS bisi\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(3);
		assertEquals("OK\r\nKO\r\nKO\r\n", msg);
	}

	@Test
	public void testUserEmptyPassword() {
		msg = "USER dani\r\nPASS \r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(3);
		assertEquals("OK\r\nKO\r\nKO\r\n", msg);
	}

	@Test
	public void testUserWrongArgumentNumberPassword() {
		msg = "USER dani\r\nPASS 2 3\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg(2);
		assertEquals("OK\r\nKO\r\n", msg);
	}

	@Test
	public void testAvailableUserWaiting() {

		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = msgS + client1.receiveMsg(3);
		client2.sendMsg(msg2);
		msg = client2.receiveMsg(3);
		msgS = msgS + client1.receiveMsg(1);// riceve la risposta
		assertEquals("OK\r\nOK\r\nMATCH FOUND: command available: \"MOVE x,y\" , \"CONCEDE\"\r\n", msg);
	}

	@Test
	public void testAvailable() {
		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msg = client1.receiveMsg(3);
		client2.sendMsg("USER giulio\r\nPASS grima\r\nAVAILABLE\r\n");// libero
																		// la
																		// stanza
		String msg2 = client2.receiveMsg(3);
		client1.receiveMsg(1);
		assertEquals("OK\r\nOK\r\nOK\r\n", msg);

	}

	@Test
	public void testMove() {

		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = msgS + client1.receiveMsg(3);
		client2.sendMsg(msg2);
		msg = client2.receiveMsg(3);
		client2.sendMsg("MOVE 0 2\r\n");
		msg = msg + client2.receiveMsg(1);
		assertEquals("OK\r\nOK\r\nMATCH FOUND: command available: \"MOVE x,y\" , \"CONCEDE\"\r\nOK\r\n", msg);
	}

	@Test
	public void testMatchDraw() {

		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = client1.receiveMsg(3);
		client2.sendMsg(msg2);
		msg = client2.receiveMsg(3);
		msgS = client1.receiveMsg(1); // riceve la notifica di match iniziato

		client2.sendMsg("MOVE 0 0\r\n");
		msg = client2.receiveMsg(1); // riceve la risposta dal server "OK"
		msgS = client1.receiveMsg(1); // riceve la notifica della mossa con la
										// scacchiera

		client1.sendMsg("MOVE 0 1\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 0 2\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 1\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 1 0\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 2\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 2 1\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 2 0\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 2 2\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		assertEquals("IT'S A DRAW\r\nIT'S A DRAW\r\n", msg + msgS);
	}

	@Test
	public void testMatchWinner() {

		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = client1.receiveMsg(3);
		client2.sendMsg(msg2);
		msg = client2.receiveMsg(3);
		msgS = client1.receiveMsg(1); // riceve la notifica di match iniziato

		client2.sendMsg("MOVE 0 0\r\n");
		msg = client2.receiveMsg(1); // riceve la risposta dal server "OK"
		msgS = client1.receiveMsg(1); // riceve la notifica della mossa con la
										// scacchiera

		client1.sendMsg("MOVE 0 1\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 0 2\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 1\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 1 0\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 2\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 2 0\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		assertEquals("YOU WIN\r\nYOU LOSE\r\n", msg + msgS);
	}

	@Test
	public void testMatchWinnerReport() {

		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = client1.receiveMsg(3);
		client2.sendMsg(msg2);
		msg = client2.receiveMsg(3);
		msgS = client1.receiveMsg(1); // riceve la notifica di match iniziato

		client2.sendMsg("MOVE 0 0\r\n");
		msg = client2.receiveMsg(1); // riceve la risposta dal server "OK"
		msgS = client1.receiveMsg(1); // riceve la notifica della mossa con la
										// scacchiera

		client1.sendMsg("MOVE 0 1\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 0 2\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 1\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 1 0\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 2\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 2 0\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		assertEquals("YOU WIN\r\nYOU LOSE\r\n", msg + msgS);
	}

	@Test
	public void testMatchWinnerRanking() {

		String msgS = "USER dani\r\nPASS bisi\r\nAVAILABLE\r\n";
		String msg2 = "USER giulio\r\nPASS grima\r\nAVAILABLE\r\n";
		client1.sendMsg(msgS);
		msgS = client1.receiveMsg(3);
		client2.sendMsg(msg2);
		msg = client2.receiveMsg(3);
		msgS = client1.receiveMsg(1); // riceve la notifica di match iniziato

		client2.sendMsg("MOVE 0 0\r\n");
		msg = client2.receiveMsg(1); // riceve la risposta dal server "OK"
		msgS = client1.receiveMsg(1); // riceve la notifica della mossa con la
										// scacchiera

		client1.sendMsg("MOVE 0 1\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 0 2\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 1\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 1 0\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 2\r\n");
		msgS = client1.receiveMsg(1);
		msg = client2.receiveMsg(1);

		client2.sendMsg("MOVE 2 0\r\n");
		msg = client2.receiveMsg(1);
		msgS = client1.receiveMsg(1);

		// ################# fine prima partita ###################
		msgS = "AVAILABLE\r\n";
		msg2 = "AVAILABLE\r\n";
		client2.sendMsg(msgS);
		msgS = client2.receiveMsg(1);
		client1.sendMsg(msg2);
		msg = client1.receiveMsg(1);
		msgS = client2.receiveMsg(1); // riceve la notifica di match iniziato

		client1.sendMsg("MOVE 0 0\r\n");
		msg = client1.receiveMsg(1); // riceve la risposta dal server "OK"
		msgS = client2.receiveMsg(1); // riceve la notifica della mossa con la
										// scacchiera

		client2.sendMsg("MOVE 0 1\r\n");
		msgS = client2.receiveMsg(1);
		msg = client1.receiveMsg(1);

		client1.sendMsg("MOVE 0 2\r\n");
		msg = client1.receiveMsg(1);
		msgS = client2.receiveMsg(1);

		client2.sendMsg("MOVE 1 1\r\n");
		msgS = client2.receiveMsg(1);
		msg = client1.receiveMsg(1);

		client1.sendMsg("MOVE 1 0\r\n");
		msg = client1.receiveMsg(1);
		msgS = client2.receiveMsg(1);

		client2.sendMsg("MOVE 1 2\r\n");
		msgS = client2.receiveMsg(1);
		msg = client1.receiveMsg(1);

		client1.sendMsg("MOVE 2 0\r\n");
		msg = client1.receiveMsg(1);
		msgS = client2.receiveMsg(1);

		client1.sendMsg("RANKING\r\n");
		msg = client1.receiveMsg(1);
		assertEquals(true, msg.contains("giulio:") && msg.contains("dani:"));
	}

	@After
	public void tearDown() {
		msg = msg.replaceAll("\r\n", " ");
		this.client1.closeSocket();
		this.client2.closeSocket();
	}

}
