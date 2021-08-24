package pixelyvulpine.api.lcdui;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.Config;
import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.api.util.GraphicsFix;
import pixelyvulpine.contents.ImageView;
import pixelyvulpine.contents.Label;

public class Layout extends Canvas{
	
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
	
	private static long timeStart;
	private static Layout current;
	private static boolean started;
	
	private Color backgroundColor = new Color(69,69,69);
	private Color navigationBarColor = new Color(34,34,34);
	private Color navigationPressColor = new Color(69,69,69);
	
	protected pixelyvulpine.contents.Canvas canvas;
	private Navbar navbar;
	private pixelyvulpine.contents.Canvas focused;
	private boolean fullscreen, painted;
	private short deltaTime;
	private short navHeight;
	
	
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
		
		navbar = new Navbar(this);
		navbar.setBackgroundColor(navigationBarColor);
		navbar.setForegroundColor(null);
		focused=canvas;
		super.setCommandListener(activityCommandListener);
		
		CommandList ml = new CommandList(CommandList.PRIORITY_MAIN_COMMANDS);
		mainCommands=ml;
		addCommandList(ml);
		
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
		
		navHeight = (short)Config.getNavbarFont().getHeight();
		if(this.hasPointerEvents()) {
			navHeight=(short) (g.getClipHeight()/13);
		}
		
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
				if(fullscreen && currentCL!=null && currentCL.size()>0) {
						
						
					tw = getWidth();
					th = getHeight()-navHeight;
						
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
				if(fullscreen && currentCL!=null && currentCL.size()>0) {
					
					
					g.translate(0, th);
					g.setClip(0, 0, getWidth(), navHeight);
					navbar.prepaint(getWidth(), g.getClipHeight());
					navbar.dispatchPaint(gf);
					g.translate(0, -th);
					g.setClip(0, 0, getWidth(), getHeight());
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
	 * @return remove successfully
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
	
	/**@deprecated**/
	public final static void setNavigationBar(boolean navigationBar) {
		
	}
	
	/**@deprecated**/
	public final static boolean getNavigationBar() {
		return false;
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
	
	private static boolean pressed;
	
	protected final void keyPressed(int keyCode){
		
		pressed=true;
		
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_DOWN, keyCode);
		keyEvent(event,false);
	}
	
	protected final void keyRepeated(int keyCode) {
		
		if(!pressed) return;
		
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_REPEAT, keyCode);
		keyEvent(event,false);
	}

	protected final void keyReleased(int keyCode){
		
		if(!pressed) return;
 		
		pressed=false;
		
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_UP, keyCode);
		keyEvent(event, false);
	}
	
