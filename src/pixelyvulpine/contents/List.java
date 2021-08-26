package pixelyvulpine.contents;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;

import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.GraphicsFix;

public class List extends Content{

	private ScrollableCanvas canvas;
	private Vector commands = new Vector(0,1); //Command
	private Vector commandContents = new Vector(0,1); //Canvas
	private Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
	
	public List(Layout layout, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
		forcePaint=true;
		
		canvas=new ScrollableCanvas(layout, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,100)));
		
	}
	
	public List(Layout layout, DimensionAttributes dimensionAttributes, Command[] commands) {
		this(layout, dimensionAttributes);
		
		if(commands!=null) {
			for(int i=0; i<commands.length; i++) {
				add(commands[i]);
			}
		}
	}
	
	public List(Layout layout, DimensionAttributes dimensionAttributes, Vector commands) {
		this(layout, dimensionAttributes);
		
		if(commands!=null) {
			for(int i=0; i<commands.size(); i++) {
				add((Command)commands.elementAt(i));
			}
		}
	}
	
	public void noPaint() {
		canvas.noPaint();
	}
	
	public int[] prepaint(int w, int h) {
		
		canvas.prepaint(w, h);
		
		return new int[] {w,h};
	}
	
	public void paint(GraphicsFix g) {
		
		canvas.dispatchPaint(g);
	}
	
	public void add(Command c) {
		commands.addElement(c);
		
		ListItem cc = new ListItem(getLayout(), c);
		
		commandContents.addElement(cc);
		canvas.addContent(cc);
		
	}
	
	public boolean remove(Command c) {
		
		int i = commands.indexOf(c);
		if(i<0) return false;
		
		canvas.removeContent((Content)commandContents.elementAt(i));
		commandContents.removeElementAt(i);
		commands.removeElementAt(i);
		return true;
	}
	
	public Command getCommand(int index) {
		return ((Command)commands.elementAt(index));
	}
	
	public boolean onTouch(MotionEvent e) {
		return canvas.onTouch(e);
	}
	
	public boolean onKey(int keycode, KeyEvent ev) {
		return canvas.onKey(keycode, ev);
	}
	
	public boolean isSelectable() {
		return canvas.isSelectable();
	}
	
	public void onSelect() {
		canvas.onSelect();
	}
	
	public void onDeselect() {
		canvas.onDeselect();
	}

	private class CommandIcon extends ImageView{

		private Command c;
		
		public CommandIcon(Layout layout, Command c) {
			super(layout, null, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,100), new DimensionAttributes.Offset(0,0,0,0)), true);
			
			this.c=c;
			
			if(c instanceof pixelyvulpine.api.lcdui.Command)
				setImage(((pixelyvulpine.api.lcdui.Command)c).getIcon());
		}
		
		public int[] prepaint(int w, int h) {
			
			if(c!=null && c instanceof pixelyvulpine.api.lcdui.Command) {
			
				if(getImage()!=((pixelyvulpine.api.lcdui.Command)c).getIcon())
					setImage(((pixelyvulpine.api.lcdui.Command)c).getIcon());
				
				return super.prepaint(w, h);
			
			}else {
				return new int[] {0,0};
			}
		}
		
	}
	
	private class ListItem extends Content{

		private boolean selected;
		private Command command;
		private CommandIcon icon;
		private Label label;
		private int[] iconDim;
		
		public ListItem(Layout layout, Command command) {
			super(layout, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 0), new DimensionAttributes.Offset(0, 0, 0, Font.getDefaultFont().getHeight()+4)));
			
			this.command = command;
			icon = new CommandIcon(layout, command);
			label = new Label(layout, new DimensionAttributes(), command.getLabel());
		}
		
		public int[] prepaint(int w, int h) {
			
			iconDim = icon.prepaint(w, h-4);
			label.prepaint(w-iconDim[0], h-4);
			
			return new int[] {w, h};
		}
		
		public void paint(GraphicsFix g) {
			
			if(selected) {
				g.setColor(0x1212ff);
				g.fillRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
				g.setColor(0x0000ff);
				g.drawRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
			}
			
			g.translate(0, 2);
			icon.dispatchPaint(g);
			g.translate(iconDim[0], 0);
			label.dispatchPaint(g);
			
		}
		
		public boolean onKey(int key, KeyEvent ev) {
			return false;	
		}
		
		public boolean onTouch(MotionEvent ev) {
			return false;
		}
		
		public boolean isSelectable() {
			return true;
		}
		
		public void onSelect() {
			selected=true;
		}
		
		public void onDeselect() {
			selected=false;
		}
		
	}
	
}
