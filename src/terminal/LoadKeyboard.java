package terminal;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
public class LoadKeyboard {
	public static void loadEscapeButton()
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(new KeyEventDispatcher() {
		      @Override
		      public boolean dispatchKeyEvent(KeyEvent e) {

		    	if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		    	{
		    		int exitOptionPane = JOptionPane.YES_NO_OPTION;
		    		int answer = JOptionPane.showConfirmDialog (null, 
		    				"Would you like to close this program?",
		    				"Please don't close me :'(",exitOptionPane);
		    		if(answer == JOptionPane.YES_OPTION)
		    			{
		    				System.exit(CallHandler.EC_SUCCESS);
		    			}
		    	}
		    	return false;
		      }
		});
	}
}
