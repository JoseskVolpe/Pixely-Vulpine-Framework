package pixelyvulpine.contents;

import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.util.GraphicsFix;

public class Button extends Content{

	private Label label;
	private boolean selected;
	
	private boolean enabled=true;
	
	public Button(Layout context, ButtonPadding buttonPadding, String text) {
		super(context, buttonPadding);
		
		label = new Label(context, dimensionAttributes, text);
		label.impact();
	}
	
	public Button(Layout context, ButtonPadding buttonPadding, String text, TextFont font) {
		super(context, buttonPadding);
		
		label = new Label(context, dimensionAttributes, text, font);
		label.impact();
	}
	
	public Button(Layout layout, DimensionAttributes dimensionAttributes, String text) {
		super(layout, dimensionAttributes);
		
		label = new Label(layout, dimensionAttributes, text);
		
	}
	
	public Button(Layout layout, DimensionAttributes dimensionAttributes, String text, TextFont font) {
		super(layout, dimensionAttributes);
		
		label = new Label(layout, dimensionAttributes, text, font);
		
	}
	
	public boolean isSelectable() {
		return isEnabled();
	}
	
	protected void onSelect() {
		selected=true;
	}
	
	protected void onDeselect() {
		selected=false;
	}
	
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
	
	public boolean pressed() {
		
		System.out.println(label.getText());
		
		return false;
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
