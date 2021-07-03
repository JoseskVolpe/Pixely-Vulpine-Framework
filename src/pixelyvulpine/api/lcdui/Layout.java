package pixelyvulpine.api.lcdui;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.util.Controls;

public class Layout extends Canvas implements CommandListener{
	
	private static Image navicons[][]=new Image[3][2]; //3 Icons (Left, Center and Right), 2 priorities (system, interface)
	private static boolean navPress[]=new boolean[3];
	
	private static final byte NAVHEIGHT = 10;//in percent
	
	public static final byte NAVBUTTON_LEFT=0;
	public static final byte NAVBUTTON_CENTER=1;
	public static final byte NAVBUTTON_RIGHT=2;
	
	public static final int NO_ANIMATION = 0;
	public static final int ANIMATION_SMOOTH_SLIDE_UP=1;
	public static final int ANIMATION_SLIDE_UP=2;
	public static final int ANIMATION_SMOOTH_SLIDE_DOWN=3;
	public static final int ANIMATION_SLIDE_DOWN=4;
	public static final int ANIMATION_SMOOTH_SLIDE_LEFT=5;
	public static final int ANIMATION_SLIDE_LEFT=6;
	public static final int ANIMATION_SMOOTH_SLIDE_RIGHT=7;
	public static final int ANIMATION_SLIDE_RIGHT=8;
	
	protected int animation = ANIMATION_SMOOTH_SLIDE_UP;
	
	protected MIDlet app;
	//private josesk.app.j2me.Canvas canvas;
	private static boolean navigationBar = false;
	private static pixelyvulpine.contents.Canvas navbar;
	
	private static long timeStart;
	private static Layout current;
	private static boolean started;
	
	private Color backgroundColor = new Color(69,69,69);
	private Color navigationBarColor = new Color(34,34,34);
	private Color navigationPressColor = new Color(69,69,69);
	
	private static short navheight;
	
	private pixelyvulpine.contents.Canvas canvas;
	private boolean fullscreen, painted;
	private CommandListener listener;
	private Vector commands = new Vector();
	
	protected static double x, y;
	
	public Layout(MIDlet app) {
		this.app = app;
		canvas = new pixelyvulpine.contents.Canvas(this, new int[] {0, 0}, new int[] {0, 0}, new int[] {100, 0}, new int[] {100, 0});
		navbar = new pixelyvulpine.contents.Canvas(this, new int[] {0,0}, new int[] {0,0}, new int[] {100,0}, new int[] {0,NAVHEIGHT});
		navbar.setBackgroundColor(navigationBarColor);
		navbar.setForegroundColor(null);
	}
	
	public final void setup() {
		
		Setup();
		
		canvas.setBackgroundColor(null);
		canvas.setForegroundColor(null);
		
		started=false;
		
		
	}
	private final void stop() {
		
		canvas.Stopped();
		
		Stopped();
		
	}
	protected void Setup() {}
	protected void posSetup() {}
	protected void Stopped() {}
	
	public final void preloadLayout(int w, int h) {
		canvas.prepaint(w, h);
	}
	
