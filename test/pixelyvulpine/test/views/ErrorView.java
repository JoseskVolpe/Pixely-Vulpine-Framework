package pixelyvulpine.test.views;

import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.Debug;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.GraphicsFix;

public class ErrorView extends Content{

	public ErrorView(Layout layout, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
	}

	public void paint(GraphicsFix g) {
		Debug.logd("¡Forcing crash!");
		g.drawString(new Integer(0/0).toString(),0,0,0);
	}
	
}
