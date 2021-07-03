package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.Image;

public class Command extends javax.microedition.lcdui.Command{

	private Image icon;
	
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

}
