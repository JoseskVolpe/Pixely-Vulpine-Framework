package pixelyvulpine.layouts;

import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.api.util.Sleep;
import pixelyvulpine.contents.CircularProgressBar;
import pixelyvulpine.contents.ImageView;

public class Logo extends Layout{
	
	private ImageView logo;
	private CircularProgressBar spinner;
	private Image logoI;
	
	public Logo(MIDlet app) {
		
		super(app);
		
		animation = Layout.ANIMATION_SMOOTH_SLIDE_UP;
		this.logo = new ImageView(this, logoI, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,60), new DimensionAttributes.Offset(0, 0, 0, 0)), true);
		this.logo.setPositioning(Content.POSITIONING_ANCHORED);
		this.logo.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		this.logo.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		
		
		this.spinner = new CircularProgressBar(this, new CircularProgressBar.CircularProgressBarDimensionAttributes(new CircularProgressBar.CircularProgressBarDimensionAttributes.Scaled(0,-5,10), new CircularProgressBar.CircularProgressBarDimensionAttributes.Offset(0,0,0)));
		this.spinner.setPositioning(Content.POSITIONING_ANCHORED);
		this.spinner.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		this.spinner.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		this.spinner.setVisible(false);
		
		this.addContent(this.logo);
		this.addContent(spinner);
		
	}
	
	public void Setup() {
		
		setFullScreenMode(true);
		
	}
	
	private boolean timedUp;
	protected void posSetup() {
		
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1300);
					timedUp=true;
					spinner.setVisible(true);
				} catch (InterruptedException e) {}
			}
		}).start();
		
	}
	
	public boolean isTimedUp() {
		return timedUp;
	}
	
	public void waitTimeUp() {
		try {
			while(!isTimedUp()) {
				Thread.sleep(1);
			}
		}catch(InterruptedException e) {}
	}
	
	public void setLogoImage(Image logo, boolean copyColor) {
		this.logoI=logo;
		this.logo.setImage(logo);
		if(!copyColor) return;
		int color[] = new int[1];
		logo.getRGB(color, 0, 1, logo.getWidth()/2, logo.getHeight()/2, 1, 1);
		Color c = new Color(color[0], false);
		this.setBackgroundColor(c);
	}
	
	public Image getLogoImage() {
		return logoI;
	}
	
}
