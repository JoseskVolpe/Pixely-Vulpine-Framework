package pixelyvulpine.test.scenes;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Command;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.List;

public class ListTest extends Layout{

	public ListTest(MIDlet app) {
		super(app);
		
		animation=Layout.ANIMATION_SLIDE_LEFT;
		
		List l = new List(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,50)));
		List l2 = new List(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,50)));
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
		
	}
	
	public void Setup() {
		setFullScreenMode(true);
	}
	

}
