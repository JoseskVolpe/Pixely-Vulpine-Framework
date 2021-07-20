package pixelyvulpine.contents;

import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.GraphicsFix;

public class CircularProgressBar extends Content {

	private Color color = new Color(255,255,255);
	private static ball[] balls = new ball[18];
	private long min, max, progress;
	
	static {
		for(int i=0; i<balls.length; i++) {
			balls[i]=new ball(i);
		}
	}
	
	/**
	 * @param Context
	 * @param CircularProgressBarDimensionAttributes
	 */
	public CircularProgressBar(Layout layout, CircularProgressBarDimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
	}
	
	/**
	 * @param Context
	 * @param CircularProgressBarDimensionAttributes
	 * @param Color
	 */
	public CircularProgressBar(Layout layout, CircularProgressBarDimensionAttributes dimensionAttributes, Color color) {
		super(layout, dimensionAttributes);
		
		this.color = color;
	}
	
	public int[] prepaint(int width, int height) {
		return new int[] {Math.max(width, height), Math.max(width, height)};
	}
	
	protected void paint(GraphicsFix g) {
		
		int clipW = g.getDimensionWidth();
		int clipH = g.getDimensionHeight();
		int mySize = Math.min(clipW, clipH);
		int nCW = clipW-(clipW-mySize);
		int nCH = clipH-(clipH-mySize);
		int tx = (clipW-mySize)/2;
		int ty = (clipH-mySize)/2;
		
		g.translate(tx,  ty);
		g.setDimension(nCW, nCH);
		
		int ballSize = (int)(Math.ceil(g.getDimensionHeight()/5));
		
		int b=0;
		int f=balls.length;
		if(min==max || max<min) {
			b=(int) ((balls.length/2)+(balls.length/2)*Math.sin(System.currentTimeMillis() * ((2*Math.PI)/1400) ));
			f=(int) ((balls.length/2)+(balls.length/2)*Math.sin(3+System.currentTimeMillis() * ((2*Math.PI)/1400) ));
			if(f==0) f=1;
			if(b==f || b>f) b=f-1;
		}else {
			f=(int)(balls.length*((progress-min)/(double)(max-min)));
			if(f==0) f=1;
		}
		
		color.updateColor(g);
		for(int i=b; i<f; i++) {
			balls[i].paint(g, ballSize);
		}
		
		g.translate(-tx,  -ty);
		g.setDimension(clipW, clipH);
		
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void setProgress(int progress) {
		this.progress=progress;
	}
	
	public void setMin(long min) {
		this.min = min;
	}
	
	public void setMax(long max) {
		this.max = max;
	}
	
	public void setProgress(long progress) {
		this.progress=progress;
	}
	
	public long getMin() {
		return min;
	}
	
	public long getMax() {
		return max;
	}
	
	public long getProgress() {
		return progress;
	}
	
	private static class ball{
		byte index;
		static byte onZero;
		public ball(int index) {
			this.index=(byte)index;
		}
		
		public void paint(GraphicsFix g, int ballSize) {
			
			int sInc=0;
			double index = this.index*0.35;
			
			int w = g.getDimensionWidth()-(ballSize);
			int h = g.getDimensionHeight()-(ballSize);
			
			int dw=w/2;
			int dh=h/2;
			int ds = ballSize/2;
			
			int cX = (g.getDimensionWidth()/2)-(ballSize/2);
			int cY = (g.getDimensionHeight()/2)-(ballSize/2);
			
			//int spin1X
			double spin3X=1;
			
			double spin1Y = ( dh * Math.sin(index - System.currentTimeMillis() * ((2*Math.PI)/1200) ) + dh );
			double spin1X = ( dw * Math.cos(index - System.currentTimeMillis() * ((2*Math.PI)/1200) ) + dw );
			//double spin1X = ( dw * Math.cos(index - System.currentTimeMillis() * ((Math.PI)/500) )  + dw); //Cool effect, but nah uwu
			
			g.fillArc((int)spin1X, (int)spin1Y, ballSize+sInc, ballSize+sInc, 0, 360);
		}
	}

	public static class CircularProgressBarDimensionAttributes extends DimensionAttributes{
		
		public CircularProgressBarDimensionAttributes(Scaled scaled, Offset offset) {
			super(scaled, offset);
		}
		
		public static class Scaled extends DimensionAttributes.Scaled{
			
			public Scaled(int x, int y, int size) {
				super(x, y, size, size);
			}
		}
		
		public static class Offset extends DimensionAttributes.Offset{
			
			public Offset(int x, int y, int size) {
				super(x,y,size,size);
			}
		}
		
	}

}