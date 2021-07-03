package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class TextFont {
	
	private Font font;
	private Color fontColor=new Color(0,0,0);
	private boolean multiline;
	
	public TextFont() {
		this.font = Font.getDefaultFont();
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

	public TextFont(int face, int style, int size, boolean multiline) {
		this.font = Font.getFont(face, style, size);
		this.multiline=multiline;
	}
	
	public final void render(String text, Graphics g) {
		fontColor.updateColor(g);
		g.setFont(font);
		
		if(!multiline) {
			g.drawString(text, 0, 0, 0);
			return ;
		}
		
		int h = getFontSize();
		int w;
		int x=0, y=0;
		StringBuffer temp = new StringBuffer();
		
		for(int i=0; i<text.length(); i++) {
			
			if(y>=g.getClipHeight()) break;
			
			if(i>=text.length()-1 || text.charAt(i)==' ' || text.charAt(i)=='\n') {
				
				if(!(text.charAt(i)==' ' && text.charAt(i)=='\n'))
					temp.append(text.charAt(i));
				
				w=font.stringWidth(temp.toString());
				
				if(w + x >= g.getClipWidth()) {
					x=0;
					y+=h;
					
				}
				
				g.drawString(temp.toString(), x, y, 0);
				x+=w;
				temp.delete(0, temp.length());
						
				if(i<text.length() && text.charAt(i)==' ')
					x+=font.charWidth(' ');
				
				if(text.charAt(i)=='\n') {
					x=0;
					y+=h;
				}
				
				continue;
			}
			
			temp.append(text.charAt(i));
			
		}
		
		temp=null;
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
	
	public final void setColor(Color fontColor){
		this.fontColor = fontColor;
	}
	
	public final Color getColor() {
		return fontColor;
	}
	
	public final void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}
	
	public final boolean getMultiline() {
		return multiline;
	}

}
