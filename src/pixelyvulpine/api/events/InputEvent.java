package pixelyvulpine.api.events;

public class InputEvent {
	
	private long eventTime;
	
	public InputEvent() {
		eventTime = System.currentTimeMillis();
	}
	
	public final long getEventTime() {
		return eventTime;
	}

}
