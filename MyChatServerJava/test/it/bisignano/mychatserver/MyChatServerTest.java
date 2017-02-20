package it.bisignano.mychatserver;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import static org.mockito.Mockito.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;

public class MyChatServerTest {
	@Rule
	public TestName name = new TestName();
	@Rule
	public Timeout globalTimeout = Timeout.seconds(3);
	private static boolean setUpIsDone = false;
	private MyChatServer myServer;
	private ChatClient client1;
	private String address;
	private int port;
	private Map<String, String> dictionary;
	private String msg;
	private StringBuilder answare;
	private Logger LOGGER = LogManager.getLogger(MyChatTest.class);

	public MyChatServerTest() {
		BasicConfigurator.configure();
		this.port = 1035;
		this.address = "127.0.0.1";
		this.dictionary = new HashMap<String, String>(200);
		this.dictionary.put("dani", "bisi");
		this.dictionary.put("giulio", "grima");
		this.dictionary.put("marco", "bura");
		this.dictionary.put("lore", "mari");
		this.answare = new StringBuilder();
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.myServer = new MyChatServer(this.dictionary, this.address, this.port);
		this.myServer.start();
		setUpIsDone = true;
		this.msg = "";
		this.client1 = new ChatClient("127.0.0.1", this.port);
		this.client1.connectServer();
	}

	@Test
	public void testContructor() {
		MyChatServer testConstructor = new MyChatServer(dictionary, address, port);
		assertEquals(dictionary, testConstructor.getDictionary());
		assertEquals(address, testConstructor.getAddress());
		assertEquals(port, testConstructor.getPort());
	}

	@Test
	public void testAddTopicName() {
		MyChatServer.addTopic("ciao");
		assertEquals("ciao", (MyChatServer.getTopicList()).get(0));
	}

	@Test
	public void testAddTopicId() {
		int idTopic = MyChatServer.addTopic("ciao");
		assertEquals(0, idTopic);
	}

	@Test
	public void testAddMoreTopic() {
		List<String> topicList = new ArrayList<String>();
		topicList.add("ciao");
		topicList.add("miao");
		MyChatServer.addTopic("ciao");
		MyChatServer.addTopic("miao");
		assertEquals(topicList, MyChatServer.getTopicList());
	}
	
	@Test
	public void testAddMessageName() {
		Message m1 = mock(Message.class);
		MyChatServer.addMessage(m1);
		assertEquals(m1, MyChatServer.getMessageList().get(0));
	}

	@Test
	public void testAddMessageId() {
		Message m1 = mock(Message.class);
		int idMessage = MyChatServer.addMessage(m1);
		assertEquals(0, idMessage);
	}

	@Test
	public void testAddMoreMessage() {
		List<Message> messageList = new ArrayList<Message>();
		Message m1 = mock(Message.class);
		Message m2 = mock(Message.class);
		MyChatServer.addMessage(m1);
		MyChatServer.addMessage(m2);
		messageList.add(m1);
		messageList.add(m2);
		assertEquals(messageList, MyChatServer.getMessageList());
	}
	
	@After
	public void tearDown() throws Exception {
		msg = msg.replaceAll("\r\n", " ");
		LOGGER.info(msg);
		this.client1.closeSocket();
		MyChatServer.topicList = new ArrayList<String>();
		MyChatServer.messageList = new ArrayList<Message>();
		MyChatServer.register = new HashMap<String, Pair<String, Integer>>(200);
		MyChatServer.subRegister = new HashMap<Integer, TreeSet<String>>(200);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
