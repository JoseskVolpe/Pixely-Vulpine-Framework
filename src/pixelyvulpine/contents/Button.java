package pixelyvulpine.contents;

import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.util.GraphicsFix;

public class Button extends Content{

	private Label label;
	
	public Button(Layout layout, DimensionAttributes dimensionAttributes, String text) {
		super(layout, dimensionAttributes);
		
		label = new Label(layout, dimensionAttributes, text);
		label.impact();
		
	}
	
	public Button(Layout layout, DimensionAttributes dimensionAttributes, String text, TextFont font) {
		super(layout, dimensionAttributes);
		
		label = new Label(layout, dimensionAttributes, text, font);
		label.impact();
		
	}
	
	public void paint(GraphicsFix g) {
		
		if(isSelected()) {
			g.setColor(0,0,50);
		}else {
			g.setColor(255,255,255);
		}
		
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

}
