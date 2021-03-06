package pixelyvulpine.contents;

import java.util.Stack;
import java.util.Vector;

import javax.microedition.lcdui.Display;

import pixelyvulpine.api.events.*;
import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.api.util.GraphicsFix;

public class Canvas extends Content{
	
	public static final byte ALIGNMENT_LEFT=-1;
	public static final byte ALIGNMENT_TOP=-1;
	public static final byte ALIGNMENT_CENTER=0;
	public static final byte ALIGNMENT_RIGHT=1;
	public static final byte ALIGNMENT_BOTTOM=1;
	
	public static final byte ARRANGEMENT_VERTICAL=0;
	public static final byte ARRANGEMENT_HORIZONTAL=1;
	
	protected Vector contents = new Vector(0);
	protected Stack contentsToRemove = new Stack();
	protected Stack[] renderData;
	protected Content selected;
	
	private byte alignment = ALIGNMENT_LEFT;
	private byte arrangement = ARRANGEMENT_VERTICAL;
	private boolean scroll = false;
	private int minX, minY, maxX, maxY, canvasDisplayW, canvasDisplayH, canvasWidth, canvasHeight, canvasX, canvasY;
	private double scrollX, scrollY, velocityX, velocityY;
	//TODO: Corrigir canvas secundários
	
	private Color backgroundColor, foregroundColor;
	
