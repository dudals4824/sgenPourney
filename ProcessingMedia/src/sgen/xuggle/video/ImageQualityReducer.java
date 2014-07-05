package sgen.xuggle.video;

import java.io.*;  
import java.util.Iterator;  
import javax.imageio.*;  
import javax.imageio.stream.*;  
import java.awt.image.*;  
  
public class ImageQualityReducer {  
    public static void main(String[] args) throws Exception {  
       File input_file = new File("C:/image/after.jpeg");
       File output_file = new File("C:/image/low_qual.jpeg");
        float quality =0.2f;
  
        Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");  
  
        ImageWriter writer = (ImageWriter)iter.next();  
  
        ImageWriteParam iwp = writer.getDefaultWriteParam();  
  
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);  
  
        iwp.setCompressionQuality(quality);  
  
          
        FileImageOutputStream output = new FileImageOutputStream(output_file);  
        writer.setOutput(output);  
  
        FileInputStream inputStream = new FileInputStream(input_file);  
        BufferedImage originalImage = ImageIO.read(inputStream);  
  
        IIOImage image = new IIOImage(originalImage, null, null);  
        writer.write(null, image, iwp);  
        writer.dispose();  
  
        System.out.println("DONE");  
    }  
}  