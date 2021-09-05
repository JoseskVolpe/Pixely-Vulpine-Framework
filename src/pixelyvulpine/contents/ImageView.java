package pixelyvulpine.contents;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.ImageTransform;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.GraphicsFix;

public class ImageView extends Content{
	
	private final static byte ERRORSIZE=60;
	
	private Image imagePointer;
	private Image cache;
	private int width, height;
	private boolean fit, error;
	
	public ImageView(Layout layout, Image image, int scaledX, int scaledY, int offsetX, int offsetY) {
		this(layout, image, new DimensionAttributes(new DimensionAttributes.Scaled(scaledX, scaledY, 0, 0), new DimensionAttributes.Offset(offsetX, offsetY, image.getWidth(), image.getHeight())));
	}
	
	public ImageView(Layout layout, Image image, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
		if(image==null) {
			return;
		}
		
		width=image.getWidth();
		height=image.getHeight();
		
		imagePointer=image;
		
	}
	
	public ImageView(Layout layout, Image image, DimensionAttributes dimensionAttributes, boolean fit) {
		this(layout, image, dimensionAttributes);
		this.fit=fit;
	}
	
	public void noPaint() {
		cache=null;
	}
	
	public int[] prepaint(int width, int height) {
		
		rescale(width, height);
		
		if(error) {
			return new int[] {Math.min(ERRORSIZE, width), Math.min(ERRORSIZE, height)};
		}
		
		return new int[] {this.width, this.height};
		
	}
	
	protected void paint(GraphicsFix g) {
		
		if(width<=0 || height<=0) return;
		
		if(cache==null) {
			try {
				cache = ImageTransform.createResizedImage(imagePointer, this.width, this.height);
				error=false;
			}catch(OutOfMemoryError e) {
					cache=null;
					error=true; //Can't show this ImageView :c
			}
		}
		
		if(error || cache==null) {
			g.setColor(0xffffff);
			g.drawRect(0, 0, imagePointer.getWidth(), imagePointer.getHeight());
			g.setColor(0xff0000);
			g.drawLine(0, 0, imagePointer.getWidth(), imagePointer.getHeight());
			g.drawLine(0, imagePointer.getHeight(), imagePointer.getWidth(), 0);
			Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
			g.setColor(0xffffff);
			g.setFont(font);
			g.drawString("Image Error", imagePointer.getWidth()/2, (imagePointer.getHeight()/2)-(font.getHeight()/2), Graphics.HCENTER|Graphics.TOP);
			font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
			g.setColor(0xff0000);
			g.setFont(font);
			g.drawString("Image Error", imagePointer.getWidth()/2, (imagePointer.getHeight()/2)-(font.getHeight()/2), Graphics.HCENTER|Graphics.TOP);
			return;
		}
		
		g.drawImage(cache, 0, 0, 0);
		
	}
	
	public void setScalePictureToFit(boolean fit) {
		this.fit=fit;
	}
	
	public final boolean getScalePictureToFit() {
		return fit;
	}
	
	public final boolean isError() {
		return error;
	}
	
	public void rescale(int width, int height) {
		
		if(imagePointer==null) {
			this.width=0;
			this.height=0;
			cache=null;
			return;
		}
		
		try {
			if(fit) {
				byte scale=0;
				if(Math.max(width, height) == height) scale=1;
				if(width==height) scale=2;
				
				switch(scale) {
					case 0: //height
						width=(int) (imagePointer.getHeight()*(height/(float)(imagePointer.getHeight())));
					break;
					
					case 1: //width
						height = (int)(imagePointer.getWidth()*(width/(float)(imagePointer.getWidth())));
					break;
					
					case 2: //Equal size
					break;
				}
			}
			
			if(width==this.width && height==this.height) return;
			cache=null;
		}catch(Exception e0) {
			this.width=width;
			this.height=height;
			error=true;
			cache=null;
			return;
		}
		
		this.width=width;
		this.height=height;
		
		error=false;
		
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setImage(Image image) {
		this.imagePointer=image;
		
		if(image==null) return;
		
		width=image.getWidth();
		height=image.getHeight();
	}
	
	public Image getImage() {
		return imagePointer;
	}

}
