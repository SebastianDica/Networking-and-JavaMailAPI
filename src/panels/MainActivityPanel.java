package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import api.MessageFilter;
import api.SSCMessage;
import api.SessionStarter;

public class MainActivityPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea mainArea;
	private JList<SSCMessage> listOfSubjects;
	private JList<MessageFilter> listOfFilters;
	private ArrayList<File> currentFileAttachment;
	private JLabel attachmentsView = new JLabel();
	private JTextField toField,fromField,subjectField,ccField;
	public MainActivityPanel()
	{
		super(new GridBagLayout());
		setBackground(new Color(24,24,24));
		mainArea = new JTextArea();
		mainView();
	}
	public void filterView(MessageFilter[] filters)
	{
		if(filters.length > 0)
		{
			removeAll();
			setLayout(new GridBagLayout());
			listOfFilters = new JList<MessageFilter>(filters);
			listOfFilters.setBackground(new Color(24,24,24));
			listOfFilters.setForeground(Color.WHITE);
			listOfFilters.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
			JScrollPane scrollPane = new JScrollPane(listOfFilters);
	        GridBagConstraints constraints = new GridBagConstraints();
	       	constraints.gridwidth = GridBagConstraints.REMAINDER;
	       	constraints.fill = GridBagConstraints.HORIZONTAL;
	      	constraints.fill = GridBagConstraints.BOTH;
	       	constraints.weightx = 1.0;
	       	constraints.weighty = 1.0;
			add(scrollPane,constraints);
			revalidate();
	       	repaint();
		}
		else
		{
			alertMessage("<<< T h e r e   a r e   n o   f i l t e r s  "
							 + " t o   b e   d i s p l a y e d >>>");
		}
	}
	public MessageFilter[] removeSelectedFilter()
	{
		int selectedMessage = listOfFilters.getSelectedIndex();
		int j = 0; MessageFilter[] newFilters = new MessageFilter[listOfFilters.getModel().getSize() - 1];
		for(int i = 0 ; i < listOfFilters.getModel().getSize() ; i++)
		{
			if(i != selectedMessage)
			{
				newFilters[j] = listOfFilters.getModel().getElementAt(i);
				j++;
			}
		}
		filterView(newFilters);
		return newFilters;
	}
	public void folderView(SSCMessage[] messages)
	{
		if(messages.length > 0)
		{
			removeAll();
			setLayout(new GridBagLayout());
			listOfSubjects = new JList<SSCMessage>(messages);
			listOfSubjects.setBackground(new Color(24,24,24));
			listOfSubjects.setForeground(Color.WHITE);
			listOfSubjects.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
			JScrollPane scrollPane = new JScrollPane(listOfSubjects);
	        GridBagConstraints constraints = new GridBagConstraints();
	       	constraints.gridwidth = GridBagConstraints.REMAINDER;
	       	constraints.fill = GridBagConstraints.HORIZONTAL;
	      	constraints.fill = GridBagConstraints.BOTH;
	       	constraints.weightx = 1.0;
	       	constraints.weighty = 1.0;
			add(scrollPane,constraints);
			revalidate();
	       	repaint();
		}
		else
		{
			alertMessage("<<< T h e r e   a r e   n o   m e s s a g e s  "
							 + " t o   b e   d i s p l a y e d >>>");
		}
	}
	public void createMailView()
	{
		currentFileAttachment = new ArrayList<File>();
		removeAll();
		setLayout(new GridBagLayout());
		GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.EAST;
        GridBagConstraints right = new GridBagConstraints();
        right.weightx = 2.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.gridwidth = GridBagConstraints.REMAINDER;
		JLabel from = new JLabel("From: "); from.setForeground(Color.WHITE);
		JLabel to = new JLabel("To: "); to.setForeground(Color.WHITE);
		JLabel cc = new JLabel("CC: "); cc.setForeground(Color.WHITE);
		JLabel subject = new JLabel("Subject: "); subject.setForeground(Color.WHITE);
		JLabel attachments = new JLabel("Attachments: "); attachments.setForeground(Color.WHITE);
		attachmentsView = new JLabel("None"); attachmentsView.setForeground(Color.WHITE);
		fromField = new JTextField(); 
			fromField.setForeground(Color.WHITE);
			fromField.setBackground(new Color(40,40,40));
		toField = new JTextField(); 
			toField.setForeground(Color.WHITE);
			toField.setBackground(new Color(40,40,40));
		ccField = new JTextField(); 
			ccField.setForeground(Color.WHITE);
			ccField.setBackground(new Color(40,40,40));
		subjectField = new JTextField(); 
			subjectField.setForeground(Color.WHITE);
			subjectField.setBackground(new Color(40,40,40));
		mainArea.setEditable(true);
       	mainArea.setBackground(new Color(24,24,24));
       	mainArea.setForeground(Color.WHITE);
       	mainArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
		JScrollPane scrollPane = new JScrollPane(mainArea);
        GridBagConstraints constraints = new GridBagConstraints();
       	constraints.gridwidth = GridBagConstraints.REMAINDER;
       	constraints.fill = GridBagConstraints.HORIZONTAL;
      	constraints.fill = GridBagConstraints.BOTH;
       	constraints.weightx = 1.0;
       	constraints.weighty = 1.0;
       	mainArea.setText("");
		revalidate();
		add(from,left); add(fromField,right);
		add(to,left); add(toField,right);
		add(cc,left); add(ccField,right);
		add(subject,left); add(subjectField,right);
		add(attachments,left); add(attachmentsView,right);
		add(scrollPane,constraints);
		repaint();
	}
	public boolean sendEmail(SessionStarter sessionStarter)
	{
		try
		{
			MimeMessage message = new MimeMessage(sessionStarter.getSession());
			message.setFrom(new InternetAddress(fromField.getText()));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toField.getText()));
			message.setSubject(subjectField.getText());
			MimeBodyPart messageBodyPart =  new MimeBodyPart();
			messageBodyPart.setText(mainArea.getText());
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			for(int i = 0 ; i < currentFileAttachment.size(); i++)
			{
				MimeBodyPart messageBodyPartAttach =  new MimeBodyPart();
				DataSource source = new FileDataSource(currentFileAttachment.get(i));
				messageBodyPartAttach.setDataHandler(new DataHandler(source));
				messageBodyPartAttach.setFileName(currentFileAttachment.get(i).getName());
				multipart.addBodyPart(messageBodyPartAttach);
			}
			message.setContent(multipart);
			message.saveChanges();
			sessionStarter.sendMessage(message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void attachFile()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Choose attachment");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			currentFileAttachment.add(chooser.getSelectedFile());
			updateAttachmentsView();
		} 
	}
	public void updateAttachmentsView()
	{
		if(currentFileAttachment.size() == 0)
		{
			attachmentsView.setText("None.");
		}
		else
		{
			attachmentsView.setText("");
		}
		for(int i = 0 ; i < currentFileAttachment.size() ; i++)
		{
			attachmentsView.setText(attachmentsView.getText() + 
					" ; " + currentFileAttachment.get(i).getName());
		}
	}
	public boolean displaySelected()
	{
		SSCMessage selectedMessage = listOfSubjects.getSelectedValue();
		if(selectedMessage == null)
		{
			return false;
		}
		else
		{
			try
			{
				mainView();
				mainArea.setText("");
				if(selectedMessage.getMessage().getContentType().contains("TEXT/PLAIN")) 
				{
					mainArea.append(selectedMessage.getMessage().getContent().toString());
				}
				else 
				{
					Multipart multipart = (Multipart) selectedMessage.getMessage().getContent();
					for (int x = 0; x < multipart.getCount(); x++) 
					{
						BodyPart bodyPart = multipart.getBodyPart(x);
						if(bodyPart.getContentType().contains("TEXT/PLAIN")) 
						{
							mainArea.append(bodyPart.getContent().toString());
						}
	
					}
				}
			}
			catch(Exception e)
			{
				return false;
			}
		}
		return true;
	}
	public boolean flagSelected(Flag flag ,boolean check)
	{
		SSCMessage selectedMessage = listOfSubjects.getSelectedValue();
		if(selectedMessage == null)
		{
			return false;
		}
		else
		{
			try
			{
				selectedMessage.getMessage().setFlag(flag, check);
			}
			catch(Exception e)
			{
				return false;
			}
		}
		return true;
	}
	public void mainView()
	{
		removeAll();
		setLayout(new GridBagLayout());
		mainArea.setEditable(false);
       	mainArea.setBackground(new Color(24,24,24));
       	mainArea.setForeground(Color.WHITE);
       	mainArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
		JScrollPane scrollPane = new JScrollPane(mainArea);
        GridBagConstraints constraints = new GridBagConstraints();
       	constraints.gridwidth = GridBagConstraints.REMAINDER;
       	constraints.fill = GridBagConstraints.HORIZONTAL;
      	constraints.fill = GridBagConstraints.BOTH;
       	constraints.weightx = 1.0;
       	constraints.weighty = 1.0;
       	add(scrollPane, constraints);
       	mainArea.setText("");
       	revalidate();
       	repaint();
	}
	public void initialNewLine(int times)
	{
		for(int i = 0 ; i < times ; i++)
		{
			mainArea.append("\n");
		}
	}
	public void alertMessage(String message)
	{
		mainView();
		mainArea.setText("");
		initialNewLine(2);
		mainArea.append(message);
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
