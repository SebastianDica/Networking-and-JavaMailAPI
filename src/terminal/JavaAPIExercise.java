package terminal;

import javax.swing.JFrame;

import api.SessionStarter;
import panels.MainPanel;

public class JavaAPIExercise 
{
	public static void main(String[] args)
	{
		CallHandler.handleArguments(args);
		SessionStarter sessionStarter = new SessionStarter();
		JFrame terminalFrame = new JFrame("SSC Exercise 3");
		terminalFrame.setResizable(false);
		terminalFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		terminalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		terminalFrame.setUndecorated(true);
		MainPanel mainPanel = new MainPanel(sessionStarter);
		terminalFrame.add(mainPanel);
		terminalFrame.setVisible(true);
		LoadKeyboard.loadEscapeButton();
	}

}
