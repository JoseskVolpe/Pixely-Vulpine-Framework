package pixelyvulpine;

public final class Config {

	public final static String framework_version="0.0.1";
	public final static String framework_version_tag="dev";
	
	private static long longPressTimeout=1000; //in millis
	private static byte minimumFlingVelocity = 10; //in percent
	private static byte maximumFlingVelocity = 100; //in percent
	private static long doubleTapTimeout = 700; //in millis
	private static short longpressTouchDistance = 8; //in pixels
	private static short doubleTapDistance = 16; //in píxels
	private static boolean showTouch;
	
	public final static long getLongPressTimeout() {
		return longPressTimeout;
	}
	
	public final static void setLongPressTimeout(long longPressTimeout) {
		Config.longPressTimeout = longPressTimeout;
	}
	
	public final static void setMinimumFlingVelocity(int minimumFlingVelocity) {
		Config.minimumFlingVelocity = (byte)minimumFlingVelocity;
	}
	
	public final static byte getMinimumFlingVelocity() {
		return minimumFlingVelocity;
	}
	
	public final static void setMaximumFlingVelocity(int maximumFlingVelocity) {
		Config.maximumFlingVelocity = (byte)maximumFlingVelocity;
	}
	
	public final static byte getMaximumFlingVelocity() {
		return maximumFlingVelocity;
	}
	
	public final static void setDoubleTapTimeout(long doubleTapTimeout) {
		Config.doubleTapTimeout = doubleTapTimeout;
	}
	
	public final static long getDoubleTapTimeout() {
		return doubleTapTimeout;
	}
	
	public final static void setLongpressTouchDistance(int longpressTouchDistance) {
		Config.longpressTouchDistance=(short)longpressTouchDistance;
	}
	
	public final static short getLongpressTouchDistance() {
		return longpressTouchDistance;
	}
	
	public final static void setDoubleTapDistance(int doubleTapDistance) {
		Config.doubleTapDistance=(short)doubleTapDistance;
	}
	
	public final static short getDoubleTapDistance() {
		return doubleTapDistance;
	}
	
	public final static void setShowTouch(boolean showTouch) {
		Config.showTouch = showTouch;
	}
	
	public final static boolean getShowTouch() {
		return showTouch;
	}
	
}
