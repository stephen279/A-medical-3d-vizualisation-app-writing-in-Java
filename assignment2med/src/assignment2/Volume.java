package assignment2;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;


public class Volume {

	private int[] voxels = null;
	
	private int width; 
	private int height;
	private int depth;
	
	private Range range = null;
	
	public Volume(int width, int height, int depth) {
		voxels = new int[width*height*depth];
		this.width = width;
		System.out.println("width"+ width);
		this.height = height;
		System.out.println("hight"+ height);
		this.depth = depth;
		System.out.println("depth"+ depth);
	}
	
	public Volume(String fileName) {
		load(fileName);
	}
	
	public int getWidth() {
		System.out.println("width"+this.width);
		return this.width;
		
	}
	
	public int getHeight() {
		System.out.println("hight"+ height);
		return this.height;
	}
	
	public int getDepth(){
		System.out.println("depth"+ depth);
		return this.depth;
	}
	
	
	public int getVoxel(int x, int y, int z) {
		return voxels[y*width*depth + z*width + x];
	}
	
	public void setVoxel(int x, int y, int z, int value) {
		voxels[y*width*depth + z*width + x] = value;
		range = null;
	}
	
	public BufferedImage getSliceAsBufferedImage() {
		
		Volume volume = new Volume("Stanford-CT_Head.vol");
		int volumeWidth = volume.getWidth();
		int volumeHeight = volume.getHeight();
		int volumeDepth = volume.getDepth();
	
		Range range = getRange();
		int min = range.getMinimum();
		System.out.println("min"+min);
		int max = range.getMaximum();
		System.out.println("max"+max);
	
		int diff = max - min;
		System.out.println("diff"+diff);

		BufferedImage image = new BufferedImage(volumeWidth, volumeHeight, BufferedImage.TYPE_INT_ARGB);
		


		float s = 8.00f;
		float eulersNum = (float)Math.exp(s);
		
	    

		
		// initialize variables
	    float  C_out = 0.0f;  // initialize to 0.0 the color coming into the voxel that is furthest from the view plane is black (0.0)lize to black (0.0)
	    float C_in;
	  
	    for(int x=0; x<volumeWidth; x++)
	    	for(int y=0; y<volumeHeight; y++){
	     	      
	        C_in = 0.0f;
	    //   for(int z=0; z<volumeDepth; z++)     // Render from front of head to back
	       for(int z=volumeDepth-1; z>=0; z--)   // come from Back of head furthest from plane. This process starts at the voxel that is furthest from the view plane
	        {
	          int voxel = getVoxel(x, y, z); // Change around axis for Question 2
	          int grey = ((voxel - min) * 255)/diff ;       
	          float alpha = grey/255.0f;    //255.0 (white); 
	          alpha = (float)(alpha * Math.exp(s*alpha)/eulersNum);
	          C_out = C_in*(1.0f - alpha) + grey*alpha;// e.g. ->  Cin = 0, C = 255, alpha = 0.8.
	          C_in = C_out;    // C_in is now the value of C_out going through the loop
	          
	        }

	        int grey = (int)C_out;   //cast to int for pixel formula
	        int pixel = 0xff000000 | (grey << 16) | (grey << 8) | grey;
	        image.setRGB(x,volumeHeight-y-1, pixel);// use volumeHeight-1 else upside down
	     }
	    	  	        
	      
	   

		return image;
		
	      }
	
	
	
	public void getSlice(int plane, int sliceIndex) {
		
		
	}
	
	public Range getRange() {
		if(range == null)
			calculateRange();
		return range;
	}
	
	public void calculateRange() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int y=0; y<height; y++)
			for(int z=0; z<depth; z++)
				for(int x=0; x<width; x++) {
					int voxel = getVoxel(x,y,z);
					if(voxel < min) min = voxel;
					if(voxel > max) max = voxel;
				}
		range = new Range(min, max);
	}
	
	public void save(String fileName) {
		try{
			System.out.println("Saving volume to file");
			long startTime = System.currentTimeMillis();

			RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
			randomAccessFile.writeInt(width);
			randomAccessFile.writeInt(height);
			randomAccessFile.writeInt(depth);
			FileChannel fileChannel = randomAccessFile.getChannel();
			ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 12, 4*voxels.length);
			for(int voxel : voxels)
				byteBuffer.putInt(voxel);
			randomAccessFile.close();
			
			long saveTime = System.currentTimeMillis() - startTime;
			System.out.println("Complete ("+saveTime+"ms)");
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void load(String fileName) {
		try {
			System.out.println("Loading volume from file");
			long startTime = System.currentTimeMillis();
			
			RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
			this.width = randomAccessFile.readInt();
			this.height = randomAccessFile.readInt();
			this.depth = randomAccessFile.readInt();
			this.voxels = new int[width*height*depth];
			FileChannel fileChannel = randomAccessFile.getChannel();
			ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 12, 4*voxels.length);
			for(int v=0; v<voxels.length; v++)
				voxels[v] = byteBuffer.getInt();
			randomAccessFile.close();
			
			long loadTime = System.currentTimeMillis() - startTime;
			System.out.println("Complete ("+loadTime+"ms)");
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	 
	

}
