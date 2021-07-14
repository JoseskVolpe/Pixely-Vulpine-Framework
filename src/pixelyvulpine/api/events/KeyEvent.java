package pixelyvulpine.api.events;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;

import pixelyvulpine.Config;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.CSVReader;

public class KeyEvent extends InputEvent{
	

	public final static int ACTION_DOWN=0;
	public final static int ACTION_REPEAT=1;
	public final static int ACTION_UP = 2;
	
	public final static int KEYCODE_0 = Canvas.KEY_NUM0;
	public final static int KEYCODE_1 = Canvas.KEY_NUM1;
	public final static int KEYCODE_2 = Canvas.KEY_NUM2;
	public final static int KEYCODE_3 = Canvas.KEY_NUM3;
	public final static int KEYCODE_4 = Canvas.KEY_NUM4;
	public final static int KEYCODE_5 = Canvas.KEY_NUM5;
	public final static int KEYCODE_6 = Canvas.KEY_NUM6;
	public final static int KEYCODE_7 = Canvas.KEY_NUM7;
	public final static int KEYCODE_8 = Canvas.KEY_NUM8;
	public final static int KEYCODE_9 = Canvas.KEY_NUM9;
	public final static int KEYCODE_STAR = Canvas.KEY_STAR;
	public final static int KEYCODE_POUND = Canvas.KEY_POUND;
	public final static int KEYCODE_DPAD_UP = 19;
	public final static int KEYCODE_DPAD_LEFT = 21;
	public final static int KEYCODE_DPAD_RIGHT = 22;
	public final static int KEYCODE_DPAD_DOWN = 20;
	public final static int KEYCODE_DPAD_CENTER = Canvas.FIRE;
	public final static int KEYCODE_BUTTON_A = Canvas.GAME_A;
	public final static int KEYCODE_BUTTON_B = Canvas.GAME_B;
	public final static int KEYCODE_BUTTON_C = Canvas.GAME_C;
	public final static int KEYCODE_BUTTON_D = Canvas.GAME_D;
	/**
	 * @deprecated Avoid using this, most phones can't detect this keycode
	 * Keycode used for green call key
	 * This will NOT be removed in future releases
	 */
	public final static int KEYCODE_CALL = -10;
	/**
	 * @deprecated Avoid using it, most phones can't detect this keycode
	 * Keycode used for red end-call key
	 * This will NOT be removed in future releases
	 */
	public final static int KEYCODE_ENDCALL = -11;
	public final static int KEYCODE_DEL = -8;
	public final static int KEYCODE_SOFT_LEFT=-6;
	public final static int KEYCODE_SOFT_RIGHT=-7;
	
	private static ConvertableCode convertableCodes[] = {
		new ConvertableCode(8,KEYCODE_DEL)
	};
	
	/**@deprecated*/private static int[] KEYCODE_LEFT_KC;
	/**@deprecated*/private static int[] KEYCODE_RIGHT_KC;
	
	public static interface Callback{
		public abstract boolean onKeyDown(int keyCode, KeyEvent event);
		public abstract boolean onKeyRepeat(int keyCode, KeyEvent event);
		public abstract boolean onKeyUp(int keyCode, KeyEvent event);
	}

	
	private int action, runtimeCode, code;
	private Layout context;
	
	public KeyEvent (Layout context, int action, int runtimeCode) {
		super();
		
		if(KEYCODE_LEFT_KC==null || KEYCODE_RIGHT_KC==null || KEYCODE_LEFT_KC.length<=0 || KEYCODE_RIGHT_KC.length<=0) detectSoftKeycodes(context);
		
		this.action=action;
		this.runtimeCode=runtimeCode;
		this.context=context;
		this.code = convertKeycode(runtimeCode);
	}
	
	public boolean dispatch(Callback receiver) {
		switch(action) {
			case ACTION_DOWN:
				return receiver.onKeyDown(code, this);
			case ACTION_REPEAT:
				return receiver.onKeyRepeat(code, this);
			case ACTION_UP:
				return receiver.onKeyUp(code, this);
		}
		
		return false;
	}
	
