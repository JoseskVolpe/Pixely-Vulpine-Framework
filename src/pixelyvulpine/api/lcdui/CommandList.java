package pixelyvulpine.api.lcdui;

import java.util.Vector;

import javax.microedition.lcdui.Command;

public class CommandList {
	
	public final static int PRIORITY_MAIN_COMMANDS=Integer.MAX_VALUE;
	public final static int PRIORITY_VIEW = 0;
	public final static int PRIORITY_POPUP = -10;
	
	private int priority;
	private Layout context;
	private Vector commands = new Vector();
	
	public CommandList(int priority) {
		this.priority=priority;
	}
	
	public final void addCommand(Command command) {
		commands.addElement(command);
		if(context!=null)
			context.updateCommands(this);
			
	}

	public final void removeCommand(Command command) {
		commands.removeElement(command);
		if(context!=null)
			context.updateCommands(this);
	}
	
	public final int size() {
		return commands.size();
	}
	
	public final Command getCommand(int index) {
		return (Command)commands.elementAt(index);
	}
	
	public final int getPriority() {
		return priority;
	}
	
	public final void assembleContext(Layout context) {
		this.context=context;
	}
	
	protected final Layout getContext() {
		return context;
	}

}
