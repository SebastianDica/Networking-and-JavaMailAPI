package panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.mail.Flags;
import javax.mail.Message;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import api.MessageFilter;
import api.SSCMessage;
import api.SessionStarter;
import terminal.CallHandler;

public class ControlPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SessionStarter sessionStarter;
	private MainActivityPanel activity;
	private TerminalPanel terminal;
	private SSCMessage[] sscmessages;
	private MessageFilter[] filters = new MessageFilter[0];
	public ControlPanel(SessionStarter sessionStarter,MainActivityPanel activity, TerminalPanel terminal)
	{
		super();
		this.terminal = terminal;
		this.activity = activity;
		this.sessionStarter = sessionStarter;
		setBackground(Color.GRAY);
		createMainControlPanel();
	}
	public void createMainControlPanel()
	{
		terminal.appendMessage("Entering main view.");
		removeAll();
		activity.mainView();
		JButton exit = new JButton("Exit System");
		exit.addActionListener(e ->
		{
			int exitOptionPane = JOptionPane.YES_NO_OPTION;
    		int answer = JOptionPane.showConfirmDialog (null, 
    				"Would you like to close this program?",
    				"Please don't close me :'(",exitOptionPane);
    		if(answer == JOptionPane.YES_OPTION)
    			{
    				sessionStarter.close();
    				System.exit(CallHandler.EC_SUCCESS);
    			}
		});
		JButton login = new JButton("Log in");
		login.addActionListener(e ->
		{
			requestLoginInformation();
		});
		setLayout(new GridLayout(10,1,10,10));
		add(exit);
		add(login);
		revalidate();
		repaint();
	}
	public void requestLoginInformation()
	{
		terminal.appendMessage("Entering login view.");
		removeAll();
		JButton back = new JButton("Back");
		back.addActionListener(e ->
		{
			createMainControlPanel();
		});
		JLabel usernameLabel = new JLabel("Username: ");
		JLabel passwordLabel = new JLabel("Password: ");
		JPasswordField password = new JPasswordField("501eb6f9", 16);  
		JTextField username = new JTextField("sscfsd491@gmail.com");
		JButton attemptLogin = new JButton("Sign in");
		attemptLogin.addActionListener(e ->
		{
			boolean response = sessionStarter.proceedLogin(
					username.getText(), new String(password.getPassword()));
			if(response)
			{
				activity.alertMessage("<<< A u t h e n t i f i c a t i o n   s u c c e s f u l >>>");
				terminal.appendMessage("Authentification succesful.");
				createAccountManagerControlPanel();
			}
			else
			{
				activity.alertMessage("<<< A u t h e n t i f i c a t i o n   f a i l u r e >>>");
				terminal.appendMessage("Authentification failed.");
			}
		});
		add(back);
		add(usernameLabel);
		//add(username);
		add(passwordLabel);
		//add(password);
		add(attemptLogin);
		revalidate();
		repaint();
	}
	public void createAccountManagerControlPanel()
	{
		terminal.appendMessage("Entering account manager view.");
		removeAll();
		JButton back = new JButton("Sign out");
		back.addActionListener(e ->
		{
			sessionStarter.close();
			activity.alertMessage("<<< S i g n e d   o u t >>>");
			terminal.appendMessage("Signed out.");
			createMainControlPanel();
		});
		JButton createMail = new JButton("Create an e-mail");
		createMail.addActionListener(e ->
		{
			activity.createMailView();
			createMailControl();
		});
		JButton viewInbox = new JButton("Inbox");
		viewInbox.addActionListener(e ->
		{
			Message[] messages = sessionStarter.getMessages();
			int numberOfIterations = messages.length;
			sscmessages = new SSCMessage[numberOfIterations];
			for(int i = 0 ; i < numberOfIterations ; i++)
			{
				sscmessages[i] = new SSCMessage(messages[i]);
			}
			activity.folderView(sscmessages);
			insideFolder();
		});
		JButton customizableFilters = new JButton("Custom filters");
		customizableFilters.addActionListener(e -> 
		{
			customFilterView();
			activity.filterView(filters);
		});
		add(back);
		add(createMail);
		add(viewInbox);
		add(customizableFilters);
		revalidate();
		repaint();
	}
	public void customFilterView()
	{
		removeAll();
		JButton back = new JButton("Back");
		back.addActionListener(e ->
		{
			activity.mainView();
			createAccountManagerControlPanel();
		});
		JButton removeFilter = new JButton("Remove filter");
		removeFilter.addActionListener(e ->
		{
			filters = activity.removeSelectedFilter();
		});
		JButton addFilter = new JButton("Add filter");
		addFilter.addActionListener(e ->
		{
			addFilterView();
		});
		add(back);
		add(removeFilter);
		add(addFilter);
		revalidate();
		repaint();
	}
	public void addFilterView()
	{
		removeAll();
		JButton back = new JButton("Back");
		back.addActionListener(e ->
		{
			customFilterView();
		});
		JLabel match = new JLabel("Mathing with: ");
		JTextField matchField = new JTextField();
		JLabel flag = new JLabel("Flag as: ");
		JTextField flagAs = new JTextField();
		JButton submit = new JButton("Submit");
		JCheckBox toBe = new JCheckBox("Apply?");toBe.setBackground(Color.GRAY);
		submit.addActionListener(e ->
		{
			String matcher = matchField.getText();
			String flager = flagAs.getText();
			MessageFilter filter = new MessageFilter(matcher,new Flags(flager),toBe.isSelected());
			MessageFilter[] result = Arrays.copyOf(filters, filters.length +1);
		    result[filters.length] = filter;
		    filters = result;
		    activity.filterView(filters);
		    for(int i = 0 ; i < filters.length ; i++)
		    {
		    	String word = filters[i].getMarkup();
		    	Message[] messages = sessionStarter.performSearch(word);
				for(int j = 0 ; j < messages.length ; j++)
				{
					try 
					{
						messages[j].setFlags(filters[i].getFlag(), toBe.isSelected());
					} 
					catch (Exception e1) 
					{
						terminal.appendMessage("Flag: " + filters[i].getFlag().getUserFlags()[0] + 
								"could not be applied on one of the messages");
					}
				}
				sessionStarter.closeFolder();
		    }
			customFilterView();
		});
		add(back);
		add(match);add(matchField);
		add(flag);add(flagAs);
		add(toBe);
		add(submit);
		revalidate();
		repaint();
	}
	public void createMailControl()
	{
		removeAll();
		JButton back = new JButton("Back");
		back.addActionListener(e ->
		{
			activity.mainView();
			createAccountManagerControlPanel();
		});
		JButton sendMail = new JButton("Send");
		sendMail.addActionListener(e ->
		{
			if(activity.sendEmail(sessionStarter))
			{
				terminal.appendMessage("E-mail has been sent.");
				activity.mainView();
				createAccountManagerControlPanel();
			}
			else
			{
				terminal.appendMessage("An error has occured. Email has not been sent");
			}
		});
		JButton attachFile = new JButton("Attach a file");
		attachFile.addActionListener(e ->
		{
			activity.attachFile();
		});
		add(back);
		add(sendMail);
		add(attachFile);
		revalidate();
		repaint();
	}
	public void insideFolder()
	{
		removeAll();
		JButton back = new JButton("Back");
		back.addActionListener(e ->
		{
			activity.mainView();
			sessionStarter.closeFolder();
			createAccountManagerControlPanel();
		});
		JButton filterBy = new JButton("Filter By");
		JTextField word = new JTextField();
		filterBy.addActionListener(e -> 
		{
			Message[] messages = sessionStarter.performSearch(word.getText());
			SSCMessage[] sscmessages = new SSCMessage[messages.length];
			for(int i = 0 ; i < messages.length ; i++)
			{
				sscmessages[i] = new SSCMessage(messages[i]);
			}
			activity.folderView(sscmessages);
		});
		JButton view = new JButton("View selected");
		view.addActionListener(e ->
		{
			if(activity.displaySelected() == false)
			{
				terminal.appendMessage("Please select an item.");
			}
			else
			{
				terminal.appendMessage("Content displayed.");
				contentDisplay();
			}
		});
		JButton setSeen = new JButton("Seen selected");
		setSeen.addActionListener(e ->
		{
			if(activity.flagSelected(Flags.Flag.SEEN,true) == false)
			{
				terminal.appendMessage("Please select an item.");
			}
			else
			{
				terminal.appendMessage("Flag appended.");
				activity.folderView(sscmessages);
			}
		});
		JButton setUnseen = new JButton("Unseen selected");
		setUnseen.addActionListener(e ->
		{
			if(activity.flagSelected(Flags.Flag.SEEN,false) == false)
			{
				terminal.appendMessage("Please select an item.");
			}
			else
			{
				terminal.appendMessage("Flag appended.");
				activity.folderView(sscmessages);
			}
		});
		JButton setRecent = new JButton("Recent selected");
		setRecent.addActionListener(e ->
		{
			if(activity.flagSelected(Flags.Flag.RECENT,true) == false)
			{
				terminal.appendMessage("Please select an item.");
			}
			else
			{
				terminal.appendMessage("Flag appended.");
				activity.folderView(sscmessages);
			}
		});
		add(back);
		add(view);
		add(setSeen);add(setUnseen);
		add(setRecent);
		add(filterBy); add(word);
		revalidate();
		repaint();
	}
	public void contentDisplay()
	{
		removeAll();
		JButton back = new JButton("Back");
		back.addActionListener(e -> 
		{
			activity.folderView(sscmessages);
			insideFolder();
		});
		add(back);
		revalidate();
		repaint();
	}
}
