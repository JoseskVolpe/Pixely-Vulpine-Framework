package pixelyvulpine.api.lcdui;

import java.io.IOException;
import java.util.Stack;
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

import pixelyvulpine.Config;
import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.InputEvent;
import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.api.util.GraphicsFix;

public class Layout extends Canvas{
	
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
	
	private static final long touch_delay = 1000; //In millis
	private static final int touch_sensibility = 8; //In pixels
	
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
	
	protected pixelyvulpine.contents.Canvas canvas;
	private pixelyvulpine.contents.Canvas focused;
	private boolean fullscreen, painted;
	private short deltaTime;
	private CommandListener listener;
	private Vector commands = new Vector();
	
	
	private paintThreadClass paintThread;
	private class paintThreadClass implements Runnable{
		private Thread thread;
		private boolean terminated;
		private boolean repaint=true;
		
		public paintThreadClass() {
			thread=new Thread(this);
			thread.setPriority(Thread.NORM_PRIORITY);
			thread.start();
		}
		
		public void run() {
			
			try {
			
				while(!terminated) {
					
					do {
						Thread.sleep(1);
					}while(!repaint);
					
					repaint=false;
					
					repaint();
				}
			
			}catch(InterruptedException e) {
				Crash.showCrashMessage(app, e, "Paint Thread unexpected interrupted", Crash.FRAMEWORK_CRASH);
			}
			
		}
		
		public void askRepaint() {
			repaint=true;
		}
		
		public void terminate() {
			terminated=true;
		}
		
	}
	
	protected static double x, y;
	
