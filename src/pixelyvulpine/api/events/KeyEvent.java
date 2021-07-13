package pixelyvulpine.api.events;

import java.io.InputStream;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;

import pixelyvulpine.Config;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.CSVReader;

public class KeyEvent {
	
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
	public final static int KEYCODE_DPAD_UP = Canvas.UP;
	public final static int KEYCODE_DPAD_LEFT = Canvas.LEFT;
	public final static int KEYCODE_DPAD_RIGHT = Canvas.RIGHT;
	public final static int KEYCODE_DPAD_DOWN = Canvas.DOWN;
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
	public final static int KEYCODE_ENCALL = -11;
	public final static int KEYCODE_DEL = -8; //TODO: KeyCodes wrapper (8 KeyCode will be -8)
	//TODO: SOFT_KEYS auto detector
	public static int KEYCODE_SOFT_LEFT=-6;
	public static int KEYCODE_SOFT_RIGHT=-7;
	
	private static int[] KEYCODE_LEFT_KC;
	private static int[] KEYCODE_RIGHT_KC;
	
	public static interface Callback{
		public abstract boolean onKeyDown(int keyCode, KeyEvent event);
		public abstract boolean onKeyLongPress(int keyCode, KeyEvent event);
		public abstract boolean onKeyMultiple(int keyCode, int count, KeyEvent event);
		public abstract boolean onKeyUp(int keyCode, KeyEvent event);
	}
	
	public static class DispatcherState{
		public DispatcherState() {
			
		}
		
		public void handleUpEvent(KeyEvent event) {
			
		}
		
		public boolean isTracking(KeyEvent event) {
			return false;
		}
		
		public void performedLongPress(KeyEvent event) {
			
		}
		
		public void reset(Object target) {
			
		}
		
		public void reset() {
			
		}
		
		public void startTracking(KeyEvent event, Object target) {
			
		}
	}

	
	private int action, code;
	private Layout context; 
	
	public KeyEvent (Layout context, int action, int code) {
		
		if(KEYCODE_LEFT_KC==null || KEYCODE_RIGHT_KC==null || KEYCODE_LEFT_KC.length<=0 || KEYCODE_RIGHT_KC.length<=0) detectSoftKeycodes(context);
		
		this.action=action;
		this.code=code;
		this.context=context;
	}
	
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
				
			}
			
		}catch(Throwable DetectException) {
			KEYCODE_LEFT_KC = new int[] {KEYCODE_SOFT_LEFT};
			KEYCODE_RIGHT_KC = new int[] {KEYCODE_SOFT_RIGHT};
		}
	}
	
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
	
}
