package pixelyvulpine.contents;

import pixelyvulpine.api.lcdui.Layout;

public class HorizontalArrangement extends Canvas{

	public HorizontalArrangement(Layout layout, int[] x, int[] y, int[] width, int[] height) {
		super(layout, x, y, width, height);
		
		this.setArrangement(ARRANGEMENT_HORIZONTAL);
		this.setContentAlignment(ALIGNMENT_CENTER);
		
	}

}
