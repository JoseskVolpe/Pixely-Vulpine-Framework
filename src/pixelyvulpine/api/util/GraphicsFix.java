package pixelyvulpine.api.util;

import javax.microedition.lcdui.Graphics;

public final class GraphicsFix {
	
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

}
