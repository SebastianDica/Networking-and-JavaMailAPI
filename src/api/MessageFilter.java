package api;

import javax.mail.Flags;

public class MessageFilter 
{
	private String markup;
	private Flags flag;
	private boolean check;
	public MessageFilter(String markup, Flags flag, boolean check)
	{
		this.markup = markup;
		this.flag = flag;
		this.check = check;
	}
	public String getMarkup()
	{
		return markup;
	}
	public Flags getFlag()
	{
		return flag;
	}
	public String toString()
	{
		if(check)
			return markup + " MARKED AS: " + flag.getUserFlags()[0];
		else
			return markup + " UNMARKED AS: " + flag.getUserFlags()[0];
			
	}
}
