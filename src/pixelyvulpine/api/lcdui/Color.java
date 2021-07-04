package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.Graphics;

public final class Color {
	
	private byte a,r,g,b;
	
	public Color (int hex, boolean alpha) {
		
		a=255+Byte.MIN_VALUE;
		if(alpha)
			a = (byte) (((hex & 0xFF000000) >> 24)+Byte.MIN_VALUE);
		
		r = (byte) (((hex & 0xFF0000) >> 16)+Byte.MIN_VALUE);
	    g = (byte) (((hex & 0xFF00) >> 8)+Byte.MIN_VALUE);
	    b = (byte) ((hex & 0xFF)+Byte.MIN_VALUE);
		
	}
	
	public Color(int a, int r, int g, int b) {
		
		this.a=(byte) (a+Byte.MIN_VALUE);
		this.r=(byte) (r+Byte.MIN_VALUE);
		this.g = (byte) (g+Byte.MIN_VALUE);
		this.b = (byte) (b+Byte.MIN_VALUE);
		
	}
	
	public Color(int r, int g, int b) {
		this.a=(byte) (255+Byte.MIN_VALUE);
		this.r=(byte) (r+Byte.MIN_VALUE);
		this.g = (byte) (g+Byte.MIN_VALUE);
		this.b = (byte) (b+Byte.MIN_VALUE);
	}
	
	public void setAlpha(int a) {
		this.a=(byte) (a+Byte.MIN_VALUE);
	}
	
	public void setRed(int r) {
		this.r=(byte) (r+Byte.MIN_VALUE);
	}
	
	public void setGreen(int g) {
		this.g = (byte) (g+Byte.MIN_VALUE);
	}
	
	public void setBlue(int b) {
		this.b = (byte) (b+Byte.MIN_VALUE);
	}
	
	public int getAlpha() {
		return a-Byte.MIN_VALUE;
	}
	
	public int getRed() {
		return r-Byte.MIN_VALUE;
	}
	
	public int getGreen() {
		return g-Byte.MIN_VALUE;
	}
	
	public int getBlue() {
		return b-Byte.MIN_VALUE;
	}
	
	public void updateColor(Graphics g) {
		g.setColor(getRed(), getGreen(), getBlue());
	}
	
	public int getHex() {
		
		return getAlpha()*0x1000000 + getRed()*0x10000 + getGreen()*0x100 + getBlue();
	}

}
