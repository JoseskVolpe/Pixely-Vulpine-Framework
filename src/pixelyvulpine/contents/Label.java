package pixelyvulpine.contents;

import javax.microedition.lcdui.Font;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.Paragraph;
import pixelyvulpine.api.system.Crash;
import pixelyvulpine.api.util.GraphicsFix;

public class Label extends Content{
	
	public static final Color NO_BACKGROUND = null;
	
	private Paragraph par = new Paragraph();
	private String text = "Label";
	private Color backgroundColor = NO_BACKGROUND;
	private Color fontColor = new Color(0,0,0);
	
	public Label(Layout layout, DimensionAttributes dimensionAttributes) {
		
		super(layout, dimensionAttributes);
		
	}
	
	public Label(Layout layout, DimensionAttributes dimensionAttributes, String text) {
		super(layout, dimensionAttributes);
		this.text = text;
		
	}
	
	public Label(Layout layout, DimensionAttributes dimensionAttributes, String text, Font font) {
		this(layout, dimensionAttributes, text);
		par.setFont(font);
	}
	
	public void noPaint() {
		par.clear();
	}
	
	public void paint(GraphicsFix g) {
		fontColor.updateColor(g);
		par.setText(text);
		par.prepareDimension(g.getDimensionWidth(), g.getDimensionHeight());
		par.render(g);
	}
	
	public final void setText(String text) {
		this.text = text;
	}
	
	public final String getText() {
		return text;
	}
	
	/**@deprecated**/
	public final void setTextFont(Font font) {
		setFont(font);
	}
	
	/**@deprecated**/
	public final Font getTextFont() {
		return getFont();
	}
	
	public final void setFont(Font font) {
		par.setFont(font);
	}
	
	public final Font getFont() {
		return par.getFont();
	}
	
	public final void setFontFace(int face) {
		par.setFont(Font.getFont(face, par.getFont().getStyle(), par.getFont().getSize()));
	}
	
	public final void setFontStyle(int style) {
		par.setFont(Font.getFont(par.getFont().getFace(), style, par.getFont().getSize()));
	}
	
	public final void setFontSize(int size) {
		par.setFont(Font.getFont(par.getFont().getFace(), par.getFont().getStyle(), size));
	}
	
	public final void setColor(Color fontColor){
		this.fontColor = fontColor;
	}
	
	public final Color getColor() {
		return fontColor;
	}
	
	public final void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public final Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public final void setMultiline(boolean multiline) {
		par.setMultiline(multiline);
	}
	
	public final boolean getMultiline() {
		return par.getMultiline();
	}
	
	/**
	 * Sets Label's size to text's size
	 * It'll only work on non-multiline texts
	 * 
	 * @return Impact sucessful
	 */
	public final boolean impact() {
		
		try {
		
			if(par==null || par.getMultiline()) return false;
			
			int w=0, h=0;
			
			w=getFont().stringWidth(text.toString());
			h=getFont().getHeight();
			
			dimensionAttributes.getScaledDimension().width=0;
			dimensionAttributes.getScaledDimension().height=0;
			dimensionAttributes.getOffsetDimension().width=(short) w;
			dimensionAttributes.getOffsetDimension().height=(short) h;
			
			return true;
		
		}catch(Exception e) {
			Crash.showCrashMessage(this.getLayout().getMIDlet(), e, "Could not impact Label '"+this.getText()+"'", Crash.FRAMEWORK_CRASH);
		}catch(Error e) {
			Crash.showCrashMessage(this.getLayout().getMIDlet(), e, "Could not impact Label '"+this.getText()+"'", Crash.FRAMEWORK_CRASH);
		}
		
		return false;
		
	}

}
