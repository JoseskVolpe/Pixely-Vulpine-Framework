package pixelyvulpine.api.system;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.Config;
import pixelyvulpine.api.lcdui.Debug;

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
		StringBuffer sb = new StringBuffer();
		
		if(e!=null) {
			sb.append(e.toString());
			sb.append(" (");
			sb.append(e.hashCode());
			sb.append(")\n");
			sb.append(e.getMessage());
			sb.append("\n\n\n");
		}
		sb.append("Device: ");
		try {
			sb.append(System.getProperty("microedition.platform"));
		}catch(NullPointerException e2) {
			sb.append("Unknown");
		}
		sb.append("\nFramework version: ");
		sb.append(Config.framework_version);
		sb.append(" (");
		sb.append(Config.framework_version_tag);
		sb.append(")\nMIDlet version: ");
		sb.append(midlet.getAppProperty("MIDlet-Version"));
		sb.append("\n\n");
		sb.append(message);
		sb.append("\n\n");
		sb.append(reportMessage[CrashType]);
		sb.append("\n");
		
		Alert log = new Alert(titles[CrashType], sb.toString(), null, AlertType.ERROR);
		log.setTimeout(Alert.FOREVER);
		log.setCommandListener(crash);
		log.addCommand(exit);
		
		Display.getDisplay(midlet).setCurrent(log);
		
		Debug.getThreadTrace(sb);
		sb.append(" <~~");
		try { 
			sb.append(e.getClass().getName().substring(e.getClass().getName().lastIndexOf('.')+1, e.getClass().getName().length()));
		}catch(Throwable t) {
			sb.append(e.toString());
		}
		
		
		sb.append("\n\nLog:");
		Debug.getTraceLog(sb);
		sb.append("\n\n");
		Debug.watchLastTrace(sb);
		System.err.println(sb.toString());
		
	}

	public void commandAction(Command arg0, Displayable arg1) {
		
		if(arg0==exitCommand) {
			midlet.notifyDestroyed();
		}
		
	}

}