	private long lastT;
	protected final void paint(Graphics g) {
		
		if(current!=this) {
			return;
		}
		
		g.translate(-g.getTranslateX(), -g.getTranslateY());
		
		try {
		
			if(!started) 
				timeStart=System.currentTimeMillis();
				
			animate(animation);
			
			backgroundColor.updateColor(g);
			if(started)
				g.fillRect(xToAnimation(0), yToAnimation(0), getWidth(), getHeight());
				
			int tw=0, th=0;
			if(navigationBar) {
					
				double p = NAVHEIGHT/100.f;
				navheight = (short)(Math.max(getHeight(), getWidth())*p);
					
				tw = getWidth();
				th = getHeight()-navheight;
				
				
					
			}else {
				tw = getWidth();
				th = getHeight();
			}
				
			g.translate(xToAnimation(0), yToAnimation(0));
			g.setClip(0, 0, tw, th);
				
			canvas.prepaint(tw, th);
			if(!started) {
				timeStart=System.currentTimeMillis();
				started=true;
				posSetup();
			}
			canvas.paint(g);
			
			g.translate(xToAnimation(0) - g.getTranslateX(), yToAnimation(0) - g.getTranslateY());
			g.setClip(0, 0, getWidth(), getHeight());
			paintLayout(g);
			
			g.translate(0 - g.getTranslateX(), 0 - g.getTranslateY());
			g.setClip(0, 0, getWidth(), getHeight());
			if(navigationBar) {
				
				
				g.translate(0, th);
				g.setClip(0, 0, getWidth(), navheight);
				navbar.prepaint(getWidth(), g.getClipHeight());
				navbar.paint(g);
				g.translate(0, -th);
				g.setClip(0, 0, getWidth(), getHeight());
				
				
				//double p = (NAVHEIGHT*(Math.min(getHeight(), getWidth())/(float)(Math.max(getHeight(), getWidth()))))/100.f;
				
						
				/*g.setColor(navigationBarColor.getRed(), navigationBarColor.getGreen(), navigationBarColor.getBlue());
				g.fillRect(0, getHeight()-((int)(navheight)), getWidth(), navheight);
				
				g.setColor(navigationPressColor.getRed(), navigationPressColor.getGreen(), navigationPressColor.getBlue());*/
				
				
				int nx, ny;
				
				/*
				if(CACHE_NAVICONS[0]!=null) {
					nx=7;
					ny = (int)(getHeight()-navheight+((navheight-CACHE_NAVICONS[0].getHeight())/2));
					if(navPress[0]) {
						g.fillRect(nx, getHeight()-navheight, CACHE_NAVICONS[0].getWidth(), navheight);
						g.fillArc(nx-(navheight/2), getHeight()-navheight, navheight, navheight, 270, -180);
						g.fillArc(nx+(CACHE_NAVICONS[0].getWidth())-(navheight/2), getHeight()-navheight, navheight, navheight, 270, 180);
					}
					CACHE_NAVICONS[0].drawOnGraphics(g, nx, ny, 0); //Left soft key
				}
				
				if(CACHE_NAVICONS[1]!=null) {
					nx=getWidth()/2-(CACHE_NAVICONS[1].getWidth()/2);
					ny = (int)(getHeight()-navheight+((navheight-CACHE_NAVICONS[1].getHeight())/2));
					if(navPress[1]) {
						g.fillRect(nx, getHeight()-navheight, CACHE_NAVICONS[1].getWidth(), navheight);
						g.fillArc(nx-(navheight/2), getHeight()-navheight, navheight, navheight, 270, -180);
						g.fillArc(nx+(CACHE_NAVICONS[1].getWidth())-(navheight/2), getHeight()-navheight, navheight, navheight, 270, 180);
					}
					CACHE_NAVICONS[1].drawOnGraphics(g, nx, ny, 0); //Center soft key
				}
				
				if(CACHE_NAVICONS[2]!=null) {
					nx=getWidth()-7-CACHE_NAVICONS[2].getWidth();
					ny = (int)(getHeight()-navheight+((navheight-CACHE_NAVICONS[2].getHeight())/2));
					if(navPress[2]) {
						g.fillRect(nx, getHeight()-navheight, CACHE_NAVICONS[2].getWidth(), navheight);
						g.fillArc(nx-(navheight/2), getHeight()-navheight, navheight, navheight, 270, -180);
						g.fillArc(nx+(CACHE_NAVICONS[2].getWidth())-(navheight/2), getHeight()-navheight, navheight, navheight, 270, 180);
					}
					CACHE_NAVICONS[2].drawOnGraphics(g, nx, ny, 0); //Right soft key
				}
				
				*/
			}
			
		}catch(Exception e) {
			
		}catch(Error e) {}
		
		g.setColor(0xffffff);
		g.setFont(Font.getDefaultFont());
		long sub=System.currentTimeMillis()-lastT;
		if(sub<=0) sub=1;
		g.drawString((1000/(sub))+" FPS", 0, 0, Graphics.LEFT|Graphics.TOP);
		
		lastT=System.currentTimeMillis();
		
		painted=true;
		repaint();
		
	}
	
	public boolean isPainted() {
		return painted;
	}
	
