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
		this.port = 80;
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

	@After
	public void tearDown() {
		this.client1.closeSocket();
		this.myServer.stop();
	}

}
