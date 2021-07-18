package pixelyvulpine.api.lcdui;

import java.util.Vector;

import pixelyvulpine.api.util.GraphicsFix;

public class Paragraph {
	
	private boolean multiline;
	private TextFont font;
	private String text;
	private int width, height, startX, lastFace, lastStyle, lastSize;
	private Vector paragraphs;
	
	public Paragraph(String text, TextFont font) {
		this.font=font;
		this.text=text;
	}
	
	public Paragraph(String text, TextFont font, boolean multiline) {
		this(text, font);
		this.multiline=multiline;
	}
	
	public void render(GraphicsFix g) {
		g.setFont(font);
		prepareDimension(g.getDimensionWidth(), g.getDimensionHeight());
		
		int x = startX;
		int y = 0;
		for(int i=0; i<paragraphs.size(); i++) {
			
			g.drawString((String)paragraphs.elementAt(i), x, y, 0);
			
			x=0;
			y+=font.getFontSize();
		}
	}
	
	public void prepareDimension(int width, int height) {
		
		if(this.width==width && this.height==height && this.lastFace==font.getFace() && this.lastStyle==font.getStyle() && this.lastSize == font.getSize()) return;
		
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
		
		int h = font.getFontSize();
		int w;
		int x=startX, y=0;
		StringBuffer temp = new StringBuffer();
		StringBuffer tmp2 = new StringBuffer();
		
		for(int i=0; i<text.length(); i++) {
			
			if(y>=height) break;
			
			if(i>=text.length()-1 || text.charAt(i)==' ' || text.charAt(i)=='\n') {
				
				if(!(text.charAt(i)==' ' && text.charAt(i)=='\n'))
					temp.append(text.charAt(i));
				
				w=font.getFont().stringWidth(temp.toString());
				
				if(w + x >= width) {
					x=0;
					y+=h;
					paragraphs.addElement("");
				}
				
				tmp2.delete(0, tmp2.length());
				tmp2.append(paragraphs.lastElement());
				tmp2.append(temp.toString());
				paragraphs.setElementAt(tmp2.toString(), paragraphs.size()-1);
				x+=w;
				temp.delete(0, temp.length());
						
				if(i<text.length() && text.charAt(i)==' ')
					x+=font.getFont().charWidth(' ');
				
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
		
	}
	
	public void setMultiline(boolean multiline) {
		this.multiline=multiline;
		prepareDimension(width, height);
	}
	
	public boolean getMultiline(){
		return multiline;
	}
	
	public void setFont(TextFont font) {
		this.font=font;
		prepareDimension(width, height);
	}
	
	public TextFont getFont() {
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
		this.text=text;
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
	
	public int getLineIndex(int index) {
		int s=0;
		for(int i=0; i<paragraphs.size(); i++) {
			s+=((String)paragraphs.elementAt(i)).length();
			if(s>=index) return s-index;
		}
		return -1;
	}
	
	public int getLineWidth(int line) {
		return font.getFont().stringWidth(getLine(line));
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