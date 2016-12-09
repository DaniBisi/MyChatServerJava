package unifi.inf.rc.DanieleBisignano;

import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;

import org.junit.FixMethodOrder;
import org.junit.Rule;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestName;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyChatServerTestGiulio {
	@Rule
	public TestName name = new TestName();
	
	Map<String, String> dic;
	private MyChatServer myChatServer;
	private ChatClient bisi;
	private ChatClient marco;
	private ChatClient carmen;
	private Thread thread;
	private String expected;
	private static boolean serverIsUp = false;


	@Before
	public void setUp() throws Exception {
		System.out.println(name.getMethodName());
		if (!serverIsUp) {
			Map<String, String> dic = new HashMap<String, String>();
			dic.put("bisi", "easy");
			dic.put("marco", "lord");
			dic.put("Carmen di Pietro", "bocchivendola");
			myChatServer = new MyChatServer(dic, "127.0.0.1", 9182);
			thread = new Thread(myChatServer);
			thread.start();
			serverIsUp = true;
		}
		
		expected="";
		bisi = new ChatClient("127.0.0.1", 9182);
		marco = new ChatClient("127.0.0.1", 9182);
		carmen = new ChatClient("127.0.0.1", 9182);
		bisi.connectServer();
		marco.connectServer();
		carmen.connectServer();
	}

	@After
	public void tearDown() throws Exception {
		expected = expected.replaceAll("\r\n", " ");
		System.out.println(expected);
		bisi.closeSocket();
		marco.closeSocket();
		carmen.closeSocket();
	}
	
	
//	@Test
//	public void a() throws IOException {
//		bisiLogin();
//		bisi.sendMsg("NEW ciao\r\nNEW miao\r\nMESSAGE 0 1\r\nciao messaggio di prova\r\n.\r\n\r\nLIST 2\r\n");
//		System.out.println(bisi.receiveMsg());
//	}
	
	@Test
	public void a_test_USER_PASS_Single_Client() throws UnknownHostException, IOException {
		String actual = "";
		String stream = "";
		
		stream = "ci sto pensando troppo\r\nUSER\r\nUSER \r\nUSER bisi\r\nca";
		expected += "KO\r\nKO\r\nKO\r\nOK\r\n";
		bisi.sendMsg(stream);
		actual += bisi.receiveMsg();
		stream = "zzo\r\nUSER marco\r\nPASS culo\r\nPASS fica\r\nUSER ";
		expected += "KO\r\nKO\r\nKO\r\nKO\r\n";
		bisi.sendMsg(stream);
		actual += bisi.receiveMsg();
		stream = "Carmen di Pietro\r\nUser bisi\r\nPASS bocchi";
		expected += "OK\r\nKO\r\n";
		bisi.sendMsg(stream);
		actual += bisi.receiveMsg();
		stream = "nara\r\nUSER Carmen di Pietro\r\nUSER marco\r\nPASS bocchivendola\r\n";
		expected += "KO\r\nOK\r\nKO\r\nOK\r\n";
		bisi.sendMsg(stream);
		actual += bisi.receiveMsg();
		assertEquals(expected,actual);
		

	}
	
	@Test
	public void b_test_USER_PASS_Multi_Client() throws UnknownHostException, IOException {
		String stream1 = "";
		String stream2 = "";
		String stream3 = "";
		

		stream1 = "USER bisi\r\nUSER marco\r\nUSER bisi\r\nPASS easy\r\nPASS lord\r\nUSER Carmen di Pietro\r\n";
		stream2 = "USER marco\r\nUSER marco\r\nUSER bisi\r\nPASS lord\r\nPASS easy\r\nUSER Carmen di Pietro\r\n";
		stream3 = "USER Carmen di Pietro\r\nUSER marco\r\nUSER bisi\r\nPASS bocchivendola\r\nPASS easy\r\nUSER marco\r\n";
		expected = "OK\r\nKO\r\nKO\r\nOK\r\nKO\r\nKO\r\n";
		bisi.sendMsg(stream1);
		marco.sendMsg(stream2);
		carmen.sendMsg(stream3);

		assertEquals(expected,bisi.receiveMsg());
		assertEquals(expected,marco.receiveMsg());
		assertEquals(expected,carmen.receiveMsg());
	}
	
	@Test
	public void c_test_NEW_TOPICS_Single_Client() throws UnknownHostException, IOException {
		bisiLogin();
		String stream = "NEW\r\nTOPICS \r\nTOPICS\r\nNEW topic0\r\nTOPICS\r\n";
		expected = "KO\r\nKO\r\nTOPIC_LIST\r\n\r\nOK 0\r\nTOPIC_LIST\r\n0 topic0\r\n\r\n";;
	
		bisi.sendMsg(stream);
		assertEquals(expected,bisi.receiveMsg());

	}
	
	@Test
	public void d_test_NEW_TOPICS_Multi_Client() throws UnknownHostException, IOException {
		String stream1 = "USER bisi\r\nPASS easy\r\nNEW topic1\r\nTOPICS\r\n";
		String stream2 = "USER marco\r\nPASS lord\r\nNEW topic2\r\nTOPICS\r\n";
		String stream3 = "USER Carmen di Pietro\r\nPASS bocchivendola\r\nTOPICS\r\n";

		bisi.sendMsg(stream1);		// mettendo le send e le receive in quest'ordine, mi assicuro che il database venga
		bisi.receiveMsg();			// popolato secondo l'ordine atteso. se facessi prima tutte le send e poi tutte le
		marco.sendMsg(stream2);	// receive e' possibile, sebbene poco probabile, che la topicList venga popolata in modo 
		marco.receiveMsg();		// inatteso per via dell'interleaving tra i processi. Questo farebbe fallire il test
		carmen.sendMsg(stream3);	// qualche volta. Se esegui il test piu' volte col codice commentato fallisce anche a te?
		
//		bisi.sendMsg(stream1);
//		marco.sendMsg(stream2);
//		carmen.sendMsg(stream3);
//		bisi.receiveMsg();
//		marco.receiveMsg();
		
		expected = "OK\r\nOK\r\nTOPIC_LIST\r\n0 topic0\r\n1 topic1\r\n2 topic2\r\n\r\n";
		assertEquals(expected,carmen.receiveMsg());

	}
	
	@Test
	public void e_test_MESSAGE_Single_Client() throws UnknownHostException, IOException {
		bisiLogin();
		String stream = "MESSAGE\r\nmessaggio non valido\r\n.\r\n\r\nMESSAGE \r\nmessaggio non valido\r\n.\r\n\r\nMESSAGE 0 1 2 3\r\nmessaggio non valido\r\n.\r\n\r\nMESSAGE 0 1 2\r\n\r\n.\r\n\r\nMESSAGE\r\n\r\n.\r\n\r\nMESSAGE 0 1 2\r\nMessaggio 0\r\n.\r\n\r\n";
		expected = "KO\r\nKO\r\nKO\r\nKO\r\nKO\r\nOK 0\r\n";
		bisi.sendMsg(stream);
		assertEquals(expected,bisi.receiveMsg());

	}
	
	@Test
	public void f_test_MESSAGE_Multi_Client() throws UnknownHostException, IOException {
		bisiLogin();
		bisi.sendMsg("MESSAGE 0 1\r\nMessaggio 1\r\n.\r\n\r\n");
		String actual = bisi.receiveMsg();
		marcoLogin();
		marco.sendMsg("MESSAGE 1 2\r\nMessaggio 2\r\n.\r\n\r\nMESSAGE 0 2\r\nMessaggio 3\r\n.\r\n\r\n");
		actual += marco.receiveMsg();
		expected = "OK 1\r\nOK 2\r\nOK 3\r\n";
		assertEquals(expected,actual);

	}
	
	@Test
	public void g_test_LIST_GET_Single_Client() throws UnknownHostException, IOException {
		bisiLogin();
		bisi.sendMsg("LIST\r\nLIST \r\nLIST 0 0 1 2 3\r\nLIST 0 5\r\nLIST 0 0\r\nLIST 1 2\r\nLIST 0\r\nLIST 2\r\nLIST 99\r\nLIST 99 99\r\n");
		expected = "KO\r\nKO\r\nKO\r\nKO\r\nMESSAGES\r\n0 bisi 0 1 2\r\n1 bisi 0 1\r\n3 marco 0 2\r\n\r\nMESSAGES\r\n2 marco 1 2\r\n3 marco 0 2\r\n\r\nMESSAGES\r\n0 bisi 0 1 2\r\n1 bisi 0 1\r\n2 marco 1 2\r\n3 marco 0 2\r\n\r\nMESSAGES\r\n2 marco 1 2\r\n3 marco 0 2\r\n\r\nMESSAGES\r\n\r\nKO\r\n";
		assertEquals(expected,bisi.receiveMsg());
		bisi.sendMsg("GET\r\nGET \r\nGET sei\r\nGET 100\r\nGET 0 1\r\nGET 0 a\r\nGET 0\r\n");
		expected = ("KO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\nMESSAGE 0\r\nUSER bisi\r\nTOPICS 0 1 2\r\nMessaggio 0\r\n.\r\n\r\n");
		assertEquals(expected, bisi.receiveMsg());
	}
	
	@Test
	public void h_test_LIST_GET_Multi_Client() throws UnknownHostException, IOException {
		bisiLogin();
		bisi.sendMsg("LIST 0\r\nGET 4\r\n");
		expected = "MESSAGES\r\n0 bisi 0 1 2\r\n1 bisi 0 1\r\n2 marco 1 2\r\n3 marco 0 2\r\n\r\nKO\r\n";
		assertEquals(expected,bisi.receiveMsg());
		marcoLogin();
		marco.sendMsg("NEW topic3\r\nMESSAGE 0 1 2 3\r\nMessaggio 4\r\n.\r\n\r\n");
		expected = "OK 3\r\nOK 4\r\n";
		assertEquals(expected,marco.receiveMsg());
		bisi.sendMsg("LIST 0\r\nGET 4\r\n");
		expected = "MESSAGES\r\n0 bisi 0 1 2\r\n1 bisi 0 1\r\n2 marco 1 2\r\n3 marco 0 2\r\n4 marco 0 1 2 3\r\n\r\nMESSAGE 4\r\nUSER marco\r\nTOPICS 0 1 2 3\r\nMessaggio 4\r\n.\r\n\r\n";
		assertEquals(expected,bisi.receiveMsg());

	}
	
	@Test
	public void i_test_REPLY_CONV() throws UnknownHostException, IOException {
		marcoLogin();
		bisiLogin();
		marco.sendMsg("REPLY\r\n\r\n.\r\n\r\nREPLY \r\n\r\n.\r\n\r\nREPLY z\r\n\r\n.\r\n\r\nREPLY 0\r\n\r\n.\r\n\r\nREPLY z\r\ninvalid\r\n.\r\n\r\nCONV\r\nCONV z\r\nCONV 5\r\nREPLY 0\r\nRisposta a 0\r\n.\r\n\r\nCONV 5\r\n");
		expected = "KO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\nKO\r\nOK 5\r\nMESSAGES\r\n0 bisi 0 1 2\r\n5 marco 0 1 2\r\n\r\n";
		assertEquals(expected,marco.receiveMsg());
		bisi.sendMsg("REPLY 0\r\nRisposta a 0\r\n.\r\n\r\n"); // msg # 6
		bisi.receiveMsg();
		marco.sendMsg("REPLY 1\r\nRisposta a 1\r\n.\r\n\r\n"); // msg # 7
		marco.receiveMsg();
		bisi.sendMsg("REPLY 5\r\nRisposta a 5\r\n.\r\n\r\n"); // msg # 8
		bisi.receiveMsg();
		bisi.sendMsg("REPLY 5\r\nRisposta a 5\r\n.\r\n\r\n"); // msg # 9
		bisi.receiveMsg();
		marco.sendMsg("REPLY 9\r\nRisposta a 9\r\n.\r\n\r\n"); // msg # 10
		marco.receiveMsg();
		
		marco.sendMsg("CONV 0\r\nCONV 1\r\nCONV 5\r\nCONV 7\r\nCONV 8\r\nCONV 9\r\n");
		expected = "MESSAGES\r\n0 bisi 0 1 2\r\n5 marco 0 1 2\r\n6 bisi 0 1 2\r\n8 bisi 0 1 2\r\n9 bisi 0 1 2\r\n10 marco 0 1 2\r\n\r\n";
		expected += "MESSAGES\r\n1 bisi 0 1\r\n7 marco 0 1\r\n\r\n";
		expected += "MESSAGES\r\n0 bisi 0 1 2\r\n5 marco 0 1 2\r\n8 bisi 0 1 2\r\n9 bisi 0 1 2\r\n10 marco 0 1 2\r\n\r\n";
		expected += "MESSAGES\r\n1 bisi 0 1\r\n7 marco 0 1\r\n\r\n";
		expected += "MESSAGES\r\n0 bisi 0 1 2\r\n5 marco 0 1 2\r\n8 bisi 0 1 2\r\n\r\n";
		expected += "MESSAGES\r\n0 bisi 0 1 2\r\n5 marco 0 1 2\r\n9 bisi 0 1 2\r\n10 marco 0 1 2\r\n\r\n";
		assertEquals(expected, marco.receiveMsg());
		
	}
	
	@Test
	public void l_test_REGISTER_UNREGISTER_Multi_Client() throws UnknownHostException, IOException {
		marcoLogin();
		bisiLogin();
		bisi.sendMsg("UNREGISTER\r\nREGISTER a b c\r\nREGISTER 256.0.0.1 1000\r\nREGISTER 127.0.0.1 1023\r\nREGISTER 127.0.0.1 10000\r\n");
		expected = "KO\r\nKO\r\nKO\r\nKO\r\nOK\r\n";
		assertEquals(expected,bisi.receiveMsg());
		marco.sendMsg("REGISTER 127.0.0.1 1000\r\nREGISTER 127.0.0.1 20000\r\n");
		expected = "KO\r\nOK\r\n";
		assertEquals(expected,marco.receiveMsg());
		bisi.sendMsg("UNREGISTER\r\nREGISTER 127.0.0.1 20000\r\nREGISTER 127.0.0.1 10000\r\nREGISTER 127.0.0.1 15000\r\n");
		expected = "OK\r\nKO\r\nOK\r\nOK\r\n";
		assertEquals(expected,bisi.receiveMsg());

	}
	
	@Test
	public void m_test_SUBSCRIBE_UNSUBSCRIBE() throws UnknownHostException, IOException {
		bisiLogin();
		marcoLogin();
		bisi.sendMsg("SUBSCRIBE\r\nSUBSCRIBE \r\nSUBSCRIBE a\r\nSUBSCRIBE 0 1 2 3 4\r\nSUBSCRIBE 0 1\r\nUNSUBSCRIBE\r\nUNSUBSCRIBE \r\nUNSUBSCRIBE a\r\nUNSUBSCRIBE 4\r\nUNSUBSCRIBE 2 3\r\nUNSUBSCRIBE 0 1\r\nSUBSCRIBE 0 2\r\n");
		expected = "KO\r\nKO\r\nKO\r\nKO\r\nOK\r\nKO\r\nKO\r\nKO\r\nKO\r\nOK\r\nOK\r\nOK\r\n";
		assertEquals(expected,bisi.receiveMsg());
		bisi.sendMsg("TOPICS\r\n");
		expected = "TOPIC_LIST\r\n*0 topic0\r\n1 topic1\r\n*2 topic2\r\n3 topic3\r\n\r\n";
		assertEquals(expected,bisi.receiveMsg());
		marco.sendMsg("TOPICS\r\n");
		expected = "TOPIC_LIST\r\n0 topic0\r\n1 topic1\r\n2 topic2\r\n3 topic3\r\n\r\n";
		assertEquals(expected, marco.receiveMsg());
	}
	
	@Test
	public void n_test_RECEIVE_MESSAGE_FROM_SUBSCRIBED_TOPICS() throws UnknownHostException, IOException {
		ServerSocket server = new ServerSocket(15000, 10, InetAddress.getByName("127.0.0.1"));
		byte[] buffer = new byte[8192];
		marcoLogin();
		marco.sendMsg("MESSAGE 0 2\r\nTest\r\n.\r\n\r\n");
		assertEquals("OK 11\r\n", marco.receiveMsg());
		Socket clientSocket = server.accept();
		InputStream in = clientSocket.getInputStream();
		String stream = new String(Arrays.copyOfRange(buffer, 0, in.read(buffer)));
		expected = "MESSAGE 11\r\nUSER marco\r\nTOPICS 0 2\r\nTest\r\n\r\n.\r\n\r\n";
		assertEquals(expected, stream);
		clientSocket.close();
		marco.sendMsg("REPLY 11\r\nTest2\r\n.\r\n\r\n");
		assertEquals("OK 12\r\n", marco.receiveMsg());
		clientSocket = server.accept();
		in = clientSocket.getInputStream();
		stream = new String(Arrays.copyOfRange(buffer, 0, in.read(buffer)));
		expected = "MESSAGE 12\r\nUSER marco\r\nTOPICS 0 2\r\nTest2\r\n\r\n.\r\n\r\n";
		assertEquals(expected,stream);
		clientSocket.close();
		server.close();
	}
	
	
	@Test
	public void o_test_DIGEST() throws UnknownHostException, IOException {
		ServerSocket server = new ServerSocket(15000, 10, InetAddress.getByName("127.0.0.1"));
		byte[] buffer = new byte[8192];
		marcoLogin();
		bisiLogin();
		bisi.sendMsg("DIGEST\r\nDIGEST z\r\nDIGEST -1\r\nDIGEST 0 4\r\nDIGEST 0\r\nDIGEST 3\r\n");
		expected = "KO\r\nKO\r\nKO\r\nKO\r\nOK\r\nOK\r\n";
		assertEquals(expected,bisi.receiveMsg());
		marco.sendMsg("MESSAGE 0\r\nCiao\r\n.\r\n\r\nMESSAGE 0\r\nMiao\r\n.\r\n\r\nMESSAGE 2\r\nBau\r\n.\r\n\r\nREPLY 15\r\nYeah!\r\n.\r\n\r\n");
		assertEquals("OK 13\r\nOK 14\r\nOK 15\r\nOK 16\r\n", marco.receiveMsg());
		Socket clientSocket = server.accept();
		InputStream in = clientSocket.getInputStream();
		String stream = new String(Arrays.copyOfRange(buffer, 0, in.read(buffer)));
		expected = "MESSAGE 13\r\nUSER marco\r\nTOPICS 0\r\nCiao\r\n\r\nMESSAGE 14\r\nUSER marco\r\nTOPICS 0\r\nMiao\r\n\r\nMESSAGE 15\r\nUSER marco\r\nTOPICS 2\r\nBau\r\n\r\n.\r\n\r\n";
		assertEquals(expected, stream);
		clientSocket.close();
		bisi.sendMsg("DIGEST 0\r\n");
		assertEquals("OK\r\n", bisi.receiveMsg());
		clientSocket = server.accept();
		in = clientSocket.getInputStream();
		stream = new String(Arrays.copyOfRange(buffer, 0, in.read(buffer)));
		expected = "MESSAGE 16\r\nUSER marco\r\nTOPICS 2\r\nYeah!\r\n\r\n.\r\n\r\n";
		assertEquals(expected, stream);
		clientSocket.close();
		server.close();
//		ServerDB.printDB();
	}

	public void bisiLogin() throws IOException {
		bisi.sendMsg("USER bisi\r\nPASS easy\r\n");
		String risp = bisi.receiveMsg();
		System.out.println(risp);
	}
	public void marcoLogin() throws IOException {
		marco.sendMsg("USER marco\r\nPASS lord\r\n");
		String risp = marco.receiveMsg();
		System.out.println(risp);
	}

}
