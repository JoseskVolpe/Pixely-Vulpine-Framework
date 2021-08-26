package pixelyvulpine;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import pixelyvulpine.api.util.CSVReader;

public final class Config {

	public final static byte ICON_BACK=0;
	public final static byte ICON_SELECT=1;
	public final static byte ICON_WRITE=2;
	public final static byte ICON_NEXT=3;
	public final static byte ICON_MENU=4;
	private static Image icons[]=new Image[5];
	
	public final static String framework_version="0.0.1";
	/**
	 * dev: not released;
	 * pre-alpha: pre-alpha release;
	 * alpha: alpha release;
	 * beta: beta release;
	 * stable: main release;
	 * custom: custom, modified framework
	 */
	public final static String framework_version_tag="dev";
	
	
	private static long longPressTimeout=1000; //in millis
	private static short minimumFlingVelocity = 8; //in pixels
	private static short maximumFlingVelocity = Short.MAX_VALUE; //in pixels
	private static long doubleTapTimeout = 700; //in millis
	private static short longpressTouchDistance = 8; //in pixels
	private static short doubleTapDistance = 16; //in píxels
	private static boolean showTouch;
	private static String deviceBrand="unknown";
	private static long numpadTextInputTimeout=1300; //In millis
	private static boolean debugViews=false;
	private static boolean XRayMode=false;
	private static Font navbarFont=Font.getDefaultFont();
	
	static {
		try {
			CSVReader reader = CSVReader.read(Runtime.getRuntime().getClass().getResourceAsStream("/pixelyvulpine/BrandClassTestNames.csv"));
			
			for(int i=0; i<reader.getRowsLength(); i++) {
				
				String classTest = reader.getValue("Class test", i);
				
				try {
					Class.forName(classTest);
					deviceBrand = reader.getValue("Brand", i);
					break;
				}catch(ClassNotFoundException e2) {
					continue;
				}
			}
			
			
			reader.erase();
			reader=null;
		}catch(Throwable e) {
			deviceBrand="unknown";
		}
	}
	
	public final static long getLongPressTimeout() {
		return longPressTimeout;
	}
	
	public final static void setLongPressTimeout(long longPressTimeout) {
		Config.longPressTimeout = longPressTimeout;
	}
	
	public final static void setMinimumFlingVelocity(int minimumFlingVelocity) {
		Config.minimumFlingVelocity = (short)minimumFlingVelocity;
	}
	
	public final static short getMinimumFlingVelocity() {
		return minimumFlingVelocity;
	}
	
	public final static void setMaximumFlingVelocity(int maximumFlingVelocity) {
		Config.maximumFlingVelocity = (short)maximumFlingVelocity;
	}
	
	public final static short getMaximumFlingVelocity() {
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
	
	public final static String getDeviceBrand() {
		return deviceBrand;
	}
	
	public final static void setNumpadTextInputTimeout(long numpadTextInputTimeout) {
		Config.numpadTextInputTimeout = numpadTextInputTimeout;
	}
	
	public final static long getNumpadTextInputTimeout() {
		return numpadTextInputTimeout;
	}
	
	public final static void setDebugViews(boolean debugViews) {
		Config.debugViews=debugViews;
	}
	
	public final static boolean getDebugViews() {
		return debugViews;
	}
	
	public final static void setXRayMode(boolean XRayMode) {
		Config.XRayMode=XRayMode;
	}
	
	public final static boolean getXRayMode() {
		return XRayMode;
	}
	
	public final static void setNavbarFont(Font font) {
		Config.navbarFont = font;
	}
	
	public final static Font getNavbarFont() {
		return navbarFont;
	}
	
	public final static void setIcon(Image icon, int type) {
		icons[type]=icon;
	}
	
	public final static Image getIcon(int type) {
		return icons[type];
	}
	
}
