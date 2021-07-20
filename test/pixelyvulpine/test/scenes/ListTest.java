package pixelyvulpine.test.scenes;

import java.io.IOException;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Command;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.Canvas;
import pixelyvulpine.contents.Label;
import pixelyvulpine.contents.List;

public class ListTest extends Layout implements CommandListener{

	private Label t;
	
	public ListTest(MIDlet app) {
		super(app);
		
		animation=Layout.ANIMATION_SLIDE_LEFT;
		
		Canvas tc = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,0), new DimensionAttributes.Offset(0, 0, 0, Font.getDefaultFont().getHeight())));
		tc.setContentAlignment(Canvas.ALIGNMENT_CENTER);
		
		t = new Label(this, new DimensionAttributes());
		t.setText("Select something ewe");
		t.setColor(new Color(255,255,255));
		t.impact();
		tc.addContent(t);
		addContent(tc);
		
		List l = new List(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,50)));
		List l2 = new List(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,50), new DimensionAttributes.Offset(0,0,0,-Font.getDefaultFont().getHeight())));
		addContent(l);
		addContent(l2);
		
		Image ic = null;
		
		try {
			ic=Image.createImage("/icons/dark/navbar/select.png");
		}catch(IOException e) {}
		
		for(int i=0; i<50; i++) {
			
			Command c = new Command("Command "+i, Command.ITEM, 1);
			c.setIcon(ic);
			l.add(c);
			
			javax.microedition.lcdui.Command c2 = new javax.microedition.lcdui.Command("Legacy Command "+i, Command.ITEM, 1);
			l2.add(c2);
		}
		
		setCommandListener(this);
		
	}
	
	public void Setup() {
		setFullScreenMode(true);
	}

	public void commandAction(javax.microedition.lcdui.Command arg0, Displayable arg1) {
		
		t.setText(arg0.getLabel());
		t.impact();
		
	}
	

}
