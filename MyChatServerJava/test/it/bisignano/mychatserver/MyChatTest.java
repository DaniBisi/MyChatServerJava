package it.bisignano.mychatserver;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;

import it.bisignano.mychatserver.ChatClient;
import it.bisignano.mychatserver.Message;
import it.bisignano.mychatserver.MyChatServer;
import it.bisignano.mychatserver.Pair;

/**
 * Unit test for simple App.
 */

public class MyChatTest {
	@Rule
	public TestName name = new TestName();
	@Rule
	public Timeout globalTimeout = Timeout.seconds(150);
	private static boolean setUpIsDone = false;
	private MyChatServer myServer;
	private ChatClient client1;
	private String address;
	private int port;
	private Map<String, String> Dictionary;
	private String msg;
	private StringBuilder answare;
	private Logger LOGGER = LogManager.getLogger(MyChatTest.class);
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public MyChatTest() {
		BasicConfigurator.configure();
		this.port = 1035;
		this.address = "127.0.0.1";
		this.Dictionary = new HashMap<String, String>(200);
		this.Dictionary.put("dani", "bisi");
		this.Dictionary.put("giulio", "grima");
		this.Dictionary.put("marco", "bura");
		this.Dictionary.put("lore", "mari");
		this.answare = new StringBuilder();
	}

	@Before
	public void setUp() {
		LOGGER.info(name.getMethodName());
		
		this.myServer = new MyChatServer(this.Dictionary, this.address, this.port);
		this.myServer.start();
		setUpIsDone = true;
		this.msg = "";
		this.client1 = new ChatClient("127.0.0.1", this.port);
		this.client1.connectServer();
	}

	@Test
	public void testMessageIllegalArgument() {
		msg = "GET 1 2\r\n";
		getAnswareFromServer(msg);
		assertEquals("KO\r\n", answare.toString());
	}

	@Test
	public void testIllegalMessage() {
		msg = "UNKNOWNCOMMAND\r\n";
		getAnswareFromServer(msg);
		assertEquals("KO\r\n", answare.toString());
	}

	@Test
	public void testNullMessage() {
		msg = "\r\n";
		getAnswareFromServer(msg);
		assertEquals("KO\r\n", answare.toString());
	}

