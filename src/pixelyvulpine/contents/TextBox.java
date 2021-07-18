package pixelyvulpine.contents;

import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.system.UserInput;
import pixelyvulpine.api.util.GraphicsFix;
import pixelyvulpine.api.events.TextSequenceInput;

public class TextBox extends Content implements TextSequenceInput.OnTextInputListener, UserInput.InputListener{
	
	
	
	private TextFont font;
	private StringBuffer text=new StringBuffer();
	private TextSequenceInput input = new TextSequenceInput();
	private boolean multiline, selected;
	private int maxCharacters=2050;
	
	private GestureDetector gesture;

	public TextBox(Layout context, DimensionAttributes dimensionAttributes) {
		super(context, dimensionAttributes);
		
		font = new TextFont();
		input.setOnTextInputListener(this);
		gesture = new GestureDetector(getLayout(), gestureListener);
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
		
		switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
				showUserInput();
				return true;
		}
		
		return ev.dispatch(input);
		
	}
	
	public boolean onTouch(MotionEvent ev) {
		return gesture.onTouchEvent(ev);
	}
	
	private TextBox me=this;
	public GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
		public boolean onSingleTapUp(MotionEvent ev) {
			showUserInput();
			return true;
		}
	};
	
	protected void showUserInput() {
		UserInput.showTextInput(getLayout(), me, text.toString(), maxCharacters);
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
		
		if(c=='\n' && !multiline) return false;
		
		text.append(c);
		
		return true;
		
	}

	public boolean onCharChanged(char c) {
		
		if(c=='\n' && !multiline) return false;
		
		text.setCharAt(text.length()-1, c);
		return true;
		
	}

	public void onCharFinished(char c) {
		
	}

	public void onCharErase() {
		
		if(text.length()>0)
			text.deleteCharAt(text.length()-1);
		
	}

	public void onInputConfirmed(String output) {
		if(text.length()>0) text.delete(0, text.length());
		
		if(multiline)
			text.append(output);
		else {
			char c;
			for(int i=0; i<output.length(); i++) {
				c = output.charAt(i);
				if(c!='\n')
					text.append(c);
			}
		}
	}

	public void onInputCanceled(String input) {}
	
	
	
	public void setMultiline(boolean multiline) {
		this.multiline=multiline;
		font.setMultiline(multiline);
	}
	
	public boolean getMultiline() {
		return multiline;
	}
	
	public void setMaxCharacters(int maxCharacters) {
		this.maxCharacters = maxCharacters;
	}
	
	public int getMaxCharacters() {
		return maxCharacters;
	}
	

}
