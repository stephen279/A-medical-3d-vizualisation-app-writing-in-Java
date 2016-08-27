package assignment2;

public class Range {
	
	private int minimum;
	private int maximum;
	private int range;
	
	public Range(int minimum, int maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}
	
	public Range(Image image) {
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		
		for(int y=0; y<height; y++)
			for(int x=0; x<width; x++) {

				int pixel = image.getPixel(x,y);
				if(pixel < min) 
					min = pixel;
				if(pixel > max)
					max = pixel;
			}
		minimum = min;
		maximum = max;
		range = max - min;	
	}
	
	public int getMinimum() {
		return minimum;
	}
	
	public int getMaximum() {
		return maximum;
	}
	
	public int getRange() {
		return range;
	}
	
	
}
