package pixelyvulpine.contents;

import pixelyvulpine.api.lcdui.Layout;

public class VerticalArrangement extends Canvas{

	public VerticalArrangement(Layout layout, int[] x, int[] y, int[] width, int[] height) {
		super(layout, x, y, width, height);
		
		this.setArrangement(ARRANGEMENT_VERTICAL);
		this.setContentAlignment(ALIGNMENT_LEFT);
		
	}

}
