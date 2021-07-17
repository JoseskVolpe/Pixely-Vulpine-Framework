package pixelyvulpine.test.scenes;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.lcdui.TextFont;
import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.DimensionAttributes.Scaled;
import pixelyvulpine.contents.Button;
import pixelyvulpine.contents.Canvas;

public class Vibrator extends Layout implements CommandListener{

	private Command vib = new Command("Vibrate", Command.ITEM, 1);
	private Button vibrate;
	
	public Vibrator(MIDlet app) {
		super(app);
		
		animation = Layout.ANIMATION_SLIDE_LEFT;
		setBackgroundColor(new Color(150,10, 37));
		
		vibrate = new Button(this, new Button.ButtonPadding(0,10,0,0), "¡Vibrate lewd! x3", new TextFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
		vibrate.setClickCommand(vib);
		
		Canvas center = new Canvas(this, new DimensionAttributes(new Scaled(0,0,100,100)));
		center.setBackgroundColor(null);
		center.setForegroundColor(null);
		center.setContentAlignment(Canvas.ALIGNMENT_CENTER);
		
		center.addContent(vibrate);
		addContent(center);
		
		this.setCommandListener(this);
		
	}
	
	public void Setup() {
		this.setFullScreenMode(true);
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if(arg0 == vib) {
			Display.getDisplay(getMIDlet()).vibrate(5000);
			vibrate.setEnabled(false);
			vibrate.setText("OwO");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
			vibrate.setText("¡Vibrate lewd! x3");
			vibrate.setEnabled(true);
		}
		
	}

}
