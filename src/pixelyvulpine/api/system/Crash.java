package pixelyvulpine.api.system;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.Config;

public class Crash implements CommandListener{
	
	public static byte FRAMEWORK_CRASH=0;
	public static byte APPLICATION_CRASH=1;
	
	private static String titles[]= {
		"Framework Crash",
		"Application Crash"
	};
	
	private static String reportMessage[]= {
		"Please report this issue at github.com/JoseskVolpe/PixelyVulpine-Layout/issues with your device number and description of the problem",
		"Please contact the developer"
	};
	
	private Command exitCommand;
	private MIDlet midlet;
	
	public Crash(MIDlet midlet, Command exitCommand) {
		this.exitCommand = exitCommand;
		this.midlet=midlet;
	}
	
	public static void showCrashMessage(MIDlet midlet, Throwable e, String message) {
		showCrashMessage(midlet, e, message, APPLICATION_CRASH);
	}
	
	public static void showCrashMessage(MIDlet midlet, Throwable e, String message, byte CrashType) {
		
		e.printStackTrace();
		
		Command exit = new Command("Close", Command.EXIT, 0);
		
		Crash crash = new Crash(midlet, exit);
		
		String error="";
		if(e!=null) {
			error=e.toString()+" ("+e.hashCode()+")\n"+e.getMessage()+"\n\n";
		}
		
		String device=null;
		try {
			device=System.getProperty("microedition.platform");
		}catch(NullPointerException e2) {
			device="Unknown";
		}
		
		String framework_version = Config.framework_version+" ("+Config.framework_version_tag+")";
		
		Alert log = new Alert(titles[CrashType], error+"Device: "+device+"\nFramework version: "+framework_version+"\nMIDlet version: "+midlet.getAppProperty("MIDlet-Version")+"\n\n"+message+"\n\n"+reportMessage[CrashType], null, AlertType.ERROR);
		log.setTimeout(Alert.FOREVER);
		log.setCommandListener(crash);
		log.addCommand(exit);
		
		Display.getDisplay(midlet).setCurrent(log);
		
	}

	public void commandAction(Command arg0, Displayable arg1) {
		
		if(arg0==exitCommand) {
			midlet.notifyDestroyed();
		}
		
	}

}
