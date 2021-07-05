package pixelyvulpine.contents;

import java.util.Stack;
import java.util.Vector;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.api.util.Controls;
import pixelyvulpine.api.util.GraphicsFix;

public class Canvas extends Content{
	
	public static final byte ALIGNMENT_LEFT=-1;
	public static final byte ALIGNMENT_TOP=-1;
	public static final byte ALIGNMENT_CENTER=0;
	public static final byte ALIGNMENT_RIGHT=1;
	public static final byte ALIGNMENT_BOTTOM=1;
	
	public static final byte ARRANGEMENT_VERTICAL=0;
	public static final byte ARRANGEMENT_HORIZONTAL=1;
	
	protected int selectedC = -1;
	protected Vector contents = new Vector(0, 1);
	protected Stack[] renderData;
	
	private int horizontalOffset=5;
	private int verticalOffset=5;
	private byte alignment = ALIGNMENT_LEFT;
	private byte arrangement = ARRANGEMENT_VERTICAL;
	private boolean scroll = true;
	private int scrollX, scrollY;
	//TODO: Corrigir canvas secundários
	
	private Color backgroundColor, foregroundColor;
	
	public Canvas(Layout layout, int[] x, int[] y, int[] width, int[] height) {
		super(layout, x, y, width, height);
		// TODO Auto-generated constructor stub
		backgroundColor=new Color (150,30,30,30);
		foregroundColor=new Color(150,255,0,0);
	}
	
	public void Stopped() {
		for(short i=0; i<contents.capacity(); i++) {
			try {
				((Content)contents.elementAt(i)).Stopped();
			}catch(NullPointerException e) {
				Crash.showCrashMessage(getLayout().getMIDlet(), e, "There was an exception trying to stop activity "+getLayout().getTitle()+"\nElement is NULL", Crash.FRAMEWORK_CRASH);
			}catch(ClassCastException e) {
				Crash.showCrashMessage(getLayout().getMIDlet(), e, "There was an exception trying to stop activity "+getLayout().getTitle()+"\nElement is not a Contest", Crash.FRAMEWORK_CRASH);
			}
		}
	}
	
