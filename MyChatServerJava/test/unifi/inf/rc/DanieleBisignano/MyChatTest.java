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

public class MyChatTest {
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

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public MyChatTest() {
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
	}

	@Ignore
	public void testServerConstructor() throws InterruptedException {
		myServer = new MyChatServer(this.Dictionary, this.address, this.port);
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

	@Ignore
	public void testUserPasswordNew() {	
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\n", msg);
	}

	@Test
	public void testUserPasswordNewTopics() {	
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nTOPICS\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nTOPIC_LIST\r\n0 ciao\r\n\r\n", msg);
	}

	@Test
	public void testUserPasswordNewTopicsSubscribed() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0\r\nTOPICS\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		// System.out.println(MyChatServer.subRegister);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nTOPIC_LIST\r\n*0 ciao\r\n\r\n", msg);
	}
	@Test
	public void testUserPasswordNewTopicsSubscribedNotRegister() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nSUBSCRIBE 0\r\nTOPICS\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		// System.out.println(MyChatServer.subRegister);
		assertEquals("OK\r\nOK\r\nOK 0\r\nKO\r\nTOPIC_LIST\r\n0 ciao\r\n\r\n", msg);
	}
	@Test
	public void testUserPasswordNewTopicsNotRegisterUNSubscribed() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nUNSUBSCRIBE 0\r\nTOPICS\r\n";
		client1.sendMsg(msg);
