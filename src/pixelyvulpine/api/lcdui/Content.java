package pixelyvulpine.api.lcdui;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.util.GraphicsFix;

public class Content{

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
	protected boolean selectable;
	
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
		
	}
	
	public void Stopped() {
		
	}
	
	public void noPaint() {}
	
	public int[] prepaint(int width, int height) {
		
		return new int[] {width, height};
		
	}
	
	public void paint(GraphicsFix g) {
		
		
		
	}
	
	private Vector historicalCoords = new Vector(0,1);
	public final boolean dispatchTouchEvent(MotionEvent event) {
		
		if(onTouchListener!=null && onTouchListener.onTouch(this, event)) {
			return true;
		}
		
		return onTouch(event);
	}
	
	public final boolean dispatchKeyEvent(int keyCode, KeyEvent event) {
		return onKey(keyCode, event);
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
	
	public final boolean isSelectable() {
		return selectable;
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
	
}
