package framework.util.email;

import java.util.Arrays;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import framework.core.ExecutionConfig;
import org.fest.assertions.api.Assertions;
import org.testng.Reporter;
import static framework.util.email.StoreType.valueOf;

public class EmailUtil{

	public static EmailMessage[] getAllEmails(String emailAddress, String password){

		Message messages[] = null;
		Folder inbox = null;
		EmailMessage emailMessages[] = null;
		Store store = null;

		try{
			store = StoreType.valueOf(getDomain(emailAddress).toUpperCase()).getStore(emailAddress, password);
			store.connect(emailAddress, password);
			inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);
			messages = inbox.getMessages();
			if(messages.length == 0){
				throw new RuntimeException("Inbox empty.");
			}
			else{
				emailMessages = new EmailMessage[messages.length];
				int i = 0;
				for(Message message: messages){
					emailMessages[i] = new EmailMessage();
					emailMessages[i].setSubject(getEmailSubjectTemp(message));
					emailMessages[i].setFrom(getEmailFromTemp(message));
					emailMessages[i].setTo(getEmailToTemp(message));
					emailMessages[i].setCc(getEmailCcTemp(message));
					emailMessages[i].setPlainText(getEmailPlainTextTemp(message));
					emailMessages[i].setHtmlText(getEmailHTMLTextTemp(message));
					i++;
				}
			}
		}catch(Throwable e){
			throw (RuntimeException) e;
		}finally{
			try{
				if(inbox != null)
					inbox.close(true);
				if(store != null)
					store.close();
			}catch(Throwable e1){
				throw (RuntimeException) e1;
			}
		}
		return emailMessages;
	}

	public static void sendEmail(String from, String to, String cc, String bcc, String subject, String body){

		Reporter.log("Send email.");
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", ExecutionConfig.SMTP_HOSTNAME);
		props.put("mail.smtp.port", ExecutionConfig.SMTP_PORT);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(ExecutionConfig.SMTP_USERNAME, ExecutionConfig.SMTP_PASSWORD);
					}
				});

		session.setDebug(true);

		try{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipients(Message.RecipientType.TO, to);
			if(null == cc){
				cc = "";
			}
			message.addRecipients(RecipientType.CC, cc);
			if(null == bcc){
				bcc = "";
			}
			message.addRecipients(RecipientType.BCC, bcc);
			message.setSubject(subject);
			message.setContent(body, "text/html" );
			Transport.send(message);
		}catch (MessagingException mex) {
			throw new RuntimeException(mex);
		}
	}

	public static void deleteAllEmails(String emailAddress, String password){

		Reporter.log("Delete all emails.");
		delete(emailAddress, password, 1, null, -1);
	}
	
	public static void deleteEmail(String emailAddress, String password, String emailSubject){

		Reporter.log("Delete email with the subject: " + emailSubject);
		delete(emailAddress, password, 2, emailSubject, -1);
	}
	
	public static void deleteEmail(String emailAddress, String password, int emailPosition){

		Reporter.log("Delete email at position: " + emailPosition);
		delete(emailAddress, password, 3, null, emailPosition);
	}

	private static String getDomain(String emailAddress){
		String abc = emailAddress.split("@")[1];
		abc = abc.split("\\.")[0];
		return abc; // abc@def.gh.ij returns "def"
	}

	private static void delete(String emailAddress, String password, int type, String emailSubject, int emailPosition){

		Store store = null;
		try {
			store = StoreType.valueOf(getDomain(emailAddress).toUpperCase()).getStore(emailAddress, password);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		}

		Folder inbox = null;
		boolean emailPresent = false;
		try{
			inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_WRITE);
			Message messages[] = inbox.getMessages();
			if(messages.length != 0){
				if(type == 1){
					emailPresent = true;
					for(Message m: messages){
						m.setFlag(Flags.Flag.DELETED, true);
					}	
				}else if(type == 2){
					for(Message m: messages){
						if(m.getSubject().equalsIgnoreCase(emailSubject)){
							emailPresent = true;
							m.setFlag(Flags.Flag.DELETED, true);
							break;
						}
					}
				}else{
					if(emailPosition >= 0 && emailPosition < messages.length){
						emailPresent = true;
						messages[emailPosition].setFlag(Flags.Flag.DELETED, true);
					}
				}
				if(!emailPresent){
					Reporter.log("Email not present.");
				}
			}else{
				Reporter.log("Inbox empty.");
			}
		}catch(Throwable e){
			throw (RuntimeException) e;
		}finally{
			try{
				if(inbox != null)
					inbox.close(true);
				if(store != null)
					store.close();
			}catch(Throwable e1){
				throw (RuntimeException) e1;
			}
		}
	}
	
	public static EmailMessage getEmail(String emailAddress, String password, int emailPosition){

		EmailMessage messages[] = getAllEmails(emailAddress, password);
		if(emailPosition < 0 || emailPosition >= messages.length){
			throw new RuntimeException("Email not present.");
		}
		return messages[emailPosition];
	}
	
	public static EmailMessage getEmail(EmailMessage messages[], String emailSubject){

		int emailPosition = getEmailPosition(messages, emailSubject);
		return messages[emailPosition];
	}
	
	public static EmailMessage getEmail(String emailAddress, String password, String emailSubject){

		Reporter.log("Get email message with the subject: " + emailSubject);
		EmailMessage messages[] = getAllEmails(emailAddress, password);
		return messages[getEmailPosition(messages, emailSubject)];
	}
	
	private static int getEmailPosition(EmailMessage messages[], String emailSubject){

		int emailPosition = -1;
		try {
			if(messages.length != 0){
				for(int i = 0; i <= messages.length; i++){
					if(messages[i].getSubject().equalsIgnoreCase(emailSubject)){
						emailPosition = i;
						break;
					}
				}
			}
			if(emailPosition == -1){
				throw new RuntimeException("Email not present.");
			}
		}catch (Throwable e) {
			throw (RuntimeException) e;
		}
		return emailPosition;
	}
	
	private static String getEmailSubjectTemp(Message message){

		String emailSubject = null;
		try {
			emailSubject = message.getSubject();
		} catch (Throwable e) {
			throw (RuntimeException) e;
		} 
		return emailSubject;
	}
	
	private static String getEmailFromTemp(Message message) {

		Address from[];
		String emailFrom = null;
		try {
			from = message.getFrom();
			emailFrom = from[0].toString();
			if(emailFrom.contains("<")){
				emailFrom = emailFrom.substring(emailFrom.indexOf("<")+1, emailFrom.indexOf(">"));
			}
		} catch (Throwable e) {
			throw (RuntimeException) e;
		}
		return emailFrom;
	}
	
	private static String[] getEmailToTemp(Message message) {

		Address to[];
		String emailTo[] = null;
		int i = 0;
		try {
			to = message.getRecipients(RecipientType.TO);
			if(to != null){
				emailTo = new String[to.length];
				for(Address a: to){
					emailTo[i] = a.toString();
					if(emailTo[i].contains("<")){
						emailTo[i] = emailTo[i].substring(emailTo[i].indexOf("<")+1, emailTo[i].indexOf(">"));
						i++;
					}
				}
			}
		} catch (Throwable e) {
			throw (RuntimeException) e;
		}
		return emailTo;
	}
	
	private static String[] getEmailCcTemp(Message message) {

		Address cc[];
		String emailCc[] = null;
		int i = 0;
		try {
			cc = message.getRecipients(RecipientType.CC);
			if(cc != null){
				emailCc = new String[cc.length];
				for(Address a: cc){
					emailCc[i] = a.toString();
					if(emailCc[i].contains("<")){
						emailCc[i] = emailCc[i].substring(emailCc[i].indexOf("<")+1, emailCc[i].indexOf(">"));
						i++;
					}
				}
			}
		} catch (Throwable e) {
			throw (RuntimeException) e;
		}
		return emailCc;
	}
	
	private static String getEmailPlainTextTemp(Message message){

		String emailText = null;
		try{
			if(message.getContent() instanceof String){
				emailText = message.getContent().toString();
			}else{
				Multipart multipart = (Multipart) message.getContent();
				for (int x = 0; x < multipart.getCount(); x++){
					BodyPart bodyPart = multipart.getBodyPart(x);
					String disposition = bodyPart.getDisposition();
					if(disposition != null && (disposition.equals(BodyPart.ATTACHMENT))){
					}else{
						if(bodyPart.getContentType().toUpperCase().contains("TEXT/PLAIN")){
							emailText = bodyPart.getContent().toString();
							break;
						}
					}
				}
			}
		}catch(Throwable e){
			throw (RuntimeException) e;
		}
		return emailText;
	}
	
	private static String getEmailHTMLTextTemp(Message message){

		String emailText = null;
		try{
			if(message.getContent() instanceof String){
				emailText = message.getContent().toString();
			}else{
				Multipart multipart = (Multipart) message.getContent();
				for (int x = 0; x < multipart.getCount(); x++){
					BodyPart bodyPart = multipart.getBodyPart(x);
					String disposition = bodyPart.getDisposition();
					if(disposition != null && (disposition.equals(BodyPart.ATTACHMENT))){
					}else{
						if(bodyPart.getContentType().toUpperCase().contains("TEXT/HTML")){
							emailText = bodyPart.getContent().toString();
							break;
						}
					}
				}
			}
		}catch(Throwable e){
			throw (RuntimeException) e;
		}
		return emailText;
	}
	
	public static void assertEmailSubject(EmailMessage message, String expectedEmailSubject){

		Reporter.log("Assert that email subject is: " + expectedEmailSubject);
		String emailSubject = message.getSubject();
		Assertions.assertThat(emailSubject).isEqualToIgnoringCase(expectedEmailSubject.trim());
	}
	
	public static void assertEmailSubjectContains(EmailMessage message, String expectedEmailSubject){

		Reporter.log("Assert that email subject contains: " + expectedEmailSubject);
		String emailSubject = message.getSubject();
		Assertions.assertThat(emailSubject).containsIgnoringCase(expectedEmailSubject.trim());
	}
	
	public static void assertEmailFrom(EmailMessage message, String expectedEmailFrom){

		Reporter.log("Assert that email from is: " + expectedEmailFrom);
		String emailFrom = message.getFrom();
		Assertions.assertThat(emailFrom.toLowerCase()).isEqualTo(expectedEmailFrom.toLowerCase().trim());
	}
	
	public static void assertEmailTo(EmailMessage message, String ...expectedEmailTo){

		Reporter.log("Assert that email To is: ");
		String emailTo[] = message.getTo();
		emailTo = convertStringArrayCase(emailTo, false);
		Arrays.sort(emailTo);

		expectedEmailTo = trimStringArray(expectedEmailTo);
		expectedEmailTo = convertStringArrayCase(expectedEmailTo, false);
		Arrays.sort(expectedEmailTo);
		
		Assertions.assertThat(emailTo).isEqualTo(expectedEmailTo);
	}

	private static String[] convertStringArrayCase(String stringArray[], boolean toUpper){

		if(toUpper){
			for(int i = 0; i < stringArray.length; i++){
				stringArray[i] = stringArray[i].toUpperCase();
			}
		}else{
			for(int i = 0; i < stringArray.length; i++){
				stringArray[i] = stringArray[i].toLowerCase();
			}
		}
		return stringArray;
	}

	private static String[] trimStringArray(String stringArray[]){

		for(int i = 0; i < stringArray.length; i++){
			stringArray[i] = stringArray[i].trim();
		}
		return stringArray;
	}

	public static void assertEmailCc(EmailMessage message, String ...expectedEmailCc){

		Reporter.log("Assert that email Cc is: ");
		String emailCc[] = message.getCc();
		emailCc = convertStringArrayCase(emailCc, false);
		Arrays.sort(emailCc);

		expectedEmailCc = trimStringArray(expectedEmailCc);
		expectedEmailCc = convertStringArrayCase(expectedEmailCc, false);
		Arrays.sort(expectedEmailCc);
		Assertions.assertThat(emailCc).isEqualTo(expectedEmailCc);
	}

	public static void assertEmailBcc(EmailMessage message, String ...expectedEmailBcc){

		Reporter.log("Assert that email Bcc is: ");
		String emailBcc[] = message.getBcc();
		emailBcc = convertStringArrayCase(emailBcc, false);
		Arrays.sort(emailBcc);

		expectedEmailBcc = trimStringArray(expectedEmailBcc);
		expectedEmailBcc = convertStringArrayCase(expectedEmailBcc, false);
		Arrays.sort(expectedEmailBcc);
		Assertions.assertThat(emailBcc).isEqualTo(expectedEmailBcc);
	}
	
}
