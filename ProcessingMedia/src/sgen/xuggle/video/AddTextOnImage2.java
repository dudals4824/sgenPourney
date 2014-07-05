package sgen.xuggle.video;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;





import javax.imageio.ImageIO;

public class AddTextOnImage2 {
	public static void main(String[] args) throws Exception {
	    final BufferedImage image = ImageIO.read(new File("C:/image/resized1.png"));
	    
	    Graphics g = image.getGraphics();
	    FontMetrics fm= g.getFontMetrics();
	    g.setFont(new Font("NANUMPEN",1,15));
	    g.setColor(Color.BLACK);
	    g.drawString("안녕, 친구야 여행을 잘갔다왔니 너는 지금 뭐해 자니 밖이야 뜬금없는 문잘 보내 보지만",0,image.getHeight()*4/5);
	    g.dispose();

	    ImageIO.write(image, "png", new File("C:/image/test.png"));
	}
}