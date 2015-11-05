package api;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;

import com.sun.mail.imap.IMAPFolder;

public class SessionStarter {
	private IMAPFolder folder = null;
	private Store store = null;
	private Transport transport = null; private Session session;
	private String smtphost = "smtp.gmail.com";
	public SessionStarter()
	{
	}
	public IMAPFolder getFolder()
	{
		return folder;
	}
	public Store getStore()
	{
		return store;
	}
	public void setFolder(IMAPFolder folder)
	{
		this.folder = folder;
	}
	public Session getSession()
	{
		return session;
	}
	public boolean proceedLogin(String username, String password)
	{
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtphost);
		props.put("mail.smtp.port", "587");
		props.setProperty("mail.store.protocol", "imaps");
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);
		session = Session.getDefaultInstance(props);
		try 
		{
			store = session.getStore("imaps");
			store.connect("imap.googlemail.com",username, password);
			transport = session.getTransport("smtp");
			transport.connect(smtphost, username, password);
			return true;
		} 
		catch (NoSuchProviderException e) 
		{
		} 
		catch (MessagingException e) 
		{
		}
		return false;
	}
	public void sendMessage(MimeMessage message)
	{
		try 
		{
			transport.sendMessage(message, message.getAllRecipients());
		} 
		catch (MessagingException e) 
		{
			
		}
	}
	public Message[] getMessages()
	{
		Message[] messages = new Message[0];
		try 
		{
			folder = (IMAPFolder) store.getFolder("inbox");
			if(!folder.isOpen()) folder.open(Folder.READ_WRITE);
			messages = folder.getMessages();
		} 
		catch (MessagingException e) 
		{
			//INBOX DOESN'T EXIST OR CANNOT BE OPENED // CORRUPTED
			e.printStackTrace();
		}
		return messages;
	}
	public Message[] performSearch(String term)
	{
		SearchTerm searchCondition = new SearchTerm() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public boolean match(Message message) {
				String content = "";
				try
				{
					content = message.getContent().toString();
				}
				catch(Exception e)
				{
					
				}
				try {
					if (message.getSubject().contains(term) || content.contains(term)) {
						return true;
					}
				} catch (MessagingException ex) {
					ex.printStackTrace();
				}
				return false;
			}
		};
		try 
		{
			getMessages();
			return folder.search(searchCondition);
		} 
		catch (MessagingException e) 
		{
			return new Message[0];
		}
	}
	public void closeFolder()
	{
		if (folder != null && folder.isOpen()) 
		{ 
			try 
			{
				folder.close(true);
			} 
			catch (MessagingException e) 
			{
				e.printStackTrace();
			} 
		}
	}
	public void close()
	{
		if (folder != null && folder.isOpen()) 
		{ 
			try 
			{
				folder.close(true);
			} 
			catch (MessagingException e) 
			{
				e.printStackTrace();
			} 
		}
		if (store != null) 
		{ 
			try 
			{
				store.close();
			} 
			catch (MessagingException e) 
			{
				e.printStackTrace();
			} 
		}
	}
}
