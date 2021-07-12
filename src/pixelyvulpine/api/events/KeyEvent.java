package pixelyvulpine.api.events;

import javax.microedition.lcdui.Canvas;

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
	 * @deprecated
	 * Keycode used for green call key
	 * Avoid using this, most phones doesn't detects this keycode
	 * This will NOT be removed in future releases
	 */
	public final static int KEYCODE_CALL = -10;
	/**
	 * @deprecated
	 * Keycode used for red end-call key
	 * Avoid using it, most phones doesn't detects this keycode
	 * This will NOT be removed in future releases
	 */
	public final static int KEYCODE_ENCALL = -11;
	public final static int KEYCODE_DEL = -8 | 8; //TODO: KeyCodes wrapper (8 KeyCode will be -8)
	//TODO: SOFT_KEYS auto detector
	public static int KEYCODE_SOFT_LEFT;
	public static int KEYCODE_SOFT_RIGHT;
	
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

	int action, code;
	
	public KeyEvent (int action, int code) {
		this.action=action;
		this.code=code;
	}
	
}
