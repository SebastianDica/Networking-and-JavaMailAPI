package api;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;

public class SSCMessage {
	private Message message;
	public SSCMessage(Message message)
	{
		this.message = message;
	}
	@Override
	public String toString()
	{
		String result = "";
		Flags flags;
		try 
		{
			flags = message.getFlags();
			for(String flag : flags.getUserFlags())
			{
				result += "<" + flag + "> ";
			}
			if(flags.contains(Flag.SEEN)) 
				result += " <SEEN> ";
			else
				result += " <UNSEEN> ";
			if(flags.contains(Flag.DELETED))
			result += " <DELETED> ";
			if(flags.contains(Flag.RECENT))
			result += " <RECENT> ";
		} 
		catch (MessagingException e1) 
		{
		}
		try 
		{
			return result + "      " + message.getSubject();
		} 
		catch (MessagingException e) 
		{
			return result + "       <<< N o   s u b j e c t >>>";
		}
	}
	public Message getMessage()
	{
		return message;
	}
}
