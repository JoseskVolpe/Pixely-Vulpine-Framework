package pixelyvulpine.test.scenes;

import javax.microedition.lcdui.Font;
import javax.microedition.midlet.MIDlet;

import pixelyvulpine.api.lcdui.Color;
import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.contents.Canvas;
import pixelyvulpine.contents.Label;

public class AlphaBlendTest extends Layout{

	public AlphaBlendTest(MIDlet app) {
		super(app);
		
		this.animation = Layout.ANIMATION_SLIDE_LEFT;
		
		this.setTitle("Alpha blending Test OwO");
		
		
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
		
		Canvas colors = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 100, 100), new DimensionAttributes.Offset(0, 0, 0, -l.getFont().getFontSize()-7)));
		colors.setBackgroundColor(new Color(128,128,128));
		colors.setForegroundColor(null);
		
		Canvas red = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 75, 75), new DimensionAttributes.Offset(0, 0, 0, 0)));
		red.setBackgroundColor(new Color(85, 255, 0, 0));
		red.setForegroundColor(null);
		red.setPositioning(Content.POSITIONING_ANCHORED);
		red.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		red.setVerticalAnchor(Content.VERTICAL_ANCHOR_TOP);
		colors.addContent(red);
		
		Canvas green = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 75, 75), new DimensionAttributes.Offset(0, 0, 0, 0)));
		green.setBackgroundColor(new Color(85, 0, 255, 0));
		green.setForegroundColor(null);
		green.setPositioning(Content.POSITIONING_ANCHORED);
		green.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
		green.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		colors.addContent(green);
		
		Canvas blue = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 75, 75), new DimensionAttributes.Offset(0, 0, 0, 0)));
		blue.setBackgroundColor(new Color(85, 0, 0, 255));
		blue.setForegroundColor(null);
		blue.setPositioning(Content.POSITIONING_ANCHORED);
		blue.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
		blue.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		colors.addContent(blue);
		
		Canvas redI = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(2, 2, 20, 20)));
		redI.setBackgroundColor(new Color(255, 0, 0));
		redI.setForegroundColor(new Color(125,0,0));
		redI.setPositioning(Content.POSITIONING_ANCHORED);
		redI.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_CENTER);
		redI.setVerticalAnchor(Content.VERTICAL_ANCHOR_TOP);
		colors.addContent(redI);
		
		Canvas greenI = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(2, -2, 20, 20)));
		greenI.setBackgroundColor(new Color(0, 255, 0));
		greenI.setForegroundColor(new Color(0,125,0));
		greenI.setPositioning(Content.POSITIONING_ANCHORED);
		greenI.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_LEFT);
		greenI.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		colors.addContent(greenI);
		
		Canvas blueI = new Canvas(this, new DimensionAttributes(new DimensionAttributes.Scaled(0, 0, 0, 0), new DimensionAttributes.Offset(-2, -2, 20, 20)));
		blueI.setBackgroundColor(new Color(0, 0, 255));
		blueI.setForegroundColor(new Color(0,0,125));
		blueI.setPositioning(Content.POSITIONING_ANCHORED);
		blueI.setHorizontalAnchor(Content.HORIZONTAL_ANCHOR_RIGHT);
		blueI.setVerticalAnchor(Content.VERTICAL_ANCHOR_BOTTOM);
		colors.addContent(blueI);
		
		this.addContent(colors);
		
	}
	
	public void Setup() {
		setFullScreenMode(true);
	}
	
	

}
