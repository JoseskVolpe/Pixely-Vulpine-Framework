package pixelyvulpine.api;

import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDletStateChangeException;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.layouts.Logo;
import pixelyvulpine.api.lcdui.Layout;

public abstract class MIDlet extends javax.microedition.midlet.MIDlet{

	private boolean paused;
	
	private Logo logo;
	
	protected final void destroyApp(boolean arg0) throws MIDletStateChangeException {
		onDestroy();
		Layout.notifyDestroyed();
	}

	protected final void pauseApp() {
		onPause();
		Layout.notifyPaused();
		paused=true;
	}

	protected final void startApp() throws MIDletStateChangeException {
		if(paused) {
			onResume();
			Layout.notifyResumed();
			paused=false;
		}else {
			logo = new Logo(this);
			onCreate();
			Layout.setCurrent(this, logo);
			onStart();
			logo=null;
		}
	}
	
	protected final void setLogo(Image image, Color backgroundColor) {
		if(logo!=null) {
			logo.setLogoImage(image, backgroundColor==null);
			if(backgroundColor!=null) logo.setBackgroundColor(backgroundColor);
		}
	}
	
	protected abstract void onCreate();
	protected abstract void onStart();
	protected abstract void onResume();
	protected abstract void onPause();
	protected abstract void onDestroy();

}
