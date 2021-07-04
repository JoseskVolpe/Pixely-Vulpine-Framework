package pixelyvulpine.layouts;

import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.ContentListener;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.LayoutLoader;
import pixelyvulpine.api.util.Sleep;
import pixelyvulpine.contents.ImageView;

public class Logo extends Layout implements Runnable{
	
	private ImageView logo;
	private Image logoI;
	private LayoutLoader ls;
	private String layout;
	private long start;
	
	public Logo(MIDlet app, Image logo, String layoutName) {
		
		super(app);
		
		animation = Layout.ANIMATION_SMOOTH_SLIDE_UP;
		
		this.logoI=logo;
		this.layout=layoutName;
		
		this.logo = new ImageView(this, logoI, new int[] {0,0}, new int[] {0,0}, new int[] {100,0}, new int[] {100,0}, true);
		this.logo.setPositioning(Content.POSITIONING_ANCHORED);
		this.logo.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		this.logo.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		
		
		this.addContent(this.logo);
		
		setBackgroundColor(new Color(0,0,0));
		
		setNavigationBar(false);
		
	}
	
	public void Setup() {
		
		setFullScreenMode(true);
		
	}
	
	protected void posSetup() {
		
		start=System.currentTimeMillis();
		
	}
	
	public void loadLayout(LayoutLoader layoutLoader) {
		
		if(Layout.getCurrent()!=this) {
			Layout.setCurrent(this.getMIDlet(), this);
		}
		
		this.ls = layoutLoader;
		
		new Thread(this).start();
	}
	
	public void run() {
		start=System.currentTimeMillis();
		Layout l = ls.loadLayout(layout);
		l.preloadLayout(getWidth(), getHeight());
		
		
		if(!logo.isError()) {
		
			while(System.currentTimeMillis()-start<1300) { //Present app logo for atleast 1,3 second
				Sleep.sleep(1);
			}
		
		}
		
		Layout.setCurrent(this.getMIDlet(), l);
		
	}
	
	public void setLogoImage(Image logo) {
		this.logoI=logo;
	}
	
	public Image getLogoImage() {
		return logoI;
	}

	public boolean contentPressed(Content content) {
		
		return false;
	}

}
