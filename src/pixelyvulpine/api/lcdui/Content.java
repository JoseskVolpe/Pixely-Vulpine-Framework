package pixelyvulpine.api.lcdui;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import pixelyvulpine.Config;
import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.lcdui.Debug.Attributes;
import pixelyvulpine.api.util.GraphicsFix;
import pixelyvulpine.contents.Canvas;

public class Content implements Debug.Watcher{

	protected Debug debug;
	protected boolean forcePaint;
	
	/**Fixed position on canvas*/
	public final static byte POSITIONING_FIXED=0;
	
	/**Use POSITION_ANCHOR to use these*/
	public final static byte VERTICAL_ANCHOR_TOP=-1;
	public final static byte VERTICAL_ANCHOR_CENTER=0;
	public final static byte VERTICAL_ANCHOR_BOTTOM=1;
	public final static byte HORIZONTAL_ANCHOR_LEFT=-1;
	public final static byte HORIZONTAL_ANCHOR_CENTER=0;
	public final static byte HORIZONTAL_ANCHOR_RIGHT=1;
	private byte horizontalAnchor, verticalAnchor;
	
	/**Anchored to another corner*/
	public final static byte POSITIONING_ANCHORED=1;
	
	/**Absolute position on canvas, won't move on animation*/
	public final static byte POSITIONING_ABSOLUTE=2;
	
	private byte positioning;
	private boolean visible=true;
	private byte ZIndex;
	
	private Layout layout;
	protected DimensionAttributes dimensionAttributes;
	protected OnTouchListener onTouchListener;
	
	/**
	 * Scalable - Defines value according to screen's resolutions, in percent
	 * Offset - Defines value absolutely, in pixels
	 * @param Context
	 * @param DimensionAttributes
	 */
	public Content(Layout layout, DimensionAttributes dimensionAttributes) {
		
		this.layout = layout;
		this.dimensionAttributes=dimensionAttributes;
		
		debug = new Debug(this);
		
	}
	
	public void Stopped() {
		
	}
	
	public final void dispatchNoPaint() {
		int TraceID = Debug.traceObject(this, "noPaint");
		noPaint();
		Debug.removeFromTrace(TraceID);
	}
	
	protected void noPaint() {}
	
	public final int[] dispatchPrepaint(int width, int height) {
		
		int TraceID = Debug.traceObject(this, "prepaint");
		int r[] = prepaint(width, height);
		Debug.removeFromTrace(TraceID);
		
		return r;
	
	}
	
	protected int[] prepaint(int width, int height) {
		
		return new int[] {width, height};
		
	}
	
	public final void dispatchPaint(GraphicsFix g) {
		
		int TraceID = Debug.traceObject(this, "paint");
		if(!Config.getXRayMode() || forcePaint)
			paint(g);
		
		Debug.removeFromTrace(TraceID);
		TraceID = Debug.traceObject(this, "debug.paint");
		debug.paint(g);
		Debug.removeFromTrace(TraceID);
	}
	
	protected void paint(GraphicsFix g) {}
	
	private Vector historicalCoords = new Vector(0,1);
	public final boolean dispatchTouchEvent(MotionEvent event) {
		
		int TraceID = Debug.traceObject(this, "onTouch");
		
		boolean r;
		
		if(onTouchListener!=null && onTouchListener.onTouch(this, event)) 
			r=true;
		else
			r=onTouch(event);
		
		Debug.removeFromTrace(TraceID);
		return r;
	}
	
	public final boolean dispatchKeyEvent(int keyCode, KeyEvent event) {
		
		int TraceID = Debug.traceObject(this, "onKey");
		boolean r;
		
		if(layout.onKey(this, keyCode, event))
			r=true;
		else
			r=onKey(keyCode, event);
		
		Debug.removeFromTrace(TraceID);
		return r;
	}
	
	public final Vector getHistoricalCoords() {
		return historicalCoords;
	}
	
	protected boolean onKey(int keyCode, KeyEvent event) {
		return false;
	}
	
	protected boolean onTouch(MotionEvent event) {
		return false;
	}
	
	public void setOnTouchListener(OnTouchListener onTouchListener) {
		this.onTouchListener = onTouchListener;;
	}
	
	public DimensionAttributes getDimension() {
		return dimensionAttributes;
	}
	
	public void setDimension(DimensionAttributes dimensionAttributes) {
		this.dimensionAttributes=dimensionAttributes;
	}
	
	protected final int getRenderWidth(GraphicsFix g) {
		return g.getDimensionWidth();
	}
	
	protected final int getRenderHeight(GraphicsFix g) {
		return g.getDimensionHeight();
	}
	
	protected final Layout getLayout() {
		return layout;
	}
	
	/**
	 * Sets positioning mode
	 * Note: Only POSITIONING_FIXED are accessible by D-Pad control, other modes are meant for personalization or for buttons with custom buttons (for example: how # key is used in WhatsApp for KaiOS)
	 * Default: POSITIONING_FIXED
	 * @param POSITIONING_FIXED, POSITIONING_ANCHORED, POSIIIONING_ABSOLUTE
	 */
	public final void setPositioning(byte positioning) {
		this.positioning=positioning;
	}
	
	/**
	 * @return positioning mode
	 */
	public final byte getPositioning() {
		return positioning;
	}
	
	/**
	 * Sets horizontal anchor
	 * @param HORIZONTAL_ANCHOR_LEFT, HORIZONAL_ANCHOR_CENTER, HORIZONTAL_ANCHOR_RIGHT
	 */
	public final void setHorizontalAnchor(byte horizontalAnchor) {
		this.horizontalAnchor = horizontalAnchor;
	}
	
	/**
	 * @return horizontal anchoring
	 */
	public final byte getHorizontalAnchor() {
		return horizontalAnchor;
	}
	
	/**
	 * Sets vertical anchor
	 * @param VERTICAL_ANCHOR_LEFT, VERTICAL_ANCHOR_CENTER, VERTICAL_ANCHOR_RIGHT
	 */
	public final void setVerticalAnchor(byte verticalAnchor) {
		this.verticalAnchor = verticalAnchor;
	}
	
	/**
	 * @return vertical anchoring
	 */
	public final byte getVerticalAnchor() {
		return verticalAnchor;
	}
	
	/**
	 * @return visible
	 */
	public final boolean isVisible() {
		return visible;
	}
	
	/**
	 * Show/Hide visible
	 * Default: true
	 * @param true to make content visible
	 */
	public final void setVisible(boolean visible) {
		
		this.visible = visible;
		
	}
	
	public boolean isSelectable() {
		return false;
	}
	
	protected void onSelect() {}
	
	protected void onDeselect() {}
	
	public final void dispatchSelected(boolean selected) {
		if(selected) 
			onSelect();
		else
			onDeselect();
	}
	
	/**
	 * Set rendering frame
	 * @param z
	 */
	public final void setZIndex(int z) {
		this.ZIndex=(byte)z;
	}
	
	/**
	 * Returns rendering frame
	 * @return Z Index
	 */
	public final byte getZIndex() {
		return ZIndex;
	}
	
	public static interface OnTouchListener{
		public boolean onTouch(Content view, MotionEvent event) ;
	}

	public final void watchAttributes(Attributes attributes) {
		
		attributes.showAttribute("layout", layout);
		attributes.showAttribute("forcePaint", forcePaint);
		attributes.showAttribute("visible", visible);
		attributes.showAttribute("ZIndex", ZIndex);
		
		watchViewAttributes(attributes);
	}
	
	protected void watchViewAttributes(Attributes attributes) {
		
	}
	
}