	public int[] prepaint(int lw, int lh) {
		sx=0;
		sy=0;
		if(scroll) {
			sx=scrollX;
			sy=scrollY;
		}
		
		int cLy=verticalOffset; //Line alignment
		
		renderData = new Stack[5];
		for(int i=0; i<renderData.length; i++) {
			renderData[i] = new Stack();
		}
		
		for(byte positioning=2; positioning>=0; positioning--) {
			for(short i=0; i<contents.capacity(); i++) {
				if(contents.elementAt(i)==null) continue;
				
				Content c = (Content)contents.elementAt(i);
				
				if(c.getPositioning()!=positioning || !c.isVisible()) continue;
				
				//Insert ID, size and pos data in renderData
				renderData[0].addElement(new Short(i)); //ID
				
				int rX=0;
				int rY=0;
				int rW=0;
				int rH=0;
				int newSize[];
				
				int cx[], cy[], cw[], ch[], tx, ty, clipW, clipH;
				
				cx = c.getX();
				cy = c.getY();
				cw = c.getWidth();
				ch = c.getHeight();
				 
				clipW = (int)(lw*(cw[0]/100.f))+cw[1];
				clipH = (int)(lh*(ch[0]/100.f))+ch[1];
				
				newSize = c.prepaint(clipW, clipH);
				
				clipW = newSize[0];
				clipH = newSize[1];
				
				switch(positioning) {
					case Content.POSITIONING_FIXED:
						
						tx=0;
						ty=0;
						int lineoffset=0;
						int increment=0;
						ARRANGE:
						switch(arrangement) {
						
							case ARRANGEMENT_VERTICAL:
								
								ty = cLy;
								
								ALIGN:
								switch (alignment) {
									case ALIGNMENT_LEFT:
										tx = horizontalOffset;
									break ALIGN;
									case ALIGNMENT_CENTER:
										tx = (lw/2)-(clipW/2);
									break ALIGN;
									case ALIGNMENT_RIGHT:
										tx = lw - (clipW) - horizontalOffset;
									break ALIGN;
								}
								
								lineoffset=verticalOffset;
								increment = clipH;
							
							break ARRANGE;
							
							case ARRANGEMENT_HORIZONTAL:
								tx = cLy;
								
								ALIGN:
								switch(alignment) {
									case ALIGNMENT_TOP:
										ty = verticalOffset;
									break ALIGN;
									case ALIGNMENT_CENTER:
										ty = (lh/2)-(clipH/2);
									break ALIGN;
									case ALIGNMENT_BOTTOM:
										ty = lh - clipH - verticalOffset;
									break ALIGN;
								}
								
								lineoffset = horizontalOffset;
								increment = clipW;
							break ARRANGE;
						}
						
						//Scroll
						//System.out.println(lh+" "+isSelected());
						if(isSelected() && scroll && selectedC==i) {
							//System.out.println((ty+sy+clipH)+">"+lh);
							if(ty+sy+clipH>lh) {
								//System.out.println((ty+sy)-(lh-clipH+sy));
								scrollY=-((ty+sy)-(lh-clipH+sy));
							}
							if(ty+sy<0) {
								//scrollY=(sy+ty); //Cool alternative
								scrollY=sy-(sy+ty);
							}
						}
						
						rX=tx+sx;
						rY=ty+sy;
						rW=clipW;
						rH=clipH;
						
						cLy+=increment+lineoffset;
						
						//g.translate(-(tx+sx), -(ty+sy));
						//g.setClip(0, 0, lw, lh);
						
					break;
					case Content.POSITIONING_ANCHORED:
						int Hanchor = c.getHorizontalAnchor();
						int Vanchor = c.getVerticalAnchor();
						
						int x=0, y=0;
						
						ANCHOR:
						switch(Hanchor) {
						
							case Content.HORIZONTAL_ANCHOR_LEFT:
								x = (int)(lw*(cx[0]/100.f))+cx[1];
							break ANCHOR;
							
							case Content.HORIZONTAL_ANCHOR_CENTER:
								x = (int)((lw/2)-(clipW/2));
							break ANCHOR;
							
							case Content.HORIZONTAL_ANCHOR_RIGHT:
								x = (int)(lw-(clipW) + (lw*(cx[0]/100.f)+cx[1]));
							break ANCHOR;
						
						}
					
						ANCHOR:
						switch(Vanchor) {
							case Content.VERTICAL_ANCHOR_TOP:
								y = (int)(lh*(cy[0]/100.f))+cy[1];
							break ANCHOR;
							
							case Content.VERTICAL_ANCHOR_CENTER:
								y = (int)((lh/2)-(clipH/2));
							break ANCHOR;
							
							case Content.VERTICAL_ANCHOR_BOTTOM:
								y = (int)(lh-(clipH) + (lh*(cy[0]/100.f)+cy[1]) );
							break ANCHOR;
							
						}
						
						
						rX=x+sx;
						rY=y+sy;
						rW=clipW;
						rH=clipH;
						
						//g.translate(-(x+sx), -(y+sy));
						//g.setClip(0, 0, lw, lh);
					break;
					case Content.POSITIONING_ABSOLUTE:
						cx = c.getX();
						cy = c.getY();
						cw = c.getWidth();
						ch = c.getHeight();
						
						tx = (int)(lw*(cx[0]/100.f))+cx[1];
						ty = (int)(lh*(cy[0]/100.f))+cy[1];
						
						rX = tx;
						rY = ty;
						rW=clipW;
						rH=clipH;
						
						//g.translate(-tx, -ty);
						//g.setClip(0, 0, lw, lh);
					break;
				}
				
				
				
				
				renderData[1].addElement(new Integer(rX)); //x
				renderData[2].addElement(new Integer(rY)); //y
				renderData[3].addElement(new Integer(clipW)); //width
				renderData[4].addElement(new Integer(clipH)); //height
				
			}
		}
		
		return new int[] {lw, lh};
		
	}
	
	private final void paintBackground(Graphics g, boolean saveRam) {
		if(backgroundColor!=null && backgroundColor.getAlpha()>0) {
			if(Display.getDisplay(getLayout().getMIDlet()).numAlphaLevels() <=2 || backgroundColor.getAlpha()>=255 || saveRam) {
				backgroundColor.updateColor(g);
				g.fillRect(0, 0, g.getClipWidth()-2, g.getClipHeight()-2);
			}else {
				int color[];
				/*
				color = new int[]{backgroundColor.getHex()};
				for(int y=0; y<g.getClipHeight()-2; y++) {
					for(int x=0; x<g.getClipWidth()-2; x++) {
						g.drawRGB(color, 0, 1, x, y, 1, 1, true);
					}
				}*/
				
				int w=g.getClipWidth()-2;
				int h=g.getClipHeight()-2;
				
				int hex = backgroundColor.getHex();
				color = new int[w*h];
				for(int i=0; i<color.length; i++)
					color[i]=hex;
				
				try {
					GraphicsFix.drawRGB(g, color, 0, w, 0, 0, w, h, true);
				}catch(Exception e) {}
				
				
				
				color=null;
			}
		}
	}
	
