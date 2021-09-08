package pixelyvulpine.layouts;

import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Layout;

public class EmptyScrollLayout extends Layout{

	public EmptyScrollLayout(MIDlet app) {
		super(app);
		
		canvas.setScrollable(true);
	}
	
	protected void onOpen() {}
	protected void onDisplay() {}
	protected void onPause() {}
	protected void onResume() {}
	protected void onClose() {}
	protected void onHidden() {}
	protected void onDestroy() {}

}
