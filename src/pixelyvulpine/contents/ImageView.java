package pixelyvulpine.contents;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.DimensionAttributes;
import pixelyvulpine.api.lcdui.ImageTransform;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.GraphicsFix;
import pixelyvulpine.api.util.ThreadFlag;

public class ImageView extends Content{
	
	private final static byte ERRORSIZE=60;
	
	private int[] data, renderData;
	private int owidth, oheight, width, height;
	private boolean fit, error;
	private ImageView me;
	
	public ImageView(Layout layout, Image image, int scaledX, int scaledY, int offsetX, int offsetY) {
		super(layout, new DimensionAttributes(new DimensionAttributes.Scaled(scaledX, scaledY, 0, 0), new DimensionAttributes.Offset(offsetX, offsetY, image.getWidth(), image.getHeight())));
		
		me=this;
		
		if(image==null) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}
		
		try {
			data=new int[image.getWidth()*image.getHeight()];
			image.getRGB(data, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
			renderData=data;
		}catch(Exception e) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}catch(Error e) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}
			
		this.owidth=image.getWidth();
		this.oheight = image.getHeight();
		this.width=owidth;
		this.height=oheight;
		
	}
	
	public ImageView(Layout layout, Image image, DimensionAttributes dimensionAttributes) {
		super(layout, dimensionAttributes);
		
		me=this;
		if(image==null) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}
		
		try {
			data=new int[image.getWidth()*image.getHeight()];
			renderData=null;
			image.getRGB(data, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
		}catch(Exception e) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}catch(Error e) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}
		
		this.owidth = image.getWidth();
		this.oheight = image.getHeight();
		
	}
	
	public ImageView(Layout layout, Image image, DimensionAttributes dimensionAttributes, boolean fit) {
		super(layout, dimensionAttributes);
		
		
		me=this;
		this.fit=fit;
		if(image==null) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}
		
		try {
			data=new int[image.getWidth()*image.getHeight()];
			renderData=null;
			image.getRGB(data, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
		}catch(Exception e) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}catch(Error e) {
			error=true;
			owidth=ERRORSIZE;
			oheight=ERRORSIZE;
			return;
		}
		
		this.owidth = image.getWidth();
		this.oheight = image.getHeight();
		
	}
	
	private static int imageWidth(Image image) {
		if(image == null) return 90;
		
		try {
			return image.getWidth();
		}catch(Exception e) {
			return ERRORSIZE;
		}
	}
	
	private static int imageHeight(Image image) {
		if(image == null) return 90;
		
		try {
			return image.getHeight();
		}catch(Exception e) {
			return ERRORSIZE;
		}
	}
	
	public int[] prepaint(int width, int height) {
		
		rescale(width, height);
		
		if(renderData==null) {
			try {
				renderData = ImageTransform.resize(data, owidth, oheight, this.width, this.height);
			}catch(OutOfMemoryError e) {
				try {
					data = ImageTransform.resize(data, owidth, oheight, this.width, this.height); //Substitute original data. Image may look weird if the ImageView is resized again, but that's ok
					renderData = data; //Pointer renderData to data
				}catch(OutOfMemoryError e2) {
					data=null;
					renderData=null;
					error=true; //Can't show this ImageView :c
					return new int[] {ERRORSIZE, ERRORSIZE};
				}
			}
		}
		
		return new int[] {this.width, this.height};
		
	}
	
	public void paint(Graphics g) {
		
		if(error) {
			g.setColor(0xffffff);
			g.drawRect(0, 0, owidth, oheight);
			g.setColor(0xff0000);
			g.drawLine(0, 0, owidth, oheight);
			g.drawLine(0, oheight, owidth, 0);
			Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
			g.setColor(0xffffff);
			g.setFont(font);
			g.drawString("Image Error", owidth/2, (oheight/2)-(font.getHeight()/2), Graphics.HCENTER|Graphics.TOP);
			font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
			g.setColor(0xff0000);
			g.setFont(font);
			g.drawString("Image Error", owidth/2, (oheight/2)-(font.getHeight()/2), Graphics.HCENTER|Graphics.TOP);
			return;
		}
		
		int width = g.getClipWidth();
		int height = g.getClipHeight();
		
		if(renderData==null) {
			return;
		}
		
		GraphicsFix.drawRGB(g, renderData, 0, width, 0, 0, width, height, true);
		
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
		
		if(width==this.width && height==this.height) return;
		
		if(data==null) {
			this.width=width;
			this.height=height;
			return;
		}
		
		try {
			if(fit) {
				byte scale=0;
				if(Math.max(width, height) == height) scale=1;
				
				switch(scale) {
					case 0: //height
						width=(int) (oheight*(height/(float)(oheight)));
					break;
					
					case 1: //width
						height = (int)(owidth*(width/(float)(owidth)));
					break;
				}
			}
			
			if(width==this.width && height==this.height) return;
			renderData=null;
		}catch(Exception e0) {
			this.width=width;
			this.height=height;
			error=true;
			renderData=null;
			return;
		}
		
		this.width=width;
		this.height=height;
		
		error=false;
		
	}

}
