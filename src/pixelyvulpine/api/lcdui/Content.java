package pixelyvulpine.api.lcdui;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Content {

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
	private ContentListener listenner;
	
	/**
	 * Use POSITIONING_ABSOLUTE or POSITIONING_ANCHORED to use that
	 * Scalable is in percent
	 * Offset is in pixel
	 * */
	private byte scalableX, scalableY;
	private int offsetX, offsetY;
	
	/**
	 * Your content's size
	 * Scalable is in percent
	 * Offset is in pixel
	 */
	private byte scalableWidth, scalableHeight;
	private int offsetWidth, offsetHeight;
	
	private Layout layout;
	private boolean selected=false;
	
	private static Image selectNavbarIconSource;
	
	/**
	 * Scalable - Defines value according to screen's resolutions, in percent
	 * Offset - Defines value absolutely, in pixels
	 * @param scalableX, offsetX
	 * @param scalableY, offsetY
	 * @param scalableWidth, offsetWidth
	 * @param scalableHeight, offsetHeight
	 */
	public Content(Layout layout, int x[], int y[], int width[], int height[]) {
		
		this.layout = layout;
		
		this.scalableX = (byte) x[0];
		this.scalableY = (byte) y[0];
		this.scalableWidth = (byte) width[0];
		this.scalableHeight = (byte) height[0];
		
		this.offsetX = x[1];
		this.offsetY = y[1];
		this.offsetWidth = width[1];
		this.offsetHeight = height[1];
		
	}
	
	public void Stopped() {
		
	}
	
	public int[] prepaint(int width, int height) {
		
		return new int[] {width, height};
		
	}
	
	public void paint(Graphics g) {
		
		
		
	}
	
	/**
	 * Sets the center navbar icon for when any content is selected
	 * @param source
	 * @throws IOException 
	 */
	public final static void setNavbarIcon(String source) throws IOException {
		
		setNavbarIcon(Image.createImage(source));
		
	}
	
	/**
	 * Sets the center navbar icon for when any content is selected
	 * @param icon
	 */
	public final static void setNavbarIcon(Image icon) {
		
		Content.selectNavbarIconSource = icon;
		
	}
	
	/**
	 * Returns the navbar icon for when any content is selected
	 * @return icon
	 */
	public final static Image getNavbarIcon() {
		return selectNavbarIconSource;
	}
	
	/**When getting pressed by key or touch
	 @return true if ignore Layout command
	 */
	public boolean pressed() {
		
		if(listenner==null) return true;
		
		return listenner.contentPressed(this);
		
	}
	
	
	public final boolean onSelect() {
		if(selected()) {
			selected=true;
			return true;
		}
		
		return false;
	}
	
	/**
	 * When getting selected
	 */
	protected boolean selected() {
		
		return false;
		
	}
	
	public final void onDeselect() {
		selected=false;
		deselected();
	}
	
	/**
	 * When getting deselected
	 */
	protected void deselected() {
		
		
		
	}
	
	/**
	 * @return true/false selected
	 */
	public final boolean isSelected() {
		return selected;
	}
	
	
	/**
	 * Getting dragged by touch
	 * @param relative x
	 * @param relative y
	 */
	public void touchDrag(int x, int y) {
		
		
		
	}
	
	/**
	 * Key pressed
	 * @param keyCode
	 * @param key
	 * 
	 * @return true if nothing is used and you want the Layout to make their operations
	 */
	public boolean keyDown(int keyCode, int key) {
		
		return true;
		
	}
	
	/**
	 * Key released
	 * @param keyCode
	 * @param key
	 * @return true if nothing is used and you want the Layout to make their operations
	 */
	public boolean keyUp(int keyCode, int key) {
		
		return true;
		
	}
	
	/**
	 * @return scalable, offset
	 */
	public final int[] getX() {
		return new int[] {scalableX, offsetX};
	}
	
	/**
	 * @param scalable, offset
	 */
	public final void setX(int x[]) {
		this.scalableX = (byte) x[0];
		this.offsetX = x[1];
	}
	
	/**
	 * @return scalable, offset
	 */
	public final int[] getY() {
		return new int[] {scalableY, offsetY};
	}
	
	/**
	 * @param scalable, offset
	 */
	public final void setY(int y[]) {
		this.scalableY = (byte) y[0];
		this.offsetY = y[1];
	}
	
	/**
	 * @return scalable, offset
	 */
	public final int[] getWidth() {
		return new int[] {scalableWidth, offsetWidth};
	}
	
	/**
	 * @param scalable, offset
	 */
	public final void setWidth(int width[]) {
		this.scalableWidth = (byte) width[0];
		this.offsetWidth = width[1];
	}
	
	/**
	 * @return scalable, offset
	 */
	public final int[] getHeight() {
		return new int[] {scalableHeight, offsetHeight};
	}
	
	/**
	 * @param scalable, offset
	 */
	public final void setHeight(int height[]) {
		this.scalableHeight = (byte) height[0];
		this.offsetHeight = height[1];
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
	
	public final ContentListener getContentListener() {
		return listenner;
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
	 * Assign a ContentListener
	 */
	public final void assign(ContentListener listenner) {
		this.listenner = listenner;
	}
	
}
