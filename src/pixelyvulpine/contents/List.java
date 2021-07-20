package pixelyvulpine.contents;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;

import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.GraphicsFix;

public class List extends Content{

	private ScrollableCanvas canvas;
	private Vector commands = new Vector(0,1); //Command
	private Vector commandContents = new Vector(0,1); //Canvas
	private Font font = Font.getDefaultFont();
	
	public List(Layout layout, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
		forcePaint=true;
		
		canvas=new ScrollableCanvas(layout, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,100)));
		canvas.setForegroundColor(new Color(0,0,0)); //TODO REMOVEME
		
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
		
		Canvas cc = new Canvas(getLayout(), new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,0), new DimensionAttributes.Offset(0,0,0,font.getHeight()+4)));
		cc.setArrangement(Canvas.ARRANGEMENT_HORIZONTAL);
		cc.setContentAlignment(Canvas.ALIGNMENT_CENTER);
		
		if(c instanceof pixelyvulpine.api.lcdui.Command && ((pixelyvulpine.api.lcdui.Command) c).getIcon() != null) {
			ImageView i = new ImageView(getLayout(), ((pixelyvulpine.api.lcdui.Command) c).getIcon(), 0, 0, 0, 0);
			i.setScalePictureToFit(true);
			cc.addContent(i);
		}
		
		Button b = new Button(getLayout(), new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,100), new DimensionAttributes.Offset(0,0,0,0)), c.getLabel());
		b.setFont(font);
		cc.addContent(b);
		canvas.addContent(cc);
		
		commandContents.addElement(cc);
		
	}
	
	public boolean remove(Command c) {
		
		int i = commands.indexOf(c);
		if(i<0) return false;
		
		commandContents.removeElementAt(i);
		commands.removeElementAt(i);
		return true;
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

}