	/**
	 * Custom, advanced rendering method for more experienced developers
	 * @param Graphics
	 */
	protected void paintLayout(Graphics g) {
		
		
		
	}
	
	
	/**
	 * Add content on layout
	 */
	public final void addContent(Content content) {
		
		canvas.addContent(content);
		
	}
	
	/**
	 * Removes content on layout
	 * @param content
	 * @return remove sucessfully
	 */
	public final boolean removeContent(Content content) {
		
		return canvas.removeContent(content);
		
	}
	
	public final void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public final void setNavigationBarColor(Color navigationBarColor) {
		this.navigationBarColor = navigationBarColor;
	}
	
	public final void setNavigationPressColor(Color navigationPressColor) {
		this.navigationPressColor = navigationPressColor;
	}
	
	public final static Layout getCurrent() {
		return current;
	}
	
	public final static void setCurrent(MIDlet midlet, Layout layout) {
		if(current!=null)
			current.stop();
			
		current=layout;
		layout.setup();
		Display.getDisplay(midlet).setCurrent(layout);
		layout.serviceRepaints();
		/*while(!layout.isLoaded()) { //Wait for the first prepaint
			Sleep.sleep(1);
		}*/
	}
	
	public final MIDlet getMIDlet() {
		return app;
	}
	
	public final Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public final Color getNavigationBarColor() {
		return navigationBarColor;
	}
	
	public final Color getNavigationPressColor() {
		return navigationPressColor;
	}
	
	public final static void setNavigationBar(boolean navigationBar) {
		if(navigationBar && current!=null) {
			current.setFullScreenMode(true);
		}
		Layout.navigationBar = navigationBar;
	}
	
	public final static boolean getNavigationBar() {
		return navigationBar;
	}
	
	public final static void setNavigationBarButton(int button, String source) throws IOException {
		setNavigationBarButton(button, Image.createImage(source));
	}
	
	public final static void setNavigationBarButton(int button, Image icon) {
		navicons[button][1] = icon;
	}
	
	public final static Image[] getNavigationBarButtons() {
		
		Image[] temp = new Image[navicons.length];
		for(int i=0; i<temp.length; i++) {
			temp[i] = navicons[i][1];
		}
		
		return temp;
	}
	
	public final static int xToAnimation(float x) {
		
		return (int)(x+(getCurrent().getWidth()*Layout.x));
		
	}
	
	public final static int yToAnimation(float y) {
		
		return (int)(y+(getCurrent().getHeight()*Layout.y));
		
	}
	
	private static void animate(int animation) {
		
		long time = System.currentTimeMillis() - timeStart;
		
		switch(animation) {
		
		case ANIMATION_SMOOTH_SLIDE_UP:
			y=0.6/(0.05*time)-0.015;
			x=0;
		break;
		case ANIMATION_SLIDE_UP:
			y = (200-time)/200.f;
			if(y<0) y=0;
			x=0;
		break;
		case ANIMATION_SMOOTH_SLIDE_DOWN:
			y=-1*(0.6/(0.05*time)-0.015);
			x=0;
		break;
		case ANIMATION_SLIDE_DOWN:
			y = ((-200+time)/200.f)-2.;
			if(y>0) y=0;
			x=0;
		break;
		case ANIMATION_SMOOTH_SLIDE_LEFT:
			x=0.6/(0.05*time)-0.015;
			y=0;
		break;
		case ANIMATION_SLIDE_LEFT:
			x = (200-time)/200.f;
			if(x<0) x=0;
			y=0;
		break;
		case ANIMATION_SMOOTH_SLIDE_RIGHT:
			x=-1*(0.6/(0.05*time)-0.015);
			y=0;
		break;
		case ANIMATION_SLIDE_RIGHT:
			x = (-200+time)/200.f;
			if(x>0) x=0;
			y=0;
		break;
		default:
			y=0;
			x=0;
		
		}
		
	}
	
