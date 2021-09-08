package pixelyvulpine.layouts;

import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.test.App;

public class EmptyLayout extends Layout {

	public EmptyLayout(App app) {
		super(app);
	}

	protected void onOpen() {}
	protected void onDisplay() {}
	protected void onPause() {}
	protected void onResume() {}
	protected void onClose() {}
	protected void onHidden() {}
	protected void onDestroy() {}
}