	public Layout(MIDlet app) {
		this.app = app;
		
		canvas = new pixelyvulpine.contents.Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 100), new DimensionAttributes.Offset(0,0, 0, 0)));
		navbar = new pixelyvulpine.contents.Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 0), new DimensionAttributes.Offset(0,0, 0, NAVHEIGHT)));
		navbar.setBackgroundColor(navigationBarColor);
		navbar.setForegroundColor(null);
		focused=canvas;
	}
	
	public final void setup() {
		
		try {
			Setup();
		}catch(Exception e) {
			Crash.showCrashMessage(app, e, "Couldn't setup activity "+getTitle(), Crash.APPLICATION_CRASH);
		}catch(Error e) {
			Crash.showCrashMessage(app, e, "Couldn't setup activity "+getTitle(), Crash.APPLICATION_CRASH);
		}
		
		try {
			canvas.setBackgroundColor(null);
			canvas.setForegroundColor(null);
		}catch(NullPointerException e) {
			Crash.showCrashMessage(app, e, "Couldn't setup activity "+getTitle()+"\nCanvas is NULL", Crash.FRAMEWORK_CRASH);
		}
		
		started=false;
		
		paintThread = new paintThreadClass();
		
	}
	private final void stop() {
		
		paintThread.terminate();
		
		try {
			canvas.Stopped();
		}catch(Exception e) {
			Crash.showCrashMessage(app, e, "Couldn't stop activity "+getTitle(), Crash.FRAMEWORK_CRASH);
		}catch(Error e) {
			Crash.showCrashMessage(app, e, "Couldn't stop activity "+getTitle(), Crash.FRAMEWORK_CRASH);
		}
		
		try {
			Stopped();
		}catch(Exception e) {
			Crash.showCrashMessage(app, e, "There was an exception trying to stop activity "+this.getTitle(), Crash.APPLICATION_CRASH);
		}catch(Error e) {
			Crash.showCrashMessage(app, e, "There was an error trying to stop activity "+this.getTitle(), Crash.APPLICATION_CRASH);
		}
		
	}
	protected void Setup() {}
	protected void posSetup() {}
	protected void Stopped() {}
	
	public final void preloadLayout(int w, int h) {
		try {
			canvas.prepaint(w, h);
		}catch(Exception e) {
			Crash.showCrashMessage(app, e, "There was an exception trying to preload activity "+getTitle(), Crash.FRAMEWORK_CRASH);
		}catch(Error e) {
			Crash.showCrashMessage(app, e, "There was an error trying to preload activity "+getTitle(), Crash.FRAMEWORK_CRASH);
		}
	}
	
	private long lastT;
	protected final void paint(Graphics g) {
		
		if(paintThread==null) {
			Crash.showCrashMessage(app, new IllegalStateException(), "No Paint Thread found\n¿Was the Activity changed by setCurrent() method?", Crash.FRAMEWORK_CRASH);
			return;
		}
			
		try {
		
			if(current!=this) {
				paintThread.askRepaint();
				return;
			}
			
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
					
				try {
					canvas.prepaint(tw, th);
				}catch(Exception e) {
					Crash.showCrashMessage(app, e, "There was an exception trying to prepaint activity "+getTitle(), Crash.FRAMEWORK_CRASH);
				}catch(Error e) {
					Crash.showCrashMessage(app, e, "There was an error trying to prepaint activity "+getTitle(), Crash.FRAMEWORK_CRASH);
				}
					
				if(!started) {
					timeStart=System.currentTimeMillis();
					started=true;
					posSetup();
				}
				
				GraphicsFix gf = new GraphicsFix(g);
				
				try {
					canvas.dispatchPaint(gf);
				}catch(Exception e) {
					Crash.showCrashMessage(app, e, "There was an exception trying to render activity "+getTitle(), Crash.FRAMEWORK_CRASH);
					return;
				}catch(Error e) {
					Crash.showCrashMessage(app, e, "There was an error trying to render activity "+getTitle(), Crash.FRAMEWORK_CRASH);
					return;
				}
				
				g.translate(xToAnimation(0) - g.getTranslateX(), yToAnimation(0) - g.getTranslateY());
				g.setClip(0, 0, getWidth(), getHeight());
				
				try {
					paintLayout(g);
				}catch(Exception e) {
					Crash.showCrashMessage(app, e, "Couldn't paint activity "+getTitle(), Crash.APPLICATION_CRASH);
					return;
				}catch(Error e) {
					Crash.showCrashMessage(app, e, "Couldn't paint activity "+getTitle(), Crash.APPLICATION_CRASH);
					return;
				}
				
				g.translate(0 - g.getTranslateX(), 0 - g.getTranslateY());
				g.setClip(0, 0, getWidth(), getHeight());
				if(navigationBar) {
					
					
					g.translate(0, th);
					g.setClip(0, 0, getWidth(), navheight);
					navbar.prepaint(getWidth(), g.getClipHeight());
					navbar.paint(gf);
					g.translate(0, -th);
					g.setClip(0, 0, getWidth(), getHeight());
					
					
					int nx, ny;
				}
				
			}catch(Exception e) {
				
			}catch(Error e) {}
			
			g.setColor(0xffffff);
			g.setFont(Font.getDefaultFont());
			long sub=System.currentTimeMillis()-lastT;
			if(sub<=0) sub=1;
			deltaTime=(short)sub;
			g.drawString((1000/(sub))+" FPS", 0, 0, Graphics.LEFT|Graphics.TOP);
			
			lastT=System.currentTimeMillis();
			
			painted=true;
			
		}catch(Exception e) {
			Crash.showCrashMessage(app, e, "Exception rendering activity "+getTitle(), Crash.FRAMEWORK_CRASH);
			return;
		}catch(Error e) {
			Crash.showCrashMessage(app, e, "Error rendering activity "+getTitle(), Crash.FRAMEWORK_CRASH);
			return;
		}
		
		if(Config.getShowTouch() && touchAction!=MotionEvent.ACTION_UP) {
			int tSize=Math.min(getWidth(), getHeight())/8;
			g.setColor(142,211,215);
			g.drawArc(touchX-(tSize/2), touchY-(tSize/2), tSize, tSize, 0, 360);
			g.fillRect(touchX, touchY, 1, 1);
		}
		
		paintThread.askRepaint();
		
	}
	
	public final int getDeltaMillis() {
		return deltaTime;
	}
	
	public final double getDeltaSec() {
		return deltaTime/1000.f;
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
		
		if(layout==null) {
			Crash.showCrashMessage(midlet, new NullPointerException(), "Couldn't set current\nActivity is NULL", Crash.APPLICATION_CRASH);
			return;
		}
		
		if(current!=null)
			current.stop();
			
		current=layout;
		System.gc();
		layout.setup();
		try {
			Display.getDisplay(midlet).setCurrent(layout);
		}catch(Exception e) {
			Crash.showCrashMessage(midlet, e, "Couldn't set current", Crash.FRAMEWORK_CRASH);
		}catch(Error e) {
			Crash.showCrashMessage(midlet, e, "Couldn't set current", Crash.FRAMEWORK_CRASH);
		}
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
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_DOWN, keyCode);
		keyEvent(event);
	}
	
	protected final void keyRepeated(int keyCode) {
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_REPEAT, keyCode);
		keyEvent(event);
	}

	protected final void keyReleased(int keyCode){
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_UP, keyCode);
		keyEvent(event);
	}
	
	private final void keyEvent(KeyEvent event) {
		//TODO: Soft keys commands
		
		if(!getFocusedCanvas().dispatchKeyEvent(event.getKeycode(), event)) {
			
		}
		
	}
	
	protected boolean onKey(int keyCode, KeyEvent event) {
		return false;
	}
	
	protected boolean onKey(Content view, int keyCode, KeyEvent event) {
		return false;
	}
	
	public void setFocusedCanvas(pixelyvulpine.contents.Canvas canvas) {
		focused=canvas;
	}
	
	public pixelyvulpine.contents.Canvas getFocusedCanvas() {
		if(focused==null) focused=canvas;
		return focused;
	}
	
	public Content getSelectedView() {
		return getFocusedCanvas().getSelected();
	}
	
	public final boolean isLoaded() {
		return started;
	}
	
	protected boolean onTouchEvent(MotionEvent event) {
		return false;
	}
	
	private Vector historicalCoords = new Vector(0,1);
	private int touchX, touchY, touchAction=MotionEvent.ACTION_UP;
	private void pointerEvent(MotionEvent e) {
		
		touchX=e.getPointerCoords().x;
		touchY=e.getPointerCoords().y;
		touchAction = e.getAction();
		
		if(!canvas.dispatchTouchEvent(e)) {
			onTouchEvent(e);
		}
	}
	
	protected final void pointerPressed(int x, int y){
		
		MotionEvent e = new MotionEvent(historicalCoords, x, y, MotionEvent.ACTION_DOWN);
		pointerEvent(e);

	}

	protected final void pointerReleased(int x, int y){
		
		MotionEvent e = new MotionEvent(historicalCoords, x, y, MotionEvent.ACTION_UP);
		pointerEvent(e);
		
		

	}

	protected final void pointerDragged(int x, int y){
		
		MotionEvent e = new MotionEvent(historicalCoords, x, y, MotionEvent.ACTION_MOVE);
		pointerEvent(e);
		
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
	
	public final CommandListener getCommandListener() {
		return listener;
	}
	
	public final void dispatchCommand(Command c) {
		dispatchCommand(c, null);
	}
	
	public final void dispatchCommand(final Command c, final Content view) {
		final Layout me = this;
		new Thread(new Runnable() {
			
			public void run() {
				if(getCommandListener()!=null && c!=null) {
					
					if(c instanceof pixelyvulpine.api.lcdui.Command)
						((pixelyvulpine.api.lcdui.Command)c).setView(view);
						
					getCommandListener().commandAction(c, me);
				}
			}
		}
		).start();
	}
	
	public final void setFullScreenMode(boolean fullscreen) {
		this.fullscreen=fullscreen;
		if(!fullscreen) {
			setNavigationBar(false);
		}
		super.setFullScreenMode(fullscreen);
	}
	
}