	protected final void keyPressed(int keyCode){
		//when player press a key
		int key = Controls.getKey(keyCode);
		switch(key) {
		case Controls.SOFTKEY_LEFT:
			navPress[0]=true;
			break;
			
		case Controls.SOFTKEY_RIGHT:
			navPress[2]=true;
			break;
			
		case Controls.SOFTKEY_CENTER:
			navPress[1]=true;
			break;
			
		}
		
		/*
		canvas.onSelect();
		if(!canvas.keyDown(keyCode, key)) {
			
			Content selected = canvas.getSelected();
			if(selected == null)
				navicons[NAVBUTTON_CENTER][0]=null;
			else
				navicons[NAVBUTTON_CENTER][0]=Content.getNavbarIcon();
			
			convertNavIcon(NAVBUTTON_CENTER);
			
			return;
		}
		*/
		

	}

	protected final void keyReleased(int keyCode){

		//when player released a key
		int key = Controls.getKey(keyCode);
		switch(key) {
		case Controls.SOFTKEY_LEFT:
			navPress[0]=false;
			break;
			
		case Controls.SOFTKEY_RIGHT:
			navPress[2]=false;
			break;
			
		case Controls.SOFTKEY_CENTER:
			navPress[1]=false;
			break;
		}
		
		if(!canvas.keyUp(keyCode, key)) return;
	
	}

		/*
		touchscreen observations:

		There's NO multitouch in J2ME, they are all single-touch
		Pointer is most used in touch screen, but may also use other controls like trackball in some devices
		*/
	
	public final boolean isLoaded() {
		return started;
	}

	protected final void pointerPressed(int x, int y){
		
		checkTouchNav(x, y);

	}

	protected final void pointerReleased(int x, int y){
		
		navPress[0]=false;
		navPress[1]=false;
		navPress[2]=false;

	}

	protected final void pointerDragged(int x, int y){
		
		checkTouchNav(x, y);
		
	}
	
	public final void addCommand(Command command) {
		commands.addElement(command);
		super.addCommand(command);
	}

	public final void removeCommand(Command command) {
		commands.removeElement(command);
		super.removeCommand(command);
	}
	
	public final void setCommandListener(CommandListener l) {
		listener=l;
		super.setCommandListener(l);
	}
	
	public final void setFullScreenMode(boolean fullscreen) {
		this.fullscreen=fullscreen;
		if(!fullscreen) {
			setNavigationBar(false);
		}
		super.setFullScreenMode(fullscreen);
	}
	
	private static final void checkTouchNav(int x, int y) {
		
		/*
		
		int nx, ny;
		
		nx=7;
		ny = (int)(App.getCurrentLayout().getHeight()-navheight+((navheight-CACHE_NAVICONS[0].getHeight())/2));
		if(x>=nx && x<=nx+CACHE_NAVICONS[0].getWidth() &&
				y>=ny && y<=ny+CACHE_NAVICONS[0].getHeight()) {
			navPress[0]=true;
		}else {
			navPress[0]=false;
		}
		
		nx=App.getCurrentLayout().getWidth()/2-(CACHE_NAVICONS[1].getWidth()/2);
		ny = (int)(App.getCurrentLayout().getHeight()-navheight+((navheight-CACHE_NAVICONS[1].getHeight())/2));
		if(x>=nx && x<=nx+CACHE_NAVICONS[1].getWidth() &&
				y>=ny && y<=ny+CACHE_NAVICONS[1].getHeight()) {
			navPress[1]=true;
		}else {
			navPress[1]=false;
		}
		
		nx=App.getCurrentLayout().getWidth()-7-CACHE_NAVICONS[2].getWidth();
		ny = (int)(App.getCurrentLayout().getHeight()-navheight+((navheight-CACHE_NAVICONS[2].getHeight())/2));
		if(x>=nx && x<=nx+CACHE_NAVICONS[2].getWidth() &&
				y>=ny && y<=ny+CACHE_NAVICONS[2].getHeight()) {
			navPress[2]=true;
		}else {
			navPress[2]=false;
		}
		
		*/
		
	}

	public void commandAction(Command command, Displayable display) {
		// TODO Auto-generated method stub
		
	}
	
	/*protected final void assignCanvas(josesk.app.j2me.Canvas canvas) {
		
		this.canvas = canvas;
		
	}*/

}
