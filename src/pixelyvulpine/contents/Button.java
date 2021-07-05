package pixelyvulpine.contents;

import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;

public class Button extends Content{

	private Label label;
	
	public Button(Layout layout, int[] x, int[] y, int[] width, int[] height, String text) {
		super(layout, x, y, width, height);
		
		label = new Label(layout, x, y, width, height, text);
		label.impact();
		
	}
	
	public Button(Layout layout, int[] x, int[] y, int[] width, int[] height, String text, TextFont font) {
		super(layout, x, y, width, height);
		
		label = new Label(layout, x, y, width, height, text, font);
		label.impact();
		
	}
	
	public void paint(Graphics g) {
		
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
		
		setWidth(new int[] {0, label.getWidth()[1]});
		setHeight(new int[] {0, label.getHeight()[1]});
		
	}
	
	public boolean pressed() {
		
		System.out.println(label.getText());
		
		return false;
	}
	
	public final void setText(String text) {
		label.setText(text);
	}
	
	public final String getText() {
		return label.getText();
	}
	
	public final void setFont(TextFont font) {
		label.setFont(font);
	}
	
	public final TextFont getFont() {
		return label.getFont();
	}

}
