package sgen.image_processing;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ResizingImage {
	
	public static void main(String args[]) throws IOException
	{
		/*File[] seqimg=new File[4];
		File[] outputImg = new File[4];
		int i;
		
		for(i=0;i<4;i++)
		seqimg[i]= new File("C:/image/"+(i+1)+".png");
		
		for(i=0;i<4;i++)
		outputImg[i] = new File("C:/image/resized"+(i+1)+".png");*/
		
		File input_image=new File("C:/image/after.png");
		File output_image=new File("C:/image/resized_after.png");
		
		int scaledWidth=300;
		int scaledHeight=300;
		boolean preserveAlpha=true;
		BufferedImage resizedImage;
	/*	for(i=0;i<4;i++)
		{*/
		BufferedImage screen = getImage(input_image);
		
		BufferedImage bgrScreen = convertToType(screen,BufferedImage.TYPE_3BYTE_BGR);
		
		resizedImage=createResizedCopy(bgrScreen,scaledWidth,scaledHeight,preserveAlpha);
		ImageIO.write(resizedImage, "png", output_image);
		/*}
	*/
	}
	  public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {

	        BufferedImage image;

	        // if the source image is already the target type, return the source image
	        if (sourceImage.getType() == targetType) {
	            image = sourceImage;
	        }
	        // otherwise create a new image of the target type and draw the new image
	        else {
	            image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
	            image.getGraphics().drawImage(sourceImage, 0, 0, null);
	        }

	        return image;

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
	private static BufferedImage createResizedCopy(Image originalImage, 
			int scaledWidth, int scaledHeight, 
			boolean preserveAlpha)
	{
		System.out.println("resizing...");
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g2d = scaledBI.createGraphics();
		if (preserveAlpha) {
			g2d.setComposite(AlphaComposite.Src);
		}
		g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
		g2d.dispose();
		return scaledBI;
	}
	
}