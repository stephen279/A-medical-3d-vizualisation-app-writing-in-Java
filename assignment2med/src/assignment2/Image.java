package assignment2;

import java.awt.image.BufferedImage;

public class Image {

	private int[] pixels = null;
	
	private int width = -1;
	private int height = -1;
	
	private Range range = null;
	
	public Image(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
	}
	
	public int getPixel(int x, int y) {
		return pixels[y*width + x];
	}
	
	public void setPixel(int x, int y, int value) {
		pixels[y*width + x] = value;
		range = null;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Range getRange() {
		if(range == null)
			range = new Range(this);
		return range;
	}
	
	public BufferedImage getAsBufferedImage() {
		Range r = getRange();
		int min = r.getMinimum();
		int range = r.getRange();
		BufferedImage bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		for(int y=0; y<this.height; y++)
			for(int x=0; x<this.width; x++) {
				int integerPixel = this.getPixel(x, y);
				int bytePixel = (255 * (integerPixel - min)) / range;
				int argbPixel = 0xFF000000 | (bytePixel << 16) | (bytePixel << 8) | bytePixel;
				bufferedImage.setRGB(x, y, argbPixel);
			}
		
		return bufferedImage;
	}
}
