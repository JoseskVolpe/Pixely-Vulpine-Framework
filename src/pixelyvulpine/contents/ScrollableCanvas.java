package pixelyvulpine.contents;

import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;

public class ScrollableCanvas extends Canvas{

	public ScrollableCanvas(Layout layout, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
		this.setScrollable(true);
	}

}
