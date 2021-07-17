package pixelyvulpine.contents;

import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.util.GraphicsFix;

public class TextBox extends Content{
	
	private TextFont font;
	private StringBuffer text=new StringBuffer();
	private boolean selected;

	public TextBox(Layout context, DimensionAttributes dimensionAttributes) {
		super(context, dimensionAttributes);
		
		font = new TextFont();
	}
	
	public TextBox(Layout context, DimensionAttributes dimensionAttributes, TextFont font) {
		this(context, dimensionAttributes);
		
		this.font = font;
	}
	
	public void paint(GraphicsFix g) {
		g.setColor(0xffffff);
		g.fillRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
		font.render(text.toString(), g);
	}
	
	public boolean onKey(int keyCode, KeyEvent ev) {
		
		if(ev.getAction()==KeyEvent.ACTION_UP) return false;
		
		switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				return false;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				return false;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				return false;
			case KeyEvent.KEYCODE_DPAD_UP:
				return false;
			case KeyEvent.KEYCODE_DEL:
				if(text.length()>0)
					text.deleteCharAt(text.length()-1);
				return true;
			case KeyEvent.KEYCODE_ENTER:
				return false;
		}
		
		text.append(ev.getChar());
		
		return true;
		
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
