package pixelyvulpine.contents;

import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.util.GraphicsFix;
import pixelyvulpine.api.events.TextSequenceInput;

public class TextBox extends Content implements TextSequenceInput.OnTextInputListener{
	
	private TextFont font;
	private StringBuffer text=new StringBuffer();
	private TextSequenceInput input = new TextSequenceInput();
	private boolean selected;

	public TextBox(Layout context, DimensionAttributes dimensionAttributes) {
		super(context, dimensionAttributes);
		
		font = new TextFont();
		input.setOnTextInputListener(this);
	}
	
	public TextBox(Layout context, DimensionAttributes dimensionAttributes, TextFont font) {
		this(context, dimensionAttributes);
		
		this.font = new TextFont(font);
	}
	
	public void paint(GraphicsFix g) {
		g.setColor(0xffffff);
		g.fillRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
		font.render(text.toString(), g);
	}
	
	public boolean onKey(int keyCode, KeyEvent ev) {
		
		/*switch(keyCode) {
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
		}*/
		
		
		return ev.dispatch(input);
		
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

	public boolean onCharAdded(char c) {
		
		text.append(c);
		
		return true;
		
	}

	public boolean onCharChanged(char c) {
		
		text.setCharAt(text.length()-1, c);
		return true;
		
	}

	public void onCharFinished(char c) {
		// TODO Auto-generated method stub
		
	}

	public void onCharErase() {
		
		if(text.length()>0)
			text.deleteCharAt(text.length()-1);
		
	}

}
