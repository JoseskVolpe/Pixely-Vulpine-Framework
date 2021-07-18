package pixelyvulpine.api.events;

import javax.microedition.lcdui.Canvas;
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
	public final static int KEYCODE_ENTER = 10;
	public final static int KEYCODE_DPAD_UP = 19;
	public final static int KEYCODE_DPAD_LEFT = 21;
	public final static int KEYCODE_DPAD_RIGHT = 22;
	public final static int KEYCODE_DPAD_DOWN = 20;
	public final static int KEYCODE_DPAD_CENTER = 23;
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
	
	private static int KEYCODE_LEFT_KC;
	private static int KEYCODE_RIGHT_KC;
	
	public static interface Callback{
		public abstract boolean onKeyDown(int keyCode, KeyEvent event);
		public abstract boolean onKeyRepeat(int keyCode, KeyEvent event);
		public abstract boolean onKeyUp(int keyCode, KeyEvent event);
	}

	
	private int action, runtimeCode, code;
	private Layout context;
	
	public KeyEvent (Layout context, int action, int runtimeCode) {
		super();
		
		if(KEYCODE_LEFT_KC==KEYCODE_RIGHT_KC) detectSoftKeycodes(context);
		
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
			case KEYCODE_DPAD_CENTER:
				return "FIRE";
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
	
	
	public static int getLeftSoftKeycodes() {
		return KEYCODE_LEFT_KC;
	}
	
	public static int getRightSoftKeycodes() {
		return KEYCODE_RIGHT_KC;
	}
	
	public static void setSoftKeycodes(int leftsoft, int rightsoft) {
		KEYCODE_LEFT_KC = leftsoft;
		KEYCODE_RIGHT_KC = rightsoft;
	}
	
	private int convertKeycode(int runtimeCode) {
		
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
			
			if(name.equals("CLEAR") || name.equals("DEL") || name.equals("DELETE") || name.equals("BACKSPACE"))
				return KEYCODE_DEL;
		}catch(Throwable e) {}
		
		if(runtimeCode==KEYCODE_LEFT_KC)
			return KEYCODE_LEFT_KC;
		if(runtimeCode==KEYCODE_RIGHT_KC)
			return KEYCODE_RIGHT_KC;
		
		try {
			if(context.getGameAction(runtimeCode) == Canvas.LEFT && runtimeCode!=KEYCODE_4) 
				return KEYCODE_DPAD_LEFT;
			if(context.getGameAction(runtimeCode) == Canvas.DOWN && runtimeCode!=KEYCODE_8) 
				return KEYCODE_DPAD_DOWN;
			if(context.getGameAction(runtimeCode) == Canvas.RIGHT && runtimeCode!=KEYCODE_6)
				return KEYCODE_DPAD_RIGHT;
			if(context.getGameAction(runtimeCode) == Canvas.UP && runtimeCode!=KEYCODE_2)
				return KEYCODE_DPAD_UP;
			if(context.getGameAction(runtimeCode) == Canvas.FIRE && runtimeCode!=KEYCODE_5)
				return KEYCODE_DPAD_CENTER;
		}catch(IllegalArgumentException e) {}
		
		
		for(int i=0; i<convertableCodes.length; i++) {
			if(runtimeCode==convertableCodes[i].from)
				return convertableCodes[i].to;
		}
		
		return runtimeCode;
		
	}
	
	private static void detectSoftKeycodes(Layout context) {
		
			String lskc = context.getMIDlet().getAppProperty("PixelyVulpineFramework.leftsoftkeycode");
			String rskc = context.getMIDlet().getAppProperty("PixelyVulpineFramework.rightsoftkeycode");
			boolean givenName=false;
			
			if(lskc!=null && rskc!=null && !lskc.equals(null) && !rskc.equals(null)) {
					try {
						KEYCODE_LEFT_KC = Integer.parseInt(lskc);
						KEYCODE_RIGHT_KC = Integer.parseInt(rskc);
						return;
					}catch(NumberFormatException e) {
						lskc.toUpperCase();
						rskc.toUpperCase();
						givenName=true;
					}
			}
			
			try {
				CSVReader csv = CSVReader.read(Runtime.getRuntime().getClass().getResourceAsStream("/pixelyvulpine/SoftKeycodes.csv"));
				int l, r;
				String le, re, kl, kr;
				for(int i=0; i<csv.getRowsLength(); i++) {
					l = Integer.parseInt(csv.getValue("soft left", i));
					r = Integer.parseInt(csv.getValue("soft right", i));
					le = csv.getValue("left name exception", i).toUpperCase();
					re = csv.getValue("right name exception", i).toUpperCase();
					
					try {
						kl = context.getKeyName(l);
						kr = context.getKeyName(r);
						
						if(!kl.equals(kr) && checkAvailableKeycode(context, l) && checkAvailableKeycode(context, r)&& 
								( (givenName && kl.toUpperCase().equals(lskc) && kr.toUpperCase().equals(rskc)) ||
								(!givenName && !kl.toUpperCase().equals(le) && !kr.toUpperCase().equals(re) && !kl.equals("") && !kr.equals("")) )
								) {
							KEYCODE_LEFT_KC = l;
							KEYCODE_RIGHT_KC = r;
							csv.erase();
							csv=null;
							return;
						}
						
					}catch(IllegalArgumentException e) {}
					
				}
				csv.erase();
				csv=null;
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			System.err.println("Pixely Vulpine warning: SOFT KEYCODES NOT FOUND");
			KEYCODE_LEFT_KC = -6;
			KEYCODE_RIGHT_KC = -7;
			
	}
	
	private static boolean checkAvailableKeycode(Layout context, int keycode) {
		switch(keycode) {
		case KEYCODE_0:
			return false;
		case KEYCODE_1:
			return false;
		case KEYCODE_2:
			return false;
		case KEYCODE_3:
			return false;
		case KEYCODE_4:
			return false;
		case KEYCODE_5:
			return false;
		case KEYCODE_6:
			return false;
		case KEYCODE_7:
			return false;
		case KEYCODE_8:
			return false;
		case KEYCODE_9:
			return false;
		}
		
		if(keycode == context.getKeyCode(Canvas.UP)) 
			return false;
		if(keycode == context.getKeyCode(Canvas.DOWN)) 
			return false;
		if(keycode == context.getKeyCode(Canvas.LEFT)) 
			return false;
		if(keycode == context.getKeyCode(Canvas.RIGHT)) 
			return false;
		if(keycode == context.getKeyCode(Canvas.FIRE))
			return false;
		
		return true;
	}
	
	private static class ConvertableCode{
		
		public int from, to;
		
		public ConvertableCode(int from, int to) {
			this.from = from;
			this.to = to;
		}
	}
	
}
