package pixelyvulpine.test.scenes;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Command;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.ContentListener;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.Button;
import pixelyvulpine.contents.Canvas;
import pixelyvulpine.contents.Label;
import pixelyvulpine.test.App;

public class TouchTest extends Layout{

	private Label touchInfo;
	
	public TouchTest(App app) {
		super(app);
		
		animation = ANIMATION_SLIDE_LEFT;
		
		this.setTitle("Booping test");
		
		Label l = new Label(this,  new int[] {0, 0}, new int[] {0, 0}, new int[] {0, 90}, new int[] {0, 40}, this.getTitle());
		l.setColor(new Color(255,255,255));
		l.getFont().setStyle(Font.STYLE_BOLD);
		l.impact();
		l.setPositioning(Content.POSITIONING_ANCHORED);
		l.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		
		Canvas LCanvas = new Canvas(this,  new int[] {0, 0}, new int[] {0, 0}, new int[] {100, 0}, new int[] {0, l.getFont().getFontSize()});
		LCanvas.addContent(l);
		LCanvas.setBackgroundColor(null);
		LCanvas.setForegroundColor(null);
		this.addContent(LCanvas);
		
		
		touchInfo = new Label(this, new int[] {0,0}, new int[] {0,0}, new int[] {100,0}, new int[] {100,-45});
		touchInfo.setColor(new Color(255,255,255));
		touchInfo.setText("¡Boop your phone!");
		touchInfo.setMultiline(true);
		addContent(touchInfo);
		
	}
	
	public void Setup() {
		
		setFullScreenMode(true);
		
	}
	
	public void paintLayout(Graphics g) {
		
		
		
	}

	protected void pointerLongTouch(int x, int y) {
		
		touchInfo.setText("Screen long-touched\nx = "+x+"\ny = "+y);
		
	}
	
	protected void pointerTouch(int x, int y) {
		
		touchInfo.setText("Screen touched\nx = "+x+"\ny = "+y);
		
	}
	
	protected void pointerDrag(int start_x, int start_y, int last_x, int last_y, int final_x, int final_y, boolean longTouch) {
	
		touchInfo.setText("Screen dragged\nstart_x = "+start_x+"\nstart_y = "+start_y+"\nlast_x = "+last_x+"\nlast_y = "+last_y+"\nfinal_x = "+final_x+"\nfinal_y = "+final_y+"\nlongTouch: "+longTouch);
		
	}

}
