package pixelyvulpine.api.system;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.StringItem;
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
	
	private static boolean crash;
	private Command close, report, reportListSelect, FileReport, ViewLog, returnToReportAction;
	private Command[] reportListCommands;
	private MIDlet midlet;
	private Throwable e;
	private String message;
	private byte CrashType;
	private Form display;
	private StringItem messageDisplay;
	private Thread thread;
	private List reportList;
	private StringBuffer log;
	private Screen current;
	
	private Crash(MIDlet midlet, Throwable e, String message, byte CrashType) {
		close = new Command("Close", Command.EXIT, 0);
		report = new Command("Report", Command.OK, 0);
		this.midlet=midlet;
		this.e = e;
		this.message = message;
		this.CrashType = CrashType;
		thread=Thread.currentThread();
		
		StringBuffer disM = new StringBuffer();
		
		try {
		if(e!=null) {
				disM.append(e.toString());
				disM.append(" (");
				disM.append(e.hashCode());
				disM.append(")\n");
				disM.append(e.getMessage());
				disM.append("\n\n");
			}
		}catch(Throwable t) {}
		disM.append(message);
		disM.append("\n\nDevice: ");
		try {
			disM.append(System.getProperty("microedition.platform"));
		}catch(NullPointerException e2) {
			disM.append("Unknown");
		}
		disM.append("\nFramework version: ");
		disM.append(Config.framework_version);
		disM.append(" (");
		disM.append(Config.framework_version_tag);
		disM.append(")\nMIDlet version: ");
		disM.append(midlet.getAppProperty("MIDlet-Version"));
		disM.append("\nMIDlet name: ");
		disM.append(midlet.getAppProperty("MIDlet-Name"));
		disM.append("\n\n");
		disM.append(reportMessage[CrashType]);
		disM.append("\n");
		
		messageDisplay = new StringItem("", disM.toString());
		display = new Form(titles[CrashType]);
		display.append(messageDisplay);
		display.setCommandListener(this);
		display.addCommand(close);
		display.addCommand(report);
		
		try {
			Alert alert = new Alert(titles[CrashType], disM.toString(), null, AlertType.ERROR);
			alert.getType().playSound(Display.getDisplay(midlet));
		}catch(Throwable e2) {}
			
		setCurrent(display);
	}
	
	public static void showCrashMessage(MIDlet midlet, Throwable e, String message) {
		showCrashMessage(midlet, e, message, APPLICATION_CRASH);
	}
	
	public static void showCrashMessage(MIDlet midlet, Throwable e, String message, byte CrashType) {
		crash=true;
		e.printStackTrace();
		new Crash(midlet, e, message, CrashType);
	}
	
	public static boolean hasCrashed() {
		return crash;
	}
	
	private void generateLog(StringBuffer sb) {
		
		sb.append(titles[CrashType]);
		sb.append("\n\n");
		if(e!=null) {
			sb.append(e.toString());
			sb.append(" (");
			sb.append(e.hashCode());
			sb.append(")\n");
			sb.append(e.getMessage());
			sb.append("\n\n");
		}
		sb.append(message);
		sb.append("\n\nDevice: ");
		try {
			sb.append(System.getProperty("microedition.platform"));
		}catch(NullPointerException e2) {
			sb.append("Unknown");
		}
		sb.append("\nTotal heap size: ");
		sb.append(Runtime.getRuntime().totalMemory());
		sb.append(" Bytes\nConfiguration: ");
		sb.append(System.getProperty("microedition.configuration"));
		sb.append("\nProfile: ");
		sb.append(System.getProperty("microedition.profiles"));
		sb.append("\nFramework version: ");
		sb.append(Config.framework_version);
		sb.append(" (");
		sb.append(Config.framework_version_tag);
		sb.append(")\nMIDlet version: ");
		sb.append(midlet.getAppProperty("MIDlet-Version"));
		sb.append("\nMIDlet name: ");
		sb.append(midlet.getAppProperty("MIDlet-Name"));
		
		Debug.getThreadTrace(sb, thread);
		sb.append(" <~~");
		try { 
			sb.append(e.getClass().getName().substring(e.getClass().getName().lastIndexOf('.')+1, e.getClass().getName().length()));
		}catch(Throwable t) {
			sb.append(e.toString());
		}
		
		
		sb.append("\n\nLog:");
		Debug.getTraceLog(sb, thread);
		sb.append("\n\n");
		Debug.watchLastTrace(sb, thread);
	}
	
	private void showReportListChoice() {
		reportList = new List("Report "+titles[CrashType], List.IMPLICIT);
		FileReport = new Command("Save log", Command.ITEM, 0);
		reportListSelect = new Command("", Command.ITEM, 0);
		reportList.setSelectCommand(reportListSelect);
		reportList.setCommandListener(this);
		ViewLog = new Command("View log", Command.ITEM, 0);
		reportList.append("Save log", null);
		reportList.append("View log", null);
		reportListCommands = new Command[] {FileReport, ViewLog};
		returnToReportAction = new Command("Return", Command.BACK, 0);
		setCurrent(reportList);
		reportList.addCommand(close);
	}
	
	private void setCurrent(Screen current) {
		Display.getDisplay(midlet).setCurrent(current);
		this.current=current;
	}

	public void commandAction(Command arg0, Displayable arg1) {
		
		if(arg0==close) {
			midlet.notifyDestroyed();
			return;
		}
		
		if(arg0==report) {
			
			messageDisplay.setText("Generating logs...\nThis may take some few minutes, please wait...");
			display.removeCommand(close);
			display.removeCommand(report);
			
			try {
				log = new StringBuffer();
				generateLog(log);
				//messageDisplay.setText(log.toString()); //TODO: Create file/Internet upload
				
			}catch(Throwable e) {
				messageDisplay.setText("We're sorry, there was an unexpected error generating logs\n"+e.toString()+": "+e.getMessage());
				display.addCommand(close);
				return;
			}
			
			showReportListChoice();
			return;
		}
		
		if(arg0==reportListSelect) {
			commandAction(reportListCommands[reportList.getSelectedIndex()], arg1);
			return;
		}
		
		if(arg0==returnToReportAction) {
			current.removeCommand(returnToReportAction);
			showReportListChoice();
			return;
		}
		
		if(arg0==ViewLog) {
			messageDisplay.setText(log.toString());
			setCurrent(display);
			current.addCommand(returnToReportAction);
		}
		
	}

}
