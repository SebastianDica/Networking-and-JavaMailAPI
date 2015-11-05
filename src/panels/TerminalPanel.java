package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class TerminalPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea mainArea;
	public TerminalPanel()
	{
		super(new GridBagLayout());
		setBackground(new Color(190,190,190));
		mainArea = new JTextArea();
		mainArea.setEditable(false);
       	mainArea.setBackground(new Color(190,190,190));
       	mainArea.setForeground(Color.BLACK);
       	mainArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
		JScrollPane scrollPane = new JScrollPane(mainArea);
        GridBagConstraints constraints = new GridBagConstraints();
       	constraints.gridwidth = GridBagConstraints.REMAINDER;
       	constraints.fill = GridBagConstraints.HORIZONTAL;
      	constraints.fill = GridBagConstraints.BOTH;
       	constraints.weightx = 1.0;
       	constraints.weighty = 1.0;
       	((DefaultCaret) mainArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
       	add(scrollPane, constraints);
	}
	public void appendMessage(String message)
	{
		mainArea.append(message + "\n");
	}
	@Override
	public void update(Observable arg0, Object arg1) {
	}
}
