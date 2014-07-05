package sgen.xuggle.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jhlabs.image.GlowFilter;

public class Filter1 {

public static void main(String args[]) throws IOException{
	File input_image = new File("C:/image/jun.png");
	File output_image = new File("C:/image/after.png");
	BufferedImage before = getImage(input_image);
	
	BufferedImage after = null;

	GlowFilter g = new GlowFilter();
	g.setAmount(0.03f);
	
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
