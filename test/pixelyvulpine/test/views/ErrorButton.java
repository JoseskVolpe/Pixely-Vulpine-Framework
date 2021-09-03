package pixelyvulpine.test.views;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.contents.Button;
import pixelyvulpine.api.lcdui.Command;

public class ErrorButton extends Button implements CommandListener{

	private Command crash = new Command("Crash", Command.ITEM, 0);
	
	public ErrorButton(Layout context) {
		super(context, new Button.ButtonPadding(0, 0, 0, 0), "Force crash");
		impact();
		crash.setCommandListenerBypass(this);
		this.setClickCommand(crash);
	}

	public void commandAction(javax.microedition.lcdui.Command arg0, Displayable arg1) {
		
		if(arg0==crash) {
			Crash.showCrashMessage(getLayout().getMIDlet(),new Exception(), "Forced crash", Crash.APPLICATION_CRASH);
		}
		
	}
	
	

}
