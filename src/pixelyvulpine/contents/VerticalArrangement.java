package pixelyvulpine.contents;

import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;

public class VerticalArrangement extends Canvas{

	public VerticalArrangement(Layout layout, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
		this.setArrangement(ARRANGEMENT_VERTICAL);
		this.setContentAlignment(ALIGNMENT_LEFT);
		
	}

}
