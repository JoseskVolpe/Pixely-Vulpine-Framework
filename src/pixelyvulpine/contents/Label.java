package pixelyvulpine.contents;

import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.system.Crash;

public class Label extends Content{
	
	public static final Color NO_BACKGROUND = null;
	
	private TextFont font = new TextFont();
	private String text = "Label";
	private Color backgroundColor = NO_BACKGROUND;
	
	public Label(Layout layout, int[] x, int[] y, int[] width, int[] height) {
		
		super(layout, x, y, width, height);
		
	}
	
	public Label(Layout layout, int[] x, int[] y, int[] width, int[] height, String text) {
		super(layout, x, y, width, height);
		this.text = text;
		
	}
	
	public Label(Layout layout, int[] x, int[] y, int[] width, int[] height, String text, TextFont font) {
		super(layout, x, y, width, height);
		this.text = text;
		setTextFont(font);
	}
	
	public void paint(Graphics g) {
		font.render(text, g);
	}
	
	public final void setText(String text) {
		this.text = text;
	}
	
	public final String getText() {
		return text;
	}
	
	public final void setTextFont(TextFont font) {
		this.font = font;
	}
	
	public final TextFont getTextFont() {
		return font;
	}
	
	public final void setColor(Color fontColor){
		font.setColor(fontColor);
	}
	
	public final Color getColor() {
		return font.getColor();
	}
	
	public final void setFont(TextFont font) {
		this.font = font;
	}
	
	public final TextFont getFont() {
		return font;
	}
	
	public final void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public final Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public final void setMultiline(boolean multiline) {
		font.setMultiline(multiline);
	}
	
	public final boolean getMultiline() {
		return font.getMultiline();
	}
	
	/**
	 * Sets Label's size to text's size
	 * It'll only work on non-multiline texts
	 * 
	 * @return Impact sucessful
	 */
	public final boolean impact() {
		
		try {
		
			if(font==null || font.getMultiline()) return false;
			
			int w=0, h=0;
			
			w=font.getFont().stringWidth(text.toString());
			h=font.getFont().getHeight();
			
			this.setWidth(new int[] {0, w});
			this.setHeight(new int[] {0, h});
			
			return true;
		
		}catch(Exception e) {
			Crash.showCrashMessage(this.getLayout().getMIDlet(), e, "Could not impact Label '"+this.getText()+"'", Crash.FRAMEWORK_CRASH);
		}catch(Error e) {
			Crash.showCrashMessage(this.getLayout().getMIDlet(), e, "Could not impact Label '"+this.getText()+"'", Crash.FRAMEWORK_CRASH);
		}
		
		return false;
		
	}

}
