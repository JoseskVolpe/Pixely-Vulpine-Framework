package pixelyvulpine.api.lcdui;

import javax.microedition.lcdui.Image;

public class ImageTransform {

	public static Image resize(Image image, int desiredWidth, int desiredHeight) {
		
		int originalWidth = image.getWidth();
		int originalHeight = image.getHeight();
		if(originalWidth==desiredWidth && originalHeight==desiredHeight) return image;
		
		int data[] = new int[originalWidth*originalHeight];
		image.getRGB(data, 0, originalWidth, 0, 0, originalWidth, originalHeight);
		return Image.createRGBImage(resize(data,  originalWidth, originalHeight, desiredWidth, desiredHeight), desiredWidth, desiredHeight, true);
	}
	
	//Thanks Illiescu for the concept, that was a huuuuuuge study >w<
	public static int[] resize(int[] data, int originalWidth, int originalHeight, int desiredWidth, int desiredHeight) {
		
		if(originalWidth==desiredWidth && originalHeight==desiredHeight) return data;
		
		int rData[] = new int[desiredWidth*desiredHeight];
		
		int hPerc = (int)((originalHeight / (double)desiredHeight) * 1024.0D);
		int wPerc = (int)((originalWidth / (double)desiredWidth) * 1024.0D);
		int cLine = desiredWidth * -1;
		
		for(int y=0; y < desiredHeight; y++) {
			int cRow = ((hPerc * y) >> 10) * originalWidth;
			cLine+=desiredWidth;
			int cWidth= wPerc * -1;
			
			for(int x=0; x<desiredWidth; x++) {
				cWidth += wPerc;
				rData[cLine + x] = data[cRow + (cWidth >> 10)];
			}
		}
		
		return rData;
	}
	
}