	public Canvas(Layout layout, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
		forcePaint=true;
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
		
		if(foregroundColor!=null && foregroundColor.getAlpha()<=0) {
			lw-=2;
			lh-=2;
			
			if(lw<0) lw=0;
			if(lh<0) lh=0;
		}
		
		sx=0;
		sy=0;
		if(scroll) {
			sx=(int)scrollX;
			sy=(int)scrollY;
			
			//TODO: Scroll limit events
			if(maxX-minX<=lw)
				sx=-minX;
			
			if(maxY-minY<=lh)
				sy=-minY;
			
		}
		
		minX=0;
		minY=0;
		maxX=0;
		maxY=0;
		
		int cLy=0; //Line alignment
		
		int selectedX=0, selectedY=0, selectedW=0, selectedH=0;
		
		Stack[] renderData = new Stack[5];
		for(int i=0; i<renderData.length; i++) {
			renderData[i] = new Stack();
		}
		
		//for(byte positioning=2; positioning>=0; positioning--) {
			for(short i=0; i<contents.size(); i++) {
				if(contents.elementAt(i)==null) continue;
				
				Content c = (Content)contents.elementAt(i);
				byte positioning=c.getPositioning();
				
				if(c.getPositioning()!=positioning || !c.isVisible()) continue;
				
				int rX=0;
				int rY=0;
				int rW=0;
				int rH=0;
				int newSize[];
				
				int cx[], cy[], cw[], ch[], tx, ty, clipW, clipH;
				
				cx = new int[] {c.getDimension().getScaledDimension().x, c.getDimension().getOffsetDimension().x};
				cy = new int[] {c.getDimension().getScaledDimension().y, c.getDimension().getOffsetDimension().y};
				cw = new int[] {c.getDimension().getScaledDimension().width, c.getDimension().getOffsetDimension().width};
				ch = new int[] {c.getDimension().getScaledDimension().height, c.getDimension().getOffsetDimension().height};
				
				clipW = (int)(lw*(cw[0]/100.f))+cw[1];
				clipH = (int)(lh*(ch[0]/100.f))+ch[1];
				
				newSize = c.dispatchPrepaint(clipW, clipH);
				
				clipW = newSize[0];
				clipH = newSize[1];
				
				switch(positioning) {
					case Content.POSITIONING_FIXED:
						
						tx=(int)(lw*(cx[0]/100.f))+cx[1];;
						ty=(int)(lh*(cy[0]/100.f))+cy[1];;
						int increment=0;
						ARRANGE:
						switch(arrangement) {
						
							case ARRANGEMENT_VERTICAL:
								
								increment = clipH+ty;
								ty += cLy;
								
								ALIGN:
								switch (alignment) {
									case ALIGNMENT_LEFT:
										tx += 0;
									break ALIGN;
									case ALIGNMENT_CENTER:
										tx += (lw/2)-(clipW/2);
									break ALIGN;
									case ALIGNMENT_RIGHT:
										tx += lw - (clipW);
									break ALIGN;
								}
								
							
							break ARRANGE;
							
							case ARRANGEMENT_HORIZONTAL:
								
								increment = clipW+tx;
								tx += cLy;
								
								ALIGN:
								switch(alignment) {
									case ALIGNMENT_TOP:
										ty += 0;
									break ALIGN;
									case ALIGNMENT_CENTER:
										ty += (lh/2)-(clipH/2);
									break ALIGN;
									case ALIGNMENT_BOTTOM:
										ty += lh - clipH;
									break ALIGN;
								}
								
								
							break ARRANGE;
						}
						
						//Scroll
						
						
						if(tx<minX)
							minX=tx;
						if(ty<minY)
							minY=ty;
						if(tx+clipW>maxX)
							maxX=tx+clipW;
						if(ty+clipH>maxY)
							maxY=ty+clipH;
						
						rX=tx+sx;
						rY=ty+sy;
						rW=clipW;
						rH=clipH;
						
						cLy+=increment;
						
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
						
						
						rX=x;
						rY=y;
						rW=clipW;
						rH=clipH;
						
						//g.translate(-(x+sx), -(y+sy));
						//g.setClip(0, 0, lw, lh);
					break;
					case Content.POSITIONING_ABSOLUTE:
						cx = new int[] {c.getDimension().getScaledDimension().x, c.getDimension().getOffsetDimension().x};
						cy = new int[] {c.getDimension().getScaledDimension().y, c.getDimension().getOffsetDimension().y};
						cw = new int[] {c.getDimension().getScaledDimension().width, c.getDimension().getOffsetDimension().width};
						ch = new int[] {c.getDimension().getScaledDimension().height, c.getDimension().getOffsetDimension().height};
						
						tx = (int)(lw*(cx[0]/100.f))+cx[1];
						ty = (int)(lh*(cy[0]/100.f))+cy[1];
						
						rX = tx;
						rY = ty;
						rW=clipW;
						rH=clipH;
						
						//g.translate(-tx, -ty);
						//g.setClip(0, 0, lw, lh);
						
						if(tx<minX)
							minX=tx;
						if(ty<minY)
							minY=ty;
						if(tx+clipW>maxX)
							maxX=tx+clipW;
						if(ty+clipH>maxY)
							maxY=ty+clipH;
					break;
				}
				
				
				if(rX+clipW>=0 && rX<=lw && rY+clipH>=0 && rY<=lh) {
					
					addToRender(renderData, i, rX, rY, clipW, clipH);
				}else {
					c.dispatchNoPaint();
				}
				
				if(c==selected) {
					selectedX=rX;
					selectedY=rY;
					selectedW=clipW;
					selectedH=clipH;
				}
				
			}
		//}
			
			canvasWidth=lw;
			canvasHeight=lh;
			
			if(lh-scrollY>maxY) {
				scrollY=lh-maxY;
				velocityY=0;
			}
			if(-scrollY<minY) {
				scrollY=-minY;
				velocityY=0;
			}
			
			if(lw-scrollX>maxX) {
				scrollX=lw-maxX;
				velocityX=0;
			}
			
			if(-scrollX<minX) {
				scrollX=-minX;
				velocityX=0;	
			}
			
		this.renderData = renderData;
		
		return new int[] {lw, lh};
		
	}
	
