package pixelyvulpine.api.lcdui;

import java.util.Vector;

import javax.microedition.lcdui.Command;

public class CommandList {
	
	public final static int PRIORITY_MAIN_COMMANDS=Integer.MAX_VALUE; /**Application commands added on Context*/
	public final static int PRIORITY_APPLICATION = 0; /**Barrier between framework and application*/
	public final static int PRIORITY_VIEW = -1; /**View command list*/
	public final static int PRIORITY_POPUP = -10; /**Pop-up command list*/
	
	public final static byte EXCLUSIVE_DEFAULT=0; /**Default exclusivity (EXCLUSIVE_INCLUSIVE)*/
	public final static byte EXLUSIVE_INCLUSIVE=0; /**Join command list with everyone if not stopped*/
	public final static byte EXCLUSIVE_STOPPABLE=1; /**Ignore higher priority command lists if applied*/
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
	
	public final void setExclusive(int exclusive) {
		this.exclusive=(byte)exclusive;
		if(getContext()!=null)
			getContext().updateCommands();
	}
	
	public final int getExclusive() {
		return exclusive;
	}
	
	public final void assembleContext(Layout context) {
		this.context=context;
	}
	
	protected final Layout getContext() {
		return context;
	}

}