	public final void paint(Graphics g) {
		
		int lw=g.getClipWidth();
		int lh=g.getClipHeight();
		int lx=0;
		int ly=0;
		
		if(foregroundColor!=null && foregroundColor.getAlpha()>0) {
			if(Display.getDisplay(getLayout().getMIDlet()).numAlphaLevels() <=2 || foregroundColor.getAlpha()>=255) {
				foregroundColor.updateColor(g);
				g.drawRect(0, 0, lw, lh);
				g.translate(1, 1);
				lx=1;
				ly=1;
			}else {
				try {
					int color[];
					color = new int[]{foregroundColor.getHex()};
					for(int x=0; x<lw; x++) {
						GraphicsFix.drawRGB(g, color, 0, 1, x, 0, 1, 1, true);
						GraphicsFix.drawRGB(g, color, 0, 1, x, lh-1, 1, 1, true);
					}
					for(int y=1; y<lh-1; y++) {
						GraphicsFix.drawRGB(g, color, 0, 1, 0, y, 1, 1, true);
						GraphicsFix.drawRGB(g, color, 0, 1, lw-1, y, 1, 1, true);
					}
					g.translate(1, 1);
					lx=1;
					ly=1;
					color=null;
				}catch(Exception e) {
					foregroundColor.updateColor(g);
					g.drawRect(0, 0, lw, lh);
					g.translate(1, 1);
					lx=1;
					ly=1;
				}
			}
		}
		
		try {
			paintBackground(g, false);
		}catch(OutOfMemoryError e) {
			System.gc();
			try {
				paintBackground(g, false);
			}catch(OutOfMemoryError e2) {
				paintBackground(g, true);
			}
		}catch(Exception e) {
			paintBackground(g, true);
		}
		
		g.translate(-lx, -ly);
		g.setClip(0, 0, lw, lh);
		
		paintContent(g);
		
		renderData=null;
		
	}

	int sx, sy;
	protected final void paintContent(Graphics g) {
		if(contents==null) return;
		
		int lw=g.getClipWidth();
		int lh=g.getClipHeight();
		
		short i=0;
		int rx, ry, rw, rh;
		while(!renderData[0].empty()) {
			i=((Short)renderData[0].pop()).shortValue();
			rx=((Integer)renderData[1].pop()).intValue();
			ry=((Integer)renderData[2].pop()).intValue();
			rw=((Integer)renderData[3].pop()).intValue();
			rh=((Integer)renderData[4].pop()).intValue();
			
			if(ry>g.getClipHeight() || (ry+rh)<0 || rx>g.getClipWidth() || (rx+rw)<0)
				continue;
			
			Content c = (Content)contents.elementAt(i);
			
			g.translate(rx, ry);
			g.setClip(0, 0, rw, rh);
			
			c.paint(g);
			
			g.translate(-rx,  -ry);
			g.setClip(0, 0, lw, lh);
			
		}
		
	}
	
	protected void deselected() {
		
		if(selectedC>=0 && selectedC<contents.capacity() && contents.elementAt(selectedC) != null) {
			((Content)contents.elementAt(selectedC)).onDeselect();
		}
		
	}
	
	protected boolean selected() {
		
		if(contents==null || contents.capacity()==0) return false;
		
		if(selectedC>=0 && selectedC<contents.capacity() && contents.elementAt(selectedC) != null && ((Content)contents.elementAt(selectedC)).onSelect()) {
			return true;
		}
		
		int inc=0;
		
		if(selectedC<0) {
			selectedC=0;
			inc++;
		}else{
			inc--;
		}
		
		for(int i=selectedC; i<contents.capacity(); i+=inc) {
			
			if(contents.elementAt(i)!=null && ((Content)contents.elementAt(i)).onSelect()) {
				selectedC=i;
				return true;
			}
			
		}
		
		return false;
		
	}
	