	protected final void addToRender(Stack renderData[], int id, int rX, int rY, int clipW, int clipH) {
		
		if((Content)contents.elementAt(id)==null)
			return;
		
		if(renderData[0].size()<=0) {
			renderData[0].addElement(new Short((short)id));
			renderData[1].addElement(new Integer(rX));
			renderData[2].addElement(new Integer(rY));
			renderData[3].addElement(new Integer(clipW));
			renderData[4].addElement(new Integer(clipH));
			return;
		}
		
		int i=(renderData[0].size()-1)/2;
		byte zIndex = ( (Content)contents.elementAt(id) ).getZIndex();
		
		int cZIndex = ( (Content)contents.elementAt( ( (Short)(renderData[0].elementAt(i)) ).shortValue() ) ).getZIndex();
		
		if( cZIndex <  zIndex) { // Middle's ZIndex is lower than adding content
			
			while(i>0) {
				
				i--;
				cZIndex = ( (Content)contents.elementAt( ( (Short)(renderData[0].elementAt(i)) ).shortValue() ) ).getZIndex();
				
				if(cZIndex>=zIndex)
					break;
				
			}
			
		}else { //Middle's ZIndex is higher or equal adding content
			
			while(i<renderData[0].size()) {
				
				i++;
				
				if(i>=renderData[0].size()) break;
				
				cZIndex = ( (Content)contents.elementAt( ( (Short)(renderData[0].elementAt(i)) ).shortValue() ) ).getZIndex();
				
				if(cZIndex<zIndex) {
					break;
				}
				
			}
			
		}
		
		renderData[0].insertElementAt(new Short((short)id), i);
		renderData[1].insertElementAt(new Integer(rX), i);
		renderData[2].insertElementAt(new Integer(rY), i);
		renderData[3].insertElementAt(new Integer(clipW), i);
		renderData[4].insertElementAt(new Integer(clipH), i);
		
	}
	
	public void noPaint() {
		if(renderData==null) return;
		
		while(!renderData[0].empty()){
			((Content)contents.elementAt(((Short)renderData[0].pop()).shortValue())).dispatchNoPaint();
			renderData[1].pop();
			renderData[2].pop();
			renderData[3].pop();
			renderData[4].pop();
		}
		
		renderData=null;
		
	}
	
	private short velLoss=40;
	protected final void paint(GraphicsFix g) {
		
		canvasX = g.getTranslateX();
		canvasY = g.getTranslateY();
		canvasDisplayW=g.getDisplayClipWidth();
		canvasDisplayH=g.getDisplayClipHeight();
		
		scrollX+=velocityX*getLayout().getDeltaSec();
		scrollY+=velocityY*getLayout().getDeltaSec();
		if(velocityX>0) {
			velocityX-=velLoss*getLayout().getDeltaSec();
			if(velocityX<0) velocityX=0;
		}else if(velocityX<0){
			velocityX+=velLoss*getLayout().getDeltaSec();
			if(velocityX>0) velocityX=0;
		}
		
		if(velocityY>0) {
			velocityY-=velLoss*getLayout().getDeltaSec();
			if(velocityY<0) velocityY=0;
		}else if(velocityY<0){
			velocityY+=velLoss*getLayout().getDeltaSec();
			if(velocityY>0) velocityY=0;
		}
		
		int lcw=g.getClipWidth();
		int lch=g.getClipHeight();
		int lw=g.getDimensionWidth();
		int lh=g.getDimensionHeight();
		int lx=0;
		int ly=0;
		
		int inc=0;
		
		if (paintFrame(foregroundColor, g, 0, 1, 1, lh-1)) {
			inc=1;
			paintFrame(foregroundColor, g, lw, 1, 1, lh-1);
			paintFrame(foregroundColor, g, 0, 0, lw, 1);
			paintFrame(foregroundColor, g, 0, lh, lw, 1);
		}
		paintFrame(backgroundColor, g, 0+inc, 0+inc, lw-inc, lh-inc);
		
		g.clipRect(0, 0, lw-lx*2, lh-ly*2);
		
		paintContent(g);
	}
	
