package imageIO;

import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.ImageReader;
import ij.plugin.GaussianBlur3D;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

public class mainShadow {
	public static void main(String[] args){
		 String pathname = "marylandgenassem.jpg";
		 ImagePlus img = new ImagePlus(pathname);
		 int dot = pathname.indexOf(".");
		 String outputString = pathname.substring(0,dot) + "-output" +
		 		".png";
		 GaussianBlur3D.blur(img, 3, 3, 3);
		 double threshold = 1.5;
		 //double[][] varianceArray = new double[img.getWidth()][img.getHeight()];
		
         BufferedImage a = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_INT_RGB);
         //find cyan pixels
         double total = 0;
         for (int y = 0; y < img.getHeight(); y++) {
             for (int x = 0; x < img.getWidth(); x++) {
                 int [] colorArray = img.getPixel(x,y);
                 int redValue = colorArray[0];
                 int greenValue = colorArray[1];
                 int blueValue = colorArray[2];
                 double rgb = (redValue + greenValue + blueValue) / 3.0;
   			     total += rgb;
             }
         }
         total /= (img.getHeight() * img.getWidth());
         for (int y = 0; y < img.getHeight(); y++) {
             for (int x = 0; x < img.getWidth(); x++) {
            	 int [] colorArray = img.getPixel(x,y);

                 int redValue = colorArray[0];
                 int greenValue = colorArray[1];
                 int blueValue = colorArray[2];
                 double rgb = Math.sqrt((redValue*redValue + greenValue*greenValue + blueValue*blueValue)/3.0);
                 if (rgb < total/threshold){
                	 a.setRGB(x, y, 0);
                 } else {
                	 a.setRGB(x, y, Integer.MAX_VALUE);
                 }
             }
         }
         
         try {
        	File outputfile = new File(outputString);
        	ImageIO.write(a, "png", outputfile);
         } catch (IOException e) {

        }
         //edgedetect(img);
	}

	
	public static void edgedetect(ImagePlus img) {
		 CannyEdgeDetector detector = new CannyEdgeDetector();
		 detector.setLowThreshold(0.7f);
		 detector.setHighThreshold(7f);
		 //apply it to an image
		 BufferedImage frame = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		 for (int y = 0; y < img.getHeight(); y++) {
             for (int x = 0; x < img.getWidth(); x++) {
            	 int [] colorArray = img.getPixel(x,y);

                 int redValue = colorArray[0];
                 int greenValue = colorArray[1];
                 int blueValue = colorArray[2];
                 int rgb = redValue;
                 rgb = (rgb << 8) + greenValue;
                 rgb = (rgb << 8) + blueValue;
            	 frame.setRGB(x, y, rgb);
             }
		 }
		 detector.setSourceImage(frame);
		 detector.process();
		 BufferedImage edges = detector.getEdgesImage();
		 File outputfile = new File("edges.png");
		 try {
			ImageIO.write(edges, "png", outputfile);
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }

	}
	
}
