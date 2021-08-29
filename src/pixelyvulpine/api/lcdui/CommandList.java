package pixelyvulpine.api.lcdui;

import java.util.Vector;

import javax.microedition.lcdui.Command;

public class CommandList {
	
	public final static int PRIORITY_MAIN_COMMANDS=Integer.MAX_VALUE;
	public final static int PRIORITY_VIEW = 0;
	public final static int PRIORITY_POPUP = -10;
	
	public final static byte EXCLUSIVE_DEFAULT=0; /**Default exclusivity (EXCLUSIVE_INCLUSIVE*/
	public final static byte EXLUSIVE_INCLUSIVE=0; /**Join command list with everyone*/
	public final static byte EXCLUSIVE_STOPPABLE=1; /**Ignore higher priority command lists*/
	public final static byte EXCLUSIVE_IGNORABLE=2; /**Ignore command list if overlay by lower priority command lists*/
	
	private int priority;
	private byte exclusive = EXCLUSIVE_DEFAULT;
	private Layout context;
	private Vector commands = new Vector();
	
	/**
	 * 
	 * @param priority - Lower priority comes first
	 */
	public CommandList(int priority) {
		this.priority=priority;
	}
	
	public final void addCommand(Command command) {
		commands.addElement(command);
		if(context!=null)
			context.updateCommands();
			
	}

	public final void removeCommand(Command command) {
		commands.removeElement(command);
		if(context!=null)
			context.updateCommands();
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
	
	public final void setExclusive(byte exclusive) {
		this.exclusive=exclusive;
		if(getContext()!=null)
			getContext().updateCommands();
	}
	
	public final byte getExclusive() {
		return exclusive;
	}
	
	public final void assembleContext(Layout context) {
		this.context=context;
	}
	
	protected final Layout getContext() {
		return context;
	}

}