	public int getAction() {
		return action;
	}
	
	public int getKeycode() {
		return code;
	}
	
	public int getRuntimeKeycode() {
		return runtimeCode;
	}
	
	public String getKeyName() {
		
		switch(code) {
			case KEYCODE_SOFT_LEFT:
				return "SOFT1";
			case KEYCODE_SOFT_RIGHT:
				return "SOFT2";
			case KEYCODE_CALL:
				return "SEND";
			case KEYCODE_ENDCALL:
				return "END";
			case KEYCODE_DPAD_LEFT:
				return "LEFT";
			case KEYCODE_DPAD_UP:
				return "UP";
			case KEYCODE_DPAD_RIGHT:
				return "RIGHT";
			case KEYCODE_DPAD_DOWN:
				return "DOWN";
			default:
				return context.getKeyName(code);
		}
	}
	
	public static String actionToString(int action) {
		switch(action) {
			case ACTION_DOWN:
				return "ACTION_DOWN";
			case ACTION_REPEAT:
				return "ACTION_REPEAT";
			case ACTION_UP:
				return "ACTION_UP";
		}
		
		return null;
	}
	
	public int getGameAction() {
		return context.getGameAction(code);
	}
	
	public int getUnicodeChar() {
		return getChar();
	}
	
	public char getChar() {
		switch(code) {
			case KEYCODE_0:
				return '0';
			case KEYCODE_1:
				return '1';
			case KEYCODE_2:
				return '2';
			case KEYCODE_3:
				return '3';
			case KEYCODE_4:
				return '4';
			case KEYCODE_5:
				return '5';
			case KEYCODE_6:
				return '6';
			case KEYCODE_7:
				return '7';
			case KEYCODE_8:
				return '8';
			case KEYCODE_9:
				return '9';
			default:
				return (char)code;
		}
	}
	
	/**@deprecated*/
	public static int[] getLeftSoftKeycodes() {
		return KEYCODE_LEFT_KC;
	}
	
	/**@deprecated*/
	public static int[] getRightSoftKeycodes() {
		return KEYCODE_RIGHT_KC;
	}
	
	/**@deprecated*/
	public static void setSoftKeycodes(int leftsoft, int rightsoft) {
		KEYCODE_LEFT_KC = new int[] {leftsoft};
		KEYCODE_RIGHT_KC = new int[] {rightsoft};
	}
	
	private int convertKeycode(int runtimeCode) {
		/*
		for(int i=0; i<KEYCODE_LEFT_KC.length; i++) {
			if(runtimeCode==KEYCODE_LEFT_KC[i])
				return KEYCODE_SOFT_LEFT;
			
			if(runtimeCode==KEYCODE_RIGHT_KC[i])
				return KEYCODE_SOFT_RIGHT;
		}*/
		
		try {
			String name = context.getKeyName(runtimeCode).toUpperCase();
			if(name.equals("SOFT1"))
				return KEYCODE_SOFT_LEFT;
			
			if(name.equals("SOFT2"))
				return KEYCODE_SOFT_RIGHT;
			
			if(name.equals("SEND"))
				return KEYCODE_CALL;
			
			if(name.equals("END"))
				return KEYCODE_ENDCALL;
			
			if(name.equals("LEFT"))
				return KEYCODE_DPAD_LEFT;
			
			if(name.equals("UP"))
				return KEYCODE_DPAD_UP;
			
			if(name.equals("RIGHT"))
				return KEYCODE_DPAD_RIGHT;
			
			if(name.equals("DOWN"))
				return KEYCODE_DPAD_DOWN;
		}catch(Throwable e) {}
		
		for(int i=0; i<convertableCodes.length; i++) {
			if(runtimeCode==convertableCodes[i].from)
				return convertableCodes[i].to;
		}
		
		return runtimeCode;
		
	}
	
