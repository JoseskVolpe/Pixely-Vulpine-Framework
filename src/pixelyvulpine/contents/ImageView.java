package pixelyvulpine.contents;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import pixelyvulpine.api.lcdui.Content;
import pixelyvulpine.api.lcdui.Layout;
import pixelyvulpine.api.util.ThreadFlag;

public class ImageView extends Content{
	
	private Image originalI, image;
	private int width, height;
	private boolean fit, error;
	private ThreadFlag threadFlag;
	private CircularProgressBar loading;
	private ImageView me;
	
	public ImageView(Layout layout, Image image, int[] x, int[] y) {
		super(layout, x, y, new int[] {0,imageWidth(image)}, new int[] {0,imageHeight(image)});
		
		this.image = image;
		originalI=this.image;
		if(image==null) error=true;
		this.width=imageWidth(image);
		this.height = imageHeight(image);
		me=this;
		
		loading=new CircularProgressBar(layout, new int[] {0,0}, new int[] {0,0}, new int[] {100,0});
		
	}
	
	public ImageView(Layout layout, Image image, int[] x, int[] y, int[] width, int[] height) {
		super(layout, x, y, width, height);
		
		originalI=image;
		me=this;
		if(image==null) error=true;
		
		loading=new CircularProgressBar(layout, new int[] {0,0}, new int[] {0,0}, new int[] {100,0});
		
	}
	
	public ImageView(Layout layout, Image image, int[] x, int[] y, int[] width, int[] height, boolean fit) {
		super(layout, x, y, width, height);
		
		me=this;
		originalI=image;
		this.fit = fit;
		if(image==null) error=true;
		
		loading=new CircularProgressBar(layout, new int[] {0,0}, new int[] {0,0}, new int[] {100,0});
		
	}
	
	private static int imageWidth(Image image) {
		if(image == null) return 20;
		
		try {
			return image.getWidth();
		}catch(Exception e) {
			return 20;
		}
	}
	
	private static int imageHeight(Image image) {
		if(image == null) return 20;
		
		try {
			return image.getHeight();
		}catch(Exception e) {
			return 20;
		}
	}
	
	public void Stopped() {
		if(threadFlag!=null) {
			threadFlag.Terminate();
			threadFlag=null;
		}
	}
	
	public int[] prepaint(int width, int height) {
		
		rescale(width, height);
		
		return new int[] {this.width, this.height};
		
	}
	
	public void paint(Graphics g) {
		
		if(error) {
			g.setColor(0xffffff);
			g.drawRect(0, 0, width, height);
			g.setColor(0xff0000);
			g.drawLine(0, 0, width, height);
			g.drawLine(0, height, width, 0);
			Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
			g.setColor(0xffffff);
			g.setFont(font);
			g.drawString("Image Error", width/2, (height/2)-(font.getHeight()/2), Graphics.HCENTER|Graphics.TOP);
			font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
			g.setColor(0xff0000);
			g.setFont(font);
			g.drawString("Image Error", width/2, (height/2)-(font.getHeight()/2), Graphics.HCENTER|Graphics.TOP);
			return;
		}
		
		if(image==null) {
			loading.prepaint(width, height);
			
			loading.paint(g);
			return;
		}
		
		g.drawImage(image, 0, 0, Graphics.LEFT|Graphics.TOP);
		
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
	
	public final boolean isLoaded() {
		return (threadFlag==null&&image!=null&&!error);
	}
	
	protected void onError(Throwable e) {}
	
	protected void onLoad() {}
	
	public void rescale(int width, int height) {
		
		if(width==this.width && height==this.height) return;
		
		if(originalI==null) {
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
						width=(int) (originalI.getHeight()*(height/(float)(originalI.getHeight())));
					break;
					
					case 1: //width
						height = (int)(originalI.getWidth()*(width/(float)(originalI.getHeight())));
					break;
				}
			}
			
			if(width==this.width && height==this.height) return;
			image=null;
		}catch(Exception e0) {
			this.width=width;
			this.height=height;
			error=true;
			return;
		}
		
		this.width=width;
		this.height=height;
		
		error=false;
		if(threadFlag!=null) 
			threadFlag.Terminate();
		
		threadFlag = new ThreadFlag();
		Thread t = new Thread(new resizeThread());
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
		
	}
	
	public final static Image resizeImage(Image image, int width, int height) {
		return resizeImage(image, width, height, null, null);
	}
	
	//TODO: Support for simple progress bar
	//TODO: Move to pixelyulpine.api.lcdui
	public final static Image resizeImage(Image image, int width, int height, CircularProgressBar progress, ThreadFlag tf) {
		
		if(image==null) return null;
		if(width==image.getWidth() && height==image.getHeight()) return image;
		
		System.gc();
		try {
		
			int[] data = new int[width*height];
			
			if(progress!=null) {
				progress.setMin(0);
				progress.setMax((width/2)*(height/2));
				progress.setProgress(0);
			}
			
			for(long y=0; y<height/2; y++) {
				for(long x=0; x<width/2; x++) {
					
					if(tf!=null && tf.isTerminated()) return null;
					
					//image.getRGB(data, width*y+x, 1, (int)(image.getWidth()*(x/(double)width)), (int)(image.getHeight()*(y/(double)height)),1,1);
					
					long rx=x;
					long ry=y;
					image.getRGB(data, (int)(width*ry+rx), 1, (int)(image.getWidth()*(rx/(double)width)), (int)(image.getHeight()*(ry/(double)height)),1,1);
					rx=(width/2)+x;
					ry=y;
					image.getRGB(data, (int) (width*ry+rx), 1, (int)(image.getWidth()*(rx/(double)width)), (int)(image.getHeight()*(ry/(double)height)),1,1);
					rx=x;
					ry=(height/2)+y;
					image.getRGB(data, (int) (width*ry+rx), 1, (int)(image.getWidth()*(rx/(double)width)), (int)(image.getHeight()*(ry/(double)height)),1,1);
					rx=(width/2)+x;
					ry=(height/2)+y;
					image.getRGB(data, (int) (width*ry+rx), 1, (int)(image.getWidth()*(rx/(double)width)), (int)(image.getHeight()*(ry/(double)height)),1,1);
					
					if(progress!=null)
						progress.setProgress(progress.getProgress()+1);
					
				}
			}
			
			
			image = Image.createRGBImage(data, width, height, true);
			data=null;
			
		}catch(Error e) {
			image=null;
			e.printStackTrace();
		}catch(Exception e) {
			image=null;
			e.printStackTrace();
		}
		return image;
		
	}
	
	private class resizeThread implements Runnable{
		
		public void run() {
			try {
				
				ThreadFlag tf = threadFlag;
				
				Image i = ImageView.resizeImage(originalI, width, height, loading, threadFlag);
				if(tf.isTerminated()) return;
				if(i==null) {
					error=true;
					getContentListener().onContentError(me, new NullPointerException());
					onError(new NullPointerException());
				}else {
					getContentListener().onContentLoad(me);
					onLoad();
				}
				image=i;
				threadFlag=null;
			}catch(Error e) {
				image=null;
				e.printStackTrace();
				threadFlag=null;
				getContentListener().onContentError(me, e);
				onError(new NullPointerException());
			}catch(Exception e) {
				image=null;
				e.printStackTrace();
				threadFlag=null;
				getContentListener().onContentError(me, e);
				onError(new NullPointerException());
			}
		}
		
	}

}
