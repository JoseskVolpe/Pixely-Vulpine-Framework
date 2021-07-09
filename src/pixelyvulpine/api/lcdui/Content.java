package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.MotionEvent;

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
	
	private Layout layout;
	protected DimensionAttributes dimensionAttributes;
	private boolean selected=false;
	
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
	
	public void paint(Graphics g) {
		
		
		
	}
	
	public boolean dispatchTouchEvent(MotionEvent event) {
		return false;
	}
	
	/**
	 * @return true/false selected
	 */
	public final boolean isSelected() {
		return selected;
	}
	
	public DimensionAttributes getDimension() {
		return dimensionAttributes;
	}
	
	public void setDimension(DimensionAttributes dimensionAttributes) {
		this.dimensionAttributes=dimensionAttributes;
	}
	
	protected final int getRenderWidth(Graphics g) {
		return g.getClipWidth();
	}
	
	protected final int getRenderHeight(Graphics g) {
		return g.getClipHeight();
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
	
	
	
}