	private boolean paintFrame(Color color, GraphicsFix g, int x, int y, int w, int h) {
		if(color==null || color.getAlpha()<=0) return false;
		if(color.getAlpha()>=255 || Display.getDisplay(getLayout().getMIDlet()).numAlphaLevels()<=2) {
			color.updateColor(g);
			g.fillRect(x, y, w, h);
			return true;
		}
		
		int hex = color.getHex();
		int temp[] = new int[w];
		for(int i=0; i<w; i++)
			temp[i]=hex;
		
		g.drawRGB(temp, 0, 0, x, y, w, h, true);
		
		temp=null;
		
		return true;
		
	}

	int sx, sy;
	protected final void paintContent(GraphicsFix g) {
		if(contents==null) return;
		
		int dw=g.getDimensionWidth();
		int dh=g.getDimensionHeight();
		int lcw=g.getClipWidth();
		int lch=g.getClipHeight();
		int lcx=g.getClipX();
		int lcy=g.getClipY();
		
		short i=0;
		int rx, ry, rw, rh;
		Content c;
		for(int index=renderData[0].size()-1; index>=0; index--) {
			//Although it's a Stack, no more pop(), it'll corrupt the data when we're making a InputEvent
			i=((Short)renderData[0].elementAt(index)).shortValue();
			rx=((Integer)renderData[1].elementAt(index)).intValue();
			ry=((Integer)renderData[2].elementAt(index)).intValue();
			rw=((Integer)renderData[3].elementAt(index)).intValue();
			rh=((Integer)renderData[4].elementAt(index)).intValue();
			
			c = (Content)contents.elementAt(i);
			if(rx>=dw || ry>=dh || rx+rw<0 || ry+rh<0 || c==null) continue;
			
			g.translate(rx, ry);
			g.setDimension(rw, rh);
			
			int tx=g.getTranslateX();
			int ty=g.getTranslateY();
			
			c.dispatchPaint(g);
			
			g.translate(tx-g.getTranslateX()-rx,  ty-g.getTranslateY()-ry);
			g.setClip(lcx, lcy ,lcw, lch);
			
		}
		
		g.setDimension(dw, dh);
		
		sweepContents();
	}
	
	//TODO: Better, automatic memory management alghoritm
	private void sweepContents() {
		int index;
		while(!contentsToRemove.isEmpty()) {
			index = ((Integer)contentsToRemove.pop()).intValue();
			if(contents.elementAt(index)!=null)
				Crash.showCrashMessage(getLayout().getMIDlet(), new IllegalAccessException("Canvas: Content to remove is not null"), null, Crash.FRAMEWORK_CRASH);
			
			contents.removeElementAt(index);
		}
	}
	
	private Object downData[];
	protected boolean onTouch(MotionEvent e) {
		Stack renderData[] = this.renderData;
		if(e.getAction()==MotionEvent.ACTION_DOWN) {
			
			if(selected!=null) selected.dispatchSelected(false);
			selected=null;
			
			
			if(contents == null || contents.size()<0 || renderData==null) return false;
			
			int px = e.getPointerCoords().x;
			int py = e.getPointerCoords().y;
			
			for(int i=0; i<renderData[0].size(); i++) {
				int cx = ((Integer)renderData[1].elementAt(i)).intValue();
				int cy = ((Integer)renderData[2].elementAt(i)).intValue();
				int cw = ((Integer)renderData[3].elementAt(i)).intValue();
				int ch = ((Integer)renderData[4].elementAt(i)).intValue();
				short index = ((Short)renderData[0].elementAt(i)).shortValue();
				
				if(px>=cx && py>=cy && px<=cx+cw && py<=cy+ch) {
					
					Content c = ((Content)contents.elementAt(index));
					if(c==null) continue;
					
					MotionEvent checkEvent = new MotionEvent(c.getHistoricalCoords(), e.getPointerCoords().x-cx, e.getPointerCoords().y-cy, e.getAction());
					downData=new Object[] {
							renderData[0].elementAt(i),
							renderData[1].elementAt(i),
							renderData[2].elementAt(i),
					};
					
					if(c.dispatchTouchEvent(checkEvent)) return true;
					
					break;
				}
			}
		
		}else if(downData!=null) {
			int cx = ((Integer)downData[1]).intValue();
			int cy = ((Integer)downData[2]).intValue();
			short index = ((Short)downData[0]).shortValue();
			Content c = ((Content)contents.elementAt(index));
			MotionEvent checkEvent = new MotionEvent(c.getHistoricalCoords(), e.getPointerCoords().x-cx, e.getPointerCoords().y-cy, e.getAction());
			
			if(e.getAction()==MotionEvent.ACTION_UP)
				downData=null;
				
			if(c.dispatchTouchEvent(checkEvent))return true;
		}
		
		//No view touched, make Canvas's Touch Events
		return gesture.onTouchEvent(e);
	}
	
