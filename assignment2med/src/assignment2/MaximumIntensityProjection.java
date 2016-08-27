package assignment2;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class MaximumIntensityProjection extends JFrame{
	
	public MaximumIntensityProjection() {
		Volume volume = new Volume("Stanford-CT_Head.vol");		

		BufferedImage bufferedImage = volume.getSliceAsBufferedImage();
			
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		JLabel label = new JLabel(new ImageIcon(bufferedImage));
		frame.add(label);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);		
	
	}
	
	public static void main(String args[]) {

		
		new MaximumIntensityProjection();
		
	}
}

