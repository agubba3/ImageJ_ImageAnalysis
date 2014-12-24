package imageIO;

import ij.ImagePlus;
import ij.plugin.GaussianBlur3D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.imageio.ImageIO;

public class VarianceFunctions {
	public static double variance(ImagePlus img, int x, int y,int size) {
		int[] colorArray = img.getPixel(x, y);

		int redValue = colorArray[0];
		int greenValue = colorArray[1];
		int blueValue = colorArray[2];
		int rgb = redValue;
		rgb = (rgb << 8) + greenValue;
		rgb = (rgb << 8) + blueValue;
		if (x - size >= 0 && y - size >= 0 && y + size < img.getHeight()
				&& x + size < img.getWidth()) {
			double tot = 0;
			for (int i = 0; i < size*2; i++) {
				for (int j = 0; j < size*2; j++) {
					int[] ca = img.getPixel(x - size + i, y - size + i);
					int rv = ca[0];
					int gv = ca[1];
					int bv = ca[2];
					int rgb2 = rv;
					rgb2 = (rgb2 << 8) + gv;
					rgb2 = (rgb2 << 8) + bv;
					tot += rgb;
				}
			}
			double avg = tot / (size*size*4);
			double var = 0;
			for (int i = 0; i < size*2; i++) {
				for (int j = 0; j < size*2; j++) {
					int[] ca = img.getPixel(x - size + i, y - size + i);
					int rv = ca[0];
					int gv = ca[1];
					int bv = ca[2];
					int rgb2 = rv;
					rgb2 = (rgb2 << 8) + gv;
					rgb2 = (rgb2 << 8) + bv;
					var += (rgb2 - avg) * (rgb2 - avg) / (size*size*4-1);
				}
			}
			return var;	
		} else {
			throw new IllegalArgumentException();
		}
	}
	public static void main(String[] args){
		 ImagePlus img = new ImagePlus("mouse2.jpg");
		 //mouse2 - brighter image
		 //GaussianBlur3D.blur(img, 10, 10, 10);
		 //double varianceterrain = variance(img,2700,1700,20);
		 //System.out.println(varianceterrain);
		 double varianceterrain;
		 File file = new File("output.txt");
		 PrintWriter pw = null;
		 try {
			 pw = new PrintWriter(file);
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 }

		/* NumberFormat nf = NumberFormat.getInstance();
		 nf.setMaximumFractionDigits(2);            
		 nf.setGroupingUsed(false);*/
		 for(int row=4; row<748;row+=4){
			 for(int colum=4; colum<560;colum+=4){
				 varianceterrain = variance(img,row,colum,2);
				 //varianceterraintruncated = Math.floor(varianceterrain * 100) / 100;
				 pw.print(varianceterrain + " ");
				 
			 }
			 
			 pw.println();
		 }
		 

		 

		 pw.close();

		/* 
		 BufferedImage a = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_INT_RGB);
		 for (int y = 0; y < img.getHeight(); y++) {
             for (int x = 0; x < img.getWidth(); x++) {

                 int [] colorArray = img.getPixel(x,y);

                 int redValue = colorArray[0];
                 int greenValue = colorArray[1];
                 int blueValue = colorArray[2];
                 int rgb = redValue;
                 rgb = (rgb << 8) + greenValue;
                 rgb = (rgb << 8) + blueValue;
                 a.setRGB(x, y, rgb);
             }
		 }
		 File outputfile = new File("out.png");
		 try {
			ImageIO.write(a,"png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
