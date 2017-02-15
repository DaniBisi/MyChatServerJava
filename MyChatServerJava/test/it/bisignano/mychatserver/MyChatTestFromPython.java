package it.bisignano.mychatserver;
// ricordare che la class MyChatServer deve estendere Thread

// ricordare che si deve implementare and 
//  @Test 
// public void run(this){
//         this.start1();  
//     @Test 

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import it.bisignano.mychatserver.ChatClient;
import it.bisignano.mychatserver.Message;
import it.bisignano.mychatserver.MyChatServer;
import it.bisignano.mychatserver.Pair;

public class MyChatTestFromPython {

	@Rule
	public TestName name = new TestName();

	private static boolean setUpIsDone = false;
	private MyChatServer myServer;
	private ChatClient client1;
	private String address;
	private int port;
	private Map<String, String> Dictionary;
	private String msg;
	private String data;

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public MyChatTestFromPython() {
		this.port = 1035;
		this.address = "127.0.0.1";
		this.Dictionary = new HashMap<String, String>(200);
		this.Dictionary.put("dani", "bisi");
		this.Dictionary.put("giulio", "grima");
		this.Dictionary.put("marco", "bura");
		this.Dictionary.put("lore", "mari");
		this.Dictionary.put("Dani", "ciao");

	}

	@Before
	public void setUp() {
		System.out.println(name.getMethodName());
		if (!setUpIsDone) {
			this.myServer = new MyChatServer(this.Dictionary, this.address, this.port);
			this.myServer.start();
			setUpIsDone = true;
		}
		this.msg = "";
		this.client1 = new ChatClient("127.0.0.1", this.port);
		this.client1.connectServer();
	}

	@After
	public void tearDown() {
		data = data.replaceAll("\r\n", " ");
		System.out.println(data);
		this.client1.closeSocket();
		// MyChatServer.Dictionary = Dictionary;
		MyChatServer.topicList = new ArrayList<String>();
		MyChatServer.messageList = new ArrayList<Message>();
		MyChatServer.register = new HashMap<String, Pair<String, Integer>>(200);
		MyChatServer.subRegister = new HashMap<Integer, TreeSet<String>>(200);
		// this.myServer.stop();

	}

	@Test
	public void TestUser() {
		msg = "USER Dani" + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(4);

		assertEquals("OK\r\n", data);
	}

	@Test
	public void TestUser2() {
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(4);
		assertEquals("OK\r\nOK\r\n", data);
	}

	@Test
	public void TestUserPassNotOk() {
		msg = "USER Dani" + '\r' + '\n' + "PASS lol" + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(4);
		assertEquals("OK\r\nKO\r\n", data);
	}

	@Test
	public void TestUserPassNotOkMessage() {
		msg = "USER Dani" + '\r' + '\n' + "PASS lol" + '\r' + '\n' + "MESSAGE 1 4 lol" + '\r' + '\n' + "ASDGAKHSDFG"
				+ '\r' + '\n' + '.' + '\r' + '\n' + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(4);
		assertEquals("OK\r\nKO\r\nKO\r\n", data);
	}

	@Test
	public void testLegalCommandNotLogin() {
		msg = "NEW CIAO" + '\r' + '\n' + "PASS ciao" + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(4);
		assertEquals("KO\r\nKO\r\n", data);
	}

	@Test
	public void testNewTopic() {
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(4);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\n", data);
	}