	public final void addContent(Content content) {
		contents.addElement(content);
	}
	
	public final boolean removeContent(Content content) { //PS: i know these add() and remove() methods are slow and inneficient, optimization is a project, ¡you can contribute! ;3
		
		boolean s = contents.removeElement(content);
		if(!s) return false;
		contents.trimToSize();
		return true;
		
	}
	
	public final void setContentOffset(int horizontal, int vertical) {
		this.horizontalOffset = horizontal;
		this.verticalOffset = vertical;
	}
	
	public final void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public final Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public final void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	
	public final Color getForegroundColor() {
		return foregroundColor;
	}
	
	public final int getHorizontalContentOffset() {
		return horizontalOffset;
	}
	
	public final int getVerticalContentOffset() {
		return verticalOffset;
	}
	
	/**
	 * Sets content alignment position
	 * @param For Vertical: ALIGNMENT_LEFT, ALIGNMENT_CENTER, ALIGNMENT_RIGHT. For horizontal: ALIGNMENT_TOP, ALIGNMENT_CENTER, ALIGNMENT_BOTTOM
	 */
	public final void setContentAlignment(int alignment) {
		this.alignment = (byte) alignment;
	}
	
	/**
	 * Sets content arrangement
	 * @param ARRANGEMENT_VERTICAL, ARRANGEMENT_HORIZONTAL
	 */
	public final void setArrangement(int arrangement) {
		this.arrangement = (byte) arrangement;
	}
	
	public final int getArrangement() {
		return arrangement;
	}
	
	public final void setScroll(boolean scroll) {
		this.scroll = scroll;
	}
	
	public final boolean getScroll() {
		return scroll;
	}
	
	public Content getSelected() {
		
		if(contents!=null && selectedC >= 0 && selectedC < contents.capacity() && contents.elementAt(selectedC) != null) {
			
			try {
				return ((Canvas) contents.elementAt(selectedC)).getSelected();
			}catch(ClassCastException e) {
				return (Content)contents.elementAt(selectedC);
			}
		}
		
		return null;
		
	}
	
	public boolean keyDown(int keyCode, int key) {
		
		if(contents!=null && selectedC >= 0 && selectedC < contents.capacity() && contents.elementAt(selectedC) != null && !((Content)contents.elementAt(selectedC)).keyDown(keyCode, key)) return false;
		
		int lc;
		
		int nextKey=0, backKey=0;
		switch(arrangement) {
			case ARRANGEMENT_VERTICAL:
				nextKey = Controls.DPAD_DOWN;
				backKey = Controls.DPAD_UP;
			break;
			case ARRANGEMENT_HORIZONTAL:
				nextKey = Controls.DPAD_RIGHT;
				backKey = Controls.DPAD_LEFT;
			break;
		}
		
		if(key == nextKey) {
				lc = selectedC;
				if(selectedC<0) selectedC=0;
				do {
					selectedC++;
				}while(selectedC<contents.capacity()-1 && !((Content)contents.elementAt(selectedC)).onSelect());
				if(selectedC>=contents.capacity() || contents.elementAt(selectedC)==null || !((Content)contents.elementAt(selectedC)).onSelect()) {
					selectedC = lc;
					return true;
				}else if(lc>=0 && lc<contents.capacity() && contents.elementAt(lc)!=null){
					((Content)contents.elementAt(lc)).onDeselect();
				}
				
				return false;
				
		}
				
		if(key == backKey) {
				lc = selectedC;
				do {
					selectedC--;
				}while(selectedC>0 && !((Content)contents.elementAt(selectedC)).onSelect());
				if(selectedC < 0 || contents.elementAt(selectedC)==null || !((Content)contents.elementAt(selectedC)).onSelect()) {
					selectedC = lc;
					return true;
				}else if(lc>=0 && lc<contents.capacity() && contents.elementAt(lc)!=null){
					((Content)contents.elementAt(lc)).onDeselect();
				}
				
				return false;
				
		}
		
				
		if(key == Controls.SOFTKEY_CENTER) {
				if(contents!=null && selectedC >= 0 && selectedC < contents.capacity() && contents.elementAt(selectedC) != null && !((Content)contents.elementAt(selectedC)).pressed()) return false;
				return true;
		}
		
		return true;
		
		
		
	}

}
