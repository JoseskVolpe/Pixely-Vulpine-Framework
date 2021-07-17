package pixelyvulpine.contents;

import javax.microedition.lcdui.Command;

import pixelyvulpine.api.events.GestureDetector;
import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.events.MotionEvent;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.util.GraphicsFix;

public class Button extends Content{

	private Label label;
	protected GestureDetector gesture;
	private boolean selected;
	protected Command clickCom, selectCom, deselectCom;
	
	private boolean enabled=true;
	
	public Button(Layout context, ButtonPadding buttonPadding, String text) {
		super(context, buttonPadding);
		
		gesture = new GestureDetector(context, gestureListener);
		label = new Label(context, dimensionAttributes, text);
		label.impact();
	}
	
	public Button(Layout context, ButtonPadding buttonPadding, String text, TextFont font) {
		this(context, buttonPadding, text);
		
		label.impact();
	}
	
	public Button(Layout context, DimensionAttributes dimensionAttributes, String text) {
		super(context, dimensionAttributes);
		
		gesture = new GestureDetector(context, gestureListener);
		label = new Label(context, dimensionAttributes, text);
	}
	
	public Button(Layout context, DimensionAttributes dimensionAttributes, String text, TextFont font) {
		this(context, dimensionAttributes, text);
		
		label.setFont(font);
	}
	
	public boolean isSelectable() {
		return isEnabled();
	}
	
	protected void onSelect() {
		getLayout().dispatchCommand(selectCom);
		selected=true;
	}
	
	protected void onDeselect() {
		getLayout().dispatchCommand(deselectCom);
		selected=false;
	}
	
	protected boolean onClick() {
		
		if(!isEnabled()) return false;
		
		getLayout().dispatchCommand(clickCom, this);
		return true;
	}
	
	public boolean onKey(int keyCode, KeyEvent ev) {
		if(ev.getAction()==KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
			return onClick();
		
		return false;
	}
	
	public boolean onTouch(MotionEvent ev) {
		return gesture.onTouchEvent(ev);
	}
	
	protected GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
		public boolean onSingleTapUp(MotionEvent ev) {
			return onClick();
		}
	};
	
	public void paint(GraphicsFix g) {
		
		if(isEnabled())
			if(selected) {
				g.setColor(155,155,255);
			}else {
				g.setColor(255,255,255);
			}
		else
			g.setColor(110, 110, 110);
		
		g.fillRect(0, 0, getRenderWidth(g), getRenderHeight(g));
		
		label.paint(g);
		
	}
	
	/**
	 * Sets button's size to label's size
	 */
	public void impact() {
		
		label.impact();
		dimensionAttributes.getScaledDimension().width=0;
		dimensionAttributes.getScaledDimension().height=0;
		dimensionAttributes.getOffsetDimension().width=label.getDimension().getOffsetDimension().width;
		dimensionAttributes.getOffsetDimension().height=label.getDimension().getOffsetDimension().height;
		
	}
	
	public final void setText(String text) {
		label.setText(text);
		label.impact();
	}
	
	public final String getText() {
		return label.getText();
	}
	
	public final void setFont(TextFont font) {
		label.setFont(font);
		label.impact();
	}
	
	public final TextFont getFont() {
		return label.getFont();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled=enabled;
	}
	
	public final void setClickCommand(Command clickCom) {
		this.clickCom=clickCom;
	}
	
	public final void setSelectCommand(Command selectCom) {
		this.selectCom=selectCom;
	}
	
	public final void setDeselectCommand(Command deselectCom) {
		this.deselectCom=deselectCom;
	}
	
	public static class ButtonPadding extends DimensionAttributes{
		
		public ButtonPadding(Scaled scaled, Offset offset) {
			super(scaled,offset);
		}
		
		public ButtonPadding(int scaledx, int scaledy, int offsetx, int offsety) {
			super(new Scaled(scaledx, scaledy), new Offset(offsetx, offsety));
		}
		
		public static class Scaled extends DimensionAttributes.Scaled{
			
			public Scaled(int x, int y) {
				super(x, y, 0,0);
			}
		}
		
		public static class Offset extends DimensionAttributes.Offset{
			
			public Offset(int x, int y) {
				super(x,y,0,0);
			}
		}
	}

}