	private final void keyEvent(KeyEvent event, boolean symbolic) {
		
		if(symbolic || !navbar.dispatchKeyEvent(event.getKeycode(), event)) {
			if(!getFocusedCanvas().dispatchKeyEvent(event.getKeycode(), event)) {
				
			}
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
	private boolean navTouch;
	private void pointerEvent(MotionEvent e) {
		
		touchX=e.getPointerCoords().x;
		touchY=e.getPointerCoords().y;
		touchAction = e.getAction();
		
		if(navTouch || (e.getAction()==MotionEvent.ACTION_DOWN && e.getPointerCoords().y>=this.getHeight()-navHeight && fullscreen && currentCL!=null && currentCL.size()>0)) {
			
			if(e.getAction() == MotionEvent.ACTION_DOWN) 
				navTouch=true;
			else if(e.getAction()==MotionEvent.ACTION_UP)
				navTouch=false;
			
			e.getPointerCoords().y -= this.getHeight()-navHeight;
			
			navbar.dispatchTouchEvent(e);
			
			return;
		}
		
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
	
	private CommandListener listener;
	private ActivityCommandListener activityCommandListener = new ActivityCommandListener();
	private CommandList mainCommands = new CommandList(CommandList.PRIORITY_MAIN_COMMANDS);
	private Vector commandLists = new Vector();
	private CommandList currentCL;
	public final void addCommand(Command command) {
		mainCommands.addCommand(command);
		updateCommands(mainCommands);
	}

	public final void removeCommand(Command command) {
		mainCommands.removeCommand(command);
		updateCommands(mainCommands);
	}
	
	public final void addCommandList(CommandList list) {
		commandLists.addElement(list);
		list.assembleContext(this);
		updateCommands();
	}
	
	public final void cancelCommandList(CommandList list) {
		commandLists.removeElement(list);
		list.assembleContext(null);
		if(currentCL==list) {
			for(int i=0; i<currentCL.size(); i++) {
				super.removeCommand(currentCL.getCommand(i));
			}
			currentCL=null;
		}
		updateCommands();
	}
	
	public final void updateCommands(CommandList list) {
		if(list==currentCL) {
			updateCommands();
		}
	}
	
	private void updateCommands() {
		
		CommandList current=currentCL;
		for(int i=commandLists.size()-1; i>=0; i--) {
			if(current==null || current.getPriority()>((CommandList)commandLists.elementAt(i)).getPriority())
				current=(CommandList)commandLists.elementAt(i);
		}
		
		if(currentCL!=null)
			for(int i=0; i<currentCL.size(); i++) {
				super.removeCommand(currentCL.getCommand(i));
			}
		navbar.setBarButton(null, Navbar.LEFT);
		navbar.setBarButton(null, Navbar.CENTER);
		navbar.setBarButton(null, Navbar.RIGHT);
		
		currentCL=current;
		
		if(this.fullscreen) {
			//TODO: Update custom navbar
			Command left=null, center=null, right=null;
			for(int i=currentCL.size()-1; i>=0; i--) {
				switch(navbar.getSoftPosition(currentCL.getCommand(i).getCommandType())) {
				case Navbar.RIGHT:
					if(right==null || right.getPriority()>=currentCL.getCommand(i).getPriority())
						right=currentCL.getCommand(i);
					break;
				case Navbar.LEFT:
					if(left==null || left.getPriority()>=currentCL.getCommand(i).getPriority())
						left=currentCL.getCommand(i);
					break;
				case Navbar.CENTER:
					if(center==null || center.getPriority()>=currentCL.getCommand(i).getPriority())
						center=currentCL.getCommand(i);
					break;
				}
			}
			
			this.navbar.setBarButton(left, Navbar.LEFT);
			this.navbar.setBarButton(center, Navbar.CENTER);
			this.navbar.setBarButton(right, Navbar.RIGHT);
		}else{
			for(int i=0; i<currentCL.size(); i++) {
				if(currentCL.getCommand(i).getCommandType() == pixelyvulpine.api.lcdui.Command.CENTER) {
					if(navbar.center.command == null || navbar.center.command.getPriority()>=currentCL.getCommand(i).getPriority())
						navbar.setBarButton(currentCL.getCommand(i), Navbar.CENTER);
					continue;
				}
				super.addCommand(currentCL.getCommand(i));
			}
			
		}
		
	}
	
	public final void setCommandListener(CommandListener l) {
		listener=l;
	}
	
	public final CommandListener getCommandListener() {
		return listener;
	}
	
	private final class ActivityCommandListener implements CommandListener{

		public void commandAction(Command arg0, Displayable arg1) {
			
			if(arg0 instanceof pixelyvulpine.api.lcdui.Command && ((pixelyvulpine.api.lcdui.Command) arg0).isSymbolic()) {
				
				switch(navbar.getSoftPosition(arg0.getCommandType())) {
					case Navbar.LEFT:
						keyEvent(new KeyEvent(current, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SOFT_LEFT), true);
					break;
					case Navbar.CENTER:
						keyEvent(new KeyEvent(current, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_CENTER), true);
					break;
					case Navbar.RIGHT:
						keyEvent(new KeyEvent(current, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SOFT_RIGHT), true);
					break;
				}
				
				return;
			}
			
			if(arg0 instanceof pixelyvulpine.api.lcdui.Command && ((pixelyvulpine.api.lcdui.Command) arg0).getCommandListenerBypass()!=null) {
				((pixelyvulpine.api.lcdui.Command) arg0).getCommandListenerBypass().commandAction(arg0, arg1);
				return;
			}
			//No View's command found
			if(getCommandListener()!=null) 
				getCommandListener().commandAction(arg0, arg1);
			
		}
		
	}
	
	public final void dispatchCommand(Command c) {
		dispatchCommand(c, null);
	}
	
	
	public final void dispatchCommand(final Command c, final Content view) {
		if(c==null) return;
	
		final Layout me = this;
		new Thread(new Runnable() {
			
			public void run() {
				if(c!=null) {
					
					if(c instanceof pixelyvulpine.api.lcdui.Command)
						((pixelyvulpine.api.lcdui.Command)c).setView(view);
						
					activityCommandListener.commandAction(c, me);
				}
			}
		}
		).start();
	}
	
	public void setFullScreenMode(boolean fullscreen) {
		this.fullscreen=fullscreen;
		super.setFullScreenMode(fullscreen);
	}
	
	private static Command downCommand;
	private class Navbar extends pixelyvulpine.contents.Canvas {

		public static final byte LEFT=-1;
		public static final byte CENTER=0;
		public static final byte RIGHT=1;
		
		public int getSoftPosition(int commandType) {
			switch(commandType) {
			case Command.BACK:
				return RIGHT;
			case Command.CANCEL:
				return RIGHT;
			case Command.EXIT:
				return RIGHT;
			case Command.HELP:
				return LEFT;
			case Command.ITEM:
				return LEFT;
			case Command.OK:
				return LEFT;
			case Command.SCREEN:
				return LEFT;
			case Command.STOP:
				return RIGHT;
			case pixelyvulpine.api.lcdui.Command.CENTER:
				return CENTER;
			default:
				throw new IllegalArgumentException();
			}
		}
		
		private NavbarButton left, center, right;
		
		public Navbar(Layout activity) {
			super(activity, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,0)));
			
			this.setArrangement(pixelyvulpine.contents.Canvas.ARRANGEMENT_HORIZONTAL);
			
			left = new NavbarButton(activity);
			left.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
			center = new NavbarButton(activity);
			center.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
			right = new NavbarButton(activity);
			right.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
			
			this.addContent(left);
			this.addContent(center);
			this.addContent(right);
		}
		
		public void setBarButton(Command command, int side) {
			switch(side) {
				case LEFT:
					left.setCommand(command);
					break;
				case CENTER:
					center.setCommand(command);
					break;
				case RIGHT:
					right.setCommand(command);
					break;
			
			}
		}
		
		public boolean onKey(int keycode, KeyEvent ev) {
			
			switch(keycode) {
				case KeyEvent.KEYCODE_SOFT_LEFT:
					return left.callCommand(ev);
				case KeyEvent.KEYCODE_SOFT_RIGHT:
					return right.callCommand(ev);
				case KeyEvent.KEYCODE_DPAD_CENTER:
					return center.callCommand(ev);
			}
			
			return false;
		}
		
		private class NavbarButton extends Content{

			private Command command;
			private Content view;
			
			public NavbarButton(Layout layout) {
				super(layout, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,100)));
				setPositioning(POSITIONING_ANCHORED);
				setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
				
				gd = new GestureDetector(getLayout(), gestureListener);
			}
			
			public int[] prepaint(int w, int h) {
				
				if(view!=null) {
					if(view instanceof ImageView) {
						w = view.prepaint(w, h)[0];
					}
					if(view instanceof Label) {
						w = view.getDimension().offset.width;
					}
				}else {
					w=0;
					h=0;
				}
				
				//System.out.println(view+" "+this.getHorizontalAnchor());
				
				return new int[] {w, h};
			}
			
			protected void paint(GraphicsFix g) {
				
				//System.out.println(view+" "+g.getTranslateX());
				
				if(view!=null) {
					
					int ly = g.getTranslateY();
					
					if(view instanceof Label) 
						g.translate(0, (navHeight/2)-(view.getDimension().getOffsetDimension().height/2));
					
					view.dispatchPaint(g);
					
					g.translate(0, -g.getTranslateY()+ly);
				}
			}
			
			private GestureDetector gd;
			
			public boolean onTouch(MotionEvent ev) {
				
				boolean ret = gd.onTouchEvent(ev);
				
				switch(ev.getAction()) {
					case MotionEvent.ACTION_DOWN:
						downCommand=command;
					break;
				}
				
				return ret;
				
			}
			
			protected GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
				public boolean onSingleTapUp(MotionEvent ev) {
					if(command!=null && command==downCommand)
						dispatchCommand(command);
						
					downCommand=null;
					
					return true;
				}
				public void onLongPress(MotionEvent ev) {
					
				}
			};
			
			public void setCommand(Command command) {
				
				this.command=command;
				if(command==null) {
					view=null;
					return;
				}
				
				if(command instanceof pixelyvulpine.api.lcdui.Command && ((pixelyvulpine.api.lcdui.Command) command).getIcon()!=null) {
					
					view = new ImageView(getLayout(), ((pixelyvulpine.api.lcdui.Command) command).getIcon(), new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 100), new DimensionAttributes.Offset(0, 0, 0, 0)), true);
					return;
				}
				
				view = new Label(getLayout(), new DimensionAttributes(), command.getLabel(), Config.getNavbarFont());
				((Label)view).impact();
				
			}
			
			public boolean callCommand(KeyEvent ev) {
				
				if(ev.getAction()==KeyEvent.ACTION_DOWN)
					downCommand=command;
				
				if(command==null || (command instanceof pixelyvulpine.api.lcdui.Command && ((pixelyvulpine.api.lcdui.Command) command).isSymbolic())) return false;
				
				if(ev.getAction()==KeyEvent.ACTION_UP && downCommand==command)
					dispatchCommand(command);
				return true;
			}
			
		}
		
	}
	
}
