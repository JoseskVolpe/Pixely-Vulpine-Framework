package pixelyvulpine.api.lcdui;

//TODO: Events crash

import java.util.Stack;
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
import pixelyvulpine.api.lcdui.DimensionAttributes.Scaled;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.api.util.GraphicsFix;
import pixelyvulpine.contents.ImageView;
import pixelyvulpine.contents.Label;
import pixelyvulpine.contents.List;

public abstract class Layout extends Canvas{
	
	//TODO: Animation class
	//Animations were temporary removed, but will be re-added more modular and easier to use
	/**@deprecated*/protected byte animation;
	
	public static final byte NAVBAR_AUTO=0;
	public static final byte NAVBAR_SHOW=1;
	public static final byte NAVBAR_HIDE=2;
	
	private byte navbar_visibility=NAVBAR_AUTO;
	
	
	protected MIDlet app;
	private static Layout current;
	
	private Color backgroundColor = new Color(69,69,69);
	private Color navigationBarColor = new Color(34,34,34);
	private Color navigationPressColor = new Color(69,69,69);
	
	protected pixelyvulpine.contents.Canvas canvas, overlay;
	private Navbar navbar;
	private Vector commandsMenu = new Vector();
	private Command menu;
	private pixelyvulpine.contents.Canvas focused;
	private boolean fullscreen, painted;
	private short deltaTime;
	private short navHeight;
	
	protected static double x, y;
	
