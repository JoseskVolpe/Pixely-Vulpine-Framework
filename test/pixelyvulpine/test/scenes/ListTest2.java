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

public class ListTest2 extends Layout implements CommandListener{

	private Label t;
	
	public ListTest2(MIDlet app) {
		super(app);
		
		animation=Layout.ANIMATION_SLIDE_LEFT;
		
		Canvas tc = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,0), new DimensionAttributes.Offset(0, 0, 0, Font.getDefaultFont().getHeight())));
		tc.setContentAlignment(Canvas.ALIGNMENT_CENTER);
		
		t = new Label(this, new DimensionAttributes());
		t.setText("Select something ewe");
		t.setFont(Font.getDefaultFont());
		t.setColor(new Color(255,255,255));
		t.impact();
		tc.addContent(t);
		addContent(tc);
		
		List l = new List(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,100,100), new DimensionAttributes.Offset(0, 0, 0, -Font.getDefaultFont().getHeight())));
		addContent(l);
		
		Image ic = null;
		Image ic2=null;
		Image ic3=null;
		
		try {
			ic=Image.createImage("/icons/dark/navbar/select.png");
			ic2=Image.createImage("/icons/dark/navbar/yes.png");
			ic3=Image.createImage("/icons/dark/navbar/no.png");
		}catch(IOException e) {}

		
		Command c = new Command("Select", Command.ITEM, 1);
		c.setIcon(ic);
		l.add(c);
		Command c2 = new Command("Yes", Command.ITEM, 1);
		c2.setIcon(ic2);
		l.add(c2);
		Command c3 = new Command("NULL", Command.ITEM, 1);
		c3.setIcon(null);
		l.add(c3);
		javax.microedition.lcdui.Command c4  = new javax.microedition.lcdui.Command("Legacy", Command.ITEM, 1);
		l.add(c4);
		Command c5 = new Command("No", Command.ITEM, 1);
		c5.setIcon(ic3);
		l.add(c5);
		
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