	@Test
	public void testUser() {
		msg = "USER dani\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\n", answare.toString());
	}

	@Test
	public void testUserUnCorrect() {
		msg = "USER lolo\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\n", answare.toString());
	}

	@Test
	public void testUserUnCorrectPassword() {
		msg = "USER lolo\r\nPASS MIAO\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testEmptyUser() {
		msg = "USER miao bao\r\n";
		getAnswareFromServer(msg);
		assertEquals("KO\r\n", answare.toString());
	}

	@Test
	public void testUserPassword() {
		msg = "USER dani\r\nPASS bisi\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\n", answare.toString());
	}

	@Test
	public void testUserWrongPassword() {
		msg = "USER dani\r\nPASS CIAO\r\nPASS bisi\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nKO\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testUserEmptyPassword() {
		msg = "USER dani\r\nPASS \r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nKO\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testUserWrongArgumentNumberPassword() {
		msg = "USER dani\r\nPASS 2 3\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testWrongUserWrongArgomentNumberPassword() {
		msg = "USER pippo\r\nPASS 2 3\r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nKO\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testUserUncorrect() {
		msg = "USER danielino\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\n", answare.toString());
	}

	@Test
	public void testUserPasswordNewTopics() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nTOPICS\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nTOPIC_LIST\r\n0 ciao\r\n\r\n", answare.toString());
	}

	@Test
	public void testUserPasswordNewTopicsWrongTOPICS() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nTOPICS 1\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testUserPasswordNewTopicsSubscribed() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0\r\nTOPICS\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nTOPIC_LIST\r\n*0 ciao\r\n\r\n", answare.toString());
	}

	@Test
	public void testUserPasswordNewTopicsSubscribedNotRegister() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nSUBSCRIBE 0\r\nTOPICS\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nKO\r\nTOPIC_LIST\r\n0 ciao\r\n\r\n", answare.toString());
	}

	@Test
	public void testUserPasswordNewTopicsNotRegisterUNSubscribed() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nUNSUBSCRIBE 0\r\nTOPICS\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nKO\r\nTOPIC_LIST\r\n0 ciao\r\n\r\n", answare.toString());
	}

	@Test
	public void testUserPasswordNewTopicMessage() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nMESSAGE 0\r\nciao messaggio di prova\r\n.\r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 0\r\n", answare.toString());
	}

	@Test
	public void testMoreTopicMessageGet() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 0\r\n";
		getAnswareFromServer(msg);
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGE 0\r\nUSER dani\r\nTOPICS 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n",
				answare.toString());
	}

	@Test
	public void testCONV() {
		msg = "USER dani\r\nPASS bisi\r\nNEW CIAO\r\nNEW ciao11\r\nMESSAGE 0 1\r\nhello first msg\r\n.\r\n\r\nMESSAGE 0\r\nhello second msg\r\n.\r\n\r\nLIST 0 1 6\r\nREPLY 0\r\nhello second msg\r\n.\r\n\r\nREPLY 2\r\nhello second msg\r\n.\r\n\r\nCONV 2\r\n";
		getAnswareFromServer(msg);
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\nOK 2\r\nOK 3\r\nMESSAGES\r\n0 dani 0 1\r\n2 dani 0 1\r\n3 dani 0 1\r\n\r\n",
				answare.toString());
	}

	@Test
	public void testMoreTopicMessageWrongGet() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nGET 7\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testMoreTopicMessage() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW Miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\n", answare.toString());
	}

	@Test
	public void testUserPasswordNewTopicMessageWrongMessage() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nMESSAGE 0\r\nciao messaggio di prova\r\n.\r\n\r\nMESSAGE 2\r\nciao messaggio di prova\r\n.\r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 0\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testSubscribe() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0\r\nSUBSCRIBE 0\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nOK\r\n", answare.toString());
	}

	@Test
	public void testUnSubscribe() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW ciao2\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0 1\r\nUNSUBSCRIBE 0 1\r\nSUBSCRIBE 1\r\nUNSUBSCRIBE 0\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK\r\nOK\r\nOK\r\nOK\r\nOK\r\n", answare.toString());
	}

	@Test
	public void testListOneMessageTopic() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 0 0\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGES\r\n0 dani 0 1\r\n\r\n", answare.toString());
	}

	@Test
	public void testListNoArg() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testListOnTopicOneMessageTopic() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 0\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nMESSAGES\r\n0 dani 0 1\r\n\r\n", answare.toString());
	}

	@Test
	public void testListOneMessageWrongtopic() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 2\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testUserPasswordWrongNew() {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao 2\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testRegister() {
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK\r\n", answare.toString());
	}

	@Test
	public void testRegisterNull() {
		msg = "USER dani\r\nPASS bisi\r\nREGISTER\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testRegisterWrong() {
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 1 \r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testRegisterSameAddress() {
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\nREGISTER 127.0.0.1 8245\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testRegisterUnregister() {
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\nUNREGISTER\r\nUNREGISTER\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK\r\nOK\r\nKO\r\n", answare.toString());
	}

	@Test
	public void testRegisterWrongUnregister() {
		msg = "USER dani\r\nPASS bisi\r\nREGISTER 127.0.0.1 8245\r\nUNREGISTER 2\r\nUNREGISTER\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK\r\nKO\r\nOK\r\n", answare.toString());
	}

	@Test
	public void testRegisterSUBSCRIBEUnregister() {
		// if (!this.client1.connectServer())
		// assertFalse(true);
		msg = "USER dani\r\nPASS bisi\r\nNEW CIAO\r\nREGISTER 127.0.0.1 8245\r\nSUBSCRIBE 0\r\nUNREGISTER\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nOK\r\n", answare.toString());
	}

	@Test
	public void testDigest() {
		//this.client1.connectServer();
		msg = "USER dani\r\nPASS bisi\r\nNEW CIAO\r\nREGISTER 127.0.0.1 4127\r\nSUBSCRIBE 0\r\nDIGEST 2\r\nMESSAGE 0\r\nprimo messaggio\r\n.\r\n\r\nMESSAGE 0\r\nsecondo messaggio\r\n.\r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nOK\r\nOK 0\r\nOK 1\r\n", answare.toString());
	}

	@Test
	public void testDigestWithoutSubscribe() {
		//this.client1.connectServer();
		msg = "USER dani\r\nPASS bisi\r\nNEW CIAO\r\nREGISTER 127.0.0.1 4127\r\nDIGEST 2\r\nMESSAGE 0\r\nprimo messaggio\r\n.\r\n\r\nMESSAGE 0\r\nsecondo messaggio\r\n.\r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK\r\nKO\r\nOK 0\r\nOK 1\r\n", answare.toString());
	}

	@Test
	public void testRegisterNotLoggedIn() {
		msg = "REGISTER 127.0.0.1 8245\r\n";
		getAnswareFromServer(msg);
		assertEquals("KO\r\n", answare.toString());
	}

	@Test // (expected=IllegalArgumentException.class)
	public void testGetMessage() {
		msg = "GET 1\r\n";
		getAnswareFromServer(msg);
		assertEquals("KO\r\n", answare.toString());
	}

	@Test
	public void testReply() throws InterruptedException {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nREPLY 0\r\nRISPOSTA\r\n.\r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\n", answare.toString());
	}

	@Test
	public void testWrongReply() throws InterruptedException {
		msg = "USER dani\r\nPASS bisi\r\nNEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nREPLY 32\r\nRISPOSTA\r\n.\r\n\r\n";
		getAnswareFromServer(msg);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nKO\r\n", answare.toString());
	}

	// #################################### TEST STATIC FUNCTION
	// ###########################
	@Test
	public void testunRegister() {
		MyChatServer.register = new HashMap(200);
		boolean c = MyChatServer.unRegister("lollo");
		assertEquals(false, c);
	}

	@Test
	public void testcheckTopicSubscription() {
		TreeSet<String> entry = new TreeSet<String>();
		MyChatServer.subRegister.put(0, entry);
		entry.add("ciao");
		boolean c = MyChatServer.checkTopicSubscription(0, "lollo");
		assertEquals(false, c);
	}

	@Test
	public void testcheckMessageError() {
		String param[] = { "ciao" };
		boolean c = MyChatServer.checkMessageError(param);
		assertEquals(true, c);
	}

	@Test
	public void testMessageAfterSubscribe() {
		MyChatServer.addTopic("ciao");
		MyChatServer.addTopic("ciao1");
		MyChatServer.addTopic("ciao2");
		MyChatServer.addRecord("127.0.0.1", 4127, "danielito");
		String params[] = { "1", "0" };
		MyChatServer.addSubscription(params, "danielito");
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(0);
		Message m = new Message("ciao message", a, "daniele");
		MyChatServer.addMessage(m);

	}

	// ############################# fine test variabili statiche
	// ##################

	// ########################## TEST COMANDI NON LOGGATI
	// #######################

	@Test // (expected=IllegalArgumentException.class)
	public void testGetMessageBatch() {
		msg = "GET 1\r\nGET 1 2\r\n";
		getAnswareFromServer(msg);
		assertEquals("KO\r\nKO\r\n", answare.toString());
	}

	// ############################# INIZIO TEST COMPONENT
	// ######################
	@Test
	public void testMessageHasTopic() {
		ArrayList<Integer> topicListP = new ArrayList<Integer>();
		topicListP.add(1);
		topicListP.add(2);
		Message a = new Message("lol non so", topicListP, "Dani");
		String param[] = { "0" };
		assertEquals(false, a.hasTopic(param));
	}

	@Test
	public void testPairNotEquals() {
		Pair<Integer, Integer> a = new Pair<Integer, Integer>(2, 3);
		assertEquals(false, a.equals("ciao"));
	}

	@Test
	public void testPairEquals() {
		Pair<Integer, Integer> a = new Pair<Integer, Integer>(2, 3);
		assertEquals(true, a.equals(new Pair<Integer, Integer>(2, 3)));
	}

	@Test
	public void testPairEqualsAnotB() {
		Pair<Integer, Integer> a = new Pair<Integer, Integer>(2, 3);
		assertEquals(false, a.equals(new Pair<Integer, String>(2, "ciao")));
	}

	@Test
	public void testPairEqualsBnotA() {
		Pair<Integer, Integer> a = new Pair<Integer, Integer>(2, 3);
		assertEquals(false, a.equals(new Pair<String, Integer>("ciao", 2)));
	}

	@After
	public void tearDown() {
		msg = msg.replaceAll("\r\n", " ");
		LOGGER.info(msg);
		this.client1.closeSocket();
		MyChatServer.topicList = new ArrayList<String>();
		MyChatServer.messageList = new ArrayList<Message>();
		MyChatServer.register = new HashMap<String, Pair<String, Integer>>(200);
		MyChatServer.subRegister = new HashMap<Integer, TreeSet<String>>(200);

	}

	public void getAnswareFromServer(String questions) {
		String[] singleQuestion = questions.split("\r\n", -1);
		int lenghtSingleQuestion = singleQuestion.length;
		String msg;
		for (int i = 0; i < lenghtSingleQuestion - 1; i++) {
			msg = singleQuestion[i];
			msg = msg + "\r\n";
			if (singleQuestion[i].contains("MESSAGE") || singleQuestion[i].contains("REPLY")) {
				i = i + 1;
				msg = msg + singleQuestion[i];// testo messaggio
				msg = msg + "\r\n";// fine messaggio
				i = i + 1;
				msg = msg + singleQuestion[i]; // il .
				i = i + 1;
				msg = msg + "\r\n";
				msg = msg + "\r\n";
			}
			client1.sendMsg(msg);
			answare.append(client1.receiveMsg());
		}

	}

}
