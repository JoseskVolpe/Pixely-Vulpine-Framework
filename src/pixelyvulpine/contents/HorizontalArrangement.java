package pixelyvulpine.contents;

import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;

public class HorizontalArrangement extends Canvas{

	public HorizontalArrangement(Layout layout, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
		this.setArrangement(ARRANGEMENT_HORIZONTAL);
		this.setContentAlignment(ALIGNMENT_CENTER);
		
	}

}