	/**@deprecated*/
	private static void detectSoftKeycodes(Layout context) {
		
		try {
			
			try {
				String lskc = context.getMIDlet().getAppProperty("PixelyVulpineFramework.leftsoftkeycode");
				String rskc = context.getMIDlet().getAppProperty("PixelyVulpineFramework.rightsoftkeycode");
				
				if(lskc==null || rskc==null || lskc.equals(null) || rskc.equals(null))
					throw new NullPointerException();
				
				KEYCODE_LEFT_KC = new int[] {Integer.parseInt(lskc)};
				KEYCODE_RIGHT_KC = new int[] {Integer.parseInt(rskc)};
				
			}catch(Exception invalidProperty) {
				
				Vector lskc = new Vector(0,1);
				Vector rskc = new Vector(0,1);
				
				String TempBrand = Config.getDeviceBrand().toLowerCase();
				
				CSVReader csv = CSVReader.read(context.getClass().getResourceAsStream("/pixelyvulpine/SoftKeycodes.csv"));
				
				if(!TempBrand.equals("unknown"))
					for(int i=0; i<csv.getRowsLength(); i++) {
						
						if(csv.getValue("Brand", i).toLowerCase().equals(TempBrand))
							break;
						
						if(i>=csv.getRowsLength()-1) {
							TempBrand = "unknown";
							break;
						}
					}
				
				
				
				String platform = System.getProperty("microedition.platform").toLowerCase();
				int i=platform.indexOf("/");
				if(i<0) {
					i=platform.indexOf("\\");
				}
				
				if(i>0)
					platform=platform.substring(0, i);
				
				detectSoftKeycodesRow(csv, lskc, rskc, TempBrand, platform);
				
				if(rskc.isEmpty() || lskc.isEmpty()) {
					platform=platform.replace('0', '#');
					platform=platform.replace('1', '#');
					platform=platform.replace('2', '#');
					platform=platform.replace('3', '#');
					platform=platform.replace('4', '#');
					platform=platform.replace('5', '#');
					platform=platform.replace('6', '#');
					platform=platform.replace('7', '#');
					platform=platform.replace('8', '#');
					platform=platform.replace('9', '#');
					
					detectSoftKeycodesRow(csv, lskc, rskc, TempBrand, platform);
					
					if(rskc.isEmpty() || lskc.isEmpty()) {
						platform="unknown";
						detectSoftKeycodesRow(csv, lskc, rskc, TempBrand, platform);
					}
					
				}
				
				KEYCODE_LEFT_KC = new int[lskc.size()];
				KEYCODE_RIGHT_KC = new int[rskc.size()];
				
				for(i=0; i<lskc.size();i++) {
					KEYCODE_LEFT_KC[i] = ((Integer)lskc.elementAt(i)).intValue();
					KEYCODE_RIGHT_KC[i] = ((Integer)rskc.elementAt(i)).intValue();
				}
				
				csv.erase();
				csv=null;
				
			}
			
		}catch(Throwable DetectException) {
			KEYCODE_LEFT_KC = new int[] {KEYCODE_SOFT_LEFT};
			KEYCODE_RIGHT_KC = new int[] {KEYCODE_SOFT_RIGHT};
		}
	}
	
	/**@deprecated*/
	private static void detectSoftKeycodesRow(CSVReader csv, Vector lskc, Vector rskc, String TempBrand, String platform) {
		//System.out.println("Searching for "+TempBrand+" "+platform);
		
		for(int i=0; i<csv.getRowsLength(); i++) {
			if(
				(csv.getValue("Brand", i).toLowerCase().equals(TempBrand) && csv.getValue("Microedition.platform", i).toLowerCase().equals(platform)) ||
				(csv.getValue("Microedition.platform", i).toLowerCase().equals(platform) && !platform.equals("unknown") && TempBrand=="unknown")
			) {
				lskc.addElement(Integer.valueOf(csv.getValue("Left soft keycode", i)));
				rskc.addElement(Integer.valueOf(csv.getValue("Right soft keycode", i)));
				
				//System.out.println("Found: " + ((Integer)lskc.lastElement()) +" "+((Integer)rskc.lastElement()));
				
			}
		}
	}
	
	private static class ConvertableCode{
		
		public int from, to;
		
		public ConvertableCode(int from, int to) {
			this.from = from;
			this.to = to;
		}
	}
	
}
