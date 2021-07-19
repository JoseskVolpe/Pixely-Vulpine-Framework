package pixelyvulpine.api.util;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class GraphicsFix {
	
	/**
	 * @deprecated
	 * @param g
	 * @param data
	 * @param offset
	 * @param scanlength
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param alpha
	 */
	public static void drawRGB(Graphics g, int[] data, int offset, int scanlength, int x, int y, int width, int height, boolean alpha){
		int lx = g.getTranslateX();
		int ly = g.getTranslateY();
		int lw = g.getClipWidth();
		int lh = g.getClipHeight();
		int lcx = g.getClipX();
		int lcy = g.getClipY();
		
		g.translate(-lx, -ly);
		g.setClip(lcx+lx, lcy+ly, lw, lh);
		
		g.drawRGB(data, offset, scanlength, x+lx, y+ly, width, height, alpha);
		
		g.translate(lx, ly);
		g.setClip(lcx, lcy, lw, lh);
	}
	
	private Graphics g;
	private int clipX, clipY, clipH, clipW, dimW, dimH;
	
	public GraphicsFix(Graphics g) {
		this.g=g;
		clipX=g.getClipX();
		clipY=g.getClipY();
		clipW=g.getClipWidth();
		clipH=g.getClipHeight();
		dimW = clipW;
		dimH = clipH;
	}
	
	public void clipRect(int x, int y, int width, int height) {
		g.clipRect(x, y, width, height);
		clipX=g.getClipX();
		clipY=g.getClipY();
		clipW=g.getClipWidth();
		clipH=g.getClipHeight();
	}
	
	public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor) {
		g.copyArea(x_src, y_src, width, height, x_dest, y_dest, anchor);
	}
	
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		g.drawArc(x, y, width, height, startAngle, arcAngle);
	}
	
	public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
		g.drawChars(data, offset, length, x, y, anchor);
	}
	
	public void drawImage(Image img, int x, int y, int anchor) {
		g.drawImage(img, x, y, anchor);
	}
	
	public void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
	}

	public void drawRect(int x, int y, int width, int height) {
		g.drawRect(x, y, width, height);
	}
	
	public void drawRegion(Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor) {
		g.drawRegion(src, x_src, y_src, width, height, transform, x_dest, y_dest, anchor);
	}
	
	public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha) {
		int lx = g.getTranslateX();
		int ly = g.getTranslateY();
		int lw = g.getClipWidth();
		int lh = g.getClipHeight();
		int lcx = g.getClipX();
		int lcy = g.getClipY();
		
		g.translate(-lx, -ly);
		g.setClip(lcx+lx, lcy+ly, lw, lh);
		
		g.drawRGB(rgbData, offset, scanlength, x+lx, y+ly, width, height, processAlpha);
		
		g.translate(lx, ly);
		g.setClip(lcx, lcy, lw, lh);
	}
	
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}
	
	public void drawString(String str, int x, int y, int anchor) {
		g.drawString(str, x, y, anchor);
	}
	
	public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
		g.drawSubstring(str, offset, len, x, y, anchor);
	}
	
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		g.fillArc(x, y, width, height, startAngle, arcAngle);
	}
	
	public void fillRect(int x, int y, int width, int height) {
		g.fillRect(x, y, width, height);
	}
	
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}
	
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		g.fillTriangle(x1, y1, x2, y2, x3, y3);
	}
	
	public int getBlueComponent() {
		return g.getBlueComponent();
	}
	
	public int getClipHeight() {
		return clipH;
	}
	
	public int getDisplayClipHeight() {
		return g.getClipHeight();
	}
	
	public int getClipWidth() {
		return clipW;
	}
	
	public int getDisplayClipWidth() {
		return g.getClipWidth();
	}
	
	public int getClipX() {
		return clipX;
	}
	
	public int getDisplayClipX() {
		return g.getClipX();
	}
	
	public int getDimensionWidth() {
		return dimW;
	}
	
	public int getDimensionHeight() {
		return dimH;
	}
	
	public int getClipY() {
		return clipY;
	}
	
	public int getDisplayClipY() {
		return getClipY();
	}
	
	public int getColor() {
		return g.getColor();
	}
	
	public int getDisplayColor(int color) {
		return g.getDisplayColor(color);
	}
	
	public Font getFont() {
		return g.getFont();
	}
	
	public int getGrayScale() {
		return g.getGrayScale();
	}
	
	public int getGreenComponent() {
		return g.getGreenComponent();
	}
	
	public int getRedComponent() {
		return g.getRedComponent();
	}
	
	public int 	getStrokeStyle() {
		return g.getStrokeStyle();
	}
	
	public int getTranslateX() {
		return g.getTranslateX();
	}
	
	public int getTranslateY() {
		return g.getTranslateY();
	}
	
	public void setClip(int x, int y, int width, int height) {
		clipX=x;
		clipY=y;
		clipW=width;
		clipH=height;
		g.setClip(x, y, width, height);
	}
	
	public void setDimension(int width, int height) {
		dimW = width;
		dimH = height;
	}
	
	public void setColor(int RGB) {
		g.setColor(RGB);
	}
	
	public void setColor(int red, int green, int blue) {
		g.setColor(red, green, blue);
	}
	
	public void setFont(Font font) {
		g.setFont(font);
	}
	
	public void setGrayScale(int value) {
		g.setGrayScale(value);
	}
	
	public void setStrokeStyle(int style) {
		g.setStrokeStyle(style);
	}
	
	public void translate(int x, int y) {
		g.translate(x, y);
	}
}
