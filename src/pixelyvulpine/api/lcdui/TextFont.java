package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.util.GraphicsFix;

public class TextFont {
	
	private Font font;
	/**@deprecated**/private boolean multiline;
	
	public TextFont() {
		this.font = Font.getDefaultFont();
	}
	
	public TextFont(TextFont font) {
		this.font = Font.getFont(font.getFont().getFace(), font.getFont().getStyle(), font.getFont().getSize());
		this.multiline=font.getMultiline();
	}
	
	public TextFont(boolean multiline) {
		this.font = Font.getDefaultFont();
		this.multiline = multiline;
	}
	
	public TextFont(Font font, boolean multiline) {
		this.font = font;
		this.multiline = multiline;
	}
	
	public TextFont(Font font) {
		this.font = font;
	}
	
	public TextFont(int face, int style, int size) {
		this.font = Font.getFont(face, style, size);
	}

	/**@deprecated**/
	public TextFont(int face, int style, int size, boolean multiline) {
		this.font = Font.getFont(face, style, size);
		this.multiline=multiline;
	}
	
	/**@deprecated**/
	public final void render(String text, GraphicsFix g) {
		
		Paragraph p = new Paragraph(text, this, multiline);
		p.render(g);
		
		p=null;
		
	}
	
	public final void setFont(Font font) {
		this.font = font;
	}
	
	public final Font getFont() {
		return font;
	}
	
	public final int getFontSize() {
		return font.getHeight();
	}
	
	public final int getSize() {
		return font.getSize();
	}
	
	public final void setSize(int size) {
		font = Font.getFont(font.getFace(), font.getStyle(), size);
	}
	
	public final void setStyle(int style) {
		this.font = Font.getFont(font.getFace(), style, font.getSize());
	}
	
	public final int getStyle() {
		return font.getStyle();
	}
	
	public final void setFace(int face) {
		this.font = Font.getFont(face, font.getStyle(), font.getSize());
	}
	
	public final int getFace() {
		return font.getFace();
	}
	
	/**@deprecated**/
	public final void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}
	/**@deprecated**/
	public final boolean getMultiline() {
		return multiline;
	}

}
