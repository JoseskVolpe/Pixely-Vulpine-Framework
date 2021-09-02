package pixelyvulpine.test.scenes;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import pixelyvulpine.api.events.KeyEvent;
import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Command;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.contents.Button;
import pixelyvulpine.contents.Canvas;
import pixelyvulpine.contents.ImageView;
import pixelyvulpine.contents.Label;
import pixelyvulpine.contents.ScrollableCanvas;
import pixelyvulpine.contents.TextBox;
import pixelyvulpine.layouts.EmptyScrollLayout;
import pixelyvulpine.test.App;
import pixelyvulpine.test.views.ErrorView;

public class ScrollTest extends EmptyScrollLayout{

	public ScrollTest(App app) {
		super(app);
		
		setContentAlignment(Canvas.ALIGNMENT_CENTER);

		Image logoI = null;
		
		try {
			logoI=Image.createImage("/joseskvolpe.png");
		}catch(IOException e) {}
		
		animation = ANIMATION_SLIDE_LEFT;
		
		this.setTitle("Scroll test uwu");
		
		Label l = new Label(this,  new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0,0,90,40)), this.getTitle());
		l.setColor(new Color(255,255,255));
		l.setFontStyle(Font.STYLE_BOLD);
		l.impact();
		l.setPositioning(Content.POSITIONING_ANCHORED);
		l.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		
		Canvas LCanvas = new Canvas(this,  new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 0), new DimensionAttributes.Offset(0,0,0,l.getFont().getHeight())));
		LCanvas.addContent(l);
		LCanvas.setBackgroundColor(null);
		LCanvas.setForegroundColor(null);
		this.addContent(LCanvas);
		
		Label label = new Label(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0,0,90,40)), "Testing OwO");
		label.setBackgroundColor(new Color(150,150,150));
		label.impact();
		this.addContent(label);
		Canvas canvas = new ScrollableCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0,0,90,30)));
		canvas.setBackgroundColor(new Color(150,25,25,25));
		canvas.setForegroundColor(new Color(150,255,0,0));
		canvas.setArrangement(Canvas.ARRANGEMENT_HORIZONTAL);
		this.addContent(canvas);
		Button button = new Button(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0,2,90,40)), "Boop UwU");
		button.impact();
		this.addContent(button);
		Button button2 = new Button(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0,2,90,40)), "heeey OwO");
		button2.impact();
		button2.setEnabled(false);
		this.addContent(button2);
		Button button3 = new Button(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0,2,90,40)), "Yiff @w@");
		button3.impact();
		this.addContent(button3);
		
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "A"));
		Label cL = new Label(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0,0,90,40)), "B");
		cL.impact();
		canvas.addContent(cL);
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "C"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "D"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "E"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "F"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "G"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "H"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "I"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "J"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "K"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "L"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "M"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "O"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "P"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "Q"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "R"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "S"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "T"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "U"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "V"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "W"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "X"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "Y"));
		canvas.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 0), "Z"));
		
		
		TextBox text = new TextBox(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,40,15), new DimensionAttributes.Offset(0, 2, 0, 0)));
		addContent(text);
		
		int b=0;
		
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new ImageView(this, logoI, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,200,200), new DimensionAttributes.Offset(0, 0, 0, 0)), true));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 150), new DimensionAttributes.Offset(0,2,90,40)), "Big Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		this.addContent(new Button(this, new Button.ButtonPadding(0, 0, 0, 2), "Button "+(b++)));
		
		this.addContent(new ErrorView(this, new DimensionAttributes(new DimensionAttributes.Offset(0,0,10,10))));
		
		
		Label ml = new Label(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 0), new DimensionAttributes.Offset(0,0,0,120)), "SO, you have come this far UwU\n¿Don't ya?\nYou may asking what the yiff is this... This is a scroll test page for a WIP UI framework, it's a open-source project, and you may find it at github.com/JoseskVolpe/Pixely-Vulpine-Framework.\n*Licks you* cutie :3");
		ml.setMultiline(true);
		ml.setColor(new Color(255,255,255));
		this.addContent(ml);
		
		
	}
	
	public void Setup() {
		
		new KeyEvent(this,0,0);//TODO: REMOVE ME
		
		
		setFullScreenMode(true);
		//setNavigationBar(true);
		//setFullScreenMode(false);
		
		Image yes=null;
		Image no=null;
		Image fox=null;
		
		try {
			yes=Image.createImage("/icons/dark/navbar/yes.png");
			no=Image.createImage("/icons/dark/navbar/no.png");
			fox=Image.createImage("/icons/dark/navbar/fox.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Command c;
		this.addCommand(new Command("Test1", Command.BACK, 1));
		this.addCommand(new Command("Test2", Command.BACK, 1));
		this.addCommand(new Command("Test3", Command.BACK, 1));
		this.addCommand(new Command("Test4", Command.BACK, 1));
		this.addCommand(new Command("Test5", Command.BACK, 1));
		this.addCommand(new Command("Test6", Command.OK, 1));
		c = new Command("Test7", Command.OK, 1);
		c.setIcon(no);
		this.addCommand(c);
		c = new Command("Test8", Command.OK, 1);
		c.setIcon(yes);
		this.addCommand(c);
		c=new Command("Test9", Command.OK, 1);
		c.setIcon(fox);
		this.addCommand(c);
		this.addCommand(new Command("Test10", Command.OK, 1));
		this.addCommand(new Command("Test11", Command.OK, 1));
		this.addCommand(new Command("Test12", Command.OK, 1));
		this.addCommand(new Command("Test13", Command.OK, 1));
		this.addCommand(new Command("Test14", Command.OK, 1));
		this.addCommand(new Command("Test15", Command.OK, 1));
		this.addCommand(new Command("Test16", Command.OK, 1));
		this.addCommand(new Command("Test17", Command.OK, 1));
		this.addCommand(new Command("Test18", Command.OK, 1));
		this.addCommand(new Command("Test19", Command.OK, 1));
		this.addCommand(new Command("Test20", Command.OK, 1));
		
	}
	
	public void paintLayout(Graphics g) {
		
		
		
	}

	public boolean onContentLoad(Content content) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onContentError(Content content, Throwable e) {
		// TODO Auto-generated method stub
		return false;
	}

}
