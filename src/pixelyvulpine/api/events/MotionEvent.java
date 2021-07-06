package pixelyvulpine.api.events;

import java.util.Vector;

public class MotionEvent extends InputEvent{
	
	public final static byte ACTION_DOWN=0;
	public final static byte ACTION_MOVE=1;
	public final static byte ACTION_UP=3;
	
	public final static String actionToString(int action) {
		switch(action) {
			case ACTION_DOWN:
				return "ACTION_DOWN";
			case ACTION_MOVE:
				return "ACTION_MOVE";
			case ACTION_UP:
				return "ACTION_UP";
			default:
				return String.valueOf(action);
		
		}
	}
	
	private static Vector historicalCoords = new Vector(0);
	
	private PointerCoords pointerCoords;
	private byte action;
	
	class PointerCoords{
		public int x, y;
		
		public PointerCoords() {}
		public PointerCoords(PointerCoords other) {
			copyFrom(other);
		}
		
		public void copyFrom(PointerCoords other) {
			this.x = other.x;
			this.y = other.y;
		}
	}
	
	
	public MotionEvent(int x, int y, int action) {
		super();
		
		this.pointerCoords = new PointerCoords();
		
		pointerCoords.x=x;
		pointerCoords.y=y;
		this.action=(byte)action;
		
		if(action==ACTION_DOWN) {
			historicalCoords.removeAllElements();
			historicalCoords.trimToSize();
		}
		
	}
	
	public PointerCoords getPointerCoords() {
		
		if(action==ACTION_MOVE) {
			historicalCoords.addElement(this);
		}
		
		return pointerCoords;
	}
	
	public static PointerCoords getHistoricalPointerCoords(int pos) {
		
		return (PointerCoords) historicalCoords.elementAt(pos);
	}
	
	public static int getHistorySize() {
		
		return historicalCoords.capacity();
	}
	
	public int getAction() {
		return action;
	}
	
}
