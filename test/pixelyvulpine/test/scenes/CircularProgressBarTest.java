package pixelyvulpine.test.scenes;

import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.CircularProgressBar;

public class CircularProgressBarTest extends Layout{

	public CircularProgressBarTest(MIDlet app) {
		super(app);
		
		this.animation = Layout.NO_ANIMATION;
		
		this.setBackgroundColor(new Color(0,0,0));
		
		CircularProgressBar spinner = new CircularProgressBar(this,new int[] {0,0}, new int[] {0,0}, new int[] {20, 0}, new Color(255,255,255));
		spinner.setPositioning(Content.POSITIONING_ANCHORED);
		spinner.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		spinner.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		
		this.addContent(spinner);
		
	}
	
	

}
