package pixelyvulpine.test.scenes;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Command;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.Button;
import pixelyvulpine.contents.Canvas;
import pixelyvulpine.contents.Label;

public class CanvasTest extends Layout{

	public CanvasTest(MIDlet app) {
		super(app);
		
		animation = ANIMATION_SLIDE_LEFT;
		
		this.setTitle("Canvas test uwu");
		
		Label l = new Label(this,  new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40)), this.getTitle());
		l.setColor(new Color(255,255,255));
		l.setFontStyle(Font.STYLE_BOLD);
		l.impact();
		l.setPositioning(Content.POSITIONING_ANCHORED);
		l.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		
		Canvas LCanvas = new Canvas(this,  new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 0), new DimensionAttributes.Offset(0, 0, 0, l.getFont().getHeight())));
		LCanvas.addContent(l);
		LCanvas.setBackgroundColor(null);
		LCanvas.setForegroundColor(null);
		this.addContent(LCanvas);
		
		CustomCanvas rowC;
		
		rowC=new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40)));
		rowC.setZIndex(1);
		addContent(rowC);
		rowC=new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40)));
		rowC.setZIndex(1);
		addContent(rowC);
		rowC=new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40)));
		rowC.setZIndex(1);
		addContent(rowC);
		rowC=new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40)));
		rowC.setZIndex(1);
		addContent(rowC);
		rowC=new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40)));
		rowC.setZIndex(1);
		addContent(rowC);
		rowC=new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(0, 0, 90, 40)));
		rowC.setZIndex(1);
		addContent(rowC);
		
		
		CustomCanvas testrb = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(-1,-1,0,0), new DimensionAttributes.Offset(-3,-3,40,40)));
		testrb.setPositioning(Content.POSITIONING_ANCHORED);
		testrb.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
		testrb.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		testrb.setZIndex(2);
		this.addContent(testrb);
		
		CustomCanvas testlb = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(1,-1,0,0), new DimensionAttributes.Offset(3,-3,40,40)));
		testlb.setPositioning(Content.POSITIONING_ANCHORED);
		testlb.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
		testlb.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		testlb.setZIndex(2);
		this.addContent(testlb);
		
		CustomCanvas testrt = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(-1,1,0,0), new DimensionAttributes.Offset(-3,3,40,40)));
		testrt.setPositioning(Content.POSITIONING_ANCHORED);
		testrt.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
		testrt.setVerticalAnchor(Content.VERTICAL_ANCHOR_TOP);
		testrt.setZIndex(2);
		this.addContent(testrt);

		CustomCanvas testlt = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(1,1,0,0), new DimensionAttributes.Offset(3,3,40,40)));
		testlt.setPositioning(Content.POSITIONING_ANCHORED);
		testlt.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
		testlt.setVerticalAnchor(Content.VERTICAL_ANCHOR_TOP);
		testlt.setZIndex(2);
		this.addContent(testlt);
		
		CustomCanvas testcl = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(1,0,0,0), new DimensionAttributes.Offset(3,0,40,40)));
		testcl.setPositioning(Content.POSITIONING_ANCHORED);
		testcl.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
		testcl.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		testcl.setZIndex(2);
		this.addContent(testcl);
		
		CustomCanvas testcc = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,0,0,0), new DimensionAttributes.Offset(0,0,40,40)));
		testcc.setPositioning(Content.POSITIONING_ANCHORED);
		testcc.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		testcc.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		testcc.setZIndex(2);
		this.addContent(testcc);
		
		CustomCanvas testcr = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(-1,0,0,0), new DimensionAttributes.Offset(-3,0,40,40)));
		testcr.setPositioning(Content.POSITIONING_ANCHORED);
		testcr.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
		testcr.setVerticalAnchor(Content.VERTICAL_ANCHOR_CENTER);
		testcr.setZIndex(2);
		this.addContent(testcr);
		
		CustomCanvas testct = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,1,0,0), new DimensionAttributes.Offset(0,3,40,40)));
		testct.setPositioning(Content.POSITIONING_ANCHORED);
		testct.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		testct.setVerticalAnchor(Content.VERTICAL_ANCHOR_TOP);
		testct.setZIndex(2);
		this.addContent(testct);
		
		CustomCanvas testcb = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0,-1,0,0), new DimensionAttributes.Offset(0,-3,40,40)));
		testcb.setPositioning(Content.POSITIONING_ANCHORED);
		testcb.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		testcb.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		testcb.setZIndex(2);
		this.addContent(testcb);
		
		
		CustomCanvas testABS = new CustomCanvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(10,10,0,0), new DimensionAttributes.Offset(5,5,40,40)));
		testABS.setPositioning(Content.POSITIONING_ABSOLUTE);
		testABS.setZIndex(3);
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
	
	private static class CustomCanvas extends Canvas{

		public CustomCanvas(Layout layout, DimensionAttributes dimensionAttributes) {
			super(layout, dimensionAttributes);
			
			this.setBackgroundColor(new Color(120,10,10,10));
			this.setForegroundColor(new Color(120,255,0,0));
			
		}
		
	}

}
