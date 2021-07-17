package pixelyvulpine.api.system;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

import pixelyvulpine.api.lcdui.Layout;

public final class TextInput {
	
	public static interface InputListener{
		public void onInputConfirmed(String output);
		public void onInputCanceled(String input);
	}
	
	public static void showTextInput(Layout context, InputListener inputListener, boolean multiline, String text, int maxSize) {
		showInput(context, inputListener, multiline, text, maxSize, TextField.ANY);
	}
	
	public static void showInput(Layout context, InputListener inputListener, boolean multiline, String text, int maxSize, int constraint) {
		
		Listener l = new Listener(context, inputListener, text);
		
		TextBox field = new TextBox("", text, maxSize, constraint);
		field.addCommand(l.Ok);
		field.addCommand(l.Cancel);
		field.setCommandListener(l);
		
		l.setField(field);
		
		Display.getDisplay(context.getMIDlet()).setCurrent(field);
	}
	
	private static class Listener implements CommandListener{

		public Command
		Ok = new Command("Ok", Command.OK, 1),
		Cancel = new Command("Cancel", Command.CANCEL, 1);
		
		private Layout context;
		private InputListener inputListener;
		private TextBox field;
		private String input;
		
		public Listener(Layout context, InputListener inputListener, String input) {
			this.context = context;
			this.inputListener = inputListener;
			this.input = input;
		}
		
		protected void setField(TextBox field) {
			this.field = field;
		}
		
		public void commandAction(Command arg0, Displayable arg1) {
			
			if(arg0==Ok) {
				Display.getDisplay(context.getMIDlet()).setCurrent(context);
				inputListener.onInputConfirmed(field.getString());
			}
			
			if(arg0==Cancel) {
				Display.getDisplay(context.getMIDlet()).setCurrent(context);
				inputListener.onInputCanceled(input);
			}
			
		}
		
	}

}
