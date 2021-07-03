//TODO: Remake this with javax.microedition.lcdui.Canvas keycode contants

package pixelyvulpine.api.util;

import javax.microedition.lcdui.Canvas;

public class Controls{

	public final static int SOFTKEY_LEFT=0;
	public final static int SOFTKEY_CENTER=1;
	public final static int SOFTKEY_RIGHT=2;
	public final static int DPAD_LEFT=3;
	public final static int DPAD_UP=4;
	public final static int DPAD_RIGHT=5;
	public final static int DPAD_DOWN=6;
	public final static int NUM0=7;
	public final static int NUM1=8;
	public final static int NUM2=9;
	public final static int NUM3=10;
	public final static int NUM4=11;
	public final static int NUM5=12;
	public final static int NUM6=13;
	public final static int NUM7=14;
	public final static int NUM8=15;
	public final static int NUM9=16;
	public final static int NUMHASH=17;
	public final static int NUMSTAR=18;
	public final static int KEY_UNKNOWN=-1;
	public final static int TOUCH=-2;
	
	private static boolean touchEnabled=false;
	
	private static int keycodes[] = {
			-6,
			-5,
			-7,
			-3,
			-1,
			-4,
			-2,
			48,
			49,
			50,
			51,
			52,
			53,
			54,
			55,
			56,
			57,
			35,
			42
	};
	
	public static void registerKey(int key, int keycode) {
		
		keycodes[key] = keycode;
		
	};
	
	/**Return registered code or a key*/
	public static int getKeycode(int key) {
		
		return keycodes[key];
		
	}
	
	/**Return registered key from a code*/
	public static int getKey(int keycode) {
		
		for(int i=0; i<keycodes.length; i++) {
			if(keycodes[i]==keycode) {
				return i;
			}
		}
		
		return KEY_UNKNOWN;
		
	}
	
	public static void setTouchEnabled(boolean touchEnabled) {
		
		Controls.touchEnabled=touchEnabled;
		
	}
	
	public static boolean isTouchEnabled() {
		return touchEnabled;
	}
	
	public static boolean autoTouchDetect(Canvas canvas) {
		
		if(canvas.hasPointerEvents() && canvas.hasPointerMotionEvents()) return true;
		
		return false;
		
	}
	
}