	public Layout(MIDlet app) {
		this.app = app;
		
		canvas = new pixelyvulpine.contents.Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 100)));
		overlay = new pixelyvulpine.contents.Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 100)));
		
		navbar = new Navbar(this);
		navbar.setBackgroundColor(navigationBarColor);
		navbar.setForegroundColor(null);
		navbar.setZIndex(1);
		navbar.setPositioning(Content.POSITIONING_ANCHORED);
		navbar.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		overlay.addContent(navbar);
		focused=canvas;
		super.setCommandListener(activityCommandListener);
		
		CommandList ml = new CommandList(CommandList.PRIORITY_MAIN_COMMANDS);
		mainCommands=ml;
		addCommandList(ml);
		
	}
	
	public final static void notifyPaused() {
		try {
			if(current!=null)
				current.onPause();
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public final static void notifyResumed() {
		try {
			if(current!=null)
				current.onResume();
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public final static void notifyDestroyed() {
		try {
			if(current!=null)
				current.onDestroy();
		}catch(Throwable e) {
			e.printStackTrace();
		}
		current=null;
	}
	
	protected abstract void onOpen(); /**When activity is being open (not called when forced)*/
	protected abstract void onDisplay(); /**When activity is displayed*/
	protected abstract void onPause(); /**When application is paused and current activity is display*/
	protected abstract void onResume(); /**When application is resumed and current activity is display*/
	protected abstract void onClose(); /**When activity is being closed (not called when forced)*/
	protected abstract void onHidden(); /**When activity is out of the view*/
	protected abstract void onDestroy(); /**When application is destroyed and current activity is display*/
	
	public final void preloadLayout(int w, int h) {
		try {
			canvas.dispatchPrepaint(w, h);
		}catch(Exception e) {
			Crash.showCrashMessage(app, e, "There was an exception trying to preload activity "+getTitle(), Crash.FRAMEWORK_CRASH);
		}catch(Error e) {
			Crash.showCrashMessage(app, e, "There was an error trying to preload activity "+getTitle(), Crash.FRAMEWORK_CRASH);
		}
	}
	
	private long lastT;
	protected final void paint(Graphics g) {
		
		if(Crash.hasCrashed())
			return;
		
		Debug.setTask("Render");
		int TraceID;
		
		try {
			
			if(current!=this) {
				if(current!=null) {
					TraceID=Debug.traceObject(current, "onHidden");
					current.onHidden();
					Debug.removeFromTrace(TraceID);
				}
				TraceID=Debug.traceObject(this, "onDisplay");
				this.onDisplay();
				Debug.removeFromTrace(TraceID);
				current=this;
			}
			
			TraceID=Debug.traceObject(this, "paint");
			
			navHeight = (short)Config.getNavbarFont().getHeight();
			if(this.hasPointerEvents()) {
				navHeight=(short)(Config.getNavbarFont().getHeight()*2);
			}
				
			backgroundColor.updateColor(g);
			g.fillRect(xToAnimation(0), yToAnimation(0), getWidth(), getHeight());
					
			int tw=0, th=0;
			if(navbar.isNavbarVisible()) {		
				tw = getWidth();
				th = getHeight()-navHeight;		
			}else {
				tw = getWidth();
				th = getHeight();
			}
					
			g.translate(xToAnimation(0), yToAnimation(0));
					
			canvas.dispatchPrepaint(tw, th);
			overlay.dispatchPrepaint(getWidth(), getHeight());
				
			GraphicsFix gf = new GraphicsFix(g);
				
			canvas.dispatchPaint(gf);
			g.translate(-g.getTranslateX(), -g.getTranslateY());
			g.setClip(0, 0, getWidth(), getHeight());
			overlay.dispatchPaint(gf);
				
			Debug.removeFromTrace(TraceID);
			TraceID = Debug.traceObject(this, "paintLayout");
			paintLayout(g);
			Debug.removeFromTrace(TraceID);
				
			g.translate(0 - g.getTranslateX(), 0 - g.getTranslateY());
			g.setClip(0, 0, getWidth(), getHeight());
			
			g.setColor(0xffffff);
			g.setFont(Font.getDefaultFont());
			long sub=System.currentTimeMillis()-lastT;
			if(sub<=0) sub=1;
			deltaTime=(short)sub;
			g.drawString((1000/(sub))+" FPS", 0, 0, Graphics.LEFT|Graphics.TOP);
			
			lastT=System.currentTimeMillis();
			
			painted=true;
			
			if(Config.getShowTouch() && touchAction!=MotionEvent.ACTION_UP) {
				int tSize=Math.min(getWidth(), getHeight())/8;
				g.setColor(142,211,215);
				g.drawArc(touchX-(tSize/2), touchY-(tSize/2), tSize, tSize, 0, 360);
				g.fillRect(touchX, touchY, 1, 1);
			}
			
			Thread.sleep(10);
		}catch(Throwable e) {
			Crash.showCrashMessage(getMIDlet(), e, "Error rendering activity "+getTitle(), Crash.FRAMEWORK_CRASH);
		}
			
		repaint();
		
		Debug.cleanThreadTrace();
		
	}
	
	public final int getDeltaMillis() {
		return deltaTime;
	}
	
	public final double getDeltaSec() {
		return deltaTime/1000.f;
	}
	
	/**
	 * Custom, advanced rendering method for more experienced developers
	 * @param Graphics
	 */
	protected void paintLayout(Graphics g) {}
	
	
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
	
	public final void setContentAlignment(int alignment) {
		canvas.setContentAlignment(alignment);
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
	
	public final void setNavbarVisibility(byte visibility) {
		this.navbar_visibility=visibility;
	}
	
	public final byte getNavbarVisibility() {
		return navbar_visibility;
	}
	
	public final void openMenu() {
		openMenu(commandsMenu);
	}
	
	public final void openMenu(Vector menu) {
		
		int TraceID = Debug.traceObject(this, "openMenu");
		
		CommandsMenu displayingMenu = new CommandsMenu(this, menu);
		displayingMenu.setZIndex(0);
		overlay.addContent(displayingMenu);
		setFocusedCanvas(displayingMenu);
		
		Debug.removeFromTrace(TraceID);
		
	}
	
	public final boolean closeMenu() {
		int TraceID = Debug.traceObject(this, "closeMenu");
		if(focused instanceof CommandsMenu) {
			focused.dispatchSelected(false);
			overlay.removeContent(focused);
			cancelCommandList(((CommandsMenu) focused).blocker);
			cancelCommandList(((CommandsMenu) focused).cl);
			setFocusedCanvas(((CommandsMenu) focused).lastFocused);
			Debug.removeFromTrace(TraceID);
			return true;
		}
			
		Debug.removeFromTrace(TraceID);
		return false;
	}
	
	public final static Layout getCurrent() {
		return current;
	}
	
	public final static void setCurrent(final MIDlet midlet, final Layout layout) {
		
		if(layout==null) {
			Crash.showCrashMessage(midlet, new NullPointerException(), "Couldn't set current\nActivity is NULL", Crash.APPLICATION_CRASH);
			return;
		}
			
		new Thread(new Runnable() {
			public void run() {
				Debug.setTask("setCurrent "+current+ " -> "+layout);
				Debug.traceObject(new Debug(layout), "setCurrent"); //Trace framework to debugger
				try {
					int TraceID;
					if(current!=null) {
						TraceID = Debug.traceObject(current, "onClose");
						current.onClose();
						Debug.removeFromTrace(TraceID);
					}
					
					System.gc();
					TraceID = Debug.traceObject(current, "onOpen");
					layout.onOpen();
					Debug.removeFromTrace(TraceID);
					Display.getDisplay(midlet).setCurrent(layout);
					layout.repaint();
				}catch(Throwable e) {
					Crash.showCrashMessage(midlet, e, "There was a error setting current Activity", Crash.APPLICATION_CRASH);
				}
				Debug.closeThread();
			}
		}).start();
		
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
	
	/**@deprecated*/
	public final static int xToAnimation(float x) {
		
		return (int)x;
		//return (int)(x+(getCurrent().getWidth()*Layout.x));
		
	}
	
	/**@deprecated*/
	public final static int yToAnimation(float y) {
		
		return (int)y;
		//return (int)(y+(getCurrent().getHeight()*Layout.y));
		
	}
	
	/**@deprecated*/
	private static void animate(int animation) {
		/*
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
		*/
	}
	
	private static boolean pressed;
	
	protected final void keyPressed(int keyCode){
		
		Debug.setTask("keyPressed");
		
		pressed=true;
		
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_DOWN, keyCode);
		keyEvent(event,false);
		
		Debug.closeThread();
	}
	
	protected final void keyRepeated(int keyCode) {
		
		Debug.setTask("keyRepeated");
		
		if(!pressed) return;
		
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_REPEAT, keyCode);
		keyEvent(event,false);
		
		Debug.closeThread();
		
	}

	protected final void keyReleased(int keyCode){
		
		Debug.setTask("keyReleased");
		
		if(!pressed) return;
 		
		pressed=false;
		
		KeyEvent event = new KeyEvent(this, KeyEvent.ACTION_UP, keyCode);
		keyEvent(event, false);
		
		Debug.cleanThreadTrace();
	}
	
	private final void keyEvent(KeyEvent event, boolean symbolic) {
		
		int TraceID = Debug.traceObject(this, "keyEvent");
		
		try {
		
			if(symbolic || !navbar.dispatchKeyEvent(event.getKeycode(), event)) {
				if(!focused.dispatchKeyEvent(event.getKeycode(), event)) {
					
				}
		}
		
		}catch(Throwable t) {
			Crash.showCrashMessage(app, t, "Error in key event", Crash.FRAMEWORK_CRASH);
		}
		
		Debug.removeFromTrace(TraceID);
		
	}
	
	protected boolean onKey(int keyCode, KeyEvent event) {
		return false;
	}
	
	protected boolean onKey(Content view, int keyCode, KeyEvent event) {
		return false;
	}
	
	private void setFocusedCanvas(pixelyvulpine.contents.Canvas canvas) {
		focused=canvas;
	}
	
	public Content getSelectedView() {
		return focused.getSelected();
	}
	
	protected boolean onTouchEvent(MotionEvent event) {
		return false;
	}
	
	private Vector historicalCoords = new Vector(0,1);
	private int touchX, touchY, touchAction=MotionEvent.ACTION_UP;
	private boolean navTouch;
	private void pointerEvent(MotionEvent e) {
		
		try {
		
			touchX=e.getPointerCoords().x;
			touchY=e.getPointerCoords().y;
			touchAction = e.getAction();
			
			if(!overlay.dispatchTouchEvent(e) && !canvas.dispatchTouchEvent(e)) {
				onTouchEvent(e);
			}
		
		}catch(Throwable t) {
			Crash.showCrashMessage(app, t, "Error in pointer event", Crash.FRAMEWORK_CRASH);
		}
	}
	
	protected final void pointerPressed(int x, int y){
		
		Debug.setTask("pointerPressed");
		
		MotionEvent e = new MotionEvent(historicalCoords, x, y, MotionEvent.ACTION_DOWN);
		pointerEvent(e);
		
		Debug.cleanThreadTrace();

	}

	protected final void pointerReleased(int x, int y){
		
		Debug.setTask("pointerReleased");
		
		MotionEvent e = new MotionEvent(historicalCoords, x, y, MotionEvent.ACTION_UP);
		pointerEvent(e);
		
		Debug.cleanThreadTrace();

	}

	protected final void pointerDragged(int x, int y){
		
		Debug.setTask("pointerDragged");
		
		MotionEvent e = new MotionEvent(historicalCoords, x, y, MotionEvent.ACTION_MOVE);
		pointerEvent(e);
		
		Debug.cleanThreadTrace();
		
	}
	
	private CommandListener listener;
	private ActivityCommandListener activityCommandListener = new ActivityCommandListener();
	private CommandList mainCommands = new CommandList(CommandList.PRIORITY_MAIN_COMMANDS);
	private Vector commandLists = new Vector();
	public final void addCommand(Command command) {
		mainCommands.addElement(command);
		updateCommands();
	}

	public final void removeCommand(Command command) {
		mainCommands.removeElement(command);
		updateCommands();
	}
	
	public final void addCommandList(CommandList list) {
		
		//Add element
		if(commandLists.size()<=0) {
			commandLists.addElement(list);
			return;
		}
		
		int i=(commandLists.size()-1)/2;
		int pr = list.getPriority();
		
		int cPr = ((CommandList)commandLists.elementAt(i)).getPriority();
		
		if( cPr < pr) { // Middle's priority is lower than adding command list
			
			while(i<commandLists.size()) {
				
				i++;
				
				if(i>=commandLists.size()) break;
				
				cPr = ((CommandList)commandLists.elementAt(i)).getPriority();
				
				if(cPr<pr) {
					break;
				}
				
			}
			
		}else { //Middle's ZIndex is higher or equal adding content
			
			while(i>0) {
				
				i--;
				cPr = ((CommandList)commandLists.elementAt(i)).getPriority();
				
				if(cPr>=cPr)
					break;
				
			}
			
		}
		
		commandLists.addElement(list);
		//
		
		
		list.assembleContext(this);
		updateCommands();
	}
	
	public final void cancelCommandList(CommandList list) {
		commandLists.removeElement(list);
		list.assembleContext(null);
		updateCommands();
	}
	
	
	/**
	 * @deprecated Will be removed
	 * TODO: Remove function
	 */
	public final void updateCommands(CommandList list) {
		updateCommands();
	}
	
	private Stack displayCommands = new Stack();
	public final void updateCommands() {
		
		int ThreadID = Debug.traceObject(this," updateCommands");
		
		while(!displayCommands.isEmpty())
			super.removeCommand((Command)displayCommands.pop());
		navbar.setBarButton(null, Navbar.LEFT);
		navbar.setBarButton(null, Navbar.CENTER);
		navbar.setBarButton(null, Navbar.RIGHT);
	
		commandsMenu.removeAllElements();
		menu=null;
		
		CommandList current;
		COMMANDUPDATE:
		for(int i=commandLists.size()-1; i>=0; i--) {
			
			current = (CommandList)commandLists.elementAt(i);
			if(i<commandLists.size()-1)
				switch(current.getExclusive()) {
					case CommandList.EXCLUSIVE_IGNORABLE:
						continue;
					case CommandList.EXCLUSIVE_IGNORABLE | CommandList.EXCLUSIVE_STOPPABLE:
						continue;
				}
			
			setNavbarCommands(current);
			
			switch(current.getExclusive()) {
				case CommandList.EXCLUSIVE_STOPPABLE:
					break COMMANDUPDATE;
				case CommandList.EXCLUSIVE_IGNORABLE | CommandList.EXCLUSIVE_STOPPABLE:
					break COMMANDUPDATE;
			}
			
		}
		
		Debug.removeFromTrace(ThreadID);
		
	}
	
	private void setNavbarCommands(CommandList list) {
		if(this.fullscreen) {
			Command left=navbar.left.command, center=navbar.center.command, right=navbar.right.command;
			for(int i=list.size()-1; i>=0; i--) {
				switch(navbar.getSoftPosition(list.getCommand(i).getCommandType())) {
				case Navbar.RIGHT:
					if(right==null || right.getPriority()>=list.getCommand(i).getPriority()) {
						if(right!=null) {
							if(left!=menu) {
								commandsMenu.insertElementAt(left, 0);
								menu = new pixelyvulpine.api.lcdui.Command("Menu", Config.getIcon(Config.ICON_MENU), Command.ITEM, 0);
								left = menu;
							}
							commandsMenu.insertElementAt(right, 0);
						}
						right=list.getCommand(i);
					}else {
						if(left!=menu) {
							commandsMenu.insertElementAt(left, 0);
							menu = new pixelyvulpine.api.lcdui.Command("Menu", Config.getIcon(Config.ICON_MENU), Command.ITEM, 0);
							left = menu;
						}
						commandsMenu.insertElementAt(list.getCommand(i), 0);
					}
					break;
				case Navbar.LEFT:
					if(left==null)
						left=list.getCommand(i);
					else {
						if(left!=menu) {
							commandsMenu.insertElementAt(left, 0);
							menu = new pixelyvulpine.api.lcdui.Command("Menu", Config.getIcon(Config.ICON_MENU), Command.ITEM, 0);
							left = menu;
						}
						commandsMenu.insertElementAt(list.getCommand(i), 0);
					}
					break;
				case Navbar.CENTER:
					if(center==null || center.getPriority()>=list.getCommand(i).getPriority()) {
						if(center!=null){
							if(left!=menu) {
								commandsMenu.insertElementAt(left, 0);
								menu = new pixelyvulpine.api.lcdui.Command("Menu", Config.getIcon(Config.ICON_MENU), Command.ITEM, 0);
								left = menu;
							}
							commandsMenu.insertElementAt(center, 0);
						}
						center=list.getCommand(i);
					}else {
						commandsMenu.insertElementAt(list.getCommand(i), 0);
						if(left!=menu) {
							commandsMenu.insertElementAt(left, 0);
							menu = new pixelyvulpine.api.lcdui.Command("Menu", Config.getIcon(Config.ICON_MENU), Command.ITEM, 0);
							left = menu;
						}
					}
					break;
				}
			}

			this.navbar.setBarButton(left, Navbar.LEFT);
			this.navbar.setBarButton(center, Navbar.CENTER);
			this.navbar.setBarButton(right, Navbar.RIGHT);
		}else{
			for(int i=0; i<list.size(); i++) {
				if(list.getCommand(i).getCommandType() == pixelyvulpine.api.lcdui.Command.CENTER) {
					if(navbar.center.command == null || navbar.center.command.getPriority()>=list.getCommand(i).getPriority())
						navbar.setBarButton(list.getCommand(i), Navbar.CENTER);
					continue;
				}
				super.addCommand(list.getCommand(i));
				displayCommands.addElement(list.getCommand(i));
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
			
			int TraceID = Debug.traceObject(this, "commandAction");
			
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
				
				Debug.removeFromTrace(TraceID);
				return;
			}
			
			if(arg0 == menu) {
				openMenu();
				Debug.removeFromTrace(TraceID);
				return;
			}
			
			if(arg0 instanceof pixelyvulpine.api.lcdui.Command && ((pixelyvulpine.api.lcdui.Command) arg0).getCommandListenerBypass()!=null) {
				Debug.traceObject(((pixelyvulpine.api.lcdui.Command) arg0).getCommandListenerBypass(), "commandAction");
				((pixelyvulpine.api.lcdui.Command) arg0).getCommandListenerBypass().commandAction(arg0, arg1);
				Debug.removeFromTrace(TraceID);
				return;
			}
			//No View's command found
			if(getCommandListener()!=null) {
				int TraceID2 = Debug.traceObject(getCommandListener(), "commandAction");
				getCommandListener().commandAction(arg0, arg1);
				Debug.removeFromTrace(TraceID2);
			}
			
			Debug.removeFromTrace(TraceID);
			
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
					
					Debug.setTask("dispatchCommand "+c.getLabel());
					
					if(c instanceof pixelyvulpine.api.lcdui.Command)
						((pixelyvulpine.api.lcdui.Command)c).setView(view);
						
					activityCommandListener.commandAction(c, me);
					
					Debug.closeThread();
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
		
		public int[] prepaint(int w, int h) {
			super.prepaint(w, navHeight);
			return new int[] {w, navHeight};
		}
		
		public boolean isNavbarVisible() {
			if(!fullscreen || getNavbarVisibility()==NAVBAR_HIDE)
				return false;
			
			return getNavbarVisibility()==NAVBAR_SHOW || left.command!=null || center.command!=null || right.command!=null;
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
		
		public boolean onTouch(MotionEvent ev) {
			super.onTouch(ev);
			return true;
		}
		
		private class NavbarButton extends Content{

			private Command command;
			private Content view;
			private int vW;
			
			public NavbarButton(Layout layout) {
				super(layout, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,33,100)));
				setPositioning(POSITIONING_ANCHORED);
				setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
				
				gd = new GestureDetector(getLayout(), gestureListener);
			}
			
			public int[] prepaint(int w, int h) {
				
				if(view!=null) {
					
					if(view instanceof ImageView) {
						if(((ImageView)view).getImage()!=((pixelyvulpine.api.lcdui.Command)command).getIcon()) {
							((ImageView)view).setImage(((pixelyvulpine.api.lcdui.Command)command).getIcon());
						}
						vW = view.dispatchPrepaint(w, h)[0];
						if(((pixelyvulpine.api.lcdui.Command)command).getIcon()==null)
							updateView();
					}else {
						if(command instanceof pixelyvulpine.api.lcdui.Command && ((pixelyvulpine.api.lcdui.Command)command).getIcon()!=null) {
							updateView();
							vW = view.dispatchPrepaint(w, h)[0];
						}
					}
					if(view instanceof Label) {
						vW = view.getDimension().offset.width;
					}
				}else {
					vW=0;
					h=0;
				}
				
				//System.out.println(view+" "+this.getHorizontalAnchor());
				
				return new int[] {w, h};
			}
			
			protected void paint(GraphicsFix g) {
				
				//System.out.println(view+" "+g.getTranslateX());
				
				if(view!=null) {
					
					int lx = g.getTranslateX();
					int ly = g.getTranslateY();
					
					g.translate((g.getDimensionWidth()/2)-(vW/2), 0);
					if(view instanceof Label) 
						g.translate(0, (navHeight/2)-(view.getDimension().getOffsetDimension().height/2));
					
					view.dispatchPaint(g);
					
					g.translate(-g.getTranslateX()-lx, -g.getTranslateY()+ly);
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
					//TODO: Secondary command
				}
			};
			
			public void setCommand(Command command) {
				
				this.command=command;
				updateView();
				
			}
			
			private void updateView() {
				if(command==null) {
					view=null;
					return;
				}
				
				if(command instanceof pixelyvulpine.api.lcdui.Command && ((pixelyvulpine.api.lcdui.Command) command).getIcon()!=null) {
					
					view = new ImageView(getLayout(), ((pixelyvulpine.api.lcdui.Command) command).getIcon(), new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 100), new DimensionAttributes.Offset(0, 0, 0, 0)), true);
					return;
				}
				
				view = new Label(getLayout(), new DimensionAttributes(), command.getLabel(), Config.getNavbarFont());
				((Label)view).setColor(new Color(255,255,255)); //TODO: Adapt text color
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

	private class CommandsMenu extends pixelyvulpine.contents.Canvas implements CommandListener{
		
		private CommandList cl, blocker;
		private pixelyvulpine.api.lcdui.Command back;
		private pixelyvulpine.contents.Canvas lastFocused;
		private List l;
		
		CommandsMenu(Layout layout, Vector commands){
			super(layout, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 100), new DimensionAttributes.Offset(0, 0, 0, -navHeight)));
			
			this.setBackgroundColor(new Color(200, 0,0,0)); //TODO: Allow developer to change menu's background color
			
			l = new List(layout, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,100)), commands);
			l.setCommandListener(this);
			l.setTextColor(new Color(255,255,255));
			this.addContent(l);
			
			lastFocused = focused;
			
			blocker = new CommandList(CommandList.PRIORITY_APPLICATION);
			blocker.setExclusive(CommandList.EXCLUSIVE_STOPPABLE);
			addCommandList(blocker);
			
			cl = new CommandList(CommandList.PRIORITY_POPUP);
			back = new pixelyvulpine.api.lcdui.Command("Back", Config.getIcon(Config.ICON_BACK), Command.BACK, 0);
			back.setCommandListenerBypass(this);
			cl.addElement(back);
			addCommandList(cl);
		}
		
		public boolean onTouch(MotionEvent ev){
			super.onTouch(ev);
			return true;
		}

		public void commandAction(Command arg0, Displayable arg1) {
			if(arg0==back) {
				closeMenu();
				return;
			}
			
			getLayout().dispatchCommand(arg0);
			closeMenu();
		}
	}
	
}
