package sgen.xuggle.video;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jhlabs.image.ExposureFilter;
import com.jhlabs.image.GlowFilter;
import com.jhlabs.image.GradientFilter;
import com.jhlabs.image.GrayFilter;
import com.jhlabs.image.MinimumFilter;
import com.jhlabs.image.PointillizeFilter;

public class ExposureFilter_Application{

public static void main(String args[]) throws IOException{
	File input_image = new File("C:/image/1.png");
	File output_image = new File("C:/image/after6.png");
	BufferedImage before = getImage(input_image);
	
	BufferedImage after = null;
	
/*GlowFilter g = new GlowFilter();*/
ExposureFilter g = new ExposureFilter();
/*PointillizeFilter g = new PointillizeFilter();*/
/*g.setAmount(0.03f);*/
/*	g.setScale(10f);
	g.setRandomness(0.1f);
	g.setAmount(0.1f);
	g.
*/
g.setExposure(3f);
after = g.filter(before,after);

	ImageIO.write(after, "png", output_image);
}
private static BufferedImage getImage(File file) {
	try {
		BufferedImage image = ImageIO.read(file);
		System.out.println(image.toString());
		return image;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
}
}
