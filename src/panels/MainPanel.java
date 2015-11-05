package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import api.SessionStarter;

public class MainPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ControlPanel control;
	private TerminalPanel terminal;
	private MainActivityPanel mainActivity;
	private TopPanel top;
	public MainPanel(SessionStarter sessionStarter)
	{
		super();
		terminal = new TerminalPanel();
		mainActivity = new MainActivityPanel();
		control = new ControlPanel(sessionStarter,mainActivity,terminal);
		top = new TopPanel();
		top.setPreferredSize(new Dimension(700,100));
		control.setPreferredSize(new Dimension(250,600));
		terminal.setPreferredSize(new Dimension(700,150));
		mainActivity.setPreferredSize(new Dimension(450,325));
		setLayout(new BorderLayout(10,10));
		add(top,BorderLayout.NORTH);
		add(control,BorderLayout.EAST);	
		add(terminal,BorderLayout.SOUTH);
		add(mainActivity,BorderLayout.CENTER);
		setBackground(Color.BLACK);
	}
}
