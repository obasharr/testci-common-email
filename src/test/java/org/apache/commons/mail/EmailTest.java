package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {

	private static final String[] TEST_EMAILS = { "ab@bc.com", "a.b@c.org",
			"abcdefg@zyx.com"
	};
	private static final String TEST_EMAIL = "ab@bc.com";
	private static final String TEST_NAME = "owen";
	private static final String TEST_MESSAGE = "Hello";
	private static final String TEST_SUBJECT = "Java";
	private static final String TEST_VALUE = "3";

	
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception{
		email = new EmailConcrete();
	}
	
	@After
	public void tearDownEmailTest() throws Exception{}
	
	
	//Tests adding multiple emails as Bcc and asserts they are added
	@Test
	public void testAddBcc() throws Exception{
		email.addBcc(TEST_EMAILS);
		
		assertEquals(3, email.getBccAddresses().size());
		
	}
	
	// Tests adding one Cc email and asserts it is added
	@Test
	public void testAddCc() throws Exception{
		email.addCc(TEST_EMAIL);
		
		assertEquals(1, email.getCcAddresses().size());
	}
	
	// Adds a header add asserts it is correctly added
	@Test
	public void testAddHeader() throws Exception{
		email.addHeader(TEST_NAME, TEST_VALUE);
		
		assertEquals(TEST_VALUE, email.headers.get(TEST_NAME));
	} 
	
	// Tests adding header without a value
	@Test(expected = IllegalArgumentException.class)
	public void testAddHeaderWithoutValue() throws Exception{
		email.addHeader(TEST_NAME, "");
	} 
	
	// Tests adding a reply and asserts it is added
	@Test
	public void testAddReplyTo() throws Exception{
		email.addReplyTo(TEST_EMAIL, TEST_NAME);
		assertEquals(1, email.getReplyToAddresses().size());
	} 
	
	// Tests calling build mime message twice, which is not allowed
	@Test(expected = IllegalStateException.class)
	public void testBuildMimeMessageCalledTwice() throws Exception {
	    email.setHostName("localhost");
	    email.setMsg(TEST_MESSAGE);

	    email.setFrom("test@example.com");
	    email.addTo("test@example.com");

	    email.buildMimeMessage();  
	    email.buildMimeMessage();  
	}
	
	// Tests build mime message without a from email
	@Test(expected = EmailException.class)
	public void testBuildMimeMessageWithoutFrom() throws Exception {
	    email.setHostName("localhost");
	    email.setMsg(TEST_MESSAGE);

	    email.addTo("test@example.com");

	    email.buildMimeMessage();    
	}
	
	// Tests build mime message without a to email
	@Test(expected = EmailException.class)
	public void testBuildMimeMessageWithoutTo() throws Exception {
	    email.setHostName("localhost");
	    email.setMsg(TEST_MESSAGE);

	    email.setFrom("test@example.com");

	    email.buildMimeMessage();    
	}
	
	// Tests a valid case of building mime message and asserts it is correct
	@Test
	public void testBuildMimeMessageValid() throws Exception {
	    email.setHostName("localhost");
	    email.setFrom("from@example.com");
	    email.addTo("to@example.com");
	    email.setSubject(TEST_SUBJECT);
	    email.setMsg(TEST_MESSAGE);

	    email.buildMimeMessage();

	    assertNotNull(email.getMimeMessage());
	    assertEquals(TEST_SUBJECT, email.getMimeMessage().getSubject());
	}
	
	// Tests building mime message with a given charset
	@Test
	public void testBuildMimeMessageWithCharset() throws Exception {
	    email.setHostName("localhost");
	    email.setFrom("from@example.com");
	    email.addTo("to@example.com");
	    email.setCharset("UTF-8");
	    email.setSubject(TEST_SUBJECT);
	    email.setMsg(TEST_MESSAGE);

	    email.buildMimeMessage();

	    assertNotNull(email.getMimeMessage());
	    assertEquals(TEST_SUBJECT, email.getMimeMessage().getSubject());
	}
	
	// Tests build mime message with a CC email
	@Test
	public void testBuildMimeMessageWithCc() throws Exception {
	    email.setHostName("localhost");
	    email.setFrom("from@example.com");
	    email.addTo("to@example.com");
	    email.addCc("cc@example.com");
	    email.setMsg(TEST_MESSAGE);

	    email.buildMimeMessage();
	    
	    assertEquals(1,
	    email.getMimeMessage().getRecipients(javax.mail.Message.RecipientType.CC).length);
	}
	
	// Tests build mime message with a BCC email
	@Test
	public void testBuildMimeMessageWithBcc() throws Exception {
	    email.setHostName("localhost");
	    email.setFrom("from@example.com");
	    email.addTo("to@example.com");
	    email.addBcc("bcc@example.com");
	    email.setMsg(TEST_MESSAGE);

	    email.buildMimeMessage();
	    assertEquals(1,
	    email.getMimeMessage().getRecipients(javax.mail.Message.RecipientType.BCC).length);
	}
	
	// Tests build mime message with its content in plain text
	@Test
	public void testBuildMimeMessageWithPlainTextContent() throws Exception {
	    email.setHostName("localhost");
	    email.setFrom("from@example.com");
	    email.addTo("to@example.com");

	    email.setContent("Hello", Email.TEXT_PLAIN);

	    email.buildMimeMessage();
	    assertNotNull(email.getMimeMessage());
	}
	
	// Tests build mime message with its content not in plain text
	@Test
	public void testBuildMimeMessageWithNonPlainTextContent() throws Exception {
	    email.setHostName("localhost");
	    email.setFrom("from@example.com");
	    email.addTo("to@example.com");

	    email.setContent("<h1>Hello</h1>", Email.TEXT_HTML);

	    email.buildMimeMessage();
	    assertNotNull(email.getMimeMessage());
	}
	
	// Tests get host name after setting it, and asserts it sets correctly 
	@Test
	public void testGetHostName() throws Exception{
	    email.setHostName("localhost");
	    assertEquals("localhost", email.getHostName());
	}
	
	// Tests get host name without setting it, and asserts that its null
	@Test
	public void testGetHostNameWithNoHost() throws Exception{
	    assertEquals(null, email.getHostName());
	}
	
	// Tests get mail session without setting host name, which throws an exception
	@Test(expected = EmailException.class)
	public void testGetMailSessionWithoutHostName() throws Exception {
	    email.getMailSession();
	}
	
	// Tests get sent date after setting it, and asserts that it sets correctly 
	@Test
	public void testGetSentDateAfterSet() throws Exception {
	    Date date = new Date();

	    email.setSentDate(date);

	    Date result = email.getSentDate();

	    assertEquals(date, result);
	}
	
	// Tests get socket connection timeout after setting it, and asserts it is set correctly
	@Test
	public void testGetSocketConnectionTimeout() throws Exception{
		int socketConnectionTimeout = 50;
		email.setSocketConnectionTimeout(socketConnectionTimeout);
		
	    assertEquals(socketConnectionTimeout, email.getSocketConnectionTimeout());
	}
}
