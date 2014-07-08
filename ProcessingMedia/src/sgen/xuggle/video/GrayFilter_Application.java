package sgen.xuggle.video;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.jhlabs.image.GlowFilter;
import com.jhlabs.image.GrayFilter;
import com.jhlabs.image.PointillizeFilter;

public class GrayFilter_Application{

public static void main(String args[]) throws IOException{
	File input_image = new File("C:/image/2.png");
	File output_image = new File("C:/image/after2.png");
	BufferedImage before = getImage(input_image);
	
	BufferedImage after = null;
	
/*GlowFilter g = new GlowFilter();*/
GrayFilter g = new GrayFilter();
/*PointillizeFilter g = new PointillizeFilter();*/
/*g.setAmount(0.03f);*/
/*	g.setScale(10f);
	g.setRandomness(0.1f);
	g.setAmount(0.1f);
	g.
*/
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
