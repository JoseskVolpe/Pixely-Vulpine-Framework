package pixelyvulpine.layouts;

import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.LayoutLoader;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.api.util.Sleep;
import pixelyvulpine.contents.CircularProgressBar;
import pixelyvulpine.contents.ImageView;

public class Logo extends Layout implements Runnable{
	
	private ImageView logo;
	private CircularProgressBar spinner;
	private Image logoI;
	private LayoutLoader ls;
	private String layout;
	private long start;
	
	public Logo(MIDlet app, Image logo, String layoutName) {
		
		super(app);
		
		animation = Layout.ANIMATION_SMOOTH_SLIDE_UP;
		
		this.logoI=logo;
		this.layout=layoutName;
		
		createContents();
		
		if(logo!=null) {
			int color[] = new int[1];
			logo.getRGB(color, 0, 1, logo.getWidth()/2, logo.getHeight()/2, 1, 1);
			Color c = new Color(color[0], false);
			this.setBackgroundColor(c);
		}
		
		setNavigationBar(false);
		
	}
	
	public Logo(MIDlet app, Image logo, String layoutName, Color background) {
		super(app);
		
		animation = Layout.ANIMATION_SMOOTH_SLIDE_UP;
		
		this.logoI=logo;
		this.layout=layoutName;
		
		createContents();
		
		this.setBackgroundColor(background);
		
		setNavigationBar(false);
	}
	
	private void createContents() {
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
	
	protected void posSetup() {
		
		start=System.currentTimeMillis();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1300);
					spinner.setVisible(true);
				} catch (InterruptedException e) {}
			}
		}).start();
		
	}
	
	public void loadLayout(LayoutLoader layoutLoader) {
		
		if(Layout.getCurrent()!=this) {
			Layout.setCurrent(this.getMIDlet(), this);
		}
		
		this.ls = layoutLoader;
		
		Thread t = new Thread(this);
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}
	
	public void run() {
		
		start=System.currentTimeMillis();
		Layout l=null;
		try {
			l = ls.loadLayout(layout);
		}catch(Exception e) {
			Crash.showCrashMessage(getMIDlet(), e, "Crash on Logo\nCouldn't load layout "+layout, Crash.APPLICATION_CRASH);
			return;
		}catch(Error e) {
			Crash.showCrashMessage(getMIDlet(), e, "Crash on Logo\nCouldn't load layout "+layout, Crash.APPLICATION_CRASH);
			return;
		}
		
		if(l==null) {
			Crash.showCrashMessage(getMIDlet(), new NullPointerException(), "Crash on Logo\nMIDlet returned nothing", Crash.APPLICATION_CRASH);
		}
		
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
