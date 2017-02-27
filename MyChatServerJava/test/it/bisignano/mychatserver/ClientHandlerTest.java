package it.bisignano.mychatserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.Theories.TheoryAnchor;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import org.apache.log4j.Logger;

//@RunWith(PowerMockRunner.class)
@PrepareForTest({ClientHandler.class,FactoryHttpCommand.class})
public class ClientHandlerTest {
	 @Rule
	 public PowerMockRule rule = new PowerMockRule(); // DEVE ESSERE ABILITATO
	 												  // PER LA COVERAGE
	private Socket client;
	private ClientHandler c1;
	private InputStream in;
	private OutputStream out;
	private FactoryHttpCommand f1;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		client = mock(Socket.class);
		in = mock(InputStream.class);
		out = mock(OutputStream.class);
		when(client.getInputStream()).thenReturn(in);
		when(client.getOutputStream()).thenReturn(out);
		c1 = new ClientHandler(client);
	}

	@Test
	public void testConstructor() throws IOException {
		verify(client).getInputStream();
		verify(client).getOutputStream();
	}
	@Test/*(expected=IllegalArgumentException.class )*/
	public void testConstructorInputStreamFail() throws IOException {
		//client = PowerMockito.mock(Socket.class);
		when(client.getInputStream()).thenThrow(new IOException());
		ClientHandler c2 = new ClientHandler(client);
		assertEquals(1, c2.getLastMethodInvocationLog());
	}

	@Test
	public void testConnected() throws IOException {
		when(client.isConnected()).thenReturn(true);
		c1.isConnected();
		verify(client).isConnected();
	}

	@Test
	public void testAcceptVisitPasswd() {
		HttpPass ps = mock(HttpPass.class);
		when(ps.getLoginResult()).thenReturn(1);
		c1.acceptVisit(ps);
		assertEquals(1, c1.getLoginStatus());
	}

	@Test
	public void testAcceptVisitUser() {
		HttpUser user = mock(HttpUser.class);
		when(user.getUserName()).thenReturn("dani");
		c1.acceptVisit(user);
		assertEquals("dani", c1.getUserName());
	}

	@Test
	public void testAcceptVisitReg() {
		HttpRegister reg = mock(HttpRegister.class);
		when(reg.getLoginResult()).thenReturn(1);
		c1.acceptVisit(reg);
		assertEquals(1, c1.getLoginStatus());
	}

	@Test
	public void testAcceptVisitSub() {
		HttpSubscribe sub = mock(HttpSubscribe.class);
		when(sub.getLoginResult()).thenReturn(1);
		c1.acceptVisit(sub);
		assertEquals(1, c1.getLoginStatus());
	}

	@Test
	public void TestRun() throws Exception {

		when(in.read()).thenReturn(13, 10, -1);
		// when(out.write("OK\r\n".getBytes("latin1"))).thenReturn(13, 10); //
		// \r\n
		// StringBuilder sb = mock(StringBuilder.class);
		// PowerMockito.whenNew(StringBuilder.class).withAnyArguments().thenReturn(sb);
		// //when(sb.append(any(String.class)).
		// when(sb.length()).thenReturn(1,2);
		// when(sb.substring(any(int.class),any(int.class))).thenReturn("'\r'","'\r''\n'");
		ClientHandler spy = PowerMockito.spy(new ClientHandler(client));
		PowerMockito.doReturn("trol\r\n").when(spy, method(ClientHandler.class, "execute", String.class)).withArguments(anyString());
		spy.run();
		assertEquals("trol\r\n", spy.getResponse());
	}
	@Test
	public void TestRunExeceptionHandler() throws Exception {

		//when(in.read()).thenReturn(13, 10, -1);
		// when(out.write("OK\r\n".getBytes("latin1"))).thenReturn(13, 10); //
		// \r\n
		// StringBuilder sb = mock(StringBuilder.class);
		// PowerMockito.whenNew(StringBuilder.class).withAnyArguments().thenReturn(sb);
		// //when(sb.append(any(String.class)).
		// when(sb.length()).thenReturn(1,2);
		// when(sb.substring(any(int.class),any(int.class))).thenReturn("'\r'","'\r''\n'");
		//ClientHandler spy = PowerMockito.spy(new ClientHandler(client));
		PowerMockito.doThrow(new IllegalArgumentException()).when(in).read();
		c1.run();
		assertEquals("Error Reading..", c1.getResponse());
	}

	
	@Ignore
	public void TestRunExecute() throws Exception {
		when(in.read()).thenReturn(13, 10, -1); // \r\n.\r\n\r\n
		
		//################### WORK WITHOUT COVERAGE
		IHttpProtocol usr = mock(IHttpProtocol.class);
		PowerMockito.when(usr, method(IHttpProtocol.class, "execute", ClientHandler.class)).withArguments(any(ClientHandler.class)).thenReturn("trollata\r\n");
		PowerMockito.mockStatic(FactoryHttpCommand.class);
		PowerMockito.when(FactoryHttpCommand.getHtmlProtocol(any(String.class),any(int.class))).thenReturn(usr);
		//#################### END WORK WITHOUT COVERAGE
		c1.run();
		assertEquals("trollata\r\n", c1.getResponse());
	}


	@Test
	public void testAcceptVisitMessage() throws IOException {
		HttpMessage sub = mock(HttpMessage.class);
		when(in.read()).thenReturn(99, 105, 97, 111 ,13, 10,46,13, 10,13, 10, -1); // ciao\r\n.\r\n\r\n
		String messageRead = c1.acceptVisit(sub);
		assertEquals("ciao", messageRead);
	}
	@Test
	public void testAcceptVisitMessageTestException() throws IOException {
		HttpMessage sub = mock(HttpMessage.class);
		when(in.read()).thenReturn(99, 105, 97, 111,13, 10,46,13, 10,13, 10, -1); // ciao\r\n.\r\n\r\n
		String messageRead = c1.acceptVisit(sub);
		assertEquals("ciao", messageRead);
	}
	
	
	@After
	public void tearDown() throws Exception {
	}

}
