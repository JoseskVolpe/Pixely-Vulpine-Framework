package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Image;

public class Command extends javax.microedition.lcdui.Command{

	public static final int CENTER = 0;
	
	private Image icon;
	private Content view;
	private CommandListener listener;
	private int commandType;
	private boolean symbolic;
	
	public Command(String label, int commandType, int priority) {
		super(label, getLegacyCommandType(commandType), priority);
		
		this.commandType=commandType;
		icon=null;
		
	}
	
	public Command(String label, Image icon, int commandType, int priority) {
		this(label, commandType, priority);
		
		this.icon = icon;
	}
	
	private static int getLegacyCommandType(int commandType) {
		if(commandType==CENTER)
			return Command.OK;
		else
			return commandType;
	}
	
	public void setIcon(Image icon) {
		this.icon=icon;
	}
	
	public Image getIcon() {
		return icon;
	}
	
	public int getCommandType() {
		return commandType;
	}
	
	public final void setView(Content view) {
		this.view=view;
	}
	
	public final Content getView() {
		return view;
	}
	
	public final void setCommandListenerBypass(CommandListener listener) {
		this.listener=listener;
	}
	
	public final CommandListener getCommandListenerBypass() {
		return listener;
	}

	public final void setSymbolic(boolean symbolic) {
		this.symbolic=symbolic;
	}
	
	public final boolean isSymbolic() {
		return symbolic; 
	}
	
}
