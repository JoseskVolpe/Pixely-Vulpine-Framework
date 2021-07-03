package pixelyvulpine.layouts;

import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.CircularProgressBar;

public class Loading extends Layout implements Runnable{

	CircularProgressBar spinner;
	Layout next;
	
	public Loading(MIDlet app) {
		super(app);
		
		setFullScreenMode(true);
		
		this.animation = Layout.NO_ANIMATION;
		this.setBackgroundColor(new Color(0,0,0));
		
		spinner = new CircularProgressBar(this,new int[] {0,0}, new int[] {0,0}, new int[] {20, 0}, new Color(255,255,255));
		spinner.setPositioning(Content.POSITIONING_ANCHORED);
		spinner.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		spinner.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		
		this.addContent(spinner);
	}
	
	public void Setup() {

		setNavigationBar(false);
	}

	public void loadLayout(Layout layout) {
		
		if(getCurrent()!=this) {
			setCurrent(getMIDlet(), this);
		}
		
		next = layout;
		
		new Thread(this).start();
		
	}

	public void run() {
		
		try {
			while(!isPainted())
				Thread.sleep(1); //Prevents freezes
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		next.preloadLayout(getWidth(), getHeight());
		setCurrent(getMIDlet(), next);
		
	}

}