	private Canvas me = this;
	private GestureDetector gesture = new GestureDetector(getLayout(), new GestureDetector.SimpleOnGestureListener() {
		
		public boolean onDown(MotionEvent e) {
			if(scroll && (velocityX!=0||velocityY!=0)) {
				velocityX=0;
				velocityY=0;
				return true;
			}
			return false;
		}
		
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			
			if(!scroll) return false;
			
			scrollX+=distanceX;
			scrollY+=distanceY;
			
			return true;
		}
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			
			if(!scroll) return false;
			
			me.velocityX=velocityX*getLayout().getDeltaMillis();
			me.velocityY=velocityY*getLayout().getDeltaMillis();
			
			return true;
		}
	});
	
	protected boolean onKey(int keyCode, KeyEvent event) {
		return event.dispatch(callback);
	}
	
	private KeyEvent.Callback callback = new KeyEvent.Callback() {
		
		public boolean onKeyUp(int keyCode, KeyEvent event) {
			if(selected!=null && selected.dispatchKeyEvent(event.getKeycode(), event)) return true;
			return false;
		}
		
		public boolean onKeyRepeat(int keyCode, KeyEvent event) {
			if(selected!=null && selected.dispatchKeyEvent(event.getKeycode(), event)) return true;
			return selectionEvent(event);
		}
		
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(selected!=null && selected.dispatchKeyEvent(event.getKeycode(), event)) return true;
			return selectionEvent(event);
		}
	};
	
	protected boolean selectionEvent(KeyEvent event) {
		
		int next=0, back=0;
		int cd[];
		switch(arrangement) {
			case ARRANGEMENT_HORIZONTAL:
				next = KeyEvent.KEYCODE_DPAD_RIGHT;
				back = KeyEvent.KEYCODE_DPAD_LEFT;
			break;
			case ARRANGEMENT_VERTICAL:
				next = KeyEvent.KEYCODE_DPAD_DOWN;
				back = KeyEvent.KEYCODE_DPAD_UP;
			break;
		}
		if(event.getKeycode()!=next && event.getKeycode()!=back) return scroll(event);
		Content c;
		if(selected==null) {
			if (event.getKeycode()==next) {
				for(int i=0; i<renderData[0].size(); i++) {
					c = contentFromRenderData(i);
					cd = getRenderData(i);
					if(c.isSelectable() && c.getPositioning()==Content.POSITIONING_FIXED && cd[0]+canvasX < canvasDisplayW && cd[0]+cd[2]+canvasX>=0 && cd[1]+canvasY < canvasDisplayH && cd[1]+cd[3]+canvasY>=0) {
						
						return setSelectedEvent(c);
					}
				}
			}else {
				for(int i=renderData[0].size()-1; i>=0; i--) {
					c = contentFromRenderData(i);
					cd = getRenderData(i);
					if(c.isSelectable() && c.getPositioning()==Content.POSITIONING_FIXED && cd[0]+canvasX < canvasDisplayW && cd[0]+cd[2]+canvasX>=0 && cd[1]+canvasY < canvasDisplayH && cd[1]+cd[3]+canvasY>=0) {
							
						return setSelectedEvent(c);
					}
				}
			}
			return scroll(event);
		}
		
		int renderIndex = renderDataIndex(selected);
		if(renderIndex==-1) {
			if(this.selected!=null)
				this.selected.dispatchSelected(false);
			
			selected=null;
			return selectionEvent(event);
		}
		
		if(event.getKeycode()==next) {
			for(int i=renderIndex+1; i<renderData[0].size(); i++) {
				c = contentFromRenderData(i);
				if(c.isSelectable() && c.getPositioning()==Content.POSITIONING_FIXED) {
					cd = getRenderData(i);
					
					if(!(cd[0]+canvasX < canvasDisplayW && cd[0]+cd[2]+canvasX>=0 && cd[1]+canvasY < canvasDisplayH && cd[1]+cd[3]+canvasY>=0))
						break;
					
					return setSelectedEvent(c);
				}
			}
		}else {
			for(int i=renderIndex-1; i>=0; i--) {
				c = contentFromRenderData(i);
				if(c.isSelectable() && c.getPositioning()==Content.POSITIONING_FIXED) {
					
					cd = getRenderData(i);
					if(!(cd[0]+canvasX < canvasDisplayW && cd[0]+cd[2]+canvasX>=0 && cd[1]+canvasY < canvasDisplayH && cd[1]+cd[3]+canvasY>=0))
						break;
					
					return setSelectedEvent(c);
				}
			}
		}
		
		return scroll(event);
	}
	
	private boolean setSelectedEvent(Content selected) {
		
		if(this.selected!=null)
			this.selected.dispatchSelected(false);
		
		this.selected=selected;
		
		int i = renderDataIndex(selected);
		
		int x = ((Integer)renderData[1].elementAt(i)).intValue();
		int y = ((Integer)renderData[2].elementAt(i)).intValue();
		int w = ((Integer)renderData[3].elementAt(i)).intValue();
		int h = ((Integer)renderData[4].elementAt(i)).intValue();
		
		if(i!=-1) {
			int s;
			double vx=velocityX;
			double vy=velocityY;
			
			if(arrangement==ARRANGEMENT_VERTICAL || h<canvasHeight) {
			if(y+h>canvasHeight || y+h>canvasDisplayH) {
				if(h>canvasDisplayH) {
					vy=-DPADScrollVelocity;
				}else {
					if(y+h>canvasDisplayH)
						s=canvasDisplayH-y-h;
					else
						s=canvasHeight-y-h;
					vy=-Math.sqrt(- 2.5*velLoss * s);
				}
			}else if(y<0 || y+canvasY<0) {

				if(h>canvasDisplayH) {
					vy=DPADScrollVelocity;
				}else {
					if(y+canvasY<0) 
						s=y+canvasY;
					else
						s=y;
					vy=Math.sqrt( - 2.5*velLoss * s);
				}
			}}
			
			if(arrangement==ARRANGEMENT_HORIZONTAL || w<canvasWidth) {
			if(x+w>canvasWidth || x+w+canvasX>canvasDisplayW) {
				
				if(w>canvasDisplayW) {
					vx=-DPADScrollVelocity;
				}else {
					if(x+w+canvasX>canvasDisplayW)
						s=canvasDisplayW-x-w-canvasX;
					else
						s=canvasWidth-x-w;
					vx=-Math.sqrt( - 2.5*velLoss * s);
				}
			}else if(x<0 || x+canvasX<0) {
				
				if(w>canvasDisplayW) {
					vx=DPADScrollVelocity;
				}else {
					if(x+canvasX<0) 
						s=x+canvasX;
					else
						s=x;
					vx=Math.sqrt( - 2.5*velLoss * s);
				}
			}
			}
			
			velocityX=vx;
			velocityY=vy;
		}
		
		this.selected.dispatchSelected(true);
		
		return true;
	}
	
	public void onSelect() {
		
		if(selected == null) {
			Content c;
			for(int i=0; i<renderData[0].size(); i++) {
				c = contentFromRenderData(i);
				int[] cd = getRenderData(i);
				if(c.isSelectable() && c.getPositioning()==Content.POSITIONING_FIXED && cd[0] < canvasWidth && cd[0]+cd[2]+canvasX>=0 && cd[1]< canvasHeight && cd[1]+cd[3]+canvasY>=0) {
					setSelectedEvent(c);
					break;
				}
			}
		}
		
		if(selected!=null) selected.dispatchSelected(true);
	}
	
	public void onDeselect() {
		if(selected!=null) selected.dispatchSelected(false);
	}
	
	public boolean isSelectable() {
		if(contents==null || contents.size()<=0) return false;
		
		for(int i=0; i<contents.size(); i++) 
			if(((Content)contents.elementAt(i)).isSelectable()) 
				return true;
		
		
		if(!(minX==maxX && minY==maxY)) return true;
		
		return false;
		
	}
	
	private int[] getRenderData(int i) {
		int cx = ((Integer)renderData[1].elementAt(i)).intValue();
		int cy = ((Integer)renderData[2].elementAt(i)).intValue();
		int cw = ((Integer)renderData[3].elementAt(i)).intValue();
		int ch = ((Integer)renderData[4].elementAt(i)).intValue();
		
		return new int[] {cx, cy, cw, ch};
	}
	
	private int renderDataIndex(Content content) {
		
		for(int i=renderData[0].size()-1; i>=0; i--) {
			if(content == contentFromRenderData(i))
				return i;
		}
		
		return -1;
	}
	
	private Content contentFromRenderData(int i) {
		return ((Content)(contents.elementAt(((Short)(renderData[0].elementAt(i))).shortValue())));
	}
	
	private double DPADScrollVelocity=40;
	protected boolean scroll(KeyEvent event) {
		if(!scroll) return false;
		
		switch(event.getKeycode()) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			
			if(-scrollX<=minX)
				return false;
			
			if(velocityX>DPADScrollVelocity/2)
				velocityX=DPADScrollVelocity*3;
			else
				velocityX=DPADScrollVelocity;
			
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			
			if(canvasWidth-scrollX>=maxX)
				return false;
			
			if(velocityX<-DPADScrollVelocity/2)
				velocityX=-DPADScrollVelocity*3;
			else
				velocityX=-DPADScrollVelocity;
			
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			
			if(-scrollY<=minY)
				return false;
			
			if(velocityY>DPADScrollVelocity/2)
				velocityY=DPADScrollVelocity*3;
			else
				velocityY=DPADScrollVelocity;
			
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			
			if(canvasHeight-scrollY>=maxY)
				return false;
			
			if(velocityY<-DPADScrollVelocity/2)
				velocityY=-DPADScrollVelocity*3;
			else
				velocityY=-DPADScrollVelocity;
			
			return true;
	}
	
	return false;
	}
	
	public final void addContent(Content content) {
		contents.addElement(content);
	}
	
	public final boolean removeContent(Content content) {
		
		
		int i =contents.indexOf(content);
		if(i<0) return false;
		
		if(selected==content) {
			content.dispatchSelected(false);
			selected=null;
		}
		
		contents.setElementAt(null, i);
		
		//Ordered add to remove array
		int add=contentsToRemove.size();
		for(int a=add-1; a>0; a--) {
			if(((Integer)contentsToRemove.elementAt(a)).intValue() < i) {
				add = a+1;
				break;
			}
		}
		contentsToRemove.insertElementAt(new Integer(i), add);
		
		return true;
		
	}
	
	public final Vector getViews(){
		return contents;
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
	
	public final void setScrollable(boolean scroll) {
		this.scroll = scroll;
	}
	
	public final boolean getScrollable() {
		return scroll;
	}
	
	public Content getSelected(){
		if(selected==null)
			return this;
		
		if(selected instanceof Canvas) {
			return ((Canvas) selected).getSelected();
		}
		
		return selected;
	}

}
