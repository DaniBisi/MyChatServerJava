package MyChatServer.MyChatServer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple App.
 */

public class MyChatTest {
	private static boolean setUpIsDone = false;
	private MyChatServer myServer;
	private ChatClient client1;
	private String address;
	private static int port;
	private Map<String, String> Dictionary;

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public MyChatTest() {
		this.port = 1033;
		this.address = "127.0.0.1";
		this.Dictionary = new HashMap<String, String>(200);
		this.Dictionary.put("dani", "bisi");
		this.Dictionary.put("giulio", "grima");
		this.Dictionary.put("marco", "bura");
		this.Dictionary.put("lore", "mari");
	}

	@Before
	public void setUp() {
		if(!setUpIsDone){
		this.myServer = new MyChatServer( this.Dictionary,this.address, this.port);
		this.myServer.start();
		setUpIsDone = true;
		}
		this.client1 = new ChatClient("127.0.0.1", this.port);
	}

	@Ignore
	public void testServerConstructor() throws InterruptedException {
		myServer = new MyChatServer(this.Dictionary,this.address, this.port) ;
		assertEquals(false, myServer == null);
	}

	@Ignore
	public void testClientConstructor() throws InterruptedException {
		this.client1 = new ChatClient("127.0.0.1", this.port);
		boolean test1 = (client1 == null);
		assertEquals(false, test1);
	}

	@Ignore
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
		msg = msg + client1.receiveMsg();
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

	@Ignore
	public void testUserPasswordNew() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\n", msg);
	}

	@Test
	public void testUserPasswordNewTopicMessage() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nMESSAGE 0\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 0\r\n", msg);
	}

	@Test
	public void testMoreTopicMessageGet() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGE 0\r\nUSER dani\r\nTOPICS 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n",
				msg);
	}

	@Test
	public void testMoreTopicMessageWrongGet() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 7\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", msg);
	}

	@Test
	public void testMoreTopicMessage() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\n", msg);
	}

	@Ignore
	public void testMoreTopicGetMessageTwoUser() {
		this.client1.connectServer();
		ChatClient client2;
		client2 = new ChatClient(this.address, this.port);
		client2.connectServer();
		String msg;
		String msgS = "USER dani\r\nPASS bisi\r\nNEW ciao\r\n";
		String msg2 = "USER dani\r\nPASS bisi\r\nNEW ciao\r\n";
		client1.sendMsg(msgS);
		client2.sendMsg(msg2);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client2.receiveMsg();
		msg = msg + client2.receiveMsg();
		msg = msg + client2.receiveMsg();
		client1.sendMsg("NEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n");
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 2\r\nOK 0\r\n", msg);
	}

	@Test
	public void testUserPasswordNewTopicMessageWrongMessage() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nMESSAGE 0\r\nciao messaggio di prova\r\n.\r\n\r\nMESSAGE 2\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 0\r\nKO\r\n", msg);
	}
	@Test
	public void testSubscribe() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0\r\nSUBSCRIBE 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nOK\r\n", msg);
	}
	@Test
	public void testListOneMessageTopic() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 0 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGES\r\n0 dani 0 1\r\n", msg);
	}

	@Test
	public void testListOneMessageWrongtopic() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", msg);
	}

	@Test
	public void testUserPasswordWrongNew() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nKO\r\n", msg);
	}

	@Test
	public void testRegister() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\n", msg);
	}

	@Test
	public void testRegisterSameAddress() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\nREGISTER 127.0.0.1 8245\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\nKO\r\n", msg);
	}

	@Test
	public void testRegisterUnregister() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\nUNREGISTER\r\nUNREGISTER\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\nOK\r\nKO\r\n", msg);
	}

	@Test
	public void testRegisterNotLoggedIn() {
		this.client1.connectServer();
		String msg = "REGISTER 127.0.0.1 8245\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}

	@Test // (expected=IllegalArgumentException.class)
	public void testGetMessage() {
		this.client1.connectServer();
		String msg = "GET 1\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}

	@Test // (expected=IllegalArgumentException.class)
	public void testGetMessageBatch() {
		this.client1.connectServer();
		String msg = "GET 1\r\nGET 1 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		assertEquals("KO\r\nKO\r\n", msg);
	}

	@Test
	public void testListReply() {
		this.client1.connectServer();
		String msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nREPLY 0\r\nRISPOSTA\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		msg = msg + client1.receiveMsg();
		System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\n", msg);
	}

	@After
	public void tearDown() {
		this.client1.closeSocket();
		MyChatServer.Dictionary = Dictionary;
		MyChatServer.TopicList = new ArrayList<String>();
		MyChatServer.MessageList = new ArrayList<Message>(); 
		MyChatServer.Register = new HashMap<String, Pair<String,Integer>>(200);
		MyChatServer.subRegister = new HashMap<Integer, TreeSet<String>>(200);
		//this.myServer.stop();
	}

}