	@Test
	public void testTopicListWrongSyntax() {
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "TOPIC_LIST 1 3\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(4);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nKO\r\n", data);
	}

	@Test
	public void testMessageNoTopic() {
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 2 1" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r'
				+ '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nKO\r\n", data);
	}

	@Test
	public void testMessage() {
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r'
				+ '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\n", data);
	}

	@Test
	public void testPiuInserimenti() {

		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\n", data);
	}

	@Test
	public void testListTopicValidPresent() {

		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1" + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nMESSAGES\r\n0 Dani 0 1\r\n\r\n", data);
	}

	@Test
	public void testListTopicValidPresentMore() {

		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1 0" + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nMESSAGES\r\n0 Dani 0 1\r\n1 Dani 0\r\n\r\n", data);

	}

	@Test
	public void testListTopicNotValidPresentMore() {

		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1 4" + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\n", data);
	}

	@Test
	public void testValidReply() {
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1 6" + '\r' + '\n' + "REPLY 0" + '\r' + '\n' + "hell first responce msg" + '\r' + '\n' + "."
				+ '\r' + '\n' + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\nOK 2\r\n", data);
	}

	@Test
	public void testInValidReply() {
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1 6" + '\r' + '\n' + "REPLY 6" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r'
				+ '\n' + '\r' + '\n';
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\nKO\r\n", data);
	}

	@Test
	public void testConv() {

		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1 6" + '\r' + '\n' + "REPLY 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r'
				+ '\n' + '\r' + '\n' + "REPLY 2" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n'
				+ '\r' + '\n' + "CONV 2\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\nOK 2\r\nOK 3\r\nMESSAGES\r\n0 Dani 0 1\r\n2 Dani 0 1\r\n3 Dani 0 1\r\n\r\n",
				data);
	}

	@Test
	public void testConvBase() {

		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "hello first msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1 6" + '\r' + '\n' + "REPLY 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r'
				+ '\n' + '\r' + '\n' + "REPLY 2" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n'
				+ '\r' + '\n' + "CONV 0\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\nOK 2\r\nOK 3\r\nMESSAGES\r\n0 Dani 0 1\r\n2 Dani 0 1\r\n3 Dani 0 1\r\n\r\n",
				data);
	}

	@Test
	public void testConvUltimo() {

		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "MESSAGGIO 1" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1 6" + '\r' + '\n' + "REPLY 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r'
				+ '\n' + '\r' + '\n' + "REPLY 2" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n'
				+ '\r' + '\n' + "CONV 3\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\nOK 2\r\nOK 3\r\nMESSAGES\r\n0 Dani 0 1\r\n2 Dani 0 1\r\n3 Dani 0 1\r\n\r\n",
				data);
	}

	@Test
	public void testGeneric() {
		msg = "USER Dani\r\nPASS ciao\r\nNEW Titolo0\r\nNEW Titolo1\r\nNEW Titolo2\r\nNEW Titolo3\r\nMESSAGE 0 1 2 3\r\nCiao! Come stai?\r\n.\r\n\r\nMESSAGE 0 1 3\r\nBau!\r\n.\r\n\r\nMESSAGE 0 2 3\r\nMiao!\r\n.\r\n\r\nLIST 0 2\r\nGET 2\r\n";
		this.client1.sendMsg(msg);

		data = this.client1.receiveMsg(1024);
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 2\r\nOK 3\r\nOK 0\r\nOK 1\r\nOK 2\r\nMESSAGES\r\n0 Dani 0 1 2 3\r\n2 Dani 0 2 3\r\n\r\nMESSAGE 2\r\nUSER Dani\r\nTOPICS 0 2 3\r\nMiao!\r\n.\r\n\r\n",
				data);
	}

	@Test
	public void testRegisterBase() {
		msg = "USER Dani\r\nPASS ciao\r\nREGISTER 127.0.0.1 89\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nOK\r\n", data);
	}

	@Test
	public void testRegisterBusy() {
		MyChatServer.register.put("user1", new Pair<String, Integer>("127.0.0.1", 89));
		msg = "USER Dani\r\nPASS ciao\r\nREGISTER 127.0.0.1 89\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nKO\r\n", data);
	}

	@Test
	public void testRegisterBusyAndReplace() {
		MyChatServer.register.put("Dani", new Pair<String, Integer>("127.0.0.1", 89));
		msg = "USER Dani\r\nPASS ciao\r\nREGISTER 127.0.0.1 130\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK\r\n", data);
	}

	@Test
	public void testUnRegisterBase() {
		msg = "USER Dani\r\nPASS ciao\r\nREGISTER 127.0.0.1 89\r\nUNREGISTER\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nOK\r\nOK\r\n", data);
	}

	@Test
	public void testUnRegisterNotRegistered() {

		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+24);

		MyChatServer.register.put("user1", new Pair<String, Integer>("127.0.0.1", 89));
		// this.client1.connect((this.address , this.port+24));
		msg = "USER Dani\r\nPASS ciao\r\nUNREGISTER\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nKO\r\n", data);
		// msg = "USER Dani"+'\r'+'\n'+"PASS ciao"+'\r'+'\n'+"NEW
		// CIAO"+'\r'+'\n'+"NEW ciao11"+'\r'+'\n'+"MESSAGE 0
		// 1"+'\r'+'\n'+"MESSAGGIO 1"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"MESSAGE
		// 0"+'\r'+'\n'+"MESSAGGIO 2"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"LIST 0
		// 1 6"+'\r'+'\n'+"REPLY 0"+'\r'+'\n'+"hello second
		// msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY 0"+'\r'+'\n'+"hello
		// second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
		// 0"+'\r'+'\n'+"hello second
		// msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY 3"+'\r'+'\n'+"hello
		// second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
		// 2"+'\r'+'\n'+"hello second
		// msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"CONV 3\r\n"
	}

	@Test
	public void testUnRegisterWrongSyntaxLegalDelete() {

		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+25);

		// MyChatServer.registerHost["user1"] = ("127.0.0.1" , 89);
		// this.client1.connect((this.address , this.port+25));
		msg = "USER Dani\r\nPASS ciao\r\nUNREGISTER 3 4\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nKO\r\n", data);
		// msg = "USER Dani"+'\r'+'\n'+"PASS ciao"+'\r'+'\n'+"NEW
		// CIAO"+'\r'+'\n'+"NEW ciao11"+'\r'+'\n'+"MESSAGE 0
		// 1"+'\r'+'\n'+"MESSAGGIO 1"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"MESSAGE
		// 0"+'\r'+'\n'+"MESSAGGIO 2"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"LIST 0
		// 1 6"+'\r'+'\n'+"REPLY 0"+'\r'+'\n'+"hello second
		// msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY 0"+'\r'+'\n'+"hello
		// second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
		// 0"+'\r'+'\n'+"hello second
		// msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY 3"+'\r'+'\n'+"hello
		// second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
		// 2"+'\r'+'\n'+"hello second
		// msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"CONV 3\r\n"
	}

	@Test
	public void testConvMoreThanEnought() {

		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+41);

		// this.client1.connect((this.address , this.port+41));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "MESSAGE 0 1" + '\r' + '\n' + "MESSAGGIO 1" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "MESSAGE 0" + '\r' + '\n' + "MESSAGGIO 2" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "LIST 0 1 6" + '\r' + '\n' + "REPLY 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r'
				+ '\n' + '\r' + '\n' + "REPLY 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n'
				+ '\r' + '\n' + "REPLY 0" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r'
				+ '\n' + "REPLY 3" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "REPLY 2" + '\r' + '\n' + "hello second msg" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "CONV 0\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\nOK 1\r\nKO\r\nOK 2\r\nOK 3\r\nOK 4\r\nOK 5\r\nOK 6\r\nMESSAGES\r\n0 Dani 0 1\r\n2 Dani 0 1\r\n3 Dani 0 1\r\n4 Dani 0 1\r\n5 Dani 0 1\r\n6 Dani 0 1\r\n\r\n",
				data);
	}
	// @Test
	// public void testMultiUser(){

	// this.client1 = [];
	// this.client1.append(socket.socket(AF_INET,SOCK_STREAM));
	// this.client1.append(socket.socket(AF_INET,SOCK_STREAM));
	// //this.MyServer = MyChatServer(this.Dizionario,this.address ,
	// this.port+131);
	//
	//
	// this.client1[0].connect((this.address , this.port+131));
	// this.client1[1].connect((this.address , this.port+131));
	// msg0 = "USER Dani"+'\r'+'\n'+"PASS ciao"+'\r'+'\n'+"NEW
	// CIAO"+'\r'+'\n'+"NEW ciao11"+'\r'+'\n'+"MESSAGE 0 1"+'\r'+'\n'+"MESSAGGIO
	// 1"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"MESSAGE 0"+'\r'+'\n'+"MESSAGGIO
	// 2"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"LIST 0 1 6"+'\r'+'\n'+"REPLY
	// 0"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
	// 0"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
	// 0"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
	// 3"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
	// 2"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"CONV
	// 0\r\n"
	// this.client1[0].send(msg0);
	// data0 = this.client1[0].recv(1024);
	// msg1 = "USER user1"+'\r'+'\n'+"PASS }1"+'\r'+'\n'+"NEW
	// CIAO"+'\r'+'\n'+"NEW ciao11"+'\r'+'\n'+"MESSAGE 0 1"+'\r'+'\n'+"MESSAGGIO
	// 1"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"MESSAGE 0"+'\r'+'\n'+"MESSAGGIO
	// 2"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"LIST 0 1 6"+'\r'+'\n'+"REPLY
	// 0"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
	// 0"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
	// 0"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
	// 3"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"REPLY
	// 2"+'\r'+'\n'+"hello second msg"+'\r'+'\n'+"."+'\r'+'\n'+'\r'+'\n'+"CONV
	// 0\r\n"
	// this.client1[1].send(msg1);
	// data1 = this.client1[1].recv(1024);
	// print "//////////////////////////////////////////////////// INIZIO PRINT
	// //////////////////////////////////////////"
	// print data0
	// print "//////////////////////////////////////////////////// FINE PRINT
	// //////////////////////////////////////////"
	//
	// print data1
	// this.client1[0].close();
	// this.client1[1].close();
	// assertEquals(data0,"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 0\r\n", data0);
	// }
	@Test
	public void testSubscribeRegister() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+38);

		// this.client1.connect((this.address , this.port+38));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "REGISTER 127.0.0.1 89\r\n" + "SUBSCRIBE 0 1 0\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK\r\nOK\r\n", data);
	}

	@Test
	public void testSubscribeNotRegister() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+38);

		// this.client1.connect((this.address , this.port+38));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "SUBSCRIBE 0 1 0\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nKO\r\n", data);
	}

	@Test
	public void testSubscribeRegisterTaken() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+40);

		// this.client1.connect((this.address , this.port+40));
		MyChatServer.register.put("user1", new Pair<String, Integer>("127.0.0.1", 89));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "REGISTER 127.0.0.1 89\r\n" + "SUBSCRIBE 0 1 0\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nKO\r\nKO\r\n", data);
	}

	@Test
	public void testUnSubscribeRegister() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+39);

		// this.client1.connect((this.address , this.port+39));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "REGISTER 127.0.0.1 95\r\n" + "SUBSCRIBE 0 1 0\r\nUNSUBSCRIBE 0 1\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK\r\nOK\r\nOK\r\n", data);
	}

	@Test
	public void testRegisterSubscribeTopicUnsubscribe() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+39);

		// this.client1.connect((this.address , this.port+39));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "REGISTER 127.0.0.1 95\r\n" + "SUBSCRIBE 0\r\nTOPICS\r\nUNSUBSCRIBE 0\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK\r\nOK\r\nTOPIC_LIST\r\n*0 CIAO\r\n1 ciao11\r\n\r\nOK\r\n", data);
	}

	@Test
	public void testRegisterSubscribeTopicUnsubscribeTopic() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+39);

		// this.client1.connect((this.address , this.port+39));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "REGISTER 127.0.0.1 95\r\n" + "SUBSCRIBE 0\r\nTOPICS\r\nUNSUBSCRIBE 0\r\nTOPICS\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK\r\nOK\r\nTOPIC_LIST\r\n*0 CIAO\r\n1 ciao11\r\n\r\nOK\r\nTOPIC_LIST\r\n0 CIAO\r\n1 ciao11\r\n\r\n",
				data);
	}

	@Test
	public void testUnSubscribeRegisterMessage() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+39);

		// this.client1.connect((this.address , this.port+39));
		MyChatServer.register.put("user1", new Pair<String, Integer>("127.0.0.1", 89));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "REGISTER 127.0.0.1 4127\r\n" + "SUBSCRIBE 0 1 0\r\n" + "MESSAGE 0 1" + '\r' + '\n'
				+ "MESSAGGIO 2" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n' + "UNSUBSCRIBE 0 1\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK\r\nOK\r\nOK 0\r\nOK\r\n", data);
	}

	@Test
	public void testSubscribeRegisterMessageUregisterAll() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+39);

		// this.client1.connect((this.address , this.port+39));
		MyChatServer.register.put("user1", new Pair<String, Integer>("127.0.0.1", 89));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "NEW ciao22" + '\r' + '\n' + "REGISTER 127.0.0.1 4127\r\n" + "SUBSCRIBE 0 2\r\n"
				+ "MESSAGE 0 1" + '\r' + '\n' + "MESSAGGIO 2" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "UNREGISTER\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 2\r\nOK\r\nOK\r\nOK 0\r\nOK\r\n", data);
	}

	@Test
	public void testUnsubscribeNotRegister() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+38);
		// this.client1.connect((this.address , this.port+38));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "UNSUBSCRIBE 0 1\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nKO\r\n", data);
	}

	@Test
	public void testUnSubscribeRegisterMessageUregisterAllRegisterAgainSuscribeAgain() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+39);

		// this.client1.connect((this.address , this.port+39));
		MyChatServer.register.put("user1", new Pair<String, Integer>("127.0.0.1", 89));
		msg = "USER Dani" + '\r' + '\n' + "PASS ciao" + '\r' + '\n' + "NEW CIAO" + '\r' + '\n' + "NEW ciao11" + '\r'
				+ '\n' + "NEW ciao22" + '\r' + '\n' + "REGISTER 127.0.0.1 4127\r\n" + "SUBSCRIBE 0 2\r\n"
				+ "MESSAGE 0 1" + '\r' + '\n' + "MESSAGGIO 2" + '\r' + '\n' + "." + '\r' + '\n' + '\r' + '\n'
				+ "UNREGISTER \r\n" + "REGISTER 127.0.0.1 4127\r\n" + "SUBSCRIBE 0 2\r\n";
		this.client1.sendMsg(msg);

		// try {
		// Thread.sleep(16000000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		data = this.client1.receiveMsg(1024);
		assertEquals("OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 2\r\nOK\r\nOK\r\nOK 0\r\nOK\r\nOK\r\nOK\r\n", data);
	}

	@Test
	public void testAllCommandNotLogin() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+39);

		// this.client1.connect((this.address , this.port+39));
		// MyChatServer.Register.put("user1",new
		// Pair<String,Integer>("127.0.0.1", 89));
		msg = "NEW CIAO" + '\r' + '\n' + "NEW ciao22" + '\r' + '\n' + "REGISTER 127.0.0.1 4127\r\n"
				+ "SUBSCRIBE 0 2\r\n" + "MESSAGE 0 1" + '\r' + '\n' + "MESSAGGIO 2" + '\r' + '\n' + "." + '\r' + '\n'
				+ '\r' + '\n' + "UNREGISTER \r\n" + "REGISTER 127.0.0.1 4127\r\n" + "SUBSCRIBE 0 2\r\n"
				+ "UNSUBSCRIBE 0 1\r\n" + "TOPICS\r\n";
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals("KO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\n", data);
	}

	@Test
	public void testGiulio() {
		// this.MyServer = MyChatServer(this.Dizionario,this.address ,
		// this.port+39);

		// this.client1.connect((this.address , this.port+39));
		// MyChatServer.Register.put("user1",new
		// Pair<String,Integer>("127.0.0.1", 89));
		msg = "USER Dani\r\nPASS ciao\r\nNEW Titolo0\r\nNEW Titolo1\r\nNEW Titolo2\r\nNEW Titolo3\r\nMESSAGE 0 1 2 3\r\nCiao! Come stai?\r\n.\r\n\r\nMESSAGE 0 1 3\r\nBau!\r\n.\r\n\r\nLIST 0 2\r\nGET 1\r\nREPLY 0\r\nMiao!\r\n.\r\n\r\nREPLY 0\r\nBene!\r\n.\r\n\r\nREPLY 2\r\nBenone!\r\n.\r\n\r\nREPLY 2\r\nBenone!\r\n.\r\n\r\nREPLY 5\r\nBenone!\r\n.\r\n\r\nCONV 2\r\nREGISTER localhost 1982\r\n";
		// msg= msg +"SUBSCRIBE 0 1 2 3\r\nUNSUBSCRIBE 0 3 5\r\nUNSUBSCRIBE 0
		// 3\r\nMESSAGE 0 1 3\r\nArriva la notifica?\r\n.\r\n\r\nREPLY
		// 0\r\nSperiamo!\r\n.\r\n\r\n"
		this.client1.sendMsg(msg);
		data = this.client1.receiveMsg(1024);

		assertEquals(
				"OK\r\nOK\r\nOK 0\r\nOK 1\r\nOK 2\r\nOK 3\r\nOK 0\r\nOK 1\r\nMESSAGES\r\n0 Dani 0 1 2 3\r\n\r\nMESSAGE 1\r\nUSER Dani\r\nTOPICS 0 1 3\r\nBau!\r\n.\r\n\r\nOK 2\r\nOK 3\r\nOK 4\r\nOK 5\r\nOK 6\r\nMESSAGES\r\n0 Dani 0 1 2 3\r\n2 Dani 0 1 2 3\r\n4 Dani 0 1 2 3\r\n5 Dani 0 1 2 3\r\n6 Dani 0 1 2 3\r\n\r\nOK\r\n",
				data);
	}
}