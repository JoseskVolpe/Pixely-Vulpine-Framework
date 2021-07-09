package pixelyvulpine;

public final class Config {

	public final static String framework_version="0.0.1";
	public final static String framework_version_tag="dev";
	private static long longPressTimeout=1000; //in millis
	private static byte minimumFlingVelocity = 10; //in percent
	private static byte maximumFlingVelocity = 100; //in percent
	private static long doubleTapTimeout = 700; //in millis
	
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
	
}