//		try {
//			Thread.sleep(150000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		msg = client1.receiveMsg();
		// System.out.println(MyChatServer.subRegister);
		assertEquals("OK\r\nOK\r\nOK 0\r\nKO\r\nTOPIC_LIST\r\n0 ciao\r\n\r\n", msg);
	}

	@Test
	public void testUserPasswordNewTopicMessage() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nMESSAGE 0\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 0\r\n", msg);
	}

	@Test
	public void testMoreTopicMessageGet() {	
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGE 0\r\nUSER dani\r\nTOPICS 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n",
				msg);
	}
	@Test
	public void testCONV() {	
		msg = "USER dani\r\nPASS bisi\r\nNEW CIAO\r\nNEW ciao11\r\nMESSAGE 0 1\r\nhello first msg\r\n.\r\n\r\nMESSAGE 0\r\nhello second msg\r\n.\r\n\r\nLIST 0 1 6\r\nREPLY 0\r\nhello second msg\r\n.\r\n\r\nREPLY 2\r\nhello second msg\r\n.\r\n\r\nCONV 2\r\n";
		client1.sendMsg(msg);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\nOK 2\r\nOK 3\r\nMESSAGES\r\n0 dani 0 1\r\n2 dani 0 1\r\n3 dani 0 1\r\n\r\n",msg);
	}
	
	@Test
	public void testMoreTopicMessageWrongGet() {	
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 7\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", msg);
	}

	@Test
	public void testMoreTopicMessage() {	
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\n", msg);
	}

	@Ignore
	public void testMoreTopicGetMessageTwoUser() {
		ChatClient client2;
		client2 = new ChatClient(this.address, this.port);
		client2.connectServer();
		String msgS = "USER dani\r\nPASS bisi\r\nNEW ciao\r\n";
		String msg2 = "USER dani\r\nPASS bisi\r\nNEW ciao\r\n";
		client1.sendMsg(msgS);
		client2.sendMsg(msg2);
		msg = client1.receiveMsg();
		msg = msg + client2.receiveMsg();
		msg = msg + client2.receiveMsg();
		msg = msg + client2.receiveMsg();
		client1.sendMsg("NEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n");
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 2\r\nOK 0\r\n", msg);
	}

	@Test
	public void testUserPasswordNewTopicMessageWrongMessage() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nMESSAGE 0\r\nciao messaggio di prova\r\n.\r\n\r\nMESSAGE 2\r\nciao messaggio di prova\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 0\r\nKO\r\n", msg);
	}

	@Test
	public void testSubscribe() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0\r\nSUBSCRIBE 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nOK\r\n", msg);
	}


	@Test
	public void testUnSubscribe() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW ciao2\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0 1\r\nUNSUBSCRIBE 0 1\r\nSUBSCRIBE 1\r\nUNSUBSCRIBE 0\r\n";
		client1.sendMsg(msg);
		
		msg = client1.receiveMsg();
		System.out.println(MyChatServer.subRegister);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK\r\nOK\r\nOK\r\nOK\r\nOK\r\n", msg);
	}

	@Test
	public void testListOneMessageTopic() {		
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 0 0\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		// System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGES\r\n0 dani 0 1\r\n\r\n", msg);
	}
	@Test
	public void testListNoArg() {		
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST\r\n";
		client1.sendMsg(msg);
//		try {
//			Thread.sleep(100000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		msg = client1.receiveMsg();
		// System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", msg);
	}
	@Test
	public void testListOnTopicOneMessageTopic() {	
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 1\r\n";
		client1.sendMsg(msg);
//		try {
//			Thread.sleep(150000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		msg = client1.receiveMsg();
		// System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGES\r\n0 dani 0 1\r\n\r\n", msg);
	}

	@Test
	public void testListOneMessageWrongtopic() {	
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 2\r\n";
		client1.sendMsg(msg);
//		try {
//			Thread.sleep(100000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", msg);
	}

	@Test
	public void testUserPasswordWrongNew() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nKO\r\n", msg);
	}

	@Test
	public void testRegister() {	
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\n", msg);
	}
	@Test
	public void testRegisterNull() {	
		msg = "USER dani\r\nPASS bisi\r\nREGISTER\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nKO\r\n", msg);
	}
	@Test
	public void testRegisterWrong() {	
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 1 \r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nKO\r\n", msg);
	}

	@Test
	public void testRegisterSameAddress() {
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\nREGISTER 127.0.0.1 8245\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\nKO\r\n", msg);
	}

	@Test
	public void testRegisterUnregister() {	
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\nUNREGISTER\r\nUNREGISTER\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\nOK\r\nKO\r\n", msg);
	}
	@Test
	public void testRegisterWrongUnregister() {	
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\nUNREGISTER 2\r\nUNREGISTER\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("OK\r\nOK\r\nOK\r\nKO\r\nOK\r\n", msg);
	}

	@Test
	public void testRegisterSUBSCRIBEUnregister() {
		if (!this.client1.connectServer())
			assertFalse(true);
		msg = "USER dani\r\nPASS bisi\r\nNEW CIAO\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0\r\nUNREGISTER\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		System.out.println(MyChatServer.subRegister);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nOK\r\n", msg);
	}

	@Test
	public void testDigest() {
		this.client1.connectServer();
		msg = "USER dani\r\nPASS bisi\r\nNEW CIAO\r\nREGISTER 127.0.0.1 4127\r\nSUBSCRIBE 0\r\nDIGEST 2\r\nMESSAGE 0\r\nprimo messaggio\r\n.\r\n\r\nMESSAGE 0\r\nsecondo messaggio\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg = client1.receiveMsg();
		System.out.println(MyChatServer.subRegister);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nOK\r\nOK 0\r\nOK 1\r\n", msg);
	}
	@Test
	public void testDigestWithoutSubscribe() {
		this.client1.connectServer();
		msg = "USER dani\r\nPASS bisi\r\nNEW CIAO\r\nREGISTER 127.0.0.1 4127\r\nDIGEST 2\r\nMESSAGE 0\r\nprimo messaggio\r\n.\r\n\r\nMESSAGE 0\r\nsecondo messaggio\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg = client1.receiveMsg();
		System.out.println(MyChatServer.subRegister);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nKO\r\nOK 0\r\nOK 1\r\n", msg);
	}
	@Test
	public void testRegisterNotLoggedIn() {
		msg = "REGISTER 127.0.0.1 8245\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}

	@Test // (expected=IllegalArgumentException.class)
	public void testGetMessage() {
		msg = "GET 1\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\n", msg);
	}


	@Test
	public void testReply() throws InterruptedException {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nREPLY 0\r\nRISPOSTA\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		//Thread.sleep(45000);
		msg = client1.receiveMsg();
		// System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\n", msg);
	}
	@Test
	public void testWrongReply() throws InterruptedException {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nREPLY 32\r\nRISPOSTA\r\n.\r\n\r\n";
		client1.sendMsg(msg);
		//Thread.sleep(45000);
		msg = client1.receiveMsg();
		// System.out.println(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", msg);
	}
	
	//#################################### TEST STATIC FUNCTION ###########################
	@Test
	public void testunRegister(){
		MyChatServer.Register=null;
		boolean c = MyChatServer.unRegister("lollo");
		assertEquals(false, c);
	}
	@Test
	public void testcheckTopicSubscription(){
		TreeSet<String> entry = new TreeSet<String>();
		MyChatServer.subRegister.put(0, entry);
		entry.add("ciao");	
		boolean c = MyChatServer.checkTopicSubscription("lollo",0);
		assertEquals(false, c);
	}

	@Test
	public void testcheckMessageError(){
		String param[] = {"ciao"};
		boolean c = MyChatServer.checkMessageError(param);
		assertEquals(true, c);
	}
	
	@Test
	public void testMessageAfterSubscribe(){
		MyChatServer.addTopic("ciao");
		MyChatServer.addTopic("ciao1");
		MyChatServer.addTopic("ciao2");
		MyChatServer.addRecord("127.0.0.1", 4127, "danielito");
		String params[] = {"1","0"};
		MyChatServer.addSubscription(params, "danielito");
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(0);
		Message m = new Message("ciao message", a, "daniele");
		MyChatServer.addMessage(m);
		
	}
	
	//############################# fine test variabili statiche ##################
	
	//########################## TEST COMANDI NON LOGGATI #######################

	@Test // (expected=IllegalArgumentException.class)
	public void testGetMessageBatch() {
		msg = "GET 1\r\nGET 1 2\r\n";
		client1.sendMsg(msg);
		msg = client1.receiveMsg();
		assertEquals("KO\r\nKO\r\n", msg);
	}

	
	
	//############################# INIZIO TEST COMPONENT ######################
	@Test
	public void testMessageHasTopic(){
		ArrayList<Integer> topicListP = new ArrayList<Integer>();
		topicListP.add(1);
		topicListP.add(2);
		Message a = new Message("lol non so", topicListP, "Dani");
		String param[] = {"0"};
		assertEquals(false,a.hasTopic(param));
	}
	@Test
	public void testPairNotEquals(){
		Pair<Integer, Integer> a = new Pair<Integer, Integer>(2, 3);
		assertEquals(false,a.equals("ciao"));
	}
	@Test
	public void testPairEquals(){
		Pair<Integer, Integer> a = new Pair<Integer, Integer>(2, 3);
		assertEquals(true,a.equals(new Pair<Integer, Integer>(2, 3)));
	}
	@Test
	public void testPairEqualsAnotB(){
		Pair<Integer, Integer> a = new Pair<Integer, Integer>(2, 3);
		assertEquals(false,a.equals(new Pair<Integer, String>(2, "ciao")));
	}
	@Test
	public void testPairEqualsBnotA(){
		Pair<Integer, Integer> a = new Pair<Integer, Integer>(2, 3);
		assertEquals(false,a.equals(new Pair<String, Integer>("ciao", 2)));
	}
	@After
	public void tearDown() {
		msg = msg.replaceAll("\r\n", " ");
		System.out.println(msg);
		this.client1.closeSocket();
		MyChatServer.Dictionary = Dictionary;
		MyChatServer.TopicList = new ArrayList<String>();
		MyChatServer.MessageList = new ArrayList<Message>();
		MyChatServer.Register = new HashMap<String, Pair<String, Integer>>(200);
		MyChatServer.subRegister = new HashMap<Integer, TreeSet<String>>(200);
		// this.myServer.stop();
		
	}
	

}
