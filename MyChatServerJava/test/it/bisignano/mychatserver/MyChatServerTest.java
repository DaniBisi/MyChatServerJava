package it.bisignano.mychatserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;

//@RunWith(PowerMockRunner.class)
@PrepareForTest({ MyChatServer.class })
public class MyChatServerTest {
	@Rule
	public PowerMockRule rule = new PowerMockRule(); // DEVE ESSERE ABILITATO
														// PER LA COVERAGE
	
	@Rule
	public Timeout globalTimeout = Timeout.seconds(150);
	private String address;
	private int port;
	private Map<String, String> dictionary;
	private String msg;
	private StringBuilder answare;
	private Logger LOGGER = LogManager.getLogger(MyChatTest.class);
	private String user = "dani";
	@Mock
	private Pair p1;
	@Mock
	private Pair p2;
	@Mock
	private SubscribedHandler sh;
	@Mock
	Message m1;
	@Mock
	Message m2;
	@Mock
	ServerSocket serverMock;
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
		
		this.msg = "";
		m1 = mock(Message.class);
		m2 = mock(Message.class);
		sh = mock(SubscribedHandler.class);
		p1 = mock(Pair.class);
		p2 = mock(Pair.class);
		serverMock = mock(ServerSocket.class);
		PowerMockito.whenNew(SubscribedHandler.class)
				.withAnyArguments().thenReturn(sh);
		//ServerSocket ssMock = PowerMockito.mock(ServerSocket.class);
		PowerMockito.whenNew(ServerSocket.class).withAnyArguments().thenReturn(serverMock);
		this.myServer = new MyChatServer(this.dictionary, this.address, this.port);
		//this.myServer.start();
	}

	@Test
	public void testContructor() {
		MyChatServer testConstructor = new MyChatServer(dictionary, address, port);
		assertEquals(dictionary, testConstructor.getDictionary());
		assertEquals(address, testConstructor.getAddress());
		assertEquals(port, testConstructor.getPort());
	}

	@Test
	public void GetDictionary() {
		assertEquals(dictionary, MyChatServer.getDictionary());
	}

	@Ignore
	public void testRun() {
		MyChatServer testConstructor = new MyChatServer(dictionary, address, 1036);
		testConstructor.run();
		verify(testConstructor, times(1)).run();
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
	@Test
	public void testAddMoreMessageInteractionWithSHandler() throws Exception {
		List<Message> messageList = new ArrayList<Message>();
		MyChatServer.addMessage(m1);
		MyChatServer.addMessage(m2);
		verify(sh, times(2)).sendMessageToSubscribed();
	}
	// // ########################### fine test addMessage ###########

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
		
		MyChatServer.register.put(user, p2);
		PowerMockito.whenNew(Pair.class).withArguments("127.0.0.1", 52).thenReturn(p1);
		assertEquals(true, MyChatServer.addRecord("127.0.0.1", 52, "dani"));
	}

	@Test
	public void testAddRecordAlreadyInserted() throws Exception {
		
		MyChatServer.register.put(user, p1);
		PowerMockito.whenNew(Pair.class).withArguments("127.0.0.1", 52).thenReturn(p1);
		assertEquals(false, MyChatServer.addRecord("127.0.0.1", 52, "dani"));
	}

	@Test
	public void testAddRecordRegisterEmpty() throws Exception {
		
		PowerMockito.whenNew(Pair.class).withArguments("127.0.0.1", 52).thenReturn(p1);
		assertEquals(true, MyChatServer.addRecord("127.0.0.1", 52, "dani"));
	}

	// ########################### inizio test addRecord ###########

	// ########################### inizio test Unregister ###########

	@Test
	public void testunRegister() {
		
		MyChatServer.register.put(user, p1);
		assertEquals(true, MyChatServer.unRegister(user));
	}

	@Test
	public void testunRegisterNotRegistered() {
		
		assertEquals(false, MyChatServer.unRegister(user));
	}
	// ########################### fine test Unregister ###########

	// ########################### inizio test checkRegisterError ###########

	@Test
	public void testCheckRegisterError() {
		
		MyChatServer.register.put(user, p1);
		assertEquals(true, MyChatServer.checkRegisterError(user));
	}

	@Test
	public void testCheckRegisterErrorNotPresent() {
		
		assertEquals(false, MyChatServer.checkRegisterError(user));
	}
	// ########################### fine test checkRegisterError ###########

	// ########################### inizio testo addSubscription ###########

	@Test
	public void testAddSubScriptionEmptyEntry() {
		
		String[] topicList = { "0" };
		TreeSet<String> entry = new TreeSet<String>();
		entry.add(user);
		MyChatServer.addSubscription(topicList, user);
		assertEquals(entry, MyChatServer.subRegister.get(0));
	}

	@Test
	public void testAddSubScription() {
		
		String[] topicList = { "0" };
		TreeSet<String> entry = new TreeSet<String>();
		entry.add(user);
		MyChatServer.subRegister.put(0, entry);
		MyChatServer.addSubscription(topicList, user);
		assertEquals(entry, MyChatServer.subRegister.get(0));
	}
	// ########################### fine test addSubscription ###########

	// ########################### inizio testo rmSubscription ###########
	@Test
	public void testRmSubScription() {
		
		String[] topicList = { "0" };
		TreeSet<String> entry = new TreeSet<String>();
		entry.add(user);
		MyChatServer.subRegister.put(0, entry);
		entry.remove(user);
		MyChatServer.rmSubScription(topicList, user);
		assertEquals(entry, MyChatServer.subRegister.get(0));
	}

	@Test
	public void testRmSubScriptionMoreThanOne() {

		Map<Integer, TreeSet<String>> subRegisterTest;
		subRegisterTest = new HashMap(200);
		
		String[] topicList = { "0", "1" };
		TreeSet<String> entry = new TreeSet<String>();
		entry.add(user);
		MyChatServer.subRegister.put(0, entry);
		MyChatServer.subRegister.put(1, entry);
		entry.remove(user);
		subRegisterTest.put(0, entry);
		subRegisterTest.put(1, entry);
		MyChatServer.rmSubScription(topicList, user);
		assertEquals(subRegisterTest, MyChatServer.subRegister);
	}
	// ########################### fine test rmSubscription ###########

	// ########################### inizio test checkTopicSubscription
	// ###########
	@Test
	public void testCheckTopicSubscription() {
		
		int idTopic = 0;
		TreeSet<String> entry = new TreeSet<String>();
		entry.add(user);
		MyChatServer.subRegister.put(0, entry);
		assertEquals(true, MyChatServer.checkTopicSubscription(idTopic, user));
	}

	@Test
	public void testCheckTopicSubscriptionEmptySubRegister() {
		
		int idTopic = 0;
		assertEquals(false, MyChatServer.checkTopicSubscription(idTopic, user));
	}

	@Test
	public void testCheckTopicSubscriptionNotEmptyNotSubsribed() {
		String user = "marco";
		int idTopic = 0;
		TreeSet<String> entry = new TreeSet<String>();
		entry.add(user);
		MyChatServer.subRegister.put(0, entry);
		assertEquals(false, MyChatServer.checkTopicSubscription(idTopic, "dani"));
	}
	// ########################### fine test checkTopicSubscription ###########

	// ########################### inizio test closeSocket ###########
	@Test
	public void testCloseSocket() {
		
		assertEquals(true, myServer.closeSocket());
	}

	@Test
	public void testCloseSocketAlreadyClose() throws UnknownHostException, Exception{
		PowerMockito.doThrow(new IOException()).when(serverMock).close();	
		assertEquals(false, myServer.closeSocket());	
	}
	// ########################### fine test closeSocket ###########

	// ########################### inizio test UnsubScribe ###########
	@Test
	public void testUnsubrscribeNotEmptySubsribeRegister() {
		Map<Integer, TreeSet<String>> subRegisterTest;
		subRegisterTest = new HashMap(200);
		
		TreeSet<String> entry = new TreeSet<String>();
		entry.add(user);
		subRegisterTest.put(0, entry);
		subRegisterTest.put(1, entry);
		subRegisterTest.get(0).remove(user);
		subRegisterTest.get(1).remove(user);
		MyChatServer.topicList.add("ciao");
		MyChatServer.topicList.add("Miao");
		MyChatServer.subRegister.put(0, entry);
		MyChatServer.subRegister.put(1, entry);		
		MyChatServer.unSubscribe(user);
		assertEquals(subRegisterTest, MyChatServer.subRegister);
		
	}
	@Test
	public void testUnsubrscribeEmptySubsribeRegister() {
		
		Map<Integer, TreeSet<String>> subRegisterTest;
		subRegisterTest = new HashMap(200);
		MyChatServer.unSubscribe(user);
		assertEquals(subRegisterTest, MyChatServer.subRegister);
		
	}

	// ########################### fine test UnsubScribe ###########
	
	


	// ########################### inizio test SetDigest ###########
	@Test
	public void testSetDigest() throws Exception{
		Digest d1 = mock(Digest.class);
		PowerMockito.whenNew(Digest.class).withAnyArguments().thenReturn(d1);
		//PowerMockito.doNothing().when(d1).setK(2);
		MyChatServer.setDigest(user, 2);
		assertEquals(d1, MyChatServer.digestReg.get(user));
	}
	@Test
	public void testSetDigestUserPresent() throws Exception{
		Digest d1 = mock(Digest.class);
		PowerMockito.whenNew(Digest.class).withAnyArguments().thenReturn(d1);
		//PowerMockito.doNothing().when(d1).setK(2);	
		MyChatServer.digestReg.put(user, d1);
		MyChatServer.setDigest(user, 2);
		verify(d1).setK(2);
	}
	// ########################### fine test SetDigest ###########
	@Test
	public void testGetUserPassword(){
		assertEquals("bisi", MyChatServer.getUserPass(user));
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
