package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.Image;

public class SymbolicCommand extends Command{

	public SymbolicCommand(String label, int commandType, int priority) {
		this(label, null, commandType, priority);
	}
	
	public SymbolicCommand(String label, Image icon, int commandType, int priority) {
		super(label, icon, commandType, priority);
		setSymbolic(true);
	}

}
