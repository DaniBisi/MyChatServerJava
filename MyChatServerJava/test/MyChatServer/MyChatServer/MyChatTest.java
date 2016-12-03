package MyChatServer.MyChatServer;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class MyChatTest {
	private MyChatServer myServer;
	private ChatClient client1;
	private String address;
	private int port;
	private Map<String, String> Dictionary;
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public MyChatTest() {
		this.port = 1025;
		this.address = "127.0.0.1";
		this.Dictionary =  new HashMap<String, String>(200);
			this.Dictionary.put("dani", "bisi");
			this.Dictionary.put("giulio", "grima");
			this.Dictionary.put("marco", "bura");
			this.Dictionary.put("lore", "mari");	
	}

	@Before
	public void setUp() {
		this.myServer = new MyChatServer(this.address, this.port,this.Dictionary);
		this.myServer.start();
		this.client1 = new ChatClient("127.0.0.1", this.port);
	}

	@Test
	public void testServerConstructor() throws InterruptedException {
		myServer = new MyChatServer(this.address, this.port,this.Dictionary);
		assertEquals(false, myServer == null);
	}
	
	@Test
	public void testClientConstructor() throws InterruptedException {
		this.client1 = new ChatClient("127.0.0.1", this.port);
		assertEquals(false, client1 == null);
	}

	@Test
	public void testComunicationClientServer() {
		assertEquals(true, this.client1.connectServer());
	}

	@Test
	public void testMessageIllegalArgument() {
		this.client1.connectServer();
		String msg = "GET 1 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}

	@Test
	public void testIllegalMessage() {
		this.client1.connectServer();
		String msg = "UNKNOWNMESSAGE\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}

	@Test
	public void testUserCorrect() {
		this.client1.connectServer();
		String msg = "USER dani\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\n", msg);
	}
	
	@Test
	public void testUserPassword() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\n", msg);
	}

	@Test
	public void testUserUncorrect() {
		this.client1.connectServer();
		String msg = "USER danielino\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}


	@Test
	public void testUserPasswordNew() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\n", msg);
	}
	

	@Test
	public void testUserPasswordNewTopicMessage() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nMESSAGE 0\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 0\r\n", msg);
	}
	@Test
	public void testMoreTopicMessageGet() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGE 0\r\nUSER dani\r\nTOPICS 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n", msg);
	}

	@Test
	public void testMoreTopicMessageWrongGet() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 7\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", msg);
	}
	@Test
	public void testMoreTopicMessage() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\n", msg);
	}
	@Test
	public void testMoreTopicGetMessage() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\n", msg);
	}
	@Test
	public void testUserPasswordNewTopicMessageWrongMessage() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nMESSAGE 0\r\nciao messaggio di prova\r\n.\r\n\r\nMESSAGE 2\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 0\r\nKO\r\n", msg);
	}
	@Test
	public void testListOneMessageTopic() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 0 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGES\r\n0 dani 0 1\r\n", msg);
	}

	@Test
	public void testListOneMessageWrongtopic() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", msg);
	}
	@Test
	public void testUserPasswordWrongNew() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nKO\r\n", msg);
	}

	@Test
	public void testRegister() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\n", msg);
	}
	@Test // (expected=IllegalArgumentException.class)
	public void testGetMessage() {
		this.client1.connectServer();
		String msg = "GET 1\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK 1\r\n", msg);
	}

	@Test // (expected=IllegalArgumentException.class)
	public void testGetMessageBatch() {
		this.client1.connectServer();
		String msg = "GET 1\r\nGET 1 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK 1\r\nKO\r\n", msg);
	}
	@Test
	public void testListReply() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nREPLY 0\r\nRISPOSTA\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		msg = msg+ client1.receiveMsg();
		System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\n", msg);
	}

	@After
	public void tearDown() {
		this.client1.closeSocket();
		this.myServer.stop();
	}

}
