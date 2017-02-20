package it.bisignano.mychatserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.expectNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;
//@RunWith(PowerMockRunner.class)
@PrepareForTest({ MyChatServer.class })
public class MyChatServerTest {
	@Rule
	public PowerMockRule rule = new PowerMockRule();
	@Rule
	public TestName name = new TestName();
	@Rule
	public Timeout globalTimeout = Timeout.seconds(150);
	private static boolean setUpIsDone = false;
	private String address;
	private int port;
	private Map<String, String> dictionary;
	private String msg;
	private StringBuilder answare;
	private Logger LOGGER = LogManager.getLogger(MyChatTest.class);
	@Mock
	private Pair p1;
	@Mock
	private SubscribedHandler sh;
	@Mock Message m1;
	@Mock Message m2;
	@InjectMocks
	private MyChatServer myServer;

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
		PowerMockito.whenNew(SubscribedHandler.class).withArguments(m1,0,MyChatServer.subRegister,MyChatServer.digestReg).thenReturn(sh);
		PowerMockito.whenNew(SubscribedHandler.class).withArguments(m2,1,MyChatServer.subRegister,MyChatServer.digestReg).thenReturn(sh);
		
	}

	@Test
	public void testContructor() {
		MyChatServer testConstructor = new MyChatServer(dictionary, address, port);
		assertEquals(dictionary, testConstructor.getDictionary());
		assertEquals(address, testConstructor.getAddress());
		assertEquals(port, testConstructor.getPort());
	}

	// ########################### inizio test addtopic ###########
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

	// ########################### fine test addTopic ###########

	// ########################### inizio test addMessage ###########
	@Test
	public void testAddMessageName() throws Exception {
		System.out.println("ciao");
		MyChatServer.addMessage(m1);
		assertEquals(m1, MyChatServer.getMessageList().get(0));
	}

	@Test
	public void testAddMessageId() throws Exception {
		int idMessage = MyChatServer.addMessage(m1);
		assertEquals(0, idMessage);
	}

	@Test
	public void testAddMoreMessage() throws Exception {
		List<Message> messageList = new ArrayList<Message>();
		MyChatServer.addMessage(m1);
		MyChatServer.addMessage(m2);
		messageList.add(m1);
		messageList.add(m2);
		assertEquals(messageList, MyChatServer.getMessageList());
	}

//	// ########################### fine test addMessage ###########

	// ########################### inizio test checkTopciError ###########
	@Test
	public void testCheckTopicErrorEmptyList() {
		String[] topicList = { "0" };
		assertEquals(true, MyChatServer.checkTopicError(topicList));
	}

	@Test
	public void testCheckTopicErrorEmptyTopicList() {
		String[] topicList = null;
		assertEquals(true, MyChatServer.checkTopicError(topicList));
	}

	@Test
	public void testCheckTopicNotError() {
		String[] topicList = { "0" };
		MyChatServer.addTopic("ciao");
		assertEquals(false, MyChatServer.checkTopicError(topicList));
	}

	@Test
	public void testCheckTopicErrorNotEmptyList() {
		String[] topicList = { "3" };
		MyChatServer.addTopic("ciao");
		MyChatServer.addTopic("miao");
		assertEquals(true, MyChatServer.checkTopicError(topicList));
	}

	// ########################### fine test checkTopciError ###########

	// ########################### inizio testo checkMessageError ###########
	@Test
	public void testCheckMessageErrorEmptyList() {
		String[] messageList = { "0" };
		assertEquals(true, MyChatServer.checkMessageError(messageList));
	}

	@Test
	public void testCheckMessageErrorEmptyTopicList() {
		String[] messageList = null;
		assertEquals(true, MyChatServer.checkMessageError(messageList));
	}

	@Test
	public void testCheckMessageNotError() {
		String[] messageList = { "0" };
		MyChatServer.addMessage(m1);
		assertEquals(false, MyChatServer.checkMessageError(messageList));
	}

	@Test // (expected = IndexOutOfBoundsException.class)
	public void testCheckMessageErrorNotEmptyList() {
		String[] messageList = { "3" };
		MyChatServer.addMessage(m1);
		MyChatServer.addMessage(m2);
		assertEquals(true, MyChatServer.checkMessageError(messageList));
	}
	// ########################### fine test checkmessagelist ###############à

	// ########################### inizio test addRecord ###########
	@Test
	public void testAddRecord() throws Exception {
		String user = "dani";
		Map<String, Pair<String, Integer>> register = new HashMap<String, Pair<String, Integer>>();
		MyChatServer.register.put(user, p1);
		PowerMockito.whenNew(Pair.class).withArguments("127.0.0.1",52).thenReturn(p1);
		assertEquals(false, MyChatServer.addRecord("127.0.0.1", 52, "dani"));
	}

	@After
	public void tearDown() throws Exception {
		msg = msg.replaceAll("\r\n", " ");
		LOGGER.info(msg);
		MyChatServer.topicList = new ArrayList<String>();
		MyChatServer.messageList = new ArrayList<Message>();
		MyChatServer.register = new HashMap<String, Pair<String, Integer>>(200);
		MyChatServer.subRegister = new HashMap<Integer, TreeSet<String>>(200);
	}

}
