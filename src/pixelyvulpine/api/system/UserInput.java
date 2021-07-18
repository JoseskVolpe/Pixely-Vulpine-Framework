package pixelyvulpine.api.system;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Layout;

public final class UserInput {
	
	public static interface InputListener{
		public void onInputConfirmed(String output);
		public void onInputCanceled(String input);
	}
	
	private static Displayable lastContext;
	private static MIDlet lastMIDlet;
	private static boolean shown=false;
	
	public static void showDecimalInput(Layout context, InputListener inputListener, float number) {
		showInput(context, inputListener, String.valueOf(number), 1024, TextField.DECIMAL);
	}
	
	public static void showDecimalInput(MIDlet midlet, Displayable context, InputListener inputListener, float number) {
		showInput(midlet, context, inputListener, String.valueOf(number), 1024, TextField.DECIMAL);
	}
	
	public static void showNumericInput(Layout context, InputListener inputListener, long number) {
		showInput(context, inputListener, String.valueOf(number), 255, TextField.NUMERIC);
	}
	
	public static void showNumericInput(MIDlet midlet, Displayable context, InputListener inputListener, long number) {
		showInput(midlet, context, inputListener, String.valueOf(number), 255, TextField.NUMERIC);
	}
	
	public static void showTextInput(Layout context, InputListener inputListener, String text) {
		showTextInput(context.getMIDlet(), context, inputListener, text);
	}
	
	public static void showTextInput(MIDlet midlet, Displayable context, InputListener inputListener, String text) {
		int max = 2050;
		if(text.length()>max) max=text.length()*2;
		
		showInput(midlet, context, inputListener, text, max, TextField.ANY);
	}
	
	public static void showTextInput(Layout context, InputListener inputListener, String text, int maxSize) {
		showInput(context, inputListener, text, maxSize, TextField.ANY);
	}
	
	public static void showTextInput(MIDlet midlet, Displayable context, InputListener inputListener, String text, int maxSize) {
		showInput(midlet, context, inputListener, text, maxSize, TextField.ANY);
	}
	
	public static void showPasswordInput(Layout context, InputListener inputListener, String text) {
		
		showPasswordInput(context.getMIDlet(), context, inputListener, text);
	}
	
	public static void showPasswordInput(MIDlet midlet, Displayable context, InputListener inputListener, String text) {
		
		int max = 2050;
		if(text.length()>max) max=text.length()*2;
		
		showInput(midlet, context, inputListener, text, max, TextField.PASSWORD);
	}
	
	public static void showPasswordInput(Layout context, InputListener inputListener, String text, int maxSize) {
		showInput(context, inputListener, text, maxSize, TextField.PASSWORD);
	}
	
	public static void showPasswordInput(MIDlet midlet, Displayable context, InputListener inputListener, String text, int maxSize) {
		showInput(midlet, context, inputListener, text, maxSize, TextField.PASSWORD);
	}
	
	public static void showEmailInput(Layout context, InputListener inputListener, String text) {
		showInput(context, inputListener, text, 320, TextField.EMAILADDR);
	}
	
	public static void showEmailInput(MIDlet midlet, Displayable context, InputListener inputListener, String text) {
		showInput(midlet, context, inputListener, text, 320, TextField.EMAILADDR);
	}
	
	public static void showInput(Layout context, InputListener inputListener, String text, int maxSize, int constraint) {
		
		showInput(context.getMIDlet(), context, inputListener, text, maxSize, constraint);
		
	}
	
	public static void showInput(MIDlet midlet, Displayable context, InputListener inputListener, String text, int maxSize, int constraint) {
		Listener l = new Listener(inputListener, text);
		
		TextBox field = new TextBox("", text, maxSize, constraint);
		field.addCommand(l.Ok);
		field.addCommand(l.Cancel);
		field.setCommandListener(l);
		
		l.setField(field);
		
		lastContext = context;
		lastMIDlet = midlet;
		shown=true;
		
		Display.getDisplay(midlet).setCurrent(field);
	}
	
	public static void hideInput() {
		hideInput(lastMIDlet, lastContext);
	}
	
	public static void hideInput(Layout context) {
		hideInput(context.getMIDlet(), context);
	}
	
	public static void hideInput(MIDlet midlet, Displayable displayable) {
		
		if(!shown) return;
		
		shown=false;
		Display.getDisplay(midlet).setCurrent(displayable);
	}
	
	private static class Listener implements CommandListener{

		public Command
		Ok = new Command("Ok", Command.OK, 1),
		Cancel = new Command("Cancel", Command.CANCEL, 1);
		
		private InputListener inputListener;
		private TextBox field;
		private String input;
		
		public Listener(InputListener inputListener, String input) {
			this.inputListener = inputListener;
			this.input = input;
		}
		
		protected void setField(TextBox field) {
			this.field = field;
		}
		
		public void commandAction(Command arg0, Displayable arg1) {
			
			if(arg0==Ok) {
				inputListener.onInputConfirmed(field.getString());
				hideInput();
			}
			
			if(arg0==Cancel) {
				inputListener.onInputCanceled(input);
				hideInput();
			}
			
		}
		
	}

}
