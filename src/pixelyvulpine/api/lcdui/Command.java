package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Image;

public class Command extends javax.microedition.lcdui.Command{

	private Image icon;
	private Content view;
	private CommandListener listener;
	
	public Command(String label, int commandType, int priority) {
		super(label, commandType, priority);
		
		icon=null;
		
	}
	
	public Command(String label, Image icon, int commandType, int priority) {
		super(label, commandType, priority);
		
		this.icon = icon;
	}
	
	public void setIcon(Image icon) {
		this.icon=icon;
	}
	
	public Image getIcon() {
		return icon;
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

}
