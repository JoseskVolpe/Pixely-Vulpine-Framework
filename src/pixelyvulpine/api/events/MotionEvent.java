package pixelyvulpine.api.events;

import java.util.Vector;

public class MotionEvent extends InputEvent{
	
	public final static byte ACTION_DOWN=0;
	public final static byte ACTION_MOVE=1;
	public final static byte ACTION_UP=2;
	
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
	
	private Vector historicalCoords;
	
	private PointerCoords pointerCoords;
	private byte action;
	
	public class PointerCoords{
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
	
	
	public MotionEvent(Vector historicalCoords, int x, int y, int action) {
		super();
		
		this.pointerCoords = new PointerCoords();
		
		this.historicalCoords=historicalCoords;
		
		pointerCoords.x=x;
		pointerCoords.y=y;
		this.action=(byte)action;
		
		switch(action) {
			case ACTION_DOWN:
				historicalCoords.removeAllElements();
				historicalCoords.trimToSize();
			break;
			
			case ACTION_MOVE:
				historicalCoords.addElement(this.getPointerCoords());
			break;
		}
		
	}
	
	public PointerCoords getPointerCoords() {
		
		return pointerCoords;
	}
	
	public PointerCoords getHistoricalPointerCoords(int pos) {
		
		return (PointerCoords) historicalCoords.elementAt(pos);
	}
	
	public int getHistorySize() {
		
		return historicalCoords.size();
	}
	
	public int getAction() {
		return action;
	}
	
}
