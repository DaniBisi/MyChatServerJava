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
import org.junit.experimental.theories.Theories.TheoryAnchor;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(UsesNewToInstantiateClass.class)
public class MyChatServerTest {
	@Rule
	public TestName name = new TestName();
	@Rule
	public Timeout globalTimeout = Timeout.seconds(150);
	private static boolean setUpIsDone = false;
	@InjectMocks
	private MyChatServer myServer;
	private ChatClient client1;
	private String address;
	private int port;
	private Map<String, String> dictionary;
	private String msg;
	private StringBuilder answare;
	private Logger LOGGER = LogManager.getLogger(MyChatTest.class);
	@Mock
	private Pair mockPair;
	      
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

	//########################### inizio test addtopic ###########
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

	//########################### fine test addTopic ###########
	
	//########################### inizio test addMessage ###########
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

	//########################### fine test addMessage ###########
	

	//########################### inizio test checkTopciError ###########
	@Test
	public void testCheckTopicErrorEmptyList(){
		String[] topicList = {"0"};
		assertEquals(true, MyChatServer.checkTopicError(topicList));
	}
	@Test
	public void testCheckTopicErrorEmptyTopicList(){
		String[] topicList = null;
		assertEquals(true, MyChatServer.checkTopicError(topicList));
	}
	@Test
	public void testCheckTopicNotError(){
		String[] topicList = {"0"};
		MyChatServer.addTopic("ciao");
		assertEquals(false, MyChatServer.checkTopicError(topicList));
	}
	@Test(expected = IndexOutOfBoundsException.class) 
	public void testCheckTopicErrorNotEmptyList(){
		String[] topicList = {"3"};
		MyChatServer.addTopic("ciao");
		MyChatServer.addTopic("miao");
		when(MyChatServer.topicList.get(3)).thenReturn(null);
		assertEquals(true, MyChatServer.checkTopicError(topicList));
	}

	//########################### fine test checkTopciError ###########
	
	//########################### inizio testo checkMessageError ###########
	@Test
	public void testCheckMessageErrorEmptyList(){
		String[] messageList = {"0"};
		assertEquals(true, MyChatServer.checkMessageError(messageList));
	}
	@Test
	public void testCheckMessageErrorEmptyTopicList(){
		String[] messageList = null;
		assertEquals(true, MyChatServer.checkMessageError(messageList));
	}
	@Test
	public void testCheckMessageNotError(){
		String[] messageList = {"0"};
		Message m1 = mock(Message.class);
		MyChatServer.addMessage(m1);
		assertEquals(false, MyChatServer.checkMessageError(messageList));
	}
	@Test//(expected = IndexOutOfBoundsException.class) 
	public void testCheckMessageErrorNotEmptyList(){
		String[] messageList = {"3"};
		Message m1 = mock(Message.class);
		Message m2 = mock(Message.class);
		MyChatServer.addMessage(m1);
		MyChatServer.addMessage(m2);
		assertEquals(true, MyChatServer.checkMessageError(messageList));
	}
	//########################### fine test checkmessagelist ###############à
	

	//########################### inizio test addRecord ###########
	@Test
	public void testAddRecord() throws Exception{
		//MyChatServer mcs = mock(MyChatServer.class);
		String user ="dani";
		Pair p1 = Mockito.mock(Pair.class);
		expectNew(Pair.class).andReturn(p1);
		Map<String,Pair<String,Integer>> register = new HashMap<String,Pair<String,Integer>>();
		register.put(user, p1);
		MyChatServer.register.put(user, p1);
		//when(MyChatServer.register.entrySet()).thenReturn(register.entrySet());
		//when(p1.equals(p1)).thenReturn(true);
		//PowerMockito.whenNew(Pair.class).withArguments("127.0.0.1",52).thenReturn(p1);
		PowerMockito.whenNew(Pair.class).withAnyArguments();
		assertEquals(false, MyChatServer.addRecord("127.0.0.1", 52, "dani"));
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


}
