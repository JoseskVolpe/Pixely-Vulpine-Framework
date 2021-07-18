package pixelyvulpine.api.lcdui;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import pixelyvulpine.api.util.GraphicsFix;

public class Paragraph {
	
	private boolean multiline;
	private Font font;
	private String text;
	private int width, height, startX, lastFace, lastStyle, lastSize;
	private boolean forcePrepare=true;
	private Vector paragraphs;
	
	public Paragraph(Font font) {
		this.font = font;
	}
	
	public Paragraph(Font font, boolean multiline) {
		this(font);
		this.multiline=multiline;
	}
	
	public Paragraph(String text, Font font) {
		this(font);
		this.text=text;
	}
	
	public Paragraph(String text, Font font, boolean multiline) {
		this(text, font);
		this.multiline=multiline;
	}
	
	public void render(GraphicsFix g) {
		g.setFont(font);
		
		if(text==null) return;
		
		prepareDimension(g.getDimensionWidth(), g.getDimensionHeight());
		
		int x = startX;
		int y = 0;
		Object el;
		for(int i=0; i<paragraphs.size(); i++) {
			
			if(y>=g.getClipHeight() || y>=g.getDimensionHeight()) break;
			
			if(y+font.getHeight()>0) {
				el = paragraphs.elementAt(i);
				if(el==null) continue;
				g.drawString((String)el, x, y, 0);
			}
			
			x=0;
			y+=font.getHeight();
		}
	}
	
	public void prepareDimension(int width, int height) {
		
		if(this.width==width && this.height==height && this.lastFace==font.getFace() && this.lastStyle==font.getStyle() && this.lastSize == font.getSize() && !forcePrepare) return;
		
		this.width=width;
		this.height=height;
		this.lastFace=font.getFace();
		this.lastStyle=font.getStyle();
		this.lastSize=font.getSize();
		
		paragraphs = new Vector(0,1);
		
		if(text==null) return;
		
		if(!multiline) {
			paragraphs.addElement(text);
			return ;
		}
		
		paragraphs.addElement("");
		
		int h = font.getHeight();
		int w;
		int x=startX, y=0;
		StringBuffer temp = new StringBuffer();
		StringBuffer tmp2 = new StringBuffer();
		
		for(int i=0; i<text.length(); i++) {
			
			paragraphs.setElementAt((String)paragraphs.lastElement()+text.charAt(i), paragraphs.size()-1);
			
			if(y>=height) break;
			
			if(i>=text.length()-1 || text.charAt(i)==' ' || text.charAt(i)=='\n') {
				
				if(!(text.charAt(i)==' ' && text.charAt(i)=='\n'))
					temp.append(text.charAt(i));
				
				w=font.stringWidth(temp.toString());
				
				if(w + x >= width) {
					x=0;
					y+=h;
					paragraphs.addElement("");
				}
				
				x+=w;
				temp.delete(0, temp.length());
						
				if(i<text.length() && text.charAt(i)==' ')
					x+=font.charWidth(' ');
				
				if(text.charAt(i)=='\n') {
					x=0;
					y+=h;
					paragraphs.addElement("");
				}
				
				continue;
			}
			
			temp.append(text.charAt(i));
			
		}
		
		if(temp!=null && temp.length()>0)
			temp.delete(0, temp.length()-1);
		
		temp=null;
		forcePrepare=false;
		
	}
	
	public void setMultiline(boolean multiline) {
		this.multiline=multiline;
		prepareDimension(width, height);
	}
	
	public boolean getMultiline(){
		return multiline;
	}
	
	public void setFont(Font font) {
		this.font=font;
		prepareDimension(width, height);
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setStartX(int startX) {
		this.startX=startX;
		prepareDimension(width, height);
	}
	
	public int getStartX() {
		return startX;
	}
	
	public void setText(String text) {
		
		if(this.text!=null && this.text.equals(text)) return;
		
		
		this.text=text;
		forcePrepare=true;
		prepareDimension(width, height);
	}
	
	public String getText() {
		return text;
	}
	
	public int getLineFromCharIndex(int index) {
		int s=0;
		for(int i=0; i<paragraphs.size(); i++) {
			s+=((String)paragraphs.elementAt(i)).length();
			if(s>=index) return i;
		}
		return -1;
	}
	
	public int getLineWidth(int line) {
		return font.stringWidth(getLine(line));
	}
	
	public int getCharXFromIndex(int index) {
		
		int s=0;
		int line=0;
		for(int i=0; i<paragraphs.size(); i++) {
			s+=((String)paragraphs.elementAt(i)).length();
			
			if(s>=index) {
				line=i;
				break;
			}
			
		}
		
		int tx=0;
		
		if(line==0) 
			tx=startX;
		
		s-=((String)paragraphs.elementAt(line)).length();
		index-=s;
		
		return tx+font.stringWidth(((String)paragraphs.elementAt(line)).substring(0, index));
	}
	
	public int getCharYFromIndex(int index) {
		return font.getHeight()*getLineFromCharIndex(index);
	}
	
	public int getLinesNumber() {
		return paragraphs.size();
	}
	
	public String getLine(int line) {
		return (String)paragraphs.elementAt(line);
	}
	
	public String getLineStringFromCharIndex(int index) {
		return getLine(getLineFromCharIndex(index));
	}

}
