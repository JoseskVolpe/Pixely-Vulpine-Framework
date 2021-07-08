package pixelyvulpine.test.scenes;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Command;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.ContentListener;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.Button;
import pixelyvulpine.contents.Canvas;
import pixelyvulpine.contents.Label;

public class CanvasTest extends Layout implements ContentListener{

	public CanvasTest(MIDlet app) {
		super(app);
		
		animation = ANIMATION_SLIDE_LEFT;
		
		this.setTitle("Canvas test uwu");
		
		Label l = new Label(this,  new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40)), this.getTitle());
		l.setColor(new Color(255,255,255));
		l.getFont().setStyle(Font.STYLE_BOLD);
		l.impact();
		l.setPositioning(Content.POSITIONING_ANCHORED);
		l.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		
		Canvas LCanvas = new Canvas(this,  new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 0), new DimensionAttributes.Offset(0, 0, 0, l.getFont().getFontSize())));
		LCanvas.addContent(l);
		LCanvas.setBackgroundColor(null);
		LCanvas.setForegroundColor(null);
		this.addContent(LCanvas);
		
		this.addContent(new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40))));
		this.addContent(new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40))));
		this.addContent(new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40))));
		this.addContent(new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40))));
		this.addContent(new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40))));
		this.addContent(new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40))));
		
		
		Canvas testrb = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(-1,-1,0,0), new DimensionAttributes.Offset(-3,-3,40,40)));
		testrb.setPositioning(Content.POSITIONING_ANCHORED);
		testrb.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
		testrb.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		this.addContent(testrb);
		
		Canvas testlb = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(1,-1,0,0), new DimensionAttributes.Offset(3,-3,40,40)));
		testlb.setPositioning(Content.POSITIONING_ANCHORED);
		testlb.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
		testlb.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		this.addContent(testlb);
		
		Canvas testrt = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(-1,1,0,0), new DimensionAttributes.Offset(-3,3,40,40)));
		testrt.setPositioning(Content.POSITIONING_ANCHORED);
		testrt.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
		testrt.setVerticalAnchor(Content.VERTICAL_ANCHOR_TOP);
		this.addContent(testrt);

		Canvas testlt = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(1,1,0,0), new DimensionAttributes.Offset(3,3,40,40)));
		testlt.setPositioning(Content.POSITIONING_ANCHORED);
		testlt.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
		testlt.setVerticalAnchor(Content.VERTICAL_ANCHOR_TOP);
		this.addContent(testlt);
		
		Canvas testcl = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(1,0,0,0), new DimensionAttributes.Offset(3,0,40,40)));
		testcl.setPositioning(Content.POSITIONING_ANCHORED);
		testcl.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
		testcl.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		this.addContent(testcl);
		
		Canvas testcc = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,0,0), new DimensionAttributes.Offset(0,0,40,40)));
		testcc.setPositioning(Content.POSITIONING_ANCHORED);
		testcc.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		testcc.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		this.addContent(testcc);
		
		Canvas testcr = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(-1,0,0,0), new DimensionAttributes.Offset(-3,0,40,40)));
		testcr.setPositioning(Content.POSITIONING_ANCHORED);
		testcr.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
		testcr.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		this.addContent(testcr);
		
		Canvas testct = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,1,0,0), new DimensionAttributes.Offset(0,3,40,40)));
		testct.setPositioning(Content.POSITIONING_ANCHORED);
		testct.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		testct.setVerticalAnchor(Content.VERTICAL_ANCHOR_TOP);
		this.addContent(testct);
		
		Canvas testcb = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,-1,0,0), new DimensionAttributes.Offset(0,-3,40,40)));
		testcb.setPositioning(Content.POSITIONING_ANCHORED);
		testcb.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		testcb.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		this.addContent(testcb);
		
		
		Canvas testABS = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(10,10,0,0), new DimensionAttributes.Offset(5,5,40,40)));
		testABS.setPositioning(Content.POSITIONING_ABSOLUTE);
		this.addContent(testABS);
		
	}
	
	public void Setup() {
		
		setFullScreenMode(true);
		//setNavigationBar(true);
		//setFullScreenMode(false);
		
		this.addCommand(new Command("Test1", Command.BACK, 1));
		this.addCommand(new Command("Test2", Command.BACK, 1));
		this.addCommand(new Command("Test3", Command.BACK, 1));
		this.addCommand(new Command("Test4", Command.BACK, 1));
		this.addCommand(new Command("Test5", Command.BACK, 1));
		this.addCommand(new Command("Test6", Command.OK, 1));
		
	}
	
	public void paintLayout(Graphics g) {
		
		
		
	}

	public boolean contentPressed(Content content) {
		
		System.out.println(((Button)content).getText());
		
		return false;
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
