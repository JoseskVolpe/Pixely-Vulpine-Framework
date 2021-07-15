package pixelyvulpine.contents;

import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.util.GraphicsFix;

public class Button extends Content{

	private Label label;
	
	public Button(Layout context, ButtonPadding buttonPadding, String text) {
		super(context, buttonPadding);
		
		selectable=true;
		label = new Label(context, dimensionAttributes, text);
		label.impact();
	}
	
	public Button(Layout context, ButtonPadding buttonPadding, String text, TextFont font) {
		super(context, buttonPadding);
		
		selectable=true;
		label = new Label(context, dimensionAttributes, text, font);
		label.impact();
	}
	
	public Button(Layout layout, DimensionAttributes dimensionAttributes, String text) {
		super(layout, dimensionAttributes);
		
		selectable=true;
		label = new Label(layout, dimensionAttributes, text);
		
	}
	
	public Button(Layout layout, DimensionAttributes dimensionAttributes, String text, TextFont font) {
		super(layout, dimensionAttributes);
		
		selectable=true;
		label = new Label(layout, dimensionAttributes, text, font);
		
	}
	
	public void paint(GraphicsFix g) {
		
		/*if(isSelected()) {
			g.setColor(0,0,50);
		}else {
			*/g.setColor(255,255,255);/*
		}*/
		
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
