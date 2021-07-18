package pixelyvulpine.contents;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.Paragraph;
import pixelyvulpine.api.system.UserInput;
import pixelyvulpine.api.util.GraphicsFix;
import pixelyvulpine.api.events.TextSequenceInput;

public class TextBox extends Content implements TextSequenceInput.OnTextInputListener, UserInput.InputListener{
	
	
	protected Paragraph par;
	protected StringBuffer text=new StringBuffer();
	protected TextSequenceInput input = new TextSequenceInput();
	private boolean multiline, selected;
	private int maxCharacters=2050;
	private int caret;
	
	private GestureDetector gesture;

	public TextBox(Layout context, DimensionAttributes dimensionAttributes) {
		super(context, dimensionAttributes);
		
		input.setOnTextInputListener(this);
		gesture = new GestureDetector(getLayout(), gestureListener);
		par = new Paragraph(Font.getDefaultFont());
		
	}
	
	public TextBox(Layout context, DimensionAttributes dimensionAttributes, Font font) {
		this(context, dimensionAttributes);
		
		par.setFont(font);
	}
	
	public int[] prepaint(int w, int h) {
		
		par.setText(text.toString());
		par.prepareDimension(w, h);
		
		return new int[] {w,h};
	}
	
	public void paint(GraphicsFix g) {

		int tx = g.getTranslateX();
		int ty = g.getTranslateY();
		int dw = g.getDimensionWidth();
		int dh = g.getDimensionHeight();
		int cw = g.getClipWidth();
		int ch = g.getClipHeight();
		
		paintBackground(g);
		g.translate(tx-g.getTranslateX(), ty-g.getTranslateY());
		g.setDimension(dw, dh);
		g.setClip(0, 0, cw, ch);
		
		if(multiline) {
			paintMultiline(g);
		}else {
			paintSingleline(g);
		}
		g.translate(tx-g.getTranslateX(), ty-g.getTranslateY());
		g.setDimension(dw, dh);
		g.setClip(0, 0, cw, ch);
		
		paintInputMode(g);
		g.translate(tx-g.getTranslateX(), ty-g.getTranslateY());
		g.setDimension(dw, dh);
		g.setClip(0, 0, cw, ch);
		
		paintForeground(g);
		g.translate(tx-g.getTranslateX(), ty-g.getTranslateY());
		g.setDimension(dw, dh);
		g.setClip(0, 0, cw, ch);
	}
	
	protected void paintBackground(GraphicsFix g) {
		g.setColor(0xffffff);
		g.fillRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
	}
	
	protected void paintForeground(GraphicsFix g){
		if(selected) {
			g.setColor(50,50,255);
			g.drawRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
		}
	}
	
	protected void paintInputMode(GraphicsFix g) {
		String m="";
		
		switch(input.getInputMode()) {
			case TextSequenceInput.INPUT_SHIFT:
				m="Ab";
				break;
			case TextSequenceInput.INPUT_CAPSLOCK:
				m="AB";
				break;
			case TextSequenceInput.INPUT_LOWERCASE:
				m="ab";
				break;
			case TextSequenceInput.INPUT_NUMERIC:
				m="12";
				break;
		}
		
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		g.drawString(m, g.getDimensionWidth(), g.getDimensionHeight(), Graphics.BOTTOM|Graphics.RIGHT);
		
	}
	
	protected void paintMultiline(GraphicsFix g) {
		
		
	}
	
	protected void paintSingleline(GraphicsFix g) {

		g.clipRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());

		int tx = g.getTranslateX();
		int ty = g.getTranslateY();
		
		g.translate(0, (g.getDimensionHeight()/2)-(par.getFont().getHeight()/2));
		
		renderCaret(g);
		
		g.setColor(0x000000);
		par.render(g);
		
		g.translate(tx-g.getTranslateX(), ty-g.getTranslateY());
		
	}
	
	protected void paintCaret(GraphicsFix g, boolean selected) {
		g.setColor(70,70, 150);
		if(!selected) {
			g.drawLine(g.getDimensionWidth()-1, 0, g.getDimensionWidth()-1, g.getDimensionHeight());
		}else {
			g.fillRect(0, 0, g.getDimensionWidth(), g.getDimensionHeight());
		}
	}
	
	protected final void renderCaret(GraphicsFix g) {
		
		int x, y, w, h;
		h=par.getFont().getHeight();
		
		if(caret>0) {
			w=par.getFont().charWidth(text.charAt(caret-1));
			y=par.getCharYFromIndex(caret-1);
			x=par.getCharXFromIndex(caret-1);
		}else {
			x=par.getStartX();
			y=0;
			w=1;
		}
		
		int tx = g.getTranslateX();
		int ty = g.getTranslateY();
		int dw = g.getDimensionWidth();
		int dh = g.getDimensionHeight();
		int cw = g.getClipWidth();
		int ch = g.getClipHeight();
		
		g.translate(x,y);
		g.setDimension(w, h);
		paintCaret(g, input.isSelecting());
		
		g.translate(tx-g.getTranslateX(), ty-g.getTranslateY());
		g.setDimension(dw, dh);
		g.setClip(0, 0, cw, ch);
		
	}
	
	public boolean onKey(int keyCode, KeyEvent ev) {
		
		switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
				showUserInput();
				return true;
		}
		
		if(ev.getAction() == KeyEvent.ACTION_DOWN || ev.getAction() == KeyEvent.ACTION_REPEAT) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (input.finishSequence()) return true;
				return false;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (input.finishSequence()) return true;
				if(caret>0) {
					caret--;
					return true;
				}
				return false;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (input.finishSequence()) return true;
				if(caret<text.length()) {
					caret++;
					return true;
				}
				return false;
			case KeyEvent.KEYCODE_DPAD_UP:
				if (input.finishSequence()) return true;
				return false;
			}
		}
		
		return ev.dispatch(input);
		
	}
	
	public boolean onTouch(MotionEvent ev) {
		return gesture.onTouchEvent(ev);
	}
	
	public GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
		public boolean onSingleTapUp(MotionEvent ev) {
			showUserInput();
			return true;
		}
	};
	
	protected void showUserInput() {
		UserInput.showTextInput(getLayout(), this, text.toString(), maxCharacters);
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
		
		if(text.length()>=maxCharacters) return false;
		
		if(c=='\n' && !multiline) return false;
		
		text.insert(caret, c);
		caret++;
		
		return true;
		
	}

	public boolean onCharChanged(char c) {
		
		if(c=='\n' && !multiline) return false;
		
		int index = caret-1;
		if(caret<0) index=0;
		
		text.setCharAt(index, c);
		return true;
		
	}

	public void onCharFinished(char c) {
		
	}

	public void onCharErase() {
		
		if(caret>0) {
			text.deleteCharAt(caret-1);
			caret--;
			if(caret<0) caret=0;
			if(caret>text.length() && text.length()>0) caret=text.length();
		}
		
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
		
		caret=text.length();
	}

	public void onInputCanceled(String input) {}
	
	
	
	public void setMultiline(boolean multiline) {
		this.multiline=multiline;
		par.setMultiline(multiline);
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
	
	public int getCaretPosition() {
		return caret;
	}
	
	public void setText(String text) {
		this.text = new StringBuffer(text);
		caret = text.length();
		par.setText(text);
	}
	
	public String getText() {
		return text.toString();
	}
	
	public void setFont(Font font) {
		par.setFont(font);
	}
	
	public Font getFont() {
		return par.getFont();
	}

}
