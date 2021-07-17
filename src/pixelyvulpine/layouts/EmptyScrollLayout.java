package pixelyvulpine.layouts;

import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Layout;

public class EmptyScrollLayout extends Layout{

	public EmptyScrollLayout(MIDlet app) {
		super(app);
		
		canvas.setScrollable(true);
	}

}
